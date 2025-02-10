<template>
  <n-card class="mb-4">
    <BasicForm @register="register" @submit="handleSubmit" @reset="handleReset"> </BasicForm>
  </n-card>
  <n-card>
    <BasicTable
      ref="actionRef"
      :columns="columns"
      :request="loadDataTable"
      :row-key="row => row.id"
      :action-column="actionColumn"
      :scroll-x="columns.length * 150"
      @update:checked-row-keys="onCheckedRow"
    >
      <template #tableTitle>
        <n-button v-if="showAddButton" class="mr-4" type="primary" @click="open('add')">
          <template #icon>
            <n-icon>
              <icon-ant-design:plus-outlined />
            </n-icon>
          </template>
          {{ t('common.addText') }}
        </n-button>
        <n-button v-if="showBatchDeleteButton" type="error" @click="batchDelete">
          <template #icon>
            <n-icon size="18">
              <icon-ant-design:delete-outlined />
            </n-icon>
          </template>
          {{ t('common.batchRemoveText') }}
        </n-button>
        <slot name="customtableTitle"></slot>
      </template>
    </BasicTable>
    <BasicModal
      ref="modalRef"
      :show-submit="formType !== FORMTYPE.detail"
      :modal-type="modalType"
      :title="formType"
      @on-ok="handleModalSubmit"
      @on-close="handleModalClose"
    >
      <template #default>
        <BasicForm
          ref="modalFormRef"
          :show-action-button-group="false"
          @register="modalRegister"
          @submit="modalSubmit"
          @field-change="handleFieldChange"
        >
          <template v-for="(_item, key, index) in $slots" :key="index" #[key]="slotScope">
            <slot :name="key" v-bind="slotScope"></slot>
          </template>
        </BasicForm>
      </template>
    </BasicModal>
  </n-card>
</template>
<script lang="ts">
import { ref, defineComponent, reactive, h, unref, nextTick } from 'vue';
import { useMessage, useDialog } from 'naive-ui';
import { useI18n } from 'vue-i18n';
import { useAppStore } from '@/store';
import { isFunction, isBoolean } from '@/utils';
import type { FormSchema, BasicForm } from '@/components/business/Form';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { useForm } from '@/components/business/Form';
import type { ActionItem } from '@/components/business/Table';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { BasicTable, TableAction } from '@/components/business/Table';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import type { BasicModal } from '@/components/business/Modal';
import { pageAPI, addAPI, delAPI, statusAPI, batchDelAPI, editAPI } from '~/src/service';
import { isUndefined } from '../../../../utils/common/typeof';
import { createPlaceholderMessage } from '../../Form/src/helper';
import { basicProps } from './props';
import { FORMTYPE } from './const';
import { componentRulesType } from './utils';

export default defineComponent({
  props: {
    ...basicProps
  },
  emits: ['open-Detail', 'close-Detail'],
  setup(props, { emit }) {
    const app = useAppStore();
    const message = useMessage();
    const dialog = useDialog();
    const actionRef = ref();
    let search_schemas: FormSchema[] = [];
    let add_schemas: FormSchema[] = [];
    let edit_schemas: FormSchema[] = [];
    const modalRef = ref<InstanceType<typeof BasicModal> | null>(null);
    const modalFormRef = ref<InstanceType<typeof BasicForm> | null>(null);
    const formType = ref(FORMTYPE.add);
    const optData = ref<Recordable>({});
    const batchDeleteData = ref([]);
    const { t } = useI18n();
    init();
    // _____________________________________________________________

    const actionColumn = reactive({
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: getActionColumn({}).length * 80,
      render(record: any) {
        return h(TableAction as any, {
          style: 'button',
          actions: getActionColumn(record)
        });
      }
    });

    const [register] = useForm({
      gridProps: { cols: '1 s:1 m:2 l:3 xl:4 2xl:4' },
      labelWidth: 80,
      schemas: search_schemas
    });
    const [modalRegister] = useForm({
      gridProps: {
        cols: '2',
        xGap: '12'
      },
      layout: 'add',
      labelWidth: 80,
      labelPlacement: 'top',
      schemas: add_schemas
    });
    const params = ref({});
    /**
     * 初始化, 从表格中提取搜索框,新增框，编辑，详情
     */
    function init(data = {}) {
      search_schemas = [];
      add_schemas = [];
      edit_schemas = [];
      // eslint-disable-next-line array-callback-return
      props.columns.map(_item => {
        // 新增,编辑默认剔除状态
        if (_item.key !== 'status' && _item.title) {
          const obj: any = {
            field: `${_item.key}`,
            label: `${_item.title}`,
            ..._item
          };
          // 属性组件默认为NInput
          if (!obj.component) obj.component = 'NInput';
          if (_item.showSearch) {
            const searchObj = { ...obj };
            delete searchObj.rules;
            search_schemas.push(searchObj);
          }
          // 新增, 编辑 默认规则
          if (!obj.rules && !obj.slot && !obj.skip) {
            obj.rules = [
              {
                required: true,
                message: createPlaceholderMessage(obj.component) + obj.title,
                trigger: obj.component === 'NInput' ? ['blur', 'input'] : ['blur', 'change']
              }
            ];
            if (componentRulesType(obj)) {
              obj.rules[0].type = componentRulesType(obj);
            }
          }
          if (!isHide(_item.hideAdd, data)) add_schemas.push(obj);
          if (!isHide(_item.hideEdit, data)) edit_schemas.push(obj);
        }
      });
    }
    function reloadTable() {
      actionRef.value.reload();
    }
    /**
     * 数据加载
     * @param res 分页参数
     */
    async function loadDataTable(res: any) {
      return pageAPI({ ...res, condition: { ...params.value } }, `relay-task-service/api/v1/${props.name}`);
    }
    /**
     * 查询操作
     * @param values 查询参数
     */
    function handleSubmit(values: Recordable) {
      params.value = values;
      reloadTable();
    }
    /**
     * 重置
     */
    function handleReset() {
      params.value = {};
      reloadTable();
    }
    /**
     * 删除
     * @param record 列数据
     */
    async function handleDel(record: { id: any }) {
      await delAPI(`relay-task-service/api/v1/${props.name}`, record.id);
      reloadTable();
    }
    /**
     * 编辑
     * @param record 列数据
     */
    async function handleEdit(record: any) {
      // slot 里的ref https://vuejs.org/guide/essentials/template-refs.html#accessing-the-refs ，暂时的实现方法: 先渲染出来，才能获取dom
      open('edit');
      // 用途： 1.提交时获取id   (有更好的方法可以优化)
      optData.value = record;
      let new_record = record;
      // 格式化处理数据
      if (props.formatEdit) new_record = await props.formatEdit(record);
      init(new_record);
      editDefaultProps(new_record);
      nextTick(() => {
        modalFormRef.value?.setProps({ schemas: edit_schemas });
        modalFormRef.value?.setFieldsValue({ ...unref(optData) });
      });
    }
    /** 详情
     *  @param record 列数据
     */
    async function handleDetail(record) {
      open('detail');
      emit('open-Detail');
      optData.value = record;
      let new_record = record;
      // 格式化处理数据
      if (props.formatEdit) new_record = await props.formatEdit(record);
      init(new_record);
      editDefaultProps(new_record);
      const detail_schemas = edit_schemas;
      detail_schemas.map(_item => {
        if (!_item.componentProps) _item.componentProps = {};
        _item.componentProps.disabled = true;
        return _item;
      });
      nextTick(() => {
        modalFormRef.value?.setProps({ schemas: detail_schemas });
        modalFormRef.value?.setFieldsValue({ ...unref(optData) });
      });
    }
    /**
     * 状态切换
     * @param record 列数据
     */
    async function handleStatus(record: { [x: string]: any; id: any }) {
      // status指 变化后/变化前
      const result = await statusAPI(`relay-task-service/api/v1/${props.name}`, record.id, record.status === 1 ? 0 : 1);
      message.success(`${result.data?.message || '切换成功'}`);
      reloadTable();
    }
    /**
     * 生产操作列
     * @param record 列数据
     */
    function getActionColumn(record) {
      let TableActionColumn: ActionItem[] = [];
      if (props.showEditButton)
        TableActionColumn.push({ label: t('common.edit'), type: 'primary', onClick: handleEdit.bind(null, record) });
      if (props.showDetailButton)
        TableActionColumn.push({ label: t('common.detail'), type: 'info', onClick: handleDetail.bind(null, record) });
      if (props.showDelButton)
        TableActionColumn.push({
          label: t('common.delete'),
          type: 'error',
          confirmHandle: handleDel.bind(null, record),
          showConfirm: true
        });
      if (props.showStatusButton)
        TableActionColumn.push({
          label: record.status === 1 ? t('common.disable') : t('common.enable'),
          type: 'warning',
          confirmHandle: handleStatus.bind(null, record),
          showConfirm: true
        });
      if (props.customAction) TableActionColumn = [...TableActionColumn, ...props.customAction];
      // props 将列数据传出去
      TableActionColumn.map(_item => {
        if (_item.event) {
          _item.onClick = _item.event.bind(null, record);
        }
        return _item;
      });
      return TableActionColumn;
    }
    /**
     * 修改表单项默认属性
     * @param schemas 表单json
     * @param record  选择列
     */
    function editDefaultProps(record) {
      // eslint-disable-next-line array-callback-return, consistent-return
      edit_schemas.values = edit_schemas.map(_item => {
        // 树下拉赋值
        if (_item.component === 'NTreeSelect') return (_item.componentProps.defaultValue = record[_item.field]);
        // 对象属性赋值
        if (_item.op) {
          const opData = _item.op.split('.');
          optData.value[_item.field] = record[opData[0]][opData[1]];
        }
        return _item;
      });
    }
    /**
     * 窗口打开
     */
    function open(type) {
      formType.value = FORMTYPE[type];
      modalRef.value?.open();
      if (type === 'add') {
        nextTick(() => {
          init();
          modalFormRef.value?.setProps({ schemas: add_schemas });
        });
      }
    }
    function handleModalClose() {
      if (formType.value === FORMTYPE.detail) emit('close-Detail');
    }
    /**
     * 窗口提交前置事件
     */
    async function handleModalSubmit() {
      await modalFormRef.value?.handleSubmit();
      modalRef.value?.closeLoading();
    }
    /**
     * 窗口提交
     */
    async function modalSubmit() {
      let formData = { ...unref(modalFormRef.value)?.formModel };
      // 格式化处理数据
      if (isFunction(props.formatData)) formData = props.formatData(formData);
      if (formType.value === FORMTYPE.add) {
        await addAPI(formData, `relay-task-service/api/v1/${props.name}` || '');
      } else if (formType.value === FORMTYPE.edit) {
        await editAPI(formData, `relay-task-service/api/v1/${props.name}` || '', optData.value?.id);
      }
      modalRef.value?.closeModal();
      if (props.reload) {
        app.reloadPage();
      } else {
        reloadTable();
      }
    }
    /**
     * 批量删除
     */
    function batchDelete() {
      if (batchDeleteData.value.length > 0) {
        dialog.error({
          title: '警告',
          content: '你确定要删除这些数据吗？',
          positiveText: '确定',
          negativeText: '取消',
          onPositiveClick: () => {
            batchDel();
          }
        });
      } else {
        message.info('请最少选择一条数据');
      }
    }
    /**
     * 批量删除
     */
    async function batchDel() {
      const result = await batchDelAPI(`relay-task-service/api/v1/${props.name}`, batchDeleteData.value);
      reloadTable();
      message.info(`${result.data?.message}`);
    }
    /**
     * 选择行为
     */
    function onCheckedRow(rowKeys) {
      batchDeleteData.value = rowKeys;
    }
    function isHide(key, data): boolean {
      const ifShow = key;
      if (isUndefined(ifShow)) return false;
      let isIfShow = true;
      if (isBoolean(ifShow)) isIfShow = ifShow;
      if (isFunction(ifShow) && JSON.stringify(data) !== '{}') isIfShow = ifShow(data);
      return isIfShow;
    }
    /**
     * 用户表单联动
     */
    function handleFieldChange(formModel) {
      init(formModel);
      modalFormRef.value?.setProps({ schemas: formType.value === FORMTYPE.add ? add_schemas : edit_schemas });
    }
    return {
      formType,
      actionColumn,
      actionRef,
      modalRef,
      modalFormRef,
      batchDeleteData,
      FORMTYPE,
      register,
      handleSubmit,
      handleReset,
      loadDataTable,
      modalRegister,
      open,
      handleModalClose,
      modalSubmit,
      handleModalSubmit,
      batchDelete,
      onCheckedRow,
      t,
      handleFieldChange
    };
  }
});
</script>
<style scoped></style>
