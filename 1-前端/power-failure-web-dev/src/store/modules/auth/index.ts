import { unref, nextTick } from 'vue';
import { defineStore } from 'pinia';
import { router } from '@/router';
import { fetchLogin, fetchSmsLogin } from '@/service';
import { useRouterPush } from '@/composables';
import { localStg } from '@/utils';
import { useTabStore } from '../tab';
import { useRouteStore } from '../route';
import { getToken, getUserInfo, clearAuthStorage } from './helpers';
interface AuthState {
  /** 用户信息 */
  userInfo: Auth.UserInfo;
  /** 用户token */
  token: string;
  /** 登录的加载状态 */
  loginLoading: boolean;
}

export const useAuthStore = defineStore('auth-store', {
  state: (): AuthState => ({
    userInfo: getUserInfo(),
    token: getToken(),
    loginLoading: false
  }),
  getters: {
    /** 是否登录 */
    isLogin(state) {
      return Boolean(state.token);
    }
  },
  actions: {
    /** 重置auth状态 */
    resetAuthStore() {
      const { toLogin } = useRouterPush(false);
      const { resetTabStore } = useTabStore();
      const { resetRouteStore } = useRouteStore();
      const route = unref(router.currentRoute);

      clearAuthStorage();
      this.$reset();

      if (route.meta.requiresAuth) {
        resetTabStore();
        resetRouteStore();
        toLogin();
      }

      nextTick(() => {
        resetTabStore();
        resetRouteStore();
        if (!this.isLogin) {
          toLogin();
        }
      });
    },
    /**
     * 处理登录后成功或失败的逻辑
     * @param backendToken - 返回的token
     */
    async handleActionAfterLogin(info: ApiAuth.info) {
      const route = useRouteStore();
      const { toLoginRedirect } = useRouterPush(false);

      const loginSuccess = await this.loginByToken(info);

      if (loginSuccess) {
        await route.initAuthRoute();
        // 跳转登录后的地址
        toLoginRedirect();

        // 登录成功弹出欢迎提示
        if (route.isInitAuthRoute) {
          window.$notification?.success({
            title: '登录成功!',
            content: `欢迎回来，${this.userInfo.name}!`,
            duration: 3000
          });
        }

        return;
      }

      // 不成功则重置状态
      this.resetAuthStore();
    },
    /**
     * 根据token进行登录
     * @param backendToken - 返回的token
     */
    async loginByToken(info: ApiAuth.info) {
      let successFlag = false;
      const token = info.info?.authc.credentials.token;

      // 先把token存储到缓存中(后面接口的请求头需要token)
      localStg.set('token', token);
      localStg.set('refreshToken', token);
      // console.log(info.info,'info.info')
      // 获取用户信息
      const { credentials } = info.info?.authc;
      if (credentials) {
        // 成功后把用户信息存储到缓存中
        localStg.set('userInfo', Object.assign(credentials, info.info?.authz));
        // 更新状态
        this.userInfo = credentials;
        this.token = token;

        successFlag = true;
      }

      return successFlag;
    },
    /**
     * 登录
     * @param userName - 用户名
     * @param password - 密码
     * @param verifyCode - 验证码
     * @param mobile - 手机号
     * @param msgVerifyCode - 短信验证码
     * @param from - 手机号登录时传入 'sms'
     */
    // eslint-disable-next-line max-params
    login(userName: string, password: string, verifyCode: string, mobile: string, msgVerifyCode: string, from: string) {
      return new Promise(async (resolve, reject) => {
        this.loginLoading = true;
        const { error, data } =
          from && from === 'sms'
            ? await fetchSmsLogin(mobile, msgVerifyCode)
            : await fetchLogin(userName, password, verifyCode);

        if (data) {
          await this.handleActionAfterLogin(data);
        }
        this.loginLoading = false;
        resolve({ error, data });
      });
    },

    /**
     * 更换用户权限(切换账号)
     * @param userRole
     */
    async updateUserRole(userRole: Auth.RoleType) {
      const { resetRouteStore, initAuthRoute } = useRouteStore();

      const accounts: Record<Auth.RoleType, { userName: string; password: string; verifyCode: string }> = {
        super: {
          userName: '',
          password: ''
        },
        admin: {
          userName: '',
          password: ''
        },
        user: {
          userName: '',
          password: ''
        }
      };
      const { userName, password } = accounts[userRole];
      const { data } = await fetchLogin(userName,password, verifyCode);
      if (data) {
        await this.loginByToken(data);
        resetRouteStore();
        initAuthRoute();
      }
    }
  }
});
