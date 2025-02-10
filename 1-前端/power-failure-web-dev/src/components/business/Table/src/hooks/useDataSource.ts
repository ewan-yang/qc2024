/* eslint-disable complexity */
import type { ComputedRef } from 'vue';
import { ref, unref, computed, onMounted, watchEffect, watch } from 'vue';
import { isBoolean, isFunction } from '@/utils';
import type { BasicTableProps } from '../types/table';
import type { PaginationProps } from '../types/pagination';
import { APISETTING } from '../const';

export function useDataSource(
  propsRef: ComputedRef<BasicTableProps>,
  { getPaginationInfo, setPagination, setLoading, tableData },
  emit
) {
  const dataSourceRef = ref<Recordable[]>([]);
  const orginDataSource = ref([]);
  watchEffect(() => {
    tableData.value = unref(dataSourceRef);
  });

  watch(
    () => unref(propsRef).dataSource,
    () => {
      const { dataSource }: any = unref(propsRef);
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

  async function fetch(opt?: { [x: string]: any } | undefined) {
    try {
      setLoading(true);
      const { request, pagination, beforeRequest, afterRequest }: any = unref(propsRef);
      if (!request) return;
      // 组装分页信息
      const pageField = APISETTING.pageField;
      const sizeField = APISETTING.sizeField;
      const totalField = APISETTING.totalField;
      const listField = APISETTING.listField;

      let pageParams = {};
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
        // The params parameter can be modified by outsiders
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
        // can modify the data returned by the interface for processing
        resultInfo = (await afterRequest(resultInfo)) || resultInfo;
      }
      console.log(resultInfo, "resultInfo");
      dataSourceRef.value = resultInfo;
      orginDataSource.value = JSON.parse(JSON.stringify(resultInfo));
      console.log(orginDataSource.value, "resultInfo orginDataSource");
      setPagination({
        [pageField]: currentPage,
        [totalField]: resultTotal
      });
      if (opt && opt[pageField]) {
        setPagination({
          [pageField]: opt[pageField] || 1
        });
      }
      emit('fetch-success', {
        items: unref(resultInfo),
        resultTotal
      });
    } catch (error) {
      console.error(error);
      emit('fetch-error', error);
      dataSourceRef.value = [];
      // setPagination({
      //   pageCount: 0,
      // });
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
    orginDataSource,
    getDataSource,
    setTableData,
    reload,
  };
}
