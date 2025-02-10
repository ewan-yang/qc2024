<template>
    <n-image
      :src="imageBase64"
      v-bind="$props"
    />
</template>
<script setup lang="ts">
  import {ref, onMounted,defineProps} from "vue";
  import { getToken} from '@/store/modules/auth/helpers';
  defineOptions({ name: 'ImgToken' });
  const imageBase64 = ref('')
  const props = defineProps({
    base64Url:String,
  });
  onMounted(async () => {
    if (props.base64Url)
      imageBase64.value = await getImageAsJPEGBase64(props.base64Url)
  })
async function getImageAsJPEGBase64(imgUrl) {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.responseType = 'blob';
    xhr.open('GET', imgUrl, true);
    xhr.setRequestHeader(
      'Authorization',
      `Bearer ${getToken()}`
    );

    xhr.onload = function () {
      if (this.status === 200) {
        const blob = this.response;
        const img = new Image();

        img.onload = () => {
          const canvas = document.createElement('canvas');
          canvas.width = img.width;
          canvas.height = img.height;
          const ctx = canvas.getContext('2d');
          ctx.drawImage(img, 0, 0, img.width, img.height);

          canvas.toBlob((jpegBlob) => {
            if (!jpegBlob) {
              reject(new Error('无法转换为JPEG Blob'));
            } else {
              const reader = new FileReader();
              reader.onloadend = () => resolve(reader.result);
              reader.onerror = reject;
              reader.readAsDataURL(jpegBlob);
            }
          }, 'image/jpeg');
        };

        img.onerror = (error) => reject(error);
        img.src = URL.createObjectURL(blob);
      } else {
        reject(new Error(`请求图片失败，状态码：${xhr.status}`));
      }
    };

    xhr.onerror = () => reject(new Error('网络请求失败'));

    xhr.send();
  });
}

</script>