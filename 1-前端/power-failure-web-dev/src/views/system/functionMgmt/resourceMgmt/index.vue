<template>
  <div>
    <BasicList :columns="columns" name="resource" :format-data="handleFormatData" :format-edit="handleFormatEdit">
    </BasicList>
  </div>
</template>
<script lang="ts" setup>
import { h, onMounted, computed } from 'vue';
import { useDicStore } from '@/store';
import { detailAPI } from '@/service/api';

const dic = useDicStore();
onMounted(async () => {
  await dic.loadData('operation');
});
const handleFormatData = formData => {
  const list: Array<any> = dic.getOperation || [];
  const permissionList = formData.permissionList.map(_item => {
    const data = list.find(v => v.id === _item);
    return {
      operationId: data.id,
      permissionCode: `${formData.code}:${data.code}`
    };
  });
  formData.permissionList = permissionList;
  return formData;
};
const handleFormatEdit = async data => {
  const res = await detailAPI('relay-task-service/api/v1/resource', data.id);
  // eslint-disable-next-line require-atomic-updates
  data.permissionList = res.data.permissionList.map(item => item.operationId);
  return data;
};
const columns = [
  {
    title: '资源名称',
    key: 'name',
    showSearch: true
  },
  {
    title: '编码',
    key: 'code',
    showSearch: true
  },

  {
    title: '操作名称',
    key: 'permissionList',
    ifShow: false,
    component: 'NSelect',
    componentProps: {
      options: computed(() => dic.getOperation),
      multiple: true
    }
  },
  {
    title: '备注',
    key: 'remark'
  },
  {
    title: '状态',
    key: 'status',
    render(row: { status: any }) {
      return h('text', null, row.status ? '启用' : '禁用');
    }
  }
];
</script>
