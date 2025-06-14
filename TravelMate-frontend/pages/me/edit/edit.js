Page({
  data: {
    userInfo: {
      id: null,
      nickname: '',
      gender: null
    },
    genderOptions: [
      { value: 0, label: '未知' },
      { value: 1, label: '男' },
      { value: 2, label: '女' },
    ]
  },

  onLoad() {
    // 获取本地存储的用户信息
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        userInfo: userInfo
      });
    }
  },

  // 更新昵称
  onNicknameInput(e) {
    this.setData({
      'userInfo.nickname': e.detail.value
    });
  },

  // 更新性别
  onGenderChange(e) {
    this.setData({
      'userInfo.gender': Number(e.detail.value)
    });
  },

  // 保存修改
  handleSave() {
    if (!this.data.userInfo.nickname) {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      });
      return;
    }

    // 更新用户信息到数据库
    wx.request({
      url: `http://139.224.36.136:8080/user/info`,
      method: 'PUT',
      header: {
        'Content-Type': 'application/json'
      },
      data: {
        id: this.data.userInfo.id,
        name: this.data.userInfo.nickname,
        gender: this.data.userInfo.gender
      },
      success: (res) => {
        if (res.data.code === 1) {
          // 更新本地存储的用户信息
          wx.setStorageSync('userInfo', this.data.userInfo);
          
          wx.showToast({
            title: '保存成功',
            icon: 'success'
          });

          // 延迟返回上一页
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        } else {
          wx.showToast({
            title: '保存失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('保存失败:', err);
        wx.showToast({
          title: '保存失败',
          icon: 'none'
        });
      }
    });
  }
});