import { listAPI } from '@/service/api/common';

import { request } from '../request';
export function getLovList(name,data) {
  return request.post<Api.common>(`/${name}/list`, { code: data});
}
export function endTaskApi(name, ids) {
  return request.put<Api.common>(`/${name}/endTask/${ids}`, {});
}
export function backCancelUserApi(name, id) {
  return request.put<Api.common>(`/${name}/${id}`);
}
export function cancelTaskApi(name,params){
  return request.put<Api.common>(`/${name}`, params);
}
export function revokeCancelTaskApi(name,params){
  return request.put<Api.common>(`/${name}`, params);
}

export function ableParamslistAPI(name, params) {
  return request.post<Api.common>(`/${name}/list`, params);
}
export function putSubmitAPI(name, params) {
  return request.put<Api.common>(`/${name}`, params);
}
export function printDataApi(name, params) {
  return request.post<Api.common>(`/${name}`, params);
}
// 重复停电弹窗（查所有停电计划）
export function repeatModalAllApi(params) {
  return request.post<Api.common>(
    `/relay-task-service/api/v1/taskRepeat/all`,
    params
  );
}
// 重复停电弹窗（查选定停电计划）
export function repeatModalTaskCodeApi(taskCode,params) {
  return request.post<Api.common>(
    `/relay-task-service/api/v1/taskRepeat/${taskCode}`,
    params
  );
}
// 关闭重复停电弹窗时调用的接口
export function repeatModalUpdateApi(params) {
  return request.post<Api.common>(
    `/relay-task-service/api/v1/taskRepeat/update`,
    params
  );
}