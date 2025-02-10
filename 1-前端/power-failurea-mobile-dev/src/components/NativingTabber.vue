<script  setup>
defineOptions({
  name: 'NativingTabber',
})
import {getIndexIconSvgToBase64,getIndexActiveIconSvgToBase64,getTaskIconSvgToBase64,getTaskActiveIconSvgToBase64,getErrorIconSvgToBase64,getErrorActiveIconSvgToBase64} from '~/utils/svgBase64'
const state = useGlobalState()
const active = ref(state.value.tabberNum)
// const active = state.value.tabberNum

const postTabberNum = (i)=>{
  state.value.tabberNum = i
  active.value = i
}
</script>

<template>
    <van-tabbar  style="height:3rem;"  v-model="active" active-color="#048D74" >
      <van-tabbar-item  to="/indexTabber" @click="postTabberNum(0)" replace><span>首页</span><template #icon="props">
        <img :src="props.active ?   getIndexActiveIconSvgToBase64() : getIndexIconSvgToBase64()" />
      </template></van-tabbar-item>
      <van-tabbar-item  to="/taskTabber" @click="postTabberNum(1)" replace><span>任务</span><template #icon="props">
        <img :src="props.active ?   getTaskActiveIconSvgToBase64() : getTaskIconSvgToBase64()" />
      </template></van-tabbar-item>
      <van-tabbar-item  to="/riskTabber" @click="postTabberNum(2)" replace><span>风险</span><template #icon="props">
        <img :src="props.active ?  getErrorActiveIconSvgToBase64() : getErrorIconSvgToBase64() " />
      </template></van-tabbar-item>
    </van-tabbar>
</template>
