<template>
  <div>
    <BasicList
      :columns="columns"
      name="role"
      modal-type="d"
      :format-data="handleFormatData"
      :format-edit="handleFormatEdit"
    ></BasicList>
  </div>
</template>
<script lang="ts" setup>
import { h, onMounted, computed } from 'vue';
import { useDicStore } from '@/store';
import { detailAPI } from '@/service/api';

const dic = useDicStore();
onMounted(async () => {
  await dic.loadData('permission,roleUser');
});
// 重置数据
const handleFormatData = formData => {
  formData.userList = formData.userList.map(id => ({ id }));
  formData.permissionList = formData.permissionList.map(id => ({ id }));
  return formData;
};
const handleFormatEdit = async data => {
  const res = await detailAPI('relay-task-service/api/v1/role', data.id);
  // eslint-disable-next-line require-atomic-updates
  data.permissionList = res.data.permissionList.map(item => item.id);
  // eslint-disable-next-line require-atomic-updates
  data.userList = res.data.userList.map(item => item.id);
  return data;
};
const columns = [
  {
    title: '名称',
    key: 'name',
    showSearch: true
  },
  {
    title: '编码',
    key: 'code',
    showSearch: true
  },
  {
    title: '功能授权',
    key: 'permissionList',
    ifShow: false,
    component: 'NSelect',
    componentProps: {
      multiple: true,
      options: computed(() => dic.getPermission)
    }
  },
  {
    title: '选择用户',
    key: 'userList',
    component: 'NSelect',
    ifShow: false,
    componentProps: {
      multiple: true,
      options: computed(() => dic.getUser)
    }
  },
  {
    title: '状态',
    key: 'status',
    render(row) {
      return h('text', null, row.status ? '启用' : '禁用');
    }
  }
];
</script>
