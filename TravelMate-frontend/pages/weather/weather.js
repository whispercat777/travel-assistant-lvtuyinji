Page({
  data: {
    weatherData: null,
    loading: false,
    today: '',
    inputLocation: '',
    inputDate: '',
    hasSelectedDate: false,
    reminders: [],
    userId: ''
  },

  onLoad() {
    // Set today's date
    const today = new Date();
    const todayStr = today.toISOString().split('T')[0];
    
    const endDate = new Date();
    endDate.setDate(today.getDate() + 3);
    const endDateStr = endDate.toISOString().split('T')[0];
    
    this.setData({ 
      today: todayStr,
      inputDate: todayStr,  // 设置默认日期为今天
      endDate: endDateStr 
    });
    
    // Request location permission and get weather
    this.getLocationAndWeather();
    const userId = wx.getStorageSync('userId');
    this.setData({ userId });
    this.fetchReminders();
  },

  // 添加一个新函数用于检查网络状态
  checkNetwork() {
    wx.getNetworkType({
      success: (res) => {
        console.log('Network type:', res.networkType);
        if (res.networkType === 'none') {
          wx.showToast({
            title: '请检查网络连接',
            icon: 'none'
          });
        }
      }
    });
  },

  getLocationAndWeather() {
    this.checkNetwork();
    wx.getLocation({
      type: 'wgs84',
      success: (res) => {
        // 使用高德地图逆地理编码API获取城市名称
        console.log('Location success:', res);
        const { longitude, latitude } = res;
        console.log(`Requesting AMap API with coordinates: ${longitude},${latitude}`);
        wx.request({
          url: 'https://restapi.amap.com/v3/geocode/regeo',
          data: {
            key: '00c7dd365854cad6083d8765ffbeab6f', // 高德地图API密钥
            location: `${res.longitude},${res.latitude}`,
          },
          success: (locationRes) => {
            console.log('AMap response:', locationRes);
            if (locationRes.data.status === '1' && locationRes.data.regeocode) {
              const addressComponent = locationRes.data.regeocode.addressComponent;
              let cityName;
              
              // 处理直辖市的情况
              if (Array.isArray(addressComponent.city) && addressComponent.city.length === 0) {
                cityName = addressComponent.province.replace(/省|市|自治区/g, '');
              } else {
                cityName = addressComponent.city.replace(/省|市|自治区/g, '');
              }
              
              console.log('Extracted city name:', cityName);
              this.getWeatherData(cityName, this.data.inputDate);
            } else {
              // 如果逆地理编码失败，使用默认城市
              wx.showToast({
                title: '定位城市失败，显示默认城市天气',
                icon: 'none',
                duration: 2000
              });
              this.getWeatherData('北京', this.data.inputDate);
            }
          },
          fail: (error) => {
            console.log('AMap API request failed:', error);
            wx.showToast({
              title: '获取城市信息失败，显示默认城市天气',
              icon: 'none',
              duration: 2000
            });
            this.getWeatherData('北京', this.data.inputDate);
          }
        });
      },
      fail: (err) => {
        // 根据错误类型显示不同提示
        let message = '获取位置失败，请手动选择城市';
        if (err.errMsg.includes('auth deny')) {
          message = '位置权限被拒绝，请手动选择城市或在设置中允许位置权限';
        }
        wx.showModal({
          title: '提示',
          content: message,
          showCancel: false,
          success: () => {
            this.setData({ weatherData: null });
          }
        });
        console.log('Location fail:', err);
      }
    });
  },

  getWeatherData(location, date) {
    this.setData({ loading: true });
    
    wx.request({
      url: `http://139.224.36.136:8080/weather/get`,
      method: 'GET',
      data: {
        location: location,
        date: date
      },
      success: (res) => {
        if (res.data.code === 1) {
          const weatherData = res.data.data;
          const iconMap = {
            '晴': '/images/sunny.png',
            '多云': '/images/cloudy.png',
            '阴': '/images/overcast.png',
            '雨': '/images/rain.png',
            '雪': '/images/snow.png'
          };
          weatherData.weatherIcon = iconMap[weatherData.description];
          
          this.setData({
            weatherData: res.data.data,
            loading: false
          });
        } else {
          wx.showToast({
            title: '获取天气失败',
            icon: 'none'
          });
          this.setData({ weatherData: null, loading: false });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
        this.setData({ weatherData: null, loading: false });
      }
    });
  },

  onLocationInput(e) {
    this.setData({
      inputLocation: e.detail.value
    });
  },

  onDateChange(e) {
    this.setData({
      inputDate: e.detail.value,
      hasSelectedDate: true
    });
  },

  searchWeather() {
    if (!this.data.inputLocation) {
      wx.showToast({
        title: '请输入城市名',
        icon: 'none'
      });
      return;
    }
    
    if (!this.data.inputDate) {
      wx.showToast({
        title: '请选择日期',
        icon: 'none'
      });
      return;
    }
    
    this.getWeatherData(this.data.inputLocation, this.data.inputDate);
  },

  // 获取提醒列表
  // 在 weather.js 中修改 fetchReminders 方法
  fetchReminders() {
    const { userId } = this.data;
    wx.request({
      url: `http://139.224.36.136:8080/reminder/get?userID=${userId}`,  // userID 改为 userId
      method: 'GET',
      success: (res) => {
        if (res.data.code === 1) {
          console.log(res.data.data);
          const reminders = res.data.data.map(item => ({
            ...item,
            time: item.time.split('T')[1].slice(0, 5),
            reminderDate: item.time.split('T')[0],
          }));
          this.setData({ reminders });
        }
      },
      fail: (err) => {
        console.error('获取提醒列表失败:', err);
      }
    });
  },

  // 添加提醒
  handleAddReminder() {
    if (!this.data.userId) {
      console.log('跳转到登录页面')
      wx.redirectTo({
        url: '/pages/login/login'
      })
      return
    }
    wx.requestSubscribeMessage({
      tmplIds: ['QiqGKiYCCTTaCynpmMLiv8FPkNfohFnHqDC4SrHYG_A'], // 替换为你在公众平台申请的模板ID
      success: (res) => {
        if (res['QiqGKiYCCTTaCynpmMLiv8FPkNfohFnHqDC4SrHYG_A'] === 'accept') {
          wx.navigateTo({
            url: '/pages/weather/editReminder/editReminder'
          });
        } else {
          wx.showToast({
            title: '需要订阅消息权限才能添加提醒',
            icon: 'none',
            duration: 2000
          });
        }
      },
      fail: (err) => {
        console.log('订阅消息失败：', err);
        wx.showToast({
          title: '订阅消息失败，请稍后重试',
          icon: 'none',
          duration: 2000
        });
      }
    });
  },

  // 编辑提醒
  handleEditReminder(e) {
    const { id } = e.currentTarget.dataset;
    const reminder = this.data.reminders.find(item => item.id === id);
    // 将选中的提醒数据序列化后通过 URL 参数传递
    const reminderStr = encodeURIComponent(JSON.stringify(reminder));
    wx.navigateTo({
      url: `/pages/weather/editReminder/editReminder?reminderData=${reminderStr}`
    });
  },

  // 删除提醒
  handleDeleteReminder(e) {
    const { id } = e.currentTarget.dataset;
    console.log(id);
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这条天气提醒吗？',
      success: (res) => {
        if (res.confirm) {
          wx.request({
            url: `http://139.224.36.136:8080/reminder/delete?id=${id}`,
            method: 'DELETE',
            success: (res) => {
              if (res.data.code === 1) {
                wx.showToast({
                  title: '删除成功',
                  icon: 'success'
                });
                this.fetchReminders();
              } else {
                wx.showToast({
                  title: '删除失败',
                  icon: 'none'
                });
              }
            }
          });
        }
      }
    });
  },
  onShow() {
    // 每次页面显示时都刷新提醒列表
    this.fetchReminders();
  }
});