import type { Ref, ComputedRef } from 'vue';
import { ref, unref, computed, watch, toRaw } from 'vue';
import { isEqual, cloneDeep } from 'lodash-es';
import { isArray, isString, isBoolean, isFunction } from '../../../../utils';
import type { ActionItem } from '../types/tableAction';
import type { BasicColumn, BasicTableProps } from '../types/table';

export function useColumns(propsRef: ComputedRef<BasicTableProps>) {
  const columnsRef = ref(unref(propsRef).columns) as unknown as Ref<BasicColumn[]>;
  let cacheColumns = unref(propsRef).columns;

  const getColumnsRef = computed(() => {
    const columns = cloneDeep(unref(columnsRef));

    handleActionColumn(propsRef, columns);
    if (!columns) return [];
    return columns;
  });

  function isIfShow(action: ActionItem): boolean {
    const ifShow = action.ifShow;

    let ifShowF = true;

    if (isBoolean(ifShow)) {
      ifShowF = ifShow;
    }
    if (isFunction(ifShow)) {
      ifShowF = ifShow(action);
    }
    return ifShowF;
  }

  const getPageColumns = computed(() => {
    const pageColumns = unref(getColumnsRef);
    const columns = cloneDeep(pageColumns);
    console.log(columns.filter((column: any) => {
      return isIfShow(column);
    }),'columns')

    return columns.filter((column: any) => {
      return isIfShow(column);
    });
  });

  watch(
    () => unref(propsRef).columns,
    columns => {
      columnsRef.value = columns;
      cacheColumns = columns;
    }
  );

  // eslint-disable-next-line @typescript-eslint/no-shadow
  function handleActionColumn(propsRef: ComputedRef<BasicTableProps>, columns: BasicColumn[]) {
    const { actionColumn } = unref(propsRef);
    if (!actionColumn) return;
    // eslint-disable-next-line no-unused-expressions
    !columns.find(col => col.key === 'action') &&
      columns.push({
        ...(actionColumn as any)
      });
  }

  /**
   * 设置
   */
  function setColumns(columnList: string[]) {
    const columns: any[] = cloneDeep(columnList);
    if (!isArray(columns)) return;

    if (!columns.length) {
      columnsRef.value = [];
      return;
    }
    const cacheKeys = cacheColumns.map(item => item.key);
    // 针对拖拽排序
    if (!isString(columns[0])) {
      columnsRef.value = columns;
    } else {
      const newColumns: any[] = [];
      cacheColumns.forEach(item => {
        if (columnList.includes(item.key)) {
          newColumns.push({ ...item });
        }
      });
      if (!isEqual(cacheKeys, columns)) {
        newColumns.sort((prev, next) => {
          return cacheKeys.indexOf(prev.key) - cacheKeys.indexOf(next.key);
        });
      }
      columnsRef.value = newColumns;
    }
  }

  /**
   * 获取
   */
  function getColumns(): BasicColumn[] {
    const columns = toRaw(unref(getColumnsRef));
    return columns.map((item: any) => {
      return {
        ...item,
        title: item.title,
        key: item.key,
        fixed: item.fixed || undefined
      };
    });
  }

  // 获取原始
  function getCacheColumns(isKey?: boolean): any[] {
    return isKey ? cacheColumns.map(item => item.key) : cacheColumns;
  }

  // 更新原始数据单个字段
  function setCacheColumnsField(key: string | undefined, value: Partial<BasicColumn>) {
    if (!key || !value) {
      return;
    }
    cacheColumns.forEach(item => {
      if (item.key === key) {
        Object.assign(item, value);
      }
    });
  }

  return {
    getColumnsRef,
    getPageColumns,
    getCacheColumns,
    setCacheColumnsField,
    setColumns,
    getColumns
  };
}
