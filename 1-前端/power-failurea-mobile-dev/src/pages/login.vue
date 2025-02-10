<script setup>
import { showToast } from 'vant'
import { getToken } from '~/utils/auth'

defineOptions({
  name: 'App',
})
const state = useGlobalState()

onMounted(async () => {
  if (!state.value.flag) {
    const res = await getToken()
    if (!res)
      showToast('失败')
  }
  else {
    // 非开发环境,本地开发环境
    showToast('当前为本地环境')
  }
})

async function gettoken() {
  const res = await getToken()
  if (!res)
    showToast('失败')
}
</script>

<template>
  登录模板
  <button @click="gettoken">
    获取igw_code
  </button>
</template>
