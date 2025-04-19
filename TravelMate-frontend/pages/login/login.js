Page({
  onLoad() {
    // 检查当前是否已经登录
    const userId = wx.getStorageSync('userId');
    const userInfo = wx.getStorageSync('userInfo');
    
    if (userId && userInfo) {
      // 说明已登录，直接跳转到首页
      wx.switchTab({
        url: '/pages/itinerary/itinerary'
      });
    }
  },
  data: {
    step: 'initial', // initial（初始）, userInfo（填写用户信息）
    userInfo: {
      id: null,
      nickname: '',
      gender: null  // 0:其他, 1:男, 2:女
    },
    genderOptions: [
      { value: 0, label: '未知' },
      { value: 1, label: '男' },
      { value: 2, label: '女' },
    ]
  },

  // 第一步：获取用户id
  handleInitialLogin() {
    // 1. 先获取用户信息（必须直接在点击事件中触发）
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: (resWechatProfile) => {
        console.log('获取用户信息成功:', resWechatProfile);
        
        // 2. 获取登录code
        wx.login({
          success: (loginResult) => {
            console.log('获取到code:', loginResult.code);
      
            // 3. 请求后端获取id
            wx.request({
              url: `http://113.44.75.241:8080/user/login?code=${loginResult.code}`,
              method: 'GET',
              header: {
                'Content-Type': 'application/json'
              },
              success: (resGetId) => {
                console.log('登录响应:', resGetId);
                if (resGetId.data.code === 1) {
                  const userId = resGetId.data.data;
                  
                  // 4. 获取到id后，请求数据库中的用户信息
                  wx.request({
                    url: `http://113.44.75.241:8080/user/info?userID=${userId}`,
                    method: 'GET',
                    success: (infoRes) => {
                      if (infoRes.data.data.name !== null) {
                        // 数据库中有用户信息，直接使用
                        const userInfo = {
                          id: userId,
                          nickname: infoRes.data.data.name,
                          gender: infoRes.data.data.gender
                        };
                        
                        // 保存到本地存储
                        wx.setStorageSync('userId', userId);
                        wx.setStorageSync('userInfo', userInfo);
                        
                        // 保存到全局数据
                        const app = getApp();
                        app.globalData.userInfo = userInfo;
                        
                        // 直接跳转到首页
                        wx.switchTab({
                          url: '/pages/itinerary/itinerary'
                        });
                      } else {
                        // 数据库中没有用户信息，使用获取到的微信用户信息
                        console.log(resWechatProfile.userInfo)
                        this.setData({
                          'userInfo.id': userId,
                          'userInfo.nickname': resWechatProfile.userInfo.nickName,
                          'userInfo.gender': resWechatProfile.userInfo.gender,
                          step: 'userInfo'
                        });
                      }
                    },
                    fail: (err) => {
                      console.error('获取用户信息失败:', err);
                      wx.showToast({
                        title: '获取用户信息失败',
                        icon: 'none'
                      });
                    }
                  });
                } else {
                  wx.showToast({
                    title: '获取id失败',
                    icon: 'none'
                  });
                }
              },
              fail: (err) => {
                console.error('请求失败:', err);
                wx.showToast({
                  title: '登录失败',
                  icon: 'none'
                });
              }
            });
          },
          fail: (err) => {
            console.error('wx.login 失败:', err);
            wx.showToast({
              title: '登录失败',
              icon: 'none'
            });
          }
        });
      },
      fail: (err) => {
        console.log('用户拒绝授权或获取信息失败:', err);
        // 用户拒绝授权，清空用户信息
        this.setData({
          'userInfo.nickname': '',
          'userInfo.gender': null
        });
      }
    });
  },
  // 确认登录，提交用户信息
  handleConfirmLogin() {
    if (!this.data.userInfo.nickname) {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      });
      return;
    }

    // 更新用户完整信息
    wx.request({
      url: `http://113.44.75.241:8080/user/info`,
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
          // 保存用户信息到本地
          wx.setStorageSync('userId', this.data.userInfo.id);
          wx.setStorageSync('userInfo', this.data.userInfo);
          console.log(this.data.userInfo);
          console.log(res.data);
          // 登录成功，跳转
          wx.switchTab({
            url: '/pages/itinerary/itinerary'
          });
          console.log('didnt redirect')
        } else {
          wx.showToast({
            title: '保存用户信息失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('确认登录错误:', err);
        wx.showToast({
          title: '确认登录失败',
          icon: 'none'
        });
      }
    });
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
  }
});