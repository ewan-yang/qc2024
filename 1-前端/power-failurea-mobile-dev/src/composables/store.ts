/** 存储全局响应变量 */
export const useGlobalState = createGlobalState(() =>
  useSessionStorage('App_Storage', {
    token: '',
    flag: false,
    ISCP_CONNECTED: false,
    ISCP_USED_PORT: '',
    ISCP_FAILD_VALUE: 0,
    tabberNum: 0,
    showTabber: false,
    name: '',
    password: '',
    engineersTeamId: '',
    authCode: '',
  }),
)
