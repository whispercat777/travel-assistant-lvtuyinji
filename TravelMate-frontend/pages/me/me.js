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
    } else {
      wx.redirectTo({
        url: '/pages/login/login'
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
    // 跳转到编辑页面
    wx.navigateTo({
      url: '/pages/me/edit/edit'
    });
  }
});