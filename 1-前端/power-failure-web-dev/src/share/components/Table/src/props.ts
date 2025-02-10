import type { PropType } from 'vue';
import { NDataTable } from 'naive-ui';
import type { BasicColumn } from './types/table';
export const basicProps = {
  ...NDataTable.props, // 这里继承原 UI 组件的 props
  dataSource: {
    type: [Object],
    default: () => []
  },
  columns: {
    type: [Array] as PropType<BasicColumn[]>,
    default: () => [],
    required: true
  },
  beforeRequest: {
    type: Function as PropType<(...arg: any[]) => void | Promise<any>>,
    default: null
  },
  request: {
    type: Function as PropType<(...arg: any[]) => Promise<any>>,
    default: null
  },
  afterRequest: {
    type: Function as PropType<(...arg: any[]) => void | Promise<any>>,
    default: null
  },
  rowKey: {
    type: [String, Function] as PropType<string | ((record: any) => string)>,
    default: (row: any) => row.id
  },
  pagination: {
    type: [Object, Boolean],
    default: () => {}
  },
  actionColumn: {
    type: Object as PropType<BasicColumn>,
    default: null
  }
};
