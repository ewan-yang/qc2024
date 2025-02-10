<template>
  <n-drawer v-model:show="showTaskUserDetailModal" style="width: 50%" :on-after-leave="cancelModalCancel">
    <n-drawer-content>
      <template #header>
        <div class="flex justify-between items-center">
          <div>回执详情</div>
          <div class="absolute right-7">
            <n-button @click="cancelModalCancel">返回</n-button>
          </div>
        </div>
      </template>
      <template #default>
        <n-form :model="taskUserDetailData" require-mark-placement="right-hanging" label-placement="left"
          label-width="auto" label-align="right">
          <div>
            <n-grid x-gap="12" cols="2">
              <n-grid-item>
                <n-form-item label="回执编号：">
                  <n-input v-model:value="taskUserDetailData.receiptCode" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
              <n-grid-item>
                <n-form-item label="户号：">
                  <n-input v-model:value="taskUserDetailData.accountNumber" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
            </n-grid>
            <n-grid x-gap="12" cols="2">
              <n-grid-item>
                <n-form-item label="客户名称：">
                  <n-input v-model:value="taskUserDetailData.customerName" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
              <n-grid-item>
                <n-form-item label="联系方式：">
                  <n-input v-model:value="taskUserDetailData.phone" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
            </n-grid>
            <n-grid x-gap="12" cols="2">
              <n-grid-item>
                <n-form-item label="客户地址：">
                  <n-input v-model:value="taskUserDetailData.customerAddress" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
              <n-grid-item>
                <n-form-item label="停电时间：">
                  <n-input v-model:value="taskUserDetailData.startTime" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
            </n-grid>
            <n-grid x-gap="12" cols="2">
              <n-grid-item>
                <n-form-item label="电系编号：">
                  <n-input v-model:value="taskUserDetailData.electricalNumber" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
              <n-grid-item>
                <n-form-item label="新的联系方式：">
                  <n-input v-model:value="taskUserDetailData.backupPhone" placeholder="" disabled />
                </n-form-item>
              </n-grid-item>
            </n-grid>
            <n-grid x-gap="12" cols="1">
              <n-grid-item>
                <n-form-item label="备注：">
                  <n-input v-model:value="taskUserDetailData.remark" disabled placeholder="" type="textarea" rows="2" />
                </n-form-item>
              </n-grid-item>
            </n-grid>
          </div>

          <n-tabs default-value="tingdian" size="large">
            <n-tab-pane name="tingdian" tab="停电通知回执">
              <n-grid x-gap="12" cols="2">
                <n-grid-item>
                  <n-form-item label="派送方式：">
                    <n-select v-model:value="taskUserDetailData.assignMethod" disabled placeholder=""
                      :options="taskAssignMethodList" />
                  </n-form-item>
                </n-grid-item>

                <n-grid-item>
                  <n-form-item label="反馈状态：">
                    <n-select v-model:value="taskUserDetailData.feedbackStatus" disabled placeholder=""
                      :options="taskUserFeedBackStatusList" />
                  </n-form-item>
                </n-grid-item>
              </n-grid>
              <n-grid x-gap="12" cols="1">
                <n-grid-item>
                  <n-form-item label="拒签理由：">
                    <n-input v-model:value="taskUserDetailData.rejectedReason" disabled placeholder="" type="textarea"
                      rows="3" />
                  </n-form-item>
                </n-grid-item>
              </n-grid>
              <div class="flex">
                <n-image-group>
                  <span class="p-r-5">附件图片：</span>

                  <ImgToken width="100" object-fit="cover" class="m-r-2" :show-toolbar="false" :base64-Url="item.fileUrl"
                    v-for="item in taskUserFeedbackImgList" :key="item.id" />
                </n-image-group>
              </div>
              <div>
                <div style="color: #1313cd; cursor: pointer"
                  @click="() => { showTaskUserFeedbackLog = !showTaskUserFeedbackLog }">
                  查看反馈记录
                </div>
                <n-data-table v-if="showTaskUserFeedbackLog" :columns="taskUserFeedbackLogColumns"
                  :data="taskUserFeedbackLogData" />
              </div>
              <div>
              <div style="color: #1313cd; cursor: pointer" @click="() => { showTaskUserFollowLog = !showTaskUserFollowLog }">
                  查看跟进记录
                </div>
                <n-data-table v-if="showTaskUserFollowLog" :columns="taskUserFollowLogColumns"
                  :data="taskUserFollowLogData" />
              </div>
            </n-tab-pane>
            <n-tab-pane name="canceltingdian" tab="取消通知回执">
              <n-grid x-gap="12" cols="2">
                <n-grid-item>
                  <n-form-item label="派送方式：">
                    <n-select v-model:value="taskUserDetailData.cancelAssignMethod" disabled placeholder=""
                      :options="taskAssignMethodList" />
                  </n-form-item>
                </n-grid-item>

                <n-grid-item>
                  <n-form-item label="反馈状态：">
                    <n-select v-model:value="taskUserDetailData.cancelFeedbackStatus" disabled placeholder=""
                      :options="taskCancelFeedbackStatusList" />
                  </n-form-item>
                </n-grid-item>
              </n-grid>
              <div class="flex">
                <n-image-group>
                  <span class="p-r-5">附件图片：</span>

                  <ImgToken width="100" object-fit="cover" class="m-r-2" :show-toolbar="false" :base64-Url="item.fileUrl"
                    v-for="item in cancelTaskUserFeedbackImgList" :key="item.id" />
                </n-image-group>
              </div>
              <div>
                <div style="color: #1313cd; cursor: pointer"
                  @click="() => { showCancelTaskUserFeedbackLog = !showCancelTaskUserFeedbackLog }">
                  查看反馈记录
                </div>
                <n-data-table v-if="showCancelTaskUserFeedbackLog" :columns="taskUserFeedbackLogColumns"
                  :data="cancelTaskUserFeedbackLogData" />
              </div>
            </n-tab-pane>
          </n-tabs>

          <div v-show="false">
              <n-image-group>
                <ImgToken id="imgToken" width="100" object-fit="cover" class="m-r-2" :show-toolbar="false"
                  :base64-Url="item.fileUrl" v-for="item in fujianImgList" :key="item.id" />
              </n-image-group>
            </div>
        </n-form>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>
<script setup lang="ts" name="TaskUserDetailDrawer">
import ImgToken from "@/components/common/ImgToken.vue";
import { ref, onMounted, defineProps, h, computed,nextTick } from "vue";
import { getToken } from '@/store/modules/auth/helpers';
import { ableParamslistAPI, } from "~/src/service/api/otherApi";
import { detailAPI, } from "~/src/service/api/common";
import { useDicStore } from "@/store";
let dic = useDicStore();
defineOptions({ name: 'TaskUserDetailDrawer' });
onMounted(async () => {
  await dic.loadData(
    "task_assign_method,task_cancel_feedback_status,task_user_feedback_status"
  );
  console.log(props, 'propsprops')

})
let taskAssignMethodList = computed(() => {
  //派送方式下拉菜单
  return dic.getTaskAssignMethodList;
});
let taskCancelFeedbackStatusList = computed(() => {
  //取消停电用户通知反馈状态下拉菜单
  return dic.getTaskCancelFeedbackStatus;
});
let taskUserFeedBackStatusList = computed(() => {
  //停电用户通知反馈状态下拉菜单
  return dic.getTaskUserFeedBackStatus;
});
const props = defineProps({ transformId: String })
const showTaskUserDetailModal = ref(false)
//跟进记录的显隐 跟进不区分是不是取消的
const showTaskUserFollowLog = ref(false)
//停电通知反馈记录的显隐
const showTaskUserFeedbackLog = ref(false)
//取消停电通知反馈记录的显隐
const showCancelTaskUserFeedbackLog = ref(false)
const fujianImgList = ref([]);
const taskUserDetailData = ref({})
const taskUserFollowLogData = ref([])
const taskUserFeedbackLogData = ref([])
const cancelTaskUserFeedbackLogData = ref([])
const taskUserFeedbackImgList = ref([]);
const cancelTaskUserFeedbackImgList = ref([]);
const taskUserFeedbackLogColumns = [
  {
    title: "反馈时间",
    key: "createDate",
    width: 130,
  },
  {
    title: "派送方式",
    key: "deliveryMethod",
    width: 100,
    render(row) {
      return h(
        "div",
        {},
        taskAssignMethodList.value.filter(
          (item) => {
            return item.value == "" + row.deliveryMethod
          }
        )[0].label
      );
    },
  },

  {
    title: "反馈状态",
    key: "feedbackStatus",
    width: 100,
    render(row) {
      return h(
        "div",
        {},
        taskUserFeedBackStatusList.value.filter(
          (item) => item.value == "" + row.feedbackStatus
        ).length != 0
          ? taskUserFeedBackStatusList.value.filter(
            (item) => item.value == "" + row.feedbackStatus
          )[0]?.label
          : taskCancelFeedbackStatusList.value.filter(
            (item) => item.value == "" + row.feedbackStatus
          )[0]?.label
      );
    },
  },
  {
    title: "拒签理由",
    key: "rejectedReason",
    width: 150,
  },
  {
    title: "备注",
    key: "remark",
    width: 150,
  },
  {
    title: "附件",
    key: "rejectedReason",
    width: 80,
    render(row) {
      if (row.commonFileList.length != 0) {
        return h(
          "button",
          {
            onClick: () => {
              fujianImgList.value = row.commonFileList
              //当fujianImgList有值后会执行v-for 出来imgToken才能被获取到 所以先nextTick等待dom刷新后再获取
              nextTick(() => {
                const parentElement = document.getElementById("imgToken");
                const imgElement = parentElement.querySelector("img");
                console.log(imgElement, 'imgElementimgElement');
                //MutationObserver 对象来监听imgToken下面的img元素的变化。当src属性发生变化时，会触发回调函数，并在获取到src后执行相应的操作。
                const observer = new MutationObserver((mutationsList) => {
                  console.log(mutationsList, 'mutationsList')
                  for (let mutation of mutationsList) {
                    if (mutation.type === 'attributes' && mutation.attributeName === 'src') {
                      console.log(imgElement.src, 'imgElementimgElement');
                      imgElement.click();
                      //当imgElement.src检测为base64的时候说明 图片已经加载好 那么imgElement.click();就已经实现了，所以关闭监听
                      if (/^(data:)?[a-z0-9+\/]+={0,2}$/i.test(imgElement.src)) {
                        observer.disconnect();
                      }
                    }
                  }
                });

                observer.observe(imgElement, { attributes: true });
              })
            },
            style: {},
          },
          "查看附件"
        );
      } else {
        return ''
      }
    },
  },
];
const taskUserFollowLogColumns = [
  {
    title: "跟进人员",
    key: "dockUser",
    width: 130,
  },
  {
    title: "跟进时间",
    key: "createDate",
    width: 130,
  },
  {
    title: "跟进方式",
    key: "followMethod",
    width: 100,
    render(row) {
      return h(
        "div",
        {},
        taskAssignMethodList.value.filter(
          (item) => item.value == "" + row.followMethod
        )[0].label
      );
    },
  },
  {
    title: "联系电话",
    key: "telPhone",
    width: 130,
  },
  {
    title: "反馈状态",
    key: "feedbackStatus",
    width: 100,
    render(row) {
      return h(
        "div",
        {},
        taskUserFeedBackStatusList.value.filter(
          (item) => item.value == "" + row.feedbackStatus
        ).length != 0
          ? taskUserFeedBackStatusList.value.filter(
            (item) => item.value == "" + row.feedbackStatus
          )[0]?.label
          : taskCancelFeedbackStatusList.value.filter(
            (item) => item.value == "" + row.feedbackStatus
          )[0]?.label
      );
    },
  },
  {
    title: "情况说明",
    key: "followDesc",
    width: 150,
  },
];
function cancelModalCancel() {
  //任务用户回执详情关闭
  taskUserDetailData.value = {};
  showTaskUserDetailModal.value = false;
  showTaskUserFollowLog.value = false;
  showTaskUserFeedbackLog.value = false;
  showCancelTaskUserFeedbackLog.value = false;
  taskUserFeedbackLogData.value = [];
  cancelTaskUserFeedbackLogData.value = [];
  taskUserFollowLogData.value = [];
}
function getTaskUserFollowLog(taskUserId) {
  detailAPI("relay-task-service/api/v1/taskUserFollow/list", taskUserId).then(
    (res) => {
      if (!res.error) {
        taskUserFollowLogData.value = res.data;
      }
    }
  );
}
function getTaskUserFeedbackLog(taskUserId) {
  detailAPI(
    "relay-task-service/api/v1/taskUserFeedbackLog/list",
    taskUserId
  ).then((res) => {
    if (!res.error) {
      taskUserFeedbackLogData.value = res.data.filter(
        (item) => item.feedbackType == 1
      );
      if (
        taskUserFeedbackLogData.value.length != 0 &&
        taskUserFeedbackLogData.value[0]?.commonFileList.length != 0
      ) {
        //停电通知记录里最新的一条作为当前反馈图片
        taskUserFeedbackImgList.value =
          taskUserFeedbackLogData.value[0]?.commonFileList;
      } else {
        taskUserFeedbackImgList.value = [];
      }

      cancelTaskUserFeedbackLogData.value = res.data.filter(
        (item) => item.feedbackType == 2
      );
      if (
        cancelTaskUserFeedbackLogData.value.length != 0 &&
        cancelTaskUserFeedbackLogData.value[0]?.commonFileList.length != 0
      ) {
        //取消停电通知记录里最新的一条作为当前反馈图片
        cancelTaskUserFeedbackImgList.value =
          cancelTaskUserFeedbackLogData.value[0]?.commonFileList;
      } else {
        cancelTaskUserFeedbackImgList.value = [];
      }
    }
  });
}
async function init(id) {
  //任务用户回执详情
  const taskUserDetailRes = await detailAPI('relay-task-service/api/v1/taskUser', id)
  taskUserDetailData.value = taskUserDetailRes.data;
  console.log(taskUserDetailData.value,'================================================================');
  taskUserDetailData.value.startTime = taskUserDetailData.value.task?.startTime;
  taskUserDetailData.value.assignMethod = "" + taskUserDetailData.value.assignMethod;
  taskUserDetailData.value.cancelAssignMethod = "" + taskUserDetailData.value.cancelAssignMethod;
  getTaskUserFollowLog(id)
  getTaskUserFeedbackLog(id);
}
async function open() {
  console.log(props.transformId, 'props.transformId')
  if (props.transformId) {
    showTaskUserDetailModal.value = true;
    await init(props.transformId)
  }
}
defineExpose({
  open,
  cancelModalCancel,
});
</script>