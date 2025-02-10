<template>
  <n-drawer v-model:show="props.showDrawer" :on-mask-click="maskClose" :on-after-enter="enterDrawer"
    style="width: 90%;min-width:1580px ">
    <n-drawer-content>
      <!-- 头部 -->
      <template #header>
        <div class="flex justify-between items-center mb-10">
          <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
            停电计划清单
          </div>
          <div class="absolute right-7 mt-12">
            <n-button type="primary" ghost size="small" @click="emit('close')">返回</n-button>
          </div>
        </div>
        <!-- 查询表单组 -->
        <div class="flex flex-justify-around">
          <n-form ref="searchFormRef" :model="model" inline class="flex flex-wrap">
            <n-form-item path="startTime" label="停役时间" label-placement="left">
              <n-date-picker v-model:value="model.startTime" type="daterange" clearable @keydown.enter.prevent
                class="width-250" />
            </n-form-item>
            <n-form-item path="endTime" label="复役时间" label-placement="left">
              <n-date-picker v-model:value="model.endTime" type="daterange" clearable @keydown.enter.prevent
                class="width-250" />
            </n-form-item>
            <n-form-item path="projectType" label="项目类型" label-placement="left">
              <n-input v-model:value="model.projectType" @keydown.enter.prevent class="width-150" />
            </n-form-item>
            <n-form-item path="constructionTeam" label="施工班组" label-placement="left">
              <n-input v-model:value="model.constructionTeam" @keydown.enter.prevent class="width-150" />
            </n-form-item>
            <n-form-item path="executeStatus" label="状态" label-placement="left">
              <n-select v-model:value="model.executeStatus" :options="options" @keydown.enter.prevent class="width-150" />
            </n-form-item>
          </n-form>
          <n-button-group class="btn-group">
            <n-space class="flex flex-nowrap">
              <n-button color="#BAD8FB" style="color: black;" @click="searchFn">
                查询
              </n-button>
              <n-button type="primary" ghost @click="resetFn">
                重置
              </n-button>
            </n-space>
          </n-button-group>
        </div>
      </template>
      <!-- 内容 -->
      <template #default>
        <n-card>
          <n-space class="flex flex-nowrap mb-4">
            <!-- <n-button type="primary" @click="showPlanModalFn( 'add' )"> -->
            <n-button type="primary" @click="addFn('add', props.planId)">
              新增
            </n-button>
            <n-popconfirm @positive-click="sureDeleteAllFn" @negative-click="cancelDeleteAllFn">
              <template #trigger>
                <n-button type="primary" ghost>
                  批量删除
                </n-button>
              </template>
              是否删除所选停电计划？
            </n-popconfirm>
          </n-space>
          <!-- 表格 -->
          <n-spin :show="loading">
            <n-data-table :columns="columns" :data="data" :max-height="730" :scroll-x="3000" :bordered="false"
              :single-line="false" :scrollbar-props="scrollbarProps" @update:checked-row-keys="handleCheck"
              :row-key="rowKey" />
            <!-- 分页 -->
            <div class="pageSty">
              <n-pagination v-model:page="pageData.pageIndex" :page-count="pageCount" :pageSizes="pageSizes"
                show-quick-jumper show-size-picker :on-update:page="pageChangeFn"
                :on-update:pageSize="pageSizeChangeFn" />
            </div>
          </n-spin>
        </n-card>
      </template>
    </n-drawer-content>
  </n-drawer>

  <!-- 停电计划详情抽屉框 -->
  <ProjectCodeDrawer ref="projectRef" @init="pageInit" @mainPageInit="emit('mainPageInit')" />
</template>

<script setup lang="ts" name="PlanDrawer">
import { ref, reactive, h, watch, defineProps, defineEmits, nextTick } from 'vue'
import { NPopconfirm, NButton, FormInst,useMessage, } from 'naive-ui'


import { projectPageAPI, delAllPlanItemAPI ,delPlanItemAPI} from '../../service/api'

// 子组件ref==========================

const projectRef = ref(null)
// 增加
function addFn(type, planId) {
  projectRef.value?.operateDrawer({ type, planId })
}

// 子组件ref==========================


// 查询状态的可选择项
let options = [
  {
    label: '未关联',
    value: '010'
  },
  {
    label: '已关联',
    value: '020'
  }
]

// 主页面传参
const props = defineProps({
  planId: {
    type: String,
    required: true
  },
  showDrawer: {
    type: Boolean,
    required: true
  }
})
// 主页面传递的函数
const emit = defineEmits(['close', 'mainPageInit'])
// 主页初始化

function maskClose() {
  emit('close')
}

const message = useMessage()

const scrollbarProps = ref({
  XScrollable: true
})

// ========查询相关====================
interface SearchModelType {
  startTime: number | null
  endTime: number | null
  projectType: string | null
  constructionTeam: string | null
  executeStatus: string | null

}

// 时间戳转换函数
function formatDate(timestamp, isMonth = false) {
  if (!timestamp) {
    return null
  }
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  let formattedDate
  if (isMonth) {
    formattedDate = `${year}-${month}`;
  } else {
    formattedDate = `${year}-${month}-${day}`;
  }
  return formattedDate;
}


// 是否是查询数据标记
const searchFlag = ref(false)
// 查询条件
let condition: any = ref({})


const searchFormRef = ref<FormInst | null>(null)

const searchModelRef = ref<SearchModelType>({
  startTime: null,
  endTime: null,
  projectType: null,
  constructionTeam: null,
  executeStatus: null
})

const model = searchModelRef

function resetFn() {
  model.value = {
    startTime: null,
    endTime: null,
    projectType: null,
    constructionTeam: null,
    executeStatus: null
  }
  searchFlag.value = false
  pageData.pageIndex = 1
  pageInit()
}


function searchFn() {
  let { projectType, constructionTeam, executeStatus } = model.value
  let startTimeStart = formatDate(model.value.startTime?.[0])
  let startTimeEnd = formatDate(model.value.startTime?.[1])
  let endTimeStart = formatDate(model.value.endTime?.[0])
  let endTimeEnd = formatDate(model.value.endTime?.[1])
  condition = { planId: props.planId, projectType, constructionTeam, executeStatus, startTimeStart, startTimeEnd, endTimeStart, endTimeEnd }
  pageData.pageIndex = 1
  searchFlag.value = true
  searchDataFn(condition)
}

// 查询函数
async function searchDataFn(condition) {
  const dataArr = await projectPageAPI({ ...pageData, condition })
  if (dataArr.data.count === 0 && pageData.pageIndex !== 0) {
    pageData.pageIndex -= 1
  }
  pageCount.value = dataArr.data.pageCount
  data.value = dataArr.data.data
}
// ========查询相关====================

// ======================================
// 表格表头数据
const createColumns = () => [
  {
    type: 'selection'
  },
  // {
  //   title: 'id',
  //   key: 'id',
  //   width: 0,
  //   fixed: 'left'
  // },
  {
    title: '工程编号',
    key: 'projectCode',
    width: 120,
    fixed: 'left',
    render(row) {
      return h(
        'span',
        {
          style: {
            color: '#0062E1',
            cursor: 'pointer'
          },
          onClick: () => {
            nextTick(() => {
              projectRef.value?.operateDrawer({ type: 'view', row })
            })
          }
        },
        { default: () => `${row.projectCode}` }
      )
    }
  },
  {
    title: '状态',
    key: 'executeStatus',
    width: 70,
    fixed: 'left',
    render(row) {
      let status = ''
      switch (row.executeStatus) {
        case '010':
          status = '未关联'
          break
        case '020':
          status = '已关联'
          break

      }
      return h(
        'span',
        {
          style:{
            display: 'inline-block',
            height:'4rem',
            lineHeight: '4rem',
          }
        },
        { default: () => `${status}` }
      )
    }
  },
  {
    title: '计划年月',
    key: 'plan.planMonth',
    width: 90,
  },
  {
    title: '工程账号',
    key: 'projectAccount',
    width: 140,
  },
  {
    title: '工程名称',
    key: 'projectName',
    width: 300,
  },
  {
    title: '项目类型',
    key: 'projectType',
    width: 100,
  },
  {
    title: '变电站/线路名称',
    key: 'stationLineName',
    width: 100,
  },
  {
    title: '停役设备',
    key: 'no1',
    align: 'center',
    children: [
      {
        title: '(注:仅供参考,请以现场勘查后实际停役单为准)',
        key: 'cosDevice',
        align: 'center',
        width: 280
      }
    ]
  },
  {
    title: '停役时间',
    key: 'startTime',
    width: 100,
  },
  {
    title: '复役时间',
    key: 'endTime',
    width: 100,
  },
  {
    title: '主要工作内容及相关验收部门',
    key: 'no1',
    align: 'center',
    children: [
      {
        title: '(注:仅供参考,请以设计图纸及现场实际工作内容为准)',
        key: 'jobContent',
        align: 'center',
        width: 350
      }
    ]
  },
  {
    title: '施工班组',
    key: 'constructionTeam',
    width: 100,
  },
  {
    title: '操作时间',
    key: 'updateDate',
    width: 130,
  },
  {
    title: '版本',
    key: 'version',
    width: 80,
    fixed: 'right'
  },
  {
    title: '操作',
    width: 130,
    fixed: 'right',
    render(row) {
      return [
        h(
          NButton,
          {
            size: 'small',
            type: 'primary',
            style: 'margin:0 10px;color:#044FE1',
            color:'transparent',
            onClick: () => nextTick(() => {
              projectRef.value?.operateDrawer({ type: 'edit', row })
            })
          },
          { default: () => '编辑' }
        ),

        h(
          NPopconfirm,
          {
            negativeText: '取消', positiveText: '确认',
            onPositiveClick: () => cancelItemFn(row),
            onNegativeClick: () => message.info('取消')
          },
          {
            trigger: () => h(
              NButton,
              {
                size: 'small',
                style: 'margin:0 10px;color:rgba(0, 0, 0, 0.6)',
                color:'transparent',
                disabled: row.executeStatus=='020'?true:false
              },
              { default: () => '删除' },
            ),
            default: () => h(
              'span',
              {},
              { default: () => '是删除所选停电计划？' }
            ),
          })
      ]
    }
  }
]

//删除项
async function cancelItemFn(row) {
  const data = await delPlanItemAPI([row.id])
  console.log(data,[row.id])
  if (!data.error) {
    message.success('删除成功')
    pageInit()
  } else {
    message.error('删除失败')
  }
}

// 批量删除
function cancelDeleteAllFn() {
  message.info('取消')
}

async function sureDeleteAllFn() {
  // console.log(deleteArr.value)
  const data = await delAllPlanItemAPI(deleteArr.value)
  if (!data.error) {
    message.success('删除成功')
    pageInit()
  } else {
    message.error('删除失败')
  }
}
let deleteArr = ref([])

// 多选函数
function handleCheck(rowKeys) {
  deleteArr.value = rowKeys
}

function rowKey(row) {
  return row.id
}


// 表格数据
let data = ref([])
// // 假数据
// data.value = [{
//   id: 1,
//   projectCode: "123",
//   executeStatus: "010",
//   planMonth: "2019-08-24",
//   projectAccount: "string",
//   projectName: "string",
//   projectType: "string",
//   stationLineName: "string",
//   cosDevice: "string",
//   startTime: "2019-08-24",
//   endTime: "2019-08-24",
//   jobContent: "string",
//   constructionTeam: "string",
//   version: 0,
//   alarmType: 0
// },
// {
//   id: 2,
//   projectCode: "123",
//   executeStatus: "010",
//   planMonth: "2019-08-24",
//   projectAccount: "string",
//   projectName: "string",
//   projectType: "string",
//   stationLineName: "string",
//   cosDevice: "string",
//   startTime: "2019-08-24",
//   endTime: "2019-08-24",
//   jobContent: "string",
//   constructionTeam: "string",
//   version: 0,
//   alarmType: 0
// }]

let columns: any = reactive([])

// 表格列数据
columns = createColumns()

// 分页
let pageData = reactive({ pageIndex: 1, pageSize: 10 })
let pageSizes = reactive([10, 20, 30, 40, 50])
let pageCount = ref(10)

//页面加载
function enterDrawer() {
  pageInit()
}

const loading = ref(true)

// 页面初始化函数
async function pageInit() {
  loading.value = true
  const params = { ...pageData, condition: { planId: props.planId } }
  const dataArr = await projectPageAPI(params)
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
  if (searchFlag.value) {
    searchDataFn(condition)
  } else {
    pageInit()
  }
}, { deep: true })

// 当前页改变
function pageChangeFn(page) {
  pageData.pageIndex = page
}
// 分页数改变
function pageSizeChangeFn(pageSizeIndex) {
  pageData.pageSize = pageSizeIndex
}
</script>

<style lang="scss" scoped>
.mb-10 {
  margin-bottom: 10px;
}

.mt-12 {
  margin-top: -12px;
  margin-right: 6px;
}

.width-150 {
  width: 150px !important;
}

.width-250 {
  width: 250px !important;
}

.btn-group {
  width: 150px;
  margin-left: 26px;
}

.pageSty {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.n-gradient-text) {
  font-weight: bold;
}

.font-18 {
  font-size: 18px;
}
</style>