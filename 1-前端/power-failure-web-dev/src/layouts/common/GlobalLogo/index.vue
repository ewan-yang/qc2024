<template>
  <router-link :to="routeHomePath" class="flex-center w-full nowrap-hidden">
    <system-logo class="text-32px text-primary" :fill="true"/>
    <h2 v-show="showTitle" class="pl-8px text-16px font-bold text-primary transition duration-300 ease-in-out " style="color: white;">
      {{ title }}
    </h2>
  </router-link>
</template>

<script setup lang="ts">
import { routePath } from '@/router';
import { useAppInfo } from '@/composables';

defineOptions({ name: 'GlobalLogo' });

interface Props {
  /** 显示名字 */
  showTitle: boolean;
}

defineProps<Props>();

const routeHomePath = routePath('root');

const { title } = useAppInfo();
</script>

<style scoped></style>
