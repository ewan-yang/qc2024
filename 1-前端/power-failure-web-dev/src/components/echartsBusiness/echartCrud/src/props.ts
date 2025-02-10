import type { PropType } from 'vue';
import type { ActionItem } from '../../Table';
import type { BasicColumn } from '../types/crud';
import type { statisticalBasicColumn} from '../types/crud';
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
  submit: {
    type: Function,
    require: false
  },
  reset: {
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
  // 是否展示操作列
  isShowAction:{
    type: Boolean,
    default:()=>{
      return true
    }
  },
  // 挑选你想要的哪几行 改变他的样式
  rowStyleClassMethod:{
    type: Function,
    require: false
  },
  //统计配置 
  /**
   * 
   * totalPosition代表着数量card的位置，tablePosition代表着table和echart谁在左边，谁在右边
   * isHaveTotal: true或false,代表着是否有统计的card
   * totalPosition：值可以选择top、tableTop、echartTop
   * tablePosition：值可以选择left、right
  */
  statisticalConfig:{
    type: Object,
    default: () => {
      return {
        isHaveTotal: true,
        totalPosition: 'top',
        tablePosition: 'left',
      }
    }
  },
  //统计自定义
  /**
   * statisticalTotalCustom为true时,不需要接收statisticalColumn,开启插槽,写入想要的样式
  */
  statisticalTotalCustom:{
    type: Boolean,
    default: () => {
      return false
    }
  },
  //传进来的统计数据数组
  statisticalColumn:{
    type: [Array] as PropType<statisticalBasicColumn[]>,
    default: () => [],
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
  },
};
