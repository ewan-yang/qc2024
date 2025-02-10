<template>
  <div><BasicList :columns="columns" name="unit" :format-data="handleFormatData" :reload="true"></BasicList></div>
</template>
<script lang="ts" setup>
import { h, onMounted } from 'vue';
import { fetchUnitTreeData } from '~/src/service/api/userMgmt';
const treeData: any = [];

const resetTreeData = data => {
  data.forEach(item => {
    item.label = item.name;
    item.key = item.id;
    // eslint-disable-next-line no-unused-expressions
    item.children.length && resetTreeData(item.children);
  });
  return data;
};
const treeFind = (tree: any, func: { (t: any): boolean; (arg0: any): any }) => {
  for (const data of tree) {
    if (func(data)) return data;
    if (data.children) {
      const res = treeFind(data.children, func);
      if (res) return res;
    }
  }
  return null;
};
const handleFormatData = m => {
  m.relationPath = treeFind(treeData, t => t.id === m.parentId)?.relationPath || '';
  return m;
};

onMounted(async () => {
  const { data } = await fetchUnitTreeData();
  treeData.push(...resetTreeData(data));
});

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
    title: '类型',
    key: 'type',
    component: 'NSelect',
    componentProps: {
      options: [
        {
          label: '公司',
          value: 1
        },
        {
          label: '部门',
          value: 2
        }
      ]
    },
    render(row: { type: number }) {
      return h('text', null, row.type === 1 ? '公司' : '部门');
    }
  },
  {
    title: '上级机构',
    key: 'parentId',
    hideAdd: row => row.type !== 2,
    hideEdit: row => row.type !== 2,
    rules: [
      {
        trigger: ['blur', 'change'],
        message: '请选择上级机构',
        type: 'number'
      }
    ],
    component: 'NTreeSelect',
    componentProps: {
      options: treeData
    },
    render(row) {
      return h('text', null, row.parentName);
    }
  },
  {
    title: '备注',
    key: 'remark'
  },
  {
    title: '状态',
    key: 'status',
    render(row: { status: number }) {
      return h('text', null, row.status ? '启用' : '禁用');
    }
  }
];
</script>
