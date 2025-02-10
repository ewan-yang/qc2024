import { request } from '../request';

// 列表查询
// 规则清单表格分页查询
export function warningRulePageAPI(params) {
  return request.post<Api.common>(`/analysis-alarm-service/api/v1/taskAdvanceRule/page`, { ...params });
}

export function  warningRuleEditAPI(params, id) {
  return request.put<Api.common>(`/analysis-alarm-service/api/v1/taskAdvanceRule/${id}`, { ...params });
}

export function  warningRuleDelAPI(id) {
  return request.delete<Api.common>(`/analysis-alarm-service/api/v1/taskAdvanceRule/${id}`, {});
}

export function  warningRuleAddAPI(params: object) {
  return request.post<Api.common>(`/analysis-alarm-service/api/v1/taskAdvanceRule`, { ...params });
}
