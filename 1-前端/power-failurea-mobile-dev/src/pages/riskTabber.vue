<script setup name="mao">
defineOptions({
  name: 'riskTabber',
})

import { getLogoSvgToBase64 } from '~/utils/svgBase64'
import { Api } from "../utils/api";
const router = useRouter();
const searchData = ref('')
const advanceNoticeTotalData = ref({})
advanceNoticeTotalData.value = {
  planItemChangeCount: 0,
  carrierReleaseCount: 0,
  releaseTimeOutCount: 0,
  userRejectCount: 0,
  repeatPowerCutCount: 0
}
const selectAlarmBusinessList = ref([])
const state = useGlobalState()
onMounted(() => {
  state.value.showTabber = true
  if (state.value.token) {
    initTotal()
    initBusinessList()
  }

})
onBeforeUnmount(()=>{
  state.value.showTabber = false
})
function initTotal() {
  const { data, execute } = usePost(Api.advanceNoticeTotal, {});
  execute().then(() => {
    advanceNoticeTotalData.value = data.value.data
  });
}

function initBusinessList(val = '') {
  const { data, execute } = usePost(Api.selectAlarmBusinessList, { ...(searchData.value != '' && { businessCode: val }) })
  execute().then(() => {
    selectAlarmBusinessList.value = data?.value?.data
    if (state.value.engineersTeamId) {
      selectAlarmBusinessList.value = selectAlarmBusinessList.value.filter(item => item.alarmType != 5 && item.alarmType != 2)
    }
  });
}
function onSearch(val) {
  initBusinessList(val)
}
function goRiskDetail(record) {
  router.push({ path: "/riskDetail", query: { id: record?.businessId, businessType: record.businessType, alarmType: record.alarmType } });
}

</script>

<template>
  <div m-b-4 style="height:100%;">
    <div style="height:7%;"  flex items-center>
      <img :src="getLogoSvgToBase64()" style="transform: scale(.7); margin-right: 9px;">
      <div font-500 color-white class="fs20">停电计划及服务通知</div>
    </div>

    <div style="height:7%;" flex items-center justify-between>
<van-search v-model="searchData" placeholder="搜索" input-align="center" bg-white @search="onSearch"
        style="padding:0 12px 0 0;width:100%" />

    </div>
    <!-- 市南权限 start -->
    <div  style="height:8%;margin-bottom: 5%;" m-t-2 v-if="!state.engineersTeamId">
      <div flex justify-between items-center style="font-size:12px;height:100%">
        <div flex items-center justify-center m-b-2 b-rd-1 bg-white w-16  style="color: #f7b88d;height:100%">
          <div>
            <div>计划变更</div>
            <div>{{ advanceNoticeTotalData?.planItemChangeCount }}</div>
          </div>
        </div>
        <div flex items-center justify-center m-b-2 b-rd-1 bg-white w-16  style="color: #f7b88d;height:100%">
          <div>
            <div>下达风险</div>
            <div>{{ advanceNoticeTotalData?.carrierReleaseCount }}</div>
          </div>
        </div>
        <div flex items-center justify-center m-b-2 b-rd-1 bg-white w-16  style="color: #DE4444;height:100%">
          <div>
            <div>下达超时</div>
            <div>{{ advanceNoticeTotalData?.releaseTimeOutCount }}</div>
          </div>
        </div>
        <div flex items-center justify-center m-b-2 b-rd-1 bg-white w-16  style="color: #DE4444;height:100%">
          <div>
            <div>用户拒签</div>
            <div>{{ advanceNoticeTotalData?.userRejectCount }}</div>
          </div>
        </div>
        <div flex items-center justify-center m-b-2 b-rd-1 bg-white w-17 h-16 style="color: #f7b88d;height:100%">
          <div>
            <div>重复停电</div>
            <div>{{ advanceNoticeTotalData?.repeatPowerCutCount }}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- 市南权限 end -->
    <template v-if="Object.keys(selectAlarmBusinessList).length">
      <div bg-white p-t-3 p-b-3 :style="!state.engineersTeamId ? { height: '65%' } : { height: '75%' }"
        overflow-scroll m-t-2>


        <div style="font-size: 14px;width: 100%;" v-for="item in selectAlarmBusinessList" :key="item.id"
          @click="goRiskDetail(item)" m-b-3>

          <div style="display: flex;justify-content: space-between;">

            <div v-if="item.alarmType == 3" b-l-5 class="red-b" style="width: 5%;"></div>
            <div v-else b-l-5 class="yellow-b" style="width: 5%;"></div>

            <div v-if="item.alarmType == 2" color-gray-4 style="width: 30%;box-sizing: border-box;padding-right: 3%;"
              text-right>任务编号:</div>
            <div v-else-if="item.alarmType == 5" color-gray-4 style="width: 30%;box-sizing: border-box;padding-right: 3%;"
              text-right>工程编号:</div>
            <div v-else color-gray-4 style="width: 30%;box-sizing: border-box;padding-right: 3%;" text-right>回执单号:</div>



            <div v-if="item.alarmType == 2 || item.alarmType == 5" style="width: 50%;" text-left>{{ item.businessCode }}
            </div>
            <div v-else style="width: 50%;" text-left>{{ item.receiptCode }}</div>

            <div v-if="item.alarmType == 3" class="red-bg" style="width: 10%;">告警</div>
            <div v-else class="yellow-bg" style="width: 10%;">预警</div>

            <div style="width: 5%;"></div>

          </div>



          <div style="display: flex;justify-content: space-between;">

            <div b-l-5 style="width: 5%;border-color: transparent;"></div>
            <div color-gray-4 style="width: 30%;box-sizing: border-box;padding-right: 3%;" text-right>预告警类型:</div>

            <div style="width: 60%;" text-left>{{ item.alarmType == 1 ? '下达超时预警' : item.alarmType == 2 ? '下达风险预警' :
              item.alarmType == 3
                ?
                '用户拒签告警' : item.alarmType == 4 ? '重复停电预警' : '计划变更预警' }}
            </div>
            <div style="width: 5%;"></div>

          </div>

          <div style="background-color: white;display: flex;flex-direction: column;align-items: center;padding: 0;">
            <div style="width: 90%;text-align: right;" color-gray-4>
              {{ item.alarmTime }}
            </div>
            <div style="border: 1px solid rgb(243, 243, 243);width:90%;">
            </div>
          </div>

        </div>

      </div>
    </template>

    <van-empty v-else description="暂无数据" />

  </div>
</template>
<style scoped>
.fs20 {
  font-size: 16px;
  margin-left: -8px;
  font-family: 'Noto Sans SC';
  font-weight: 400;
}

.red-b {
  --un-border-opacity: 1;
  border-color: rgba(218, 67, 59, var(--un-border-opacity));
}

.yellow-b {
  --un-border-opacity: 1;
  border-color: rgba(255, 164, 52, var(--un-border-opacity));
}

.red-bg {
  --un-bg-opacity: 1;
  background-color: rgba(255, 241, 240, var(--un-bg-opacity));
  color: rgba(188, 47, 50, 1);
  font-size: 12px;
  line-height: 21px;
}

.yellow-bg {
  --un-bg-opacity: 1;
  background-color: rgba(255, 246, 243, var(--un-bg-opacity));
  color: rgba(245, 127, 23, 1);
  font-size: 12px;
  line-height: 21px;
}</style>
