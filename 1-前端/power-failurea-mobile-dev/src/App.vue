<script setup>

import { useGlobalState } from '~/composables/store'
import { Api } from "./utils/api";
import { getToken,getNoProToken } from '~/utils/auth'
// 导入进度条
import { NProgressStart, NProgressClose } from "~/utils/nporgress";
defineOptions({
  name: 'App',
})
import {getIndexIconSvgToBase64,getIndexActiveIconSvgToBase64,getTaskIconSvgToBase64,getTaskActiveIconSvgToBase64,getErrorIconSvgToBase64,getErrorActiveIconSvgToBase64} from '~/utils/svgBase64'
const state = useGlobalState()
const active = ref(0)
const router = useRouter()
const postTabberNum = (i)=>{
  state.value.tabberNum = i
  active.value = i
}

onMounted(async ()=>{
    //原生获取屏幕高度
    var orderHight = document.body.clientHeight
    const tabberHeight = document.getElementById('tabberWu').style.height
    document.getElementById('mainWu').style.height = orderHight - tabberHeight + 'px'
    router.replace({ path: '/' })
})

// 在组件即将被卸载时执行
onBeforeUnmount(() => {
  state.value.token = '';
  state.value.flag = false;
  state.value.ISCP_CONNECTED = false;
  state.value.ISCP_USED_PORT = '';
  state.value.ISCP_FAILD_VALUE = 0;
  state.value.tabberNum = 0;
  state.value.name = '';
  state.value.password = '';
  state.value.engineersTeamId = '';
  state.value.showTabber = false
});
</script>


<template >
  <main style="height:90%" id="mainWu"  p="x-4" text="center gray-700 dark:gray-200">
    <div style="height:100%" pt-2>
      <RouterView />
    </div>
  </main>
  <van-tabbar id="tabberWu" v-if="state.token && state.showTabber" fixed flex items-center style="bottom: 0;"  v-model="active" active-color="#048D74" >
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
<style scoped>

</style>
