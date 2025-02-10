<script setup>
defineOptions({
  name: "taskDetail",
});
import { Api } from "../utils/api";
import { taskExecuteStatusMap } from "../composables/map";
import { showToast, showDialog } from 'vant';
// import '../../public/iscp/jweixin-1.2.0'
import { getUserListLogoSvgToBase64 } from '~/utils/svgBase64'
const router = useRouter();
const route = useRoute();
const state = useGlobalState()
const activeName = ref("type");
const assignStatusStyle1 = ref("filter");
const assignStatusStyle2 = ref("filter");
const assignStatusStyle3 = ref("filter");
const assignStatusStyle4 = ref("filter");
const userTypeStyle1 = ref("filter");
const userTypeStyle2 = ref("filter");
const userTypeStyle3 = ref("filter");
const stagingData = ref({})
const assignStatusType = ref(0);
const userType = ref(0);
const assignTeam = ref(null)

const endTask = ref(false)
const taskExecuteStatusList = ref([]);
// const detailData = ref({
// 					unassignedQty: 20,
// 					type: -40199429,
// 					id: 1,
// 					ange: "elitsuntelitsuntelitsuntelitsuntelitsuntelitsuntelitsuntelit",
// 					boolNotifyMedia: 15374158,
// 					agreedQty: 210,
// 					updateBy: 72313525,
// 					jobContent: "consequat",
// 					createBy: -86648962,
// 					startTime: "1945-07-29 16:46:52",
// 					updateDate: "1945-07-29 16:46:52",
// 					endTime: "1994-12-10T18:20:10.459Z",
// 					attribute3: "laborum ex nulla elit cupidatat",
// 					taskCode: "1234122341",
// 					attribute1: "consectetur",
// 					status: -98435955,
// 					advanceWarning: 0,
// 					attribute2: "enim in",
// 					createDate: "1963-12-05T21:56:07.329Z",
// 					assignedBy: -93746527,
// 					unitName: "nulla",
// 					cancelledQty: 10,
//           ranges: '先缴纳几年级擦可能性就按',
// 					remark: "Excepteur adipisicing qui pariatur",
// 					deviceName: "dolor enim aute",
// 					unsignedQty: 210,
// 					lineName: "laboris in magna",
// 					reason: -6140884,
// 					assPlan: "fugiat aliqua eiusmod",
// 					assignedQty: 100,
// 					location: "sed ut enim minim",
// 					rejectedQty: 120,
// 					executeStatusId: 1,
// 					version: -44206842,
// 					stationName: "exercitation sed do"
// 				})
const detailData = ref({});
const userDataList = ref([]);
const changeAssignTeamColumns = ref([])
const cancelTaskUserColumns = ref([])
const showFilter = ref(false);
const showChangeAssignTeam = ref(false);
const showCancelTaskUser = ref(false);
const cancelUserId = ref(null)
const assignType = ref('')
const scanRes = ref({})
onMounted(async () => {
  console.log(route.query.id, "route.query.id");
  init();
  getUsetList();
  const { data, execute } = usePost(Api.lovList, {
    code: "task_execute_status",
  });
  execute().then(() => {
    taskExecuteStatusList.value = data?.value?.data[0]?.lovLineList;
  });
});
watch(detailData, async (newVal, oldVal) => {
  if (newVal.taskExecuteStatus.executeStatus == 161 || newVal.taskExecuteStatus.executeStatus == 162) {
    // showToast('该任务已完成或已终结')
    // router.push({
    //   path: "/taskTabber",
    // });
    endTask.value = true
  }
})
const init = async (taskId = "") => {
  //请求详情
  const { data, execute } = useGet(
    Api.taskDetail,
    taskId == "" ? route.query.id : taskId
  );
  execute().then(() => {
    console.log(data.value.data, "data.value.data");
    detailData.value = data?.value?.data;
  });
};
const getUsetList = async (params = {}) => {
  if (state.value.engineersTeamId != '') {
    if (route.query.taskExecuteStatus != 150) {
      params.assignStatusList = ['220', '230']
    } else {
      params.cancelAssignStatusList = ['420', '430']
    }
  }
  const { data, execute } = usePost(Api.taskUserList, {
    taskId: route.query.id,
    ...params,
  });
  execute().then(() => {
    userDataList.value = data?.value?.data;
  });
};
const assignTaskUser = async (taskUserId) => {
  let params = {
    taskUserIdList: [...taskUserId],
    type: detailData.value?.taskExecuteStatus.executeStatus == 150 ? 3 : 1,
    ...(route.query.taskExecuteStatus != 150 && { engineersTeamId: assignTeam.value })
  }
  const { data, execute } = usePut(Api.taskUserBatchAssign, params);
  execute().then(() => {
    if (data.value?.code == 0) {
      showDialog({
        title: "提示",
        message: "派发成功",
      }).then(() => {
        assignType.value = ''
        scanRes.value = {}
        if (stagingData.value.task) {
          detailData.value = stagingData.value.task
        }

        getUsetList({ taskId: detailData.value?.id })
      });
    }
  });


};
function openTaskUserPopup(currentId) {
  const { data, execute } = usePost(Api.lovList, {
    code: "task_user_cancel_reason",
  });
  execute().then(() => {
    data?.value?.data[0]?.lovLineList.forEach((item) => {
      item.text = item.name
    })
    cancelTaskUserColumns.value = data?.value?.data[0]?.lovLineList;
  });
  cancelUserId.value = currentId
  showCancelTaskUser.value = true
}
function onConfirmCancelTaskUser(selected) {
  const { data, execute } = usePut(
    Api.cancelTaskUser,
    { taskUserIdList: [cancelUserId.value], cancelReason: selected.selectedOptions[0].value }
  );
  execute().then(() => {
    showDialog({
      title: "提示",
      message: "取消成功",

    }).then(() => {
      getUsetList({ taskId: detailData.value?.id })
    });
  });
  closeShowCancelTaskUser()
}
function closeShowCancelTaskUser() {
  showCancelTaskUser.value = false
}
function backHandle(item) {
  const { data, execute } = usePut(Api.recoverCancelTaskUser + '/' + item.id, {});
  execute().then(() => {
    showDialog({
      title: "提示",
      message: "恢复派发成功",

    }).then(() => {
      getUsetList({ taskId: detailData.value?.id })
    });
  });

}

function scanAssignMethod() {
  wx.scanQRCode({
    dec: "scanQRCode desc",
    needResult: 1, // 默认为0，扫描结果由i国网处理，1则直接返回扫描结果，
    scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
    success: function (res) {
      assignType.value = 'scan'
      scanRes.value = res
      if (route.query.taskExecuteStatus != 150) {
        openShowChangeAssignTeam(res)
      } else {
        scanGetDetail(scanRes.value)
      }
      console.log(res, "scanAssignRes");


    },
    error: function (res) {
      console.log(res, "errorRes");
      if (res.errMsg.indexOf("function_not_exist") > 0) {
        alert("版本过低请升级");
      }
    },
  });
}
function allAssignMethod() {
  if (route.query.taskExecuteStatus != 150) {
    openShowChangeAssignTeam()
  } else {
    const arr = [];
    userDataList.value.forEach((item) => {
      arr.push(item.id);
    });
    assignTaskUser(arr);
  }

}
function openShowChangeAssignTeam(res) {
  const { data, execute } = usePost(Api.engineersTeamList, {});
  execute().then(() => {
    console.log(data, 'datadata')
    data?.value?.data.forEach((item) => {
      item.text = item.name
      item.value = item.id
    })
    changeAssignTeamColumns.value = data?.value?.data;
  });
  showChangeAssignTeam.value = true;
}
function closeShowChangeAssignTeam() {
  showChangeAssignTeam.value = false;
}
function onConfirmAssignTeam(selected) {
  console.log(selected, 'selectedValuesselectedValues')
  assignTeam.value = selected.selectedOptions[0].value
  if (assignType.value == 'scan') {
    scanGetDetail(scanRes.value)
  } else {
    const arr = [];
    userDataList.value.forEach((item) => {
      arr.push(item.id);
    });

    assignTaskUser(arr);
  }

  closeShowChangeAssignTeam()
}
function scanGetDetail(res) {
  const { data, execute } = useGet(Api.taskUserDetail, res.resultStr);
  execute().then(() => {
    console.log('stagingData暂存详情数据')
    //stagingData暂存详情数据
    stagingData.value = data.value?.data
    if (data.value?.data.taskId == detailData.value.id) {
      console.log('扫描扫出来的用户***是***现在的任务下面的，直接派发')
      //扫描扫出来的用户***是***现在的任务下面的，直接派发
      assignTaskUser(res.resultStr);
    } else {
      console.log('扫描扫出来的用户***不是***现在的任务下面的，需要弹提示')
      //扫描扫出来的用户***不是***现在的任务下面的，需要弹提示
      showConfirmDialog({
        title: "提示",
        message: "当前扫的用户和之前的用户不是同一任务，是否继续执行",
      })
        .then(() => {
          userDataList.value = [];
          assignTaskUser(res.resultStr);
        })
        .catch(() => { });
    }
  });
}
function openFilter() {
  showFilter.value = true;
  assignStatusType.value = 0;
  assignStatusStyle1.value = "filter";
  assignStatusStyle2.value = "filter";
  assignStatusStyle3.value = "filter";
  assignStatusStyle4.value = "filter";
}
function closeFilter() {
  showFilter.value = false;
}
function changeAssignStatusClassName(num) {
  assignStatusType.value = num;
  if (num == 1) {
    assignStatusStyle1.value = "filterActive";
    assignStatusStyle2.value = "filter";
    assignStatusStyle3.value = "filter";
    assignStatusStyle4.value = "filter";
  } else if (num == 2) {
    assignStatusStyle1.value = "filter";
    assignStatusStyle2.value = "filterActive";
    assignStatusStyle3.value = "filter";
    assignStatusStyle4.value = "filter";
  } else if (num == 3) {
    assignStatusStyle1.value = "filter";
    assignStatusStyle2.value = "filter";
    assignStatusStyle3.value = "filterActive";
    assignStatusStyle4.value = "filter";
  } else if (num == 4) {
    assignStatusStyle1.value = "filter";
    assignStatusStyle2.value = "filter";
    assignStatusStyle3.value = "filter";
    assignStatusStyle4.value = "filterActive";
  }
}
function changeUserTypeClassName(num) {
  userType.value = num;
  if (num == 1) {
    userTypeStyle1.value = "filterActive";
    userTypeStyle2.value = "filter";
    userTypeStyle3.value = "filter";
  } else if (num == 2) {
    userTypeStyle1.value = "filter";
    userTypeStyle2.value = "filterActive";
    userTypeStyle3.value = "filter";
  } else {
    userTypeStyle1.value = "filter";
    userTypeStyle2.value = "filter";
    userTypeStyle3.value = "filterActive";
  }
}
function submitFilter() {
  if (activeName.value == "type") {
    getUsetList({ userType: userType.value });
  } else {
    if (detailData.value?.taskExecuteStatus?.executeStatus != 150) {
      if (assignStatusType.value == 1) {
        //停电任务未派发
        getUsetList({ assignStatusList: [210] });
      } else if (assignStatusType.value == 2) {
        //停电任务已派发
        getUsetList({ assignStatusList: [220] });
      } else if (assignStatusType.value == 3) {
        //停电任务已反馈
        getUsetList({ assignStatusList: [230] });
      } else if (assignStatusType.value == 4) {
        //停电任务不派发
        getUsetList({ assignStatusList: [240] });
      }
    } else {
      if (assignStatusType.value == 1) {
        //取消停电任务未派发
        getUsetList({ cancelAssignStatusList: [410] });
      } else if (assignStatusType.value == 2) {
        //取消停电任务已派发
        getUsetList({ cancelAssignStatusList: [420] });
      } else if (assignStatusType.value == 3) {
        //取消停电任务已反馈
        getUsetList({ cancelAssignStatusList: [430] });
      } else if (assignStatusType.value == 4) {
        //取消停电任务不派发
        getUsetList({ cancelAssignStatusList: [440] });
      }
    }
  }

  showFilter.value = false;
}
console.log(route, "route");
console.log(route.query, "route====");
function goFeedBack(record) {
  if (detailData.value?.taskExecuteStatus?.executeStatus != 150) {
    if (record.assignStatus == 220 || record.assignStatus == 230) {
      //已派发和已反馈
      router.push({
        path: "/taskUserfeedback",
        query: {
          taskUserId: record.id,
          taskExecuteStatus: detailData.value?.taskExecuteStatus?.executeStatus,
        },
      });
    } else {
      showDialog({
        title: "提示",
        message: "不派发和未派发不能进入停电通知反馈页面",

      }).then(() => {
      });
    }
  } else {
    if (record.cancelAssignStatus == 420 || record.cancelAssignStatus == 430) {
      //已派发和已反馈
      router.push({
        path: "/taskUserfeedback",
        query: {
          taskUserId: record.id,
          taskExecuteStatus: detailData.value?.taskExecuteStatus?.executeStatus,
        },
      });
    } else {
      showDialog({
        title: "提示",
        message: "不派发和未派发不能进入取消停电通知反馈页面",

      }).then(() => {
      });
    }
  }

}
function cancelAssignCondition(record) {
  let taskExecuteStatus = record.task.taskExecuteStatus.executeStatus;
  if (taskExecuteStatus == 150) {
    if (record.cancelAssignStatus == 440 || record.cancelAssignStatus == 430) {
      return false;
    }
  } else {
    if (record.assignStatus == 230 || record.assignStatus == 240) {
      return false;
    }
  }
  return true
}
function backAssignCondition(record) {
  let taskExecuteStatus = record.task.taskExecuteStatus.executeStatus;
  if (taskExecuteStatus == 150) {
    if (record.assignStatus != 220) {
      return false;
    }
    if (record.cancelAssignStatus != 440) {
      return false;
    }
  } else {
    if (record.assignStatus != 240) {
      return false;
    }
  }
  return true;
}
function backFn() {
  router.back()
  state.value.tabberNum = 1
}
</script>

<template>
  <div style="height:35%;margin-bottom:1%">
    <div style="height:20%;"  flex items-center>
      <van-icon name="arrow-left" color-white font-500 style="font-size: 20px" @click="backFn" />
      <div font-500 color-white m-l-5 style="font-size: 20px">{{ detailData.taskExecuteStatus?.executeStatus == 150 ? '取消'
        : '' }}停电通知详情</div>
    </div>
    <div style="height:76%;padding:2%;" bg-white overflow-auto>
      <div flex m-b-2>
        <div class="labelText" w-21 text-right>通知编号：</div>
        <div class="valueText" w-60 text-left>{{ detailData.taskCode }}</div>
      </div>
      <div flex m-b-2>
        <div class="labelText" w-21 text-right>停电时间：</div>
        <div class="valueText" w-60 text-left>
          {{ detailData.startTime }}至{{ detailData.endTime }}
        </div>
      </div>
      <div flex m-b-2>
        <div class="labelText" w-21 text-right>停电范围：</div>
        <div class="valueText" w-60 text-left>{{ detailData.ranges }}</div>

      </div>
      <div flex m-b-2>
        <div class="labelText" w-21 text-right>派发时间：</div>
        <div class="valueText" w-60 text-left>{{ detailData.startTime }}</div>
      </div>
      <div flex m-b-2>
        <div class="labelText" w-21 text-right>执行状态：</div>
        <div class="valueText" w-60 text-left>
          {{
            JSON.parse(JSON.stringify(taskExecuteStatusList)).filter(
              (val) => detailData.taskExecuteStatus?.executeStatus == val.value
            )[0]?.name
          }}
        </div>
      </div>
      <div flex >
        <div class="labelText" w-21 text-right>工作内容：</div>
        <div class="valueText" w-60 text-left>{{ detailData.jobContent }}</div>

      </div>
    </div>
  </div>
  <div style="height:10%">
    <div style="height:30%" flex justify-between>
      <div flex items-center>
        <img :src="getUserListLogoSvgToBase64()" style="width: 22px;" w-6 />
        <div style="color: #02382E;line-height: 15px;" font-500 p-l-2>停电用户</div>
      </div>
      <div flex items-center @click="openFilter" v-if="state.engineersTeamId == ''">
        <van-icon name="filter-o" size="20" />
        <div font-500>筛选</div>
      </div>
    </div>
    <div style="height:70%" v-if="route.query.taskExecuteStatus != 150" class="label-1"  flex  items-center>
      <span class="label-item c-1 " @click="getUsetList({feedbackStatus:320})"><span class="c-4">{{ detailData.agreedQty }}</span> 同意</span>
      <span class="label-item c-2" @click="getUsetList({feedbackStatus:310})"><span class="c-5">{{ detailData.unsignedQty }}</span> 未签</span>
      <span class="label-item c-3"  @click="getUsetList({feedbackStatus:330})"><span class="c-6">{{ detailData.rejectedQty }}</span>拒签</span>

    </div>
    <div v-else class="label-1" flex items-center>
      <span class="label-item c-1"  @click="getUsetList({cancelFeedbackStatus:520})"><span class="c-4">{{ detailData.cancelAgreedQty }}</span>同意</span>
      <span class="label-item c-2" @click="getUsetList({cancelFeedbackStatus:510})"><span class="c-5">{{ detailData.cancelUnsignedQty }}</span>未签</span>
    </div>
  </div>

<!-- list -->
  <div style="height:55%" overflow-auto>

    <div v-for="item in userDataList" :key="item.id" bg-white m-b-4 p-b-2 relative>
      <div h-10 b-b-1 b-b-gray-4 flex items-center justify-between m-b-2 p-l-3>
        <div flex>
          <div class="labelText" w-21 text-right>回执单号：</div>
          <div class="valueText">{{ item.receiptCode }}</div>
        </div>
        <div>
          <div class="labelText" m-r-3 v-if="detailData.taskExecuteStatus?.executeStatus == 150">
            <span v-if="item.cancelAssignStatus == 410" style="color: #facc15">未派发</span>
            <span v-else-if="item.cancelAssignStatus == 420" color-green-5>已派发</span>
            <span v-else-if="item.cancelAssignStatus == 430" color-blue-5>已反馈</span>
            <span v-else-if="item.cancelAssignStatus == 440" color-red-6>不派发</span>
          </div>
          <div class="labelText" m-r-3 v-else>
            <span v-if="item.assignStatus == 210" style="color: #facc15">未派发</span>
            <span v-else-if="item.assignStatus == 220" color-green-5>已派发</span>
            <span v-else-if="item.assignStatus == 230" color-blue-5>已反馈</span>
            <span v-else-if="item.assignStatus == 240" color-red-6>不派发</span>
            <!-- {{
              item.assignStatus == 210
                ? "未派发"
                : item.assignStatus == 220
                ? "已派发"
                : item.assignStatus == 230
                ? "已反馈"
                : "不派发"
            }} -->
          </div>
        </div>
      </div>
      <div @click="goFeedBack(item)" p-l-3>
        <div flex m-b-2>
          <div class="labelText" w-22 text-right>客户名称：</div>
          <div class="valueText" w-55 text-left>{{ item.customerName }}</div>
        </div>
        <div flex m-b-2>
          <div class="labelText" w-22 text-right>客户地址：</div>
          <div class="valueText" w-55 text-left>{{ item.customerAddress }}</div>
        </div>
        <div flex m-b-2>
          <div class="labelText" w-22 text-right>派发负责方：</div>
          <div class="valueText" w-55 text-left>{{ item.engineersTeamName }}</div>
        </div>
        <div flex m-b-2>
          <div flex m-r-3 w-45>
            <div class="labelText" w-22 text-right>联系方式：</div>
            <div class="valueText" text-left>{{ item.phone }}</div>
          </div>
          <div flex>
            <div class="labelText" w-18 text-right>电系编号：</div>
            <div class="valueText" text-left>{{ item.electricalNumber }}</div>
          </div>
        </div>
        <div flex m-b-2>
          <div flex m-r-3 w-45>
            <div class="labelText" w-22 text-right>回执状态：</div>
            <div class="valueText" text-left :style="{
              color:
                detailData.taskExecuteStatus?.executeStatus == 150
                  ? (item.cancelFeedbackStatus == 520 ? 'green' : (item.cancelFeedbackStatus == 510 ? '#facc15' : ''))
                  : (item.feedbackStatus == 320 ? 'green' : item.feedbackStatus == 330 ? 'red' : item.feedbackStatus == 310 ? '#facc15' : '')
            }">

              {{
                detailData.taskExecuteStatus?.executeStatus == 150
                ? item.cancelFeedbackStatus == 510
                  ? "未签"
                  : item.cancelFeedbackStatus == 520
                    ? "同意"
                    : ''
                : item.feedbackStatus == 310
                  ? "未签"
                  : item.feedbackStatus == 320
                    ? "同意"
                    : item.feedbackStatus == 330
                      ? "拒签"
                      : ''
              }}
            </div>
          </div>
          <div flex>
            <div class="labelText" w-18 text-left>超期状态：</div>
            <div class="valueText" text-left>{{ item.overdueStatus }}</div>
          </div>
        </div>
      </div>
      <div v-if="state.engineersTeamId == ''">
        <div b-t-1 b-t-gray-4 flex items-center h-10 justify-end z-36 v-if="cancelAssignCondition(item)">
          <van-button style="color: white; background: red; width: 30%; height: 80%"
            @click="openTaskUserPopup(item.id)">取消派发</van-button>
        </div>
        <div b-t-1 b-t-gray-4 flex items-center h-10 justify-end z-36 v-if="backAssignCondition(item)">
          <van-button style="color: white; background: rgb(0, 255, 13); width: 30%; height: 80%"
            @click="backHandle(item)">恢复派发</van-button>
        </div>
      </div>

    </div>
  </div>

  <!-- 底部弹出 筛选 -->
  <van-popup v-model:show="showFilter" position="bottom" round :style="{ height: '40%' }">
    <template #default>
      <div flex justify-between items-center m-b-5 b-b-gray b-b-1 p-l-5 h-10>
        <van-tabs v-model:active="activeName" color="#005A50" m-b-1>
          <van-tab
            title="用户类型"
            name="type"
            title-style="font-size: 16px;margin-right: 10px"
          ></van-tab>
          <van-tab title="派发状态" name="status" title-style="font-size: 16px"></van-tab>
        </van-tabs>
        <div @click="closeFilter" m-r-5 style="color: #005a50">取消</div>
      </div>
      <div
        flex
        justify-start
        items-center
        flex-wrap
        p-l-8
        m-t-10
        m-b-10
        v-if="activeName == 'type'"
      >
        <span :class="userTypeStyle1" @click="changeUserTypeClassName(1)"
          >低非</span
        >
        <span :class="userTypeStyle2" @click="changeUserTypeClassName(2)"
          >高大</span
        >
        <span :class="userTypeStyle3" @click="changeUserTypeClassName(3)"
          >居委</span
        >
      </div>
      <div flex justify-start items-center flex-wrap p-l-8 m-t-10 m-b-10 v-if="activeName == 'status'">
        <span :class="assignStatusStyle1" @click="changeAssignStatusClassName(1)">未派发</span>
        <span :class="assignStatusStyle2" @click="changeAssignStatusClassName(2)">已派发</span>
        <span :class="assignStatusStyle3" @click="changeAssignStatusClassName(3)">已反馈</span>
        <span :class="assignStatusStyle4" @click="changeAssignStatusClassName(4)">不派发</span>
      </div>
      <van-button style="color: white; background: #005a50; width: 90%" m-t-6 @click="submitFilter">确认</van-button>
    </template>
  </van-popup>
  <!-- 底部弹出 选择派发负责方 -->
  <van-popup v-model:show="showChangeAssignTeam" position="bottom" :style="{ height: '35%' }">
    <van-picker title="工程队" :columns="changeAssignTeamColumns" @confirm="onConfirmAssignTeam"
      @cancel="closeShowChangeAssignTeam" />
  </van-popup>
  <!-- 底部弹出 取消派发 -->
  <van-popup v-model:show="showCancelTaskUser" position="bottom" :style="{ height: '35%' }">
    <van-picker title="取消派发" :columns="cancelTaskUserColumns" @confirm="onConfirmCancelTaskUser"
      @cancel="closeShowCancelTaskUser" />
  </van-popup>

  <div fixed bottom-0 bg-white h-12 w-85 v-if="state.engineersTeamId == '' && !endTask" class="bt-btn">
    <div flex justify-between>
      <n-button @click="scanAssignMethod" style="
          background-color: white;
          border: 1px solid #005a50;
          color: #005a50;
        " w-40 h-8 flex justify-center items-center>扫码派发</n-button>
      <n-button @click="allAssignMethod" style="background-color: #005a50" color-white w-40 h-8 flex justify-center
        items-center>全部派发</n-button>
    </div>
  </div>
</template>
<style>
.label-1 {
  font-size: 14px;
  font-weight: 400;
  color: #646464;
}

.label-item {
  display: inline-block;
  color: rgba(0, 0, 0, 0.6);
  font-size: 16px;
  width: Hug (50px);
  height: Hug (22px);
  line-height: 22px;
  padding: 0px, 4px, 0px, 4px;
  border-radius: 2px;
  gap: 4px;
  padding: 2px
}

.c-1 {
  background-color: #F3FFEF;
}

.c-2 {
  background-color: #FFF2EE;
  margin-left: .4rem;
}

.c-3 {
  background-color: #ffd7cb;
  margin-left: .4rem;
  padding: 0px, 3px, 0px, 3px;
}

.c-4 {
  color: #3D7329;
}

.c-5 {
  color: #F57F17;
}

.c-6 {
  color: #f52d17;
}


.filter {
  display: block;
  width: 92px;
  height: 32px;
  font-size: 16px;
  line-height: 32px;
  margin-right: 12px;
  margin-bottom: 12px;
  background: #d9d9d9;
}

.filterActive {
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

.bt-btn {
  background-color: white;
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 3.5rem;
  padding: .5rem 1.2rem;
}
</style>
