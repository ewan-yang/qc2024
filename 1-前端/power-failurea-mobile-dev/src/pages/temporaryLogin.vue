<template>
  <van-form @submit="onSubmit">
  <van-cell-group inset>
    <van-field
      v-model="username"
      name="username"
      label="账号"
      placeholder="账号"
      :rules="[{ required: true, message: '请填写账号' }]"
    />
    <van-field
      v-model="password"
      type="password"
      name="password"
      label="密码"
      placeholder="密码"
      :rules="[{ required: true, message: '请填写密码' }]"
    />
  </van-cell-group>
  <div style="margin: 16px;">
    <van-button round block type="primary" bg-blue-5  color-white native-type="submit">
      登录
    </van-button>
  </div>
</van-form>
</template>
<script>
import { ref } from 'vue';
import {temporaryLoginApi} from '../utils/auth'
import CryptoJS from 'crypto-js'
export default {
  setup() {
    const router = useRouter()
    const username = ref('');
    const password = ref('');
    const onSubmit =   (values) => {
      console.log('submit', values);
       temporaryLoginApi(values.username,CryptoJS.SHA1(values.password).toString(),router)

    }
    return {
        username,
        password,
        onSubmit,
      };
  }
}
</script>
