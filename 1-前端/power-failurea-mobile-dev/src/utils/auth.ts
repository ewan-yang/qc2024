
import { Api } from './api'
import { agentid, appid, authorizeUrl, serviceTicketUrl } from './config'
import CryptoJS from 'crypto-js'
const state = useGlobalState()

export function hasAuthorizeCode() {
  return new URLSearchParams(location.search).get('code') != null
}

export function getNewQgwServiceTicket(code:any) {
  const data = {
    CODE: code,
    appId: agentid,
  }
  return postData(serviceTicketUrl, data)
}

function postData(url: RequestInfo | URL, data: { CODE: string | null; appId: string }) {
  return fetch(url, {
    body: JSON.stringify(data),
    cache: 'no-cache',
    headers: {
      'content-type': 'application/json;charset=utf-8',
    },
    method: 'POST',
    mode: 'cors',
  }).then(response =>
    response.json(),
  )
}
export function getAuthCode(router:any) {
  wx.invoke('getAuthCode', {
    response_type: 'code',//固定参数
    scope: 'snsapi_base'//固定参数
  }, function (res) {
    //app_log(JSON.stringify(res))调试日志输出
    console.log('js获取code:', res)
    getNewQgwServiceTicket(res.code).then((res) => {
      console.log('-.token:', res)
      if (res.code === '100001') {
        const params = {
          serviceTicket: res.data,
          Client: '11111111'
        }
          const { data, execute } = useGet(Api.iscLogin, params)
          execute().then(() => {
            console.log('iscLogin data:', data)
            state.value.token = data.value.data.info?.authc?.principal?.token
            state.value.flag = true
            state.value.engineersTeamId = data.value.data.info?.authc?.principal?.engineersTeamId
            state.value.showTabber = true
            router.replace({ path: '/indexTabber' })
          });
      }
    })
  })
}
export function getWxCode() {
  let sURL = authorizeUrl
  sURL += `appid=${appid}`
  sURL += `&redirect_uri=${encodeURIComponent('zipapp://local.host/index.html')}`
  sURL += '&response_type=code'
  // sURL += '&scope=snsapi_base'
  sURL += '&scope=SCOPE'
  sURL += '&state=STATE'
  sURL += `&agentid=${agentid}`
  sURL += '#wechat_redirect'
  location.href = sURL
}
// igw生产环境获取token
export function getToken(router:any) {
  return new Promise(async (resolve, reject) => {
    await getAuthCode(router)
  })
}
export function getNoProToken(router:any){
  // const { data, execute } = usePost(Api.login, {
  //   name: state.value.name,
  //   password:state.value.password
  // });
  const { data, execute } = usePost(Api.login, {name:"admin",password:CryptoJS.SHA1('Aa123456').toString()});

  execute().then(() => {
      state.value.token = data.value.data.info?.authc?.principal?.token
      state.value.flag = true
      state.value.engineersTeamId = data.value.data.info?.authc?.principal?.engineersTeamId
    //   useStorage('_token', data.value.data.info?.authc?.principal?.token)
    //   useStorage('_username', data.value.data.info?.authc?.principal?.name)
    //   useStorage('_createBy', data.value.data.info?.authc?.principal?.id)
    // useStorage('_discussion', data.value.data.info?.authz.permissions)
            state.value.showTabber = true

      router.replace({ path: '/indexTabber' })
    });
}
export function temporaryLoginApi(name:string,password:string,router:any){
  const { data, execute } = usePost(Api.login, {
    name: name,
    password:password
  });
  execute().then(() => {
  state.value.name = name
  state.value.password = password
    state.value.token = data.value.data.info?.authc?.principal?.token
    state.value.flag = true
    state.value.engineersTeamId = data.value.data.info?.authc?.principal?.engineersTeamId
    // useStorage('_token', data.value.data.info?.authc?.principal?.token)
    // useStorage('_username', data.value.data.info?.authc?.principal?.name)
    // useStorage('_createBy', data.value.data.info?.authc?.principal?.id)
    // useStorage('_discussion',data.value.data.info?.authz.permissions)
            state.value.showTabber = true

    router.push({ path: '/indexTabber' })
  });
}


