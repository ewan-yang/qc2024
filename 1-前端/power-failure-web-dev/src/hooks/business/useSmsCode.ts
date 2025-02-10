import { computed } from 'vue';
import { fetchSmsCode } from '@/service';
import { useLoading } from '../common';
import useCountDown from './useCountDown';

export default function useSmsCode() {
  const { loading, startLoading, endLoading } = useLoading();
  const { counts, start, isCounting } = useCountDown(30);

  const initLabel = '获取验证码';
  const countingLabel = (second: number) => `${second}秒后重新获取`;
  const label = computed(() => {
    let text = initLabel;
    if (loading.value) {
      text = '';
    }
    if (isCounting.value) {
      text = countingLabel(counts.value);
    }
    return text;
  });

  /**
   * 获取短信验证码
   */
  async function getSmsCode(phone, pwdVerifyCode) {
    startLoading();
    const data = await fetchSmsCode(phone, pwdVerifyCode);
    if (data.data) {
      window.$message?.success('验证码发送成功！');
      start();
    }
    endLoading();
  }
  return {
    label,
    start,
    isCounting,
    getSmsCode,
    loading
  };
}
