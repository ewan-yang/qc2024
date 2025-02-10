<template>
  <vertical-mix-sider v-if="isVerticalMix" class="global-sider" />
  <vertical-sider v-else class="global-sider"  />
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useThemeStore } from '@/store';
import { VerticalMixSider, VerticalSider } from './components';

defineOptions({ name: 'GlobalSider' });

const theme = useThemeStore();

const isVerticalMix = computed(() => theme.layout.mode === 'vertical-mix');
</script>

<style scoped>
.global-sider {
  box-shadow: 2px 0 8px 0 rgba(3, 114, 225, 0.05);
  background-image:linear-gradient(180deg, rgba(8, 28, 58, .7) 0%,  rgba(66, 77, 98, .5) 100%), url('../../../assets/svg-icon/Background-image1.svg');
  background-size:cover;
  background-repeat: no-repeat;
  background-position: center;

}
</style>
