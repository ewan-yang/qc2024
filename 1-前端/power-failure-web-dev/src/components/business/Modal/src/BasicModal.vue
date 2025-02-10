<template>
  <!-- dialog -->
  <component :is="NModal" :show-icon="true" v-if="props.modalType === 'm'" v-model:show="showModal" v-bind="getBindValue">
    <template #header>
      <div id="basic-modal-bar" class="w-full cursor-move">{{ getBindValue.title }}</div>
    </template>
    <template #icon>
      
    </template>
    <template #default>
      <slot name="default"></slot>
    </template>

    <template v-if="!$slots.action" #action>
      <n-space>
        <n-button v-if="showSubmit" type="primary" :loading="subLoading" @click="handleSubmit">{{
          subBtuText
        }}</n-button>
        <slot name="footerSlot"></slot>
        <n-button @click="closeModal">取消</n-button>
      </n-space>
    </template>
    <template v-else #action>
      <slot name="action"></slot>
    </template>
  </component>

  <!-- drawer -->
  <n-drawer v-if="props.modalType === 'd'" v-model:show="showModal" class="w-80vw! p-20px">
    <n-drawer-content>
      <template #header>
        <div id="basic-modal-bar" class="w-full cursor-move">{{ getBindValue.title }}</div>
      </template>
      <template #default>
        <slot name="default"></slot>
      </template>
      <template #footer>
        <n-space v-if="!$slots.action">
          <n-button @click="closeModal">取消</n-button>
          <n-button v-if="showSubmit" type="primary" :loading="subLoading" @click="handleSubmit">{{
            subBtuText
          }}</n-button>
        </n-space>
      </template>

    </n-drawer-content>
  </n-drawer>
</template>
<script lang="ts" setup name="BasicModal">
import { ref, unref, computed, useAttrs } from 'vue';
import { NModal, NDrawer } from 'naive-ui';
import { basicProps } from './props';

const attrs = useAttrs();
const props = defineProps({ ...basicProps });
const emit = defineEmits(['on-close', 'on-ok']);
const showModal = ref(false);
const subLoading = ref(false);

const getBindValue = computed(() => {
  const bindValue: { [key: string]: any } = {
    ...attrs,
    ...unref(props)
  };
  if (props.modalType === 'm') {
    bindValue.preset = 'dialog';
    bindValue.showIcon = false;
  }
  return bindValue;
});

function handleSubmit() {
  subLoading.value = true;
  emit('on-ok');
}
function closeModal() {
  showModal.value = false;
  subLoading.value = false;
  emit('on-close');
}
function closeLoading() {
  subLoading.value = false;
}

function open() {
  showModal.value = true;
}
defineExpose({
  open,
  closeModal,
  closeLoading
});
</script>
