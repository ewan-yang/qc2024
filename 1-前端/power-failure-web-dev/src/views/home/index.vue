<template>
  <n-grid cols="3" item-responsive>
    <!-- 左侧 -->
    <n-grid-item span="0:3 1200:2">
      <div class="light-green min-w">

        <!-- 停电计划统计 -->
        <div>
          <n-card>
            <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
              停电计划统计 | &nbsp;<n-select v-model:value="noticeSelectVal" placeholder="本月" :options="options" />
            </div>
            <n-spin :show="noticeLoading">
              <div>
                <n-grid x-gap="24" :cols="6" class="text-center">
                  <n-gi>
                    <div>
                      <div>
                        <n-gradient-text type="info" class="font-40 ">
                          {{ notificationStatistics.releaseQty }}
                        </n-gradient-text>
                      </div>
                      <span>待派发</span>
                    </div>
                  </n-gi>
                  <n-gi>
                    <div>
                      <div>
                        <n-gradient-text type="info" class="font-40 ">
                          {{ notificationStatistics.executionQty }}
                        </n-gradient-text>
                      </div>
                      <span>执行中</span>
                    </div>
                  </n-gi>
                  <n-gi>
                    <div>
                      <div>
                        <n-gradient-text type="info" class="font-40 ">
                          {{ notificationStatistics.feedbackQty }}
                        </n-gradient-text>
                      </div>
                      <span>已反馈</span>
                    </div>
                  </n-gi>
                  <n-gi>
                    <div>
                      <div>
                        <n-gradient-text type="info" class="font-40 ">
                          {{ notificationStatistics.cancelQty }}
                        </n-gradient-text>
                      </div>
                      <span>已取消</span>
                    </div>
                  </n-gi>
                  <n-gi>
                    <div>
                      <div>
                        <n-gradient-text type="info" class="font-40 ">
                          {{ notificationStatistics.finishedQty }}
                        </n-gradient-text>
                      </div>
                      <span>已完成</span>
                    </div>
                  </n-gi>
                  <n-gi>
                    <div>
                      <div>
                        <n-gradient-text type="info" class="font-40 ">
                          {{ notificationStatistics.releaseQty + notificationStatistics.executionQty +
                            notificationStatistics.cancelQty + notificationStatistics.finishedQty +
                             notificationStatistics.feedbackQty }}
                        </n-gradient-text>
                      </div>
                      <span>总数</span>
                    </div>
                  </n-gi>
                </n-grid>
              </div>
            </n-spin>
          </n-card>
        </div>
        <!-- 停电通知统计 -->
        <div>
          <n-card>
            <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
              停电通知统计 | &nbsp;<n-select v-model:value="receiptNoticeSelectVal" placeholder="本月" :options="options" />
            </div>
            <n-spin :show="receiptNoticeLoading">
                <n-grid x-gap="24" :cols="6">
                   <n-gi span="4">
                     <span style="font-weight:700;padding-left:20px">停电通知回执</span>
                   </n-gi>
                   <n-gi span="2">
                     <span style="font-weight:700;padding-left:10px">取消停电通知回执</span>
                   </n-gi>
                </n-grid>
                <n-grid x-gap="24" :cols="6" class="text-center">
                    <n-gi v-if="!auth.userInfo.engineersTeamId">
                      <div>
                        <div>
                          <n-gradient-text type="info" class="font-40 ">
                            {{ receiptNotice.undistributedQty }}
                          </n-gradient-text>
                        </div>
                        <span>未派发</span>
                      </div>
                    </n-gi>
                    <n-gi>
                      <div>
                        <div>
                          <n-gradient-text type="info" class="font-40 ">
                            {{ receiptNotice.noFeedbackQty }}
                          </n-gradient-text>
                        </div>
                        <span>未签</span>
                      </div>
                    </n-gi>
                    <n-gi>
                      <div>
                        <div>
                          <n-gradient-text type="info" class="font-40 ">
                            {{ receiptNotice.refusedSignQty }}
                          </n-gradient-text>
                        </div>
                        <span>拒签</span>
                      </div>
                    </n-gi>
                    <n-gi style="border-right:1px solid #EFEFF5">
                      <div>
                        <div>
                          <n-gradient-text type="info" class="font-40 ">
                            {{ receiptNotice.timeOutQty }}
                          </n-gradient-text>
                        </div>
                        <span>已超时</span>
                      </div>
                    </n-gi>
                    
                    <n-gi v-if="!auth.userInfo.engineersTeamId">
                      <div>
                        <div>
                          <n-gradient-text type="info" class="font-40 ">
                            {{ receiptNotice.cancelUndistributedQty }}
                          </n-gradient-text>
                        </div>
                        <span>未派发</span>
                      </div>
                    </n-gi>
                    <n-gi>
                      <div>
                        <div>
                          <n-gradient-text type="info" class="font-40 ">
                            {{ receiptNotice.cancelNoFeedbackQty }}
                          </n-gradient-text>
                        </div>
                        <span>未签</span>
                      </div>
                    </n-gi>
                </n-grid>
            </n-spin>
          </n-card>
        </div>
        <!-- 停电计划清单表格 -->
        <div>
          <n-card>
            <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
              停电计划清单 | &nbsp;<n-select placeholder="全部" v-model:value="tableSelectVal" :options="optionsTable" />
            </div>
            <!-- 表格 -->
            <n-spin :show="loading">
              <n-data-table :columns="columns" :data="data" :max-height="520" :min-height="520" :scroll-x="1600"
                :bordered="false" size="small" :single-line="false" :scrollbar-props="scrollbarProps" />
              <!-- 分页 -->
              <div class="pageSty">
                <n-pagination v-model:page="pageData.pageIndex" :page-count="pageCount" :pageSizes="pageSizes"
                  show-quick-jumper show-size-picker :on-update:page="pageChangeFn"
                  :on-update:pageSize="pageSizeChangeFn" />
              </div>
            </n-spin>
          </n-card>
        </div>

      </div>
    </n-grid-item>

    <!-- 右侧 -->
    <n-grid-item span="0 1200:1">
      <div class="m-w530">
        <!-- 预告警信息卡片 -->
        <n-card>
          <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
            预告警信息
            <n-select placeholder="全部" v-model:value="warningInfoSelectVal" :options="optionsRight" class="float-right"
              style="width: 130px !important;" />
          </div>
          <!-- 告警盒子 -->
          <n-spin :show="warnLoading">
            <div class="warningContent">
              <template v-if="Object.keys(warningContent).length">
                <template v-for=" item in warningContent">
                  <div class="wc-wrap1">
                    <div class="font-700  p-l-2 wc-wrap2">
                      <div class="wc-left" :style="isWarning(item.alarmTitle) ? 'color:orange;border-color:orange' : ''">
                        <n-icon>
                          <icon-ant-design:warning class="wc-icon" />
                        </n-icon> {{ item.alarmTitle }}&nbsp;
                      </div>
                      <div class="wd-350">
                        <p class="wc-center-p">
                          {{ item.alarmContent }}
                        </p>
                      </div>
                      <div>
                        <p class="wc-right-p">
                          {{ item.alarmTime.slice(0, 10) }}
                        </p>
                      </div>
                    </div>
                  </div>
                </template>
              </template>
              <template v-else>
                <div class="empty">
                  <n-empty description="暂无数据">
                  </n-empty>
                </div>
              </template>
            </div>
          </n-spin>
        </n-card>
        <!-- 停电计划通知卡片 -->
        <n-card>
          <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
            通知
            <n-select placeholder="全部" v-model:value="planInfoSelectVal" :options="optionsRight2" class="float-right"
              style="width: 160px !important;" />
          </div>
          <!-- 计划通知盒子 -->
          <n-spin :show="planContentLoading">
            <div class="planContent">
              <template v-if="planContent.length">
                <template v-for=" item in planContent">
                  <div class="pc-wrap1">
                    <div class="font-700  p-l-2 pc-wrap2">
                      <div class="pc-left">
                        {{ item.title }}
                      </div>
                      <div class="pc-center">
                        <p class="pc-center-p">
                          {{ item.content }}
                        </p>
                      </div>
                      <div class="pc-right">
                        <p class="pc-right-p">
                          {{ item.createDate.slice(0, 10) }}
                        </p>
                      </div>
                    </div>
                  </div>
                </template>
              </template>
              <template v-else>
                <div class="empty">
                  <n-empty description="暂无数据">
                  </n-empty>
                </div>
              </template>
            </div>
          </n-spin>
        </n-card>
      </div>
    </n-grid-item>

  </n-grid>

  <!-- 关联通知单抽屉 -->
  <TaskDetailDrawer ref="modalRef" :transformId="transfromData.id" transformIdType="task"></TaskDetailDrawer>
</template>

<script lang="ts" setup>
import { ref, reactive, h, onMounted, watch, nextTick } from 'vue'
import { NTag } from 'naive-ui'
import { receiptNoticeStatisticsAPI,selectAlarmBusinessListAPI, notificationStatisticsAPI, powerCutPageAPI, planStatisticsAPI, planContentAPI } from '../../service/api'
import { localStg } from "@/utils";
import { useAuthStore } from '@/store';
const auth = useAuthStore();

//=============================== 计算季度，年份，月份函数
// 获取本月开始时间和结束时间
function getMonthDates() {
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1;

  const createDateBegin = `${year}-${month.toString().padStart(2, '0')}-01`;
  const lastDay = new Date(year, month, 0).getDate();
  const createDateEnd = `${year}-${month.toString().padStart(2, '0')}-${lastDay}`;

  return {
    createDateBegin,
    createDateEnd
  };
}

// 获取本季度开始时间和结束时间
function getQuarterDates() {

  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth();

  const seasonStartMonthArr = [1, 4, 7, 9]
  const seasonStartMonth = seasonStartMonthArr[month % 3 === 0 ? parseInt((month / 3).toString()) - 1 : parseInt((month / 3).toString())]
  const createDateBegin = `${year}-${seasonStartMonth.toString().padStart(2, '0')}-01`;

  const lastDay = new Date(year, seasonStartMonth + 2, 0).getDate();

  const createDateEnd = `${year}-${(seasonStartMonth + 2).toString().padStart(2, '0')}-${lastDay}`;

  return {
    createDateBegin,
    createDateEnd
  };
}

// 获取本年开始时间和结束时间
function getYearDates() {
  const now = new Date();
  const year = now.getFullYear();

  const createDateBegin = `${year}-01-01`;
  const lastDay = new Date(year, 12, 0).getDate();
  const createDateEnd = `${year}-12-${lastDay}`;

  return {
    createDateBegin,
    createDateEnd
  };
}
//=============================== 计算季度，年份，月份函数



//=============================== 统计

// 年月季度查询条件定义
const searchDate = ref({})

const noticeLoading = ref(true)

// 停电通知统计
const notificationStatistics = ref({})
// 数据更新
async function notificationStatisticsInit(params) {
  noticeLoading.value = true
  const notificationStatisticsArr = await notificationStatisticsAPI(params)
  notificationStatistics.value = notificationStatisticsArr.data
  let timer = setTimeout(() => {
    noticeLoading.value = false
    clearTimeout(timer)
  }, 300)
}
// 选择框
let noticeSelectVal = ref('month')
//监听
watch(() => noticeSelectVal.value, () => {
  console.log(noticeSelectVal.value)
  switch (noticeSelectVal.value) {
    case 'month':
      searchDate.value = getMonthDates()
      break;
    case 'season':
      searchDate.value = getQuarterDates()
      break;
    case 'year':
      searchDate.value = getYearDates()
      break;
  }
  notificationStatisticsInit(searchDate.value)
})

//停电通知回执
const receiptNotice = ref({
  undistributedQty: 0,
  noFeedbackQty: 0,
  refusedSignQty: 0,
  timeOutQty: 0,
  cancelUndistributedQty: 0,
  cancelNoFeedbackQty: 0
})
// 年月季度查询条件定义
const receiptSearchDate = ref({})

const receiptNoticeLoading = ref(true)
// 选择框
let receiptNoticeSelectVal = ref('month')
//监听
watch(() => receiptNoticeSelectVal.value, () => {
  console.log(receiptNoticeSelectVal.value)
  switch (receiptNoticeSelectVal.value) {
    case 'month':
      receiptSearchDate.value = getMonthDates()
      break;
    case 'season':
      receiptSearchDate.value = getQuarterDates()
      break;
    case 'year':
      receiptSearchDate.value = getYearDates()
      break;
  }
  receiptNoticeStatisticsInit(receiptSearchDate.value)
})
// 数据更新
async function receiptNoticeStatisticsInit(params) {
  receiptNoticeLoading.value = true
  const receiptNoticeStatisticsArr = await receiptNoticeStatisticsAPI(params)
  receiptNotice.value = receiptNoticeStatisticsArr.data
  let timer = setTimeout(() => {
    receiptNoticeLoading.value = false
    clearTimeout(timer)
  }, 300)
}


//=============================== 统计



//=============================== 右侧列表

// 判断标题颜色
function isWarning(str) {
  let arr = ["下达风险告警", "重复停电预警", "计划变更预警"]
  return Boolean(arr.indexOf(str) + 1);
}


let warnLoading = ref(true)

// 预告警信息
const warningContent = ref({})

// 预告警信息初始化函数
async function warningInfoInit(params = '') {
  warnLoading.value = true
  const warningInfoArr = await selectAlarmBusinessListAPI({ alarmTitle: params })
  warningContent.value = warningInfoArr.data
  let timer = setTimeout(() => {
    warnLoading.value = false
    clearTimeout(timer)
  }, 300)
}

//选择框
let warningInfoSelectVal = ref('')

// 监听
watch(() => warningInfoSelectVal.value, () => {
  warningInfoInit(warningInfoSelectVal.value)
})


// 停电计划通知
const planContent = ref([])

let planContentLoading = ref(true)
// 预告警信息初始化函数
async function planInfoInit(params = '') {
  planContentLoading.value = true
  const planInfoArr = await planContentAPI(params)
  planContent.value = planInfoArr.data.data
  let timer = setTimeout(() => {
    planContentLoading.value = false
    clearTimeout(timer)
  }, 300)
}

//选择框
let planInfoSelectVal = ref('')

// 监听
watch(() => planInfoSelectVal.value, () => {
  planInfoInit(planInfoSelectVal.value)
})

//=============================== 右侧列表

// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)


// ==================停电计划和通知统计多选框
const options = ref([
  {
    label: '本月',
    value: 'month',

  },
  {
    label: '本季度',
    value: 'season'
  },
  {
    label: '本年',
    value: 'year'
  }
])

const optionsTable = ref([
  {
    label: '全部',
    value: '',

  },
  {
    label: '待提交',
    value: '110',

  },
  {
    label: '待派发',
    value: '120',

  },
  {
    label: '执行中',
    value: '130'
  },
  {
    label: '已反馈',
    value: '140'
  },
  {
    label: '取消中',
    value: '150',

  },
  {
    label: '已完成',
    value: '161'
  },
  {
    label: '已终结',
    value: '162'
  }
])

const optionsRight = ref([
  {
    label: '全部',
    value: ''
  },
  {
    label: '下达超时预警',
    value: '下达超时预警'
  },
  {
    label: '下达风险预警',
    value: '下达风险预警'
  },
  {
    label: '用户拒签告警',
    value: '用户拒签告警'
  },
  {
    label: '重复停电预警',
    value: '重复停电预警'
  },
  {
    label: '计划变更预警',
    value: '计划变更预警'
  }
])
const optionsRight2 = ref([
  {
    label: '全部',
    value: ''
  },
  {
    label: '任务派发通知',
    value: '任务派发通知'
  },
  {
    label: '任务取消通知',
    value: '任务取消通知'
  },
  {
    label: '任务派发取消通知',
    value: '任务派发取消通知'
  },
  {
    label: '停电任务新增',
    value: '停电任务新增'
  },
  {
    label: '停电任务变更',
    value: '停电任务变更'
  },
])
// ==================停电计划和通知统计多选框


// ==================表格

// 表格加载中
const loading = ref(true)

const tableSelectVal = ref('')

// 监听
watch(() => tableSelectVal.value, () => {
  columns.value = columnsInit.value
  if (tableSelectVal.value === '161' || tableSelectVal.value === '162') {
    columns.value = columnsInit.value.slice(0, columns.value.length - 1)
    console.log(1)
  }
  pageInit(tableSelectVal.value)
  pageData.pageIndex = 1
})

// 表格横向滚动
const scrollbarProps = ref({
  XScrollable: true
})

// 表格数据
let data = ref([])
let columnsInit: any = ref([
  // {
  //   title: 'id',
  //   key: 'id',
  //   width: 0,
  //   fixed: 'left'
  // },
  {
    title: '停电计划编号',
    key: 'taskCode',
    width: 50,
    fixed: 'left',
    render(row) {
      return h(
        'span',
        {
          style: {
            color: '#0062E1',
            cursor: 'pointer',
            display: 'inline-block',
            height: '3rem',
            lineHeight: '3rem',
          },
          onClick: () => {
            transfromData.value = row
            nextTick(() => {
              modalRef.value?.open()
            })
          }
        },
        { default: () => `${row.taskCode}` }
      )
    }
  },
  {
    title: '停电时间',
    key: 'startTime',
    width: 60,
  },
  {
    title: '送电时间',
    key: 'endTime',
    width: 60,
  },
  {
    title: '停电范围',
    key: 'ranges',
    width: 100,
  },
  {
    title: '状态',
    key: 'executeStatus',
    width: 30,
    render(row) {
      const code = row.taskExecuteStatus.executeStatus
      let statusArr: string[] = []
      let statusIndex = 0
      if (code.includes('6')) {
        statusArr = ['已完成', '已终结']
        statusIndex = code % 160 - 1
      } else {
        statusArr = ['待提交', '待派发', '执行中', '已反馈', '取消中']
        statusIndex = (code / 10) % 10 - 1
      }
      return h(
        'span',
        {},
        { default: () => `${statusArr[statusIndex]}` }
      )
    }
  },
  {
    title: `通知派发情况`,
    key: 'no',
    align: 'center',
    children: [
      {
        title: ' 未派发',
        key: 'unassignedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.unassignedQty == 0 ? 'c-6' : 'c-4'
            },
            { default: () => `${row.unassignedQty}` }
          )
        }
      },
      {
        title: '已派发',
        key: 'assignedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.assignedQty == 0 ? 'c-6' : 'c-7'
            },
            { default: () => `${row.assignedQty}` }
          )
        }
      }
    ]
  },
  {
    title: `用户反馈情况`,
    key: 'no',
    align: 'center',
    children: [
      {
        title: ' 拒签',
        key: 'rejectedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.rejectedQty == 0 ? 'c-6' : 'c-4'
            },
            { default: () => `${row.rejectedQty}` }
          )
        }
      },
      {
        title: '同意',
        key: 'agreedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.agreedQty == 0 ? 'c-6' : 'c-7'
            },
            { default: () => `${row.agreedQty}` }
          )
        }
      },
      {
        title: '未签',
        key: 'unsignedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.unsignedQty == 0 ? 'c-6' : 'c-1'
            },
            { default: () => `${row.unsignedQty}` }
          )
        }
      },
    ]
  },
  {
    title: `取消派发情况`,
    key: 'no',
    align: 'center',
    children: [
      {
        title: ' 已派发',
        key: 'cancelAssignedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.cancelAssignedQty == 0 ? 'c-6' : 'c-7'
            },
            { default: () => `${row.cancelAssignedQty}` }
          )
        }
      },
      {
        title: '未派发',
        key: 'cancelUnassignedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.cancelUnassignedQty == 0 ? 'c-6' : 'c-1'
            },
            { default: () => `${row.cancelUnassignedQty}` }
          )
        }
      }
    ]
  },
  {
    title: `取消反馈情况`,
    key: 'no',
    align: 'center',
    children: [
      {
        title: ' 同意',
        key: 'cancelAgreedQty',
        width: 25,
        render(row) {
          return h(
            'span',
            {
              class: row.cancelAgreedQty == 0 ? 'c-6' : 'c-7'
            },
            { default: () => `${row.cancelAgreedQty}` }
          )
        }
      },
      {
        title: '未签',
        key: 'cancelUnsignedQty',
        width: 25,
        className: 'c-1',
        render(row) {
          return h(
            'span',
            {
              class: row.cancelUnsignedQty == 0 ? 'c-6' : 'c-1'
            },
            { default: () => `${row.cancelUnsignedQty}` }
          )
        }
      }
    ]
  },
  {
    title: '预告警',
    width: 50,
    fixed: 'right',
    render(row) {
      const alarmTypeArr = ["正常", "下达超时告警", "下达风险告警", "用户拒签告警", "重复停电预警", "计划变更预警"]
      let type = 'error'
      switch (row.advanceStatus) {
        case 0:
          type = 'success'
          break;
        case 2:
        case 4:
        case 5:
          type = 'warning'
          break;
        default:
          type = 'error'
      }
      return h(
        NTag,
        {
          size: 'small',
          type: `${type}`,
        },
        { default: () => `${alarmTypeArr[row.advanceStatus]}` }
      )
    }
  }
])
let columns = ref([])
columns.value = columnsInit.value

// // 假数据
// data.value = [
//   {
//     taskId: "5",
//     taskCode: "RW20230169",
//     startTime: "2023-08-20 04:00",
//     endTime: "2023-08-20 06:00",
//     confirmTime: "2023 -08 - 14 10: 24: 52",
//     type: 1,
//     reason: 1,
//     stationName: "繁兴繁兴",
//     lineName: "繁兴繁兴",
//     deviceName: "繁兴繁兴",
//     ranges: "繁兴繁兴",
//     location: null,
//     jobContent: null,
//     unassignedQty: 0,
//     assignedQty: 5,
//     cancelledQty: 0,
//     rejectedQty: 3,
//     agreedQty: 1,
//     unsignedQty: 1,
//     executeStatus: "130",
//     overdueDuration: 2,
//     version: 2,
//     alarmType: 2
//   }
// ]

// 分页
let pageData = reactive({ pageIndex: 1, pageSize: 10 })
let pageSizes = reactive([10, 20, 30, 40, 50])
let pageCount = ref(10)

//页面加载
onMounted(() => {
console.log('release20240929_1')

  searchDate.value = getMonthDates()
  notificationStatisticsInit(searchDate.value)
  receiptSearchDate.value = getMonthDates()
  receiptNoticeStatisticsInit(receiptSearchDate.value)
  pageInit('')
  warningInfoInit('')
  planInfoInit('')
})


// 页面初始化函数
async function pageInit(params) {
  loading.value = true
  let dataArr = {}
  if (params === '') {
    dataArr = await powerCutPageAPI({ ...pageData, condition: { executeStatusList: ['110', '120', '130', '140', '150'] } })
  } else {
    dataArr = await powerCutPageAPI({ ...pageData, condition: { executeStatusList: [params] } })
  }
  let timer = setTimeout(() => {
    loading.value = false
    clearTimeout(timer)
  }, 300)
  if (dataArr.data.count === 0 && pageData.pageIndex !== 0) {
    pageData.pageIndex -= 1
  }
  pageCount.value = dataArr.data.pageCount
  data.value = dataArr.data.data
}


// 监听分页数据
watch(() => pageData, () => {
  pageInit(tableSelectVal.value)
}, { deep: true })

// 当前页改变
function pageChangeFn(page) {
  pageData.pageIndex = page
}
// 分页数改变
function pageSizeChangeFn(pageSizeIndex) {
  pageData.pageSize = pageSizeIndex
}

// ==================表格
</script>

<style scoped>
.wd-350 {
  width: 365px;
}

.empty {
  display: flex;
  justify-content: center;
  align-items: center;
  height: inherit;
  margin-right: -20px;
}

.pc-right-p {
  font-size: 12px;
  font-weight: 500;
  width: 70px;
  margin-left: 2px;
}

.pc-right {
  width: 80px;
}

.pc-center-p {
  font-size: 12px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  transform: scale(.96);
}

.pc-left {
  width: 120px;
  border-right: 1px solid #044FE1;
  font-size: 12px;
  margin-left: -12px;
  height: 20px;
  color: #044FE1;
  text-align: center;
}

.pc-wrap1 {
  border-bottom: 1px solid rgb(228, 228, 228);
  padding: 3px;
  min-width: 360px;
  margin-bottom: 10px;
}

.pc-wrap2 {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 55px;

}

.planContent {
  height: 340px;
  overflow: scroll;
  overflow-x: hidden;
}

.wc-right-p {
  font-size: 12px;
  font-weight: 500;
  width: 70px;
  line-height: 30px;
  margin-right: -6px;
}

.wc-center-p {
  font-size: 12px;
  font-weight: 500;
  line-height: 15px;
  overflow: hidden;
  height: 45px;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  transform: scale(.94);
  margin-left: -4px;
}

.wc-icon {
  font-size: 15px;
  line-height: 10px;
}

.wc-left {
  width: 100px;
  height: 30px;
  border-right: 1px solid red;
  line-height: 30px;
  font-size: 12px;
  color: red;
  transform: scale(.9);
  margin-left: -11px;
}

.wc-wrap2 {
  height: 48px;
  display: flex;
  justify-content: center;
}

.wc-wrap1 {
  border-bottom: 1px solid rgb(228, 228, 228);
  padding: 3px;
  margin-bottom: 10px;
}

.warningContent {
  height: 40vh;
  overflow: scroll;
  overflow-x: hidden;
  margin-left: -10px;
  min-width: 518px;
}

.m-w530 {
  min-width: 530px;
}

.pageSty {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.n-data-table-th__title) {
  color: black !important;
}

:deep(.c-1) {
  color: #F59A23 !important;
}

:deep(.c-2) {
  color: #0096FA;
}

:deep(.c-3) {
  color: #044FE1;
}

:deep(.c-4) {
  color: #D9001B;
}

:deep(.c-5) {
  color: #0000FF;
}

:deep(.c-6) {
  color: #001410;
}

:deep(.c-7) {
  color: green;
}

:deep(.n-select) {
  display: inline-block !important;
  width: 100px !important;
}


.font-18 {
  font-size: 18px;
}

.font-40 {
  font-size: 40px;
}

.min-w {
  min-width: 750px;
}

.pc-center {
  width: 330px;
}

:deep(.n-data-table-tr td) {
  height: 70px !important;
}
</style>