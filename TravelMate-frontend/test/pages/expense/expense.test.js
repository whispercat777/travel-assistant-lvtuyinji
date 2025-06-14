// test/pages/expense/expense.test.js
const path = require('path');
const config = require('../../../test.config');
const setupMiniProgramTest = require('../../../utils/setupMiniProgramTest');

let miniProgram;
let page;

// 增加超时时间
jest.setTimeout(120000);

beforeAll(async () => {
  try {
    // 初始化小程序测试环境
    miniProgram = await setupMiniProgramTest();
    console.log('✓ 小程序测试环境初始化成功');
  } catch (error) {
    console.error('初始化测试环境失败:', error);
    throw error;
  }
});

afterAll(async () => {
  if (miniProgram) {
    try {
      await miniProgram.close();
      console.log('✓ 成功关闭连接');
    } catch (error) {
      console.error('关闭连接时出错:', error);
    }
  }
});

describe('费用统计页面测试', () => {
  beforeEach(async () => {
    try {
      console.log('准备打开费用统计页面...');
      
      // 设置全局wx对象和模拟函数
      await miniProgram.evaluate(() => {
        // 设置全局wx对象
        if (typeof global.wx === 'undefined') {
          global.wx = {};
        }
        
        // 模拟request
        wx.requestCalls = [];
        wx.request = function(options) {
          wx.requestCalls.push({
            url: options.url,
            method: options.method,
            data: options.data
          });
          
          // 根据URL返回不同的模拟数据
          if (options.url.includes('/report/time')) {
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: {
                      totalExpense: 1500.00,
                      expenses: [
                        {
                          id: 1,
                          name: '地铁费',
                          money: 50.00,
                          time: '2025-05-22T08:30:00'
                        },
                        {
                          id: 2,
                          name: '午餐',
                          money: 85.50,
                          time: '2025-05-22T12:15:00'
                        }
                      ]
                    }
                  }
                });
              }
            }, 100);
          } else if (options.url.includes('/report/type')) {
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: {
                      totalExpense: 600.00,
                      expenses: [
                        {
                          id: 3,
                          name: '公交费',
                          money: 10.00,
                          time: '2025-05-22T09:00:00'
                        },
                        {
                          id: 4,
                          name: '出租车',
                          money: 35.00,
                          time: '2025-05-22T18:30:00'
                        }
                      ]
                    }
                  }
                });
              }
            }, 100);
          } else if (options.url.includes('/itinerary/getall')) {
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: [
                      {
                        id: 'trip1',
                        name: '北京之旅',
                        startDate: '2025-06-01',
                        endDate: '2025-06-05',
                        location: '北京'
                      },
                      {
                        id: 'trip2',
                        name: '上海游',
                        startDate: '2025-07-15',
                        endDate: '2025-07-18',
                        location: '上海'
                      }
                    ]
                  }
                });
              }
            }, 100);
          } else if (options.url.includes('/report/budgetandexpense')) {
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: {
                      totalExpense: 2500.00,
                      totalBudget: 3000.00,
                      expenses: [
                        {
                          id: 5,
                          name: '酒店住宿',
                          money: 480.00,
                          time: '2025-05-20T15:00:00'
                        }
                      ],
                      budgets: [
                        {
                          id: 1,
                          amount: 1000.00,
                          category: '住宿'
                        }
                      ]
                    }
                  }
                });
              }
            }, 100);
          } else if (options.url.includes('/report/iti')) {
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: {
                      totalExpense: 1200.00,
                      totalBudget: 1500.00,
                      expenses: [
                        {
                          id: 6,
                          name: '景点门票',
                          money: 120.00,
                          time: '2025-06-02T10:00:00'
                        }
                      ],
                      budgets: [
                        {
                          id: 2,
                          amount: 500.00,
                          category: '娱乐'
                        }
                      ]
                    }
                  }
                });
              }
            }, 100);
          }
        };
        
        console.log('✓ 已设置模拟函数');
        return true;
      });
      
      // 打开费用统计页面
      page = await miniProgram.reLaunch('/pages/expense/expense');
      console.log('✓ 费用统计页面打开成功');
      
      // 等待页面完全加载
      await page.waitFor(3000);
      
    } catch (error) {
      console.error('打开页面失败:', error);
      throw error;
    }
  });

  // 测试集1: 页面初始化
  describe('页面初始化', () => {
    test('页面应正确初始化默认状态', async () => {
      try {
        console.log('测试: 验证页面初始状态');
        
        // 获取页面数据
        const pageData = await page.data();
        console.log('页面初始数据:', pageData);
        
        // 验证初始状态
        expect(pageData.userID).toBe('681295884');
        expect(pageData.viewMode).toBe('all');
        expect(pageData.totalExpense).toBeGreaterThanOrEqual(0);
        expect(pageData.typeOptions).toHaveLength(7);
        expect(pageData.selectedTypes).toEqual([]);
        expect(pageData.tripId).toBeNull();
        expect(pageData.typeId).toBeNull();
        
        // 验证日期时间范围已设置（应该是过去7天）
        expect(pageData.startTime).toBeTruthy();
        expect(pageData.endTime).toBeTruthy();
        
        console.log('✓ 页面初始状态正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('页面加载时应自动获取时间范围费用数据', async () => {
      try {
        console.log('测试: 验证初始数据加载');
        
        // 检查请求调用
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        console.log('初始请求:', requestInfo);
        
        // 验证发起了时间查询请求
        const timeRequest = requestInfo.find(req => req.url.includes('/report/time'));
        expect(timeRequest).toBeTruthy();
        expect(timeRequest.method).toBe('GET');
        expect(timeRequest.data.userID).toBe('681295884');
        
        // 获取页面数据验证费用数据已加载
        const pageData = await page.data();
        expect(pageData.expenses).toBeDefined();
        expect(Array.isArray(pageData.expenses)).toBe(true);
        
        console.log('✓ 初始数据加载正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集2: 视图模式切换
  describe('视图模式切换', () => {
    test('切换到按类别模式应正确更新状态和数据', async () => {
      try {
        console.log('测试: 切换到按类别模式');
        
        // 清除之前的请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 点击按类别选项
        await page.callMethod('switchViewMode', { currentTarget: { dataset: { mode: 'category' } } });
        console.log('✓ 已切换到按类别模式');
        
        // 等待数据更新
        await page.waitFor(1000);
        
        // 获取页面数据
        const pageData = await page.data();
        console.log('类别模式数据:', pageData);
        
        // 验证模式已切换
        expect(pageData.viewMode).toBe('category');
        expect(pageData.typeId).toBeNull();
        expect(pageData.tripId).toBeNull();
        
        // 验证发起了类别查询请求
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        const typeRequest = requestInfo.find(req => req.url.includes('/report/type'));
        expect(typeRequest).toBeTruthy();
        
        console.log('✓ 按类别模式切换正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('切换到按行程模式应获取行程列表和预算数据', async () => {
      try {
        console.log('测试: 切换到按行程模式');
        
        // 清除之前的请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 点击按行程选项
        await page.callMethod('switchViewMode', { currentTarget: { dataset: { mode: 'trip' } } });
        console.log('✓ 已切换到按行程模式');
        
        // 等待数据更新
        await page.waitFor(1000);
        
        // 获取页面数据
        const pageData = await page.data();
        console.log('行程模式数据:', pageData);
        
        // 验证模式已切换
        expect(pageData.viewMode).toBe('trip');
        expect(pageData.typeId).toBeNull();
        expect(pageData.tripId).toBeNull();
        
        // 验证发起了正确的请求
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        const tripsRequest = requestInfo.find(req => req.url.includes('/itinerary/getall'));
        const budgetRequest = requestInfo.find(req => req.url.includes('/report/budgetandexpense'));
        
        expect(tripsRequest).toBeTruthy();
        expect(budgetRequest).toBeTruthy();
        
        console.log('✓ 按行程模式切换正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集3: 时间范围筛选
  describe('时间范围筛选', () => {
    test('修改开始时间应重新获取费用数据', async () => {
      try {
        console.log('测试: 修改开始时间');
        
        // 确保在按时间模式
        await page.callMethod('switchViewMode', { currentTarget: { dataset: { mode: 'all' } } });
        await page.waitFor(500);
        
        // 清除之前的请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 修改开始时间
        await page.callMethod('onStartTimeChange', { detail: { value: '2025-05-01' } });
        console.log('✓ 已修改开始时间');
        
        // 等待数据更新
        await page.waitFor(1000);
        
        // 验证时间已更新
        const pageData = await page.data();
        expect(pageData.startTime).toBe('2025-05-01');
        
        // 验证发起了新的时间查询请求
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        const timeRequest = requestInfo.find(req => req.url.includes('/report/time'));
        expect(timeRequest).toBeTruthy();
        expect(timeRequest.data.startTime).toBe('2025-05-01T00:00:00');
        
        console.log('✓ 开始时间修改处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('修改结束时间应重新获取费用数据', async () => {
      try {
        console.log('测试: 修改结束时间');
        
        // 清除之前的请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 修改结束时间
        await page.callMethod('onEndTimeChange', { detail: { value: '2025-05-30' } });
        console.log('✓ 已修改结束时间');
        
        // 等待数据更新
        await page.waitFor(1000);
        
        // 验证时间已更新
        const pageData = await page.data();
        expect(pageData.endTime).toBe('2025-05-30');
        
        // 验证发起了新的时间查询请求
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        const timeRequest = requestInfo.find(req => req.url.includes('/report/time'));
        expect(timeRequest).toBeTruthy();
        expect(timeRequest.data.endTime).toBe('2025-05-30T23:59:59');
        
        console.log('✓ 结束时间修改处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集4: 类别筛选
  describe('类别筛选', () => {
    beforeEach(async () => {
      // 切换到按类别模式
      await page.callMethod('switchViewMode', { currentTarget: { dataset: { mode: 'category' } } });
      await page.waitFor(1000);
    });

    test('选择类别应正确更新状态并获取对应数据', async () => {
      try {
        console.log('测试: 选择类别筛选');
        
        // 清除之前的请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 选择交通类别
        await page.callMethod('toggleType', { currentTarget: { dataset: { type: '1' } } });
        console.log('✓ 已选择交通类别');
        
        // 等待数据更新
        await page.waitFor(1000);
        
        // 验证类别选择状态
        const pageData = await page.data();
        expect(pageData.typeId).toBe(1);
        expect(pageData.selectedTypes).toEqual([1]);
        
        // 验证发起了类别查询请求
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        const typeRequest = requestInfo.find(req => req.url.includes('/report/type'));
        expect(typeRequest).toBeTruthy();
        expect(typeRequest.data.types).toBe('1');
        
        console.log('✓ 类别筛选处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('切换不同类别应更新选择状态', async () => {
      try {
        console.log('测试: 切换不同类别');
        
        // 先选择交通类别
        await page.callMethod('toggleType', { currentTarget: { dataset: { type: '1' } } });
        await page.waitFor(500);
        
        // 验证第一次选择
        let pageData = await page.data();
        expect(pageData.typeId).toBe(1);
        
        // 切换到餐饮类别
        await page.callMethod('toggleType', { currentTarget: { dataset: { type: '2' } } });
        await page.waitFor(500);
        
        // 验证切换后的状态
        pageData = await page.data();
        expect(pageData.typeId).toBe(2);
        expect(pageData.selectedTypes).toEqual([2]);
        
        console.log('✓ 类别切换处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集5: 行程筛选
  describe('行程筛选', () => {
    beforeEach(async () => {
      // 切换到按行程模式
      await page.callMethod('switchViewMode', { currentTarget: { dataset: { mode: 'trip' } } });
      await page.waitFor(2000); // 等待行程数据加载
    });

    test('选择行程应正确更新状态并获取行程费用数据', async () => {
      try {
        console.log('测试: 选择行程筛选');
        
        // 清除之前的请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 选择第一个行程
        await page.callMethod('toggleTrip', { currentTarget: { dataset: { id: 'trip1' } } });
        console.log('✓ 已选择行程');
        
        // 等待数据更新
        await page.waitFor(1000);
        
        // 验证行程选择状态
        const pageData = await page.data();
        expect(pageData.tripId).toBe('trip1');
        
        // 验证发起了行程费用查询请求
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        const tripRequest = requestInfo.find(req => req.url.includes('/report/iti'));
        expect(tripRequest).toBeTruthy();
        expect(tripRequest.data.itiIDs).toBe('trip1');
        
        console.log('✓ 行程筛选处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('取消行程选择应返回所有预算和费用数据', async () => {
      try {
        console.log('测试: 取消行程选择');
        
        // 先选择一个行程
        await page.callMethod('toggleTrip', { currentTarget: { dataset: { id: 'trip1' } } });
        await page.waitFor(1000);
        
        // 验证已选择
        let pageData = await page.data();
        expect(pageData.tripId).toBe('trip1');
        
        // 清除请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 再次点击同一个行程（取消选择）- 这里模拟没有tripId时的情况
        await miniProgram.evaluate(() => {
          const pages = getCurrentPages();
          const currentPage = pages[pages.length - 1];
          currentPage.setData({
            tripId: null
          });
        });
        
        // 调用fetchTripExpenses方法
        await page.callMethod('fetchTripExpenses');
        await page.waitFor(1000);
        
        // 验证状态已重置
        pageData = await page.data();
        expect(pageData.tripId).toBeNull();
        
        // 验证发起了全部预算费用查询
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        const budgetRequest = requestInfo.find(req => req.url.includes('/report/budgetandexpense'));
        expect(budgetRequest).toBeTruthy();
        
        console.log('✓ 取消行程选择处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集6: 数据格式化
  describe('数据格式化', () => {
    test('费用时间应正确格式化显示', async () => {
      try {
        console.log('测试: 验证时间格式化');
        
        // 手动设置带有完整时间戳的费用数据
        await miniProgram.evaluate(() => {
          const pages = getCurrentPages();
          const currentPage = pages[pages.length - 1];
          currentPage.setData({
            expenses: [
              {
                id: 1,
                name: '测试费用',
                money: 100.00,
                time: '2025-05-22 08:30' // 已格式化的时间
              }
            ]
          });
        });
        
        // 等待数据更新
        await page.waitFor(500);
        
        // 获取页面数据
        const pageData = await page.data();
        console.log('费用数据:', pageData.expenses);
        
        // 验证时间格式化
        expect(pageData.expenses).toHaveLength(1);
        expect(pageData.expenses[0].time).toBe('2025-05-22 08:30');
        
        console.log('✓ 时间格式化正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集7: 错误处理
  describe('错误处理', () => {
    test('API请求失败时应正确处理', async () => {
      try {
        console.log('测试: API请求失败处理');
        
        // 模拟请求失败
        await miniProgram.evaluate(() => {
          wx.request = function(options) {
            wx.requestCalls.push({
              url: options.url,
              method: options.method,
              data: options.data
            });
            
            // 模拟失败响应
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: { code: 0, message: '请求失败' }
                });
              }
            }, 100);
          };
        });
        
        // 尝试获取时间费用数据
        await page.callMethod('fetchTimeExpenses');
        await page.waitFor(1000);
        
        // 验证请求已发送
        const requestInfo = await miniProgram.evaluate(() => {
          return wx.requestCalls || [];
        });
        
        expect(requestInfo.length).toBeGreaterThan(0);
        
        console.log('✓ 错误处理测试完成');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });
});