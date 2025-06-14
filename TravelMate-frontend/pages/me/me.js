Page({
  data: {
    userInfo: {
      id: '',
      nickname: '',
      gender: 0
    }
  },

  onLoad() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        userInfo: userInfo
      });
    }
  },

  onShow() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        userInfo: userInfo
      });
    }
  },

  handleEdit() {
    const userId = wx.getStorageSync('userId')
    if (!userId) {
      console.log('跳转到登录页面')
      wx.redirectTo({
        url: '/pages/login/login'
      })
      return
    }
    // 跳转到编辑页面
    wx.navigateTo({
      url: '/pages/me/edit/edit'
    });
  }
});