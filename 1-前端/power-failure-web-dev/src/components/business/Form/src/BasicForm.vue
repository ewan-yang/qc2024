<template>
  <n-form v-bind="getBindValue" ref="formElRef" :key="'form' + getSchema.length" :model="formModel">
    <n-grid v-bind="getGrid">
      <n-gi v-for="schema in getSchema" v-bind="schema.giProps" :key="schema.field">
        <n-form-item :label="schema.label" :path="schema.field">
          <!--标签名右侧温馨提示-->
          <template v-if="schema.labelMessage" #label>
            {{ schema.label }}
            <n-tooltip trigger="hover" :style="schema.labelMessageStyle">
              <template #trigger>
                <n-icon size="18" class="cursor-pointer text-gray-400">
                  <icon-ant-design:question-circle-outlined />
                </n-icon>
              </template>
              {{ schema.labelMessage }}
            </n-tooltip>
          </template>

          <!--判断插槽-->
          <template v-if="schema.slot">
            <slot :name="schema.slot" :model="formModel" :field="schema.field" :value="formModel[schema.field]"></slot>
          </template>

          <!--NCheckbox-->
          <template v-else-if="schema.component === 'NCheckbox'">
            <n-checkbox-group v-model:value="formModel[schema.field]" @change="$emit('field-change', formModel)">
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
            <n-radio-group v-model:value="formModel[schema.field]" @change="$emit('field-change', formModel)">
              <n-space>
                <n-radio v-for="item in schema.componentProps.options" :key="item.value" :value="item.value">
                  {{ item.label }}
                </n-radio>
              </n-space>
            </n-radio-group>
          </template>

          <template v-else-if="schema?.componentProps?.valueFormat">
            <component
              v-bind="getComponentProps(schema)"
              :is="schema.component"
              v-model:formatted-value="formModel[schema.field]"
              :class="{ isFull: schema.isFull != false && getProps.isFull }"
              @update:value="$emit('field-change', formModel)"
            />
          </template>
          <!--动态渲染表单组件-->
          <component
            v-bind="getComponentProps(schema)"
            :is="schema.component"
            v-else
            v-model:value="formModel[schema.field]"
            :class="{ isFull: schema.isFull != false && getProps.isFull }"
            @update:value="$emit('field-change', formModel)"
          />
          <!--组件后面的内容-->
          <template v-if="schema.suffix">
            <slot :name="schema.suffix" :model="formModel" :field="schema.field" :value="formModel[schema.field]">
            </slot>
          </template>
        </n-form-item>
      </n-gi>
      <!--提交 重置 展开 收起 按钮-->
      <n-gi
        v-if="getProps.showActionButtonGroup"
        :span="isInline ? 1 : 24"
        :suffix="isInline ? true : false"
        #="{ overflow }"
      >
        <n-space
          align="center"
          :justify="isInline ? 'end' : 'start'"
          :style="{ 'margin-left': `${isInline ? 12 : getProps.labelWidth}px` }"
        >
          <n-button
            v-if="getProps.showSubmitButton"
            v-bind="getSubmitBtnOptions"
            :loading="loadingSub"
            @click="handleSubmit"
            color="#BAD8FB"
            style="color: black;"
            >{{ getProps.submitButtonText ?? t('common.queryText') }}</n-button
          >
          <n-button  ghost v-if="getProps.showResetButton" v-bind="getResetBtnOptions" @click="resetFields">{{
            getProps.resetButtonText ?? t('common.resetText')
          }}</n-button>
          <n-button
            v-if="isInline && getProps.showAdvancedButton && getSchema.length > 4"
            type="primary"
            text
            icon-placement="right"
            @click="unfoldToggle"
          >
            <template #icon>
              <n-icon v-if="overflow" size="14" class="unfold-icon">
                <icon-ant-design:down-outlined />
              </n-icon>
              <n-icon v-else size="14" class="unfold-icon">
                <icon-ant-design:up-outlined />
              </n-icon>
            </template>
            {{ overflow ? t('common.spreadText') : t('common.foldText') }}
          </n-button>
        </n-space>
      </n-gi>
    </n-grid>
  </n-form>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, computed, unref, onMounted, watch } from 'vue';
import type { Ref } from 'vue';
import type { GridProps } from 'naive-ui/lib/grid';
import { merge } from 'lodash-es';
import { useI18n } from 'vue-i18n';
import { isArray } from '@/utils';
import { isUndefined } from '../../../../utils/common/typeof';
import { createPlaceholderMessage } from './helper';
import { useFormEvents } from './hooks/useFormEvents';
import { useFormValues } from './hooks/useFormValues';
import { basicProps } from './props';
import type { FormSchema, FormProps, FormActionType } from './types/form';

export default defineComponent({
  name: 'BasicForm',
  props: {
    ...basicProps
  },
  emits: ['reset', 'submit', 'register', 'field-change'],
  setup(props, { emit, attrs }) {
    const defaultFormModel = ref<Recordable>({});
    const formModel = reactive<Recordable>({});
    const propsRef = ref<Partial<FormProps>>({});
    const schemaRef = ref<Nullable<FormSchema[]>>(null);
    const formElRef = ref<Nullable<FormActionType>>(null);
    const gridCollapsed = ref(true);
    const loadingSub = ref(false);
    const isUpdateDefaultRef = ref(false);
    const { t } = useI18n();
    const getSubmitBtnOptions = computed(() => {
      return {
        size: props.size,
        type: 'primary',
        ...props.submitButtonOptions
      };
    });

    const getResetBtnOptions = computed(() => {
      return {
        size: props.size,
        type: 'primary',
        ...props.resetButtonOptions
      };
    });

    function getComponentProps(schema: any) {
      const compProps = schema.componentProps ?? {};
      const component = schema.component;
      return {
        clearable: true,
        placeholder: createPlaceholderMessage(unref(component)),
        ...compProps
      };
    }

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

    const isInline = computed(() => {
      const { layout } = unref(getProps);
      return layout === 'inline';
    });

    const getGrid = computed((): GridProps => {
      const { gridProps } = unref(getProps);
      return {
        ...gridProps,
        collapsed: isInline.value ? gridCollapsed.value : false,
        responsive: 'screen'
      };
    });

    const getBindValue = computed(() => ({ ...attrs, ...props, ...unref(getProps) } as Recordable));

    const getSchema = computed((): FormSchema[] => {
      const schemas: FormSchema[] = unref(schemaRef) || (unref(getProps).schemas as any);
      for (const schema of schemas) {
        const { defaultValue } = schema;
        // handle date type
        // dateItemType.includes(component as string)
        if (defaultValue) {
          schema.defaultValue = defaultValue;
        }
      }
      return schemas as FormSchema[];
    });

    const { handleFormValues, initDefault } = useFormValues({
      defaultFormModel,
      getSchema,
      formModel
    });

    const { handleSubmit, validate, resetFields, getFieldsValue, clearValidate, setFieldsValue } = useFormEvents({
      emit,
      getProps,
      formModel,
      getSchema,
      formElRef: formElRef as Ref<FormActionType>,
      defaultFormModel,
      loadingSub,
      handleFormValues
    });

    function unfoldToggle() {
      gridCollapsed.value = !gridCollapsed.value;
    }

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

    const formActionType: Partial<FormActionType> = {
      getFieldsValue,
      setFieldsValue,
      resetFields,
      validate,
      clearValidate,
      setProps,
      submit: handleSubmit
    };

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

    onMounted(() => {
      initDefault();
      emit('register', formActionType);
    });

    return {
      formElRef,
      formModel,
      getGrid,
      getProps,
      getBindValue,
      getSchema,
      getSubmitBtnOptions,
      getResetBtnOptions,
      loadingSub,
      isInline,
      t,
      handleSubmit,
      resetFields,
      setProps,
      getComponentProps,
      unfoldToggle,
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

.unfold-icon {
  display: flex;
  align-items: center;
  height: 100%;
  margin-left: -3px;
}
</style>
