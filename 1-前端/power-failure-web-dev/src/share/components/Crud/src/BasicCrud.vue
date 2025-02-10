<template>
  <div>
    <n-card class="mb-4" hoverable>
      <proForm
        ref="formRef"
        :schemas="schema.form"
        :grid-props="{ cols: '1 s:1 m:2 l:3 xl:4 2xl:4', xGap: 50 }"
        :label-width="80"
        @submit="submit"
        @reset="reset"
      ></proForm>
    </n-card>
    <n-card hoverable>
      <proTable ref="tableRef" :columns="schema.table" :request="loadData" :action-column="actionColumn">
        <template #toolbar>
          <n-button v-if="crudConfig.add" class="mr-4" type="primary" @click="openSchemas('add')">
            {{ crudConfig.addText }}
          </n-button>
          <n-button v-if="crudConfig.batchDelete" class="mr-4" type="warning">
            {{ crudConfig.batchDeleteText }}
          </n-button>
          <slot name="toolbar"></slot>
        </template>
        <template #tableTitle>
          <slot name="tableTitle"></slot>
        </template>
      </proTable>
    </n-card>
    <n-drawer v-model:show="active" width="60%">
      <n-drawer-content :title="modalType === 'add' ? crudConfig.addText : crudConfig.editText">
        <proForm
          ref="modalFormRef"
          :grid-props="{ cols: '1 s:1 m:2 l:3 xl:4 2xl:4', xGap: 50 }"
          :label-width="80"
        ></proForm>
        <template #footer>
          <n-space>
            <n-button @click="active = false">关闭</n-button>
            <n-button type="primary" @click="modalSubmit">提交</n-button>
          </n-space>
        </template>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>
<script lang="ts">
import type { Ref } from 'vue';
import { defineComponent, ref } from 'vue';
import { NCard, NDrawer, NDrawerContent } from 'naive-ui';
import type { FormActionType } from '../../Form';
import { proForm } from '../../Form';
import type { TableActionType } from '../../Table';
import { proTable } from '../../Table';
import { useCurdEvents } from '../hooks/useCurdEvents';
import { useCurdColumn } from '../hooks/useCurdColumn';
import { basicProps } from './props';

export default defineComponent({
  name: 'BasicCurd',
  components: { proForm, proTable, NCard, NDrawer, NDrawerContent },
  props: {
    ...basicProps
  },
  setup(props) {
    const formRef = ref<Nullable<FormActionType>>(null);
    const tableRef = ref<Nullable<TableActionType>>(null);
    const modalFormRef = ref<Nullable<FormActionType>>(null);
    const active = ref(false);

    const { transformColumns, openSchemas, actionColumn, modalType, tableRows } = useCurdColumn({
      props,
      active,
      modalFormRef: modalFormRef as Ref<FormActionType>,
      tableRef: tableRef as Ref<TableActionType>
    });

    const { submit, loadData, reset, modalSubmit } = useCurdEvents({
      tableRef: tableRef as Ref<TableActionType>,
      props,
      modalFormRef: modalFormRef as Ref<FormActionType>,
      active,
      modalType,
      tableRows
    });

    return {
      formRef,
      tableRef,
      modalFormRef,
      schema: transformColumns(),
      active,
      actionColumn: actionColumn(),
      tableRows,
      modalType,
      openSchemas,
      submit,
      reset,
      loadData,
      modalSubmit
    };
  }
});
</script>
<style scoped>
.mb-4 {
  margin-bottom: 1rem;
}
.mr-4 {
  margin-right: 1rem;
}
</style>
