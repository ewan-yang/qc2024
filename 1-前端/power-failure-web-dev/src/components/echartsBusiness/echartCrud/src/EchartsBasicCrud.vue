<template>
  <div>
    <n-card class="mb-4" hoverable>
      <slot name="formTitle"></slot>
      <proForm
        ref="formRef"
        :schemas="schema.form"
        :grid-props="{ cols: '1 s:1 m:2 l:3 xl:4 2xl:4', xGap: 50 }"
        :label-width="80"

        @submit="submit"
        @reset="reset"
      >
      
    </proForm>

    </n-card>
    <!-- 整体头部统计部分 -->
    <n-card hoverable v-if="statisticalConfig.isHaveTotal && statisticalConfig.totalPosition === 'top'" class="mb-4 h-50" >
      <slot name="statisticalTotalTitle"></slot>
      <slot name="statisticalTotalCustom" v-if="statisticalTotalCustom"></slot>
      <div class="flex justify-between" v-else>
        <div v-for="(item,index) in statisticalColumn" :key="index" class="w-45 h-28   text-center boxShow" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4">{{item.label}}</div>
          <div><span :style="getStatisticalColumnStyle(item.style)">{{item.value}}</span><span>{{item.unit}}</span></div>
        </div>
        <slot name="statisticalTotalEchart"></slot>
      </div>
    </n-card>
    <div class="flex justify-between" :style="statisticalConfig.tablePosition == 'left' ? {flexDirection: 'row'} : {flexDirection: 'row-reverse' }">
      <!-- 表格部分 -->
      <div class="w-70%">
        <!-- 表格头部统计部分 -->
        <n-card hoverable v-if="statisticalConfig.isHaveTotal && statisticalConfig.totalPosition === 'tableTop'" class="m-b-4">
          <slot name="statisticalTotalTitle"></slot>
          <slot name="statisticalTotalCustom" v-if="statisticalTotalCustom"></slot>
          <div class="flex justify-between" v-else>
            <div v-for="(item,index) in statisticalColumn" :key="index" class="w-45 h-28   text-center boxShow" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
              <div class="p-t-2 m-b-4">{{item.label}}</div>
              <div><span :style="getStatisticalColumnStyle(item.style)">{{item.value}}</span><span>{{item.unit}}</span></div>
            </div>
            <slot name="statisticalTotalEchart"></slot>
          </div>
        </n-card>
        <!-- 表格部分 -->
        <n-card hoverable >
          <proTable ref="tableRef"  :columns="schema.table" :request="loadData" :action-column="actionColumn" :row-class-name="rowStyleClassMethod" :scroll-x="schema.table.length * 130" >
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
      </div>
      <!-- 图表部分 -->
      <div class="w-29%">
        <!-- 图表头部统计部分 -->
        <n-card hoverable v-if="statisticalConfig.isHaveTotal && statisticalConfig.totalPosition === 'echartTop'" class="m-b-4">
          <slot name="statisticalTotalTitle"></slot>
          <slot name="statisticalTotalCustom" v-if="statisticalTotalCustom"></slot>
          <div class="flex justify-between" v-else>
            <div v-for="(item,index) in statisticalColumn" :key="index" class="w-45 h-28   text-center boxShow" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
              <div class="p-t-2 m-b-4" style="height: 28%;">{{item.label}}</div>
              <div><span :style="getStatisticalColumnStyle(item.style)">{{item.value!==null?item.value:'--'}}</span><span>{{item.unit}}</span></div>
            </div>
            <slot name="statisticalTotalEchart"></slot>
          </div>
        </n-card>
        <!-- 图表部分 -->
        <n-card hoverable >
          <slot name="echartsTitle"></slot>
          <slot name="echarts"></slot>
        </n-card>
      </div>

    </div>

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
import {  Ref, } from 'vue';
import { defineComponent, ref } from 'vue';
import { NCard, NDrawer, NDrawerContent } from 'naive-ui';
import type { FormActionType } from '../../Form';
import { proForm } from '../../Form';
import type { TableActionType } from '../../Table';
import { proTable } from '../../Table';
import { useCurdEvents } from '../hooks/useCurdEvents';
import { useCurdColumn } from '../hooks/useCurdColumn';
import { basicProps } from './props';
import { isFunction } from '../../../utils';
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

    const {  submit,loadData, reset, modalSubmit, } = useCurdEvents({
      tableRef: tableRef as Ref<TableActionType>,
      props,
      modalFormRef: modalFormRef as Ref<FormActionType>,
      active,
      modalType,
      tableRows,
    });
    function getStatisticalColumnStyle(style){
      return {
        ...style
      };
    }
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
      modalSubmit,
      getStatisticalColumnStyle,
      
    };
  }
});
</script>
<style scoped lang="scss">
.mb-4 {
  margin-bottom: 1rem;
}
.mr-4 {
  margin-right: 1rem;
}


</style>
