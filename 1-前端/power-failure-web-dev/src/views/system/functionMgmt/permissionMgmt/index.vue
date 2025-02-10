<template>
  <div><BasicList :columns="columns" name="permission" :format-data="handleFormatData"> </BasicList></div>
</template>
<script lang="ts" setup>
import { h, onMounted, reactive } from 'vue';
import { fetchAllResource, fetchAllOperation } from '@/service';
import { convertOptions } from '@/utils/common/common';

const resourceOptions: any = reactive([]);
const operationOptions: any = reactive([]);
const handleFormatData = formData => {
  formData.permissionCode = `${resourceOptions.find(_item => _item.value === formData.resourceId)?.code}:${
    operationOptions.find(_item => _item.value === formData.operationId)?.code
  }`;
  return formData;
};
const columns = reactive([
  {
    title: '资源名称',
    key: 'resourceId',
    component: 'NSelect',
    componentProps: {
      options: resourceOptions
    },
    showSearch: true,
    // 编辑时映射多层map的属性
    op: 'sysResource.id',
    render(row) {
      return h('text', null, row.sysResource?.name);
    }
  },
  {
    title: '操作名称',
    key: 'operationId',
    op: 'sysOperation.id',
    component: 'NSelect',
    componentProps: {
      options: operationOptions
    },
    render(row) {
      return h('text', null, row.sysOperation?.name);
    }
  },
  {
    title: '状态',
    key: 'status',
    render(row) {
      return h('text', null, row.status ? '启用' : '禁用');
    }
  }
]);

// 获取组织单树结构
const getOperationResourceList = async () => {
  const { data: operationData } = await fetchAllOperation();
  // eslint-disable-next-line no-unsafe-optional-chaining
  operationOptions.push(...convertOptions({ sourceData: operationData }));

  const { data: resourceData } = await fetchAllResource();
  resourceOptions.push(...convertOptions({ sourceData: resourceData }));
};

onMounted(async () => {
  await getOperationResourceList();
});
</script>
