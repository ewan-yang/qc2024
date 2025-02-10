<template>
  <n-form ref="formRef" :model="model" :rules="rules" size="large" :show-label="false">
    <n-form-item path="code">
      <!-- 躲避浏览器自动填充 -->
      <input  style="width: 0; height: 0; border: 0; position: absolute" />
      <n-input v-model:value="model.code" placeholder="请输入用户名" />
    </n-form-item>
    <n-form-item path="oldPass">
      <input type="password" style="width: 0; height: 0; border: 0; position: absolute" />
      <n-input v-model:value="model.oldPass" type="password" show-password-on="click" placeholder="原密码" />
    </n-form-item>
    <n-form-item path="newPass">
      <n-input v-model:value="model.newPass" type="password" show-password-on="click" placeholder="新密码" />
    </n-form-item>
    <n-form-item path="newPassVerify">
      <n-input v-model:value="model.newPassVerify" type="password" show-password-on="click" placeholder="确认密码" />
    </n-form-item>
    <n-space :vertical="true" size="large">
      <n-button type="primary" size="large" :block="true" :round="true" @click="handleSubmit">确定</n-button>
      <n-button size="large" :block="true" :round="true" @click="toLoginModule('pwd-login')">返回</n-button>
    </n-space>
  </n-form>
</template>

<script lang="ts" setup>
import { reactive, ref, toRefs } from 'vue';
import type { FormInst, FormRules } from 'naive-ui';
import { useMessage } from 'naive-ui';
import { resetPassword,newResetPassword } from '@/service';
import { useRouterPush } from '@/composables';
import { useSmsCode } from '@/hooks';
import { formRules, getConfirmPwdRule } from '@/utils';
import { sha1 } from '~/src/utils/crypto';
const { toLoginModule } = useRouterPush();
const { toLogin } = useRouterPush(false);

const formRef = ref<HTMLElement & FormInst>();
const message = useMessage();

const model = reactive({
  code: '',
  oldPass: '',
  newPass: '',
  newPassVerify:''
});

const rules: FormRules = {
  newPass: formRules.passWo,
  oldPass: formRules.oldPassWo,
  newPassVerify: getConfirmPwdRule(toRefs(model).newPass)
};


async function handleSubmit() {
  await formRef.value?.validate();

  newResetPassword(model.code, sha1(model.oldPass), sha1(model.newPass)).then((res) => {
    console.log(res,'res')
    if(!res.error?.msg){
      // 跳转登录页
      toLoginModule('pwd-login');
      message.success('密码重置成功');
    }

  });
}
</script>

<style scoped></style>
