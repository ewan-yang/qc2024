import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
NProgress.configure({
  speed: 300, // 递增进度条的速度
  showSpinner: false, // 是否显示加载ico
  trickleSpeed: 50, // 自动递增间隔
  template: `<div id="progress-container" style="width: 100%;height:100%">
  <div id="progress-bar" role="bar"></div>
  <div id="progress-img"></div>
  <div id="progress-percent"></div>
  </div>`,
  parent: '#app', //指定进度条的父容器
})

// 开启进度条
export const NProgressStart = () => {
  NProgress.start(); // 开始进度条
  console.log(NProgress,'NProgress')
  const el = document.getElementsByTagName('main')[0]
  const progressPercent = document.getElementById('progress-percent');
  if(progressPercent){
    progressPercent.textContent ='应用数据加载中：(' + '0%)...'; // 初始化百分比为 0%
  }
  el.style.display = 'none'
  console.log("🚀 ~ file: nporgress.ts:18 ~ start ~ el:", el)

};
// 关闭进度条
export const NProgressSet = (value: any) => {
  NProgress.set(value);
};
// 关闭进度条
export const NProgressClose = () => {
  NProgress.done();
  const el = document.getElementsByTagName('main')[0]
  el.style.display = 'block'

};
// 获取进度条百分比
export const getProgressPercent = () => {
  const status = NProgress.status;
  const percent = Math.round(status * 100);
  return percent;
};
export const updateProgressPercent = () => {
  const progressPercent = document.getElementById('progress-percent');
  if (progressPercent) {
    const percent = getProgressPercent();
    console.log("🚀 ~ file: nporgress.ts:36 ~ updateProgressPercent ~ percent:", percent)
    progressPercent.textContent = '应用数据加载中：(' + percent + '%)...'; // 更新百分比内容
    if (percent >= 99) {
      clearInterval(progressInterval); // 进度达到 99%，停止定时器
    }
  }
};

// 每隔一段时间调用 updateProgressPercent 方法来更新进度条百分比
// 使用全局变量存储定时器的引用
let progressInterval =setInterval(updateProgressPercent, 100); // 每 0.5 秒更新一次
