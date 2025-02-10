<template>
  <div>
    <div class="table-toolbar">
      <!--顶部左侧区域-->
      <div class="flex items-center table-toolbar-left">
        <slot name="tableTitle"></slot>
      </div>
      <div class="flex items-center table-toolbar-right">
        <!--顶部右侧区域-->
        <slot name="toolbar"></slot>
      </div>
    </div>
    <div class="s-table">
      <n-data-table
        ref="tableElRef"
        v-bind="getBindValues"
        :pagination="pagination"
        @update:page="updatePage"
        @update:page-size="updatePageSize"
      >
        <template v-for="item in Object.keys($slots)" #[item]="data" :key="item">
          <slot :name="item" v-bind="data"></slot>
        </template>
      </n-data-table>
    </div>
  </div>
</template>
<script lang="ts">
import { ref, defineComponent, toRaw, unref, computed } from 'vue';
import { NDataTable } from 'naive-ui';
import { usePagination } from './hooks/usePagination';
import { basicProps } from './props';
import { useLoading } from './hooks/useLoading';
import { useDataSource } from './hooks/useDataSource';
import type { BasicTableProps } from './types/table';
import { useColumns } from './hooks/useColumns';

export default defineComponent({
  name: 'BasicTable',
  components: { NDataTable },
  props: {
    ...basicProps
  },
  setup(props) {
    const tableElRef = ref<ComponentRef>(null);
    const tableData = ref<Recordable[]>([]);
    const innerPropsRef = ref<Partial<BasicTableProps>>();
    const getProps = computed(() => {
      return { ...props, ...unref(innerPropsRef) } as BasicTableProps;
    });
    // usePagination 主要封装页面数据行为
    const { getPaginationInfo, setPagination } = usePagination(getProps);
    const { getLoading, setLoading } = useLoading(getProps);
    // useDataSource 主要封装表格数据行为
    const { getDataSourceRef, getRowKey, reload } = useDataSource(getProps, {
      getPaginationInfo,
      setPagination,
      tableData,
      setLoading
    });
    // useColumns 主要处理一些列的特殊功能，比如权限,是否显示等
    const { getPageColumns } = useColumns(getProps);
    // 获取分页信息
    const pagination = computed(() => toRaw(unref(getPaginationInfo)));
    // 组装表格信息
    const getBindValues = computed(() => {
      return {
        ...unref(getProps),
        loading: unref(getLoading),
        columns: toRaw(unref(getPageColumns)),
        rowKey: unref(getRowKey),
        data: unref(getDataSourceRef),
        remote: true
      };
    });
    // --------------------------------------组件事件-----------------------------------------------------------
    // 页码切换
    function updatePage(page: number) {
      setPagination({ page });
      reload();
    }

    // 分页数量切换
    function updatePageSize(size: number) {
      setPagination({ page: 1, pageSize: size });
      reload();
    }
    // eslint-disable-next-line @typescript-eslint/no-shadow
    function setProps(props: Partial<BasicTableProps>) {
      innerPropsRef.value = { ...unref(innerPropsRef), ...props };
    }

    return {
      tableElRef,
      pagination,
      getBindValues,
      setProps,
      updatePage,
      updatePageSize,
      reload
    };
  }
});
</script>
<style lang="scss" scoped>
.flex {
  display: flex;
}
.items-center {
  align-items: center;
}
.table-toolbar {
  display: flex;
  justify-content: space-between;
  padding: 0 0 16px 0;

  &-left {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex: 1;

    &-title {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-size: 16px;
      font-weight: 600;
    }
  }

  &-right {
    display: flex;
    justify-content: flex-end;
    flex: 1;

    &-icon {
      margin-left: 12px;
      font-size: 16px;
      cursor: pointer;
      color: var(--text-color);

      :hover {
        color: #1890ff;
      }
    }
  }
}
</style>
