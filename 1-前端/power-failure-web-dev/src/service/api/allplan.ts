import { request } from "../request";

// 停电计划列表查询
export function allPlanPageAPI(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/plan/page`, {
    ...params,desc:true,orderBy:'updateDate'
  });
}

export function allPlanEditAPI(params,id) {
  return request.put<Api.common>(
    `/relay-task-service/api/v1/plan/updatePlanMonth/${id}`,
    { ...params }
  );
}

// planId查询工程project
export function projectPageAPI(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/planItem/page`, {
    ...params,
  });
}

export function updatePlanItemApi(params, id) {
  return request.put<Api.common>(`/relay-task-service/api/v1/planItem/${id}`, {...params});
}

export function addPlanItemApi(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/planItem`, {
    ...params,
  });
}

export function delPlanItemAPI( id) {
  return request.delete<Api.common>(`/relay-task-service/api/v1/planItem/${id}`, {});
}

// 批量删除
export function delAllPlanItemAPI( arr) {
  return request.post<Api.common>(`/relay-task-service/api/v1/planItem/cancelByIdList`, arr);
}

export function getPlanItemAPI (id) {
  return request.get<Api.common>(`/relay-task-service/api/v1/planItem/${id}`);
}