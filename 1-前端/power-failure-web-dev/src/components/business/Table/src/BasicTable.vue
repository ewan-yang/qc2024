<!-- eslint-disable @typescript-eslint/no-shadow -->
<template>
  <div class="table-toolbar">
    <!--顶部左侧区域-->
    <div class="flex items-center table-toolbar-left">
      <template v-if="title">
        <div class="table-toolbar-left-title">
          {{ title }}
          <n-tooltip v-if="titleTooltip" trigger="hover">
            <template #trigger>
              <n-icon size="18" class="ml-1 text-gray-400 cursor-pointer">
                <icon-ant-design:question-circle-outlined />
              </n-icon>
            </template>
            {{ titleTooltip }}
          </n-tooltip>
        </div>
      </template>
      <slot name="tableTitle"></slot>
    </div>

    <div class="flex items-center table-toolbar-right">
      <!--顶部右侧区域-->
      <slot name="toolbar"></slot>

      <!--斑马纹-->
      <!-- <n-tooltip trigger="hover">
        <template #trigger>
          <div class="mr-2 table-toolbar-right-icon">
            <n-switch v-model:value="isStriped" @update:value="setStriped" />
          </div>
        </template>
        <span>表格斑马纹</span>
      </n-tooltip>
      <n-divider vertical /> -->

      <!--刷新-->
      <n-tooltip trigger="hover">
        <template #trigger>
          <div class="table-toolbar-right-icon" @click="reload">
            <n-icon size="18">
              <icon-ant-design:reload-outlined />
            </n-icon>
          </div>
        </template>
        <span>刷新</span>
      </n-tooltip>

      <!--密度-->
      <n-tooltip trigger="hover">
        <template #trigger>
          <div class="table-toolbar-right-icon">
            <n-dropdown v-model:value="tableSize" trigger="click" :options="densityOptions" @select="densitySelect">
              <n-icon size="18">
                <icon-ant-design:column-height-outlined />
              </n-icon>
            </n-dropdown>
          </div>
        </template>
        <span>密度</span>
      </n-tooltip>

      <!--表格设置单独抽离成组件-->
      <ColumnSetting />
    </div>
  </div>
  <div class="table-toolbar-bottom">
    <slot name="tableToolbarBottom"></slot>
  </div>
  <div class="s-table">
    <n-data-table
      ref="tableElRef"
      v-bind="getBindValues"
      :striped="isStriped"
      :pagination="pagination"
      @update:page="updatePage"
      @update:page-size="updatePageSize"
      :scroll-x="columns.length * 160"
    >
      <template v-for="item in Object.keys($slots)" #[item]="data" :key="item">
        <slot :name="item" v-bind="data"></slot>
      </template>
    </n-data-table>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent, reactive, unref, toRaw, computed, toRefs, onMounted, nextTick } from 'vue';
import { getViewportOffset, isBoolean } from '@/utils';
import { useWindowSizeFn } from '@/hooks/common/useWindowSizeFn';
import { createTableContext } from './hooks/useTableContext';
import ColumnSetting from './components/settings/ColumnSetting.vue';
import { useLoading } from './hooks/useLoading';
import { useColumns } from './hooks/useColumns';
import { useDataSource } from './hooks/useDataSource';
import { usePagination } from './hooks/usePagination';
import { basicProps } from './props';
import type { BasicTableProps } from './types/table';

const densityOptions = [
  {
    type: 'menu',
    label: '紧凑',
    key: 'small'
  },
  {
    type: 'menu',
    label: '默认',
    key: 'medium'
  },
  {
    type: 'menu',
    label: '宽松',
    key: 'large'
  }
];

export default defineComponent({
  name: 'BasicTable',
  components: {
    ColumnSetting
  },
  props: {
    ...basicProps
  },
  emits: [
    'fetch-success',
    'fetch-error',
    'update:checked-row-keys',
    'edit-end',
    'edit-cancel',
    'edit-row-end',
    'edit-change'
  ],
  setup(props, { emit }) {
    const deviceHeight = ref(150);
    const tableElRef = ref<ComponentRef>(null);
    const wrapRef = ref<Nullable<HTMLDivElement>>(null);
    let paginationEl: HTMLElement | null;
    const isStriped = ref(false);
    const tableData = ref<Recordable[]>([]);
    const innerPropsRef = ref<Partial<BasicTableProps>>();
    console.log(props,'propsprops')
    const getProps = computed(() => {
      return { ...props, ...unref(innerPropsRef) } as BasicTableProps;
    });
    const tableName = props.tableName
    const { getLoading, setLoading } = useLoading(getProps);

    const { getPaginationInfo, setPagination } = usePagination(getProps);

    const { getDataSourceRef,orginDataSource, getDataSource, getRowKey, reload } = useDataSource(
      getProps,
      {
        getPaginationInfo,
        setPagination,
        tableData,
        setLoading
      },
      emit
    );

    const { getPageColumns, setColumns, getColumns, getCacheColumns, setCacheColumnsField } = useColumns(getProps);
    const state = reactive({
      tableSize: unref(getProps as any).size || 'medium',
      isColumnSetting: false
    });
    const sorterOrder = ref('false')

    // 页码切换
    function updatePage(page) {
      setPagination({ page });
      reload();
      setTimeout(()=>{
       initSorter()
      },500)
    }

    // 分页数量切换
    function updatePageSize(size) {
      setPagination({ page: 1, pageSize: size });
      reload();
      setTimeout(()=>{
       initSorter()
      },500)

    }

    // 密度切换
    function densitySelect(e) {
      state.tableSize = e;
    }

    // 选中行
    // function updateCheckedRowKeys(rowKeys) {
    //   emit('update:checked-row-keys', rowKeys);
    // }

    // 获取表格大小
    const getTableSize = computed(() => state.tableSize);


    function sorterTableData(options){
      console.log(options,'options')
      console.log(getDataSourceRef,'getDataSourceRef')
      if(options.columnKey === 'startTime'){
        if(options.order === 'ascend'){
          sorterOrder.value = 'ascend'
          getDataSourceRef.value.sort((row1, row2) => {
            const date1 = new Date(row1.startTime);
            const date2 = new Date(row2.startTime);
            return date1.getTime() - date2.getTime();
          });
        }else if(options.order === 'descend'){
          sorterOrder.value = 'descend'
          getDataSourceRef.value.sort((row1, row2) => {
            const date1 = new Date(row1.startTime);
            const date2 = new Date(row2.startTime);
            return date2.getTime() - date1.getTime();
          });
        }else{

         // 正确的方法应该是直接修改数组内容
         getDataSourceRef.value.splice(0, getDataSourceRef.value.length, ...orginDataSource.value); // 替换整个数组
        }
      }else if(options.columnKey === 'accountNumber'){
        if(options.order === 'ascend'){
          sorterOrder.value = 'ascend'
          getDataSourceRef.value.sort((row1, row2) => row1.accountNumber - row2.accountNumber);
        }else if(options.order === 'descend'){
          sorterOrder.value = 'descend'
          getDataSourceRef.value.sort((row1, row2) => row2.accountNumber - row1.accountNumber);
        }else{
         // 正确的方法应该是直接修改数组内容
         getDataSourceRef.value.splice(0, getDataSourceRef.value.length, ...orginDataSource.value); // 替换整个数组
        }
      }
    }

    function initSorter(){
      if(tableName === 'user'){
        if(sorterOrder.value === 'ascend'){
          getDataSourceRef.value.sort((row1, row2) => row1.accountNumber - row2.accountNumber);
        }else if(sorterOrder.value === 'descend'){
          getDataSourceRef.value.sort((row1, row2) => row2.accountNumber - row1.accountNumber);
        }
      }else if(tableName === 'task'){
       if(sorterOrder.value === 'ascend'){
          getDataSourceRef.value.sort((row1, row2) => {
            const date1 = new Date(row1.startTime);
            const date2 = new Date(row2.startTime);
            return date1.getTime() - date2.getTime();
          });
        }else if(sorterOrder.value === 'descend'){
          getDataSourceRef.value.sort((row1, row2) => {
            const date1 = new Date(row1.startTime);
            const date2 = new Date(row2.startTime);
            return date2.getTime() - date1.getTime();
          });
        }
      }

    }
    // 组装表格信息
    const getBindValues = computed(() => {
      // eslint-disable-next-line @typescript-eslint/no-shadow
      const tableData = unref(getDataSourceRef);
      const maxHeight = tableData.length ? `${unref(deviceHeight)}px` : 'auto';
      return {
        ...unref(getProps),
        loading: unref(getLoading),
        columns: toRaw(unref(getPageColumns)),
        rowKey: unref(getRowKey),
        data: tableData,
        size: unref(getTableSize),
        remote: true,
        'max-height': maxHeight
      };
    });
    console.log('getBindValues', unref(getBindValues));
    // 获取分页信息
    const pagination = computed(() => toRaw(unref(getPaginationInfo)));

    // eslint-disable-next-line @typescript-eslint/no-shadow
    function setProps(props: Partial<BasicTableProps>) {
      innerPropsRef.value = { ...unref(innerPropsRef), ...props };
    }

    const setStriped = (value: boolean) => (isStriped.value = value);

    const tableAction = {
      reload,
      setColumns,
      setLoading,
      setProps,
      getColumns,
      getPageColumns,
      
      getCacheColumns,
      setCacheColumnsField,
      emit
    };

    const getCanResize = computed(() => {
      const { canResize } = unref(getProps);
      return canResize;
    });

    async function computeTableHeight() {
      const table = unref(tableElRef);
      if (!table) return;
      if (!unref(getCanResize)) return;
      const tableEl: any = table?.$el;
      const headEl = tableEl.querySelector('.n-data-table-thead ');
      const { bottomIncludeBody } = getViewportOffset(headEl);
      const headerH = 64;
      let paginationH = 2;
      const marginH = 24;
      if (!isBoolean(pagination)) {
        paginationEl = tableEl.querySelector('.n-data-table__pagination') as HTMLElement;
        if (paginationEl) {
          const offsetHeight = paginationEl.offsetHeight;

          paginationH += offsetHeight || 0;
        } else {
          paginationH += 28;
        }
      }
      let height = bottomIncludeBody - (headerH + paginationH + marginH + (props.resizeHeightOffset || 0));
      const maxHeight = props.maxHeight;
      const minHeight = props.minHeight;
      height = maxHeight && maxHeight < height ? maxHeight : height;
      height = minHeight && minHeight > height ? minHeight : height;

      deviceHeight.value = height;
    }

    useWindowSizeFn(computeTableHeight, 280);

    onMounted(() => {
      nextTick(() => {
        computeTableHeight();
      });
    });

    createTableContext({ ...tableAction, wrapRef, getBindValues });

    return {
      ...toRefs(state),
      tableElRef,
      getBindValues,
      getDataSource,
      densityOptions,
      reload,
      densitySelect,
      updatePage,
      updatePageSize,
      pagination,
      tableAction,
      setStriped,
      isStriped,
      setPagination,
      sorterTableData,
      sorterOrder,
    };
  }
});
</script>
<style lang="scss" scoped>
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

.table-toolbar-inner-popover-title {
  padding: 2px 0;
}

:deep(.n-data-table .n-data-table-thead .n-data-table-th){
  background-color:#EDF2FA !important;
}
</style>
