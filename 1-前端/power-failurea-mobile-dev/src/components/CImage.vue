<script  lang="ts">
import type { ImageProps } from 'vant'

import { Image } from 'vant'
import { getBase64 } from '~/utils/transBase64'

interface cImgProps {
  base64Url: string
}

export default defineComponent<ImageProps & cImgProps>({
  name: 'CImage',
  props: {
    ...Image.props,
    base64Url: { type: String },
  },
  setup(props) {
    const imageBase64 = ref<string>('')
    onMounted(async () => {
      if (props.base64Url)
        imageBase64.value = await getBase64(props.base64Url) as string
    })
    return {
      imageBase64,
    }
  },
})
</script>

<template>
  <van-image v-bind="$props" :src="imageBase64" >
    <template #error>
      加载失败
    </template>
  </van-image>
</template>
