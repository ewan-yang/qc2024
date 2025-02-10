<template>
  <div class="tableAction">
    <div class="flex items-center justify-center">
      <template v-for="action in getActions" :key="`z-${action.label}`">
        <n-popconfirm
          v-if="action.showConfirm"
          @positive-click="
            () => {
              if (action.confirmHandle) action.confirmHandle();
            }
          "
        >
          <template #trigger>
            <n-button v-bind="action" class="mx-2">
              {{ action.label }}
              <template v-if="action.hasOwnProperty('icon')" #icon>
                <n-icon :component="action.icon" />
              </template>
            </n-button>
          </template>
          {{ action.popconfirmText || '请确认此操作?' }}
        </n-popconfirm>
        <n-button v-else class="mx-2" v-bind="action" @click="action.onClick">
          {{ action.label }}
          <template v-if="action.hasOwnProperty('icon')" #icon>
            <n-icon :component="action.icon" />
          </template>
        </n-button>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
import type { PropType } from 'vue';
import { defineComponent, toRaw, computed } from 'vue';
import { NButton, NIcon, NPopconfirm } from 'naive-ui';
import type { ActionItem } from '../../../Table';
import { isBoolean, isFunction } from '../../../../utils';

export default defineComponent({
  name: 'TableAction',
  components: { NButton, NIcon, NPopconfirm },
  props: {
    actions: {
      type: Array as PropType<ActionItem[]>,
      default: null,
      required: true
    }
  },
  setup(props) {
    function isIfShow(action: any): boolean {
      const ifShow = action.ifShow;

      let isIfShowC = true;

      if (isBoolean(ifShow)) {
        isIfShowC = ifShow;
      }
      if (isFunction(ifShow)) {
        isIfShowC = ifShow(action);
      }
      return isIfShowC;
    }
    const getActions = computed(() => {
      return (toRaw(props.actions) || []).filter(action => {
        return isIfShow(action);
      });
    });
    return {
      getActions
    };
  }
});
</script>
<style scoped>
.mx-2 {
  margin-left: 0.5rem;
  margin-right: 0.5rem;
}
.flex {
  display: flex;
}
.items-center {
  align-items: center;
}
.justify-center {
  justify-content: center;
}
</style>
