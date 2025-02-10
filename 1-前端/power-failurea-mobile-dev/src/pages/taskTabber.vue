<script setup>
import { showToast } from 'vant'
import { Api } from '../utils/api'
import { getLogoSvgToBase64 } from '~/utils/svgBase64'
defineOptions({
  name: 'TaskTabber',
})

const state = useGlobalState()
const searchData = ref('')
const router = useRouter()
const dataList = ref([])
const currentPage = ref(1)
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)
const total = ref(0)
const showFilter = ref(false)
const taskExecuteStatusList = ref([])
const executionStatus = ref([])
const executionStatusStyle1 = ref('executionStatus')
const executionStatusStyle2 = ref('executionStatus')
const executionStatusStyle3 = ref('executionStatus')
const executionStatusStyle4 = ref('executionStatus')
const executionStatusStyle5 = ref('executionStatus')
const executionStatusStyle6 = ref('executionStatus')
const executionStatusStyle7 = ref('executionStatus')
const changeStatus = ref()
const activeName = ref('time')
const activeTaskTabber = ref('task')
const activeTime = ref(0)
const startDateBegin = ref(undefined)
const startDateEnd = ref(undefined)

const date = new Date()
const minDate = ref(
  new Date(
    new Date().getFullYear(),
    new Date().getMonth(),
    new Date().getDate(),
  ),
)
const currentDate = [date.getFullYear(), date.getMonth()+ 1 <10? '0'+(date.getMonth()+1) : date.getMonth()+1, date.getDate() < 10 ? '0'+date.getDate() : date.getDate()]
const columnsType = ['year', 'month', 'day']
const INITIAL_PARAMS = {
  pageIndex: 1,
  pageSize: 10,
  condition: {},
}
onMounted(async () => {
  state.value.showTabber = true
  if (state.value.token) {
    initPage(true)
    // 任务执行状态
    const { data, execute } = usePost(Api.lovList, {
      code: 'task_execute_status',
    })
    execute().then(() => {
      taskExecuteStatusList.value = data.value.data[0]?.lovLineList
    })
  }
})
onBeforeUnmount(() => {
  state.value.showTabber = false
})
async function initPage(init = false) {
  loading.value = true
  if (init)
    currentPage.value = 1
  if (activeTaskTabber.value == 'task') {
    const params = Object.assign(INITIAL_PARAMS, {
      pageIndex: currentPage.value,
      pageSize: 10,
      condition: {
        ...((startDateBegin.value && [...startDateBegin.value].length > 0) && { startDateBegin: [...startDateBegin.value].join('-') }),
        ...((startDateEnd.value && [...startDateEnd.value].length > 0) && { startDateEnd: [...startDateEnd.value].join('-') }),
        ...(searchData.value && { taskCode: searchData.value }),
        executeStatusList:
          refreshing.value
            ? ['120', '130', '140', '150']
            : executionStatus.value && executionStatus.value.length > 0
              ? executionStatus.value
              : ['120', '130', '140', '150'],
      },
    })
    console.log(state.value.engineersTeamId, 'state.engineersTeamId')
    // 当角色为工程队的时候默认筛选出来的执行状态 没有待派发
    if (state.value.engineersTeamId) {
      if (refreshing.value || !executionStatus.value || executionStatus.value.length === 0)
        params.condition.executeStatusList = ['130', '140', '150']
      else
        params.condition.executeStatusList = executionStatus.value
    }
    console.log(params, 'params')

    const { data, execute } = usePost(Api.taskPage, params)
    execute().then(() => {
      init
        ? (dataList.value = data.value.data?.data)
        : (dataList.value = dataList.value.concat(data.value.data?.data))
      console.log(init, dataList.value, 'init: dataList.value ===')
      total.value = data.value.data?.total
      if (dataList.value.length >= total.value)
        finished.value = true

      loading.value = false
    })
  }
  else {
    let params
    if (activeTaskTabber.value == 'taskUserAssignNormal') {
      params = Object.assign(INITIAL_PARAMS, {
        pageIndex: currentPage.value,
        pageSize: 10,
        orderBy: 'main.assign_time desc',
        condition: { assignStatus: 220, receiptCode: searchData.value, taskExecuteStatusList: ['120', '130', '140'] },
      })
      console.log('111111=====')
    }
    else {
      params = Object.assign(INITIAL_PARAMS, {
        pageIndex: currentPage.value,
        pageSize: 10,
        condition: { cancelAssignStatus: 420, receiptCode: searchData.value, taskExecuteStatusList: ['150'] },
      })
    }
    const { data, execute } = usePost(Api.taskUserPage, params)
    execute().then(() => {
      init
        ? (dataList.value = data.value.data?.data)
        : (dataList.value = dataList.value.concat(data.value.data?.data))
      total.value = data.value.data?.total
      if (dataList.value.length >= total.value)
        finished.value = true

      loading.value = false
    })
  }
}
async function onLoad() {
  console.log(refreshing.value)
  if (refreshing.value) {
    dataList.value = []
    currentPage.value = 1
    await initPage(true)
    refreshing.value = false
    return
  }
  else {
    if (dataList.value.length < total.value) {
      console.log('-----')
      currentPage.value += 1
    }
  }
  console.log(currentPage.value, 'currentPage.value')
  console.log(dataList.value.length, 'dataList.value.length')
  console.log(total.value, 'total.value')
  await initPage()
}
function onRefresh() {
  startDateBegin.value = undefined
  startDateEnd.value = undefined
  // 清空列表数据
  finished.value = false
  // 重新加载数据
  // 将 loading 设置为 true，表示处于加载状态
  loading.value = true
  onLoad()
}
function onClickTab() {
  console.log(activeTaskTabber.value, 'activeTaskTabber.value')
  currentPage.value = 1
  initPage(true)
}
function goTaskDetail(record) {
  router.push({ path: '/taskDetail', query: { id: record.id, taskExecuteStatus: record.taskExecuteStatus.executeStatus } })
}
function onSearch(val) {
  console.log(val, 'valval')
  refreshing.value = true
  onRefresh()
}
function openFilter() {
  showFilter.value = true
  // 初始化
  executionStatus.value = []
  startDateBegin.value = currentDate
  startDateEnd.value = currentDate
  // executionStatusStyle1.value = 'executionStatus'
  // executionStatusStyle2.value = 'executionStatus'
  // executionStatusStyle3.value = 'executionStatus'
  // executionStatusStyle4.value = 'executionStatus'
  // executionStatusStyle5.value = 'executionStatus'
  // executionStatusStyle6.value = 'executionStatus'
  // executionStatusStyle7.value = 'executionStatus'
}
function closeFilter() {
  showFilter.value = false
  startDateBegin.value = undefined
  startDateEnd.value = undefined
}
function submitFilter() {
  if (activeName.value == 'time') {
    if (startDateEnd.value && [...startDateEnd.value].length > 0) {
      currentPage.value = 1
      initPage(true)
      showFilter.value = false
    }
    else {
      showToast({ message: '请选择结束时间' })
    }
  }
  else if (activeName.value == 'executionStatus') {
    startDateBegin.value = undefined
    startDateEnd.value = undefined
    if (executionStatus.value) {
      // usePost(Api.taskPage, {})
      initPage(true)
      showFilter.value = false
    }
    else {
      showToast({ message: '请选择执行状态' })
    }
  }
}
function changeStatusClassName(num) {
  const index = executionStatus.value.indexOf(num);
  if (index < 0) {
    // 添加元素
    executionStatus.value.push(num);
  } else {
    // 移除元素
    executionStatus.value.splice(index, 1);
  }
}
function formatter(type, option) {
  if (type === 'year')
    option.text += '年'

  if (type === 'month')
    option.text += '月'

  if (type === 'day')
    option.text += '日'

  return option
}
function goFeedBack(record) {
  router.push({
    path: '/taskUserfeedback',
    query: {
      taskUserId: record.id,
      taskExecuteStatus: record.task?.taskExecuteStatus?.executeStatus,
    },
  })
}
</script>

<template>
  <div style="height:100%;">
    <div style="height:15%;">
      <div m-b-2 flex items-center>
        <img :src="getLogoSvgToBase64()" style="transform: scale(.7); margin-right: 9px;">
        <div font-500 color-white class="fs20">
          停电计划及服务通知
        </div>
      </div>
      <div flex items-center justify-between>
        <div>
          <van-search
            v-model="searchData" placeholder="搜索" input-align="center" bg-white style="padding:0 12px 0 0;width:75vw"
            @search="onSearch"
          />
        </div>
        <van-button
          icon="filter-o" type="primary" style="
            font-size: 18px;
            color: #005a50;
            width: 9vw;
            height: 9vw;
            background-color: white;
            border: 0;
          " @click="openFilter"
        />
      </div>
    </div>

    <div style="height:80%" overflow-scroll>
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了~" offset="5" @load="onLoad">
          <div
            v-for="item in dataList" :key="item.id" relative m-b-5 bg-white p-b-2 :class="{ mt: !Boolean(dataList.length) }"
            @click="goTaskDetail(item)"
          >
            <div m-b-2 h-10 flex items-center justify-around b-b-1 b-b-gray-4>
              <div class="taskCodeStyle">
                计划编号：{{ item?.taskCode }}
              </div>
              <div style="font-size: 14px; color: #858585" w-33>
                {{ item?.confirmTime }}
              </div>
            </div>
            <div>
              <div m-b-2 flex justify-between>
                <div class="labelText" w-27>
                  停电时间：
                </div>
                <div class="valueText" w-70>
                  {{ item?.startTime }}至{{ item?.endTime }}
                </div>
              </div>
              <div m-b-2 flex justify-between>
                <div class="labelText" w-27>
                  停电范围：
                </div>
                <div class="valueText" w-70 overflow-hidden text-ellipsis>
                  {{ item?.ranges }}
                </div>
              </div>
              <div m-b-2 flex justify-between>
                <div class="labelText" w-27>
                  执行状态：
                </div>

                <div class="valueText" w-70>
                  {{
                    JSON.parse(JSON.stringify(taskExecuteStatusList)).filter(
                      (val) => item?.taskExecuteStatus?.executeStatus == val.value,
                    )[0]?.name
                  }}
                </div>
              </div>
              <div m-b-2 flex justify-between>
                <div class="labelText" w-27>
                  派发情况：
                </div>
                <div v-if="!state.engineersTeamId" class="valueText" w-70>
                  <span style="color: #facc15" p-r-1>{{ item?.taskExecuteStatus?.executeStatus == 150
                    ? item?.cancelUnassignedQty : item?.unassignedQty }}</span>|<span p-l-1 color-green-5>{{
                    item?.taskExecuteStatus?.executeStatus == 150 ? item?.cancelAssignedQty : item?.assignedQty
                  }}</span>（<span style="color: #facc15">未派发</span> | <span color-green-5>已派发</span> ）
                </div>
                <div v-else class="valueText" w-70>
                  <span color-green-5>{{ item?.taskExecuteStatus?.executeStatus == 150 ? item?.cancelAssignedQty
                    : item?.assignedQty }}</span>（<span color-green-5>已派发</span>）
                </div>
              </div>
              <div m-b-2 flex justify-between>
                <div class="labelText" w-27>
                  反馈情况：
                </div>
                <div class="valueText" w-70>
                  <span p-r-1 color-red-6>{{ item?.taskExecuteStatus?.executeStatus == 150 ? 0 : item?.rejectedQty
                  }}</span>|<span p-l-1 p-r-1 color-green-5>{{ item?.taskExecuteStatus?.executeStatus == 150
                    ? item?.cancelAgreedQty : item?.agreedQty }}</span>|<span p-l-1 color-yellow-4>{{
                    item?.taskExecuteStatus?.executeStatus == 150 ? item?.cancelUnsignedQty : item?.unsignedQty
                  }}</span>（<span color-red-6>拒签</span> | <span color-green-5>同意</span> | <span color-yellow-4>未签）</span>
                </div>
              </div>
            </div>
            <div m-b-2 flex justify-between>
              <div class="labelText" w-27>
                工作内容：
              </div>
              <div class="valueText" w-70 overflow-hidden text-ellipsis>
                {{ item?.jobContent }}
              </div>
            </div>
            <div absolute right-5 top-28>
              <van-icon name="arrow" color-green-5 />
            </div>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
    <!-- 底部占位 -->
    <div style="height:5%" />
  </div>
  <!-- 底部弹出 -->
  <van-popup v-model:show="showFilter" position="bottom" round :style="{ height: '40%' }">
    <template #default>
      <div m-b-5 flex items-center justify-between b-b-1 b-b-gray p-l-5>
        <van-tabs v-model:active="activeName" color="#005A50">
          <van-tab
            title="停电时间" name="time" title-style="font-size: 16px;margin-right: 10px"
            style="border-top: 2px solid #green"
          />
          <van-tab title="执行状态" name="executionStatus" title-style="font-size: 16px" />
        </van-tabs>
        <div m-r-5 style="color: #005a50" @click="closeFilter">
          取消
        </div>
      </div>
      <van-tabs v-if="activeName == 'time'" v-model:active="activeTime" type="card">
        <van-tab title="选择起始时间">
          <van-date-picker
            v-model="startDateBegin" :show-toolbar="false" :formatter="formatter"
            visible-option-num="3" option-height="40" :columns-type="columnsType"
          />
        </van-tab>
        <van-tab title="选择结束时间">
          <van-date-picker
            v-model="startDateEnd" :show-toolbar="false" :formatter="formatter" visible-option-num="3"
            option-height="40" :columns-type="columnsType"
          />
        </van-tab>
      </van-tabs>
      <div v-if="activeName == 'executionStatus'" m-b-10 m-t-10 flex flex-wrap items-center justify-start p-l-8>
        <!-- <span :class="executionStatusStyle1" @click="changeStatusClassName(110)">待提交</span> -->
        <span v-if="!state.engineersTeamId" :class="['executionStatus',{executionStatusActive:executionStatus.indexOf('120') >-1}]" @click="changeStatusClassName('120')">待派发</span>
        <span :class="['executionStatus',{executionStatusActive:executionStatus.indexOf('130') >-1}]" @click="changeStatusClassName('130')">执行中</span>
        <span :class="['executionStatus',{executionStatusActive:executionStatus.indexOf('140') >-1}]" @click="changeStatusClassName('140')">已反馈</span>
        <span :class="['executionStatus',{executionStatusActive:executionStatus.indexOf('150') >-1}]" @click="changeStatusClassName('150')">取消中</span>

        <span :class="['executionStatus',{executionStatusActive:executionStatus.indexOf('161') >-1}]" @click="changeStatusClassName('161')">已完成</span>
        <span :class="['executionStatus',{executionStatusActive:executionStatus.indexOf('162') >-1}]" @click="changeStatusClassName('162')">已终结</span>
      </div>
      <van-button style="color: white; background: #005a50; width: 90%" m-t-6 @click="submitFilter">
        确认
      </van-button>
    </template>
  </van-popup>
</template>

<style>
.executionStatus {
  display: block;
  width: 92px;
  height: 32px;
  font-size: 16px;
  line-height: 32px;
  margin-right: 12px;
  margin-bottom: 12px;
  background: #d9d9d9;
}

.executionStatusActive {
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

.taskCodeStyle {
  font-size: 14px;
  color: #4959e4;
  font-weight: 400;
}

.fs20 {
  font-size: 16px;
  margin-left: -8px;
  font-family: 'Noto Sans SC';
  font-weight: 400;
}

.mt {
  margin-top: 6vw;
}
</style>
