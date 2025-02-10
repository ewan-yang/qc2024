import { request } from '../request';

export function fetchAllOperation(data = {}) {
  return request.post<Api.common>('relay-task-service/api/v1/operation/list', data);
}
