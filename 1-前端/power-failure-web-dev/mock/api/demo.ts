import type { MockMethod } from 'vite-plugin-mock';
import { tableList } from '../model';

const apis: MockMethod[] = [
  {
    url: '/mock/api/v1/demo/page',
    method: 'post',
    response: (options: Service.MockOption): Service.MockServiceResult => {
      const { page = 1, pageSize = 10 } = options.query;
      const list = tableList(Number(pageSize));
      return {
        code: 0,
        message: 'ok',
        data: {
          page: Number(page),
          pageSize: Number(pageSize),
          pageCount: 60,
          list
        }
      };
    }
  }
];

export default apis;
