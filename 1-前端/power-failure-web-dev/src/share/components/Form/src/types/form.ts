import type { CSSProperties } from 'vue';
import type { GridProps, GridItemProps } from 'naive-ui/lib/grid';
import type { ButtonProps } from 'naive-ui/lib/button';
import type { FormProps as fp } from 'naive-ui/lib/form/src/Form';
import type { ComponentType } from './index';

export interface FormSchema {
  /**
   * 是否显示在查询表单中,只针对CRUD组件有效
   */
  hideSearch?: boolean;
  /**
   * 是否显示在新增表单中,只针对CRUD组件有效
   */
  hideAdd?: boolean;
  /**
   * 是否显示在编辑表单中,只针对CRUD组件有效
   */
  hideEdit?: boolean;
  detail?: boolean;
  field: string;
  label: string;
  defaultValue?: any;
  component?: ComponentType;
  componentProps?: any;
  slot?: string;
  rules?: object | object[];
  giProps?: GridItemProps;
}

export interface FormProps extends fp {
  model?: Recordable;
  schemas?: FormSchema[];
  labelWidth?: number | string;
  showActionButtonGroup?: boolean;
  showResetButton?: boolean;
  resetButtonOptions?: Partial<ButtonProps>;
  showSubmitButton?: boolean;
  showAdvancedButton?: boolean;
  submitButtonOptions?: Partial<ButtonProps>;
  submitButtonText?: string;
  resetButtonText?: string;
  gridProps?: GridProps;
  giProps?: GridItemProps;
  resetFunc?: () => Promise<void>;
  submitFunc?: () => Promise<void>;
  submitOnReset?: boolean;
  baseGridStyle?: CSSProperties;
}
/**
 * 表单行为,用于【register】事件抛出获取；onMounted
 */
export interface FormActionType {
  [x: string]: any;
  /**
   * 提交表单
   */
  submit: () => Promise<any>;
  /**
   * 手动设置属性
   * @param formProps
   */
  setProps: (formProps: Partial<FormProps>) => Promise<void>;
  /**
   * 设置表单字段值
   */
  setFieldsValue: (values: Recordable) => Promise<void>;
  /**
   * 清空校验
   */
  clearValidate: (name?: string | string[]) => Promise<void>;
  /**
   * 获取表单值
   */
  getFieldsValue: () => Recordable;
  /**
   * 重置
   */
  resetFields: () => Promise<void>;
  /**
   * 验证
   * @returns 返回验证结果
   */
  validate: (nameList?: any[]) => Promise<any>;
}

export type RegisterFn = (formInstance: FormActionType) => void;

export type UseFormReturnType = [RegisterFn, FormActionType];

export interface slotType {
  model: () => Recordable;
  field: string;
  key: number;
  value: any;
}
