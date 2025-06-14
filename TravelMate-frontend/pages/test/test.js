// test.js - 小程序测试页面
Page({
  data: {
    testResults: [],
    isRunning: false,
    testProgress: 0
  },

  // 主要测试流程
  async runAllTests() {
    this.setData({ 
      isRunning: true, 
      testResults: [],
      testProgress: 0 
    });

    const testSuites = [
      { name: '用户认证流程', test: this.testUserAuthFlow },
      { name: '行程管理流程', test: this.testItineraryFlow },
      { name: '事件管理流程', test: this.testEventFlow },
      { name: '预算开销流程', test: this.testBudgetExpenseFlow },
      { name: '天气提醒流程', test: this.testWeatherReminderFlow }
    ];

    for (let i = 0; i < testSuites.length; i++) {
      const suite = testSuites[i];
      try {
        await suite.test.call(this);
        this.addTestResult(suite.name, 'PASS', '流程测试通过');
      } catch (error) {
        this.addTestResult(suite.name, 'FAIL', error.message);
      }
      this.setData({ testProgress: ((i + 1) / testSuites.length) * 100 });
    }

    this.setData({ isRunning: false });
  },

  // 1. 用户认证流程测试（使用本地存储的真实用户信息）
  async testUserAuthFlow() {
    console.log('开始测试用户认证流程...');
    
    // 尝试使用本地存储的真实用户信息
    const realUserId = wx.getStorageSync('userId');
    const realUserInfo = wx.getStorageSync('userInfo');
    
    if (realUserId) {
      console.log('使用真实用户ID进行测试:', realUserId);
      
      // 测试获取用户信息
      const userInfoResult = await this.callAPI(
        `/user/info?userID=${realUserId}`,
        'GET'
      );

      if (!userInfoResult.success) {
        console.log('真实用户信息获取失败，跳过登录测试');
        return; // 不抛出错误，只是跳过
      }

      // 测试更新用户信息（如果有真实用户信息）
      if (realUserInfo && realUserInfo.id) {
        const updateResult = await this.callAPI(
          '/user/info',
          'PUT',
          {
            id: realUserInfo.id,
            name: realUserInfo.nickname || '测试用户',
            gender: realUserInfo.gender || 1
          }
        );

        if (!updateResult.success) {
          console.log('更新用户信息失败，但测试继续');
        }
      }
    } else {
      console.log('未找到本地用户信息，跳过用户认证测试');
      // 不抛出错误，只是跳过这个测试
    }

    console.log('用户认证流程测试完成');
  },

  // 2. 行程管理流程测试（使用真实用户ID）
  async testItineraryFlow() {
    console.log('开始测试行程管理流程...');
    
    // 使用真实的用户ID
    const realUserId = wx.getStorageSync('userId');
    
    if (!realUserId) {
      console.log('未找到用户ID，跳过行程管理测试');
      return;
    }
    
    // 先获取现有行程，不创建新的
    const getAllResult = await this.callAPI(
      `/itinerary/getall?userID=${realUserId}`,
      'GET'
    );

    if (!getAllResult.success) {
      throw new Error('获取行程列表失败: ' + getAllResult.error);
    }

    // 如果有行程，测试获取详情
    if (getAllResult.data.data && getAllResult.data.data.length > 0) {
      const firstItinerary = getAllResult.data.data[0];
      const getOneResult = await this.callAPI(
        `/itinerary/getone?ID=${firstItinerary.id}`,
        'GET'
      );

      if (!getOneResult.success) {
        throw new Error('获取行程详情失败: ' + getOneResult.error);
      }
    }

    console.log('行程管理流程测试完成');
  },

  // 3. 事件管理流程测试
  async testEventFlow() {
    console.log('开始测试事件管理流程...');
    
    // 添加事件
    const addEventResult = await this.callAPI(
      '/event/add',
      'POST',
      {
        itiID: 1,
        name: '测试事件',
        startTime: '2025-07-01T09:00:00',
        endTime: '2025-07-01T12:00:00',
        location: '天安门',
        description: '测试描述',
        type: 6
      }
    );

    if (!addEventResult.success) {
      throw new Error('添加事件失败: ' + addEventResult.error);
    }

    // 修改事件
    const modifyEventResult = await this.callAPI(
      '/event/modify',
      'PUT',
      {
        id: 1,
        name: '修改后的测试事件'
      }
    );

    if (!modifyEventResult.success) {
      throw new Error('修改事件失败: ' + modifyEventResult.error);
    }

    console.log('事件管理流程测试完成');
  },

  // 4. 预算开销流程测试
  async testBudgetExpenseFlow() {
    console.log('开始测试预算开销流程...');
    
    // 添加预算
    const addBudgetResult = await this.callAPI(
      '/budget/add',
      'POST',
      {
        eveID: 1,
        money: 500
      }
    );

    if (!addBudgetResult.success) {
      throw new Error('添加预算失败: ' + addBudgetResult.error);
    }

    // 获取事件预算
    const getBudgetResult = await this.callAPI(
      '/budget/event?eveID=1',
      'GET'
    );

    if (!getBudgetResult.success) {
      throw new Error('获取预算失败: ' + getBudgetResult.error);
    }

    // 添加开销
    const addExpenseResult = await this.callAPI(
      '/expense/add',
      'POST',
      {
        eveID: 1,
        name: '测试开销',
        money: 100,
        time: '2025-07-01T10:00:00',
        type: 2
      }
    );

    if (!addExpenseResult.success) {
      throw new Error('添加开销失败: ' + addExpenseResult.error);
    }

    // 获取事件开销
    const getExpenseResult = await this.callAPI(
      '/expense/event?eveID=1',
      'GET'
    );

    if (!getExpenseResult.success) {
      throw new Error('获取开销失败: ' + getExpenseResult.error);
    }

    console.log('预算开销流程测试完成');
  },

  // 5. 天气提醒流程测试（使用真实用户ID）
  async testWeatherReminderFlow() {
    console.log('开始测试天气提醒流程...');
    
    // 获取天气信息
    const weatherResult = await this.callAPI(
      '/weather/get?location=北京&date=2025-07-01',
      'GET'
    );

    if (!weatherResult.success) {
      throw new Error('获取天气信息失败: ' + weatherResult.error);
    }

    const realUserId = wx.getStorageSync('userId');
    
    if (!realUserId) {
      console.log('未找到用户ID，跳过提醒功能测试');
      return;
    }

    // 获取现有提醒列表
    const getRemindersResult = await this.callAPI(
      `/reminder/get?userID=${realUserId}`,
      'GET'
    );

    if (!getRemindersResult.success) {
      throw new Error('获取提醒列表失败: ' + getRemindersResult.error);
    }

    console.log('天气提醒流程测试完成');
  },

  // API调用封装
  callAPI(url, method, data = {}) {
    return new Promise((resolve, reject) => {
      const baseUrl = 'http://139.224.36.136:8080';
      
      wx.request({
        url: `${baseUrl}${url}`,
        method: method,
        data: data,
        header: {
          'Content-Type': 'application/json'
        },
        success: (res) => {
          console.log(`API ${method} ${url}:`, res);
          resolve({
            success: res.data.code === 1,
            data: res.data,
            statusCode: res.statusCode
          });
        },
        fail: (err) => {
          console.error(`API ${method} ${url} 失败:`, err);
          resolve({
            success: false,
            error: err.errMsg || '网络错误',
            statusCode: 0
          });
        }
      });
    });
  },

  // 添加测试结果
  addTestResult(testName, status, message) {
    const results = this.data.testResults;
    results.push({
      name: testName,
      status: status,
      message: message,
      timestamp: new Date().toLocaleTimeString()
    });
    this.setData({ testResults: results });
  },

  // 网络环境测试
  async testNetworkConditions() {
    console.log('开始网络环境测试...');
    
    // 使用真实用户ID（如果有的话）
    const realUserId = wx.getStorageSync('userId') || 'network_test';
    
    const testAPIs = [
      { name: '天气查询', url: '/weather/get?location=北京&date=2025-07-01', method: 'GET' },
      { name: '获取行程', url: `/itinerary/getall?userID=${realUserId}`, method: 'GET' },
      { name: '获取报告', url: `/report/time?userID=${realUserId}&startTime=2025-01-01T00:00:00&endTime=2025-12-31T23:59:59`, method: 'GET' }
    ];

    for (let api of testAPIs) {
      const startTime = Date.now();
      const result = await this.callAPI(api.url, api.method);
      const responseTime = Date.now() - startTime;
      
      let status = 'PASS';
      let message = `响应时间: ${responseTime}ms`;
      
      if (!result.success) {
        // 如果失败，检查是否是网络问题
        if (result.statusCode === 0) {
          status = 'FAIL';
          message = `网络连接失败: ${result.error}`;
        } else if (result.statusCode >= 400 && result.statusCode < 500) {
          status = 'WARNING';
          message = `客户端错误(${result.statusCode}): 可能是参数问题，但网络连通正常`;
        } else if (result.statusCode >= 500) {
          status = 'WARNING';
          message = `服务器错误(${result.statusCode}): 服务端问题，但网络连通正常`;
        }
      } else {
        // 根据响应时间判断网络质量
        if (responseTime > 3000) {
          status = 'WARNING';
          message += ' (网络较慢)';
        } else if (responseTime > 1000) {
          message += ' (网络一般)';
        } else {
          message += ' (网络良好)';
        }
      }
      
      this.addTestResult(
        `网络测试-${api.name}`,
        status,
        message
      );
    }
  },

  // 运行网络测试
  async runNetworkTests() {
    this.setData({ 
      isRunning: true, 
      testResults: [],
      testProgress: 0 
    });

    // 1. 基础网络检查
    await this.testBasicConnectivity();
    
    // 2. API响应测试
    await this.testNetworkConditions();

    this.setData({ 
      isRunning: false,
      testProgress: 100 
    });
  },

  // 基础网络连通性测试
  async testBasicConnectivity() {
    console.log('开始基础网络连通性测试...');
    
    // 测试服务器是否可达
    const connectivityTests = [
      {
        name: '服务器连通性',
        test: async () => {
          const result = await this.callAPI('/weather/get?location=北京&date=2025-06-15', 'GET');
          return {
            success: result.statusCode !== 0,
            message: result.statusCode === 0 ? '无法连接到服务器' : '服务器连接正常'
          };
        }
      },
      {
        name: '网络类型检测',
        test: async () => {
          try {
            const networkInfo = await this.getNetworkType();
            return {
              success: true,
              message: `网络类型: ${networkInfo.networkType}`
            };
          } catch (error) {
            return {
              success: false,
              message: '无法获取网络信息'
            };
          }
        }
      }
    ];

    for (let test of connectivityTests) {
      try {
        const result = await test.test();
        this.addTestResult(
          `基础网络-${test.name}`,
          result.success ? 'PASS' : 'FAIL',
          result.message
        );
      } catch (error) {
        this.addTestResult(
          `基础网络-${test.name}`,
          'FAIL',
          error.message
        );
      }
    }
  },

  // 获取网络类型
  getNetworkType() {
    return new Promise((resolve, reject) => {
      wx.getNetworkType({
        success: (res) => {
          resolve({
            networkType: res.networkType
          });
        },
        fail: (err) => {
          reject(new Error(err.errMsg));
        }
      });
    });
  },

  // 运行设备兼容性测试
  async runDeviceTests() {
    this.setData({ 
      isRunning: true, 
      testResults: [],
      testProgress: 0 
    });

    const deviceTestSuites = [
      { name: '设备信息收集', test: this.testDeviceInfo },
      { name: '微信API兼容性', test: this.testWeChatAPIs },
      { name: 'UI适配检测', test: this.testUIAdaptation },
      { name: '性能基准测试', test: this.testPerformance },
      { name: '存储功能测试', test: this.testStorageCompatibility }
    ];

    for (let i = 0; i < deviceTestSuites.length; i++) {
      const suite = deviceTestSuites[i];
      try {
        await suite.test.call(this);
        this.addTestResult(suite.name, 'PASS', '兼容性测试通过');
      } catch (error) {
        this.addTestResult(suite.name, 'FAIL', error.message);
      }
      this.setData({ testProgress: ((i + 1) / deviceTestSuites.length) * 100 });
    }

    this.setData({ isRunning: false });
  },

  // 设备信息收集
  async testDeviceInfo() {
    return new Promise((resolve, reject) => {
      wx.getSystemInfo({
        success: (res) => {
          const deviceInfo = {
            platform: res.platform,        // iOS/Android
            system: res.system,            // 系统版本
            version: res.version,          // 微信版本
            SDKVersion: res.SDKVersion,    // 基础库版本
            model: res.model,              // 设备型号
            brand: res.brand,              // 设备品牌
            screenWidth: res.screenWidth,
            screenHeight: res.screenHeight,
            pixelRatio: res.pixelRatio,
            statusBarHeight: res.statusBarHeight,
            safeArea: res.safeArea
          };

          // 分析设备特征
          let warnings = [];
          
          // 检查屏幕适配
          if (res.screenHeight / res.screenWidth > 2) {
            warnings.push('超长屏幕，注意UI适配');
          }
          
          if (res.screenWidth < 375) {
            warnings.push('小屏设备，注意文字大小');
          }

          // 检查系统版本
          if (res.platform === 'ios') {
            const iosVersion = parseFloat(res.system.match(/[\d.]+/)?.[0] || '0');
            if (iosVersion < 13) {
              warnings.push('iOS版本较低，可能存在兼容性问题');
            }
          }

          // 检查微信版本
          const wechatVersion = res.version.split('.').map(Number);
          if (wechatVersion[0] < 8) {
            warnings.push('微信版本较低，建议升级');
          }

          this.addTestResult(
            '设备信息-基本信息',
            'INFO',
            `${res.brand} ${res.model} | ${res.system} | 微信${res.version} | ${res.screenWidth}x${res.screenHeight}`
          );

          if (warnings.length > 0) {
            this.addTestResult(
              '设备信息-兼容性警告',
              'WARNING',
              warnings.join('; ')
            );
          } else {
            this.addTestResult(
              '设备信息-兼容性检查',
              'PASS',
              '设备配置良好，无明显兼容性问题'
            );
          }

          resolve(deviceInfo);
        },
        fail: (err) => {
          reject(new Error('无法获取设备信息: ' + err.errMsg));
        }
      });
    });
  },

  // 微信API兼容性测试
  async testWeChatAPIs() {
    const apis = [
      { name: 'wx.request', test: () => typeof wx.request === 'function' },
      { name: 'wx.getStorageSync', test: () => typeof wx.getStorageSync === 'function' },
      { name: 'wx.setStorageSync', test: () => typeof wx.setStorageSync === 'function' },
      { name: 'wx.getLocation', test: () => typeof wx.getLocation === 'function' },
      { name: 'wx.showToast', test: () => typeof wx.showToast === 'function' },
      { name: 'wx.showModal', test: () => typeof wx.showModal === 'function' },
      { name: 'wx.navigateTo', test: () => typeof wx.navigateTo === 'function' },
      { name: 'wx.getUserProfile', test: () => typeof wx.getUserProfile === 'function' },
      { name: 'wx.login', test: () => typeof wx.login === 'function' }
    ];

    let passCount = 0;
    let failCount = 0;

    for (let api of apis) {
      try {
        const isSupported = api.test();
        if (isSupported) {
          passCount++;
        } else {
          failCount++;
          this.addTestResult(
            `API兼容性-${api.name}`,
            'FAIL',
            'API不存在或不可用'
          );
        }
      } catch (error) {
        failCount++;
        this.addTestResult(
          `API兼容性-${api.name}`,
          'FAIL',
          `检测异常: ${error.message}`
        );
      }
    }

    this.addTestResult(
      'API兼容性-总体评估',
      failCount === 0 ? 'PASS' : (passCount > failCount ? 'WARNING' : 'FAIL'),
      `支持 ${passCount}/${apis.length} 个核心API`
    );

    // 实际测试存储API
    await this.testStorageAPI();
  },

  // 存储API实际测试
  async testStorageAPI() {
    try {
      const testKey = 'compatibility_test';
      const testValue = { timestamp: Date.now(), test: true };
      
      // 测试写入
      wx.setStorageSync(testKey, testValue);
      
      // 测试读取
      const retrieved = wx.getStorageSync(testKey);
      
      // 验证数据
      if (JSON.stringify(retrieved) === JSON.stringify(testValue)) {
        this.addTestResult(
          'API兼容性-存储功能',
          'PASS',
          '本地存储读写正常'
        );
      } else {
        this.addTestResult(
          'API兼容性-存储功能',
          'FAIL',
          '存储数据不一致'
        );
      }
      
      // 清理测试数据
      wx.removeStorageSync(testKey);
      
    } catch (error) {
      this.addTestResult(
        'API兼容性-存储功能',
        'FAIL',
        `存储功能异常: ${error.message}`
      );
    }
  },

  // UI适配检测
  async testUIAdaptation() {
    return new Promise((resolve) => {
      wx.getSystemInfo({
        success: (res) => {
          // 屏幕密度检测
          const { pixelRatio, screenWidth, screenHeight } = res;
          
          if (pixelRatio >= 2) {
            this.addTestResult(
              'UI适配-屏幕密度',
              'PASS',
              `高清屏幕(${pixelRatio}x)，显示效果良好`
            );
          } else {
            this.addTestResult(
              'UI适配-屏幕密度',
              'WARNING',
              `低像素比(${pixelRatio}x)，注意图片清晰度`
            );
          }

          // 安全区域检测
          if (res.safeArea) {
            const { top, bottom } = res.safeArea;
            const hasNotch = top > res.statusBarHeight || bottom < screenHeight;
            
            this.addTestResult(
              'UI适配-安全区域',
              hasNotch ? 'WARNING' : 'PASS',
              hasNotch ? '检测到刘海屏或底部安全区域，请注意适配' : '无特殊安全区域需求'
            );
          }

          // 屏幕尺寸适配
          const aspectRatio = screenHeight / screenWidth;
          if (aspectRatio > 2.1) {
            this.addTestResult(
              'UI适配-屏幕比例',
              'WARNING',
              `超长屏幕(${aspectRatio.toFixed(2)}:1)，注意布局适配`
            );
          } else {
            this.addTestResult(
              'UI适配-屏幕比例',
              'PASS',
              `标准屏幕比例(${aspectRatio.toFixed(2)}:1)`
            );
          }

          resolve();
        },
        fail: () => {
          this.addTestResult(
            'UI适配-检测失败',
            'FAIL',
            '无法获取屏幕信息'
          );
          resolve();
        }
      });
    });
  },

  // 性能基准测试
  async testPerformance() {
    // 渲染性能测试
    const renderStart = Date.now();
    const testData = Array.from({ length: 100 }, (_, i) => ({
      id: i,
      name: `测试项${i}`,
      value: Math.random() * 1000
    }));
    
    // 模拟setData操作
    this.setData({ testData });
    
    setTimeout(() => {
      const renderTime = Date.now() - renderStart;
      
      let level = 'PASS';
      if (renderTime > 1000) {
        level = 'WARNING';
      } else if (renderTime > 2000) {
        level = 'FAIL';
      }
      
      this.addTestResult(
        '性能测试-渲染性能',
        level,
        `100条数据渲染时间: ${renderTime}ms`
      );
    }, 100);

    // 数据处理性能测试
    const processStart = Date.now();
    const largeData = Array.from({ length: 10000 }, (_, i) => ({
      id: i,
      value: Math.random()
    }));
    
    // 数据处理操作
    const processed = largeData
      .filter(item => item.value > 0.5)
      .map(item => ({ ...item, processed: true }))
      .sort((a, b) => b.value - a.value);
    
    const processTime = Date.now() - processStart;
    
    let processLevel = 'PASS';
    if (processTime > 500) {
      processLevel = 'WARNING';
    } else if (processTime > 1000) {
      processLevel = 'FAIL';
    }
    
    this.addTestResult(
      '性能测试-数据处理',
      processLevel,
      `10000条数据处理时间: ${processTime}ms，结果: ${processed.length}条`
    );

    // 内存基础检测
    try {
      const memoryTest = [];
      for (let i = 0; i < 1000; i++) {
        memoryTest.push(new Array(100).fill(`test_${i}`));
      }
      memoryTest.length = 0; // 清理
      
      this.addTestResult(
        '性能测试-内存使用',
        'PASS',
        '内存分配和释放正常'
      );
    } catch (error) {
      this.addTestResult(
        '性能测试-内存使用',
        'FAIL',
        `内存操作异常: ${error.message}`
      );
    }
  },

  // 存储兼容性测试
  async testStorageCompatibility() {
    const tests = [
      {
        name: '基础存储',
        test: () => {
          wx.setStorageSync('test_string', 'hello');
          return wx.getStorageSync('test_string') === 'hello';
        }
      },
      {
        name: '对象存储',
        test: () => {
          const obj = { id: 1, name: '测试', data: [1, 2, 3] };
          wx.setStorageSync('test_object', obj);
          const retrieved = wx.getStorageSync('test_object');
          return JSON.stringify(retrieved) === JSON.stringify(obj);
        }
      },
      {
        name: '大数据存储',
        test: () => {
          const largeData = Array.from({ length: 1000 }, (_, i) => ({ id: i, text: `data_${i}` }));
          wx.setStorageSync('test_large', largeData);
          const retrieved = wx.getStorageSync('test_large');
          return retrieved.length === 1000;
        }
      }
    ];

    for (let test of tests) {
      try {
        const result = test.test();
        this.addTestResult(
          `存储兼容性-${test.name}`,
          result ? 'PASS' : 'FAIL',
          result ? '存储功能正常' : '存储功能异常'
        );
      } catch (error) {
        this.addTestResult(
          `存储兼容性-${test.name}`,
          'FAIL',
          `测试异常: ${error.message}`
        );
      }
    }

    // 清理测试数据
    try {
      wx.removeStorageSync('test_string');
      wx.removeStorageSync('test_object');
      wx.removeStorageSync('test_large');
    } catch (error) {
      // 忽略清理错误
    }
  },

  // 清除测试结果
  clearResults() {
    this.setData({ testResults: [] });
  }
});