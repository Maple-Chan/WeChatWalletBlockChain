// pages/maplewallet/maplewallet.js

const datecurrent = new Date();
const yearcur = datecurrent.getFullYear();
const monthcur = datecurrent.getMonth();
const datecur = datecurrent.getDate();
// const visitUrl = 'https://127.0.0.1:443/';
const visitUrl = 'https://maplestory.work/'
var addzero = "";
if( monthcur < 9){
  addzero = "0";
}

const currentDateString = yearcur + "-" + addzero + (monthcur + 1) + "-" + datecur
Page({

  /**
   * 页面的初始数据
   */
  data: {
    items: [] ,// 数据列表
    curMoney: '',
    hiddenincomes :true,

    //有客户选择转账类型的数据
    typeindex: 1,
    typeArray: ['incomes','outcomes'],

    //发送往服务器的数据
    id: '',
    money: '',
    time:'',
    type: '',
    description:'',

    //客户选择时间的数据
    selectdate: currentDateString,
    enddata: datecurrent,


  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var main = this
    console.log("on load maple's wallet page")
    
    wx.request({
      // url: 'http://106.54.115.151:8080/queryAllByID',
      url: visitUrl+'queryAllByID',
      method: 'GET',
      header: {
        'content-type': 'application/json'
      },
      success: function (res){
        console.log("get ALL Data Succeed!")
        console.log(res.data);
        var list = res.data;
        console.log(list);
        // console.log(list.money)
        main.setData({
          items: list
        })
      }
    })

    wx.request({
      // url: 'http://106.54.115.151:8080/query',
      url: visitUrl+'query',
      method: 'GET',
      header: {
        'content-type': 'application/json'
      },
      success: function(res){
        console.log("get Latest Data Succeed")
        console.log(res.data)
        var recent = res.data.currentMoney

        main.setData({
          curMoney: recent
        })
      }
    })

    

  },

  /* 自定义按钮事件 */
  /* ------------------提交转入 -------------------*/
  submitIncomes: function(){
    console.log("click submit incomes")
    this.setData({
      hiddenincomes: !this.data.hiddenincomes
    })
    
  },
  //取消按钮
  cancelIncomes: function () {
    console.log("click cancel incomes")
    this.setData({
      hiddenincomes: true
    });
  },

  //确认
  confirmIncomes: function () {
    console.log("click confirm incomes")
    var that = this
    this.setData({
      hiddenincomes: true, // 控制是否显示modal
      time: that.data.selectdate,
      type: that.data.typeArray[that.data.typeindex],

    })
    console.log("time:", that.data.time,
      " type:", that.data.type,
      " id:", that.data.id,
      " money:", that.data.money,
      " description", that.data.description
    )

    wx.request({
      // url: 'http://106.54.115.151:8080/submit',
      url: visitUrl+'submit',
      method: "GET",
      header: {
        'content-type': 'application/json'
      },
      data: {
        'typeTransaction': that.data.type,
        'identity': that.data.id,
        'money': that.data.money,
        'time': that.data.time,
        'description': that.data.description,
        /*需要继续补充所有参数*/
      },
      success:function(res){
        console.log("提交方法回调函数：" + res.data);
        var resultData = res.data;
        if( resultData == true){
          wx.showToast({
            title: '提交成功',
            duration: 2000,
          })
        }else{
          wx.showToast({
            title: '提交失败',
            duration: 2000,
          })
        }
      }
    })

    //提交成功后的刷新
    that.onLoad()
  },
  /* ----------------- 提交部分结束 -----------------*/
  //选择类型
  bindTypePickerChange: function(e) {
    console.log('picker Type 发送选择改变，携带值为', e.detail.value)
    this.setData({
      typeindex: e.detail.value,
    })
  },
  //选择时间
  bindDatePickerChange: function (e) {
    console.log('picker Time 发送选择改变，携带值为', e.detail.value)
    this.setData({
      selectdate: e.detail.value
    })
  },


  idInput: function(e){
    this.setData({
      id: e.detail.value
    })
  },
  moneyInput: function (e) {
    this.setData({
      money: e.detail.value
    })
  },
  descriptionInput: function (e) {
    this.setData({
      description: e.detail.value
    })
  },



  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})