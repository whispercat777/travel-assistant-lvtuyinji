// test/pages/itinerary/showEvent/event.test.js
const path = require('path');
const config = require('../../../../test.config');
const setupMiniProgramTest = require('../../../../utils/setupMiniProgramTest');

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

// 模拟行程数据
const mockItineraryData = {
  id: 'itinerary123',
  name: '上海三日游',
  startDate: '2025-06-01',
  endDate: '2025-06-03',
  location: '上海',
  events: [
    {
      id: 'event1',
      name: '早餐',
      type: 2, // 餐饮
      location: '外滩早餐店',
      description: '品尝上海特色早点',
      startTime: '2025-06-01T08:00:00',
      endTime: '2025-06-01T09:00:00',
      formattedStartTime: '2025-06-01 08:00',
      formattedEndTime: '2025-06-01 09:00'
    },
    {
      id: 'event2',
      name: '参观外滩',
      type: 6, // 观光
      location: '上海外滩',
      description: '欣赏上海标志性景点',
      startTime: '2025-06-01T10:00:00',
      endTime: '2025-06-01T12:00:00',
      formattedStartTime: '2025-06-01 10:00',
      formattedEndTime: '2025-06-01 12:00'
    }
  ]
};

describe('行程事件页面测试', () => {
  beforeEach(async () => {
    try {
      console.log('准备测试环境...');
      
      // 设置模拟API
      await miniProgram.evaluate(() => {
        // 确保wx对象存在
        if (typeof global.wx === 'undefined') {
          global.wx = {};
        }
        
        // 模拟request API
        wx.request = function(options) {
          console.log('模拟请求:', options.url);
          
          // 模拟获取行程详情
          if (options.url.includes('/itinerary/getone')) {
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: {
                      id: 'itinerary123',
                      name: '上海三日游',
                      startDate: '2025-06-01',
                      endDate: '2025-06-03',
                      location: '上海',
                      events: [
                        {
                          id: 'event1',
                          name: '早餐',
                          type: 2, // 餐饮
                          location: '外滩早餐店',
                          description: '品尝上海特色早点',
                          startTime: '2025-06-01T08:00:00',
                          endTime: '2025-06-01T09:00:00'
                        },
                        {
                          id: 'event2',
                          name: '参观外滩',
                          type: 6, // 观光
                          location: '上海外滩',
                          description: '欣赏上海标志性景点',
                          startTime: '2025-06-01T10:00:00',
                          endTime: '2025-06-01T12:00:00'
                        }
                      ]
                    }
                  }
                });
              }
            }, 100);
          } 
          // 模拟删除事件
          else if (options.url.includes('/event/delete')) {
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: { code: 1, data: { success: true } }
                });
              }
            }, 100);
          }
        };
        
        // 模拟导航API
        wx.navigateTo = function(options) {
          console.log('模拟导航:', options.url);
          
          // 记录导航调用
          if (!wx.navigateCalls) {
            wx.navigateCalls = [];
          }
          wx.navigateCalls.push(options.url);
          
          if (options.success) {
            options.success();
          }
        };
        
        // 模拟显示Toast
        wx.showToast = function(options) {
          console.log('模拟Toast:', options.title);
          
          // 记录Toast调用
          if (!wx.toastCalls) {
            wx.toastCalls = [];
          }
          wx.toastCalls.push(options);
          
          if (options.complete) {
            setTimeout(options.complete, options.duration || 1500);
          }
        };
        
        // 模拟显示Modal
        wx.showModal = function(options) {
          console.log('模拟Modal:', options.title);
          
          // 记录Modal调用
          if (!wx.modalCalls) {
            wx.modalCalls = [];
          }
          wx.modalCalls.push(options);
          
          // 默认用户点击确认
          setTimeout(() => {
            if (options.success) {
              options.success({ confirm: true });
            }
          }, 100);
        };
        
        // 模拟设置导航栏标题
        wx.setNavigationBarTitle = function(options) {
          console.log('设置导航栏标题:', options.title);
        };
        
        console.log('✓ 已设置API模拟');
        return true;
      });
      
      // 打开行程事件页面
      console.log('打开行程事件页面...');
      page = await miniProgram.reLaunch('/pages/itinerary/showEvent/event?id=itinerary123');
      console.log('✓ 行程事件页面打开成功');
      
      // 等待页面加载和数据获取
      await page.waitFor(2000);
      
    } catch (error) {
      console.error('beforeEach错误:', error);
      throw error;
    }
  });
  
  // 测试1: 页面是否正确加载行程数据
  test('应正确加载并显示行程数据', async () => {
    try {
      console.log('测试1: 验证行程数据加载');
      
      // 获取页面数据
      const pageData = await page.data();
      console.log('页面数据:', JSON.stringify({
        itineraryId: pageData.itinerary?.id,
        itineraryName: pageData.itinerary?.name,
        eventCount: pageData.itinerary?.events?.length
      }, null, 2));
      
      // 验证行程数据
      expect(pageData.itinerary).not.toBeNull();
      expect(pageData.itinerary.id).toBe('itinerary123');
      expect(pageData.itinerary.name).toBe('上海三日游');
      expect(pageData.itinerary.location).toBe('上海');
      
      // 验证事件数据
      expect(pageData.itinerary.events.length).toBe(2);
      expect(pageData.itinerary.events[0].id).toBe('event1');
      expect(pageData.itinerary.events[0].name).toBe('早餐');
      
      // 验证格式化的时间字段
      expect(pageData.itinerary.events[0].formattedStartTime).toBe('2025-06-01 08:00');
      expect(pageData.itinerary.events[0].formattedEndTime).toBe('2025-06-01 09:00');
      
      console.log('✓ 行程数据加载正确');
    } catch (error) {
      console.error('测试失败:', error);
      throw error;
    }
  });
  
  // 测试2: 测试添加事件功能
  test('添加事件按钮应导航到正确页面', async () => {
    try {
      console.log('测试2: 测试添加事件功能');
      
      // 等待页面数据加载完成
      await miniProgram.evaluate(() => {
        const page = getCurrentPages()[0];
        if (page) {
          page.setData({
            itinerary: {
              id: 'itinerary123',
              name: '上海三日游',
              startDate: '2025-06-01',
              endDate: '2025-06-03',
              location: '上海',
              events: []
            }
          });
        }
        return true;
      });
      
      // 等待数据设置完成
      await page.waitFor(500);
      
      // 找到添加事件按钮
      const addBtn = await page.$('.add-btn');
      expect(addBtn).not.toBeNull();
      console.log('✓ 找到添加事件按钮');
      
      // 点击添加事件按钮
      await addBtn.tap();
      console.log('✓ 点击添加事件按钮');
      
      // 等待导航
      await page.waitFor(500);
      
      // 验证导航调用
      const navigateCalls = await miniProgram.evaluate(() => {
        return wx.navigateCalls || [];
      });
      
      console.log('导航调用:', navigateCalls);
      expect(navigateCalls.length).toBeGreaterThan(0);
      
      // 验证导航到编辑事件页面
      const lastNavigation = navigateCalls[navigateCalls.length - 1];
      expect(lastNavigation).toContain('/pages/itinerary/showEvent/editEvent/editEvent');
      expect(lastNavigation).toContain('itiID=itinerary123');
      
      console.log('✓ 添加事件功能测试通过');
    } catch (error) {
      console.error('测试失败:', error);
      throw error;
    }
  });
  
  // 测试3: 测试编辑事件功能
  test('编辑事件按钮应正确响应点击', async () => {
    try {
      console.log('测试3: 测试编辑事件功能');
      
      // 设置模拟数据
      await miniProgram.evaluate(() => {
        const page = getCurrentPages()[0];
        if (page) {
          page.setData({
            itinerary: {
              id: 'itinerary123',
              name: '上海三日游',
              startDate: '2025-06-01',
              endDate: '2025-06-03',
              location: '上海',
              events: [
                {
                  id: 'event1',
                  name: '早餐',
                  type: 2,
                  location: '外滩早餐店',
                  description: '品尝上海特色早点',
                  startTime: '2025-06-01T08:00:00',
                  endTime: '2025-06-01T09:00:00',
                  formattedStartTime: '2025-06-01 08:00',
                  formattedEndTime: '2025-06-01 09:00'
                }
              ]
            }
          });
        }
        
        // 清除之前的导航记录
        wx.navigateCalls = [];
        
        return true;
      });
      
      // 等待数据设置完成
      await page.waitFor(500);
      
      // 直接调用编辑事件处理方法
      await page.callMethod('handleEditEvent', {
        currentTarget: { dataset: { id: 'event1' } }
      });
      console.log('✓ 调用编辑事件处理方法');
      
      // 等待导航
      await page.waitFor(500);
      
      // 验证导航调用
      const navigateCalls = await miniProgram.evaluate(() => {
        return wx.navigateCalls || [];
      });
      
      console.log('导航调用:', navigateCalls);
      expect(navigateCalls.length).toBeGreaterThan(0);
      
      // 验证导航到编辑事件页面
      const lastNavigation = navigateCalls[navigateCalls.length - 1];
      expect(lastNavigation).toContain('/pages/itinerary/showEvent/editEvent/editEvent');
      expect(lastNavigation).toContain('id=event1');
      
      console.log('✓ 编辑事件功能测试通过');
    } catch (error) {
      console.error('测试失败:', error);
      throw error;
    }
  });
  
  // 测试4: 测试删除事件功能
  test('删除事件按钮应显示确认对话框并处理删除', async () => {
    try {
      console.log('测试4: 测试删除事件功能');
      
      // 设置模拟数据
      await miniProgram.evaluate(() => {
        const page = getCurrentPages()[0];
        if (page) {
          page.setData({
            itinerary: {
              id: 'itinerary123',
              name: '上海三日游',
              startDate: '2025-06-01',
              endDate: '2025-06-03',
              location: '上海',
              events: [
                {
                  id: 'event1',
                  name: '早餐',
                  type: 2,
                  location: '外滩早餐店',
                  description: '品尝上海特色早点',
                  startTime: '2025-06-01T08:00:00',
                  endTime: '2025-06-01T09:00:00',
                  formattedStartTime: '2025-06-01 08:00',
                  formattedEndTime: '2025-06-01 09:00'
                }
              ]
            }
          });
        }
        
        // 清除之前的记录
        wx.modalCalls = [];
        wx.toastCalls = [];
        
        return true;
      });
      
      // 等待数据设置完成
      await page.waitFor(500);
      
      // 直接调用删除事件处理方法
      await page.callMethod('handleDeleteEvent', {
        currentTarget: { dataset: { id: 'event1' } }
      });
      console.log('✓ 调用删除事件处理方法');
      
      // 等待模态框和请求处理
      await page.waitFor(1000);
      
      // 验证模态框调用
      const modalCalls = await miniProgram.evaluate(() => {
        return wx.modalCalls || [];
      });
      
      console.log('模态框调用:', modalCalls.length);
      expect(modalCalls.length).toBeGreaterThan(0);
      expect(modalCalls[0].title).toBe('确认删除');
      
      // 验证Toast调用
      const toastCalls = await miniProgram.evaluate(() => {
        return wx.toastCalls || [];
      });
      
      console.log('Toast调用:', toastCalls.length);
      expect(toastCalls.length).toBeGreaterThan(0);
      expect(toastCalls[0].title).toBe('删除成功');
      
      console.log('✓ 删除事件功能测试通过');
    } catch (error) {
      console.error('测试失败:', error);
      throw error;
    }
  });
  
  // 测试5: 测试预算开销功能
  test('预算开销按钮应导航到预算页面', async () => {
    try {
      console.log('测试5: 测试预算开销功能');
      
      // 设置模拟数据
      await miniProgram.evaluate(() => {
        const page = getCurrentPages()[0];
        if (page) {
          page.setData({
            itinerary: {
              id: 'itinerary123',
              name: '上海三日游',
              startDate: '2025-06-01',
              endDate: '2025-06-03',
              location: '上海',
              events: [
                {
                  id: 'event1',
                  name: '早餐',
                  type: 2,
                  location: '外滩早餐店',
                  description: '品尝上海特色早点',
                  startTime: '2025-06-01T08:00:00',
                  endTime: '2025-06-01T09:00:00',
                  formattedStartTime: '2025-06-01 08:00',
                  formattedEndTime: '2025-06-01 09:00'
                }
              ]
            }
          });
        }
        
        // 清除之前的导航记录
        wx.navigateCalls = [];
        
        return true;
      });
      
      // 等待数据设置完成
      await page.waitFor(500);
      
      // 直接调用预算开销处理方法
      await page.callMethod('handleShowBudget', {
        currentTarget: { dataset: { id: 'event1' } }
      });
      console.log('✓ 调用预算开销处理方法');
      
      // 等待导航
      await page.waitFor(500);
      
      // 验证导航调用
      const navigateCalls = await miniProgram.evaluate(() => {
        return wx.navigateCalls || [];
      });
      
      console.log('导航调用:', navigateCalls);
      expect(navigateCalls.length).toBeGreaterThan(0);
      
      // 验证导航到预算页面
      const lastNavigation = navigateCalls[navigateCalls.length - 1];
      expect(lastNavigation).toContain('/pages/itinerary/showEvent/budget/budget');
      expect(lastNavigation).toContain('id=event1');
      
      console.log('✓ 预算开销功能测试通过');
    } catch (error) {
      console.error('测试失败:', error);
      throw error;
    }
  });
  
  // 测试6: 测试智能推荐功能
  test('智能推荐按钮应导航到推荐页面', async () => {
    try {
      console.log('测试6: 测试智能推荐功能');
      
      // 设置模拟数据
      await miniProgram.evaluate(() => {
        const page = getCurrentPages()[0];
        if (page) {
          page.setData({
            itinerary: {
              id: 'itinerary123',
              name: '上海三日游',
              startDate: '2025-06-01',
              endDate: '2025-06-03',
              location: '上海',
              events: []
            }
          });
        }
        
        // 清除之前的导航记录
        wx.navigateCalls = [];
        
        return true;
      });
      
      // 等待数据设置完成
      await page.waitFor(500);
      
      // 找到智能推荐按钮
      const intelligenceBtn = await page.$('.intelligence-btn');
      expect(intelligenceBtn).not.toBeNull();
      console.log('✓ 找到智能推荐按钮');
      
      // 点击智能推荐按钮
      await intelligenceBtn.tap();
      console.log('✓ 点击智能推荐按钮');
      
      // 等待导航
      await page.waitFor(500);
      
      // 验证导航调用
      const navigateCalls = await miniProgram.evaluate(() => {
        return wx.navigateCalls || [];
      });
      
      console.log('导航调用:', navigateCalls);
      expect(navigateCalls.length).toBeGreaterThan(0);
      
      // 验证导航到智能推荐页面
      const lastNavigation = navigateCalls[navigateCalls.length - 1];
      expect(lastNavigation).toContain('/pages/itinerary/intelligence/intelligence');
      expect(lastNavigation).toContain('itiID=itinerary123');
      
      console.log('✓ 智能推荐功能测试通过');
    } catch (error) {
      console.error('测试失败:', error);
      throw error;
    }
  });
});