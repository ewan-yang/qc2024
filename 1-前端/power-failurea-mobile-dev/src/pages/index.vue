<template>
  <div class="login-container">
    <div class="title-container">
      <h3 class="title">停电通知</h3>
    </div>

    <div class="loader-wrapper">
      <!-- <div class="loader"><img src="@/static/images/icon.png" alt="" /></div> -->
      <div class="loader-text">
        <!-- 10个div -->
        <div>L</div>
        <div>O</div>
        <div>A</div>
        <div>D</div>
        <div>I</div>
        <div>N</div>
        <div>G</div>
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineOptions({
  name: 'index',
})
import { useGlobalState } from '~/composables/store'
import { getToken,getNoProToken } from '~/utils/auth'
const state = useGlobalState()
const router = useRouter()
onMounted(async ()=>{
  if(import.meta.env.VITE_NODE_ENV === 'production'){
    if(state.value.token){
      router.replace({ path: '/indexTabber' })
    }else{
      await getToken(router)
    }
  }else{
    await getNoProToken(router)
  }
})
</script>

<style scoped>
* {
  /* 初始化 */
  margin: 0;
  padding: 0;
}
body {
  /* 100%窗口高度 */
  height: 100vh;
}
.loader-wrapper {
  /* 固定定位 */
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1;
  width: 100%;
  height: 100%;
  /* 溢出隐藏 */
  overflow: hidden;
  /* 渐变背景 */

  background-color: rgb(255, 255, 255);

  background-size: 400%;
  background-position: 0% 100%;
}
/* 旋转loading的外圈 */
.loader {
  width: 150px;
  height: 150px;
  border: 3px solid transparent;
  border-top-color: #005a50;
  /* 相对定位 居中 */
  position: relative;
  left: 50%;
  top: 50%;
  margin-left: -75px;
  margin-top: -75px;
  z-index: 2;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* 执行旋转动画 */
  animation: spin 1.7s linear infinite;
}
/* 旋转loading的中圈 */
.loader::before {
  content: '';
  /* 绝对定位 */
  position: absolute;
  top: 5px;
  left: 5px;
  bottom: 5px;
  right: 5px;
  border-radius: 50%;
  border: 3px solid transparent;
  border-top-color: #005a50;
  /* 执行反向的旋转动画 reverse:反向 */
  animation: spin 0.6s linear infinite reverse;
}
/* 旋转loading的内圈 */
.loader::after {
  content: '';
  /* 绝对定位 */
  position: absolute;
  top: 15px;
  left: 15px;
  bottom: 15px;
  right: 15px;
  border-radius: 50%;
  border: 3px solid transparent;
  border-top-color: #005a50;
  /* 执行旋转动画 */
  animation: spin 1s linear infinite;
}
/* logo */
.loader img {
  width: 55%;
  height: 55%;
  border-radius: 50%;
  /* 执行反向的旋转动画,时长必须和外圈的动画一致(不让其跟着旋转) */
  animation: spin 1.7s linear infinite reverse;
}
.loader-text {
  width: 50%;
  height: 36px;
  /* 绝对定位 水平居中 */
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translateX(-50%);
  z-index: 3;
  /* 防止选取 */
  user-select: none;
}
.loader-text div {
  width: 30px;
  height: 36px;
  color: #005a50;
  font-size: 20px;
  margin: 0 20px;
  /* 绝对定位 */
  position: absolute;
  /* 默认隐藏+旋转180度 */
  opacity: 0;
  transform: rotate(180deg);
  /* 执行文字移动动画 */
  animation: move 2s linear infinite;
}
/* 最后面的三个圆 */
.loader-text div:nth-child(8)::before,
.loader-text div:nth-child(9)::before,
.loader-text div:nth-child(10)::before {
  content: '';
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background-color: #005a50;
  position: absolute;
  left: 0;
  bottom: 0;
}
/* 文字下的投影 */
.loader-text div::after {
  content: '';
  width: 10px;
  height: 5px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.15);
  position: absolute;
  bottom: -40px;
  left: 50%;
  margin-left: -5px;
}
/* 最后面的三个圆的投影 */
.loader-text div:nth-child(8)::after,
.loader-text div:nth-child(9)::after,
.loader-text div:nth-child(10)::after {
  left: 0;
  margin-left: 0;
}
/* 接下来为各个文字设置动画延迟时间 */
.loader-text div:nth-child(2) {
  animation-delay: 0.2s;
}
.loader-text div:nth-child(3) {
  animation-delay: 0.4s;
}
.loader-text div:nth-child(4) {
  animation-delay: 0.6s;
}
.loader-text div:nth-child(5) {
  animation-delay: 0.8s;
}
.loader-text div:nth-child(6) {
  animation-delay: 1s;
}
.loader-text div:nth-child(7) {
  animation-delay: 1.2s;
}
.loader-text div:nth-child(8) {
  animation-delay: 1.4s;
}
.loader-text div:nth-child(9) {
  animation-delay: 1.6s;
}
.loader-text div:nth-child(10) {
  animation-delay: 1.8s;
}

/* 定义动画 */
/* 背景渐变动画 */
@keyframes gradient {
  50% {
    background-position: 100% 0%;
  }
}
/* 旋转动画 */
@keyframes spin {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
/* 文字移动动画 */
@keyframes move {
  0% {
    right: 0;
    opacity: 0;
  }
  35% {
    right: 41%;
    opacity: 1;
    transform: rotate(0);
  }
  65% {
    right: 59%;
    opacity: 1;
    transform: rotate(0);
  }
  100% {
    right: 100%;
    transform: rotate(-180deg);
  }
}

.fingerprint {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0vh;
  margin: auto;
  margin-bottom: 2vh;
}
#mainLoading {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

#mainLoading .ddr {
  width: 8px;
  height: 100px;
  float: left;
  margin: 2px;
  background-color: #255353de;
  animation: loading 1s infinite ease-in-out;
  /*animation：动画名称 持续时间 动画速度曲线 延迟 执行多少次 是否正反方向轮流播放*/
}

#mainLoading .ddr2 {
  animation-delay: -0.9s;
  /*定义开始执行的地方，负号表示直接从第900ms开始执行*/
}

#mainLoading .ddr3 {
  animation-delay: -0.8s;
}

#mainLoading .ddr4 {
  animation-delay: -0.7s;
}

#mainLoading .ddr5 {
  animation-delay: -0.6s;
}

@keyframes loading {
  0%,
  40%,
  100% {
    /*定义每帧的动作*/
    -webkit-transform: scaleY(0.5);
  }

  20% {
    -webkit-transform: scaleY(1);
  }
}

.loginField {
  margin-bottom: 20px;
}
  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;

    padding: 160px 35px 0;
    margin: 0 auto;
    margin-top: -50%;
    overflow: hidden;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: #889aa4;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }
    .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: #889aa4;
    cursor: pointer;
    user-select: none;
  }
   .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }
  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
  .login-verify-img {
    width: 100%;
    height: 53px;
  }
.login-container {
  min-height: 100%;
  width: 100%;
  height: 100vh;
  background-color: #f8f9f9;
  overflow: hidden;
  display: flex;
  align-items: center;












}
</style>
