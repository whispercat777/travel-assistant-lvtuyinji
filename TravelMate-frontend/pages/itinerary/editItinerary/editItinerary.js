Page({
  data: {
    id: null,
    isEditMode: false,
    name: '',
    startDate: '',
    endDate: '',
    location: '',
    originalName: '',
    originalStartDate: '',
    originalEndDate: '',
    originalLocation: '',
    isFormValid: false
  },

  onLoad(options) {
    // 如果是编辑模式，这里会接收到id参数
    if (options.id) {
      this.setData({
        id: options.id,
        isEditMode: true
      })
      this.fetchItineraryDetail(options.id)
    }
  },

  fetchItineraryDetail(id) {
    wx.request({
      url: `http://113.44.75.241:8080/itinerary/getone?ID=${id}`,
      method: 'GET',
      success: (res) => {
        if (res.data.code === 1) {
          const { name, startDate, endDate, location } = res.data.data
          this.setData({
            name,
            startDate,
            endDate,
            location,
            originalName: name,
            originalStartDate: startDate,
            originalEndDate: endDate,
            originalLocation: location
          }, () => {
            this.checkFormValidity()
          })
        } else {
          wx.showToast({
            title: '获取行程信息失败',
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
  },

  // 检查表单是否所有字段都已填写
  checkFormValidity() {
    const { name, startDate, endDate, location } = this.data
    const isValid = name && startDate && endDate && location
    this.setData({ isFormValid: isValid })
  },

  handleNameInput(e) {
    this.setData({ 
      name: e.detail.value 
    }, () => {
      this.checkFormValidity()
    })
  },

  handleStartDateChange(e) {
    const startDate = e.detail.value
    // 如果结束日期早于开始日期，清空结束日期
    if (this.data.endDate && this.data.endDate < startDate) {
      this.setData({
        endDate: ''
      })
    }
    this.setData({ 
      startDate
    }, () => {
      this.checkFormValidity()
    })
  },

  handleEndDateChange(e) {
    this.setData({ 
      endDate: e.detail.value 
    }, () => {
      this.checkFormValidity()
    })
  },

  handleLocationInput(e) {
    this.setData({ 
      location: e.detail.value 
    }, () => {
      this.checkFormValidity()
    })
  },

  handleSave() {
    if (!this.data.isFormValid) return

    const userId = wx.getStorageSync('userId')
    let requestData = {
      userID: userId,
      name: this.data.name,
      startDate: this.data.startDate,
      endDate: this.data.endDate,
      location: this.data.location
    }

    // 根据是否为编辑模式选择不同的URL
    const url = this.data.isEditMode ? 
      'http://113.44.75.241:8080/itinerary/modify' :  // 编辑模式URL
      'http://113.44.75.241:8080/itinerary/add'       // 新建模式URL

      if (this.data.isEditMode) {
        requestData = { id: this.data.id }
        if (this.data.name !== this.data.originalName) requestData.name = this.data.name
        if (this.data.startDate !== this.data.originalStartDate) requestData.startDate = this.data.startDate
        if (this.data.endDate !== this.data.originalEndDate) requestData.endDate = this.data.endDate
        if (this.data.location !== this.data.originalLocation) requestData.location = this.data.location
      }

    wx.request({
      url: url,
      method: this.data.isEditMode ? 'PUT' : 'POST',
      data: requestData,
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.data.code === 1) {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 1500,
            complete: () => {
              // 延迟返回，让用户看到提示
              setTimeout(() => {
                wx.navigateBack()
              }, 1500)
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
})