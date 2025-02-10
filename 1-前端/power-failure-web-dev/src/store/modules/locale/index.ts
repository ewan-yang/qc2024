import { defineStore } from 'pinia';
import type { NLocale, NDateLocale } from 'naive-ui';
import { zhCN, dateZhCN } from 'naive-ui';
import { localeSetting } from '@/settings';
import type { LocaleSetting, LocaleType } from '@/typings/config';

interface LocaleState {
  localInfo: LocaleSetting;
}

interface NaiveLocale {
  locale: NLocale;
  dateLocale: NDateLocale;
}

export const useLocaleStore = defineStore('locale-store', {
  state: (): LocaleState => ({
    localInfo: {} as LocaleSetting
  }),
  getters: {
    getShowPicker(): boolean {
      return Boolean(this.localInfo?.showPicker);
    },
    getLocale(): LocaleType {
      return this.localInfo?.locale ?? 'zh-CN';
    },
    getNaiveLocale(): NaiveLocale | null {
      return this.getLocale === 'zh-CN'
        ? {
            locale: zhCN,
            dateLocale: dateZhCN
          }
        : null;
    }
  },
  actions: {
    /**
     * 设置多语言信息
     * @param info multilingual info
     */
    setLocaleInfo(info: Partial<LocaleSetting>) {
      this.localInfo = { ...this.localInfo, ...info };
    },
    /**
     * 初始化多语言信息
     */
    initLocale() {
      this.setLocaleInfo({
        ...localeSetting,
        ...this.localInfo
      });
    }
  }
});
