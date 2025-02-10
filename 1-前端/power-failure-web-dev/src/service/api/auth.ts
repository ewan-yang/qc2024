import { sha1 } from '~/src/utils/crypto';
import { getServiceEnvConfig } from '~/.env-config';
import { mockRequest, request } from '../request';

/**
 * 注册
 */
export function register(password: string, mobile: string, verifyCode: string) {
  return request.post<ApiAuth.info>('relay-task-service/api/v1/access/register', {
    password: sha1(password),
    mobile,
    verifyCode
  });
}

/**
 * 密码重置
 */
export function resetPassword(mobile: string, password: string, verifyCode: string) {
  return request.put<ApiAuth.info>('relay-task-service/api/v1/access/resetPwd', {
    password: sha1(password),
    mobile,
    verifyCode
  });
}

/**
 * 重置密码
 */
 export function newResetPassword(code: string, oldPass: string, newPass: string) {
  return request.post<ApiAuth.info>('relay-task-service/api/v1/user/updatePassword', {
    code: code,
    oldPass: oldPass,
    newPass: newPass,
  });
}
/**
 * 获取短信验证码
 * @param phone - 手机号
 * @param pwdVerifyCode - 1表示用户注册/重置密码
 */
export function fetchSmsCode(phone, pwdVerifyCode) {
  return request.get<any>(`relay-task-service/api/v1/access/msgVerifyCode?mobile=${phone}&pwdVerifyCode=${pwdVerifyCode}`);
}
/**
 * 获取验证码
 */
export function fetchCode(): string {
  const config = getServiceEnvConfig(import.meta.env);
  return `${config.url}/relay-task-service/api/v1/access/kaptcha?${Math.random() * 100}`;
}

/**
 * 登录
 * @param userName - 用户名
 * @param password - 密码
 * @param mobile - 手机号
 * @param msgVerifyCode - 短信验证码
 */
export function fetchLogin(userName: string, password: string, verifyCode: string) {
  return request.post<ApiAuth.info>('/relay-task-service/api/v1/access/login', {
    name: userName,
    password: password,
    verifyCode
  });
}
/**
 * 手机号验证登录
 */
export function fetchSmsLogin(mobile: string, msgVerifyCode: string) {
  return request.post<ApiAuth.info>('relay-task-service/api/v1/access/login', {
    mobile,
    msgVerifyCode
  });
}

/** 获取用户信息 */
export function fetchUserInfo() {
  return mockRequest.get<ApiAuth.UserInfo>('relay-task-service/api/v1/getUserInfo');
}

/**
 * 获取用户路由数据
 * @description 后端根据用户id查询到对应的角色类型，并将路由筛选出对应角色的路由数据返回前端
 */
export function fetchUserRoutes(userId: string) {
  return mockRequest.post<ApiRoute.Route>('relay-task-service/api/v1/getUserRoutes', { userId });
}

/**
 * 获取路由数据
 * @description 路由筛选出对应角色的路由数据返回前端
 */
export function fetchRoutes() {
  return request.get<ApiRoute.Route>('/relay-task-service/api/v1/menu/format');
}

/**
 * 刷新token
 * @param refreshToken
 */
export function fetchUpdateToken(refreshToken: string) {
  return mockRequest.post<ApiAuth.info>('/updateToken', { refreshToken });
}
