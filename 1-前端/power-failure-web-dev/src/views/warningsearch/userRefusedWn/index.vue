<template>
    <n-card class="mb-4">
        <div class="font-700 m-b-3 b-l-#044FE1 b-l-5 p-l-2 font-18">
            用户拒签告警
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
            @click="exportExcal('/analysis-alarm-service/api/v1/taskAdvanceNotice/searchUserRejectPageList/export', { condition }, '用户拒签告警')">导出</n-button>
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

    <!-- 编辑模态框 -->
    <!-- <n-modal v-model:show="showEditModal" transform-origin="center">
        <n-card class="width-980" title="回执跟进情况" :bordered="false" size="huge" role="dialog" aria-modal="true">
            <div>
                <n-form ref="searchFormRef" :model="model" inline class="flex flex-wrap flex-justify-around">
                    <n-form-item path="taskCode" label="跟进人员" label-placement="left">
                        <n-input v-model:value="model.taskCode" @keydown.enter.prevent class="w-300" />
                    </n-form-item>
                    <n-form-item path="receiptCode" label="跟进时间" label-placement="left">
                        <n-input v-model:value="model.receiptCode" @keydown.enter.prevent class="w-300" />
                    </n-form-item>
                    <n-form-item path="taskCode" label="跟进方式" label-placement="left">
                        <n-select v-model:value="model.taskCode" @keydown.enter.prevent class="w-300" />
                    </n-form-item>
                    <n-form-item path="receiptCode" label="对接人员" label-placement="left">
                        <n-input v-model:value="model.receiptCode" @keydown.enter.prevent class="w-300" />
                    </n-form-item>
                    <n-form-item path="taskCode" label="联系电话" label-placement="left">
                        <n-input v-model:value="model.taskCode" @keydown.enter.prevent class="w-300" />
                    </n-form-item>
                    <n-form-item path="receiptCode" label="反馈状态" label-placement="left">
                        <n-select v-model:value="model.receiptCode" @keydown.enter.prevent class="w-300" />
                    </n-form-item>
                    <n-form-item path="taskCode" label="情况说明" label-placement="left">
                        <n-input v-model:value="model.taskCode" type="textarea" @keydown.enter.prevent class="w-300" />
                    </n-form-item>
                </n-form>
            </div>
            <template #footer>
                <div class="flex justify-end pr-10">
                    <n-space>
                        <n-button type="default" @click="showEditModal = false">返回</n-button>
                        <n-button type="primary" @click="showEditModal = false">确定</n-button>
                    </n-space>
                </div>
            </template>
        </n-card>
    </n-modal> -->

    <n-modal v-model:show="showEditModal" preset="dialog" title="Dialog" :style="{ width: '600px' }"
        :on-after-leave="cancelModalCancel">
        <template #header>
            <div>任务跟进情况</div>
        </template>
        <n-form ref="followModalRef" :model="editModalData" label-placement="left" label-width="auto" class="editModelSty"
            require-mark-placement="right-hanging">
            <n-grid x-gap="12" cols="2">
                <n-grid-item>
                    <n-form-item label="跟进方式：" path="followMethod">
                        <n-select v-model:value="editModalData.followMethod" placeholder="请选择跟进方式" :options="[
                            { label: '送达现场', value: 1 },
                            { label: '微信通知', value: 2 },
                            { label: '营销通知', value: 3 },
                        ]" /> </n-form-item></n-grid-item>
                <n-grid-item><n-form-item label="对接人员：" path="dockUser">
                        <n-input v-model:value="editModalData.dockUser" placeholder="请输入对接人员名称" />
                    </n-form-item></n-grid-item>
            </n-grid>
            <n-grid x-gap="12" cols="2">
                <n-grid-item><n-form-item label="联系电话：" path="telPhone">
                        <n-input v-model:value="editModalData.telPhone" placeholder="请输入联系电话" />
                    </n-form-item></n-grid-item>
                <n-grid-item>
                    <n-form-item label="反馈状态：" path="feedbackStatus">
                        <n-select v-model:value="editModalData.feedbackStatus" placeholder="请选择反馈状态" :options="[
                            { label: '未签', value: 310 },
                            { label: '同意', value: 320 },
                            { label: '拒签', value: 330 },
                        ]" /> </n-form-item></n-grid-item>
            </n-grid>
            <n-grid x-gap="12" cols="1">
                <n-grid-item><n-form-item label="跟进情况说明：" path="followDesc">
                        <n-input v-model:value="editModalData.followDesc" placeholder="请输入跟进情况说明" type="textarea" :autosize="{
                            minRows: 3,
                            maxRows: 8,
                        }" /> </n-form-item></n-grid-item>
            </n-grid>
        </n-form>
        <template #action>
            <div class="flex">
                <n-button type="primary" @click="editModalSubmit"> 确认 </n-button>
                <n-button class="m-l-4" @click="cancelModalCancel"> 取消 </n-button>
            </div>
        </template>
    </n-modal>

    <!-- 回执单号抽屉 -->
    <TaskUserDetailDrawer ref="taskUserModalRef" :transformId="transformData?.taskUserId"></TaskUserDetailDrawer>

    <!-- 关联通知单抽屉 -->
    <TaskDetailDrawer ref="modalRef" :transformId="transfromData.taskId" transformIdType="task"></TaskDetailDrawer>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, watch, nextTick } from 'vue'
import { NButton } from 'naive-ui'
import {
    FormInst,
    FormItemRule,
    useMessage,
    FormRules
} from 'naive-ui'
import { exportExcal } from '~/src/utils/common/exportExcal'
import { userRefusedWnPageAPI, putSubmitAPI } from '../../../service/api'


const message = useMessage()

// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)

// 回执详情抽屉数据
let transformData = ref({})
let taskUserModalRef = ref(null)

interface SearchModelType {
    powerCutTime: number | null
    sendPowerTime: number | null
    taskCode: string | null
    receiptCode: string | null

}

//表格横向滚动配置
const scrollbarProps = ref({
    XScrollable: true
})
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
    console.log(condition)
    pageData.pageIndex = 1
    searchFlag.value = true
    searchDataFn(condition)
}

// 查询函数
async function searchDataFn(condition) {
    const dataArr = await userRefusedWnPageAPI({ ...pageData, condition })
    if (dataArr.data.count === 0 && pageData.pageIndex !== 0) {
        pageData.pageIndex -= 1
    }
    pageCount.value = dataArr.data.pageCount
    data.value = dataArr.data.data
}


// ======================================

const assignStatusArr = [
    {
        text: '未派发',
        color: 'red'
    },
    {
        text: '已派发',
        color: 'green'
    },
    {
        text: '已反馈',
        color: 'orange'
    },
    {
        text: '不派发',
        color: 'black'
    },
]


// 表格表头数据
const createColumns = () => [
    {
        title: '回执单号',
        key: 'receiptCode',
        width: 130,
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
            let index = parseInt(row.assignStatus.slice(1, 2)) - 1
            return h(
                'span',
                {
                    style: {
                        color: assignStatusArr[index].color
                    }
                },
                { default: () => `${assignStatusArr[index].text}` }
            )
        }
    },
    {
        title: '停电时间',
        key: 'startTime',
        width: 160,
    },
    {
        title: '送电时间',
        key: 'endTime',
        width: 160,
    },
    {
        title: '反馈情况',
        key: 'feedbackStatus',
        width: 100,
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
        width: 150,
    },
    {
        title: '客户名称',
        key: 'customerName',
        width: 220,
    },
    {
        title: '地址',
        key: 'customerAddress',
        width: 270,
    },
    {
        title: '联系方式',
        key: 'phone',
        width: 130,
    },
    {
        title: '用户类型',
        key: 'userType',
        width: 90,
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
        title: '操作',
        width: 200,
        fixed: 'right',
        render(row) {
            return h(
                NButton,
                {
                    size: 'small',
                    style: {
                        color: '#033FB4',

                    },
                    color: 'transparent',
                    onClick: () => showEditModalFn(row.taskId)
                },
                { default: () => '编辑' }
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
    const dataArr = await userRefusedWnPageAPI(pageData)
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


//编辑模态框
const showEditModal = ref(false)

function showEditModalFn(id) {
    showEditModal.value = !showEditModal.value
    editModalData.value.taskUserId = id
}

const editModalData = ref({})


function editModalSubmit() {
    putSubmitAPI("relay-task-service/api/v1/taskUserFollow/follow", {
        ...editModalData.value,
    }).then((res) => {
        if (!res.error) {
            message.success("操作成功");
            cancelModalCancel();
        }
    });
}

function cancelModalCancel() {
    //跟进回执关闭
    editModalData.value = {};
    showEditModal.value = false;
}

</script>

<style scoped>
.mw-1380 {
    min-width: 1380px;
}

.editModelSty {
    padding-top: 20px;
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

.width-980 {
    width: 980px !important;
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