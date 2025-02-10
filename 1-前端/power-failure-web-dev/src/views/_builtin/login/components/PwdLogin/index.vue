<template>
  <n-form ref="formRef" :model="model" :rules="rules" size="large" :show-label="false">
    <n-form-item path="userName">
      <n-input v-model:value="model.userName" placeholder="请输入用户名" />
    </n-form-item>
    <n-form-item path="password">
      <n-input v-model:value="model.password" type="password" show-password-on="click" placeholder="请输入密码" />
    </n-form-item>
    <!-- <n-form-item path="code">
      <div class="flex-y-center w-full" style="justify-content: space-between;">
        <n-input v-model:value="model.verifyCode" placeholder="验证码"  style="margin-right: 40px;"/>
        <div class="w-140px">
          <n-image class="mt-2" preview-disabled style="transform: scale(1.16);margin-left: -5px;" :src="code" @click="getCode()" />
        </div>
      </div>
    </n-form-item> -->
    <n-space :vertical="true" :size="24">
      <!-- <div class="flex-y-center justify-between">
        <n-checkbox v-model:checked="rememberMe" name="rememberMe">记住我</n-checkbox>
        <n-button :text="true" @click="toLoginModule('reset-pwd')">忘记密码？</n-button>
      </div> -->
      <n-button
        type="primary"
        size="large"
        :block="true"
        :loading="auth.loginLoading"
        @click="handleSubmit"
      >
        登录
      </n-button>
      <div class="flex justify-center">
        <n-button :text="true" @click="toLoginModule('reset-pwd')">重置密码</n-button>
      </div>
      <!-- <div class="flex-y-center justify-between">
        <n-button class="flex-1" :block="true" @click="toLoginModule('code-login')">
          {{ EnumLoginModule['code-login'] }}
        </n-button>
        <div class="w-12px"></div>
        <n-button class="flex-1" :block="true" @click="toLoginModule('register')">
          {{ EnumLoginModule.register }}
        </n-button>
      </div> -->
    </n-space>
  </n-form>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInst, FormRules } from 'naive-ui';
import { EnumLoginModule } from '@/enum';
import { fetchCode } from '@/service';
import { useAuthStore } from '@/store';
import { useRouterPush } from '@/composables';
import { sha1 } from '~/src/utils/crypto';
const auth = useAuthStore();
const { login } = useAuthStore();
const { toLoginModule } = useRouterPush();
const formRef = ref<HTMLElement & FormInst>();
const code = ref(fetchCode());
const model = reactive({
  userName: '',
  password: '',
  // verifyCode: ''
});
console.log('release20240929_1')


const rules: FormRules = {};

const rememberMe = ref(false);

async function handleSubmit() {
  await formRef.value?.validate();

  // const {  verifyCode } = model;

  const { error } = await login(model.userName, sha1(model.password),);
  error?.code === 4007 && getCode();
}

function getCode() {
  code.value = fetchCode();
}
</script>

<style scoped></style>
