// test/pages/weather/weather.test.js
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

describe('天气页面测试', () => {
  beforeEach(async () => {
    try {
      console.log('准备打开天气页面...');
      
      // 设置全局wx对象和模拟函数
      await miniProgram.evaluate(() => {
        // 设置全局wx对象
        if (typeof global.wx === 'undefined') {
          global.wx = {};
        }
        
        // 模拟StorageSync
        wx.setStorageSync = function(key, value) {
          if (!this._storage) this._storage = {};
          this._storage[key] = value;
        };
        
        wx.getStorageSync = function(key) {
          if (!this._storage) this._storage = {};
          return this._storage[key] || '';
        };
        
        // 设置用户ID
        wx.setStorageSync('userId', 'testUserId');
        
        // 模拟getLocation
        wx.getLocationCalls = [];
        wx.getLocation = function(options) {
          wx.getLocationCalls.push(options);
          
          // 模拟成功获取位置
          setTimeout(() => {
            if (options.success) {
              options.success({
                longitude: 116.397128,
                latitude: 39.916527
              });
            }
          }, 100);
        };
        
        // 模拟getNetworkType
        wx.getNetworkType = function(options) {
          if (options.success) {
            options.success({
              networkType: 'wifi'
            });
          }
        };
        
        // 模拟request
        wx.requestCalls = [];
        wx.request = function(options) {
          wx.requestCalls.push({
            url: options.url,
            method: options.method,
            data: options.data
          });
          
          console.log('模拟请求:', options.url);
          
          // 根据URL返回不同的模拟数据
          if (options.url.includes('restapi.amap.com')) {
            // 高德地图逆地理编码API
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    status: '1',
                    regeocode: {
                      addressComponent: {
                        city: '北京市',
                        province: '北京市'
                      }
                    }
                  }
                });
              }
            }, 100);
          } else if (options.url.includes('/weather/get')) {
            // 天气API
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: {
                      location: '北京',
                      date: '2025-05-22',
                      maxTemperature: 25,
                      minTemperature: 15,
                      description: '晴',
                      wind: 3
                    }
                  }
                });
              }
            }, 100);
          } else if (options.url.includes('/reminder/get')) {
            // 获取提醒列表
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: {
                    code: 1,
                    data: [
                      {
                        id: 'reminder1',
                        location: '北京',
                        date: '2025-05-25',
                        time: '2025-05-25T08:00:00'
                      },
                      {
                        id: 'reminder2',
                        location: '上海',
                        date: '2025-05-26',
                        time: '2025-05-26T09:30:00'
                      }
                    ]
                  }
                });
              }
            }, 100);
          } else if (options.url.includes('/reminder/delete')) {
            // 删除提醒
            setTimeout(() => {
              if (options.success) {
                options.success({
                  data: { code: 1 }
                });
              }
            }, 100);
          }
        };
        
        // 模拟Toast
        wx.toastCalls = [];
        wx.showToast = function(options) {
          wx.toastCalls.push(options);
        };
        
        // 模拟Modal
        wx.modalCalls = [];
        wx.showModal = function(options) {
          wx.modalCalls.push(options);
          // 默认点击确认
          setTimeout(() => {
            if (options.success) {
              options.success({ confirm: true });
            }
          }, 100);
        };
        
        // 模拟navigateTo
        wx.navigateToCalls = [];
        wx.navigateTo = function(options) {
          wx.navigateToCalls.push(options);
        };
        
        // 模拟订阅消息
        wx.requestSubscribeMessageCalls = [];
        wx.requestSubscribeMessage = function(options) {
          wx.requestSubscribeMessageCalls.push(options);
          // 模拟用户同意订阅
          setTimeout(() => {
            if (options.success) {
              const result = {};
              options.tmplIds.forEach(id => {
                result[id] = 'accept';
              });
              options.success(result);
            }
          }, 100);
        };
        
        console.log('✓ 已设置模拟函数');
        return true;
      });
      
      // 打开天气页面
      page = await miniProgram.reLaunch('/pages/weather/weather');
      console.log('✓ 天气页面打开成功');
      
      // 等待页面完全加载
      await page.waitFor(5000);
      
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
        
        // 验证初始状态 - 如果后端运行，weatherData可能已有数据
        // 所以我们验证weatherData要么为null，要么有正确的结构
        if (pageData.weatherData !== null) {
          // 如果有天气数据，验证数据结构
          expect(pageData.weatherData).toHaveProperty('location');
          expect(pageData.weatherData).toHaveProperty('date');
          expect(pageData.weatherData).toHaveProperty('maxTemperature');
          expect(pageData.weatherData).toHaveProperty('minTemperature');
          expect(pageData.weatherData).toHaveProperty('description');
          expect(pageData.weatherData).toHaveProperty('wind');
          console.log('✓ 天气数据结构正确');
        } else {
          console.log('✓ 天气数据为空（正常初始状态）');
        }
        
        expect(pageData.loading).toBe(false);
        expect(pageData.inputLocation).toBe('');
        expect(pageData.hasSelectedDate).toBe(false);
        expect(pageData.userId).toBe('testUserId');
        expect(Array.isArray(pageData.reminders)).toBe(true);
        
        // 验证日期设置（今天和结束日期）
        expect(pageData.today).toBeTruthy();
        expect(pageData.inputDate).toBeTruthy();
        expect(pageData.endDate).toBeTruthy();
        
        console.log('✓ 页面初始状态正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('页面加载时应自动获取位置和天气数据', async () => {
      try {
        console.log('测试: 验证自动获取位置和天气');
        
        // 等待异步操作完成
        await page.waitFor(2000);
        
        // 检查位置请求
        const locationInfo = await miniProgram.evaluate(() => {
          return {
            locationCalls: wx.getLocationCalls || [],
            requests: wx.requestCalls || []
          };
        });
        
        console.log('位置和请求信息:', locationInfo);
        
        // 验证调用了获取位置
        expect(locationInfo.locationCalls.length).toBeGreaterThan(0);
        
        // 验证发起了高德地图和天气API请求
        const amapRequest = locationInfo.requests.find(req => req.url.includes('restapi.amap.com'));
        const weatherRequest = locationInfo.requests.find(req => req.url.includes('/weather/get'));
        const reminderRequest = locationInfo.requests.find(req => req.url.includes('/reminder/get'));
        
        expect(amapRequest).toBeTruthy();
        expect(weatherRequest).toBeTruthy();
        expect(reminderRequest).toBeTruthy();
        
        console.log('✓ 自动获取位置和天气数据正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集2: 搜索功能
  describe('搜索功能', () => {
    test('输入城市名应正确更新状态', async () => {
      try {
        console.log('测试: 输入城市名');
        
        // 模拟输入城市名
        await page.callMethod('onLocationInput', { detail: { value: '上海' } });
        console.log('✓ 已输入城市名');
        
        // 等待数据更新
        await page.waitFor(500);
        
        // 验证输入状态
        const pageData = await page.data();
        expect(pageData.inputLocation).toBe('上海');
        
        console.log('✓ 城市名输入处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('选择日期应正确更新状态', async () => {
      try {
        console.log('测试: 选择日期');
        
        // 模拟选择日期
        await page.callMethod('onDateChange', { detail: { value: '2025-05-25' } });
        console.log('✓ 已选择日期');
        
        // 等待数据更新
        await page.waitFor(500);
        
        // 验证日期状态
        const pageData = await page.data();
        expect(pageData.inputDate).toBe('2025-05-25');
        expect(pageData.hasSelectedDate).toBe(true);
        
        console.log('✓ 日期选择处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('搜索天气应验证输入并发起请求', async () => {
      try {
        console.log('测试: 搜索天气功能');
        
        // 先清空之前的请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
          wx.toastCalls = [];
        });
        
        // 测试空城市名的情况
        await page.callMethod('searchWeather');
        await page.waitFor(500);
        
        // 验证显示了提示
        let toastInfo = await miniProgram.evaluate(() => wx.toastCalls || []);
        expect(toastInfo.length).toBeGreaterThan(0);
        expect(toastInfo[toastInfo.length - 1].title).toBe('请输入城市名');
        
        // 设置城市名和日期
        await page.callMethod('onLocationInput', { detail: { value: '广州' } });
        await page.callMethod('onDateChange', { detail: { value: '2025-05-25' } });
        await page.waitFor(500);
        
        // 清空toast记录
        await miniProgram.evaluate(() => {
          wx.toastCalls = [];
        });
        
        // 进行搜索
        await page.callMethod('searchWeather');
        await page.waitFor(1000);
        
        // 验证发起了天气请求
        const requestInfo = await miniProgram.evaluate(() => wx.requestCalls || []);
        const weatherRequest = requestInfo.find(req => 
          req.url.includes('/weather/get') && 
          req.data.location === '广州' && 
          req.data.date === '2025-05-25'
        );
        
        expect(weatherRequest).toBeTruthy();
        
        console.log('✓ 搜索天气功能正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集3: 天气数据显示
  describe('天气数据显示', () => {
    test('获取天气数据成功时应正确更新显示', async () => {
      try {
        console.log('测试: 天气数据显示');
        
        // 清空当前的天气数据，模拟重新获取
        await miniProgram.evaluate(() => {
          const pages = getCurrentPages();
          const currentPage = pages[pages.length - 1];
          currentPage.setData({
            weatherData: null,
            loading: true
          });
        });
        
        // 等待状态更新
        await page.waitFor(500);
        
        // 直接调用获取天气数据方法
        await page.callMethod('getWeatherData', '北京', '2025-05-22');
        await page.waitFor(2000);  // 增加等待时间，确保API调用完成
        
        // 获取页面数据
        const pageData = await page.data();
        console.log('天气数据:', pageData.weatherData);
        
        // 验证天气数据已更新（可能来自真实API或模拟数据）
        expect(pageData.weatherData).toBeTruthy();
        expect(pageData.weatherData).toHaveProperty('location');
        expect(pageData.weatherData).toHaveProperty('date');
        expect(pageData.weatherData).toHaveProperty('maxTemperature');
        expect(pageData.weatherData).toHaveProperty('minTemperature');
        expect(pageData.weatherData).toHaveProperty('description');
        expect(pageData.weatherData).toHaveProperty('wind');
        
        // 验证加载状态已结束
        expect(pageData.loading).toBe(false);
        
        console.log('✓ 天气数据显示正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('天气API请求失败时应正确处理', async () => {
      try {
        console.log('测试: 天气API失败处理');
        
        // 模拟失败的请求
        await miniProgram.evaluate(() => {
          wx.request = function(options) {
            if (options.url.includes('/weather/get')) {
              if (options.fail) {
                options.fail({ errMsg: 'request:fail' });
              }
            }
          };
          wx.toastCalls = [];
        });
        
        // 调用获取天气数据
        await page.callMethod('getWeatherData', '北京', '2025-05-22');
        await page.waitFor(1000);
        
        // 验证错误处理
        const pageData = await page.data();
        expect(pageData.weatherData).toBeNull();
        expect(pageData.loading).toBe(false);
        
        // 验证显示了错误提示
        const toastInfo = await miniProgram.evaluate(() => wx.toastCalls || []);
        expect(toastInfo.length).toBeGreaterThan(0);
        expect(toastInfo[toastInfo.length - 1].title).toBe('网络请求失败');
        
        console.log('✓ 天气API失败处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集4: 提醒功能
  describe('提醒功能', () => {
    test('页面加载时应获取提醒列表', async () => {
      try {
        console.log('测试: 获取提醒列表');
        
        // 等待提醒数据加载
        await page.waitFor(2000);
        
        // 获取页面数据
        const pageData = await page.data();
        console.log('提醒数据:', pageData.reminders);
        
        // 验证提醒数据已加载
        expect(Array.isArray(pageData.reminders)).toBe(true);
        expect(pageData.reminders.length).toBeGreaterThan(0);
        
        // 验证提醒数据格式正确
        const firstReminder = pageData.reminders[0];
        expect(firstReminder.id).toBe('reminder1');
        expect(firstReminder.location).toBe('北京');
        expect(firstReminder.date).toBe('2025-05-25');
        expect(firstReminder.time).toBe('08:00');
        expect(firstReminder.reminderDate).toBe('2025-05-25');
        
        console.log('✓ 提醒列表获取正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('添加提醒应请求订阅权限并导航', async () => {
      try {
        console.log('测试: 添加提醒');
        
        // 清空导航记录
        await miniProgram.evaluate(() => {
          wx.requestSubscribeMessageCalls = [];
          wx.navigateToCalls = [];
        });
        
        // 点击添加提醒
        await page.callMethod('handleAddReminder');
        await page.waitFor(1000);
        
        // 验证请求了订阅消息权限
        const subscribeInfo = await miniProgram.evaluate(() => {
          return {
            subscribeCalls: wx.requestSubscribeMessageCalls || [],
            navigateCalls: wx.navigateToCalls || []
          };
        });
        
        expect(subscribeInfo.subscribeCalls.length).toBeGreaterThan(0);
        expect(subscribeInfo.subscribeCalls[0].tmplIds).toContain('QiqGKiYCCTTaCynpmMLiv8FPkNfohFnHqDC4SrHYG_A');
        
        // 验证导航到编辑页面
        expect(subscribeInfo.navigateCalls.length).toBeGreaterThan(0);
        expect(subscribeInfo.navigateCalls[0].url).toBe('/pages/weather/editReminder/editReminder');
        
        console.log('✓ 添加提醒处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('编辑提醒应正确传递数据并导航', async () => {
      try {
        console.log('测试: 编辑提醒');
        
        // 先确保有提醒数据
        await miniProgram.evaluate(() => {
          const pages = getCurrentPages();
          const currentPage = pages[pages.length - 1];
          currentPage.setData({
            reminders: [
              {
                id: 'reminder1',
                location: '北京',
                date: '2025-05-25',
                time: '08:00',
                reminderDate: '2025-05-25'
              }
            ]
          });
          wx.navigateToCalls = [];
        });
        
        // 点击编辑提醒
        await page.callMethod('handleEditReminder', { 
          currentTarget: { dataset: { id: 'reminder1' } } 
        });
        await page.waitFor(500);
        
        // 验证导航调用
        const navigateInfo = await miniProgram.evaluate(() => wx.navigateToCalls || []);
        expect(navigateInfo.length).toBeGreaterThan(0);
        
        const editUrl = navigateInfo[navigateInfo.length - 1].url;
        expect(editUrl).toContain('/pages/weather/editReminder/editReminder');
        expect(editUrl).toContain('reminderData=');
        
        console.log('✓ 编辑提醒处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('删除提醒应显示确认弹窗并发送删除请求', async () => {
      try {
        console.log('测试: 删除提醒');
        
        // 清空记录
        await miniProgram.evaluate(() => {
          wx.modalCalls = [];
          wx.requestCalls = [];
          wx.toastCalls = [];
        });
        
        // 点击删除提醒
        await page.callMethod('handleDeleteReminder', { 
          currentTarget: { dataset: { id: 'reminder1' } } 
        });
        await page.waitFor(1000);
        
        // 验证显示了确认弹窗
        const modalInfo = await miniProgram.evaluate(() => wx.modalCalls || []);
        expect(modalInfo.length).toBeGreaterThan(0);
        expect(modalInfo[modalInfo.length - 1].title).toBe('确认删除');
        expect(modalInfo[modalInfo.length - 1].content).toContain('确定要删除这条天气提醒吗？');
        
        // 验证发送了删除请求
        const requestInfo = await miniProgram.evaluate(() => wx.requestCalls || []);
        const deleteRequest = requestInfo.find(req => 
          req.url.includes('/reminder/delete') && 
          req.url.includes('id=reminder1')
        );
        expect(deleteRequest).toBeTruthy();
        expect(deleteRequest.method).toBe('DELETE');
        
        // 验证显示了成功提示
        const toastInfo = await miniProgram.evaluate(() => wx.toastCalls || []);
        expect(toastInfo.length).toBeGreaterThan(0);
        expect(toastInfo[toastInfo.length - 1].title).toBe('删除成功');
        
        console.log('✓ 删除提醒处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集5: 位置和网络处理
  describe('位置和网络处理', () => {
    test('位置权限被拒绝时应显示提示', async () => {
      try {
        console.log('测试: 位置权限被拒绝');
        
        // 模拟位置权限被拒绝
        await miniProgram.evaluate(() => {
          wx.getLocation = function(options) {
            if (options.fail) {
              options.fail({ errMsg: 'getLocation:fail auth deny' });
            }
          };
          wx.modalCalls = [];
        });
        
        // 调用获取位置和天气
        await page.callMethod('getLocationAndWeather');
        await page.waitFor(1000);
        
        // 验证显示了权限提示
        const modalInfo = await miniProgram.evaluate(() => wx.modalCalls || []);
        expect(modalInfo.length).toBeGreaterThan(0);
        expect(modalInfo[modalInfo.length - 1].content).toContain('位置权限被拒绝');
        
        console.log('✓ 位置权限拒绝处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });

    test('网络检查功能应正确工作', async () => {
      try {
        console.log('测试: 网络检查');
        
        // 模拟无网络状态
        await miniProgram.evaluate(() => {
          wx.getNetworkType = function(options) {
            if (options.success) {
              options.success({ networkType: 'none' });
            }
          };
          wx.toastCalls = [];
        });
        
        // 调用网络检查
        await page.callMethod('checkNetwork');
        await page.waitFor(500);
        
        // 验证显示了网络提示
        const toastInfo = await miniProgram.evaluate(() => wx.toastCalls || []);
        expect(toastInfo.length).toBeGreaterThan(0);
        expect(toastInfo[toastInfo.length - 1].title).toBe('请检查网络连接');
        
        console.log('✓ 网络检查处理正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });

  // 测试集6: 页面生命周期
  describe('页面生命周期', () => {
    test('onShow时应刷新提醒列表', async () => {
      try {
        console.log('测试: onShow刷新提醒');
        
        // 清空请求记录
        await miniProgram.evaluate(() => {
          wx.requestCalls = [];
        });
        
        // 调用onShow
        await page.callMethod('onShow');
        await page.waitFor(1000);
        
        // 验证发起了获取提醒的请求
        const requestInfo = await miniProgram.evaluate(() => wx.requestCalls || []);
        const reminderRequest = requestInfo.find(req => req.url.includes('/reminder/get'));
        expect(reminderRequest).toBeTruthy();
        
        console.log('✓ onShow刷新提醒正确');
      } catch (error) {
        console.error('测试失败:', error);
        throw error;
      }
    });
  });
});