import type { Component } from 'vue';
import type { NButton } from 'naive-ui';
import type { BasicColumn } from './table';
export interface ActionItem extends Partial<InstanceType<typeof NButton>> {
  onClick?: Fn;
  label?: string;
  type?: 'success' | 'error' | 'warning' | 'info' | 'primary' | 'default';
  // 设定 color 后会覆盖 type 的样式
  color?: string;
  icon?: Component;
  /**
   * 业务控制是否显示
   */

  ifShow?: boolean | ((column: BasicColumn) => boolean);
  /**
   * 控制是否有确认提示
   */
  showConfirm?: boolean;
  /**
   * 确认提示文案，默认 '请确认此操作?'
   */
  popconfirmText?: string;
  /**
   * 确认提示方法
   */
  confirmHandle?: Fn;
}
