/**
 * 行程事件页面
 * 展示行程详情，包括事件列表、添加/编辑/删除事件等功能
 */
Page({
  /**
   * 页面的初始数据
   */
  data: {
    itinerary: null,             // 当前行程数据
    showBudgetModal: false,      // 预算模态框显示状态
    currentEventId: null,        // 当前选中的事件ID
    eventTypes: {                // 事件类型配置
      1: { name: '交通', icon: '/images/transport.png' },
      2: { name: '餐饮', icon: '/images/food.png' },
      3: { name: '娱乐', icon: '/images/entertainment.png' },
      4: { name: '购物', icon: '/images/shopping.png' },
      5: { name: '住宿', icon: '/images/accommodation.png' },
      6: { name: '观光', icon: '/images/sightseeing.png' },
      7: { name: '其他', icon: '/images/others.png' }
    }
  },

  /**
   * 生命周期函数--监听页面加载
   * @param {Object} options - 页面参数
   */
  onLoad(options) {
    const { id } = options;
    this.fetchItineraryData(id);
  },

  /**
   * 获取行程数据
   * @param {string} itineraryId - 行程ID
   */
  fetchItineraryData(itineraryId) {
    wx.request({
      url: `http://139.224.36.136:8080/itinerary/getone?ID=${itineraryId}`,
      method: 'GET',
      success: (res) => {
        if (res.data.code === 1) {
          // 按开始时间排序并格式化时间
          console.log(res.data.data.events);
          const sortedEvents = res.data.data.events.map(event => ({
            ...event,
            formattedStartTime: event.startTime.split('T').join(' ').substring(0, 16),
            formattedEndTime: event.endTime.split('T').join(' ').substring(0, 16)
          })).sort((a, b) => new Date(a.startTime) - new Date(b.startTime));
          
          res.data.data.events = sortedEvents;
          
          this.setData({
            itinerary: res.data.data
          });
          wx.setNavigationBarTitle({
            title: res.data.data.name
          });
        } else {
          wx.showToast({
            title: '获取行程失败',
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
  },

  /**
   * 处理编辑事件
   * @param {Object} event - 事件对象
   */
  handleEditEvent(event) {
    const { id } = event.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/itinerary/showEvent/editEvent/editEvent?id=${id}`
    });
  },

  /**
   * 处理显示预算
   * @param {Object} event - 事件对象
   */
  handleShowBudget(event) {
    const { id } = event.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/itinerary/showEvent/budget/budget?id=${id}`
    });
  },

  /**
   * 处理添加事件
   */
  handleAddEvent() {
    wx.navigateTo({
      url: `/pages/itinerary/showEvent/editEvent/editEvent?itiID=${this.data.itinerary.id}`
    });
  },

  /**
   * 处理删除事件
   * @param {Object} event - 事件对象
   */
  handleDeleteEvent(event) {
    const { id } = event.currentTarget.dataset;
    wx.showModal({
      title: '确认删除',
      content: '确定要删除此事件吗？',
      success: (res) => {
        if (res.confirm) {
          wx.request({
            url: `http://139.224.36.136:8080/event/delete?id=${id}`,
            method: 'DELETE',
            success: (res) => {
              if (res.data.code === 1) {
                wx.showToast({
                  title: '删除成功',
                  icon: 'success'
                });
                this.fetchItineraryData(this.data.itinerary.id);
              } else {
                wx.showToast({
                  title: '删除失败', 
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
  },

  /**
   * 处理智能推荐
   * @param {Object} event - 事件对象
   */
  handleIntelligence(event) {
    wx.navigateTo({
      url:`/pages/itinerary/intelligence/intelligence?itiID=${this.data.itinerary.id}`
    });
  }
});