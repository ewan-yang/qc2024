<template>
	<!-- 主页 -->
	<n-card class="mb-4">
		<div class="font-700 m-b-3 b-l-#148c78 b-l-5 p-l-2 font-18">
			<span>停电计划列表</span>
			<!-- <n-button type="primary" class="mb float-right" @click="showEditModal = true">导入</n-button> -->
			<div class="mb float-right inline-block">
				<n-upload class="mr-3" :action="`${baseUrl}/relay-task-service/api/v1/plan/importPlan`" accept=".xlsx"
					:headers="{ Authorization: `Bearer ${localStg.get('token')}`, }" :show-file-list="false" :response-type="'json'"
					:on-finish="modalUserListImport" :on-error="modalUserListImportError">
					<n-button type="primary">导入</n-button>
				</n-upload>
			</div>
		</div>
		<!-- 主页表格 -->
		<n-spin :show="loading">
			<n-data-table :columns="columns" :data="data" :max-height="730" :bordered="false" :single-line="false" />
			<!-- 分页 -->
			<div class="pageSty">
				<n-pagination v-model:page="pageData.pageIndex" :page-count="pageCount" :pageSizes="pageSizes" show-quick-jumper
					show-size-picker :on-update:page="pageChangeFn" :on-update:pageSize="pageSizeChangeFn" />
			</div>
		</n-spin>
	</n-card>

	<!-- 主页编辑模态框 -->
	<n-modal v-model:show="showEditModal" transform-origin="center">
		<n-card class="width-500" title="修改计划名称" :bordered="false" size="huge" role="dialog" aria-modal="true">
			<div>
				<n-form ref="editFormRef" :model="editModel">
					<n-form-item path="taskCode" label="计划年月" label-placement="left">
						<n-date-picker v-model:value="editModel.planMonth" type="month" clearable @keydown.enter.prevent
							class="w-300" />
					</n-form-item>
					<n-form-item path="receiptCode" label="标题名称" label-placement="left">
						<n-input v-model:value="editModel.title" @keydown.enter.prevent class="w-300" />
					</n-form-item>
				</n-form>
			</div>
			<template #footer>
				<div class="flex justify-end pr-10">
					<n-space>
						<n-button type="default" @click="showEditModal = false">返回</n-button>
						<n-button type="primary" @click="editModalSubmit">确定</n-button>
					</n-space>
				</div>
			</template>
		</n-card>
	</n-modal>

	<PlanDrawer :planId="planId" :showDrawer="showDrawer" @close="closeDrawerFn" @mainPageInit="mainPageInit" />
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, watch } from 'vue'
import { NButton } from 'naive-ui'
import {
	useMessage,
} from 'naive-ui'
import { localStg } from "@/utils";

import { allPlanPageAPI, allPlanEditAPI } from '../../service/api'
import { getServiceEnvConfig } from '~/.env-config';

const message = useMessage()
const baseUrl = ref(getServiceEnvConfig(import.meta.env).url)

// 导入======================
//停电计划列表导入方法
function modalUserListImport() {
	message.success("导入成功");
	pageInit()
}

function modalUserListImportError() {
	message.error("导入失败");
}

// 抽屉
let planId = ref('')
let showDrawer = ref(false)

function closeDrawerFn() {
	showDrawer.value = false
}


function mainPageInit(){
	pageInit()
}

// ======================================
// 表格表头数据
const createColumns = () => [
	// {
	// 	title: 'id',
	// 	key: 'id',
	// 	width: 0,
	// 	fixed: 'left'
	// },
	{
		title: '计划编号',
		key: 'code',
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
            height:'4rem',
            lineHeight: '4rem',
					},
					onClick: () => {
						planId.value = row.id
						showDrawer.value = true
					}
				},
				{ default: () => `${row.code}` }
			)
		}
	},
	{
		title: '计划年月',
		key: 'planMonth',
		width: 90,
		fixed: 'left'
	},
	{
		title: '标题名称',
		key: 'title',
		width: 160,
	},
	{
		title: '总数',
		key: 'total',
		width: 50,
	},
	{
		title: '已关联数',
		key: 'beExecuteQty',
		width: 70,
		render(row) {
			return h('span', {
				style: 'color:#F59A23'
			},
				{ default: () => row.associated }
			)
		}
	},
	{
		title: '未关联数',
		key: 'executeQty',
		width: 70,
		render(row) {
			return h('span', {
				style: 'color:#02A7F0'
			},
				{ default: () => row.unassociatedQty }
			)
		}
	},
	{
		title: '生成时间',
		key: 'updateDate',
		width: 180,
	},
	{
		title: '操作',
		width: 80,
		fixed: 'right',
		render(row) {
			return h(
				NButton,
				{
					size: 'small',
					style:{
						color:'#033FB4',
						
					},
					color:'transparent',
					onClick: () => {
						let { id, title, planMonth } = row
						showEditModalFn(id, title, planMonth)
					}
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
	const dataArr = await allPlanPageAPI(pageData)
	console.log(dataArr)
	let timer = setTimeout(() => {
		loading.value = false
		clearTimeout(timer)
	}, 300)
	if (dataArr.data?.count === 0 && pageData.pageIndex !== 0) {
		pageData.pageIndex -= 1
	}
	pageCount.value = dataArr.data?.pageCount
	data.value = dataArr.data?.data
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


//==========================编辑模态框
const showEditModal = ref(false)
let editModel = ref({
	id: null,
	planMonth: null,
	title: null
})


// ============时间格式转换函数===========
// "2023-07"转换时间戳
function convertToTimestamp(dateStr) {
	// 将时间字符串拆分为年份和月份
	const [year, month] = dateStr.split("-");

	// 创建一个 Date 对象，设置年份和月份
	const dateObj = new Date(`${year}-${month}`);

	// 获取对应日期的时间戳（毫秒级）
	const timestamp = dateObj.getTime();

	return timestamp;
}
// 时间戳转字符串
function convertToDateFormat(timestamp) {
	// 创建一个 Date 对象，传入时间戳（毫秒级）
	const dateObj = new Date(timestamp);

	// 获取年份和月份
	const year = dateObj.getFullYear();
	const month = dateObj.getMonth() + 1; // 月份从 0 开始，需要加 1

	// 格式化为 "XXXX-XX" 的字符串
	const formattedDate = `${year}-${String(month).padStart(2, "0")}-01`;

	return formattedDate;
}
// ============时间格式转换函数===========


// 模态框打开函数
function showEditModalFn(id, title, planMonth) {
	showEditModal.value = !showEditModal.value
	planMonth = convertToTimestamp(planMonth)
	editModel.value = { id, title, planMonth }
}

//提交编辑
async function editModalSubmit() {
	let planMonth = convertToDateFormat(editModel.value.planMonth)
	let params = { title: editModel.value.title, planMonth }
	let data = await allPlanEditAPI(params, editModel.value.id)
	if (!data.error) {
		message.success('修改成功')
		pageInit()
	} else {
		message.error("修改失败")
	}
	showEditModal.value = false

}

</script>

<style scoped>
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

.width-500 {
	width: 500px !important;
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

:deep(.n-data-table-tr td) {
  height: 80px !important;
}
</style>