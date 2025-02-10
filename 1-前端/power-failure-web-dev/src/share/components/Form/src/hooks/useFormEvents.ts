import type { ComputedRef, Ref } from 'vue';
import { unref, toRaw } from 'vue';
import type { FormProps, FormSchema, FormActionType } from '../types/form';
import { isFunction } from '../../../../utils';

declare type EmitType = (event: 'reset' | 'submit' | 'register', ...args: any[]) => void;

interface UseFormActionContext {
  /**
   * 事件
   */
  emit: EmitType;
  /**
   * 组件属性ref容器
   */
  getProps: ComputedRef<FormProps>;
  /**
   * 表单schema容器
   */
  getSchema: ComputedRef<FormSchema[]>;
  /**
   * 表单数据源
   */
  formModel: Recordable;
  /**
   * 表单ref容器
   */
  formElRef: Ref<FormActionType>;
  /**
   * 重置,手动设置默认值
   */
  defaultFormModel: Recordable;
  /**
   * 重置,处理数据清空, 用户表单数据方面的hooks传入方法，此hooks只用于事件行为
   */
  handleFormValues: Fn;
}
/**
 * 表单事件
 * @param emit 事件
 * @param getProps  组件属性ref容器
 * @param formModel  表单数据源
 * @param getSchema  表单schema容器
 * @param formElRef  表单ref容器
 * @param defaultFormModel  重置默认值
 * @param handleFormValues   重置,处理数据清空, 用户表单数据方面的hooks传入方法，此hooks只用于事件行为

 */
export function useFormEvents({
  emit,
  getProps,
  formModel,
  getSchema,
  formElRef,
  defaultFormModel,
  handleFormValues
}: UseFormActionContext): {
  /**
   * 提交 调用getProps中的提交接口，验证
   * @param e 默认行为
   * @returns 抛出submit事件
   */
  handleSubmit: (e?: Event) => Promise<void>;
  /**
   * 验证
   * @returns 返回验证结果
   */
  validate: () => Promise<any>;
  /**
   * 重置
   */
  resetFields: () => Promise<void>;
  /**
   * 获取表单值
   */
  getFieldsValue: () => Recordable;
  /**
   * 清空校验
   */
  clearValidate: () => Promise<void>;
  /**
   * 设置表单字段值
   */
  setFieldsValue: (values: Recordable) => Promise<void>;
} {
  async function validate() {
    return unref(formElRef)?.validate();
  }

  async function handleSubmit(e?: Event): Promise<void> {
    // eslint-disable-next-line no-unused-expressions
    e && e.preventDefault();
    const { submitFunc } = unref(getProps);
    if (submitFunc && isFunction(submitFunc)) {
      await submitFunc();
      return;
    }
    const formEl = unref(formElRef);
    if (!formEl) return;
    try {
      await validate();
      emit('submit', handleFormValues(toRaw(unref(formModel))));
    } catch (error) {}
  }

  // 清空校验
  async function clearValidate() {
    await unref(formElRef)?.restoreValidation();
  }

  // 重置
  async function resetFields(): Promise<void> {
    const { resetFunc, submitOnReset } = unref(getProps);
    // eslint-disable-next-line no-unused-expressions
    resetFunc && isFunction(resetFunc) && (await resetFunc());

    const formEl = unref(formElRef);
    if (!formEl) return;
    Object.keys(formModel).forEach(key => {
      formModel[key] = unref(defaultFormModel)[key] || null;
    });
    await clearValidate();
    const fromValues = handleFormValues(toRaw(unref(formModel)));
    emit('reset', fromValues);
    // eslint-disable-next-line no-unused-expressions
    submitOnReset && (await handleSubmit());
  }

  // 获取表单值
  function getFieldsValue(): Recordable {
    const formEl = unref(formElRef);
    if (!formEl) return {};
    return handleFormValues(toRaw(unref(formModel)));
  }

  // 设置表单字段值
  async function setFieldsValue(values: Recordable): Promise<void> {
    const fields = unref(getSchema)
      .map(item => item.field)
      .filter(Boolean);

    Object.keys(values).forEach(key => {
      const value = values[key];
      if (fields.includes(key)) {
        formModel[key] = value;
      }
    });
  }

  return {
    handleSubmit,
    validate,
    resetFields,
    getFieldsValue,
    clearValidate,
    setFieldsValue
  };
}
