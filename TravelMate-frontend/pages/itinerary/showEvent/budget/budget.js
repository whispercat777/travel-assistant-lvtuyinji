Page({
  data: {
    eventId: null,
    budget: null,
    expenses: [],
    totalExpenses: 0,
    remainingBudget: 0,
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
    this.setData({ eventId: options.id })
    this.fetchData()
  },

  onShow() {
    this.fetchData()
  },

  fetchData() {
    // Fetch budget data
    wx.request({
      url: `http://113.44.75.241:8080/budget/event?eveID=${this.data.eventId}`,
      success: (res) => {
        if (res.data.code === 1 && res.data.data.length > 0) {
          this.setData({ budget: res.data.data[0] }, () => {
            this.calculateBudget()
          })
        } else {
          this.setData({ budget: null })
        }
      }
    })

    // Fetch expenses data
    wx.request({
      url: `http://113.44.75.241:8080/expense/event?eveID=${this.data.eventId}`,
      success: (res) => {
        if (res.data.code === 1) {
          const expenses = res.data.data.map(expense => ({
            ...expense,
            time: expense.time.replace('T', ' ').substring(0, 16)
          }))
          this.setData({ expenses }, () => {
            this.calculateBudget()
          })
        }
      }
    })
  },

  calculateBudget() {
    const totalExpenses = this.data.expenses.reduce((sum, expense) => sum + expense.money, 0)
    const remainingBudget = this.data.budget ? this.data.budget.money - totalExpenses : 0
    
    this.setData({
      totalExpenses: totalExpenses.toFixed(2),  // 保留两位小数
      remainingBudget,
      // 预算金额也保留两位小数
      budget: this.data.budget ? {
        ...this.data.budget,
        money: Number(this.data.budget.money).toFixed(2)
      } : null
    })
  },
  
  // 修改标记预算的文案
  handleEditBudget() {
    wx.showModal({
      title: '编辑预算',
      placeholderText: '请输入预算金额',
      editable: true,
      success: (res) => {
        if (res.confirm) {
          const money = parseFloat(res.content)
          if (isNaN(money) || money <= 0) {
            wx.showToast({
              title: '请输入有效金额',
              icon: 'none'
            })
            return
          }
  
          wx.request({
            url: 'http://113.44.75.241:8080/budget/modify',
            method: 'PUT',
            data: {
              id: this.data.budget.id,
              money: money
            },
            success: (res) => {
              if (res.data.code === 1) {
                this.fetchData()
                wx.showToast({ title: '编辑成功' })
              }
            }
          })
        }
      }
    })
  },

  handleAddBudget() {
    wx.showModal({
      title: '添加预算',
      placeholderText: '请输入预算金额',
      editable: true,
      success: (res) => {
        if (res.confirm) {
          const money = parseFloat(res.content)
          if (isNaN(money) || money <= 0) {
            wx.showToast({
              title: '请输入有效金额',
              icon: 'none'
            })
            return
          }

          wx.request({
            url: 'http://113.44.75.241:8080/budget/add',
            method: 'POST',
            data: {
              eveID: this.data.eventId,
              money: money
            },
            success: (res) => {
              if (res.data.code === 1) {
                this.fetchData()
                wx.showToast({ title: '添加成功' })
              }
            }
          })
        }
      }
    })
  },


  handleAddExpense() {
    wx.navigateTo({
      url: './editExpense/editExpense?eventId=' + this.data.eventId
    })
  },

  handleEditExpense(e) {
    const { id } = e.currentTarget.dataset
    const expense = this.data.expenses.find(e => e.id === id)
    if (expense) {
      // 将完整的expense对象序列化后传递
      const expenseStr = JSON.stringify(expense)
      wx.navigateTo({
        url: `./editExpense/editExpense?eventId=${this.data.eventId}&expense=${expenseStr}`
      })
    }
  },

  handleDeleteExpense(e) {
    const { id } = e.currentTarget.dataset
    wx.showModal({
      title: '确认删除',
      content: '确定要删除此开销吗？',
      success: (res) => {
        if (res.confirm) {
          wx.request({
            url: `http://113.44.75.241:8080/expense/delete?id=${id}`,
            method: 'DELETE',
            success: (res) => {
              if (res.data.code === 1) {
                this.fetchData()
                wx.showToast({ title: '删除成功' })
              }
            }
          })
        }
      }
    })
  }
})