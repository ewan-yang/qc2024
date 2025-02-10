// 通用的crud，批量操作接口

import { request } from '../request';

/** 获取用户信息 */
export function fetchUnitTreeData(data = {}) {
  return request.post<Api.common>('relay-task-service/api/v1/unit/tree', data);
}

// 页面初始化
export function initPageAPI(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/user/page`, {
    ...params,
  });
}


// 新增
export function addUserApi(params) {
  return request.post<Api.common>(`/relay-task-service/api/v1/user`, {
    ...params,
  });
}

// 删除
export function delUserAPI(id) {
  return request.delete<Api.common>(`/relay-task-service/api/v1/user/${id}`, {});
}

// 批量删除
export function  delAllUserAPI(ids) {
  return request.delete<Api.common>(`/relay-task-service/api/v1/user/batch/${ids}`, {});
}

// 停用启用
export function stopUserAPI( id, status) {
  return request.put<Api.common>(`/relay-task-service/api/v1/user/${id}/status/${status}`);
}
// 修改
export function editUserAPI(params, id) {
  return request.put<Api.common>(`relay-task-service/api/v1/user/${id}`, { ...params });
}
