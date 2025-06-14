Page({
  data: {
    itineraries: [],
    pages: [],
    currentIndex: 0
  },

  onLoad() {
    this.fetchItineraries()
  },

  onShow() {
    this.fetchItineraries()
  },

  fetchItineraries() {
    const userId = wx.getStorageSync('userId')
    wx.request({
      url: `http://139.224.36.136:8080/itinerary/getall?userID=${userId}`,
      method: 'GET',
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.data.code === 1) {
          const itineraries = res.data.data
          // 将行程按每页4个分组
          const pages = []
          for (let i = 0; i < itineraries.length; i += 4) {
            pages.push(itineraries.slice(i, i + 4))
          }
          
          this.setData({
            itineraries: itineraries,
            pages: pages
          })
        } else {
          wx.showToast({
            title: '获取行程失败',
            icon: 'none'
          })
        }
      },
      fail: (error) => {
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        })
      }
    })
  },

  handleSwiperChange(e) {
    const current = e.detail.current
    this.setData({
      currentIndex: current
    })
  },  

  handleCardClick(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/itinerary/showEvent/event?id=${id}`
    })
    console.log('showEvent',id);
  },

  handleEdit(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/itinerary/editItinerary/editItinerary?id=${id}`
    })
    console.log('edit',id);
  },

  handleAdd() {
    const userId = wx.getStorageSync('userId')
    if (!userId) {
      console.log('跳转到登录页面')
      wx.redirectTo({
        url: '/pages/login/login'
      })
      return
    }
    
    wx.navigateTo({
      url: '/pages/itinerary/editItinerary/editItinerary'
    })
    console.log('add')
  },

  handleDelete(e) {
    const { id } = e.currentTarget.dataset
    wx.showModal({
      title: '确认删除',
      content: '确定要删除此行程吗？',
      success: (res) => {
        if (res.confirm) {
          wx.request({
            url: `http://139.224.36.136:8080/itinerary/delete?id=${id}`,
            method: 'DELETE',
            success: (res) => {
              if (res.data.code === 1) {
                wx.showToast({
                  title: '删除成功',
                  icon: 'success'
                })
                this.fetchItineraries()
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
   }
})