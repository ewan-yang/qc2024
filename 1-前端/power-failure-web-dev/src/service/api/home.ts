import { request } from "../request";

// 预告警信息查询
export function selectAlarmBusinessListAPI(params) {
  return request.post<Api.common>(
    `/analysis-alarm-service/api/v1/taskAdvanceNotice/selectAlarmBusinessList`,
    { ...params }
  );
}

export function planContentAPI(params) {
  return request.post<Api.common>(
    `/relay-task-service/api/v1/taskNotice/page`,
    { condition: { title: params } }
  );
}
// 首页 原来停电通知  现在叫停电计划统计
export function receiptNoticeStatisticsAPI(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/taskUser/total`, {
    ...params,
  });
}
// 首页 原来停电通知  现在叫停电计划统计
export function notificationStatisticsAPI(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/task/total`, {
    ...params,
  });
}
export function planStatisticsAPI(params) {
  return request.post<Api.common>(
    `/relay-task-service/api/v1/plan/homePageStatistics`,
    {
      ...params,
    }
  );
}

export function powerCutPageAPI(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/task/page`, {
    ...params,
  });
}
