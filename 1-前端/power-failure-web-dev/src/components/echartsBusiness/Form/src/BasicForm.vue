<template>
  <n-form ref="formElRef" :model="formModel" v-bind="getBindValue">
    <n-grid v-bind="getGrid" :cols="5"
        >
      <n-gi v-for="schema in getSchema" v-bind="schema.giProps" :key="schema.field">
        <n-form-item :label="schema.label" :path="schema.field">
          <!--判断插槽-->
          <slot
            v-if="schema.slot"
            :name="schema.slot"
            :model="formModel"
            :field="schema.field"
            :value="formModel[schema.field]"
          ></slot>
          <!--NCheckbox-->
          <template v-else-if="schema.component === 'NCheckbox'">
            <n-checkbox-group v-model:value="formModel[schema.field]">
              <n-space>
                <n-checkbox
                  v-for="item in schema.componentProps.options"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                />
              </n-space>
            </n-checkbox-group>
          </template>

          <!--NRadioGroup-->
          <template v-else-if="schema.component === 'NRadioGroup'">
            <n-radio-group v-model:value="formModel[schema.field]">
              <n-space>
                <n-radio v-for="item in schema.componentProps.options" :key="item.value" :value="item.value">
                  {{ item.label }}
                </n-radio>
              </n-space>
            </n-radio-group>
          </template>
          <!--特殊处理-->
          <template v-else-if="schema?.componentProps?.valueFormat">
            <component
              v-bind="getComponentProps(schema)"
              :is="schema.component"
              v-model:formatted-value="formModel[schema.field]"
              class="isFull"
            />
          </template>

          <!--动态渲染表单组件-->
          <component
            v-bind="getComponentProps(schema)"
            :is="schema.component"
            v-else
            v-model:value="formModel[schema.field]"
            class="isFull"
          />
        </n-form-item>
      </n-gi>
      <!--提交 重置  按钮-->
      <n-gi v-if="getProps.showActionButtonGroup"  suffix class="p-t-5">
        <n-space align="center" justify="end" :style="{ 'margin-left': `${getProps.labelWidth}px`,'margin-top': '5px' }">
          <n-button color="#BAD8FB" style="color:black" v-if="getProps.showSubmitButton" v-bind="getProps.submitButtonOptions" @click="handleSubmit">
            {{ getProps.submitButtonText }}
          </n-button>
          <n-button color="#044FE1" ghost v-if="getProps.showResetButton" v-bind="getProps.resetButtonOptions" @click="resetFields">
            {{ getProps.resetButtonText }}
          </n-button>
        </n-space>
      </n-gi>
    </n-grid>
  </n-form>
</template>

<script lang="ts">
// 类型
import type { Ref } from 'vue';
// 库
import { defineComponent, reactive, ref, computed, unref, onMounted, watch } from 'vue';
import { NForm, NGrid, NButton, NSpace, NInput, NSelect, NDatePicker, NGi, NFormItem } from 'naive-ui';
import type { GridProps } from 'naive-ui/lib/grid';
import { merge } from 'lodash-es';
import { isArray, isUndefined } from '../../../utils';
import type { FormActionType, FormProps, FormSchema } from './types/form';
// hooks
import { emituseFormValues as useFormValues } from './hooks/useFormValues';
import { useFormEvents } from './hooks/useFormEvents';
// 函数库
import { createPlaceholderMessage } from './helper';
import { basicProps } from './props';
/**
 *高级表单
 * 公共组件严谨禁止改动，影响多个项目,公共需求联系陈前，业务需求copy二次开发
 * 一般来说，组件要遵循单一职责，只做一件事
 * 用于封装表单的各种形态（查询表单，正常表单，弹窗表单，抽屉表单）,动态布局 , 以及子项
 * hooks概念，容器的数据不同，但是它的行为相同，封装hooks,传入不同容器ref,获取相应的行为能力
 */
export default defineComponent({
  name: 'BasicForm',
  components: {
    NForm,
    NGrid,
    NButton,
    NSpace,
    NGi,
    NFormItem,
    NInput,
    NSelect,
    NDatePicker
  },
  props: {
    ...basicProps
  },
  emits: ['reset', 'submit', 'register'],
  setup(props, { emit, attrs }) {
    //  ---------------------------------容器：封装一个对象的数据以及行为 ---------------------------------
    // 表单值对象
    const formModel = reactive<Recordable>({});
    // 表单ref容器
    const formElRef = ref<Nullable<FormActionType>>(null);
    // 组件属性ref容器
    const propsRef = ref<Partial<FormProps>>({});
    // 表单schema容器
    const schemaRef = ref<Nullable<FormSchema[]>>(null);
    // 表单默认对象
    const defaultFormModel = ref<Recordable>({});
    // 当schema变更时, 变量控制刷新
    const isUpdateDefaultRef = ref(false);
    // ---------------------------------针对组件传入的属性数据解构加工,派发对于的组件----------------------------
    /**
     * 获取表单组件属性
     */
    const getProps = computed((): FormProps => {
      const formProps = { ...props, ...unref(propsRef) } as FormProps;
      const rulesObj: any = {
        rules: {}
      };
      const schemas: any = formProps.schemas || [];
      schemas.forEach((item: { rules: any; field: string | number }) => {
        if (item.rules && isArray(item.rules)) {
          rulesObj.rules[item.field] = item.rules;
        }
      });
      return { ...formProps, ...unref(rulesObj) };
    });
    /**
     * 获取表单属性
     */
    const getBindValue = computed(() => ({ ...attrs, ...props, ...unref(getProps) } as Recordable));
    /**
     * 获取布局属性
     */
    const getGrid = computed((): GridProps => {
      const { gridProps } = unref(getProps);
      return {
        ...gridProps,
        responsive: 'screen'
      };
    });
    /**
     * 获取表单Schema
     */
    const getSchema = computed((): FormSchema[] => {
      const schemas: FormSchema[] = unref(schemaRef) || (unref(getProps).schemas as any);
      for (const schema of schemas) {
        const { defaultValue } = schema;
        if (defaultValue) {
          schema.defaultValue = defaultValue;
        }
      }
      return schemas as FormSchema[];
    });
    /**
     * 表单项-组件 属性初始化
     * clearable [true]
     * placeholder [FN]
     */
    function getComponentProps(schema: any) {
      const compProps = schema.componentProps ?? {};
      const component = schema.component;
      return {
        clearable: true,
        placeholder: createPlaceholderMessage(unref(component)),
        ...compProps
      };
    }
    // -----------------------------------注册事件，挂载-------------------------------------------------------------------
    const { handleFormValues, initDefault } = useFormValues({
      defaultFormModel,
      getSchema,
      formModel
    });

    const { handleSubmit, resetFields, setFieldsValue } = useFormEvents({
      emit,
      getProps,
      formModel,
      getSchema,
      formElRef: formElRef as Ref<FormActionType>,
      defaultFormModel,
      handleFormValues
    });

    onMounted(() => {
      initDefault();
    });
    watch(
      () => getSchema.value,
      schema => {
        if (unref(isUpdateDefaultRef)) {
          return;
        }
        if (schema?.length) {
          initDefault();
          isUpdateDefaultRef.value = true;
        }
      }
    );
    // ------------------------------------------------------------------------------------------
    async function setProps(formProps: Partial<FormProps>): Promise<void> {
      const propsRefs = merge(unref(propsRef) || {}, formProps);
      propsRefs.schemas = formProps.schemas;
      propsRef.value = propsRefs;
      // eslint-disable-next-line array-callback-return
      Object.keys(formModel).map(key => {
        const obj = formProps.schemas?.find(t1 => t1.field === key);
        if (isUndefined(obj)) {
          formModel[key] = '';
        }
      });
    }

    return {
      formModel,
      formElRef,
      getBindValue,
      getGrid,
      getSchema,
      getProps,
      getComponentProps,
      handleSubmit,
      resetFields,
      setProps,
      setFieldsValue
    };
  }
});
</script>
<style lang="scss" scoped>
.isFull {
  width: 100%;
  justify-content: flex-start;
}
</style>
