import { ref, onUnmounted, unref, nextTick, watch } from 'vue';
import { getDynamicProps } from '@/utils';
import type { FormProps, FormActionType, UseFormReturnType } from '../types/form';
import type { TypeUtil } from '~/src/typings/utils';

type Props = Partial<TypeUtil.DynamicProps<FormProps>>;

const isProdMode = import.meta.env.PROD;

export function useForm(props?: Props): UseFormReturnType {
  const formRef = ref<Nullable<FormActionType>>(null);
  const loadedRef = ref<Nullable<boolean>>(false);

  async function getForm() {
    const form = unref(formRef);
    if (!form) {
      console.error('尚未获得表单实例，请确保在执行表单操作时已呈现表单!');
    }
    await nextTick();
    return form as FormActionType;
  }

  function register(instance: FormActionType) {
    // eslint-disable-next-line no-unused-expressions
    isProdMode &&
      onUnmounted(() => {
        formRef.value = null;
        loadedRef.value = null;
      });
    if (unref(loadedRef) && isProdMode && instance === unref(formRef)) return;

    formRef.value = instance;
    loadedRef.value = true;

    watch(
      () => props,
      () => {
        // eslint-disable-next-line no-unused-expressions
        props && instance.setProps(getDynamicProps(props));
      },
      {
        immediate: true,
        deep: true
      }
    );
  }

  const methods: FormActionType = {
    setProps: async (formProps: Partial<FormProps>) => {
      const form = await getForm();
      await form.setProps(formProps);
    },

    resetFields: async () => {
      getForm().then(async form => {
        await form.resetFields();
      });
    },

    clearValidate: async (name?: string | string[]) => {
      const form = await getForm();
      await form.clearValidate(name);
    },

    getFieldsValue: <T>() => {
      return unref(formRef)?.getFieldsValue() as T;
    },

    setFieldsValue: async (values: any) => {
      const form = await getForm();
      await form.setFieldsValue(values);
    },

    submit: async (): Promise<any> => {
      const form = await getForm();
      return form.submit();
    },

    validate: async (nameList?: any[]): Promise<Recordable> => {
      const form = await getForm();
      return form.validate(nameList);
    }
  };

  return [register, methods];
}
