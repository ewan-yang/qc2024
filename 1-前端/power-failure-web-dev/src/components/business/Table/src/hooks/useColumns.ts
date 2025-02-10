import type { Ref, ComputedRef, RendererElement, RendererNode, VNode } from 'vue';
import { ref, unref, computed, watch, toRaw, h } from 'vue';
import { NTooltip, NIcon } from 'naive-ui';
import { isEqual, cloneDeep } from 'lodash-es';
import { useIconRender } from '@/composables';
import { isArray, isString, isBoolean, isFunction } from '@/utils';
import type { ActionItem } from '@/components/business/Table';
import type { BasicColumn, BasicTableProps } from '../types/table';
import { renderEditCell } from '../components/editable';

export function useColumns(propsRef: ComputedRef<BasicTableProps>) {
  const columnsRef = ref(unref(propsRef).columns) as unknown as Ref<BasicColumn[]>;
  let cacheColumns = unref(propsRef).columns;

  const getColumnsRef = computed(() => {
    const columns = cloneDeep(unref(columnsRef));

    handleActionColumn(propsRef, columns);
    if (!columns) return [];
    return columns;
  });
  // 表格权限暂时不需要
  // const { hasPermission } = usePermission();
  const { iconRender } = useIconRender();

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

  const renderTooltip = (trigger: VNode<RendererNode, RendererElement, { [key: string]: any }>, content: string) => {
    return h(NTooltip, null, {
      trigger: () => trigger,
      default: () => content
    });
  };

  const getPageColumns = computed(() => {
    const pageColumns = unref(getColumnsRef);
    const columns = cloneDeep(pageColumns);
    return (
      columns
        // auth xx:xx
        // .filter((column: any) => {
        //   return hasPermission(column.auth) && isIfShow(column);
        // })
        .filter((column: any) => {
          return isIfShow(column);
        })
        .map(column => {
          // 默认 ellipsis 为true
          column.ellipsis = typeof column.ellipsis === 'undefined' ? { tooltip: true } : false;
          const { edit } = column;
          if (edit) {
            column.render = renderEditCell(column);
            if (edit) {
              const title: any = column.title;
              column.title = () => {
                return renderTooltip(
                  h('span', {}, [
                    h('span', { style: { 'margin-right': '5px' } }, title),
                    h(
                      NIcon,
                      {
                        size: 14
                      },
                      {
                        default: () => h(iconRender({ icon: 'ant-design:form-outlined' }))
                      }
                    )
                  ]),
                  '该列可编辑'
                );
              };
            }
          }
          return column;
        })
    );
  });

  watch(
    () => unref(propsRef).columns,
    columns => {
      columnsRef.value = columns;
      cacheColumns = columns;
    }
  );

  function handleActionColumn(propsRef: ComputedRef<BasicTableProps>, columns: BasicColumn[]) {
    const { actionColumn } = unref(propsRef);
    if (!actionColumn) return;
    !columns.find(col => col.key === 'action') &&
      columns.push({
        ...(actionColumn as any)
      });
  }

  // 设置
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

  // 获取
  function getColumns(): BasicColumn[] {
    const columns = toRaw(unref(getColumnsRef));
    return columns.map(item => {
      return { ...item, title: item.title, key: item.key, fixed: item.fixed || undefined };
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
    getCacheColumns,
    setCacheColumnsField,
    setColumns,
    getColumns,
    getPageColumns
  };
}
