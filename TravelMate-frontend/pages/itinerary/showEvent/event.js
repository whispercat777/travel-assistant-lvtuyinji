Page({
  data: {
    itinerary: null,
    showBudgetModal: false,
    currentEventId: null,
    eventTypes: {
      1: { name: '交通', icon: '/images/transport.png' },
      2: { name: '餐饮', icon: '/images/food.png' },
      3: { name: '娱乐', icon: '/images/entertainment.png' },
      4: { name: '购物', icon: '/images/shopping.png' },
      5: { name: '住宿', icon: '/images/accommodation.png' },
      6: { name: '观光', icon: '/images/sightseeing.png' },
      7: { name: '其他', icon: '/images/others.png' }
    }
  },

  onLoad(options) {
    const { id } = options
    this.fetchItineraryData(id)
  },

  fetchItineraryData(id) {
    wx.request({
      url: `http://113.44.75.241:8080/itinerary/getone?ID=${id}`,
      method: 'GET',
      success: (res) => {
        if (res.data.code === 1) {
          // Sort events by start time and format the time
          console.log(res.data.data.events);
          const sortedEvents = res.data.data.events.map(event => ({
            ...event,
            formattedStartTime: event.startTime.split('T').join(' ').substring(0, 16),
            formattedEndTime: event.endTime.split('T').join(' ').substring(0, 16)
          })).sort((a, b) => new Date(a.startTime) - new Date(b.startTime))
          
          res.data.data.events = sortedEvents
          
          this.setData({
            itinerary: res.data.data
          })
          wx.setNavigationBarTitle({
            title: res.data.data.name
          })
        } else {
          wx.showToast({
            title: '获取行程失败',
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

  handleEditEvent(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/itinerary/showEvent/editEvent/editEvent?id=${id}`
    })
  },

  handleShowBudget(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/itinerary/showEvent/budget/budget?id=${id}`
    })
  },

  handleAddEvent() {
    wx.navigateTo({
      url: `/pages/itinerary/showEvent/editEvent/editEvent?itiID=${this.data.itinerary.id}`
    })
  },

  handleDeleteEvent(e) {
    const { id } = e.currentTarget.dataset
    wx.showModal({
      title: '确认删除',
      content: '确定要删除此事件吗？',
      success: (res) => {
        if (res.confirm) {
          wx.request({
            url: `http://113.44.75.241:8080/event/delete?id=${id}`,
            method: 'DELETE',
            success: (res) => {
              if (res.data.code === 1) {
                wx.showToast({
                  title: '删除成功',
                  icon: 'success'
                })
                this.fetchItineraryData(this.data.itinerary.id)
              } else {
                wx.showToast({
                  title: '删除失败', 
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
   },

  handleIntelligence(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url:`/pages/itinerary/intelligence/intelligence?itiID=${this.data.itinerary.id}`
    })
  }
})