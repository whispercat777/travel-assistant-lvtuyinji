App({
  onLaunch() {
    // 检查登录状态
    this.checkLoginStatus();
    wx.switchTab({
      url: '/pages/itinerary/itinerary'
    });
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
        url: `http://139.224.36.136:8080/user/info?userID=${userId}`,
        method: 'GET',
        success: (res) => {
          if (res.data.code === 1) {
            // 登录状态有效，保存到全局数据
            this.globalData.userInfo = userInfo;
          } else {
            // 登录状态失效，清除本地存储并跳转到登录页
            this.clearLoginInfo();
          }
        },
        fail: () => {
          // 请求失败，清除登录信息并跳转到登录页
          this.clearLoginInfo();
        }
      });
    }
  },

  clearLoginInfo() {
    wx.removeStorageSync('userId');
    wx.removeStorageSync('userInfo');
    this.globalData.userInfo = null;
  },

  globalData: {
    userInfo: null
  }
});