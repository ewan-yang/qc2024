import type { TableBaseColumn } from 'naive-ui/lib/data-table/src/interface';
export interface BasicColumn extends TableBaseColumn {
  [x: string]: any;
  // 权限编码控制是否显示
  auth?: string[];
  // 业务控制是否显示
  ifShow?: boolean | ((column: BasicColumn) => boolean);
  // 控制是否支持拖拽，默认支持
  draggable?: boolean;
}

export interface TableActionType {
  setProps: (opt?: any) => Promise<void>;
  reload: (opt?: any) => Promise<void>;
  emit?: any;
  getColumns: (opt?: any) => BasicColumn[];
  setColumns: (columns: BasicColumn[] | string[]) => void;
}

export interface BasicTableProps {
  title?: string;
  dataSource: Fn;
  columns: any[];
  pagination: object;
  showPagination: boolean;
  actionColumn: any[];
  canResize: boolean;
  resizeHeightOffset: number;
  loading: boolean;
}
