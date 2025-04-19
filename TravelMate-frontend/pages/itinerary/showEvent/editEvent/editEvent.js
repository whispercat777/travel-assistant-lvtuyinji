Page({
  data: {
    isEdit: false,
    itiID: null, 
    event: {
      name: '',
      startDate: '',
      startTime: '',
      endDate: '',
      endTime: '',
      location: '',
      type: '',
      description: ''
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
    const { id, itiID } = options  // 添加 itiID
    console.log(itiID)
    if (itiID) {
      this.setData({ itiID })
    }
    if (id) {
      this.setData({
        isEdit: true,
        'event.id': id
      })
      const pages = getCurrentPages()
      const prevPage = pages[pages.length - 2]
      const eventData = prevPage.data.itinerary.events.find(e => e.id === parseInt(id))
      if (eventData) {
        // 解析日期时间
        const startDateTime = new Date(eventData.startTime)
        const endDateTime = new Date(eventData.endTime)
        
        this.setData({
          event: {
            ...eventData,
            startDate: this.formatDate(startDateTime),
            startTime: this.formatTime(startDateTime),
            endDate: this.formatDate(endDateTime),
            endTime: this.formatTime(endDateTime)
          }
        })
      }
    }
    this.checkSaveDisabled()
  },

  formatDate(date) {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  },

  formatTime(date) {
    return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
  },

  handleInputChange(e) {
    const { field } = e.currentTarget.dataset
    this.setData({
      [`event.${field}`]: e.detail.value
    })
    this.checkSaveDisabled()
  },

  handleDateChange(e) {
    const { field } = e.currentTarget.dataset
    const value = e.detail.value
    this.setData({
      [`event.${field}`]: value
    })
    this.checkSaveDisabled()
  },

  handleTimeChange(e) {
    const { field } = e.currentTarget.dataset
    const value = e.detail.value
    this.setData({
      [`event.${field}`]: value
    })
    this.checkSaveDisabled()
  },

  handleTypeSelect(e) {
    const { type } = e.currentTarget.dataset
    this.setData({
      'event.type': type
    })
    this.checkSaveDisabled()
  },

  checkSaveDisabled() {
    const { name, startDate, startTime, endDate, endTime, location, type } = this.data.event
    let isTimeValid = false
    
    if (startDate && startTime && endDate && endTime) {
      const startDateTime = new Date(`${startDate} ${startTime}`)
      const endDateTime = new Date(`${endDate} ${endTime}`)
      
      // 检查日期是否有效
      if (!isNaN(startDateTime) && !isNaN(endDateTime)) {
        isTimeValid = endDateTime > startDateTime
      }
    }
    
    const isSaveDisabled = !name || !startDate || !startTime || !endDate || !endTime || !location || !type || !isTimeValid
    
    if (!isTimeValid && startDate && startTime && endDate && endTime) {
      wx.showToast({
        title: '结束时间必须晚于开始时间',
        icon: 'none'
      })
    }
    
    this.setData({ isSaveDisabled })
  },

  combineDateTime(date, time) {
    if (!date || !time) return '';
    return `${date} ${time}:00`;
  },

  handleSave() {
    if (this.data.isSaveDisabled) return
   
    const { startDate, startTime, endDate, endTime } = this.data.event
    const startDateTime = `${startDate}T${startTime}:00`
    const endDateTime = `${endDate}T${endTime}:00`
   
    if (this.data.isEdit) {
      // 构建编辑请求数据
      const requestData = { id: this.data.event.id }
   
      if (this.data.event.name) requestData.name = this.data.event.name
      if (this.data.event.location) requestData.location = this.data.event.location
      if (this.data.event.type) requestData.type = this.data.event.type
      if (this.data.event.description) requestData.description = this.data.event.description
      if (startDateTime) requestData.startTime = startDateTime
      if (endDateTime) requestData.endTime = endDateTime
   
      wx.request({
        url: 'http://113.44.75.241:8080/event/modify',
        method: 'PUT',
        data: requestData,
        success: (res) => {
          if (res.data.code === 1) {
            const pages = getCurrentPages()
            const prevPage = pages[pages.length - 2]
            prevPage.fetchItineraryData(prevPage.data.itinerary.id)
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
          } else {
            wx.showToast({
              title: '修改失败',
              icon: 'none'
            })
          }
        },
        fail: () => {
          wx.showToast({
            title: '网络错误',
            icon: 'none'
          })
        }
      })
    } else {
      // 添加事件逻辑
      wx.request({
        url: 'http://113.44.75.241:8080/event/add',
        method: 'POST',
        data: {
          itiID: this.data.itiID,
          name: this.data.event.name,
          startTime: startDateTime,
          endTime: endDateTime,
          location: this.data.event.location,
          description: this.data.event.description,
          type: this.data.event.type
        },
        success: (res) => {
          if (res.data.code === 1) {
            const pages = getCurrentPages()
            const prevPage = pages[pages.length - 2]
            prevPage.fetchItineraryData(this.data.itiID)
            wx.showToast({
              title: '保存成功',
              icon: 'success',
              duration: 2000,
              complete: () => {
                setTimeout(() => {
                  wx.navigateBack()
                }, 2000)
              }
            })
          } else {
            wx.showToast({
              title: '保存失败',
              icon: 'none'
            })
          }
        },
        fail: () => {
          wx.showToast({
            title: '网络错误',
            icon: 'none'
          })
        }
      })
    }
   }
})