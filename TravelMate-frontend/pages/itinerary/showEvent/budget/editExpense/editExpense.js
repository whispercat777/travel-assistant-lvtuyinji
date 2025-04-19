Page({
  data: {
    eventId: null,
    expenseId: null,
    expense: {
      name: '',
      money: '',
      date: '',
      time: '',
      type: ''
    },
    eventTypes: {
      1: { name: '交通', icon: '/images/transport.png' },
      2: { name: '餐饮', icon: '/images/food.png' },
      3: { name: '娱乐', icon: '/images/entertainment.png' },
      4: { name: '购物', icon: '/images/shopping.png' },
      5: { name: '住宿', icon: '/images/accommodation.png' },
      6: { name: '观光', icon: '/images/sightseeing.png' },
      7: { name: '其他', icon: '/images/others.png' }
    },
    isSaveDisabled: true
  },

  onLoad(options) {
    const { eventId, expense } = options
    this.setData({ 
      eventId
    })
  
    if (expense) {
      // 解析传递来的expense数据
      const expenseData = JSON.parse(expense)
      const dateTime = new Date(expenseData.time)
      
      this.setData({
        expenseId: expenseData.id,
        expense: {
          name: expenseData.name,
          money: expenseData.money,
          date: this.formatDate(dateTime),
          time: this.formatTime(dateTime),
          type: expenseData.type
        }
      })
      this.checkSaveDisabled()
    }
  },

  formatDate(date) {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  },

  formatTime(date) {
    return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
  },

  handleInputChange(e) {
    const { field } = e.currentTarget.dataset
    const value = field === 'money' ? this.formatMoney(e.detail.value) : e.detail.value
    
    this.setData({
      [`expense.${field}`]: value
    })
    this.checkSaveDisabled()
  },

  formatMoney(value) {
    // 限制只能输入数字和小数点，且最多两位小数
    value = value.replace(/[^\d.]/g, '')
    const parts = value.split('.')
    if (parts.length > 2) {
      value = parts[0] + '.' + parts[1]
    }
    if (parts.length === 2 && parts[1].length > 2) {
      value = parts[0] + '.' + parts[1].substring(0, 2)
    }
    return value
  },

  handleDateChange(e) {
    this.setData({
      'expense.date': e.detail.value
    })
    this.checkSaveDisabled()
  },

  handleTimeChange(e) {
    this.setData({
      'expense.time': e.detail.value
    })
    this.checkSaveDisabled()
  },

  handleTypeSelect(e) {
    const { type } = e.currentTarget.dataset
    this.setData({
      'expense.type': type
    })
    this.checkSaveDisabled()
  },

  checkSaveDisabled() {
    const { name, money, date, time, type } = this.data.expense
    const isSaveDisabled = !name || !money || !date || !time || !type
    this.setData({ isSaveDisabled })
  },

  handleSave() {
    if (this.data.isSaveDisabled) return

    const { name, money, date, time, type } = this.data.expense
    const timeString = `${date}T${time}:00`
    
    if (this.data.expenseId) {
      // 编辑
      wx.request({
        url: 'http://113.44.75.241:8080/expense/modify',
        method: 'PUT',
        data: {
          id: this.data.expenseId,
          name,
          money: parseFloat(money),
          time: timeString,
          type
        },
        success: (res) => {
          if (res.data.code === 1) {
            wx.showToast({
              title: '修改成功',
              icon: 'success',
              duration: 2000,
              complete: () => {
                setTimeout(() => {
                  wx.navigateBack()
                }, 2000)
              }
            })
          }
        }
      })
    } else {
      // 新增
      wx.request({
        url: 'http://113.44.75.241:8080/expense/add',
        method: 'POST',
        data: {
          eveID: this.data.eventId,
          name,
          money: parseFloat(money),
          time: timeString,
          type
        },
        success: (res) => {
          if (res.data.code === 1) {
            wx.showToast({
              title: '添加成功',
              icon: 'success',
              duration: 2000,
              complete: () => {
                setTimeout(() => {
                  wx.navigateBack()
                }, 2000)
              }
            })
          }
        }
      })
    }
  }
})