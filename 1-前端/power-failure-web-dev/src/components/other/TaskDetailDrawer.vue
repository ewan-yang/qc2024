<template>
  <n-drawer v-model:show="showModal" :on-after-leave="cancelDrawerButton" style="width: 90%">
    <n-drawer-content>
      <template #header>

        <div class="flex justify-between items-center">
          <div class="font-700">
            停电通知详情
          </div>
          <div class="absolute right-7">
            <n-button @click="cancelDrawerButton">取消</n-button>
          </div>
        </div>
      </template>
      <template #default>
        <n-card class="m-b-4">
          <div style="font-size: 18px">
            <div class="flex b-l-#044FE1 b-l-5 p-l-2" v-if="modelData?.taskExecuteStatus?.executeStatus != '150'">
              停电通知内容（

              <span style="color: #D9001B">未派发{{
                modelData.unassignedQty == null
                ? 0
                : modelData.unassignedQty
              }}</span>，<span style="color: green">已派发{{
  modelData.assignedQty == null ? 0 : modelData.assignedQty
}}</span>，<span style="color: #F59A23">不派发{{
  modelData.cancelledQty == null ? 0 : modelData.cancelledQty
}}</span>

              ）
            </div>
            <div class="b-l-#044FE1 b-l-5 p-l-2" v-else style="font-size: 18px">
              取消停电通知内容（

              <span style="color: #D9001B">未派发{{
                modelData.cancelUnassignedQty == null
                ? 0
                : modelData.cancelUnassignedQty
              }}</span>，<span style="color: green">已派发{{
  modelData.cancelAssignedQty == null
  ? 0
  : modelData.cancelAssignedQty
}}</span>

              ）
            </div>
          </div>
          <n-form ref="formRef" :model="modelData" :rules="rules" label-width="auto" class="py-4 max-h-2xl pr-1"
            label-placement="left">
            <n-grid x-gap="12" cols="4">
              <n-grid-item><n-form-item label="关联停电计划:" path="planItemId">
                  <n-select v-model:value="modelData.planItemId" disabled placeholder="请选择停电计划" label-field="projectName"
                    value-field="id" :options="planItemList" @click="showPlanItemTable = true"/> </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="停电计划编号:" path="taskCode">
                  <n-input v-model:value="modelData.taskCode" disabled placeholder="" /> </n-form-item></n-grid-item>
              <n-grid-item>
                <n-form-item label="通知来源:" path="taskSource">
                  <n-select v-model:value="modelData.taskSource" disabled placeholder="请选择通知来源"
                    :options="taskSourceUserList" /> </n-form-item></n-grid-item>
              <n-grid-item>
                <n-form-item label="是否通知媒体:" path="boolNotifyMedia">
                  <n-select v-model:value="modelData.boolNotifyMedia" disabled placeholder="请选择是否通知媒体" :options="[
                    { label: '否', value: 0 },
                    { label: '是', value: 1 },
                  ]" /> </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="停电时间:" path="startTime">
                  <n-date-picker class="w-75" value-format="yyyy-MM-dd HH:mm"
                    v-model:formatted-value="modelData.startTime" disabled
                    :is-date-disabled="(ts) => { return ts < new Date().setHours(0, 0, 0, 0) }" type="datetime" />
                </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="送电时间:" path="endTime">
                  <n-date-picker class="w-75" value-format="yyyy-MM-dd HH:mm" v-model:formatted-value="modelData.endTime"
                    disabled
                    :is-date-disabled="(ts) => { return ts < new Date(modelData.startTime).setHours(0, 0, 0, 0) }"
                    :is-time-disabled="(time) => {
                      return {
                        isHourDisabled: (hour) => {
                          if (new Date(modelData.startTime) > new Date(time)) {
                            return (
                              new Date(time).getHours() <
                              new Date(modelData.startTime).getHours());
                          } else {
                            return false
                          }
                        },
                        isMinuteDisabled: (minute, hour) => {
                          if (new Date(modelData.startTime) > new Date(time)) {
                            return (
                              new Date(time).getMinutes() <
                              new Date(modelData.startTime).getMinutes()
                            );
                          } else {
                            return false
                          }
                        },

                      };
                    }
                      " type="datetime" /> </n-form-item></n-grid-item>
              <n-grid-item>
                <n-form-item label="停电类型:" path="type">
                  <n-select v-model:value="modelData.type" disabled placeholder="请选择停电类型"
                    :options="powerFailureTypeList" /> </n-form-item></n-grid-item>
              <n-grid-item>
                <n-form-item label="停电原因:" path="reason">
                  <n-select v-model:value="modelData.reason" disabled placeholder="请选择停电原因"
                    :options="powerFailureReasonList" /> </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="变电站名称:" path="stationName">
                  <n-input v-model:value="modelData.stationName" disabled placeholder="请输入变电站名称" />
                </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="设备名称:" path="deviceName">
                  <n-input v-model:value="modelData.deviceName" disabled placeholder="请输入设备名称" />
                </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="线路名称:" path="lineName">
                  <n-input v-model:value="modelData.lineName" disabled placeholder="请输入线路名称" />
                </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="停电范围:" path="ranges">
                  <n-input v-model:value="modelData.ranges" disabled placeholder="请输入停电范围" /> </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="停电位置:" path="location">
                  <n-input v-model:value="modelData.location" disabled placeholder="请输入停电位置" />
                </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="工作内容:" path="jobContent">
                  <n-input type="textarea" rows="1" v-model:value="modelData.jobContent" disabled placeholder="请输入工作内容" />
                </n-form-item></n-grid-item>
              <n-grid-item><n-form-item label="备注:" path="remark">
                  <n-input v-model:value="modelData.remark" disabled placeholder="请输入备注" /> </n-form-item></n-grid-item>
                            <n-grid-item><n-form-item label="配网办编号:" path="outId">
                  <n-input type="textarea" rows="1" v-model:value="modelData.outId" disabled placeholder="请输入配网办编号" />
                </n-form-item></n-grid-item>
                  <!-- 详情上传附件 -->
              <n-grid-item><n-form-item label="附件:" path="fujian">
                  <n-upload abstract>
                    <div v-for="(item, index) in fileListUpload" :key="index" @click="onIsEditDownUploadData(item)"
                      style="cursor: pointer;height: 20px;padding-bottom:2px;margin-right:10px">
                      <a :href="item.url" onclick="return false" style="color: green">{{ item.name.substring(0,
                        item.name.indexOf("_")) }}、</a>
                    </div>
                  </n-upload>
                </n-form-item>


              </n-grid-item>
            </n-grid>
          </n-form>
        </n-card>
        <n-card>
          <div class="flex justify-between h-12 items-center">
            <div>
              <h1 class="font-500" style="font-size: 18px">
                <div class="b-l-#044FE1 b-l-5 p-l-2" v-if="modelData?.taskExecuteStatus?.executeStatus != '150'">
                  停电通知单<span>（<span style="color: #D9001B">拒签{{
                    modelData.rejectedQty == null
                    ? 0
                    : modelData.rejectedQty
                  }}</span>，<span style="color: green">同意{{
  modelData.agreedQty == null ? 0 : modelData.agreedQty
}}</span>，<span style="color: #F59A23">未签{{
  modelData.unsignedQty == null
  ? 0
  : modelData.unsignedQty
}}</span>）</span>
                </div>
                <div class="b-l-#044FE1 b-l-5 p-l-2" v-else>
                  停电通知单<span>（<span style="color: green">同意{{
                    modelData.cancelAgreedQty == null
                    ? 0
                    : modelData.cancelAgreedQty
                  }}</span>，<span style="color: #F59A23">未签{{
  modelData.cancelUnsignedQty == null
  ? 0
  : modelData.cancelUnsignedQty
}}</span>）</span>
                </div>
              </h1>
            </div>
          </div>
          <!-- 不可编辑的表格 -->
          <n-spin :show="loading">
            <n-data-table :scroll-x="2200" :columns="modalUserColumns" :data="modalUserList" :bordeD9001B="false"
              :single-line="false" :max-height="500" />
          </n-spin>
        </n-card>
      </template>
    </n-drawer-content>
    <!-- 停电计划项列表抽屉 -->
    <PlanItemTable :type="'isDetail'" :defaultCheckedRowKeys="modelData.planItemId" :showPlanItemTable="showPlanItemTable" :tableData="planItemList" @close="showPlanItemTable = false" ></PlanItemTable>
  </n-drawer>
  
</template>
<script setup lang="ts" name="TaskDetailDrawer">

import { ref, onMounted, defineProps, h, computed } from "vue";
import { getToken } from '@/store/modules/auth/helpers';
import { ableParamslistAPI, } from "~/src/service/api/otherApi";
import { detailAPI, } from "~/src/service/api/common";
import { useDicStore } from "@/store";
let dic = useDicStore();
defineOptions({ name: 'TaskDetailDrawer' });
onMounted(async () => {
  await dic.loadData(
    "user,task_type,task_reason,task_user_type,task_user_priority"
  );
  const res = await ableParamslistAPI("relay-task-service/api/v1/planItem", {})
  planItemList.value = res.data;
  // console.log(planItemList.value,`================================================================`)
  // console.log(props, 'propsprops')

})
const props = defineProps({
  transformId: String,
  transformIdType: String
})
const showModal = ref(false)
const showPlanItemTable = ref(false)
const modelData = ref({})
const modalUserList = ref([])
const fileListUpload = ref([])
const planItemList = ref([])
const modalUserColumns = [
  {
    title: "用户类型",
    key: "userType",
    render(row, index) {
      return h(
        "span",
        {
          style:{
            display: 'inline-block',
            height:'4rem',
            lineHeight: '4rem',
          }
        },
        taskUserTypeList.value?.filter(
          (item) => item.value == row.userType
        )[0].label
      );
    },
    width: 120,
  },
  {
    title: "户号",
    key: "accountNumber",
    render(row, index) {
      return h("span", {}, row.accountNumber);
    },
    width: 150,
  },
  {
    title: "客户名称",
    key: "customerName",
    render(row, index) {

      return h("span", {}, row.customerName);
    },
    width: 120,
  },
  {
    title: "客户地址",
    key: "customerAddress",
    render(row, index) {
      return h("span", {}, row.customerAddress);
    },
    width: 150,
  },
  {
    title: "联系方式",
    key: "phone",
    render(row, index) {
      return h("span", {}, row.phone);
    },
    width: 130,
  },
  {
    title: "邮编",
    key: "postCode",
    render(row, index) {
      return h("span", {}, row.postCode);
    },
    width: 120,
  },
  {
    title: "是否短时停电",
    key: "boolShortTime",
    render(row, index) {
      return h("span", {}, row.boolShortTime == 1 ? "是" : "否");
    },
    width: 100,
  },
  {
    title: "用户重要性",
    key: "userPriority",
    render(row, index) {
      return h(
        "span",
        {},
        userPriorityList.value?.filter(
          (item) => item.value == row.userPriority
        )[0].label
      );
    },
    width: 130,
  },
  {
    title: "所属台区",
    key: "region",
    render(row, index) {
      return h("span", {}, row.region);
    },
    width: 90,
  },
  {
    title: "电系编号",
    key: "electricalNumber",
    render(row, index) {
      return h("span", {}, row.electricalNumber);
    },
    width: 90,
  },
  {
    title: "地址",
    key: "address",
    render(row, index) {
      return h("span", {}, row.address);
    },
    width: 120,
  },
  {
    title: "电压等级",
    key: "voltageLevel",
    render(row, index) {
      return h("span", {}, row.voltageLevel);
    },
    width: 120,
  },
  {
    title: "所属接入点",
    key: "accessPoint",
    render(row, index) {
      return h("span", {}, row.accessPoint);
    },
    width: 90,
  },
  {
    title: "所属接入点名称",
    key: "accessPointName",
    render(row, index) {
      return h("span", {}, row.accessPointName);
    },
    width: 120,
  },
  {
    title: "装接容量",
    key: "capacity",
    render(row, index) {
      return h("span", {}, row.capacity);
    },
    width: 90,
  },
  {
    title: "备注",
    key: "remark",
    render(row, index) {
      return h("span", {}, row.remark);
    },
    width: 150,
  },
];
let taskSourceUserList = computed(() => {
  //通知来源下拉菜单
  //'user'
  return dic.getUser;
});
let powerFailureTypeList = computed(() => {
  //停电类型下拉菜单
  //'task_type'
  return dic.getPowerFailureType;
});
let powerFailureReasonList = computed(() => {
  //停电原因下拉菜单
  //'task_reason'
  return dic.getPowerFailureReason;
});
let taskUserTypeList = computed(() => {
  //用户类型下拉菜单
  //'task_user_type'
  return dic.getTaskUserType;
});
let userPriorityList = computed(() => {
  //用户重要性下拉菜单
  //'task_user_priority'
  return dic.getUserPriority;
});

let loading = ref(true);

async function init(id) {

  //任务通知详情数据
  const taskDetailRes = await detailAPI('relay-task-service/api/v1/task', id)
  // console.log(taskDetailRes,`================================1231232131`)
  modelData.value = taskDetailRes.data
  modelData.value.type = "" + modelData.value.type;
  modelData.value.reason = "" + modelData.value.reason;
  modelData.value.planItemId = modelData.value.planItemId ? "" + modelData.value.planItemId : null;
  modelData.value.commonFileList.forEach((item) => {
    item.url = item.fileUrl;
    item.name = item.fileName;
    item.status = "pending";
  });
  fileListUpload.value = modelData.value.commonFileList;
  //用户表数据
  loading.value = true
  const modalUserListRes = await ableParamslistAPI('relay-task-service/api/v1/taskUser', {
    taskId: id,
  })
  // console.log(modalUserListRes,'modalUserListResmodalUserListRes')
  modalUserList.value = modalUserListRes.data
  let timer = setTimeout(() => {
    loading.value = false
    clearTimeout(timer)
  }, 300)
}
function onIsEditDownUploadData(item) {
  // console.log("onIsEditDownUploadData===");
  // console.log(item, "itemitem");
  var xhr = new XMLHttpRequest();
  xhr.open("GET", item.url, true);
  xhr.setRequestHeader("Authorization", `Bearer ${getToken()}`);
  xhr.responseType = "blob";

  xhr.onload = function (e) {
    //如果请求执行成功
    if (this.status == 200) {
      var blob = this.response;

      var filename = item.name.substring(0, item.name.indexOf("_"));
      var a = document.createElement("a"); // blob.type = "application/octet-stream"; //创键临时url对象

      var url = URL.createObjectURL(blob);

      a.href = url;

      a.download = filename;

      a.click(); //释放之前创建的URL对象

      window.URL.revokeObjectURL(url);
    }
  }; //发送请求
  xhr.send();
}
function cancelDrawerButton() {
  modelData.value = {};
  fileListUpload.value = [];
  modalUserList.value = [];
  showModal.value = false;
}
async function open() {
  // console.log(props.transformId, 'props.transformId')
  if (props.transformId) {
    showModal.value = true;
    if (props.transformIdType == 'taskUser') {
      //用户详情数据
      const taskUserDetailRes = await detailAPI('relay-task-service/api/v1/taskUser', props.transformId)
      init(taskUserDetailRes.data.task?.id)
    } else if (props.transformIdType == 'task') {
      init(props.transformId)
    }
  }
}
defineExpose({
  open,
  cancelDrawerButton,
});
</script>