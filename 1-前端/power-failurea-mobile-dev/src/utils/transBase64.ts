/**
 * 使用xhr请求图片,并设置返回的文件类型为Blob对象[xhr.responseType = “blob”],
 * 使用FileReader 对象接收blob。
 * @imgUrl 图片地址
 */
import { useGlobalState } from '~/composables/store'

const state = useGlobalState()

function imageToBase64(imgUrl: string | URL) {
  return new Promise((resolve, reject) => {
    if (!String(imgUrl).trim())
      reject(new Error('URL为空'))
    window.URL = window.URL || window.webkitURL
    const xhr = new XMLHttpRequest()
    xhr.responseType = 'blob'
    xhr.open('get', imgUrl, true)
    // 携带请求头
    xhr.setRequestHeader(
      'Authorization',
      `Bearer ${state.value.token}`,
    )
    xhr.onload = function () {
      if (this.status === 200) {
        // 得到一个blob对象
        const blob = this.response
        // 至关重要
        const oFileReader = new FileReader()
        oFileReader.onloadend = function (e) {
          // 此处拿到的已经是 base64的图片了
          const base64 = e.target.result
          resolve(`${base64}`)
        }
        oFileReader.readAsDataURL(blob)
      }
      else {
        // 解析错误
        reject(new Error('解析错误'))
      }
    }
    // 异常处理
    xhr.onerror = function () {
      resolve(false)
    }
    xhr.send()
  })
}

export async function getBase64(imgUrl: string) {
  const URL = imgUrl.replace('/innovation/resource', `http://127.0.0.1:${state.value.ISCP_USED_PORT}/tdglxt/tdglxt.relay-task-service`)
  // const URL = imgUrl
  return await imageToBase64(URL)
}
