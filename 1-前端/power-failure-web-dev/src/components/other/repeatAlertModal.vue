<template>
  <div>
    <n-modal style="width:35%;" v-model:show="props.repeatAlert" preset="dialog"  title="重复停电提醒" :onClose="handleAfterLeave">
      <template #header>
        <div style="display: flex; justify-content: space-between;width:100%;align-items: center;margin-bottom:-5px">
          <div>重复停电提醒</div>
          <n-button strong secondary type="error" @click="emit('close')">
            不再提醒
          </n-button>
        </div>
      </template>
      <n-data-table
        :bordered="false"
        :single-line="false"
        :columns="columns"
        :data="props.repeatAlertModalData"
        :max-height="600"
      />
    </n-modal>
  </div>
</template>

<script setup>
import { ref, onMounted,h} from 'vue';
const props = defineProps({
  repeatAlert: {
    type: Boolean,
    default: false
  },
  repeatAlertModalData:{
    type: Array,
    default: ()=>[]
  }
})
const emit = defineEmits(['update:repeatAlert','close'])
const columns = [
  {
    title: '停电计划编码',
    key: 'taskCodeList',
    render: (row) => {
      return row.taskCodeList.length > 0 ? row.taskCodeList.join(' , ') : '--'
    }
  },
{
  title: '户号',
  key: 'accessNumberList',
  render: (row) => {
    return h('div', {
      style: {
        display: 'flex',
        flexDirection: 'column'  // 垂直排列
      }
    }, row.accessNumberList.map(item => h('span', item)))
  }
},
{
  title: '台区',
  key: 'region',
  render: (row) => {
   return row.region ? row.region : '--'
  }
}
]
const handleAfterLeave = () => {
  emit('update:repeatAlert', false)
  console.log('afterLeave')
}

</script>

<style scoped lang="scss">

</style>