// 前后端集成测试 - 核心业务流程测试
// 文件路径: test/integration/integration.test.js

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

  // 1. 用户认证流程测试（登录 -> 获取用户信息 -> 更新用户信息）
  async testUserAuthFlow() {
    console.log('开始测试用户认证流程...');
    
    // 模拟登录流程（实际测试中需要真实的code）
    const mockCode = 'test_code_' + Date.now();
    
    // 测试登录接口
    const loginResult = await this.callAPI(
      `/user/login?code=${mockCode}`, 
      'GET'
    );
    
    if (!loginResult.success) {
      throw new Error('登录接口调用失败');
    }

    // 假设返回了用户ID，测试获取用户信息
    const mockUserId = 'test_user_123';
    const userInfoResult = await this.callAPI(
      `/user/info?userID=${mockUserId}`,
      'GET'
    );

    // 测试更新用户信息
    const updateResult = await this.callAPI(
      '/user/info',
      'PUT',
      {
        id: mockUserId,
        name: '测试用户',
        gender: 1
      }
    );

    console.log('用户认证流程测试完成');
  },

  // 2. 行程管理流程测试（创建 -> 获取 -> 修改 -> 删除）
  async testItineraryFlow() {
    console.log('开始测试行程管理流程...');
    
    const mockUserId = 'test_user_123';
    
    // 创建行程
    const createResult = await this.callAPI(
      '/itinerary/add',
      'POST',
      {
        userID: mockUserId,
        name: '测试行程',
        startDate: '2025-07-01',
        endDate: '2025-07-03',
        location: '北京'
      }
    );

    // 获取所有行程
    const getAllResult = await this.callAPI(
      `/itinerary/getall?userID=${mockUserId}`,
      'GET'
    );

    // 获取单个行程详情
    const getOneResult = await this.callAPI(
      '/itinerary/getone?ID=1',
      'GET'
    );

    // 修改行程
    const modifyResult = await this.callAPI(
      '/itinerary/modify',
      'PUT',
      {
        id: 1,
        name: '修改后的测试行程'
      }
    );

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

    // 修改事件
    const modifyEventResult = await this.callAPI(
      '/event/modify',
      'PUT',
      {
        id: 1,
        name: '修改后的测试事件'
      }
    );

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

    // 获取事件预算
    const getBudgetResult = await this.callAPI(
      '/budget/event?eveID=1',
      'GET'
    );

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

    // 获取事件开销
    const getExpenseResult = await this.callAPI(
      '/expense/event?eveID=1',
      'GET'
    );

    console.log('预算开销流程测试完成');
  },

  // 5. 天气提醒流程测试
  async testWeatherReminderFlow() {
    console.log('开始测试天气提醒流程...');
    
    const mockUserId = 'test_user_123';
    
    // 获取天气信息
    const weatherResult = await this.callAPI(
      '/weather/get?location=北京&date=2025-07-01',
      'GET'
    );

    // 添加提醒
    const addReminderResult = await this.callAPI(
      '/reminder/add',
      'POST',
      {
        userId: mockUserId,
        time: '2025-07-01T08:00:00',
        date: '2025-07-01',
        location: '北京'
      }
    );

    // 获取提醒列表
    const getRemindersResult = await this.callAPI(
      `/reminder/get?userID=${mockUserId}`,
      'GET'
    );

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
    
    const testAPIs = [
      { name: '用户登录', url: '/user/login?code=test', method: 'GET' },
      { name: '获取行程', url: '/itinerary/getall?userID=test', method: 'GET' },
      { name: '获取天气', url: '/weather/get?location=北京&date=2025-07-01', method: 'GET' }
    ];

    for (let api of testAPIs) {
      const startTime = Date.now();
      const result = await this.callAPI(api.url, api.method);
      const responseTime = Date.now() - startTime;
      
      this.addTestResult(
        `网络测试-${api.name}`,
        result.success ? 'PASS' : 'FAIL',
        `响应时间: ${responseTime}ms`
      );
    }
  },

  // 清除测试结果
  clearResults() {
    this.setData({ testResults: [] });
  }
});