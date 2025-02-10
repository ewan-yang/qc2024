<template>
    <n-card class="mb-4">
        <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
            下达超时预警
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
                <n-form-item path="taskCode" label="关联通知单" label-placement="left">
                    <n-input v-model:value="model.taskCode" @keydown.enter.prevent class="width-160" />
                </n-form-item>
                <n-form-item path="receiptCode" label="回执单号" label-placement="left">
                    <n-input v-model:value="model.receiptCode" @keydown.enter.prevent class="width-160" />
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
            @click="exportExcal('/analysis-alarm-service/api/v1/taskAdvanceNotice/searchReleaseTimeOutPageList/export', { condition }, '下达超时告警')">导出</n-button>
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

    <!-- 回执单号抽屉 -->
    <TaskUserDetailDrawer ref="taskUserModalRef" :transformId="transformData?.taskUserId"></TaskUserDetailDrawer>

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
import { exportExcal } from '~/src/utils/common/exportExcal'
import { deliveryTimeoutWnPageAPI } from '../../../service/api'

const message = useMessage()

// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)

/// 回执详情抽屉数据
let transformData = ref({})
let taskUserModalRef = ref(null)

interface SearchModelType {
    powerCutTime: number | null
    sendPowerTime: number | null
    taskCode: string | null
    receiptCode: string | null

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
    taskCode: null,
    receiptCode: null
})

const model = searchModelRef

function resetFn() {
    model.value = {
        powerCutTime: null,
        sendPowerTime: null,
        taskCode: null,
        receiptCode: null
    }
    condition = {}
    searchFlag.value = false
    pageData.pageIndex = 1
    pageInit()
}


function searchFn() {
    let { receiptCode, taskCode } = model.value
    let startDateBegin = formatDate(model.value.powerCutTime?.[0])
    let startDateEnd = formatDate(model.value.powerCutTime?.[1])
    let endDateBegin = formatDate(model.value.sendPowerTime?.[0])
    let endDateEnd = formatDate(model.value.sendPowerTime?.[1])
    condition = { receiptCode, taskCode, startDateBegin, startDateEnd, endDateBegin, endDateEnd }
    pageData.pageIndex = 1
    searchFlag.value = true
    searchDataFn(condition)
}

// 查询函数
async function searchDataFn(condition) {
    const dataArr = await deliveryTimeoutWnPageAPI({ ...pageData, condition })
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
        title: '回执单号',
        key: 'receiptCode',
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
                        transformData.value = row
                        nextTick(() => {
                            taskUserModalRef.value?.open()
                        })
                    }
                },
                { default: () => `${row.receiptCode}` }
            )
        }
    },
    {
        title: '派发状态',
        key: 'assignStatus',
        width: 90,
        fixed: 'left',
        render(row) {
            const assignStatusArr = [
                {
                    '210': '未派发',
                    color: 'orange'
                },
                {
                    '220': '已派发',
                    color: 'green'
                },
                {
                    '230': '已反馈',
                    color: 'blue'
                },
                {
                    '240': '不派发',
                    color: 'black'
                },
            ]
            for (const item of assignStatusArr) {
                if (Object.keys(item).includes(row.assignStatus)) {
                    return h(
                        'span',
                        {
                            style: {
                                color: item.color
                            }
                        },
                        { default: () => `${item[row.assignStatus]}` }
                    )
                    break
                }
            }

        }
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
        title: '距离停电时长（天）',
        key: 'distanceDuration',
        width: 150,
    },
    {
        title: '反馈情况',
        key: 'feedbackStatus',
        width: 120,
        render(row) {
            const feedbackStatusArr = [
                {
                    '310': '未签',
                    color: 'orange'
                },
                {
                    '320': '同意',
                    color: 'green'
                },
                {
                    '330': '拒签',
                    color: 'red'
                }
            ]
            for (const item of feedbackStatusArr) {
                if (Object.keys(item).includes(row.feedbackStatus)) {
                    return h(
                        'span',
                        {
                            style: {
                                color: item.color
                            }
                        },
                        { default: () => `${item[row.feedbackStatus]}` }
                    )
                    break
                }
            }

        }
    },
    {
        title: '户号',
        key: 'accountNumber',
        width: 130,
    },
    {
        title: '客户名称',
        key: 'customerName',
        width: 200,
    },
    {
        title: '地址',
        key: 'customerAddress',
        width: 300,
    },
    {
        title: '联系方式',
        key: 'phone',
        width: 170,
    },
    {
        title: '用户类型',
        key: 'userType',
        width: 100,
        render(row) {
            const userTypeArr = ['高大', '底大', '低非', '居民', '居委', '物业', '抄送用户', '考核']
            return h(
                'span',
                {},
                { default: () => `${userTypeArr[row.userType - 1]}` }
            )
        }
    },
    {
        title: '是否短时停电',
        key: 'boolShortTime',
        width: 100,
        render(row) {
            const boolShortTimeArr = ['否', '是']
            return h(
                'span',
                {},
                { default: () => `${boolShortTimeArr[row.boolShortTime]}` }
            )
        }
    },
    {
        title: '用户重要性',
        key: 'userPriority',
        width: 100,
        render(row) {
            const userPriorityArr = ['普通用户', '重要用户']
            return h(
                'span',
                {},
                { default: () => `${userPriorityArr[row.userPriority - 1]}` }
            )
        }
    },
    {
        title: '所属台区',
        key: 'region',
        width: 180,
    },

    {
        title: '关联通知单',
        key: 'taskCode',
        width: 200,
        fixed: 'right',
        render(row) {
            return h(
                'span',
                {
                    style: {
                        color: '#0062E1',
                        cursor: 'pointer'
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
        title: '回执告警状态',
        key: 'alarmTitle',
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
    const dataArr = await deliveryTimeoutWnPageAPI(pageData)
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

.w-300 {
    width: 300px !important;
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
</style>