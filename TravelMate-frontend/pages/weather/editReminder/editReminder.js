Page({
  data: {
    isEdit: false,
    reminder: {
      id: null,
      reminderDate: '',
      reminderTime: '',
      queryDate: '',
      location: '',
      userId: ''
    },
    today: '',
    currentTime: '',
    endDate: '',
    isSaveDisabled: true
  },

  onLoad(options) {
    const userId = wx.getStorageSync('userId');
    const today = new Date();
    const todayStr = today.toISOString().split('T')[0];
    const currentTime = `${String(today.getHours()).padStart(2, '0')}:${String(today.getMinutes()).padStart(2, '0')}`;
    
    const endDate = new Date();
    endDate.setDate(today.getDate() + 3);
    const endDateStr = endDate.toISOString().split('T')[0];
    
    this.setData({
      today: todayStr,
      currentTime: currentTime,
      endDate: endDateStr,
      'reminder.userId': userId
    });
  
    if (options.reminderData) {
      const reminderData = JSON.parse(decodeURIComponent(options.reminderData));
      const time = reminderData.time.split('T');
      this.setData({
        isEdit: true,
        reminder: {
          ...this.data.reminder,
          id: reminderData.id,
          reminderDate: reminderData.reminderDate,
          reminderTime: reminderData.time,
          queryDate: reminderData.date,
          location: reminderData.location
        }
      });
    }
  },

  handleDateChange(e) {
    const { field } = e.currentTarget.dataset;
    const value = e.detail.value;
    
    // 如果选择的是今天，需要限制时间选择
    if (value === this.data.today) {
      const currentTime = this.data.currentTime;
      const selectedTime = this.data.reminder.reminderTime;
      
      // 如果已经选择了时间，检查是否需要重置
      if (selectedTime && selectedTime < currentTime) {
        this.setData({
          'reminder.reminderTime': ''  // 清空无效的时间选择
        });
      }
    }
    
    // 计算新的查询日期范围结束时间（提醒日期后3天）
    const reminderDate = new Date(value);
    const newEndDate = new Date(reminderDate);
    newEndDate.setDate(reminderDate.getDate() + 3);
    const newEndDateStr = newEndDate.toISOString().split('T')[0];
    
    this.setData({
      [`reminder.${field}`]: value,
      'reminder.queryDate': value, // 重置查询日期为提醒时间的日期
      endDate: newEndDateStr // 更新查询日期的结束范围
    });
    this.checkSaveDisabled();
  },

  handleTimeChange(e) {
    const selectedTime = e.detail.value;
    const currentDate = new Date();
    const currentTimeStr = `${String(currentDate.getHours()).padStart(2, '0')}:${String(currentDate.getMinutes()).padStart(2, '0')}`;
    
    // 如果选择的是今天，需要验证时间是否有效
    if (this.data.reminder.reminderDate === this.data.today) {
      if (selectedTime <= currentTimeStr) {
        wx.showToast({
          title: '请选择当前时间之后的时间',
          icon: 'none'
        });
        return;
      }
    }
    
    this.setData({
      'reminder.reminderTime': selectedTime
    });
    this.checkSaveDisabled();
  },

  handleQueryDateChange(e) {
    this.setData({
      'reminder.queryDate': e.detail.value
    });
    this.checkSaveDisabled();
  },

  handleInputChange(e) {
    const { field } = e.currentTarget.dataset;
    this.setData({
      [`reminder.${field}`]: e.detail.value
    });
    this.checkSaveDisabled();
  },

  checkSaveDisabled() {
    const { reminderDate, reminderTime, queryDate, location } = this.data.reminder;
    const isSaveDisabled = !reminderDate || !reminderTime || !queryDate || !location;
    this.setData({ isSaveDisabled });
  },

  handleSave() {
    if (this.data.isSaveDisabled) return;

    // 再次验证时间是否有效
    if (this.data.reminder.reminderDate === this.data.today) {
      const currentDate = new Date();
      const currentTimeStr = `${String(currentDate.getHours()).padStart(2, '0')}:${String(currentDate.getMinutes()).padStart(2, '0')}`;
      
      if (this.data.reminder.reminderTime <= currentTimeStr) {
        wx.showToast({
          title: '请选择当前时间之后的时间',
          icon: 'none'
        });
        return;
      }
    }

    const { reminderDate, reminderTime, queryDate, location, userId, id } = this.data.reminder;

    const time = `${reminderDate}T${reminderTime}:00`;
    
    if (this.data.isEdit) {
      // 编辑提醒
      wx.request({
        url: 'http://139.224.36.136:8080/reminder/modify',
        method: 'PUT',
        data: {
          id,
          time,
          date: queryDate,
          location
        },
        success: (res) => {
          if (res.data.code === 1) {
            const pages = getCurrentPages();
            const prevPage = pages[pages.length - 2];
            prevPage.fetchReminders(); // 调用上一页的刷新方法
            wx.showToast({
              title: '修改成功',
              icon: 'success',
              duration: 2000,
              complete: () => {
                setTimeout(() => {
                  wx.navigateBack();
                }, 2000);
              }
            });
          } else {
            wx.showToast({
              title: '修改失败',
              icon: 'none'
            });
          }
        },
        fail: () => {
          wx.showToast({
            title: '网络错误',
            icon: 'none'
          });
        }
      });
    } else {
      // 新增提醒
      wx.request({
        url: 'http://139.224.36.136:8080/reminder/add',
        method: 'POST',
        data: {
          userId,
          time,
          date: queryDate,
          location
        },
        success: (res) => {
          if (res.data.code === 1) {
            const pages = getCurrentPages();
            const prevPage = pages[pages.length - 2];
            prevPage.fetchReminders(); // 调用上一页的刷新方法
            wx.showToast({
              title: '添加成功',
              icon: 'success',
              duration: 2000,
              complete: () => {
                setTimeout(() => {
                  wx.navigateBack();
                }, 2000);
              }
            });
          } else {
            wx.showToast({
              title: '添加失败',
              icon: 'none'
            });
          }
        },
        fail: () => {
          wx.showToast({
            title: '网络错误',
            icon: 'none'
          });
        }
      });
    }
  }
});