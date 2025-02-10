<template>
  <div><BasicList :columns="columns" name="operation"></BasicList></div>
</template>
<script lang="ts" setup>
import { h } from 'vue';

const columns = [
  {
    title: '操作名称',
    key: 'name',
    showSearch: true
  },
  {
    title: '操作编码',
    key: 'code',
    showSearch: true
  },
  {
    title: '备注',
    key: 'remark'
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
