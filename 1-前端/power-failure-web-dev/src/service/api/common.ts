// 通用的crud，批量操作接口

import { request } from '../request';

// 列表查询
export function pageAPI(params, name) {
  return request.post<Api.common>(`/${name}/page`, { ...params });
}

export function editAPI(params, name, id) {
  return request.put<Api.common>(`/${name}/${id}`, { ...params });
}

export function delAPI(name, id) {
  return request.delete<Api.common>(`/${name}/${id}`, {});
}

export function detailAPI(name, id) {
  return request.get<Api.common>(`/${name}/${id}`);
}

export function statusAPI(name, id, status) {
  return request.put<Api.common>(`/${name}/${id}/status/${status}`);
}

export function addAPI(params: object, name: string) {
  return request.post<Api.common>(`/${name}`, { ...params });
}

export function batchDelAPI(name, ids) {
  return request.delete<Api.common>(`/${name}/batch/${ids}`, {});
}
export function batchDeleteAPI(name, ids) {
  return request.delete<Api.common>(`/${name}/batchDelete/${ids}`, {});
}
export function batchPutApi(name, params) {
  return request.put<Api.common>(`/${name}`, {...params});
}
export function batchPostApi(name, params) {
  return request.post<Api.common>(`/${name}`, { ...params });
}
export function listAPI(name) {
  return request.post<Api.common>(`/${name}/list`, { status: 1 });
}
export function getListAPI(name) {
  return request.get<Api.common>(`/${name}/list`);
}
export function downModalApi(url){
	return request.get(`/${url}`);
};
export function uploadApi(name,formData){
  return request.post<Api.common>(`/${name}`, formData);
}
