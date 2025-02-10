<script setup>
defineOptions({
  name: 'riskDetail',
})
import { getLogoSvgToBase64 } from '~/utils/svgBase64'
import { Api } from "../utils/api";
const router = useRouter();
const route = useRoute();
const detailData = ref({})
const taskExecuteStatusList = ref([]);
onMounted(() => {
  if (route.query.businessType == 1) {
    initGetDetail1()
  } else if (route.query.businessType == 2) {
    initGetDetail2()
  } else if (route.query.businessType == 3) {
    initGetDetail3()
  }
  //任务执行状态
  const { data, execute } = usePost(Api.lovList, {
    code: "task_execute_status",
  });
  execute().then(() => {
    taskExecuteStatusList.value = data.value.data[0]?.lovLineList;
  });
})
function initGetDetail1() {
  const { data, execute } = useGet(Api.taskDetail, route.query.id);
  execute().then(() => {
    detailData.value = data?.value?.data;
  });
}
function initGetDetail2() {
  const { data, execute } = useGet(Api.taskUserDetail, route.query.id);
  execute().then(() => {
    detailData.value = data?.value?.data;
  });
}
function initGetDetail3() {
  const { data, execute } = useGet(Api.taskPlanItemDetail, route.query.id);
  execute().then(() => {
    detailData.value = data?.value?.data;
  });
}

function getExecuteStatus(num) {
  switch (num) {
    case '210':
      return '未派发';
    case '220':
      return '已派发';
    case '230':
      return '已反馈';
    case '240':
      return '不派发';
    default:
      return '未知状态';
  }
}
function backFn(){
  router.back()
  state.value.tabberNum=2
}
</script>

<template>
  <div m-b-4>
    <div flex items-center m-b-7 m-t-3>
      <van-icon name="arrow-left" color-white font-500 style="font-size: 20px" @click="backFn" />
      <div font-500 color-white m-l-5 style="font-size: 20px;">风险预警详情</div>
    </div>
    <div bg-white m-b-2 p-l-2 p-r-2 class="pt-2 pb-2 mt-8">
      <div class="mb-2 font-700 title" text-left>通知任务信息</div>
      <div style="font-size: 13px;">
        <div v-if="route.query.businessType != 3">
          <div text-left class="mb-3 flex" >
            <span  class="w-8" text-left>计划编号</span>
            <span text-right class="w-9">{{ route.query.businessType == 1 ? detailData?.taskCode :
              route.query.businessType == 2 ?
                detailData?.task?.taskCode : "" }}
            </span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>停电时间</span>
            <span text-right class="w-9">{{ route.query.businessType == 1 ? detailData?.startTime : route.query.businessType == 2 ?
              detailData?.task?.startTime : '' }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>送电时间</span>
            <span text-right class="w-9">{{ route.query.businessType == 1 ? detailData?.endTime : route.query.businessType == 2 ?
              detailData?.task?.endTime : '' }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>设备名称</span>
            <span text-right class="w-9">{{ route.query.businessType == 1 ? detailData?.deviceName : route.query.businessType == 2 ?
              detailData?.task?.deviceName : '' }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>停电原因</span>
            <span text-right class="w-9" v-if="route.query.businessType == 1">{{ detailData?.reason == 1 ? '设备检修' : detailData?.reason == 2 ?
              '配合客户接入' : '配合市政过程'
            }}</span>
            <span text-right class="w-9" v-else>{{ detailData?.task?.reason == 1 ? '设备检修' : detailData?.task?.reason == 2 ? '配合客户接入' : '配合市政过程'
            }}</span>
          </div>
        </div>
        <div v-else>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>工程编号</span>
            <span text-right class="w-9">{{ detailData?.projectCode }}</span>
          </div>
          <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>工程名称</span>
          <span  inline-block text-right class="w-9  " >{{ detailData?.projectName }}</span>
        </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>停役时间</span>
            <span text-right class="w-9">{{ detailData?.startTime }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>复役时间</span>
            <span text-right class="w-9">{{ detailData?.endTime }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>项目类型</span>
            <span text-right class="w-9">{{ detailData?.projectType }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>变电站/线路名称</span>
            <span text-right class="w-9">{{ detailData?.stationLineName }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>状态</span>
            <span text-right class="w-9">{{ detailData?.executeStatus == '010' ? '未关联' : '已关联' }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>版本</span>
            <span text-right class="w-9">{{ detailData?.version }}.0</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>预告警状态</span>
            <span style="color:orange;" text-right class="w-9">计划变更预警</span>
          </div>
        </div>
        <!-- 下达风险独有部分 开始 -->
        <div text-left class="mb-3 flex" v-if="route.query.alarmType == 2">
          <span inline-block class="w-8" text-left>停电范围</span>
          <span text-right class="w-9">{{ detailData?.ranges }}</span>
        </div>
        <!-- <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>执行状态</span>
          <span>{{
            JSON.parse(JSON.stringify(taskExecuteStatusList)).filter(
              (val) => detailData?.taskExecuteStatus?.executeStatus == val.value
            )[0]?.name
          }}</span>
        </div> -->
        <!-- <div text-left class="mb-3 flex" v-if="route.query.alarmType != 1">
          <span inline-block class="w-8" text-left>任务派发情况</span>
          <span>{{ getExecuteStatus(detailData?.assignStatus) }}</span>
        </div> -->
        <div text-left class="mb-3 flex" v-if="route.query.alarmType == 2">
          <span inline-block class="w-8" text-left>预告警状态</span>
          <span style="color:orange;" text-right class="w-9">下达风险预警</span>
        </div>
        <div text-left class="mb-3 flex" v-if="route.query.alarmType == 2">
          <span inline-block class="w-8" text-left>超期时长</span>
          <span text-right class="w-9">{{ detailData?.overdueDuration }}</span>
        </div>
        <!-- 下达风险独有部分 结束 -->
      </div>
    </div>
    <div bg-white p-l-2 p-r-2 class="pt-2 pb-2 mt-3" v-if="route.query.businessType == 2">
      <div class="mb-2 font-700 title" text-left>回执反馈信息</div>
      <div style="font-size: 13px;">
        <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>回执单号</span>
          <span text-right class="w-9">{{ detailData?.receiptCode }}</span>
        </div>
        <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>户号</span>
          <span text-right class="w-9">{{ detailData?.accountNumber }}</span>
        </div>
        <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>客户名称</span>
          <span text-right class="w-9">{{ detailData?.customerName }}</span>
        </div>
        <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>客户地址</span>
          <span inline-block text-right class="w-9  ">{{ detailData?.customerAddress }}</span>
        </div>
        <!-- 重复停电预警独有部分 开始-->
        <div v-if="route.query.alarmType == 4">
          <div text-left flex>
            <span inline-block class="w-8" text-left>所属台区</span>
            <span text-right class="w-9">{{ detailData?.region }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>电压等级</span>
            <span text-right class="w-9">{{ detailData?.voltageLevel }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>电系编号</span>
            <span text-right class="w-9">{{ detailData?.electricalNumber }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>最后一次停电时间</span>
            <span text-right class="w-9">{{ detailData?.lastPowerCutTime }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>停电间隔天数</span>
            <span text-right class="w-9">{{ detailData?.intervalDays }}</span>
          </div>
          <div text-left class="mb-3 flex">
            <span inline-block class="w-8" text-left>重复停电状态</span>
            <span style="color:orange;" text-right class="w-9">重复停电预警</span>
          </div>
        </div>

        <!--重复停电预警独有部分 结束 -->
        <div text-left class="mb-3 flex" v-if="route.query.alarmType != 4">
          <span inline-block class="w-8" text-left>联系方式</span>
          <span text-right class="w-9">{{ detailData?.phone }}</span>
        </div>
        <div text-left class="mb-3 flex" v-if="route.query.alarmType != 4">
          <span inline-block class="w-8" text-left>派发状态</span>
          <span text-right class="w-9">{{ detailData?.assignStatus == 210 ? '未派发' : detailData?.assignStatus == 220 ? '已派发' :
            detailData?.assignStatus == 230 ? '已反馈' : '不派发' }}</span>
        </div>
        <!-- <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>取消停电通知派发状态</span>
          <span text-right class="w-9">{{ detailData?.cancelAssignStatus == 410 ? '未派发' : detailData?.cancelAssignStatus == 420 ? '已派发' :
            detailData?.cancelAssignStatus == 430 ? '已反馈' : '不派发' }}</span>
        </div> -->
        <div text-left class="mb-3 flex" v-if="route.query.alarmType != 4">
          <span inline-block class="w-8" text-left>反馈情况</span>
          <span  text-right :class="{ 'c-red': detailData?.feedbackStatus == 330 ? true : false ,'w-9':true}">{{ detailData?.feedbackStatus ==
            310 ? '未签'
            : detailData?.feedbackStatus == 320 ? '同意' : '拒签' }}</span>
        </div>
        <!-- <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>取消停电通知反馈情况</span>
          <span>{{ detailData?.cancelFeedbackStatus == 510 ? '未签' : '同意' }}</span>
        </div> -->
        <div text-left class="mb-3 flex" v-if="route.query.alarmType != 4">
          <span inline-block class="w-8" text-left>反馈时间</span>
          <span text-right class="w-9">{{ detailData?.feedbackTime }}</span>
        </div>
        <!-- <div text-left class="mb-3 flex">
          <span inline-block class="w-8" text-left>取消停电通知反馈时间</span>
          <span>{{ detailData?.cancelFeedbackTime }}</span>
        </div> -->
        <!-- 下达超时预警独有部分 开始 -->
        <div text-left flex v-if="route.query.alarmType == 1">
          <span inline-block class="w-8 mb-3" text-left>距离停电时长</span>
          <span text-right class="w-9">{{ detailData?.distanceDuration }}</span>
        </div>
        <div text-left flex v-if="route.query.alarmType == 1">
          <span inline-block class="w-8 mb-3" text-left>预告警状态</span>
          <span style="color: red;" text-right class="w-9">下达超时预警 </span>
        </div>
        <!-- 下达超时预警独有部分 结束 -->
      </div>
    </div>
  </div>

</template>

<style scoped>
.c-red {
  color: red !important;
}

.w-8 {
  width: 35%;
}
.w-9{
  width:65%;
  color: black;
}

.w-47 {
  width: 47vw;
}

.mb-5 {
  margin-bottom: 5px;
}

.mb-3 {
  margin-bottom: 5px;
  color: rgba(0, 0, 0, 0.6);
}

.title {
  border-left: 4px solid #148C78;
  padding-left: .6rem;
  padding-top: .3rem;
  font-size: 16px;
  line-height: 13px;
  font-weight: 600;
  height: 20px;
}

</style>
