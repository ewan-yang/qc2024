<script setup>
defineOptions({
  name: "taskUserfeedback",
});
import { Api } from "../utils/api";
import { taskExecuteStatusMap } from "../composables/map";
import { showToast,showDialog } from 'vant';
import { useFetch } from '@vueuse/core'

const router = useRouter();
const route = useRoute();
const state = useGlobalState()
const fileList = ref([]);
const fileListUpload = ref([]);
const imgList = ref([]);
const showPicker = ref(false);
const showDeliveryMethodPicker = ref(false);
const showFeedbackStatusPicker = ref(false);
const showHaveBackupPhonePicker = ref(false);
const canAbleInput = ref(false)
const formData = ref({});
const detailData = ref({});
const deliveryMethodColumns = [
  { text: "送达现场", value: 1 },
  { text: "微信通知", value: 2 },
  { text: "营销通知", value: 3 },
];
const feedbackStatusColumns = [
  { text: "同意", value: "320" },
  { text: "拒签", value: "330" },
];
const cancelFeedbackStatusColumns = [
  { text: "同意", value: "520" },
];
const haveBackupPhoneColumns = [
  { text: "是", value: "是" },
  { text: "否", value: "否" },
];
const showFilter = ref(false);
onMounted(async () => {
  init();
  initImg();
});

const init = () => {
  //请求用户详情
  const { data, execute } = useGet(Api.taskUserDetail, route.query.taskUserId);
  execute().then(() => {
    detailData.value = data.value?.data;
  });
};
const initImg = ()=>{
  const { data, execute } = useGet(Api.taskUserFeedbackLogList, route.query.taskUserId);
  execute().then(() => {
    imgList.value = data.value?.data[0].commonFileList;
  });
}
function deliveryMethodConfirm({ selectedOptions }) {
  formData.value.deliveryMethod = selectedOptions[0]?.text;
  showDeliveryMethodPicker.value = false;
}
function feedbackStatusConfirm({ selectedOptions }) {
  formData.value.feedbackStatus = selectedOptions[0]?.text;
  showFeedbackStatusPicker.value = false;
}
function haveBackupPhoneConfirm({ selectedOptions }) {
  formData.value.haveBackupPhone = selectedOptions[0]?.text;
  showHaveBackupPhonePicker.value = false;
}
const onSubmit = (values) => {
  values.deliveryMethod = deliveryMethodColumns.filter(
    (item) => values.deliveryMethod == item.text
  )[0]?.value;
  if(route.query.taskExecuteStatus == 150){
    values.feedbackStatus = cancelFeedbackStatusColumns.filter(
    (item) => values.feedbackStatus == item.text
  )[0]?.value;
  }else{
    values.feedbackStatus = feedbackStatusColumns.filter(
    (item) => values.feedbackStatus == item.text
  )[0]?.value;
  }

  values.taskUserId = route.query.taskUserId
  const {data,execute } = usePost(Api.taskUserFeedbackLog,{...values})
  execute().then(()=>{
    if(data.value.code == 0){
      console.log(data.value,'feec')
      upload(data.value.data)
      showToast('反馈成功')
      router.back()
    }
  })
  console.log("submit", values);
};
const upload=async (id)=>{
  console.log('fileListUpload.value',fileListUpload.value)
  var aa= new FormData()
  aa.append('id',id)
  fileListUpload.value.forEach((item)=>{
    aa.append('fileList',item)
  })
  const { data,execute} = await useFetch(Api.taskUserFeedbackUpload, {

    async beforeFetch({ url, options, cancel }) {
      url =  url
      options.headers = {
        ...options.headers,
        Authorization: `Bearer ${state.value.token}`,
      }
    return {url,options}
  },
}).post(aa)
}
const afterRead=async (file)=>{
  if(file.length > 1){
    file.forEach((item)=>{
      fileListUpload.value.push(item.file)
    })
  }else{
    fileListUpload.value.push(file.file)
  }

}

</script>

<template>
  <div>
    <div flex items-center m-b-10>
      <van-icon
        name="arrow-left"
        color-white
        font-500
        style="font-size: 20px"
        @click="router.back()"
      />
      <div font-500 color-white m-l-5 style="font-size: 20px">停电通知反馈详情</div>
    </div>
    <div bg-white m-b-4 p-t-5 p-b-5 p-l-5>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27 text-left>编号：</div>
        <div class="valueText" w-70>{{ detailData.receiptCode }}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27 text-left>户号：</div>
        <div class="valueText" w-70>{{ detailData.accountNumber }}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27 text-left>客户名称：</div>
        <div class="valueText" w-70>{{ detailData.customerName }}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27 text-left>联系方式：</div>
        <div class="valueText" w-70>{{ detailData.phone }}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27 text-left>客户地址：</div>
        <div class="valueText" w-70 >{{ detailData.customerAddress }}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27 text-left>停电时间：</div>
        <div class="valueText" w-70>{{ detailData.assignTime }}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27 text-left>电系编号：</div>
        <div class="valueText" w-70>{{ detailData.electricalNumber }}</div>
      </div>
    </div>
    <van-form @submit="onSubmit">
      <div bg-white m-b-4 p-t-5 p-b-5>
        <van-cell-group inset>
          <div flex m-b-2 justify-between>
            <div class="labelText" w-32 text-left>派送方式：</div>
            <div class="valueText" w-70 v-if="route.query.taskExecuteStatus != 150">{{ detailData.deliveryMethod == 1 ? '送达现场' : detailData.deliveryMethod == 2 ? '微信通知' : detailData.deliveryMethod == 3 ? '营销通知' : ''}}</div>
            <div class="valueText" w-70 v-else>{{ detailData.cancelAssignMethod == 1 ? '送达现场' : detailData.cancelAssignMethod == 2 ? '微信通知' : detailData.cancelAssignMethod == 3 ? '营销通知' : ''}}</div>
          </div>
          <div flex m-b-2 justify-between>
            <div class="labelText" w-32 text-left>联系方式变动：</div>
            <div class="valueText" w-70>{{detailData.backupPhone ? '是' : '否'}}</div>
          </div>
          <div flex m-b-2 justify-between>
            <div class="labelText" w-32 text-left>新的联系方式：</div>
            <div class="valueText" w-70>{{detailData.backupPhone}}</div>
          </div>
          <div flex m-b-2 justify-between>
            <div class="labelText" w-32 text-left>反馈状态：</div>
            <div class="valueText" w-70 v-if="route.query.taskExecuteStatus != 150">{{detailData.feedbackStatus == 320 ? '同意' : detailData.feedbackStatus == 310 ? '未签' : '拒签'}}</div>
            <div class="valueText" w-70 v-else>{{detailData.cancelFeedbackStatus == 520 ? '同意' : detailData.cancelFeedbackStatus == 510 ? '未签' : ''}}</div>
          </div>

          <van-field
            v-model="detailData.rejectedReason"
            name="rejectedReason"
            rows="2"
            readonly
            autosize
            label=""
            type="textarea"
            maxlength="150"
            placeholder="拒签理由"
            show-word-limit
            style="border: 1px solid #d9d9d9"
          />
        </van-cell-group>
      </div>
      <div bg-white m-b-4 flex items-center justify-between p-l-2 p-r-4 flex-wrap>
        <div v-for="item in imgList" :key="item.id" m-l-2>
          <c-image :base64-url="item.fileUrl" w-38 h-25  contain/>
        </div>
      </div>

      <div bg-white m-b-4 >
        <van-field
          v-model="detailData.remark"
          name="remark"
          rows="2"
          autosize
          label=""
          readonly
          type="textarea"
          maxlength="150"
          placeholder="备注"
          show-word-limit
        />
      </div>
    </van-form>
  </div>
</template>
<style>
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
