import { request } from '../request';

export function fetchAllResource(data = {}) {
  return request.post<Api.common>('relay-task-service/api/v1/resource/list', data);
}
