import { computed } from 'vue';
import { fetchCode } from '@/service';
import { useLoading } from '../common';
import useCountDown from './useCountDown';

export default function useCode() {
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
   * 获取验证码
   */
  async function getCode() {
    startLoading();
    const { data } = await fetchCode();
    if (data) {
      window.$message?.success('验证码发送成功！');
      start();
    }
    endLoading();
  }
  return {
    label,
    start,
    isCounting,
    getCode,
    loading
  };
}
