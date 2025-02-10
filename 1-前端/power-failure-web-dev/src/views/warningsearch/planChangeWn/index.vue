<template>
    <n-card class="mb-4">
        <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
            计划变更预警
        </div>

        <!-- 查询表单组 -->
        <div class="flex flex-justify-around mw-1380">
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
                <n-form-item path="planMonth" label="计划年月" label-placement="left">
                    <n-date-picker v-model:value="model.planMonth" type="month" @keydown.enter.prevent class="width-150" />
                </n-form-item>
            </n-form>
            <n-button-group class="btn-group">
                <n-space class="flex flex-nowrap">
                    <n-button color="#BAD8FB" style="color:black" @click="searchFn">
                        查询
                    </n-button>
                    <n-button type="primary" ghost @click="resetFn">
                        重置
                    </n-button>
                </n-space>
            </n-button-group>
        </div>
    </n-card>
    <n-card>
        <n-button type="primary" class="mb" @click="exportExcal('/analysis-alarm-service/api/v1/taskAdvanceNotice/searchPlanChangePageList/export',{condition},'计划变更预警')">导出</n-button>
        <!-- 表格 -->
        <n-spin :show="loading">
            <n-data-table :columns="columns" :data="data" :max-height="730" :scroll-x="3000" :bordered="false"
                :single-line="false" :scrollbar-props="scrollbarProps" />
            <!-- 分页 -->
            <div class="pageSty">
                <n-pagination v-model:page="pageData.pageIndex" :page-count="pageCount" :pageSizes="pageSizes"
                    show-quick-jumper show-size-picker :on-update:page="pageChangeFn"
                    :on-update:pageSize="pageSizeChangeFn" />
            </div>
        </n-spin>
    </n-card>
    <!-- 停电计划详情模态框 -->
    <n-modal v-model:show="showPlanModal" transform-origin="center" class="receiptModal">
        <n-card class="width-1000" title="回执详情" :bordered="false" size="huge" role="dialog" aria-modal="true">
            <template #header-extra>
                <n-button type="default" @click="showPlanModal = false">返回</n-button>
            </template>
            <!-- 内容 -->
            <div>
                <n-form ref="planFormRef" :model="planModel" inline class="flex flex-wrap flex-justify-around">
                    <n-form-item path="projectCode" label-placement="left">
                        <template #label>
                            <div class="w-110">工程编号</div>
                        </template>
                        <n-input v-model:value="planModel.projectCode" @keydown.enter.prevent class="w-300" readonly />
                    </n-form-item>
                    <n-form-item path="projectAccount" label-placement="left">
                        <template #label>
                            <div class="w-110">工程账号</div>
                        </template>
                        <n-input v-model:value="planModel.projectAccount" @keydown.enter.prevent class="w-300" readonly />
                    </n-form-item>
                    <n-form-item path="projectName" label-placement="left">
                        <template #label>
                            <div class="w-110">工程名称</div>
                        </template>
                        <n-input v-model:value="planModel.projectName" @keydown.enter.prevent class="width-738" readonly />
                    </n-form-item>
                    <n-form-item path="projectType" label-placement="left">
                        <template #label>
                            <div class="w-110">项目类型</div>
                        </template>
                        <n-input v-model:value="planModel.projectType" @keydown.enter.prevent class="w-300" readonly />
                    </n-form-item>
                    <n-form-item path="constructionTeam" label-placement="left">
                        <template #label>
                            <div class="w-110">施工班组</div>
                        </template>
                        <n-input v-model:value="planModel.constructionTeam" @keydown.enter.prevent class="w-300" readonly />
                    </n-form-item>
                    <n-form-item path="startTime"  label-placement="left">
                        <template #label>
                            <div class="w-110">停役时间</div>
                        </template>
                        <n-input v-model:value="planModel.startTime" @keydown.enter.prevent class="w-300" readonly />
                    </n-form-item>
                    <n-form-item path="endTime"  label-placement="left">
                        <template #label>
                            <div class="w-110">复役时间</div>
                        </template>
                        <n-input v-model:value="planModel.endTime" @keydown.enter.prevent class="w-300" readonly />
                    </n-form-item>
                    <n-form-item path="stationLineName"  label-placement="left">
                        <template #label>
                            <div class="w-110">变电站/线路名称</div>
                        </template>
                        <n-input type="textarea" v-model:value="planModel.stationLineName" @keydown.enter.prevent
                            class="width-738" readonly />
                    </n-form-item>
                    <n-form-item path="jobContent" label-placement="left">
                        <template #label>
                            <div class="w-110">主要工作内容</div>
                        </template>
                        <n-input type="textarea" v-model:value="planModel.jobContent" @keydown.enter.prevent
                            class="width-738" readonly />
                    </n-form-item>
                    <n-form-item path="cosDevice" label-placement="left">
                        <template #label>
                            <div class="w-110">停役设备</div>
                        </template>
                        <n-input type="textarea" v-model:value="planModel.cosDevice" @keydown.enter.prevent
                            class="width-738" readonly />
                    </n-form-item>
                </n-form>
            </div>
        </n-card>
    </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, watch } from 'vue'
import { NTag } from 'naive-ui'
import {
    FormInst,
} from 'naive-ui'
import {exportExcal} from '~/src/utils/common/exportExcal'
import { planChangeWnPageAPI } from '../../../service/api'

// 导出功能函数
async function exportFn(){
   let data = await planChangeWnExportAPI()
   console.log(data)
}



const scrollbarProps = ref({
    XScrollable: true
})

interface SearchModelType {
    startTime: number | null
    endTime: number | null
    projectType: string | null
    constructionTeam: string | null
    planMonth: string | null

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
    planMonth: null
})

const model = searchModelRef

function resetFn() {
    model.value = {
        startTime: null,
        endTime: null,
        projectType: null,
        constructionTeam: null,
        planMonth: null
    }
    condition = {}
    searchFlag.value = false
    pageData.pageIndex = 1
    pageInit()
}


function searchFn() {
    let { projectType, constructionTeam, planMonth } = model.value
    let startDateBegin = formatDate(model.value.startTime?.[0])
    let startDateEnd = formatDate(model.value.startTime?.[1])
    let endDateBegin = formatDate(model.value.endTime?.[0])
    let endDateEnd = formatDate(model.value.endTime?.[1])
    planMonth = formatDate(planMonth, true)
    condition = { projectType, constructionTeam, planMonth, startDateBegin, startDateEnd, endDateBegin, endDateEnd }
    pageData.pageIndex = 1
    searchFlag.value = true
    searchDataFn(condition)
}

// 查询函数
async function searchDataFn(condition) {
    const dataArr = await planChangeWnPageAPI({ ...pageData, condition })
    if (dataArr.data.count === 0 && pageData.pageIndex !== 0) {
        pageData.pageIndex -= 1
    }
    pageCount.value = dataArr.data.pageCount
    data.value = dataArr.data.data
}


// ======================================
// 表格表头数据
const createColumns = () => [
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
                        cursor: 'pointer',
                        display: 'inline-block',
            height:'4rem',
            lineHeight: '4rem',
                    },
                    onClick: () => showPlanModalFn(row)
                },
                { default: () => `${row.projectCode}` }
            )
        }
    },
    {
        title: '状态',
        key: 'executeStatus',
        width: 80,
        fixed: 'left',
        render(row) {
            let a = ''
            switch(row.executeStatus){
                case '010':
                    a='未关联';
                    break;
                    case '020':
                    a='已关联';
                    break;
    
            }
            return h(
                'span',
                {},
                { default: () => `${a}` }
            )
        }
    },
    {
        title: '计划年月',
        key: 'planMonth',
        width: 100,
    },
    {
        title: '工程账号',
        key: 'projectAccount',
        width: 120,
    },
    {
        title: '工程名称',
        key: 'projectName',
        width: 120,
    },
    {
        title: '项目类型',
        key: 'projectType',
        width: 100,
    },
    {
        title: '变电站/线路名称',
        key: 'stationLineName',
        width: 200,
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
        width: 200,
    },
    {
        title: '复役时间',
        key: 'endTime',
        width: 200,
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
                width: 300
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
        title: '计划变更预警',
        width: 200,
        fixed: 'right',
        render(row) {
            const alarmTypeArr = ["下达超时告警", "下达风险告警", "用户拒签告警", "重复停电预警", "计划变更预警"]
            return h(
                NTag,
                {
                    size: 'large',
                    type: 'error',
                },
                { default: () => `${alarmTypeArr[row.alarmType - 1]}` }
            )
        }
    }
]

// 表格数据
let data = ref([])

let columns: any = reactive([])

// 表格列数据
columns = createColumns()

// 分页
let pageData = reactive({ pageIndex: 1, pageSize: 10 })
let pageSizes = reactive([10, 20, 30, 40, 50])
let pageCount = ref(10)

//页面加载
onMounted(() => {
    pageInit()
})

const loading = ref(true)

// 页面初始化函数
async function pageInit() {
    loading.value = true
    const dataArr = await planChangeWnPageAPI(pageData)
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

// 模态框
// 停电计划详情模态框

const showPlanModal = ref(false)

function showPlanModalFn(row) {
    showPlanModal.value = !showPlanModal.value
    planModel.value = { ...row }
}

// 停电计划表单
interface PlanModelType {
    projectCode: string | null
    projectAccount: string | null
    projectName: string | null
    projectType: string | null
    constructionTeam: string | null
    startTime: string | null
    endTime: string | null
    stationLineName: string | null
    jobContent: string | null
    cosDevice: string | null
}

let planFormRef = ref<FormInst | null>(null)

let planModelRef = ref<PlanModelType>({
    projectCode: null,
    projectAccount: null,
    projectName: null,
    projectType: null,
    constructionTeam: null,
    startTime: null,
    endTime: null,
    stationLineName: null,
    jobContent: null,
    cosDevice: null
})

let planModel = planModelRef


</script>

<style scoped>
.mw-1380{
    min-width: 1500px;
}
.w-110{
    width: 110px;
}
.width-150 {
    width: 150px !important;
}

.width-250 {
    width: 250px !important;
}

.btn-group {
    width: 200px;
    margin-right: -50px;
}

.w-300 {
    width: 300px !important;
}

.pageSty {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
}

/* :deep(.n-form-item-label) {
    width: 120px;
} */


:deep(.n-gradient-text) {
    font-weight: bold;
}

.font-18 {
    font-size: 18px;
}

.width-200 {
    width: 200px !important;
}

.width-600 {
    width: 600px !important;
}

.width-738 {
    width: 738px !important;
}


.width-800 {
    width: 800px !important;
}

.width-1000 {
    width: 1000px !important;
}

.width-1800 {
    width: 1800px !important;
}

.width-1900 {
    width: 1900px !important;
}
</style>