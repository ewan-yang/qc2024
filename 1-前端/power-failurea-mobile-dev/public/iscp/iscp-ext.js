const ISCP_IP = 'aqjh.sh.sgcc.com.cn';
const ISCP_PORT = '10080';
const ISCP_VALUE = `${ISCP_IP}:${ISCP_PORT}`;
const ISCP_APPID = 'tdtzgl';
const ISCP_USER = 'user';
import { agentid} from '../../src/utils/config'
export function iscpDelay() {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve(true);
      // reject(false);
    }, 512)
  })
}

export function iscpMethod() {
  wx.invoke("ext_ISCP_Init", {
    data: {
      "iscpIP": agentid
    }
  }, function (res) {
    console.log("ext_ISCP_Init end", res, res.err_msg);
    if (res.err_msg === 'ext_ISCP_Init:ok') {
      wx.invoke("ext_ISCP_ConnectService", {
        data: {
          "ip": ISCP_IP,
          "port": ISCP_PORT,
          "user": ISCP_USER,
          "appid": ISCP_APPID,
          "iscpIP": agentid
        }
      }, function (res) {
        console.log("ext_ISCP_ConnectService end", res, res.err_msg)
        if (res.err_msg === 'ext_ISCP_ConnectService:ok') {
          window.ISCP_CONNECTED = true;
          wx.invoke("ext_ISCP_GetLocalPort", {
            data: {
              "ip": "188.188.4.213",
              "port": "18081",
              "iscpIP": agentid,
            }
          }, function (response) {
            console.log('ext_ISCP_GetLocalPort end', JSON.stringify(response));
            if (response.err_msg === 'ext_ISCP_GetLocalPort:ok') {
              //成功处理
              console.log("端口号:", response.result);
              window.ISCP_USED_PORT = response.result
            } else {
              alert(res.err_msg)
            }
          })
        } else {
          alert(res.err_msg)
        }
      })
    } else {
      alert(res.err_msg)
    }
  })
}

// export function initiscp(callback, callbackError) {
//   return new Promise((resolve, reject) => {
//     wx.invoke("ext_ISCP_Init", {
//       data: {
//         "iscpIP": agentid
//       }
//     }, function (res) {
//       console.log("ext_ISCP_Init ", res, res.err_msg);
//       if (res.err_msg === 'ext_ISCP_Init:ok') {
//         resolve(true)
//         if (typeof (callback) == 'function') callback();
//       } else {
//         reject(false)
//         if (typeof (callbackError) == 'function') callbackError();
//       }
//     })
//   })
// }

// export function initiscpDirect(callback, callbackError) {
//   wx.invoke("ext_ISCP_Init", {
//     data: {
//       "iscpIP": agentid
//     }
//   }, function (res) {
//     console.log("ext_ISCP_Init ", res, res.err_msg);
//     if (res.err_msg === 'ext_ISCP_Init:ok') {
//       if (typeof (callback) == 'function') callback();
//     } else {
//       if (typeof (callbackError) == 'function') callbackError();
//     }
//   })
// }

// function iscpconnect() {
//     return new Promise((resolve, reject) => {
//         wx.invoke("ext_ISCP_ConnectService", {
//             data: {
//                 "iscpIP": "ksjhdfsalkjfsm1211",
//                 "ip": ISCP_IP,
//                 "port": ISCP_PORT,
//                 "user": ISCP_USER,
//                 "appid": ISCP_APPID,
//             }
//         }, function (res) {
//             console.log("ext_ISCP_ConnectService ", res, res.err_msg)
//             if (res.err_msg === 'ext_ISCP_ConnectService:ok') {
//                 resolve(true)
//                 uni.setStorageSync(
//                     "_wxConfig",
//                     true
//                 );
//             } else {
//                 reject()
//             }
//         })
//     })
// }


// export function iscpconnectDirect() {
//   wx.invoke("ext_ISCP_ConnectService", {
//     data: {
//       "ip": ISCP_IP,
//       "port": ISCP_PORT,
//       "user": ISCP_USER,
//       "appid": ISCP_APPID,
//       "iscpIP": agentid
//     }
//   }, function (res) {
//     console.log("ext_ISCP_ConnectService ", res, res.err_msg)
//     if (res.err_msg === 'ext_ISCP_ConnectService:ok') {
//       window.ISCP_CONNECTED = true;
//       if (typeof (callback) == 'function') callback();
//     } else {
//       if (typeof (callbackError) == 'function') callbackError();
//     }
//   })
// }

export function iscpGetLocalPort() {
  return new Promise((resolve, reject) => {
    wx.invoke("ext_ISCP_GetLocalPort", {
      data: {
        "ip": "188.188.4.213",
        "port": "18081",
        "iscpIP": agentid,
      }
    }, function (response) {
      // alert('response'+response)
      console.log('iscp:get local port ', JSON.stringify(response));
      if (response.err_msg === 'ext_ISCP_GetLocalPort:ok') {
        //成功处理
        console.log("端口号:", response.result);
        window.ISCP_USED_PORT = response.result
        resolve(response.result);
      }
      if (response.err_msg === 'ext_ISCP_GetLocalPort:fail') {
        //失败处理 console.log(response.result);
        reject();
      }
      if (response.err_msg === 'ext_ISCP_GetLocalPort:cancel') {
        //取消处理 console.log(response.result);
        reject();
      }
    })
  })
}

export function iscpClose() {
  return new Promise((resolve, reject) => {
    wx.invoke("ext_ISCP_Close", {
      data: {
        "iscpIP": agentid,
        "user": ISCP_USER,
        "appid": ISCP_APPID,
        "ip": ISCP_IP,
        "port": ISCP_PORT,
      }
    }, function (res) {
      console.log('iscp:close', JSON.stringify(res))
      if (res.error_msg === 'ext_ISCP_Close:ok') {
        //成功处理
        console.log(res.result);
        resolve(true)
      }
      if (res.error_msg === 'ext_ISCP_Close:fail') {
        //失败处理 console.log(res.result);
        reject();
      }
      if (res.error_msg === 'ext_ISCP_Close:cancel') {
        //取消处理 console.log(res.result);
        reject();
      }
    })
  })
}

export function iscpCloseDirect() {
  wx.invoke("ext_ISCP_Close", {
    data: {
      "iscpIP": agentid,
      "user": ISCP_USER,
      "appid": ISCP_APPID,
      "ip": ISCP_IP,
      "port": ISCP_PORT,
    }
  }, function (res) {
    console.log('iscp:close', JSON.stringify(res))
    if (res.err_msg === 'ext_ISCP_Close:ok') {
      //成功处理
      console.log(res.result);
    }
    if (res.err_msg === 'ext_ISCP_Close:fail') {
      //失败处理 console.log(res.result);
    }
    if (res.err_msg === 'ext_ISCP_Close:cancel') {
      //取消处理 console.log(res.result);
    }
  })
}


export function iscpStatus() {
  return new Promise((resolve, reject) => {
    wx.invoke("ext_ISCP_Status", {
      data: {
        "iscpIp": ISCP_VALUE
      }
    }, function (response) {
      console.log('iscp:status', JSON.stringify(response))
      if (response.error_msg === 'ext_ISCP_Status:ok') {
        //成功处理
        console.log(response.result);
        resolve(true)
      }
      if (response.error_msg === 'ext_ISCP_Status:fail') {
        //失败处理 console.log(response.result);
        reject();
      }
      if (response.error_msg === 'ext_ISCP_Status:cancel') {
        //取消处理 console.log(response.result);
        reject();
      }
    })
  })
}

export function checkJs(){
  console.log("checkJsApi start")
    wx.checkJsApi({
      jsApiList: ['chooseImage','scanQRCode'], // 需要检测的JS接口列表
      success: function(res) {
        console.log('2.相册接口是否有用',res)
        console.log('3.能否成功执行')
          // 以键值对的形式返回，可用的api值true，不可用为false
          // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
      },
      fail:function(res){
        console.log("2.",res)
      }
  });
}
// $(function () {
//   /**
//    * 此处使用异步加载引用 js-sdk，在页面中直接引用的话，
//    * 有些方法找不到，用异步方法就可以调用
//    */
//   $.getScript("./iscp/jweixin-1.2.0.js", function () {
//     'use strict';
//       wx.ready(function () {
//         console.log('wx.config: ready 111111111')
//         window.setTimeout(function(){
//           wx.invoke("agentConfig", {
//             agentid: '1003177', // 对应AppId 必填，企业应用的agentid
//             corpid: 'ww4d11a39991ebffdc',  // 必填，企业微信的corpid
//               timestamp: 1414587457, // 必填，生成签名的时间戳,int类型, 如 1539100800
//               nonceStr: 'Wm3WZYTPz0wzccnW', // 必填，生成签名的随机串
//               signature: '6a002281a4a5684d2318132575a05651da2f625d',// 必填，签名
//           }, async function (res) {
//             console.log('wx.invoke agentConfig:over',res)
//               //初始化
//             await initiscpDirect(iscpconnectDirect)
//             window.addEventListener("beforeunload", function (event) {
//               iscpCloseDirect()
//               event.preventDefault();
//             });
//               if (res.err_msg !== "agentConfig:ok") {
//                   return;
//               }
//           });
//         },2000)

//       })
//       wx.error(function (res){
//         console.log('wx.error',res)
//       })
//       wx.config({
//           beta: true,// 调用wx.invoke形式的接口值时，该值必须为true。
//           debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
//         appId: 'ww4d11a39991ebffdc', // 必填，政务微信的cropID
//           timestamp: '1414587457', // 必填，生成签名的时间戳
//           nonceStr: 'Wm3WZYTPz0wzccnW', // 必填，生成签名的随机串
//           signature: 'f08ab17ecd1db25652f81a0e518fd370c03cc335',// 必填，签名
//           jsApiList: ['multiWindows_open', 'multiWindows_close', 'multiWindows_subscribe', 'multiWindows_publish', 'multiWindows_finish',
//               'ext_ISCP_Init', 'ext_ISCP_ConnectService', 'ext_ISCP_Close',
//               'ext_ISCP_GetLocalPort', 'ext_ISCP_AnalyseUrl', 'ext_ISCP_Status', 'ext_ISCP_ResetConnect',
//               'openEnterpriseApp', 'ext_SGMap_init', 'ext_SGMap_Operation', 'ext_Popover_Open', 'ext_Popover_Close', 'ext_DataCache_Get', 'ext_DataCache_Save',
//               'ext_WeMeet_Join', 'ext_WeMeet_Login', 'ext_WeMeet_Init', 'selectEnterpriseContact', 'openChatWithMsg', 'changeNaviColor', 'getLocalResPath', 'shareWechatMessage', 'shareAppMessage', 'ext_wxlog_setLogOpen',
//               'ext_wxlog_getLogFile', 'scanQRCode', 'chooseImage', 'previewImage', 'openLocation', 'ext_Net_Upload', 'ext_Net_CancelUpload', 'ext_Net_Download', 'onHistoryBack',
//               'ext_Etas_List', 'ext_Etas_Init', 'ext_Etas_Reg', 'ext_Etas_UnReg', 'ext_Etas_checkStatus', 'ext_Etas_Verify', 'ext_Etas_SaveData', 'ext_Etas_GetData', 'previewFile', 'ext_SGMap_Location', 'startRecord',
//               'translateVoice', 'stopRecord', 'ocr', 'chooseVideo', 'getLocalImgData', 'getZipAppDirectory'] // 必填，需要使用的JS接口列表
//       });
//   });
// })
