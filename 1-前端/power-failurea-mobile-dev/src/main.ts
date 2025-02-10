import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router'
import routes from 'virtual:generated-pages'
import '@unocss/reset/tailwind.css'
import './styles/main.css'
import 'uno.css'
import 'vant/es/toast/style'
import 'vant/es/dialog/style'
import 'vant/es/image-preview/style'
import { iscpCloseDirect, iscpMethod } from '../public/iscp/iscp-ext'
import { agentid, appid } from './utils/config'
import { getAuthCode } from './utils/auth'
import App from './App.vue'

console.log('wuan  test  fileSize')
const app = createApp(App)
const router = createRouter({
  history: createWebHashHistory(import.meta.env.VITE_BASE_URL),
  routes,
})
app.use(router)
if (import.meta.env.VITE_NODE_ENV === 'production') {
  import('../public/iscp/jweixin-1.2.0.js').then((module) => {
    // 模块加载成功后，可以使用 module 中导出的功能
    iscpGet()
  }).catch((err) => {
    console.error('Failed to load jweixin', err)
  })
}
app.mount('#app')

function iscpGet() {
  wx.ready(() => {
    console.log('wuan  test  fileSize')

    // window.setTimeout(function(){
    wx.invoke('agentConfig', {
      agentid, // 对应AppId 必填，企业应用的agentid
      corpid: appid, // 必填，企业微信的corpid
      timestamp: 1414587457, // 必填，生成签名的时间戳,int类型, 如 1539100800
      nonceStr: 'Wm3WZYTPz0wzccnW', // 必填，生成签名的随机串
      signature: '6a002281a4a5684d2318132575a05651da2f625d', // 必填，签名
    }, async (res) => {
      console.log('wx.invoke agentConfig:over', res)
      // 初始化
      await iscpMethod()
      await getAuthCode(router)
      window.addEventListener('beforeunload', (event) => {
        iscpCloseDirect()
        event.preventDefault()
      })
      if (res.err_msg !== 'agentConfig:ok') {

      }
    })
    // },2000)
  })
  wx.error((res) => {
    console.log('wx.error', res)
  })
  wx.config({
    beta: true, // 调用wx.invoke形式的接口值时，该值必须为true。
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: appid, // 必填，政务微信的cropID
    timestamp: '1414587457', // 必填，生成签名的时间戳
    nonceStr: 'Wm3WZYTPz0wzccnW', // 必填，生成签名的随机串
    signature: 'f08ab17ecd1db25652f81a0e518fd370c03cc335', // 必填，签名
    jsApiList: ['getAuthCode', 'ext_DeviceInfo_GetInfo', 'multiWindows_open', 'multiWindows_close', 'multiWindows_subscribe', 'multiWindows_publish', 'multiWindows_finish',
      'ext_ISCP_Init', 'ext_ISCP_ConnectService', 'ext_ISCP_Close',
      'ext_ISCP_GetLocalPort', 'ext_ISCP_AnalyseUrl', 'ext_ISCP_Status', 'ext_ISCP_ResetConnect',
      'openEnterpriseApp', 'ext_SGMap_init', 'ext_SGMap_Operation', 'ext_Popover_Open', 'ext_Popover_Close', 'ext_DataCache_Get', 'ext_DataCache_Save',
      'ext_WeMeet_Join', 'ext_WeMeet_Login', 'ext_WeMeet_Init', 'selectEnterpriseContact', 'openChatWithMsg', 'changeNaviColor', 'getLocalResPath', 'shareWechatMessage', 'shareAppMessage', 'ext_wxlog_setLogOpen',
      'ext_wxlog_getLogFile', 'scanQRCode', 'chooseImage', 'previewImage', 'openLocation', 'ext_Net_Upload', 'ext_Net_CancelUpload', 'ext_Net_Download', 'onHistoryBack',
      'ext_Etas_List', 'ext_Etas_Init', 'ext_Etas_Reg', 'ext_Etas_UnReg', 'ext_Etas_checkStatus', 'ext_Etas_Verify', 'ext_Etas_SaveData', 'ext_Etas_GetData', 'previewFile', 'ext_SGMap_Location', 'startRecord',
      'translateVoice', 'stopRecord', 'ocr', 'chooseVideo', 'getLocalImgData', 'getZipAppDirectory'], // 必填，需要使用的JS接口列表
  })
}
