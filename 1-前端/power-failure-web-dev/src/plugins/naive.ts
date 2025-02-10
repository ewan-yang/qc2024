import type { App } from 'vue';
import * as NaiveUI from 'naive-ui';

// NaiveUI 全局方法注册
const { message, dialog, notification, loadingBar } = NaiveUI.createDiscreteApi([
  'message',
  'dialog',
  'notification',
  'loadingBar'
]);
window.$message = message;
window.$dialog = dialog;
window.$notification = notification;
window.$loading = loadingBar;

const naive = NaiveUI.create({
  components: [
    NaiveUI.NMessageProvider,
    NaiveUI.NDialogProvider,
    NaiveUI.NConfigProvider,
    NaiveUI.NInput,
    NaiveUI.NButton,
    NaiveUI.NForm,
    NaiveUI.NFormItem,
    NaiveUI.NCheckboxGroup,
    NaiveUI.NCheckbox,
    NaiveUI.NIcon,
    NaiveUI.NLayout,
    NaiveUI.NLayoutHeader,
    NaiveUI.NLayoutContent,
    NaiveUI.NLayoutFooter,
    NaiveUI.NLayoutSider,
    NaiveUI.NMenu,
    NaiveUI.NBreadcrumb,
    NaiveUI.NBreadcrumbItem,
    NaiveUI.NDropdown,
    NaiveUI.NSpace,
    NaiveUI.NTooltip,
    NaiveUI.NAvatar,
    NaiveUI.NTabs,
    NaiveUI.NTabPane,
    NaiveUI.NCard,
    NaiveUI.NRow,
    NaiveUI.NCol,
    NaiveUI.NDrawer,
    NaiveUI.NDrawerContent,
    NaiveUI.NDivider,
    NaiveUI.NSwitch,
    NaiveUI.NBadge,
    NaiveUI.NAlert,
    NaiveUI.NElement,
    NaiveUI.NTag,
    NaiveUI.NNotificationProvider,
    NaiveUI.NProgress,
    NaiveUI.NPopselect,
    NaiveUI.NDatePicker,
    NaiveUI.NGrid,
    NaiveUI.NGridItem,
    NaiveUI.NList,
    NaiveUI.NListItem,
    NaiveUI.NThing,
    NaiveUI.NDataTable,
    NaiveUI.NPopover,
    NaiveUI.NPagination,
    NaiveUI.NSelect,
    NaiveUI.NTreeSelect,
    NaiveUI.NRadioGroup,
    NaiveUI.NRadio,
    NaiveUI.NSteps,
    NaiveUI.NStep,
    NaiveUI.NInputGroup,
    NaiveUI.NResult,
    NaiveUI.NDescriptions,
    NaiveUI.NDescriptionsItem,
    NaiveUI.NTable,
    NaiveUI.NInputNumber,
    NaiveUI.NLoadingBarProvider,
    NaiveUI.NModal,
    NaiveUI.NUpload,
    NaiveUI.NTree,
    NaiveUI.NSpin,
    NaiveUI.NTimePicker,
    NaiveUI.NBackTop,
    NaiveUI.NSkeleton
  ]
});

export function setupNaive(app: App<Element>) {
  app.use(naive);
}
