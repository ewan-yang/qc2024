/* eslint-disable complexity */
import type { ComputedRef } from 'vue';
import { ref, unref, computed, onMounted, watchEffect, watch } from 'vue';
import { isBoolean, isFunction } from '../../../../utils';
import type { BasicTableProps } from '../types/table';
import type { PaginationProps } from '../types/pagination';

export function useDataSource(
  propsRef: ComputedRef<BasicTableProps>,
  { getPaginationInfo, setPagination, setLoading, tableData }: any
) {
  const dataSourceRef = ref<Recordable[]>([]);
  const apiSetting = {
    // 当前页的字段名
    pageField: 'pageIndex',
    // 每页数量字段名
    sizeField: 'pageSize',
    // 接口返回的数据字段名
    listField: 'data',
    // 接口返回总页数字段名
    totalField: 'pageCount'
  };
  watchEffect(() => {
    tableData.value = unref(dataSourceRef);
  });

  watch(
    () => unref(propsRef).dataSource,
    () => {
      const { dataSource }: any = unref(propsRef);
      // eslint-disable-next-line no-unused-expressions
      dataSource && (dataSourceRef.value = dataSource);
    },
    {
      immediate: true
    }
  );

  const getRowKey = computed(() => {
    const { rowKey }: any = unref(propsRef);
    return rowKey
      ? rowKey
      : () => {
          return 'key';
        };
  });

  const getDataSourceRef = computed(() => {
    const dataSource = unref(dataSourceRef);
    if (!dataSource || dataSource.length === 0) {
      return unref(dataSourceRef);
    }
    return unref(dataSourceRef);
  });

  /**
   * 封装表格请求
   * @param opt 表格参数
   */
  async function fetch(opt?: { [x: string]: any } | undefined) {
    try {
      setLoading(true);
      const { request, pagination, beforeRequest, afterRequest }: any = unref(propsRef);
      if (!request) return;
      // 组装分页信息
      const pageField = apiSetting.pageField;
      const sizeField = apiSetting.sizeField;
      const totalField = apiSetting.totalField;
      const listField = apiSetting.listField;

      let pageParams: any = {};
      const { page = 1, pageSize = 10 } = unref(getPaginationInfo) as PaginationProps;

      if ((isBoolean(pagination) && !pagination) || isBoolean(getPaginationInfo)) {
        pageParams = {};
      } else {
        pageParams[pageField] = (opt && opt[pageField]) || page;
        pageParams[sizeField] = pageSize;
      }

      let params = {
        ...pageParams
      };
      if (beforeRequest && isFunction(beforeRequest)) {
        // params参数可以被修改
        params = (await beforeRequest(params)) || params;
      }
      const res = await request(params);
      const resultTotal = res?.data[totalField] || 0;
      const currentPage = res?.data[pageField];

      // 如果数据异常，需获取正确的页码再次执行
      if (resultTotal) {
        if (page > resultTotal) {
          setPagination({
            [pageField]: resultTotal
          });
          fetch(opt);
        }
      }
      let resultInfo = res?.data[listField] ? res?.data[listField] : [];
      if (afterRequest && isFunction(afterRequest)) {
        // 可以修改接口返回的数据进行处理
        resultInfo = (await afterRequest(resultInfo)) || resultInfo;
      }
      dataSourceRef.value = resultInfo;
      setPagination({
        [pageField]: currentPage,
        [totalField]: resultTotal
      });
      if (opt && opt[pageField]) {
        setPagination({
          [pageField]: opt[pageField] || 1
        });
      }
    } catch (error) {
      dataSourceRef.value = [];
    } finally {
      setLoading(false);
    }
  }

  onMounted(() => {
    setTimeout(() => {
      fetch();
    }, 16);
  });

  function setTableData(values: Recordable<any>[]) {
    dataSourceRef.value = values;
  }

  function getDataSource(): any[] {
    return getDataSourceRef.value;
  }

  async function reload(opt?: any) {
    await fetch(opt);
  }

  return {
    fetch,
    getRowKey,
    getDataSourceRef,
    getDataSource,
    setTableData,
    reload
  };
}
