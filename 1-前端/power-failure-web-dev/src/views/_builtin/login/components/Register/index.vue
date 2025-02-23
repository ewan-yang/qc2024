<template>
  <n-form ref="formRef" :model="model" :rules="rules" size="large" :show-label="false">
    <n-form-item path="phone">
      <!-- 躲避浏览器自动填充 -->
      <input type="password" style="width: 0; height: 0; border: 0; position: absolute" />
      <n-input v-model:value="model.phone" placeholder="手机号码" />
    </n-form-item>
    <n-form-item path="code">
      <div class="flex-y-center w-full">
        <input type="password" style="width: 0; height: 0; border: 0; position: absolute" />
        <n-input v-model:value="model.code" placeholder="验证码" />
        <div class="w-18px"></div>
        <n-button size="large" :disabled="isCounting" :loading="smsLoading" @click="handleSmsCode">
          {{ label }}
        </n-button>
      </div>
    </n-form-item>
    <n-form-item path="pwd">
      <input type="password" style="width: 0; height: 0; border: 0; position: absolute" />
      <n-input v-model:value="model.pwd" type="password" show-password-on="click" placeholder="密码" />
    </n-form-item>
    <n-form-item path="confirmPwd">
      <n-input v-model:value="model.confirmPwd" type="password" show-password-on="click" placeholder="确认密码" />
    </n-form-item>
    <n-space :vertical="true" :size="18">
      <login-agreement v-model:value="agreement" />
      <n-button type="primary" size="large" :block="true" :round="true" @click="handleSubmit">确定</n-button>
      <n-button size="large" :block="true" :round="true" @click="toLoginModule('pwd-login')">返回</n-button>
    </n-space>
  </n-form>
</template>

<script lang="ts" setup>
import { reactive, ref, toRefs } from 'vue';
import { useMessage } from 'naive-ui';
import type { FormInst, FormRules } from 'naive-ui';
import { register } from '@/service';
import { useRouterPush } from '@/composables';
import { useSmsCode } from '@/hooks';
import { formRules, getConfirmPwdRule } from '@/utils';

const { toLoginModule } = useRouterPush();
const { toLogin } = useRouterPush(false);
const { label, isCounting, loading: smsLoading, start, getSmsCode } = useSmsCode();

const formRef = ref<HTMLElement & FormInst>();
const message = useMessage();

const model = reactive({
  phone: '',
  code: '',
  pwd: '',
  confirmPwd: ''
});

const rules: FormRules = {
  phone: formRules.phone,
  code: formRules.code,
  pwd: formRules.pwd,
  confirmPwd: getConfirmPwdRule(toRefs(model).pwd)
};

const agreement = ref(false);

function handleSmsCode() {
  start();
  getSmsCode(model.phone, '1');
}

async function handleSubmit() {
  await formRef.value?.validate();
  // 注册接口
  register(model.pwd, model.phone, model.code).then(() => {
    message.success('注册成功');
    // 跳转登录
    toLogin();
  });
}
</script>

<style scoped></style>
