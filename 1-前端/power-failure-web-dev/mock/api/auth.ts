import type { MockMethod } from 'vite-plugin-mock';
import { userModel } from '../model';

/** 参数错误的状态码 */
const ERROR_PARAM_CODE = 10000;

const ERROR_PARAM_MSG = '参数校验失败！';

const apis: MockMethod[] = [
  // 获取验证码
  {
    url: '/mock/getSmsCode',
    method: 'post',
    response: (): Service.MockServiceResult<boolean> => {
      return {
        code: 200,
        message: 'ok',
        data: true
      };
    }
  },
  // 用户+密码 登录
  {
    url: '/mock/access/login',
    method: 'post',
    response: (): Service.MockServiceResult<ApiAuth.info | null> => {
      return {
        code: 0,
        data: {
          info: {
            authc: {
              principal: {
                name: '管理员',
                id: '1',
                code: 'admin',
                token:
                  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjczNDY0MjMxfQ.qu6oLjlJww16v-YdTVs1ECY8soBvpO99FP1cw96g7F3O5VxP-ZSEwzJLItild_laeXQ1cOfWW4kCyA55DonpzA'
              },
              credentials: {
                name: '管理员',
                id: '1',
                code: 'admin',
                token:
                  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjczNDY0MjMxfQ.qu6oLjlJww16v-YdTVs1ECY8soBvpO99FP1cw96g7F3O5VxP-ZSEwzJLItild_laeXQ1cOfWW4kCyA55DonpzA'
              }
            },
            authz: {
              permissions: [
                'unit:view',
                'unit:add',
                'unit:update',
                'unit:delete',
                'user:view',
                'user:add',
                'user:update',
                'user:delete',
                'role:view',
                'role:add',
                'role:update',
                'role:delete',
                'menu:view',
                'menu:add',
                'menu:update',
                'menu:delete',
                'resource:view',
                'resource:add',
                'resource:update',
                'resource:delete',
                'operation:view',
                'operation:add',
                'operation:update',
                'operation:delete',
                'permission:view',
                'permission:add',
                'permission:update',
                'permission:delete',
                'lov:view',
                'lov:add',
                'lov:update',
                'lov:delete',
                'lovLine:view',
                'lovLine:add',
                'lovLine:update',
                'lovLine:delete'
              ],
              roles: ['ROLE_admin']
            }
          }
        },
        message: 'OK',
        success: true
      };
    }
  },
  // 获取用户信息(请求头携带token, 根据token获取用户信息)
  {
    url: '/mock/getUserInfo',
    method: 'get',
    response: (options: Service.MockOption): Service.MockServiceResult<ApiAuth.UserInfo | null> => {
      // 这里的mock插件得到的字段是authorization, 前端传递的是Authorization字段
      const { authorization = '' } = options.headers;
      const REFRESH_TOKEN_CODE = 66666;

      if (!authorization) {
        return {
          code: REFRESH_TOKEN_CODE,
          message: '用户已失效或不存在！',
          data: null
        };
      }
      const userInfo: Auth.UserInfo = {
        id: '',
        name: '',
        code: 'user'
      };
      const isInUser = userModel.some(item => {
        const flag = item.token === authorization;
        if (flag) {
          const { id: itemUserId, name, code } = item;
          Object.assign(userInfo, { id: itemUserId, name, code });
        }
        return flag;
      });

      if (isInUser) {
        return {
          code: 200,
          message: 'ok',
          data: userInfo
        };
      }

      return {
        code: REFRESH_TOKEN_CODE,
        message: '用户信息异常！',
        data: null
      };
    }
  },
  {
    url: '/mock/updateToken',
    method: 'post',
    response: (options: Service.MockOption): Service.MockServiceResult<ApiAuth.info | null> => {
      const { refreshToken = '' } = options.body;

      const findItem = userModel.find(item => item.refreshToken === refreshToken);

      if (findItem) {
        return {
          code: 200,
          message: 'ok',
          data: {
            token: findItem.token,
            refreshToken: findItem.refreshToken
          }
        };
      }
      return {
        code: 3000,
        message: '用户已失效或不存在！',
        data: null
      };
    }
  }
];

export default apis;
