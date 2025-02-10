<template>
  <n-grid cols="24" style="height: 100%;">
    <n-gi span='14' class="bg-img1">
      <div></div>
    </n-gi>
    <n-gi span="10" class="bg-img2">


      <div class="relative flex-center wh-full" >
        <!-- <dark-mode-switch :dark="theme.darkMode" class="absolute left-48px top-24px z-3 text-20px"
      @update:dark="theme.setDarkMode" /> -->
        <n-card :bordered="false" size="large" class="z-4 !w-auto " style="background-color: transparent;">
          <div class="w-300px sm:w-360px">
            <header class="flex-y-center justify-center">
              <div style="display: flex;width: Hug (214px);height: Hug (68px);top: 36px;left: 36px;padding: 16px, 0px, 16px, 0px;gap: 12px;margin-bottom: 4em;">
                <system-logo class="text-70px text-primary" style="width: 40px;height: 40px;" />
                <span
                  style="font-family: 'Alibaba PuHuiTi' 3.0;font-size: 25px;font-weight: 600;line-height: 40px;letter-spacing: 0.06em;text-align: center;">停电项目管理系统</span>
              </div>
            </header>
            <main class="pt-24px">
              <div class="pt-2">
                <transition name="fade-slide" mode="out-in" appear>
                  <component :is="activeModule.component" />
                </transition>
              </div>
            </main>
          </div>
        </n-card>
        <!-- <login-bg :theme-color="bgThemeColor" /> -->
      </div>
    </n-gi>

  </n-grid>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { Component } from 'vue';
import { EnumLoginModule } from '@/enum';
import { useThemeStore } from '@/store';
import { useAppInfo } from '@/composables';
import { getColorPalette, mixColor } from '@/utils';
import { BindWechat, CodeLogin, LoginBg, PwdLogin, Register, ResetPwd } from './components';

interface Props {
  /** 登录模块分类 */
  module: EnumType.LoginModuleKey;
}

const props = defineProps<Props>();

const theme = useThemeStore();
const { title } = useAppInfo();

interface LoginModule {
  key: EnumType.LoginModuleKey;
  label: EnumLoginModule;
  component: Component;
}

const modules: LoginModule[] = [
  { key: 'pwd-login', label: EnumLoginModule['pwd-login'], component: PwdLogin },
  { key: 'code-login', label: EnumLoginModule['code-login'], component: CodeLogin },
  { key: 'register', label: EnumLoginModule.register, component: Register },
  { key: 'reset-pwd', label: EnumLoginModule['reset-pwd'], component: ResetPwd },
  { key: 'bind-wechat', label: EnumLoginModule['bind-wechat'], component: BindWechat }
];

const activeModule = computed(() => {
  const active: LoginModule = { ...modules[0] };
  const findItem = modules.find(item => item.key === props.module);
  if (findItem) {
    Object.assign(active, findItem);
  }
  return active;
});

const bgThemeColor = computed(() => (theme.darkMode ? getColorPalette(theme.themeColor, 7) : theme.themeColor));

const bgColor = computed(() => {
  const COLOR_WHITE = '#ffffff';
  const ratio = theme.darkMode ? 0.5 : 0.2;
  return mixColor(COLOR_WHITE, theme.themeColor, ratio);
});
</script>

<style scoped>
.bg-img1 {
  background-image: url('../../../assets/svg-icon/login.svg');
  background-size: cover;
  background-repeat: no-repeat;
}
.bg-img2 {
  background-image: url('../../../assets/svg-icon/login-bg.svg');
  background-size: cover;
  background-repeat: no-repeat;
}
</style>
