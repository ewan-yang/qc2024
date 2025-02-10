<template>
  <div>
    <BasicList
      :columns="columns"
      name="menu"
      modal-type="d"
      :reload="true"
      :format-data="handleFormatData"
      :format-edit="handleFormatEdit"
    ></BasicList>
  </div>
</template>
<script lang="ts" setup>
import { computed, h, onMounted, ref } from 'vue';
import { listAPI } from '@/service';
import { useDicStore } from '@/store';
import { isArray } from '@/utils';
const menuData = ref([]);

const dic = useDicStore();

onMounted(async () => {
  const { data } = await listAPI('relay-task-service/api/v1/menu');
  menuData.value = data;
  await dic.loadData('permission');
});
const columns = [
  {
    title: '名称',
    key: 'name',
    showSearch: true
  },
  {
    title: '父级名称',
    key: 'parentId',
    skip: true,
    component: 'NSelect',
    componentProps: {
      options: computed(() => {
        return menuData.value;
      }),
      labelField: 'name',
      valueField: 'id'
    },
    render(row) {
      return h('text', null, row.parentName);
    }
  },
  {
    title: '编码',
    key: 'code',
    showSearch: true
  },
  {
    title: '图标',
    key: 'css',
    labelMessage: '图标网址：https://icones.js.org/'
  },
  {
    title: '类型',
    key: 'type',
    ifShow: false,
    component: 'NSelect',
    componentProps: {
      options: [
        {
          label: '公共布局',
          value: 1
        },
        {
          label: '多级路由布局',
          value: 2
        },
        {
          label: '多级路由子路由布局',
          value: 3
        },
        {
          label: '空白布局',
          value: 4
        },
        {
          label: '单级路由公共布局',
          value: 5
        },
        {
          label: '单级路由空白布局',
          value: 6
        }
      ]
    }
  },

  {
    title: '权限',
    key: 'permission',
    ifShow: false,
    component: 'NSelect',
    skip: true,
    componentProps: {
      multiple: true,
      valueField: 'code',
      labelField: 'label',
      options: computed(() => {
        return (dic.getPermission || []).filter(t => t && t.sysOperation.code === 'view');
      })
    }
  },
  {
    title: 'sort',
    key: 'sort',
    component: 'NInputNumber'
  },
  {
    title: '状态',
    key: 'status',
    render(row) {
      return h('text', null, row.status ? '启用' : '禁用');
    }
  }
];
const handleFormatData = formData => {
  formData.permission = formData.permission ? formData.permission.join(',') : '';
  return formData;
};
const handleFormatEdit = data => {
  if (isArray(data.permission)) return data;
  data.permission = data.permission ? data.permission.split(',') : '';
  return data;
};
</script>
