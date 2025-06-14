// test/mocks/wx-api.js
const mockWxApi = {
  // 模拟存储数据
  storageData: {},
  
  // 模拟wx.setStorageSync
  setStorageSync: function(key, data) {
    this.storageData[key] = data;
  },
  
  // 模拟wx.getStorageSync
  getStorageSync: function(key) {
    return this.storageData[key] || null;
  },
  
  // 模拟网络请求
  requestResponses: {
    // 预设响应数据
    'http:/127.0.0.1:8080/user/login': { code: 1, data: 'user123' },
    'http://127.0.0.1:8080/user/info': { code: 1, data: { name: 'Test User', gender: 1 } }
  },
  
  request: function(options) {
    // 从URL获取基础路径
    const baseUrl = options.url.split('?')[0];
    
    // 检查是否有预设的响应
    if (this.requestResponses[baseUrl]) {
      setTimeout(() => {
        if (options.success) {
          options.success({ data: this.requestResponses[baseUrl] });
        }
      }, 100);
    } else {
      if (options.fail) {
        options.fail({ errMsg: 'request:fail mock' });
      }
    }
  },
  
  // 模拟导航函数
  navigateCalls: [],
  
  switchTab: function(options) {
    this.navigateCalls.push({ type: 'switchTab', url: options.url });
    if (options.success) options.success();
  },
  
  navigateTo: function(options) {
    this.navigateCalls.push({ type: 'navigateTo', url: options.url });
    if (options.success) options.success();
  },
  
  redirectTo: function(options) {
    this.navigateCalls.push({ type: 'redirectTo', url: options.url });
    if (options.success) options.success();
  },
  
  navigateBack: function() {
    this.navigateCalls.push({ type: 'navigateBack' });
  },
  
  // 模拟登录API
  login: function(options) {
    setTimeout(() => {
      if (options.success) {
        options.success({ code: 'mock_code_123' });
      }
    }, 100);
  },
  
  // 模拟获取用户信息
  getUserProfile: function(options) {
    setTimeout(() => {
      if (options.success) {
        options.success({
          userInfo: {
            nickName: 'Mock User',
            gender: 1,
            avatarUrl: 'mock_avatar.png'
          }
        });
      }
    }, 100);
  },
  
  // 模拟弹窗
  toastMessages: [],
  
  showToast: function(options) {
    this.toastMessages.push(options);
  },
  
  // 重置所有模拟数据
  reset: function() {
    this.storageData = {};
    this.navigateCalls = [];
    this.toastMessages = [];
    // 重置请求响应为默认值
    this.requestResponses = {
      'http://127.0.0.1:8080/user/login': { code: 1, data: 'user123' },
      'http://127.0.0.1:8080/user/info': { code: 1, data: { name: 'Test User', gender: 1 } }
    };
  }
};

module.exports = mockWxApi;