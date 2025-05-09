Page({
  data: {
    nodes: [],
    loading: true
  },
  onLoad(options) {
    const { itiID } = options;
    this.fetchIntelligence(itiID);
  },
  fetchIntelligence(itiID) {
    wx.request({
      url: `http://113.44.75.241:8080/itinerary/getintrec?id=${itiID}`,
      success: (res) => {
        if (res.data.code === 1) {
          this.setData({
            nodes: [{
              name: 'div',
              children: res.data.data.split('\n').map(line => {
                let className = '';
                
                if (line.match(/^\d+\./)) className = 'day-title';
                else if (line.match(/^-\s+(上午|下午|晚上):/)) className = 'period-title';
                else if (line.match(/^-\s+\d{2}:\d{2}/)) className = 'time-entry';
                else if (line.startsWith('-')) className = 'period-title';
                
                return {
                  name: 'div',
                  attrs: {
                    class: className
                  },
                  children: [{
                    type: 'text',
                    text: line.trim()
                  }]
                };
              })
            }],
            loading: false
          });
        }
      }
    });
  }
 });