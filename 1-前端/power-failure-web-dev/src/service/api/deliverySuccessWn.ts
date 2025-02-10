import { request } from "../request";

// 列表查询
// 规则清单表格分页查询
export function deliverySuccessWnPageAPI(params) {
  return request.post<Api.common>(`/analysis-alarm-service/api/v1/taskAdvanceNotice/searchCarrierReleasePageList`,{ ...params });
}
