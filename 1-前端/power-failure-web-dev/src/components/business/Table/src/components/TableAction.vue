<template>
  <div class="tableAction">
    <div class="flex items-center justify-center">
      <template v-for="(action) in getActions" :key="`zz-${action.label}`">
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
          确定要{{ action.label }}此数据吗？
        </n-popconfirm>
        <n-button v-else v-bind="action"  class="mx-2">
          <!-- <template v-if="action.hasOwnProperty('icon')" #icon>
            <n-icon :component="action.icon" />
          </template> -->
          <template v-if="action.hasOwnProperty('iconType')" #icon>
            <n-icon v-if="action.iconType == 'cancel'">
              <icon-ant-design:close-circle-outlined/>
            </n-icon>
            <n-icon v-if="action.iconType == 'record'">
              <icon-ant-design:undo-outlined/>
            </n-icon>
            
            <n-icon v-if="action.iconType == 'print'">
              <icon-mingcute:print-line />
            </n-icon>
            <n-icon v-if="action.iconType == 'assign'">
              <svg xmlns="http://www.w3.org/2000/svg" width="1024" height="1024" viewBox="0 0 1024 1024"><g transform="rotate(90 512 512)"><path fill="currentColor" d="M521.7 82c-152.5-.4-286.7 78.5-363.4 197.7c-3.4 5.3.4 12.3 6.7 12.3h70.3c4.8 0 9.3-2.1 12.3-5.8c7-8.5 14.5-16.7 22.4-24.5c32.6-32.5 70.5-58.1 112.7-75.9c43.6-18.4 90-27.8 137.9-27.8c47.9 0 94.3 9.3 137.9 27.8c42.2 17.8 80.1 43.4 112.7 75.9c32.6 32.5 58.1 70.4 76 112.5C865.7 417.8 875 464.1 875 512c0 47.9-9.4 94.2-27.8 137.8c-17.8 42.1-43.4 80-76 112.5s-70.5 58.1-112.7 75.9A352.8 352.8 0 0 1 520.6 866c-47.9 0-94.3-9.4-137.9-27.8A353.84 353.84 0 0 1 270 762.3c-7.9-7.9-15.3-16.1-22.4-24.5c-3-3.7-7.6-5.8-12.3-5.8H165c-6.3 0-10.2 7-6.7 12.3C234.9 863.2 368.5 942 520.6 942c236.2 0 428-190.1 430.4-425.6C953.4 277.1 761.3 82.6 521.7 82zM395.02 624v-76h-314c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h314v-76c0-6.7 7.8-10.5 13-6.3l141.9 112a8 8 0 0 1 0 12.6l-141.9 112c-5.2 4.1-13 .4-13-6.3z"/></g></svg>
            </n-icon>
            <n-icon v-if="action.iconType == 'changeEngineersTeam'">
              <icon-ant-design:user-switch-outlined />
            </n-icon>
            <n-icon v-if="action.iconType == 'follow'">
              <icon-ant-design:profile-filled />
            </n-icon>
            
          </template>

          {{ action.label }}
        </n-button>
      </template>
      <n-dropdown
        v-if="dropDownActions && getDropdownList.length"
        trigger="hover"
        :options="getDropdownList"
        @select="select"
      >
        <slot name="more"></slot>
        <n-button v-if="!$slots.more" v-bind="getMoreProps" class="mx-2" icon-placement="right">
          <div class="flex items-center">
            <span>更多</span>
            <n-icon size="14" class="ml-1">
              <icon-ant-design:down-outlined />
            </n-icon>
          </div>
        </n-button>
      </n-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
import type { PropType } from 'vue';
import { defineComponent, computed, toRaw } from 'vue';
// import { usePermission } from '@/composables';
import { isBoolean, isFunction } from '@/utils';
import type { ActionItem } from '@/components/business/Table';
export default defineComponent({
  name: 'TableAction',
  props: {
    actions: {
      type: Array as PropType<ActionItem[]>,
      default: null,
      required: true
    },
    dropDownActions: {
      type: Array as PropType<ActionItem[]>,
      default: null
    },
    style: {
      type: String as PropType<string>,
      default: 'button'
    },
    select: {
      type: Function as PropType<Function>,
      default: () => {}
    }
  },
  setup(props) {
    // const { hasPermission } = usePermission();

    const actionType = props.style === 'button' ? 'default' : props.style === 'text' ? 'primary' : 'default';
    const actionText = props.style === 'button' ? undefined : props.style === 'text' ? true : undefined;

    const getMoreProps = computed(() => {
      return {
        text: actionText,
        type: actionType,
        size: 'small'
      };
    });

    const getDropdownList = computed(() => {
      return (
        (toRaw(props.dropDownActions) || [])
          // .filter(action => {
          //   return hasPermission("xxx:add") && isIfShow(action);
          // })
          .filter(action => {
            return isIfShow(action);
          })
          .map(action => {
            const { popConfirm } = action;
            return {
              size: 'small',
              text: actionText,
              type: actionType,
              ...action,
              ...popConfirm,
              onConfirm: popConfirm?.confirm,
              onCancel: popConfirm?.cancel
            };
          })
      );
    });

    function isIfShow(action: ActionItem): boolean {
      const ifShow = action.ifShow;

      let isIfShow = true;

      if (isBoolean(ifShow)) {
        isIfShow = ifShow;
      }
      if (isFunction(ifShow)) {
        isIfShow = ifShow(action);
      }
      return isIfShow;
    }

    const getActions = computed(() => {
      return (toRaw(props.actions) || [])
        .filter(action => {
          return isIfShow(action);
        })
        .map(action => {
          const { popConfirm } = action;
          // 需要展示什么风格，自己修改一下参数
          return {
            size: 'small',
            text: actionText,
            type: actionType,
            ...action,
            ...(popConfirm || {}),
            onConfirm: popConfirm?.confirm,
            onCancel: popConfirm?.cancel,
            enable: Boolean(popConfirm)
          };
        });
    });

    return {
      getActions,
      getDropdownList,
      getMoreProps
    };
  }
});
</script>
