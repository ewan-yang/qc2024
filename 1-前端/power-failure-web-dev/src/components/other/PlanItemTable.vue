<template>
  <n-drawer v-model:show="props.showPlanItemTable" :on-mask-click="maskClose" :on-after-leave="maskClose" style="width: 80%">
    <n-drawer-content >
      <template #header >
        <div class="flex justify-between ">
          <div class="font-700">停电计划列表</div>
          <div >
            <n-button type="primary" @click="handleConfirm" v-if="props.type != 'isDetail'">确认</n-button>
            <n-button type="default" @click="maskClose" class="m-l-2 m-r-4">返回</n-button>
          </div>
          
        </div>
      </template>
      <template #default>
        <n-data-table
          :max-height="650"
          :row-key="(row)=> row.id"
          v-model:checked-row-keys="checkedRowKeys"
          :columns="columns"
          :data="props.tableData"
          :single-line="false"

        />
      </template>
    </n-drawer-content>
  </n-drawer>
  <!-- 停电计划详情抽屉框 -->
  <ProjectCodeDrawer ref="projectRef" />
</template>

<script setup lang="ts" name="PlanItemTable">
import { ref, h, defineProps,onMounted} from 'vue'

// 主页面传参
const props = defineProps({
  tableData: {
    type: Array,
    required: true
  },
  showPlanItemTable: {
    type: Boolean,
    required: true
  },
  defaultCheckedRowKeys:{
    type: String,
    required: true
  },
  type:{
    type: String,
    required: true
  }
})
const projectRef = ref(null)
// 主页面传递的函数
const emit = defineEmits(['close','confirm'])
const checkedRowKeys = ref([props.defaultCheckedRowKeys])


const columns = [
    {
      type: 'selection',
      multiple: false,
      disabled(row){
        return props.type === 'isDetail' && row.id != props.defaultCheckedRowKeys
      }
    },
    {
      title: '工程编号',
      key: 'projectCode',
      width: 100,
      render:(row)=>{
        return h('span',{
          style:{
              color: 'rgb(0,122,255)',
              cursor: 'pointer'
            },
          onClick:()=>{
            projectRef.value.operateDrawer({id:row.id})
          }
        },row.projectCode)
      }
    },
    {
      title: '工程帐号',
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
      width: 90,
    },
    {
      title: '变电站/线路名称',
      key: 'stationLineName',
      width: 120,
    },
    {
      title: '停役设备',
      key: 'projectType',
      align: 'center',
      children: [
        {
          title: '(注:仅供参考,请以现场勘查后实际停役单为准)',
          key: 'cosDevice',
          align: 'center',
          width: 220
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
      key: 'projectType',
      align: 'center',
      children: [
        {
          title: '(注:仅供参考,请以设计图纸及现场实际工作内容为准)',
          key: 'jobContent',
          align: 'center',
          width: 220
        }
      ]
    },
    {
      title: '施工班组',
      key: 'constructionTeam',
      width: 120,
    },
 ]
function maskClose() {
  emit('close')
}
function handleConfirm() {
  emit('confirm', checkedRowKeys.value);
  emit('close')
}
</script>

<style>
.n-drawer-header__main {
  width: 100%;
}
</style>