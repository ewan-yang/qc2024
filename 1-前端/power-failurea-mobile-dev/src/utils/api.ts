/** 储存 api 请求地址 */
export const Api = {
  isHealth:'relay-task-out/api/v1/is_health',
  /** 登陆 */
  iscLogin: 'api/v1/access/igwLogin',
  /** 登陆 */
  login: 'api/v1/access/login',
  /** 停电任务列表 */
  taskPage: 'api/v1/task/page',
  /** 停电任务详情 */
  taskDetail: 'api/v1/task/',
  /** 停电任务用户详情 */
  taskUserDetail: 'api/v1/taskUser/',
  /** 字典 */
  lovList: 'api/v1/lov/list',
  /** 停电用户列表 */
  taskUserPage: 'api/v1/taskUser/page',
  /** 停电用户List */
  taskUserList: 'api/v1/taskUser/list',
  /** 停电任务首页通知列表 */
  taskNoticePage: 'api/v1/taskNotice/page',
  /** 移动端首页统计数量 */
  statisticsTotal: 'api/v1/taskUser/total',
  /** 派发接口 */
  taskUserBatchAssign: 'api/v1/taskUser/batchAssign',
  /** 反馈接口 */
  taskUserFeedbackLog: 'api/v1/taskUserFeedbackLog',
  /** 反馈记录接口 */
  taskUserFeedbackLogList: 'api/v1/taskUserFeedbackLog/list/',
  /** 反馈上传图片 */
  taskUserFeedbackUpload: 'api/v1/taskUserFeedbackLog/batchUpload',
  /** 工程队List */
  engineersTeamList: 'api/v1/engineersTeam/list',
  /** 取消派发 */
  cancelTaskUser: 'api/v1/taskUser/cancel',
  /** 恢复派发 */
  recoverCancelTaskUser: 'api/v1/taskUser/recoverCancel',
  /** 预警 风险统计数量 */
  advanceNoticeTotal: 'api/v1/taskNotice/selectAlarmTotal',
  /** 预警 风险业务数据 */
  selectAlarmBusinessList: 'api/v1/taskNotice/selectAlarmBusinessList',
  /** 停电计划详情 */
  taskPlanDetail: 'api/v1/plan/',
  taskPlanItemDetail: 'api/v1/planItem/',
  // 首页停电通知
  powerCutNotice:'api/v1/task/total',
  // 首页数据汇总
  dataTotal:'api/v1/task/sumTotal',
  // 工程队首页数据汇总
  engineersSummary:'api/v1/taskUser/engineersSummary',
}
