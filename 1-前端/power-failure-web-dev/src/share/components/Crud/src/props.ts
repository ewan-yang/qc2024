import type { PropType } from 'vue';
import type { ActionItem } from '../../Table';
import type { BasicColumn } from '../types/crud';

export const basicProps = {
  // 列配置
  columns: {
    type: [Array] as PropType<BasicColumn[]>,
    default: () => [],
    required: true
  },
  // CRUD 增删改查接口前缀
  name: {
    type: String,
    require: true
  },
  // 自定义操作列
  customAction: {
    type: Function as PropType<(record: any) => ActionItem[]>,
    required: false,
    default: () => []
  },
  // 自定义提交前置数据处理
  submitData: {
    type: Function,
    require: false
  },
  // 自定义提交行为
  submitFunc: {
    type: Function,
    require: false
  },
  // 查询前置数据处理
  loadData: {
    type: Function,
    require: false
  },
  // 自定义操作自定义宽度
  actionColumnWidth: {
    type: String
  },
  // crud 配置
  crudConfig: {
    type: Object,
    default: () => {
      return {
        add: true,
        addText: '新增',
        batchDelete: false,
        batchDeleteText: '批量删除',
        delete: true,
        deleteText: '删除',
        edit: true,
        editText: '编辑'
      };
    }
  }
};
