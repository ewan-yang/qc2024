import type { ComputedRef } from 'vue';
import { computed, unref, ref } from 'vue';
import { isBoolean } from '../../../../utils';
import type { PaginationProps } from '../types/pagination';
import type { BasicTableProps } from '../types/table';

export function usePagination(refProps: ComputedRef<BasicTableProps>) {
  const configRef = ref<PaginationProps>({});

  const getPaginationInfo = computed((): PaginationProps => {
    const { pagination } = unref(refProps);
    return {
      pageSize: 10,
      pageSizes: [10, 20, 30, 40, 50],
      showSizePicker: true,
      showQuickJumper: true,
      ...(isBoolean(pagination) ? {} : pagination),
      ...unref(configRef),
      pageCount: unref(configRef).pageCount
    };
  });

  function setPagination(info: Partial<PaginationProps>) {
    const paginationInfo = unref(getPaginationInfo);
    configRef.value = {
      ...(!isBoolean(paginationInfo) ? paginationInfo : {}),
      ...info
    };
  }

  return { getPaginationInfo, setPagination };
}
