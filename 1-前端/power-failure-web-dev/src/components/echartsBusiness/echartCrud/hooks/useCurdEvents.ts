import { ref, typeRef, unref, reactive, Ref } from 'vue';
import { useMessage } from 'naive-ui';
import { request } from '@/service/request';
import type { TableActionType } from '../../Table';
import type { FormActionType } from '../../Form';
import { isFunction } from '../../../utils';
import type { ActionType } from '../types/crud';

export function useCurdEvents({
  tableRef,
  props,
  modalFormRef,
  active,
  modalType,
  tableRows,
}: {
  tableRef: Ref<TableActionType>;
  props: any;
  modalFormRef: Ref<FormActionType>;
  active: Ref<boolean>;
  modalType: Ref<ActionType>;
  tableRows: any;
}) {
  const message = useMessage();
  const params = ref({});
  /**
   * 重载
   */
  function reload() {
    const tableEl = unref(tableRef);
    tableEl.reload();
  }
  /**
   * 查询
   */
  async function submit(values: Recordable) {
    if (props.submit && isFunction(props.submit)) {
      if(values.confirmTime){
        values.confirmTimeBegin = values.confirmTime[0]
        values.confirmTimeEnd = values.confirmTime[1]
      }
      if(values.startTime){
        values.startDateBegin = values.startTime[0]
        values.startDateEnd = values.startTime[1]
      }
      if(values.feedbackTime){
        values.feedbackTimeBegin = values.feedbackTime[0]
        values.feedbackTimeEnd = values.feedbackTime[1]
      }
      if(values.assignTime){
        values.assignTimeBegin = values.assignTime[0]
        values.assignTimeEnd = values.assignTime[1]
      }
      delete values.startTime;
      delete values.confirmTime;
      delete values.assignTime;
      delete values.feedbackTime;

      await props.submit(values);
    }
    console.log(tableRef.value, "tableRef.value");
    // tableRef.value.setPagination({ page: 1 })
    params.value = unref(values);
    reload();
    tableRef.value.updatePage(1);

  }

  /**
   * 数据加载
   * 考虑要代码和多个项目 请求库和封装方式不同，抛出去
   */
  async function loadData(res: any) {
    let param = { ...res, condition: { ...params.value } };
    if (props.loadData && isFunction(props.loadData)) param = props.loadData(param);
    return request.post(`/${props.name}/page`, param);
  }
  /**
   * 重置
   */
  async function reset() {
    params.value = {};
    if (props.reset && isFunction(props.reset)) {
      await props.reset(params);
      // return;
    }
    // tableRef.value.setPagination({ page: 1 });
    reload();
    tableRef.value.updatePage(1);

  }
  /**
   * 窗口提交
   */
  async function modalSubmit() {
    const type = modalType.value;
    const row = tableRows.value;
    let formData = unref(modalFormRef).formModel;
    // 格式化处理数据
    if (isFunction(props.submitData)) formData = props.submitData(formData);

    if (props.submitFunc && isFunction(props.submitFunc)) {
      await props.submitFunc();
      return;
    }
    // 内置的一些行为(新增，编辑)
    try {
      if (type === 'add') await request.post(`/${props.name}`, { ...formData });
      else if (type === 'edit') await request.put(`/${props.name}/${row.id}`, { ...formData });
      message.success('执行成功');
      active.value = false;
      reload();
    } catch (error) {
      message.success('执行失败');
    }
  }

  return {
    reload,
    submit,
    loadData,
    reset,
    modalSubmit,
  };
}
