import type { App } from 'vue';
import type { I18n, I18nOptions } from 'vue-i18n';
import { createI18n } from 'vue-i18n';
import { useLocaleStore } from '@/store';
import { localeSetting } from '@/settings/localeSetting';
import { setHtmlPageLang, setLoadLocalePool } from './helper';

const { fallback, availableLocales } = localeSetting;

// eslint-disable-next-line import/no-mutable-exports
export let i18n: ReturnType<typeof createI18n>;

async function createI18nOptions(): Promise<I18nOptions> {
  const localeStore = useLocaleStore();
  const locale = localeStore.getLocale;
  const defaultLocal = await import(`./lang/${locale}.ts`);
  const message = defaultLocal.default?.message ?? {};
  setHtmlPageLang(locale);
  setLoadLocalePool(loadLocalePool => {
    loadLocalePool.push(locale);
  });

  return {
    legacy: false,
    locale,
    fallbackLocale: fallback,
    messages: {
      [locale]: message
    },
    availableLocales,
    sync: true, // 如果你不想从全局作用域继承locale，你需要将sync of i18n组件选项设置为false。
    silentTranslationWarn: true, // true - warning off
    missingWarn: false,
    silentFallbackWarn: true
  };
}

// setup i18n instance with glob
export async function setupI18n(app: App) {
  const options = await createI18nOptions();
  i18n = createI18n(options) as I18n;
  app.use(i18n);
}
