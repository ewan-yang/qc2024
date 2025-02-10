// 根据环境变量判断当前环境
const isProd = process.env.NODE_ENV === 'production'

// 环境配置
interface EnvConfig {
  agentid: string
  appid: string
  authorizeUrl: string
  serviceTicketUrl: string
}

const prodConfig: EnvConfig = {
  agentid: '1003177',
  appid: 'ww4d11a39991ebffdc',
  authorizeUrl: 'https://igw.sgcc.com.cn/connect/oauth2/authorize?',
  serviceTicketUrl: 'https://id.sgcc.com.cn:10443/igwmobile/proxy/getUserByIgwCode'
}

const uatConfig: EnvConfig = {
  agentid: '1009595',
  appid: 'ww445f8033443a14aa',
  authorizeUrl: 'https://igw.isgcc.net:18081/connect/oauth2/authorize?',
  serviceTicketUrl: 'https://igw.isgcc.net:18443/proxy/getUserByIgwCode'
}

// 导出当前环境的配置
export const { agentid, appid, authorizeUrl, serviceTicketUrl } = isProd ? prodConfig : uatConfig