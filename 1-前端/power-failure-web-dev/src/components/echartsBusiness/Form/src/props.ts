import type { CSSProperties, PropType } from 'vue';
import type { GridProps, GridItemProps } from 'naive-ui/lib/grid';
import type { ButtonProps } from 'naive-ui/lib/button';
import type { FormSchema } from './types/form';

export const basicProps = {
  // 表单配置规则
  schemas: {
    type: [Array] as PropType<FormSchema[]>,
    default: () => []
  },
  // 是否显示操作按钮（查询/重置）
  showActionButtonGroup: {
    type: Boolean,
    default: true
  },
  // 显示重置按钮
  showResetButton: {
    type: Boolean,
    default: true
  },
  // 重置按钮配置
  resetButtonOptions: {
    type: Object as PropType<Partial<ButtonProps>>,
    default: {
      type: 'default'
    }
  },
  // 显示确认按钮
  showSubmitButton: {
    type: Boolean,
    default: true
  },
  // 确认按钮配置
  submitButtonOptions: {
    type: Object as PropType<Partial<ButtonProps>>,
    default: {
      type: 'primary'
    }
  },
  // 展开收起按钮
  showAdvancedButton: {
    type: Boolean,
    default: true
  },
  // 查询按钮文字
  submitButtonText: {
    type: String,
    default: '查询'
  },
  // 重置按钮文字
  resetButtonText: {
    type: String,
    default: '重置'
  },
  // grid 配置
  gridProps: Object as PropType<GridProps>,
  // gi配置
  giProps: Object as PropType<GridItemProps>,
  // grid 样式
  baseGridStyle: {
    type: Object as PropType<CSSProperties>
  }
};
