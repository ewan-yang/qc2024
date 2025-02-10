<template>
    <n-card class="mb-4">
        <!-- 页面标题 -->
        <div class="font-700 m-b-2 b-l-#044FE1 b-l-5 p-l-2 font-18">
            预告警规则清单
        </div>
        <div>
            <!-- 新增按钮 -->
            <n-space>
                <n-button type="primary" class="add-btn " @click="openModelFn('add')">
                    <template #icon>
                        <n-icon>
                            <icon-ant-design:plus-outlined />
                        </n-icon>
                    </template>
                    新增
                </n-button>
            </n-space>
            <!-- 表格 -->
            <n-spin :show="loading">
                <n-data-table :columns="columns" :data="data" :max-height="730" :scroll-x="1000" :bordered="false"
                    :single-line="false" />
                <!-- 分页 -->
                <div class="pageSty">
                    <n-pagination v-model:page="pageData.pageIndex" :page-count="pageCount" :pageSizes="pageSizes"
                        show-quick-jumper show-size-picker :on-update:page="pageChangeFn"
                        :on-update:pageSize="pageSizeChangeFn" />
                </div>
            </n-spin>
        </div>
    </n-card>
    <!-- 增加编辑模态框 -->
    <n-modal v-model:show="showModal" :mask-closable="false" preset="dialog" :title="modelTitle" transform-origin="center"
        style="width: 600px;" :close-on-esc="false">
        <!-- 框内表单 -->
        <div class="p-6">
            <!-- 编辑中规则单号回显 -->
            <n-form-item path="ruleCode" label="规则编号" :style="`display:${editRuleCode.show}`">
                <n-input :value="editRuleCode.ruleCode" readonly />
            </n-form-item>
            <n-form ref="formRef" :model="model" :rules="rules">
                <n-form-item path="ruleName" label="规则名称">
                    <n-input v-model:value="model.ruleName" @keydown.enter.prevent />
                </n-form-item>
                <n-form-item path="days" label="时长（天）">
                    <n-input v-model:value="model.days" @keydown.enter.prevent />
                </n-form-item>
                <n-form-item path="params1" label="区别参数1">
                    <n-input v-model:value="model.params1" @keydown.enter.prevent />
                </n-form-item>
                <n-form-item path="params2" label="区别参数2">
                    <n-input v-model:value="model.params2" @keydown.enter.prevent />
                </n-form-item>
                <n-form-item path="remark" label="备注">
                    <n-input type="textarea" v-model:value="model.remark" @keydown.enter.prevent />
                </n-form-item>
            </n-form>
            <n-space class="float-right">
                <n-button type="primary" @click="onCancelClick">取消</n-button>
                <n-button type="primary" @click="onSubmitClick">提交</n-button>
            </n-space>
        </div>
    </n-modal>
</template>

<script lang="ts" setup>
import { h, ref, reactive, onMounted, watch } from 'vue'
import { NButton, useMessage, FormInst, FormItemRule, FormRules, NPopconfirm } from 'naive-ui'
import { warningRulePageAPI, warningRuleDelAPI, warningRuleAddAPI, warningRuleEditAPI } from '../../../service/api'


// 框内表单
interface ModelType {
    ruleName: string | null
    days: string | null
    params1: string | null
    params2: string | null
    remark: string | null
}

const formRef = ref<FormInst | null>(null)
const modelRef = ref<ModelType>({
    ruleName: null,
    days: null,
    params1: null,
    params2: null,
    remark: null
})

const rules: FormRules = {
    ruleName: [
        {
            required: true,
            validator(rule: FormItemRule, value: string) {
                if (!value) {
                    rule.message = '规则不能为空'
                    return false
                }
                return true
            },
            trigger: ['input', 'blur']
        }
    ],
    days: [
        {
            required: true,
            validator(rule: FormItemRule, value: string) {
                if (!value) {
                    rule.message = '时长不能为空'
                    return false
                } else if (!/^\d*$/.test(value)) {
                    rule.message = '时长应该为整数'
                    return false
                } else if (isNaN(Number(value))) {
                    rule.message = '请输入数字'
                    return false
                }
                return true
            },
            trigger: ['input', 'blur']
        }
    ]
}

let model = modelRef

//验证函数
function handleValidateButtonClick() {
    return new Promise((resolve, reject) => {
        formRef.value?.validate((errors) => {
            if (!errors) {
                resolve(true)
            } else {
                reject(false)
            }
        })
    })

}

// 模态框标题
let modelTitle = ref('编辑')
let editRuleCode = reactive({ show: 'none', ruleCode: null })

//触发模态框，增加或者编辑
function openModelFn(params, row = null) {
    model.value = {
        ruleName: null,
        days: null,
        params1: null,
        params2: null,
        remark: null
    }
    if (params === "add") {
        editRuleCode.show = 'none'
        showModal.value = true
        modelTitle.value = '增加'

    } else {
        editRuleCode.show = 'block'
        showModal.value = true
        modelTitle.value = '编辑'
        for (let keys in model.value) {
            model.value[keys] = row?.[keys]
        }
        // @ts-ignore
        rowId.value = row.id
        //@ts-ignore
        editRuleCode.ruleCode = row.ruleCode
    }
}

// 模态框部分
const message = useMessage()
const showModal = ref(false)
const rowId = ref(0)

// 取消
function onCancelClick() {
    showModal.value = false
}

// 提交
async function onSubmitClick() {
    let verify = await handleValidateButtonClick()
    // 验证
    if (verify) {
        if (modelTitle.value === '增加') {
            // 增加操作
            let { data } = await warningRuleAddAPI(model.value)
            if (data) {
                pageInit()
                message.success('提交成功')
                showModal.value = false
            } else {
                message.error('提交失败')
            }
        } else {
            // 修改操作提交
            let data = await warningRuleEditAPI(model.value, rowId.value)
            if (data) {
                pageInit()
                message.success('修改成功')
                showModal.value = false
            } else {
                message.error('修改失败')
            }
        }

    } else {
        showModal.value = true
        message.error('输入有误')
    }
}



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
    const dataArr = await warningRulePageAPI(pageData)
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
    pageInit()
}, { deep: true })
// 当前页改变
function pageChangeFn(page) {
    pageData.pageIndex = page
}
// 分页数改变
function pageSizeChangeFn(pageSizeIndex) {
    pageData.pageSize = pageSizeIndex
}



// 表格表头数据
const createColumns = () => [
    {
        title: '规则编号',
        key: 'ruleCode',
        width: 110,
        fixed: 'left'
    },
    {
        title: '规则名称',
        key: 'ruleName',
        width: 150,
        fixed: 'left'
    },
    {
        title: '时长（天）',
        key: 'days',
        width: 100,
    },
    {
        title: '区别参数1',
        key: 'params1',
        width: 170,
    },
    {
        title: '区别参数2',
        key: 'params2',
        width: 170
    },
    {
        title: '备注说明',
        key: 'remark',
        width: 300,
    },
    {
        title: '操作时间',
        key: 'updateDate',
        width: 200,
    },
    {
        title: '操作',
        width: 200,
        fixed: 'right',
        render(row) {
            const operationList = ['edit', 'delete']
            const operation = operationList.map(item => {
                if (item === 'edit') {
                    return h(
                        NButton,
                        {
                            size: 'small',
                            style: 'margin:0 10px;color:#044FE1',
                            color:'transparent',
                            onClick: () => operationFn(row, item)
                        },
                        { default: () => '编辑' }
                    )
                } else {
                    return [
                        h(
                            NPopconfirm,
                            {
                                negativeText: '取消', positiveText: '确认',
                                onPositiveClick: () => operationFn(row, item),
                                onNegativeClick: () => message.info('取消')
                            },
                            {
                                trigger: () => h(
                                    NButton,
                                    {
                                        size: 'small',
                                        style: 'margin:0 10px;color:rgba(0, 0, 0, 0.6)',
                                        color:'transparent',
                                    },
                                    { default: () => '删除' },
                                ),
                                default: () => h(
                                    'span',
                                    {},
                                    { default: () => '确定删除此数据吗？' }
                                ),
                            })]
                }

            })
            return operation

        }
    }
]



// 表格中点击操作按钮回调
async function operationFn(row, item) {
    // 删除
    if (item === 'delete') {
        let id = row.id
        let { error } = await warningRuleDelAPI(id)
        if (!error) {
            message.success("删除成功")
            pageInit()
        } else {
            message.error("删除失败")
        }
    } else {//编辑
        openModelFn(item, row)
    }
}
// 表格数据
let data = ref([])
let columns: any = reactive([])

// 表格列数据
columns = createColumns()

</script>

<style scoped>
.add-btn {
    margin: 10px 0 20px 0;
    color: white;
}

.pageSty {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
}

.font-18 {
    font-size: 18px;
}
</style>