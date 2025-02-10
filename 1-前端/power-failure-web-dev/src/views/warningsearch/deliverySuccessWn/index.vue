<template>
    <n-card class="mb-4">
        <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
            运方下达风险预警
        </div>

        <!-- 查询表单组 -->
        <div class="flex flex-justify-around mw-1380">
            <n-form ref="searchFormRef" :model="model" inline class="flex flex-wrap">
                <n-form-item path="powerCutTime" label="停电时间" label-placement="left">
                    <n-date-picker v-model:value="model.powerCutTime" type="daterange" clearable @keydown.enter.prevent
                        class="width-250" />
                </n-form-item>
                <n-form-item path="sendPowerTime" label="送电时间" label-placement="left">
                    <n-date-picker v-model:value="model.sendPowerTime" type="daterange" clearable @keydown.enter.prevent
                        class="width-250" />
                </n-form-item>
                <n-form-item path="ranges" label="停电范围" label-placement="left">
                    <n-input v-model:value="model.ranges" @keydown.enter.prevent class="width-160" />
                </n-form-item>
                <n-form-item path="deviceName" label="设备名称" label-placement="left">
                    <n-input v-model:value="model.deviceName" @keydown.enter.prevent class="width-160" />
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
        <n-button type="primary" class="mb"
            @click="exportExcal('/analysis-alarm-service/api/v1/taskAdvanceNotice/searchCarrierReleasePageList/export', { condition }, '运方下达风险预警')">导出</n-button>

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

    <!-- 关联通知单抽屉 -->
    <TaskDetailDrawer ref="modalRef" :transformId="transfromData.taskId" transformIdType="task"></TaskDetailDrawer>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, watch, nextTick } from 'vue'
import { NTag } from 'naive-ui'
import {
    FormInst,
    useMessage,
} from 'naive-ui'

import { deliverySuccessWnPageAPI } from '../../../service/api'
import { exportExcal } from '~/src/utils/common/exportExcal'

const message = useMessage()

// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)

interface SearchModelType {
    powerCutTime: number | null
    sendPowerTime: number | null
    ranges: string | null
    deviceName: string | null

}

// 时间戳转换函数
function formatDate(timestamp) {
    if (!timestamp) {
        return null
    }
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    return formattedDate;
}

const scrollbarProps = ref({
    XScrollable: true
})

// 是否是查询数据标记
const searchFlag = ref(false)
// 查询条件
let condition: any = ref({})


const searchFormRef = ref<FormInst | null>(null)

const searchModelRef = ref<SearchModelType>({
    powerCutTime: null,
    sendPowerTime: null,
    ranges: null,
    deviceName: null
})

const model = searchModelRef

function resetFn() {
    model.value = {
        powerCutTime: null,
        sendPowerTime: null,
        ranges: null,
        deviceName: null
    }
    condition = {}
    searchFlag.value = false
    pageData.pageIndex = 1
    pageInit()
}


function searchFn() {
    let { ranges, deviceName } = model.value
    let startDateBegin = formatDate(model.value.powerCutTime?.[0])
    let startDateEnd = formatDate(model.value.powerCutTime?.[1])
    let endDateBegin = formatDate(model.value.sendPowerTime?.[0])
    let endDateEnd = formatDate(model.value.sendPowerTime?.[1])
    condition = { ranges, deviceName, startDateBegin, startDateEnd, endDateBegin, endDateEnd }
    pageData.pageIndex = 1
    searchFlag.value = true
    searchDataFn(condition)
}

// 查询函数
async function searchDataFn(condition) {
    const dataArr = await deliverySuccessWnPageAPI({ ...pageData, condition })
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
        title: '停电计划编号',
        key: 'taskCode',
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
                        height: '4rem',
                        lineHeight: '4rem',
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
        title: '确认时间',
        key: 'confirmTime',
        width: 170,
        fixed: 'left'
    },
    {
        title: '停电时间',
        key: 'startTime',
        width: 150,
    },
    {
        title: '送电时间',
        key: 'endTime',
        width: 150,
    },
    {
        title: '任务超期时长(天)',
        key: 'overdueDuration',
        width: 170,
    },
    {
        title: '设备名称',
        key: 'deviceName',
        width: 200,
    },
    {
        title: '工作内容',
        key: 'jobContent',
        width: 200,
    },
    {
        title: '停电类型',
        key: 'type',
        width: 200,
        render(row) {
            const typeArr = ['计划停电']
            return h(
                'span',
                {},
                { default: () => `${typeArr[row.type - 1]}` }
            )
        }
    },
    {
        title: '停电原因',
        key: 'reason',
        width: 200,
        render(row) {
            const reasonArr = ['设备检修', '配合客户接入', '配合市政过程']
            return h(
                'span',
                {},
                { default: () => `${reasonArr[row.reason - 1]}` }
            )
        }
    },
    {
        title: '停电范围',
        key: 'ranges',
        width: 310,
    },
    {
        title: '状态',
        key: 'executeStatus',
        width: 100,
        render(row) {
            const statusArr = ['待提交', '待派发', '执行中', '已反馈', '取消中']
            const statusCode = (row.executeStatus / 10) % 10
            return h(
                'span',
                {},
                { default: () => `${statusArr[statusCode - 1]}` }
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
                width: 70,
                className: 'red'
            },
            {
                title: '已派发',
                key: 'assignedQty',
                width: 70,
                className: 'green'
            },
            {
                title: '不派发',
                key: 'cancelledQty',
                width: 70,
                className: 'orange'
            },
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
                width: 70,
                className: 'red'
            },
            {
                title: '同意',
                key: 'agreedQty',
                width: 70,
                className: 'green'
            },
            {
                title: '未签',
                key: 'unsignedQty',
                width: 70,
                className: 'orange'
            },
        ]
    },
    {
        title: '版本',
        key: 'version',
        width: 80,
        fixed: 'right'
    },
    {
        title: '任务预警状态',
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
    const dataArr = await deliverySuccessWnPageAPI(pageData)
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

<style scoped>
.mw-1380 {
    min-width: 1380px;
}

:deep(.red) {
    color: red !important;
}

:deep(.green) {
    color: green !important;
}

:deep(.orange) {
    color: orange !important;
}

:deep(.n-data-table-th__title) {
    color: black !important;
}

.pageSty {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
}

:deep(.n-form-item-label) {
    width: 90px;
}

:deep(.n-gradient-text) {
    font-weight: bold;
}

.font-18 {
    font-size: 18px;
}

.width-200 {
    width: 200px !important;
}

.width-160 {
    width: 160px !important;
}

.width-250 {
    width: 250px !important;
}

.btn-group {
    width: 200px;
    margin-right: -50px;
}

.width-600 {
    width: 600px !important;
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