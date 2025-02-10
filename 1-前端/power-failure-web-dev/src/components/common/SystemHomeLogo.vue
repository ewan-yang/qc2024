<template>
  <icon-local-login-icon />
</template>

<script lang="ts" setup>
defineOptions({ name: 'SystemHomeLogo' });

interface Props {
  /** logo是否填充 */
  fill?: boolean;
}

withDefaults(defineProps<Props>(), {
  fill: false
});
</script>

<style scoped></style>
