import type { PropType } from 'vue';
import { propTypes } from '@/utils';
import type { BasicColumn } from '@/components/business/Table/src/types/table';
import type { ActionItem } from '@/components/business/Table/src/types/tableAction';
/**
 * 字段属性的crud, 字段的搜索列显示需要显式声明showSearch， 新增和编辑详情都是默认声明隐藏hideAdd,hideEdit, hideDetail,组件默认为NInput ,新增，编辑默认剔除状态字段
 */

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
  formatData: {
    type: Function,
    require: false
  },
  // 编辑数据源加点料
  formatEdit: {
    type: Function,
    require: false
  },
  reload: propTypes.bool.def(false),
  // 弹窗模式
  modalType: {
    type: String,
    require: true,
    // m：modal || d：drawer
    default: 'm'
  },
  // 自定义操作列
  customAction: {
    type: Array as PropType<ActionItem[]>,
    default: null
  },
  // 是否显示批量删除按钮
  showBatchDeleteButton: propTypes.bool.def(true),
  // 是否显示新增按钮
  showAddButton: propTypes.bool.def(true),
  // 是否显示编辑按钮
  showEditButton: propTypes.bool.def(true),
  // 是否显示详情按钮
  showDetailButton: propTypes.bool.def(false),
  // 是否显示删除按钮
  showDelButton: propTypes.bool.def(true),
  // 是否显示状态切换按钮
  showStatusButton: propTypes.bool.def(true)
};
