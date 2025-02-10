declare namespace RouterPage {
  /** 根路由 */
  type RootRouteKey = 'root';

  /** 未找到路由(捕获无效路径的路由) */
  type NotFoundRouteKey = 'not-found';

  /** 页面路由 */
  type RouteKey =
    | '403'
    | '404'
    | '500'
    | 'allplan'
    | 'baseDataMgmt'
    | 'baseDataMgmt_warningRule'
    | 'constant-page'
    | 'home'
    | 'login'
    | 'not-found'
    | 'notification'
    | 'statisticalSearch'
    | 'statisticalSearch_createDuration'
    | 'statisticalSearch_distributionDuration'
    | 'statisticalSearch_notificationChange'
    | 'statisticalSearch_powerOutage'
    | 'statisticalSearch_powerOutagePlan'
    | 'statisticalSearch_receiveDuration'
    | 'statisticalSearch_repeatedPowerOutage'
    | 'system'
    | 'system_dataMgmt'
    | 'system_functionMgmt'
    | 'system_functionMgmt_menuMgmt'
    | 'system_functionMgmt_operationMgmt'
    | 'system_functionMgmt_permissionMgmt'
    | 'system_functionMgmt_resourceMgmt'
    | 'system_orgMgmt'
    | 'system_orgMgmt_organizationMgmt'
    | 'system_orgMgmt_roleMgmt'
    | 'system_orgMgmt_userMgmt'
    | 'warningsearch'
    | 'warningsearch_deliverySuccessWn'
    | 'warningsearch_deliveryTimeoutWn'
    | 'warningsearch_planChangeWn'
    | 'warningsearch_repeatedPowerCutWn'
    | 'warningsearch_userRefusedWn';

  /** 最后一级路由(该级路有对应的vue文件) */
  type LastDegreeRouteKey = Extract<RouteKey, '403' | '404' | '500' | 'allplan' | 'baseDataMgmt_warningRule' | 'constant-page' | 'home' | 'login' | 'not-found' | 'notification' | 'statisticalSearch_createDuration' | 'statisticalSearch_distributionDuration' | 'statisticalSearch_notificationChange' | 'statisticalSearch_powerOutage' | 'statisticalSearch_powerOutagePlan' | 'statisticalSearch_receiveDuration' | 'statisticalSearch_repeatedPowerOutage' | 'system_dataMgmt' | 'system_functionMgmt_menuMgmt' | 'system_functionMgmt_operationMgmt' | 'system_functionMgmt_permissionMgmt' | 'system_functionMgmt_resourceMgmt' | 'system_orgMgmt_organizationMgmt' | 'system_orgMgmt_roleMgmt' | 'system_orgMgmt_userMgmt' | 'warningsearch_deliverySuccessWn' | 'warningsearch_deliveryTimeoutWn' | 'warningsearch_planChangeWn' | 'warningsearch_repeatedPowerCutWn' | 'warningsearch_userRefusedWn'>
}
