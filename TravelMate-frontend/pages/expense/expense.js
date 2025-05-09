// pages/expense/expense.js
const app = getApp()

Page({
  data: {
    userID: '681295884',
    viewMode: 'all',
    totalExpense: 0,
    totalBudget: 0,
    expenses: [],
    budgets: [],
    trips: [], 
    selectedTypes: [],
    tripId: null,  // 改为单个tripId
    startTime: '',
    endTime: '',
    typeId: null, 
    typeOptions: [
      { id: 1, name: '交通', icon: '/images/transport.png'},
      { id: 2, name: '餐饮', icon: '/images/food.png'},
      { id: 3, name: '娱乐', icon: '/images/entertainment.png'},
      { id: 4, name: '购物', icon: '/images/shopping.png'},
      { id: 5, name: '住宿', icon: '/images/accommodation.png'},
      { id: 6, name: '观光', icon: '/images/sightseeing.png'},
      { id: 7, name: '其他', icon: '/images/others.png'}
    ]
  },

  onLoad() {
    const now = new Date()
    const startOfWeek = new Date(now.setDate(now.getDate() - 7))
    const endOfWeek = new Date()
    
    this.setData({
      startTime: startOfWeek.toISOString().split('T')[0],
      endTime: endOfWeek.toISOString().split('T')[0]
    })
    
    this.fetchTimeExpenses()
  },

  onStartTimeChange(e) {
    this.setData({ startTime: e.detail.value })
    this.fetchTimeExpenses()
  },

  onEndTimeChange(e) {
    this.setData({ endTime: e.detail.value })
    this.fetchTimeExpenses()
  },

  switchViewMode(e) {
    const mode = e.currentTarget.dataset.mode
    this.setData({ 
      viewMode: mode,
      typeId: null,  // 切换模式时重置类别选择
      tripId: null   // 切换模式时重置行程选择
    })
    
    if (mode === 'all') {
      this.fetchTimeExpenses()
    } else if (mode === 'category') {
      this.fetchByType()
    } else if (mode === 'trip') {
      this.fetchAllTrips()
      this.fetchBudgetAndExpense()
    }
  },

  fetchAllTrips() {
    wx.request({
      url: `http://113.44.75.241:8080/itinerary/getall`,
      method: 'GET',
      data: {
        userID: this.data.userID
      },
      success: (res) => {
        if (res.data.code === 1) {
          this.setData({
            trips: res.data.data
          })
        }
      }
    })
  },

  fetchTimeExpenses() {
    wx.request({
      url: `http://113.44.75.241:8080/report/time`,
      method: 'GET',
      data: {
        userID: this.data.userID,
        startTime: this.data.startTime + 'T00:00:00',
        endTime: this.data.endTime + 'T23:59:59'
      },
      success: (res) => {
        if (res.data.code === 1) {
          const formattedExpenses = res.data.data.expenses.map(expense => ({
            ...expense,
            time: expense.time.replace('T', ' ').substring(0, 16)
          }));
          this.setData({
            totalExpense: res.data.data.totalExpense,
            expenses: formattedExpenses
          })
        }
      }
    })
  },

  toggleType(e) {
    const typeId = parseInt(e.currentTarget.dataset.type)
    this.setData({
      selectedTypes: [typeId],
      typeId: typeId
    })
    this.fetchByType()
  },

  toggleTrip(e) {
    const tripId = e.currentTarget.dataset.id
    this.setData({
      tripId: tripId
    })
    this.fetchTripExpenses()
  },
  
  fetchByType() {
    const types = this.data.selectedTypes.join(',')
    wx.request({
      url: `http://113.44.75.241:8080/report/type`,
      method: 'GET',
      data: {
        userID: this.data.userID,
        types: types || '1,2,3,4,5,6,7'
      },
      success: (res) => {
        if (res.data.code === 1) {
          const formattedExpenses = res.data.data.expenses.map(expense => ({
            ...expense,
            time: expense.time.replace('T', ' ').substring(0, 16)
          }));
          this.setData({
            totalExpense: res.data.data.totalExpense,
            expenses: formattedExpenses
          })
        }
      }
    })
  },

  fetchBudgetAndExpense() {
    wx.request({
      url: `http://113.44.75.241:8080/report/budgetandexpense`,
      method: 'GET',
      data: {
        userID: this.data.userID
      },
      success: (res) => {
        if (res.data.code === 1) {
          const formattedExpenses = res.data.data.expenses.map(expense => ({
            ...expense,
            time: expense.time.replace('T', ' ').substring(0, 16)
          }));
          this.setData({
            totalExpense: res.data.data.totalExpense,
            totalBudget: res.data.data.totalBudget,
            expenses: formattedExpenses,
            budgets: res.data.data.budgets
          })
        }
      }
    })
  },

  fetchTripExpenses() {
    if (!this.data.tripId) {
      this.fetchBudgetAndExpense()
      return
    }
    wx.request({
      url: `http://113.44.75.241:8080/report/iti`,
      method: 'GET',
      data: {
        itiIDs: this.data.tripId
      },
      success: (res) => {
        if (res.data.code === 1) {
          const formattedExpenses = res.data.data.expenses.map(expense => ({
            ...expense,
            time: expense.time.replace('T', ' ').substring(0, 16)
          }));
          this.setData({
            totalExpense: res.data.data.totalExpense,
            totalBudget: res.data.data.totalBudget,
            expenses: formattedExpenses,
            budgets: res.data.data.budgets
          })
        }
      }
    })
  }
})