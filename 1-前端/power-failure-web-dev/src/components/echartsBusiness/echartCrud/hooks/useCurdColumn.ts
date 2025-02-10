import { unref, type Ref, nextTick, h, ref } from 'vue';
import { request } from '@/service/request';
import type { FormActionType, FormSchema } from '../../Form';
import type { TableActionType, BasicColumn } from '../../Table';
import type { BasicColumn as curdColumn, ActionType } from '../types/crud';
import { TableAction } from '../../Table';
import { isBoolean, isFunction, isUndefined } from '../../../../utils';

export function useCurdColumn({
  props,
  active,
  modalFormRef,
  tableRef
}: {
  props: any;
  active: Ref<boolean>;
  modalFormRef: Ref<FormActionType>;
  tableRef: Ref<TableActionType>;
}) {
  const formSchemas: FormSchema[] = [];
  const tableSchemas: BasicColumn[] = [];
  // 当前表单打开类型
  const modalType: Ref<ActionType> = ref('add');
  // 当前操作行
  const tableRows: any = ref({});
  function transformColumns(): { form: FormSchema[]; table: BasicColumn[] } {
    props.columns.forEach(({ field, label, formConfig = {}, tableConfig = {} }: curdColumn) => {
      const tableSchema: BasicColumn = {
        key: field,
        title: label,
        ...tableConfig
      };
      tableSchemas.push(tableSchema);

      const formSchema: FormSchema = {
        field,
        label,
        component: 'NInput',
        ...formConfig
      };
      formSchemas.push(formSchema);
    });
    return {
      form: formSchemas.filter(t => (isBoolean(t.hideSearch) && !t.hideSearch) || isUndefined(t.hideSearch)),
      table: tableSchemas
    };
  }

  function openSchemas(type: ActionType, record: any = null) {
    active.value = true;
    const mt = unref(modalType);
    if (mt !== type) modalType.value = type;
    tableRows.value = record;
    // nextTick 是弹窗没打开时，modalFormRef为null
    // 待扩展, 比如自定义行为
    nextTick(() => {
      const mfr = unref(modalFormRef);
      mfr.setProps({
        schemas: formSchemas.filter(
          schema =>
            !(type === 'add' && ((isBoolean(schema.hideAdd) && !schema.hideAdd) || !isUndefined(schema.hideAdd))) &&
            !((type === 'edit' && isBoolean(schema.hideEdit) && !schema.hideEdit) || !isUndefined(schema.hideEdit))
        ),
        showActionButtonGroup: false
      });
      // 通用行为，如果record给值了,就一定渲染
      if (record) mfr.setFieldsValue(record);
    });
  }
  async function deleteRow(record: { id: any }) {
    const tableref = unref(tableRef);
    const res = await request.delete(`${props.name}/${record.id}`);
    if (!res.error) {
      tableref?.reload();
    }
  }
  function actionColumn() {
    const column = {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: props.actionColumnWidth || getActionColumn({}).length * 82,
      render(record: any) {
        return h(TableAction, {
          actions: getActionColumn(record)
        });
      }
    };
    if(props.isShowAction){
      return column;
    }else{
      return {ifShow:false}
    }
  }
  function getActionColumn(record: any) {
    let TableActionColumn: any[] = [];
    // 提供一些内置行为
    if (props.crudConfig.edit) {
      TableActionColumn.push({
        label: props.crudConfig.editText,
        type: 'info',
        onClick: openSchemas.bind(null, 'edit', record)
      });
    }
    if (props.crudConfig.delete) {
      TableActionColumn.push({
        label: props.crudConfig.deleteText,
        type: '',
        showConfirm: true,
        confirmHandle: deleteRow.bind(null, record)
      });
    }
    if (props.customAction && isFunction(props.customAction))
      TableActionColumn = [...props.customAction(record), ...TableActionColumn];
    return TableActionColumn;
  }
  return {
    transformColumns,
    openSchemas,
    actionColumn,
    modalType,
    tableRows
  };
}
