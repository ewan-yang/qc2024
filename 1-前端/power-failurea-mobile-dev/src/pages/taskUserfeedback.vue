<script setup>
import { showDialog, showToast } from 'vant'
import { useFetch } from '@vueuse/core'
import { Api } from '../utils/api'
// import { showImagePreview } from 'vant';
import { getBase64 } from '~/utils/transBase64'
// import '../../public/iscp/jweixin-1.2.0';
defineOptions({
  name: 'TaskUserfeedback',
})
const env = ref(import.meta.env.VITE_NODE_ENV)
const router = useRouter()
const route = useRoute()
const state = useGlobalState()
const endTask = ref(false)

const fileList = ref([])
const fileListUpload = ref([])
const initFileListUpload = ref([])
const uploadMobileImg = ref({})
const imgLoading = ref(false)
const showPicker = ref(false)
const showDeliveryMethodPicker = ref(false)
const showFeedbackStatusPicker = ref(false)
const showHaveBackupPhonePicker = ref(false)
const showPhotoActionSheet = ref(false)
const canAbleInput = ref(false)
const formData = ref({})
const detailData = ref({})
const deliveryMethodColumns = [
  { text: '送达现场', value: 1 },
  { text: '微信通知', value: 2 },
  { text: '营销通知', value: 3 },
]
const feedbackStatusColumns = [
  { text: '同意', value: '320' },
  { text: '拒签', value: '330' },
]
const cancelFeedbackStatusColumns = [
  { text: '同意', value: '520' },
]
const haveBackupPhoneColumns = [
  { text: '是', value: '是' },
  { text: '否', value: '否' },
]
const showFilter = ref(false)
onMounted(async () => {
  init()
  initImg()
})
async function initImg() {
  const { data, execute } = useGet(Api.taskUserFeedbackLogList, route.query.taskUserId)
  execute().then(async() => {
    console.log(data.value?.data.filter(item => item.feedbackType == 1)[0].commonFileList)

    if (route.query.taskExecuteStatus == 150)
      initFileListUpload.value = data.value?.data.filter(item => item.feedbackType == 2)[0].commonFileList
    else
      initFileListUpload.value = data.value?.data.filter(item => item.feedbackType == 1)[0].commonFileList
      console.log(initFileListUpload.value,'initFileListUpload.value')
      if (initFileListUpload.value.length > 0) {
      for (const item of initFileListUpload.value) {
        if (env.value === 'production')
          item.fileUrl = item.fileUrl.replace(/^(.*?\/api\/v1)/, `http://127.0.0.1:${state.value.ISCP_USED_PORT}/api/v1`)
          item.base64 = await getBase64(item.fileUrl)
        blobToFile(item).then((file) => {
          item.file = file
        })
      }
    }
  })
}

function blobToFile(file) {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest()
    xhr.open('GET', file.fileUrl, true)
    xhr.setRequestHeader('Authorization', `Bearer ${state.value.token}`)
    xhr.responseType = 'blob'
    xhr.onload = function (e) {
      if (this.status == 200) {
        const blob = this.response
        const name = file.fileName
        const fileType = new File([blob], name, { lastModified: Date.now() })
        resolve(fileType)
      }
      else {
        reject(new Error('Failed to fetch the file.'))
      }
    }
    xhr.onerror = function () {
      reject(new Error('Failed to fetch the file.'))
    }
    xhr.send()
  })
}
function init() {
  // 请求用户详情
  const { data, execute } = useGet(Api.taskUserDetail, route.query.taskUserId)
  execute().then(() => {
    detailData.value = data.value?.data
    console.log('detailData.value', detailData.value)
    // 赋予初始值派发方式 反馈 拒签理由
    if (detailData.value.backupPhone) {
      formData.value.haveBackupPhone = '是'
      formData.value.backupPhone = detailData.value.backupPhone
    }
    else {
      formData.value.haveBackupPhone = '否'
    }
    if(route.query.taskExecuteStatus == 161 || route.query.taskExecuteStatus == 162){
       endTask.value = true
    }
    if (route.query.taskExecuteStatus == 150) {
      formData.value.deliveryMethod = deliveryMethodColumns.filter(item => item.value == detailData.value?.cancelAssignMethod)[0]?.text
      formData.value.feedbackStatus = '同意'
      // formData.value.remark = detailData.value?.remark
    }
    else {
      formData.value.deliveryMethod = deliveryMethodColumns.filter(item => item.value == detailData.value?.assignMethod)[0]?.text
      formData.value.feedbackStatus = feedbackStatusColumns.filter(item => item.value == detailData.value?.feedbackStatus)[0]?.text
      formData.value.rejectedReason = detailData.value?.rejectedReason
      // formData.value.remark = detailData.value?.remark
    }
  })
}
function deliveryMethodConfirm({ selectedOptions }) {
  formData.value.deliveryMethod = selectedOptions[0]?.text
  showDeliveryMethodPicker.value = false
}
function feedbackStatusConfirm({ selectedOptions }) {
  formData.value.feedbackStatus = selectedOptions[0]?.text
  showFeedbackStatusPicker.value = false
}
function haveBackupPhoneConfirm({ selectedOptions }) {
  formData.value.haveBackupPhone = selectedOptions[0]?.text
  showHaveBackupPhonePicker.value = false
}
async function onSubmit(values) {
  values.deliveryMethod = deliveryMethodColumns.filter(
    item => values.deliveryMethod == item.text,
  )[0]?.value
  if (route.query.taskExecuteStatus == 150) {
    values.feedbackStatus = cancelFeedbackStatusColumns.filter(
      item => values.feedbackStatus == item.text,
    )[0]?.value
  }
  else {
    values.feedbackStatus = feedbackStatusColumns.filter(
      item => values.feedbackStatus == item.text,
    )[0]?.value
  }

  values.taskUserId = route.query.taskUserId
  const arr = []
  initFileListUpload.value.forEach((item) => {
    arr.push({ id: item.id, fileName: item.fileName })
  })
  console.log('🚀 ~ file: taskUserfeedback.vue:146 ~ initFileListUpload.value.forEach ~ initFileListUpload:', initFileListUpload.value)
  values.commonFileList = arr
  console.log('🚀 ~ file: taskUserfeedback.vue:147 ~ onSubmit ~ values.commonFileList:', values.commonFileList)
  const { data, execute } = usePost(Api.taskUserFeedbackLog, { ...values })
  execute().then(() => {
    console.log('🚀 ~ file: taskUserfeedback.vue:152 ~ onSubmit ~ data:', data.value)
    if (data.value.code == 0) {
      showToast('反馈成功')
      router.back()
    }
  })
  console.log('submit', values)
}
function uploadMethod(file, object) {
  console.log('🚀 ~ uploadMethod ~ file:', file.name)
  const formData = new FormData()
  formData.append('fileList', file)
  useFetch(Api.taskUserFeedbackUpload, {
    async beforeFetch({ url, options, cancel }) {
      if (env.value === 'production')
        url = `http://127.0.0.1:${state.value.ISCP_USED_PORT}/${url}`

      else
        url = `${import.meta.env.VITE_BASE_URL}/${url}`

      options.headers = {
        ...options.headers,

        Authorization: `Bearer ${state.value.token}`,
      }
      return { url, options }
    },
    async afterFetch({ data, response, error }) {
      const obj = {}
      obj.id = data?.data[0]?.id
      obj.fileName = data?.data[0]?.fileName
      if(env.value === 'production'){
        obj.fileUrl =data?.data[0]?.fileUrl.replace(/^(.*?\/api\/v1)/, `http://127.0.0.1:${state.value.ISCP_USED_PORT}/api/v1`)
      }else{
        obj.fileUrl =data?.data[0]?.fileUrl
      }
      initFileListUpload.value.push({ ...object, ...obj }) // localData是图片的base64数据，可以用img标签显示
      imgLoading.value = false
      return { data, response }
    },
    onFetchError({ data, response, error }) {
      if (response.status == 401) {
        state.value.token = ''
        state.value.flag = false
        showDialog({
          title: '提示',
          message: '登录已经过期',
        }).then(() => {
          // getToken()
          wx.invoke('multiWindows_close', {}, (res) => {
            // app_log(res);
          })
        })
      }
      else if (response.status == 403) {
        state.value.token = ''
        state.value.flag = false
        showDialog({
          title: '提示',
          message: '权限不足',
        }).then(() => {
          // getToken()
          wx.invoke('multiWindows_close', {}, (res) => {
            // app_log(res);
          })
        })
      }

      data = undefined
      return { data, error }
    },
  }).post(formData).json()
}
async function afterRead(file) {
  console.log(file, 'filefile')
  if (file.length > 1) {
    file.forEach((item) => {
      fileListUpload.value.push(item.file)
    })
  }
  else {
    fileListUpload.value.push(file.file)
  }
}
// 删除图片
function deleteImg(index) {
  initFileListUpload.value.splice(index, 1)
}
function isFile(data) {
  return data instanceof File || data instanceof Blob
}
// //判断是不是文件  并且上传图片
// async function isFileAndUploadMethod(file,res){

// }
// 上传图片动作面板选择其中一项时触发的操作
function onSelectPhotoActionSheet(item) {
  showPhotoActionSheet.value = false
  console.log(item, 'item')
  if (item.name === '相册') {
    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album'], // 可以指定来源是相册还是相机，默认二者都有
      quality: 1, // 压缩质量，范围0～1，数值越小，质量越低，压缩率越高（仅对jpg有效）
      success(res) {
        wx.getLocalImgData({
          localId: res.localIds[0], // 图片的localID
          success(res) {
            base64ImageCompressWithSizeCheck(res.localData, 1000, 1000, 0.8, 1000, (file) => {
              const obj = { base64: res.localData, file }
              imgLoading.value = true
              uploadMethod(file, obj)
            }, 1, 5)
          },
          fail(err) {
            console.log('getLocalImgData err', err) // 打印错误信息
          },
        })
      },
      fail(err) {
        console.log('chooseImage err', err) // 打印错误信息
      },
    })
  }
  else {
    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['camera'], // 可以指定来源是相册还是相机，默认二者都有
      quality: 1, // 压缩质量，范围0～1，数值越小，质量越低，压缩率越高（仅对jpg有效）
      success(res) {
        wx.getLocalImgData({
          localId: res.localIds[0], // 图片的localID
          success(res) {
            base64toFile(res.localData, new Date().getTime()).then(async (file) => {
              base64ImageCompressWithSizeCheck(res.localData, 1000, 1000, 0.8, 1000, (file) => {
                const obj = { base64: res.localData, file }
                imgLoading.value = true
                uploadMethod(file, obj)
              }, 1, 5)
            }).catch((error) => {
              console.error(error)
            })
          },
          error(res) {
            console.log('getLocalImgData err', res)
          },
        })
      },
      fail(err) {
        console.log('chooseImage err', err) // 打印错误信息
      },
    })
  }
}

function base64toFile(base64Data, filename) {
  return new Promise((resolve, reject) => {
    const dataArr = base64Data.split(',')
    const mime = dataArr[0].match(/:(.*?);/)[1]
    const suffix = mime.split('/')[1]
    console.log('🚀 ~ returnnewPromise ~ suffix:', suffix)
    const bStr = window.atob(dataArr[1])
    let n = bStr.length
    const u8arr = new Uint8Array(n)
    while (n--)
      u8arr[n] = bStr.charCodeAt(n)

    const file = new File([u8arr], `${filename}.${suffix}`, { type: mime })
    resolve(file)
  })
}
/**
 * 将Base64数据转换为File对象
 * @param {string} base64Data - Base64数据
 * @param {Number} maxWidth - 最大宽度
 * @param {Number} maxHeight - 最大高度
 * @param {string} initialQuality - 初始分辨率质量
 * @param {Number} maxSizeInKB - 图片上传最大大小
 * @param {Number} currentTry - 当前尝试次数
 * @param {Number} maxAttempts - 最大尝试次数
 *
 * @returns {File} - 转换后的File对象
 */
function base64ImageCompressWithSizeCheck(base64Data, maxWidth, maxHeight, initialQuality, maxSizeInKB, callback, currentTry = 1, maxAttempts = 5) {
  // 将Base64数据转换为Image对象
  const img = new Image()
  img.src = base64Data
  img.onload = function () {
    const canvas = document.createElement('canvas')

    const ctx = canvas.getContext('2d')

    let width = img.width
    let height = img.height

    if (width > maxWidth || height > maxHeight) {
      if (width > height) {
        height *= maxWidth / width
        width = maxWidth
      }
      else {
        width *= maxHeight / height
        height = maxHeight
      }
    }

    canvas.width = width
    canvas.height = height

    ctx.drawImage(img, 0, 0, width, height)

    const dataUrl = canvas.toDataURL('image/jpeg', initialQuality)
    const blob = dataURLToBlob(dataUrl)
    const fileSizeInKB = blob.size / 1024
    console.log('🚀 ~ base64ImageCompressWithSizeCheck ~ fileSizeInKB:', fileSizeInKB)

    if (fileSizeInKB <= maxSizeInKB) {
      // 文件大小满足要求，转换为File对象并返回
      const filename = `${new Date().getTime()}.jpeg`
      const file = new File([blob], filename, { type: 'image/jpeg' })
      callback(file)
    }
    else if (currentTry <= maxAttempts) {
      // 文件大小仍超过限制，递归调用自己减小质量并重试
      console.log(`尝试#${currentTry}: 文件大小${Math.round(fileSizeInKB)}KB超过限制，降低质量重试...`)
      base64ImageCompressWithSizeCheck(base64Data, maxWidth, maxHeight, initialQuality * 0.9, maxSizeInKB, callback, currentTry + 1, maxAttempts)
    }
    else {
      // 达到最大尝试次数，但仍超出大小限制，根据情况处理
      console.warn(`超过最大尝试次数(${maxAttempts})，文件大小仍大于${maxSizeInKB}KB，压缩失败。`)
      showDialog({
        title: '提示',
        message: '图片过大',
      }).then(() => {
      })
    }
  }
}
function dataURLToBlob(dataUrl) {
  // Split the base64 string from the data URL
  const parts = dataUrl.split(';base64,')
  const contentType = parts[0].split(':')[1]
  const raw = window.atob(parts[1])
  const rawLength = raw.length

  // Create an ArrayBuffer and a view (Uint8Array) for the binary data
  const array = new Uint8Array(new ArrayBuffer(rawLength))

  // Populate the Uint8Array with the binary data from the base64 string
  for (let i = 0; i < rawLength; i++)
    array[i] = raw.charCodeAt(i)

  // Return a new Blob using the ArrayBuffer
  return new Blob([array], { type: contentType })
}
/**
 * @description 预览图片
 * @param startPosition: 预览图片的初始位置
 */
function previewImage(index,item){
      wx.previewImage({
          current: `${item.fileUrl}`, // 当前显示图片的http链接
          urls: initFileListUpload.value.map(item =>
            item.fileUrl
          ), // 需要预览的图片http链接列表
          hidePreviewMenuList : [] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮
      });
}

</script>

<template>
  <div>
    <div m-b-7 m-t-3 flex items-center>
      <van-icon name="arrow-left" font-500 color-white style="font-size: 20px" @click="router.back()" />
      <div m-l-5 font-500 color-white style="font-size: 20px">
        {{ route.query.taskExecuteStatus == 150 ? '取消' : '' }}停电通知反馈
      </div>
    </div>
    <div m-b-4 bg-white style="padding: 1rem .5rem;">
      <div text-left class="mb-3 flex">
        <span inline-block class="w-8" text-left>编号：</span>
        <span text-right class="w-9">{{ detailData.receiptCode }}</span>
      </div>

      <div text-left class="mb-3 flex">
        <span inline-block class="w-8" text-left>户号：</span>
        <span text-right class="w-9">{{ detailData.accountNumber }}</span>
      </div>

      <div text-left class="mb-3 flex">
        <span inline-block class="w-8" text-left>客户名称：</span>
        <span text-right class="w-9">{{ detailData.customerName }}</span>
      </div>

      <div text-left class="mb-3 flex">
        <span inline-block class="w-8" text-left>联系方式：</span>
        <span text-right class="w-9">{{ detailData.phone }}</span>
      </div>

      <div text-left class="mb-3 flex">
        <span inline-block class="w-8" text-left>客户地址：</span>
        <span text-right class="w-9">{{ detailData.customerAddress }}</span>
      </div>

      <div text-left class="mb-3 flex">
        <span inline-block class="w-8" text-left>停电时间：</span>
        <span text-right class="w-9"> {{ detailData.task?.startTime }} 至 {{
          detailData.task?.endTime }}</span>
      </div>

      <div text-left class="mb-3 flex">
        <span inline-block class="w-8" text-left>电系编号：</span>
        <span text-right class="w-9">{{ detailData.electricalNumber }}</span>
      </div>
    </div>
    <van-form @submit="onSubmit">
      <div m-b-4 bg-white p-b-5 p-t-5>
        <van-cell-group>
          <van-field
            v-model="formData.deliveryMethod" name="deliveryMethod" is-link readonly label="派送方式："
            placeholder="选择派送方式" label-width="110" @click="showDeliveryMethodPicker = true"
          />
          <van-field
            v-model="formData.haveBackupPhone" is-link readonly label="联系方式变动：" placeholder="请选择是或者否"
            label-width="110" @click="showHaveBackupPhonePicker = true"
          />
          <van-field
            v-if="formData.haveBackupPhone == '是'" v-model="formData.backupPhone" name="backupPhone"
            label="新的联系方式：" required placeholder="新的联系方式：" :rules="[{ required: true, message: '请输入新的联系方式' }]" label-width="110"
          />
          <van-field
            v-model="formData.feedbackStatus" name="feedbackStatus" is-link readonly label="反馈状态："
            placeholder="选择反馈状态" required :rules="[{ required: true, message: '请选择反馈状态' }]" label-width="110"
            @click="route.query.taskExecuteStatus == 150 ? showFeedbackStatusPicker = false : showFeedbackStatusPicker = true"
          />
          <van-field
            v-if="formData.feedbackStatus == '拒签'" v-model="formData.rejectedReason"
            :rules="[{ required: true, message: '请输入拒签理由' }]" name="rejectedReason" rows="2" autosize label=""
            type="textarea" maxlength="150" placeholder="拒签理由" show-word-limit style="border: 1px solid #d9d9d9"
          />
        </van-cell-group>
        <van-popup v-model:show="showDeliveryMethodPicker" round position="bottom">
          <van-picker
            :columns="deliveryMethodColumns" @cancel="showDeliveryMethodPicker = false"
            @confirm="deliveryMethodConfirm"
          />
        </van-popup>
        <van-popup v-model:show="showFeedbackStatusPicker" round position="bottom">
          <van-picker
            :columns="route.query.taskExecuteStatus == 150 ? cancelFeedbackStatusColumns : feedbackStatusColumns"
            @cancel="showFeedbackStatusPicker = false" @confirm="feedbackStatusConfirm"
          />
        </van-popup>
        <van-popup v-model:show="showHaveBackupPhonePicker" round position="bottom">
          <van-picker
            :columns="haveBackupPhoneColumns" @cancel="showHaveBackupPhonePicker = false"
            @confirm="haveBackupPhoneConfirm"
          />
        </van-popup>
      </div>
      <!-- pc端展示 只有上传文件 -->
      <!-- <div  m-b-4  flex  bg-white flex-wrap m-l-1 p-b-2>
        <div relative v-for="(item, index) in initFileListUpload" :key="index" flex m-r-1 m-l-1 m-b-1>
          <c-image   :base64-url="item.fileUrl" fill   h-20 w-20 ></c-image>
          <div w-4 h-4 flex align-middle absolute right-0 top-0 text-center color-white  style="background-color: rgba(0, 0, 0, .7);border-radius: 0 0 0 2rem" @click="deleteImg(index)">
            <span absolute style="top: -4px;right: 2px">×</span>
          </div>
        </div>
        <van-uploader v-model="fileList" max-count="1"  h-20 w-20 m-l-1 :after-read="afterRead" />
      </div> -->
      <!-- 移动端展示 拍照和相册 -->
      <div m-b-4 m-l-1 flex flex-wrap p-b-2 p-t-2>
        <div v-if="imgLoading" h-22 w-22 bg-white>
          <van-image h-22 w-22 bg-white>
            <template #loading>
              <van-loading type="spinner" size="20" />
            </template>
          </van-image>
        </div>

        <div v-for="(item, index) in initFileListUpload" :key="index" relative m-b-1 m-l-1 m-r-1 flex>
          <c-image v-if="item.fileUrl" :base64-url="item.fileUrl" fill h-22 w-22 @click="previewImage(index,item)"/>
          <van-image v-else :src="item.base64" fill h-22 w-22 @click="previewImage(index,item)"/>
          <div
            absolute right-0 top-0 h-4 w-4 flex text-center align-middle color-white
            style="background-color: rgba(0, 0, 0, .7);border-radius: 0 0 0 2rem" @click="deleteImg(index)"
          >
            <span absolute style="top: -4px;right: 2px">×</span>
          </div>
        </div>
        <div m-l-1 m-r-1 h-22 w-22 bg-gray-1 bg-white p-t-2 color-gray @click="showPhotoActionSheet = true">
          <van-icon name="plus" size="3rem" font-100 />
          <div style="fontSize: 14px">
            上传照片
          </div>
        </div>
      </div>
      <div m-b-4 bg-white>
        <van-field
          v-model="formData.remark" name="remark" rows="2" autosize label="" type="textarea" maxlength="150"
          placeholder="备注" show-word-limit
        />
      </div>
      <div v-if="!endTask" m-b-10 h-10 flex items-center justify-between bg-white>
        <van-button style="color: #005a50; border: 1px solid #005a50" w-40 @click="router.back()">
          取消
        </van-button>
        <van-button
          style="
            color: white;
            border: 1px solid #005a50;
            background-color: #005a50;
          " w-40 native-type="submit"
        >
          提交
        </van-button>
      </div>
    </van-form>
    <!-- 移动端展示 拍照和相册 上传图片动作面板 -->
    <van-action-sheet
      v-model:show="showPhotoActionSheet" :actions="[
        { name: '拍照' }, { name: '相册' },
      ]" @select="onSelectPhotoActionSheet"
    />
  </div>
</template>

<style>
.van-image-preview__image {
  width: 100% !important;
  height: auto !important;
   object-fit: cover; /* 保持图片长宽比的同时填充整个容器 */
}

.w-8 {
  width: 25%;
}
.w-9{
  width:75%;
  color: black;
}

.mb-3 {
  margin-bottom: 5px;
  color: rgba(0, 0, 0, 0.6);
  font-size: 14px;
}

.userType {
  display: block;
  width: 92px;
  height: 32px;
  font-size: 16px;
  line-height: 32px;
  margin-right: 12px;
  margin-bottom: 12px;
  background: #d9d9d9;
}

.userTypeActive {
  display: block;
  width: 92px;
  height: 32px;
  font-size: 16px;
  line-height: 32px;
  color: white;
  margin-right: 12px;
  margin-bottom: 12px;
  background: #005a50;
}

.labelText {
  font-size: 14px;
  color: #858585;
}

.valueText {
  font-size: 14px;
  color: #333333;
  text-align: left;
}
</style>
