import type { Component } from 'vue';
import type { NButton } from 'naive-ui';
import type { PermissionsEnum } from '@/enum';
export interface ActionItem extends Partial<InstanceType<typeof NButton>> {
  onClick?: Fn;
  label?: string;
  type?: 'success' | 'error' | 'warning' | 'info' | 'primary' | 'default';
  // 设定 color 后会覆盖 type 的样式
  color?: string;
  icon?: Component;
  popConfirm?: PopConfirm;
  disabled?: boolean;
  divider?: boolean;
  // 权限编码控制是否显示
  auth?: PermissionsEnum | PermissionsEnum[] | string | string[];
  // 业务控制是否显示
  ifShow?: boolean | ((action: ActionItem) => boolean);
  // 业务控制是否显示在新增
  hideAdd?: boolean | ((action: ActionItem) => boolean);
  // 控制是否有确认提示
  showConfirm?: boolean;
  // 通用操作方法
  confirmHandle?: Fn;
  // 自定义事件
  event?: Fn;
}

export interface PopConfirm {
  title: string;
  okText?: string;
  cancelText?: string;
  confirm: Fn;
  cancel?: Fn;
  icon?: Component;
}
