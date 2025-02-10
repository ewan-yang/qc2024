import type { FormActionType, FormSchema } from '../../Form';
import type { BasicColumn as tableColumn, ActionItem, TableActionType } from '../../Table';

export interface curdProps {
  columns: BasicColumn[];
  name: string;
  customAction: ActionItem[];
  crudConfig: any;
  /**
   * 自定义操作自定义宽度
   */
  actionColumnWidth: string;
  /**
   * @description:modal自定义提交
   * @returns void
   */
  submitFunc?: () => Promise<void>;
  /**
   * @description:窗口提交 -> formModel前置处理
   * @param data
   * @returns Promise<any>
   */
  submitData?: (data: any) => Promise<any>;
  /**
   * @description:数据加载 -> params参数前置处理
   * @param data
   * @returns Promise<any>
   */
  loadData?: (data: any) => object;
}

interface d {
  field: string;
  label: string;
}

interface formCrudConfig {
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
}

export type BasicColumn = {
  field: string;
  label: string;
  tableConfig?: Omit<tableColumn, keyof d>;
  formConfig?: Omit<FormSchema, keyof d> & formCrudConfig;
};
export type statisticalBasicColumn = {
  field: string;
  label: string;
  unit: string;
  value: string | number;
  style: Object;
}
export type ActionType = 'add' | 'edit' | string;

export interface curdActionType {
  formRef: FormActionType;
  tableRef: TableActionType;
  modalFormRef: FormActionType;
  schema: any;
  active: boolean;
  actionColumn: any[];
  tableRows: object;
  modalType: ActionType;
  openSchemas: Fn;
  submit: Fn;
  reset: Fn;
  loadData: Fn;
  modalSubmit: Fn;
}
