import type { SelectOption } from 'naive-ui';
import type { LocaleSetting, LocaleType } from '@/typings/config';

export const LOCALE: { [key: string]: LocaleType } = {
  'zh-CN': 'zh-CN',
  EN_US: 'en'
};

export const localeSetting: LocaleSetting = {
  showPicker: true,
  // Locale
  locale: LOCALE['zh-CN'],
  // Default locale
  fallback: LOCALE['zh-CN'],
  // available Locales
  availableLocales: [LOCALE['zh-CN'], LOCALE.EN_US]
};

// locale list
export const localeList: SelectOption[] = [
  {
    label: '简体中文',
    value: LOCALE['zh-CN']
  },
  {
    label: 'English',
    value: LOCALE.EN_US
  }
];
