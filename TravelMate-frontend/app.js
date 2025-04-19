App({
  onLaunch() {
    // 检查登录状态
    this.checkLoginStatus();
  },

  checkLoginStatus() {
    // 从本地存储获取用户信息
    const userId = wx.getStorageSync('userId');
    const userInfo = wx.getStorageSync('userInfo');
    console.log(userId);
    console.log(userInfo);
    if (userId && userInfo) {
      // 用户信息存在，验证登录状态是否有效
      wx.request({
        url: `http://113.44.75.241:8080/user/info?userID=${userId}`,
        method: 'GET',
        success: (res) => {
          if (res.data.code === 1) {
            // 登录状态有效，保存到全局数据
            this.globalData.userInfo = userInfo;
            wx.redirectTo({
              url: '/pages/itinerary/itinerary'
            });
          } else {
            // 登录状态失效，清除本地存储并跳转到登录页
            this.clearLoginInfo();
            this.redirectToLogin();
          }
        },
        fail: () => {
          // 请求失败，清除登录信息并跳转到登录页
          this.clearLoginInfo();
          this.redirectToLogin();
        }
      });
    } else {
      // 没有登录信息，跳转到登录页
      this.redirectToLogin();
    }
  },

  clearLoginInfo() {
    wx.removeStorageSync('userId');
    wx.removeStorageSync('userInfo');
    this.globalData.userInfo = null;
  },

  redirectToLogin() {
    // 需要先在 app.json 中添加登录页面路径
    wx.redirectTo({
      url: '/pages/login/login'
    });
  },

  globalData: {
    userInfo: null
  }
});