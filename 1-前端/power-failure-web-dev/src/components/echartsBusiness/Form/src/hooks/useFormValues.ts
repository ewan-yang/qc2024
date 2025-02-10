import { unref } from 'vue';
import type { Ref, ComputedRef } from 'vue';
import { set } from 'lodash-es';
import { isArray, isFunction, isObject, isString, isNullOrUnDef } from '../../../../utils';
import type { FormSchema } from '../types/form';

interface UseFormValuesContext {
  defaultFormModel: Ref<any>;
  getSchema: ComputedRef<FormSchema[]>;
  formModel: Recordable;
}
export function emituseFormValues({ defaultFormModel, getSchema, formModel }: UseFormValuesContext): {
  /**
   * 用于重置初始化model
   * @param values 数据集
   * @returns 处理后的数据集
   */
  handleFormValues: (values: Recordable) => Recordable<any>;
  /**
   * 初始化默认值，从getSchema容器里面取defaultValue
   */
  initDefault: () => void;
} {
  // 加工 form values

  function handleFormValues(values: Recordable) {
    if (!isObject(values)) {
      return {};
    }
    const res: Recordable = {};
    for (const item of Object.entries(values)) {
      let [, value] = item;
      const [key] = item;
      if (!key || (isArray(value) && value.length === 0) || isFunction(value) || isNullOrUnDef(value)) {
        // eslint-disable-next-line no-continue
        continue;
      }
      // 删除空格
      if (isString(value)) {
        value = value.trim();
      }
      set(res, key, value);
    }
    return res;
  }

  // 初始化默认值
  function initDefault() {
    const schemas = unref(getSchema);
    const obj: Recordable = {};
    schemas.forEach(item => {
      const { defaultValue } = item;
      if (!isNullOrUnDef(defaultValue)) {
        obj[item.field] = defaultValue;
        formModel[item.field] = defaultValue;
      }
    });
    defaultFormModel.value = obj;
  }

  return { handleFormValues, initDefault };
}
