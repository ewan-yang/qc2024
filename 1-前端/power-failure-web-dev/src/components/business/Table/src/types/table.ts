import type { TableBaseColumn } from 'naive-ui/lib/data-table/src/interface';
import type { ComponentType } from './componentType';
export interface BasicColumn extends TableBaseColumn {
  [x: string]: any;
  // 编辑表格
  edit?: boolean;
  editRow?: boolean;
  editable?: boolean;
  editComponent?: ComponentType;
  editComponentProps?: Recordable;
  editRule?: boolean | ((text: string, record: Recordable) => Promise<string>);
  editValueMap?: (value: any) => string;
  onEditRow?: () => void;
  // 权限编码控制是否显示
  auth?: string[];
  // 业务控制是否显示
  ifShow?: boolean | ((column: BasicColumn) => boolean);
  // 控制是否支持拖拽，默认支持
  draggable?: boolean;
  // 是否为搜索列
  showSearch?: boolean;
}

export interface TableActionType {
  reload: (opt: any) => Promise<void>;
  emit?: any;
  getColumns: (opt?: any) => BasicColumn[];
  setColumns: (columns: BasicColumn[] | string[]) => void;
}

export interface BasicTableProps {
  title?: string;
  dataSource: Function;
  columns: any[];
  pagination: object;
  showPagination: boolean;
  actionColumn: any[];
  canResize: boolean;
  resizeHeightOffset: number;
  loading: boolean;
}
