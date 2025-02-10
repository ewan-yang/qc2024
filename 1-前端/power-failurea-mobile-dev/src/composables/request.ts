import type { LocationQueryRaw } from 'vue-router'
import { stringifyQuery } from 'vue-router'
import type { MaybeRef, UseFetchReturn } from '@vueuse/core'
import { createFetch, isObject,useFetch  } from '@vueuse/core'
import { showToast,showDialog } from 'vant'
import { useGlobalState } from './store'
import { iscpDelay,iscpGetLocalPort } from '../../public/iscp/iscp-ext'
// import '../../public/iscp/jweixin-1.2.0'
const state = useGlobalState()
let baseUrl = import.meta.env.VITE_BASE_URL
const useRequest = createFetch({
  baseUrl,
  options: {
    immediate: false,
    timeout: 30000,
    // 在请求前修改配置，如：注入 token 值
    async beforeFetch({ url, options }) {
        if(window.ISCP_CONNECTED){
          state.value.ISCP_CONNECTED = window.ISCP_CONNECTED
        }
        if(window.ISCP_USED_PORT){
          state.value.ISCP_USED_PORT = window.ISCP_USED_PORT
        }
      if (!state.value.ISCP_CONNECTED) await iscpDelay()

      // url = `http://127.0.0.1:${state.value.ISCP_USED_PORT}/tdglxt/tdglxt.relay-task-service/${url}`

      if (import.meta.env.VITE_NODE_ENV === 'production') {
        if (!state.value.ISCP_USED_PORT) {
          await getLocalPort()
        }
        url = `http://127.0.0.1:${state.value.ISCP_USED_PORT}/${url}`
      }

      if (state.value.token) {
        options.headers = Object.assign(options.headers || {}, {
          'Authorization': `Bearer ${state.value.token}`,
          'Content-Type': 'application/json',
        })
        console.log(options.headers,'options.headers')
      }

      return { url,options }
    },
    // 在请求后处理数据，如：拦截错误、处理过期
    afterFetch({ data, response ,error}) {
      const status = data.code

      // NOTE: 拦截返回，需要根据具体返回修改
      if (status === 200) {
        data = data.data || {}
      }
      else if (status === 401) {
        data = null
      }
      else if (status === 1000) {
        showToast(data.message)
        data = null
      }
      else if (status) {
        // eslint-disable-next-line no-console
        console.log('出现未全局拦截错误')
        console.log(data,'出现未全局拦截错误data')
        showDialog({
          title: '提示',
          message: data.message,
        }).then(() => {
          // on close
        });
        data = undefined
      }
      // eslint-disable-next-line no-console
      import.meta.env.MODE === 'development' && console.log('result:', data)

      return { data, response }
    },
    onFetchError({ data, response,error, }) {
      console.log(response,'response')
      if(response.status == 401){
        state.value.token = ''
        state.value.flag = false
          showDialog({
            title: '提示',
            message: '登录已经过期',
          }).then(async () => {
              // close（关闭窗口）
              wx.invoke("multiWindows_close", {}, function (res) {
                // app_log(res);
              });
          });

      }else if(response.status == 403){
        state.value.token = ''
        state.value.flag = false
          showDialog({
            title: '提示',
            message: '权限不足',
          }).then(async () => {
              // close（关闭窗口）
              wx.invoke("multiWindows_close", {}, function (res) {
                // app_log(res);
              });
          });
      }

      data = undefined
      return { data, error }
    },
  },
  fetchOptions: {
    mode: 'cors',
    credentials: 'include',
  },
})

/**
 * 封装 get 请求
 * @param url 请求地址
 * @param query 请求参数
 */
export function useGet<T = unknown>(
  url: MaybeRef<string>,
  query?: MaybeRef<unknown>,
): UseFetchReturn<T> {
  const _url = computed(() => {
    const _url = unref(url)
    const _query = unref(query)

    const queryString = isObject(_query)
      ? stringifyQuery(_query as LocationQueryRaw)
      : ('')
    return `${_url}${queryString ? '?' : _query }${queryString}`
  })

  return useRequest<T>(_url).json()
}
/**
 * 封装 没有参数get 请求
 * @param url 请求地址
 * @param query 请求参数
 */
export function useNoParamsGet<T = unknown>(
  url: MaybeRef<string>,
): UseFetchReturn<T> {
  return useRequest<T>(url).json()
}
/**
 * 封装 post 请求
 * @param url 请求地址
 * @param payload 请求参数
 */
export function usePost<T = unknown>(
  url: MaybeRef<string>,
  payload?: MaybeRef<unknown>,
): UseFetchReturn<T> {
  return useRequest<T>(url).post(payload).json()
}


/**
 * 封装 put 请求
 * @param url 请求地址
 * @param payload 请求参数
 */
export function usePut<T = unknown>(
  url: MaybeRef<string>,
  payload?: MaybeRef<unknown>,
): UseFetchReturn<T> {
  return useRequest<T>(url).put(payload).json()
}

/**
 * 封装 delete 请求
 * @param url 请求地址
 * @param payload 请求参数
 */
export function useDelete<T = unknown>(
  url: MaybeRef<string>,
  payload?: MaybeRef<unknown>,
): UseFetchReturn<T> {
  return useRequest<T>(url).delete(payload).json()
}

/**
 * 封装获取Blob进行下载
 * @param url 请求地址
 */
export function useBlob(url: MaybeRef<string>): UseFetchReturn<Blob> {
  return useRequest(url).blob()
}

async function getLocalPort() {
  console.log('getLocalPortgetLocalPort')
  const port = await iscpGetLocalPort()
  console.log('== port  == port  ==')

  const isSuccess = port !== '-1'
  if (isSuccess) {
    state.value.ISCP_FAILD_VALUE = 0
    state.value.ISCP_USED_PORT = port
    baseUrl = `http://127.0.0.1:${state.value.ISCP_USED_PORT}/`
    return
  } else {
    setTimeout(() => {
      state.value.ISCP_FAILD_VALUE = state.value.ISCP_FAILD_VALUE + 1
      if (state.value.ISCP_FAILD_VALUE > 3) {
        showToast('ISCP端口获取失败')
        return
      }
      return getLocalPort()
    }, 500)
  }
}
