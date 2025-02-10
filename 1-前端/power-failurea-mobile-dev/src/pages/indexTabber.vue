<template>
  <div style="height:100%" overflow-auto>

    <div style="height:10%"  flex items-center justify-between>
      <div flex items-center>
        <!-- <c-image round  base64-url="../img/logo.png"  w-8 h-8 m-r/> -->
        <img :src="getLogoSvgToBase64()" class="h5-t">
        <div font-500 color-white class="fs20">停电计划及服务通知</div>
      </div>
      <!-- 扫描二维码 -->
      <van-icon name="scan" size="21" color="white" @click="scan" />
    </div>
    <div style="height:90%">
      <!-- ========================================================================== -->
      <!-- 停电通知 -->
      <div style="height:15%;" v-if="!state.engineersTeamId">
        <div style="height:20%;"  class="box-title">
          <img :src="getNoticeLogoSvgToBase64()" class="noticeLogo">
          <span>停电计划</span>
        </div>
              <div style="height:80%">
                <van-row class="color-5" style="height:100%;">
          <van-col span="6" class="pd-c-0" style="height:100%;">
            <div class="box-notice bgc-0 bs-1" style="height:100%">
              <div class="f-all color-6">{{ statisticsTotal.releaseQty }}</div>
              <div class="mb-1">待派发</div>
            </div>
          </van-col>
          <van-col span="6" class="pd-c-1" style="height:100%;">
            <div class="box-notice bgc-0 bs-2" style="height:100%;">
              <div class="f-all color-7">{{ statisticsTotal.executionQty }}</div>
              <div class="mb-1">执行中</div>
            </div>
          </van-col>
          <van-col span="6" class="pd-c-1" style="height:100%;">
            <div class="box-notice bgc-0 bs-3" style="height:100%;">
              <div class="f-all color-8">{{ statisticsTotal.cancelQty }}</div>
              <div class="mb-1">已取消</div>
            </div>
          </van-col>
          <van-col span="6" class="pd-c-1" style="height:100%;">
            <div class="box-notice bgc-0 bs-4" style="height:100%;">
              <div class="f-all color-9">{{ statisticsTotal.feedbackQty }}</div>
              <div class="mb-1">已反馈</div>
            </div>
          </van-col>
          </van-row>
        </div>
      </div>

      <!-- 停电通知回执和取消 -->
      <div class="mt mb">
        <van-row>
          <!-- 停电通知回执 -->
          <van-col span="15" class="color-5">
            <div class="receipt-box">
              <div :class="!state.engineersTeamId ? 'box-title bt-1 mb-2' : 'box-title bt-1 mb-2 color-0'">
                <img :src="!state.engineersTeamId ? getReceiptLogoSvgToBase64() : getReceiptLogoWhiteSvgToBase64()"
                  class="receiptLogo">
                <span> 停电通知回执</span>
              </div>
              <div class="bgc-0 rp-wrap1">
                <van-row v-if="!state.engineersTeamId" class="rp-row">
                  <van-col span="6">
                    <div class="rp-box">
                      <div class="f-all color-6">{{ receiptNotice.undistributedQty }}
                      </div>
                      <div class="mb-1">未派发</div>
                    </div>
                  </van-col>
                  <van-col span="6">
                    <div class="rp-box">
                      <div class="f-all color-10">{{ receiptNotice.noFeedbackQty }}</div>
                      <div class="mb-1">未签</div>
                    </div>
                  </van-col><van-col span="6">
                    <div class="rp-box">
                      <div class="f-all color-11">{{ receiptNotice.refusedSignQty }}</div>
                      <div class="mb-1">拒签</div>
                    </div>
                  </van-col><van-col span="6">
                    <div class="rp-box">
                      <div class="f-all color-12">{{ receiptNotice.timeOutQty }}</div>
                      <div class="mb-1">已超时</div>
                    </div>
                  </van-col>
                </van-row>
                <van-row v-else class="rp-row">
                  <van-col span="8">
                    <div class="rp-box ">
                      <div class="f-all color-10">{{ receiptNotice.noFeedbackQty }}</div>
                      <div class="mb-1">未签</div>
                    </div>
                  </van-col><van-col span="8">
                    <div class="rp-box">
                      <div class="f-all color-11">{{ receiptNotice.refusedSignQty }}</div>
                      <div class="mb-1">拒签</div>
                    </div>
                  </van-col><van-col span="8">
                    <div class="rp-box ">
                      <div class="f-all color-12">{{ receiptNotice.timeOutQty }}</div>
                      <div class="mb-1">已超时</div>
                    </div>
                  </van-col>
                </van-row>
              </div>

            </div>
          </van-col>

          <!-- 取消停电回执 -->
          <van-col span="9" class="color-5">
            <div class="cancel-box">
              <div :class="!state.engineersTeamId ? 'box-title bt-1 mb-2' : 'box-title bt-1 mb-2 color-0'">
                <img
                  :src="!state.engineersTeamId ? getCancelReceiptLogoSvgToBase64() : getCancelReceiptWhiteLogoSvgToBase64()"
                  class="cancelLogo">
                <span>取消停电回执</span>
              </div>
              <div class="bgc-0 rp-wrap2">
                <van-row v-if="!state.engineersTeamId" style="height: 55px;">
                  <van-col span="12">
                    <div class="rp-box">
                      <div class="f-all color-6">{{ receiptNotice.cancelUndistributedQty
                      }}</div>
                      <div class="mb-1">未派发</div>
                    </div>
                  </van-col>
                  <van-col span="12">
                    <div class="rp-box">
                      <div class="f-all color-10">{{ receiptNotice.cancelNoFeedbackQty }}
                      </div>
                      <div class="mb-1">未签</div>
                    </div>
                  </van-col>
                </van-row>
                <van-row v-else>
                  <van-col span="24">
                    <div class="rp-box">
                      <div class="f-all color-10">{{ receiptNotice.cancelNoFeedbackQty }}
                      </div>
                      <div class="mb-1">未签</div>
                    </div>
                  </van-col>
                </van-row>
              </div>

            </div>
          </van-col>
        </van-row>
      </div>

      <!-- 通知消息 -->
      <div class="text-box">
        <div class="box-title bt-1">
          <img :src="getMessageLogoSvgToBase64()" class="messageLogo">
          <span> 通知消息</span>
        </div>
        <van-row>
          <van-col span="24" class="pd-c-1">
            <div v-if="taskNoticeArr.length" class="text-content bgc-0">
              <div v-for="item in taskNoticeArr" class="notice-b">
                <div class="notice-t1">{{ item.content.slice(item.content.indexOf(']') + 1) }}</div>
                <div class="notice-t2">{{ item.content.slice(1, item.content.indexOf(']')) }}</div>
              </div>
            </div>
            <van-empty v-else description="暂无数据" class="h-30" />
          </van-col>
        </van-row>
      </div>

      <!-- 数据汇总 -->
      <div class="total-box" >
        <div class="box-title bt-1 mb-2">
          <img :src="getTotalLogoSvgToBase64()" class="messageLogo">
          <span> 数据汇总</span>
        </div>
        <div v-if="!state.engineersTeamId">
          <!-- 第一行 -->
          <van-row>
            <van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0 bgimg-1 div-r">
                <img :src="getTotalNoticeLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-10">{{ dataTotal.taskSumQty
                }} </span> <span class="one">个</span></div>
                <div class="mb-3 common-f">停电通知总数</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskSumTb === null ? ' -- ' : (Number(dataTotal.taskSumTb) >= 0 ? '增加'
                      + Math.abs(Number(dataTotal.taskSumTb)) : '减少' + Math.abs(Number(dataTotal.taskSumTb)))
                  }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.taskSumTb) > 0 ? 'arrow-up' : (Number(dataTotal.taskSumTb) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskSumTbPercentage === null ? ' -- ' : ((Number(dataTotal.taskSumTbPercentage) >= 0 ?
                      '上涨' : '下降') + Math.abs(Number(dataTotal.taskSumTbPercentage))) }}</span>%<van-icon name="down"
                      :class="Number(dataTotal.taskSumTbPercentage) > 0 ? 'arrow-up' : (Number(dataTotal.taskSumTbPercentage) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col>
            <van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0  div-r">
                <img :src="getTotalNoticeSuccessLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-13">{{
                  dataTotal.taskFinishedQty }} </span> <span class="one">个</span>
                </div>
                <div class="mb-3 common-f">已完成通知数</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskFinishedTb === null ? ' -- ' : (Number(dataTotal.taskFinishedTb) >= 0 ? '增加'
                      + Math.abs(Number(dataTotal.taskFinishedTb)) : '减少' + Math.abs(Number(dataTotal.taskFinishedTb)))
                  }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.taskFinishedTb) > 0 ? 'arrow-up' : (Number(dataTotal.taskFinishedTb) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskFinishedTbPercentage === null ? ' -- ' : (
                      (Number(dataTotal.taskFinishedTbPercentage) >= 0 ? '上涨' : '下降')
                      + Math.abs(Number(dataTotal.taskFinishedTbPercentage))) }}</span>%<van-icon name="down"
                      :class="Number(dataTotal.taskFinishedTbPercentage) > 0 ? 'arrow-up' : (Number(dataTotal.taskFinishedTbPercentage) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col><van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0  div-r">
                <img :src="getTotalNoticeOverLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-11">{{
                  dataTotal.taskCancelQty }} </span> <span class="one">个</span>
                </div>
                <div class="mb-3 common-f">已取消通知数</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskCancelTb === null ? ' -- ' : (Number(dataTotal.taskCancelTb) >= 0 ? '增加'
                      + Math.abs(Number(dataTotal.taskCancelTb)) : '减少' + Math.abs(Number(dataTotal.taskCancelTb)))
                  }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.taskCancelTb) > 0 ? 'arrow-up' : (Number(dataTotal.taskCancelTb) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskCancelTbPercentage === null ? ' -- ' : ((Number(dataTotal.taskCancelTbPercentage)
                      >= 0 ? '上涨' : '下降') + Math.abs(Number(dataTotal.taskCancelTbPercentage))) }}</span>%<van-icon
                      name="down"
                      :class="Number(dataTotal.taskCancelTbPercentage) > 0 ? 'arrow-up' : (Number(dataTotal.taskCancelTbPercentage) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col>
          </van-row>
          <!-- 第二行 -->
          <van-row>
            <van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0  div-r">
                <img :src="getTotalUserLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-10">{{
                  dataTotal.taskUserSumQty }} </span> <span class="one">个</span>
                </div>
                <div class="mb-3 common-f">停电用户数</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskUserSumTb === null ? ' -- ' : (Number(dataTotal.taskUserSumTb) >= 0 ? '增加'
                      + Math.abs(Number(dataTotal.taskUserSumTb)) : '减少' + Math.abs(Number(dataTotal.taskUserSumTb)))
                  }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.taskUserSumTb) > 0 ? 'arrow-up' : (Number(dataTotal.taskUserSumTb) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.taskUserSumTbPercentage === null ? ' -- ' : ((Number(dataTotal.taskUserSumTbPercentage)
                      >= 0 ? '上涨' : '下降') + Math.abs(Number(dataTotal.taskUserSumTbPercentage))) }}</span>%<van-icon
                      name="down"
                      :class="Number(dataTotal.taskUserSumTbPercentage) > 0 ? 'arrow-up' : (Number(dataTotal.taskUserSumTbPercentage) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col>
            <van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0  div-r">
                <img :src="getTotalRegionLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-13">{{
                  dataTotal.powerCutAreaQty }} </span> <span class="one">个</span>
                </div>
                <div class="mb-3 common-f">停电区域数</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.powerCutAreaTb === null ? ' -- ' : (Number(dataTotal.powerCutAreaTb) >= 0 ? '增加'
                      + Math.abs(Number(dataTotal.powerCutAreaTb)) : '减少' + Math.abs(Number(dataTotal.powerCutAreaTb)))
                  }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.powerCutAreaTb) >= 0 ? 'arrow-up' : (Number(dataTotal.powerCutAreaTb) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.powerCutAreaTbPercentage === null ? ' -- ' : ((Number(dataTotal.powerCutAreaTbPercentage)
                      >= 0 ? '上涨' : '下降') + Math.abs(Number(dataTotal.powerCutAreaTbPercentage))) }}</span>%<van-icon
                      name="down"
                      :class="Number(dataTotal.powerCutAreaTbPercentage) > 0 ? 'arrow-up' : (Number(dataTotal.powerCutAreaTbPercentage) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col>
            <van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0  div-r">
                <img :src="getTotalRepeatLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-11">{{
                  dataTotal.powerCutRepeatAreaQty }} </span> <span class="one">个</span></div>
                <div class="mb-3 common-f">重复停电区域数</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.powerCutRepeatAreaTb === null ? ' -- ' : (Number(dataTotal.powerCutRepeatAreaTb) >= 0 ?
                      '增加' + Math.abs(Number(dataTotal.powerCutRepeatAreaTb)) :
                      '减少' + Math.abs(Number(dataTotal.powerCutRepeatAreaTb))) }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.powerCutRepeatAreaTb) >= 0 ? 'arrow-up' : (Number(dataTotal.powerCutRepeatAreaTb) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.powerCutRepeatAreaTbPercentage === null ? ' -- ' : (
                      (Number(dataTotal.powerCutRepeatAreaTbPercentage) >= 0 ? '上涨' : '下降')
                      + Math.abs(Number(dataTotal.powerCutRepeatAreaTbPercentage))) }}</span>%<van-icon name="down"
                      :class="Number(dataTotal.powerCutRepeatAreaTbPercentage) > 0 ? 'arrow-up' : (Number(dataTotal.powerCutRepeatAreaTbPercentage) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col>
          </van-row>
        </div>
        <div v-else>
          <!-- 第一行 -->
          <van-row>
            <van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0 bgimg-1  div-r">
                <img :src="getTotalNoticeLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-10">{{ dataTotal.assignQty
                }} </span> <span class="one">个</span></div>
                <div class="mb-3 common-f">停电通知派发</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.assignIncrease === null ? '--' : (Number(dataTotal.assignIncrease) >= 0 ? '增加'
                      + Math.abs(Number(dataTotal.assignIncrease)) : '减少' + Math.abs(Number(dataTotal.assignIncrease)))
                  }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.assignIncrease) >= 0 ? 'arrow-up' : (Number(dataTotal.assignIncrease) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.assignRisePct === null ? '--' : ((Number(dataTotal.assignRisePct) >= 0 ? '上涨' : '下降')
                      + Math.abs(Number(dataTotal.assignRisePct))) }}</span>%<van-icon name="down"
                      :class="Number(dataTotal.assignRisePct) > 0 ? 'arrow-up' : (Number(dataTotal.assignRisePct) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col>
            <van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0 bgimg-1  div-r">
                <img :src="getTotalCancelLogoSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-13">{{
                  dataTotal.cancelAssignQty }} </span> <span class="one">个</span>
                </div>
                <div class="mb-3 common-f">取消通知派发</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.cancelAssignIncrease === null ? '--' : (Number(dataTotal.cancelAssignIncrease) >= 0 ?
                      '增加' + Math.abs(Number(dataTotal.cancelAssignIncrease)) :
                      '减少' + Math.abs(Number(dataTotal.cancelAssignIncrease))) }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.cancelAssignIncrease) >= 0 ? 'arrow-up' : (Number(dataTotal.cancelAssignIncrease) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.cancelAssignRisePct === null ? '--' : ((Number(dataTotal.cancelAssignRisePct) >= 0 ?
                      '上涨' : '下降') + Math.abs(Number(dataTotal.cancelAssignRisePct))) }}</span>%<van-icon name="down"
                      :class="Number(dataTotal.cancelAssignRisePct) > 0 ? 'arrow-up' : (Number(dataTotal.cancelAssignRisePct) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col><van-col span="8" class="pd-1">
              <div class="t-box-item bgc-0 bgimg-1  div-r">
                <img :src="getTotalTimeoutSvgToBase64()" class="commonLogo">
                <div><span class="fs-large f-all color-11">{{
                  dataTotal.overTimeQty }} </span> <span class="one">个</span>
                </div>
                <div class="mb-3 common-f">派发超时</div>
                <div>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.overTimeIncrease === null ? '--' : (Number(dataTotal.overTimeIncrease) >= 0 ? '增加'
                      + Math.abs(Number(dataTotal.overTimeIncrease)) : '减少' + Math.abs(Number(dataTotal.overTimeIncrease)))
                  }}</span>个<van-icon name="down"
                      :class="Number(dataTotal.overTimeIncrease) >= 0 ? 'arrow-up' : (Number(dataTotal.overTimeIncrease) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                  <p class="common-f">同比<span class="f-all1">{{
                    dataTotal.overTimeRisePct === null ? '--' : ((Number(dataTotal.overTimeRisePct) >= 0 ? '上涨' :
                      '下降') + Math.abs(Number(dataTotal.overTimeRisePct))) }}</span>%<van-icon name="down"
                      :class="Number(dataTotal.overTimeRisePct) > 0 ? 'arrow-up' : (Number(dataTotal.overTimeRisePct) < 0 ? 'arrow-down' : 'arrow-horizontal ')" />
                  </p>
                </div>
              </div>
            </van-col>
          </van-row>
          <van-row>
            <van-col span="8" class="pd-1" style="height: 9rem;">

            </van-col>
            <van-col span="8" class="pd-1" style="height: 9rem;">

            </van-col>
            <van-col span="8" class="pd-1" style="height: 9rem;">

            </van-col>
          </van-row>

        </div>
      </div>
      <van-button v-if="env" @click="outLogin" color-white style="color: #447DDC;border: none;margin-bottom:10%">退出登录</van-button>

      <!-- ========================================================================== -->
    </div>


  </div>
  <!-- <van-loading v-else size="24px" type="spinner" vertical >加载中...</van-loading> -->
  <!-- 底部弹出 切换派发工程队 -->
  <van-popup v-model:show="showChangeAssignTeam" position="bottom" :style="{ height: '35%' }">
    <van-picker title="工程队" :columns="changeAssignTeamColumns" @confirm="onConfirmAssignTeam"
      @cancel="closeShowChangeAssignTeam" />
  </van-popup>

</template>

<script setup>
defineOptions({
  name: 'indexTabber',
})
import { Api } from "~/utils/api"
import { showToast, showDialog, Space, Button } from 'vant';
import { getTotalCancelLogoSvgToBase64, getTotalTimeoutSvgToBase64, getCancelReceiptWhiteLogoSvgToBase64, getReceiptLogoWhiteSvgToBase64, getTotalUserLogoSvgToBase64, getTotalRegionLogoSvgToBase64, getTotalRepeatLogoSvgToBase64, getTotalNoticeOverLogoSvgToBase64, getTotalNoticeSuccessLogoSvgToBase64, getTotalNoticeLogoSvgToBase64, getTotalLogoSvgToBase64, getMessageLogoSvgToBase64, getLogoSvgToBase64, getNoticeLogoSvgToBase64, getReceiptLogoSvgToBase64, getCancelReceiptLogoSvgToBase64 } from '~/utils/svgBase64'

//<!-- ========================================================================== -->



//<!-- ========================================================================== -->

// 通知消息
const taskNoticeArr = ref([

])
// 停电通知数据
const statisticsTotal = ref({
  releaseQty: null,
  executionQty: null,
  cancelQty: null,
  feedbackQty: null
})

statisticsTotal.value = {
  releaseQty: 0,
  executionQty: 0,
  cancelQty: 0,
  feedbackQty: 0
}
//停电通知回执
const receiptNotice = ref({
  undistributedQty: null,
  noFeedbackQty: null,
  refusedSignQty: null,
  timeOutQty: null,
  cancelUndistributedQty: null,
  cancelNoFeedbackQty: null
})

receiptNotice.value={
  undistributedQty: 0,
  noFeedbackQty: 0,
  refusedSignQty: 0,
  timeOutQty: 0,
  cancelUndistributedQty: 0,
  cancelNoFeedbackQty: 0
}

//数据汇总
const dataTotal = ref({
  taskSumQty: null,
  taskSumTb: null,
  taskSumTbPercentage: null,
  taskFinishedQty: null,
  taskFinishedTb: null,
  taskFinishedTbPercentage: null,
  taskCancelQty: null,
  taskCancelTb: null,
  taskCancelTbPercentage: null,
  taskUserSumQty: null,
  taskUserSumTb: null,
  taskUserSumTbPercentage: null,
  powerCutAreaQty: null,
  powerCutAreaTb: null,
  powerCutAreaTbPercentage: null,
  powerCutRepeatAreaQty: null,
  powerCutRepeatAreaTb: null,
  powerCutRepeatAreaTbPercentage: null
})
const env = ref(import.meta.env.VITE_NODE_ENV !== 'production')
const state = useGlobalState()
state.value.tabberNum=0
const router = useRouter()

const scanAssignMessage = ref({})
const showChangeAssignTeam = ref(false)
const changeAssignTeamColumns = ref([])

onMounted(async () => {
   state.value.showTabber = true
  if(state.value.token){
    initTaskNotice()
    if(!state.value.engineersTeamId){
      initStatisticsTotal()
    }
    initAssignTeamColumns()
    initReceiptNoticeFn()
    initDataTotal()
  }

})
onBeforeUnmount(()=>{
  state.value.showTabber = false
})
const initAssignTeamColumns = () => {
  const { data, execute } = usePost(Api.engineersTeamList, {});
  execute().then(() => {
    data?.value?.data.forEach((item) => {
      item.text = item.name
      item.value = item.id
    })
    changeAssignTeamColumns.value = data?.value?.data;
  });
}
// 通知消息
const initTaskNotice = () => {
  const params = {
    pageIndex: 1,
    pageSize: 10,
    condition: {}
  }
  const { data, execute } = usePost(Api.taskNoticePage, params)
  execute().then(() => {
    taskNoticeArr.value = data.value.data?.data
  })
}
// 停电通知初始化
const initStatisticsTotal = () => {
  const { data, execute } = usePost(Api.powerCutNotice, {})
  execute().then(() => {
    statisticsTotal.value.releaseQty = data.value.data?.releaseQty
    statisticsTotal.value.executionQty = data.value.data?.executionQty
    statisticsTotal.value.cancelQty = data.value.data?.cancelQty
    statisticsTotal.value.feedbackQty = data.value.data?.feedbackQty
  })
}

// 停电通知回执初始化
const initReceiptNoticeFn = () => {
  const { data, execute } = usePost(Api.statisticsTotal, {})
  execute().then(() => {
    receiptNotice.value.undistributedQty = data.value.data?.undistributedQty
    receiptNotice.value.noFeedbackQty = data.value.data?.noFeedbackQty
    receiptNotice.value.refusedSignQty = data.value.data?.refusedSignQty
    receiptNotice.value.timeOutQty = data.value.data?.timeOutQty
    receiptNotice.value.cancelUndistributedQty = data.value.data?.cancelUndistributedQty
    receiptNotice.value.cancelNoFeedbackQty = data.value.data?.cancelNoFeedbackQty
  })
}

// 数据汇总初始化
const initDataTotal = () => {
  if (!state.value.engineersTeamId) {
    const { data, execute } = usePost(Api.dataTotal, {})
    execute().then(() => {
      dataTotal.value.taskSumQty = data.value.data?.taskSumQty
      dataTotal.value.taskSumTb = data.value.data?.taskSumTb
      dataTotal.value.taskSumTbPercentage = data.value.data?.taskSumTbPercentage

      dataTotal.value.taskFinishedQty = data.value.data?.taskFinishedQty
      dataTotal.value.taskFinishedTb = data.value.data?.taskFinishedTb
      dataTotal.value.taskFinishedTbPercentage = data.value.data?.taskFinishedTbPercentage

      dataTotal.value.taskCancelQty = data.value.data?.taskCancelQty
      dataTotal.value.taskCancelTb = data.value.data?.taskCancelTb
      dataTotal.value.taskCancelTbPercentage = data.value.data?.taskCancelTbPercentage

      dataTotal.value.taskUserSumQty = data.value.data?.taskUserSumTotalResource.taskUserSumQty
      dataTotal.value.taskUserSumTb = data.value.data?.taskUserSumTotalResource.taskUserSumTb
      dataTotal.value.taskUserSumTbPercentage = data.value.data?.taskUserSumTotalResource.taskUserSumTbPercentage

      dataTotal.value.powerCutAreaQty = data.value.data?.taskUserSumTotalResource.powerCutAreaQty
      dataTotal.value.powerCutAreaTb = data.value.data?.taskUserSumTotalResource.powerCutAreaTb
      dataTotal.value.powerCutAreaTbPercentage = data.value.data?.taskUserSumTotalResource.powerCutAreaTbPercentage

      dataTotal.value.powerCutRepeatAreaQty = data.value.data?.taskUserSumTotalResource.powerCutRepeatAreaQty
      dataTotal.value.powerCutRepeatAreaTb = data.value.data?.taskUserSumTotalResource.powerCutRepeatAreaTb
      dataTotal.value.powerCutRepeatAreaTbPercentage = data.value.data?.taskUserSumTotalResource.powerCutRepeatAreaTbPercentage
    })
  } else {
    const { data, execute } = usePost(Api.engineersSummary, {})
    execute().then(() => {
      dataTotal.value.assignQty = data.value.data?.assignQty
      dataTotal.value.assignIncrease = data.value.data?.assignIncrease
      dataTotal.value.assignRisePct = data.value.data?.assignRisePct

      dataTotal.value.cancelAssignQty = data.value.data?.cancelAssignQty
      dataTotal.value.cancelAssignIncrease = data.value.data?.cancelAssignIncrease
      dataTotal.value.cancelAssignRisePct = data.value.data?.cancelAssignRisePct

      dataTotal.value.overTimeQty = data.value.data?.overTimeQty
      dataTotal.value.overTimeIncrease = data.value.data?.overTimeIncrease
      dataTotal.value.overTimeRisePct = data.value.data?.overTimeRisePct
    })
  }

}

function closeShowChangeAssignTeam() {
  showChangeAssignTeam.value = false;
}
function onConfirmAssignTeam(selected) {

  const { data, execute } = usePut(Api.taskUserBatchAssign, { taskUserIdList: [scanAssignMessage.value?.resultStr], engineersTeamId: selected.selectedOptions[0].value })
  execute().then(() => {
    closeShowChangeAssignTeam()
    if (data.value?.code == 0) {
      showDialog({
        title: '提示',
        message: '派发成功',
      }).then(() => {
        // router.push({path: '/taskUserAssign',query: {taskUserId:scanAssignMessage.value?.resultStr}})
      });
    }
  })

}
function cancelAssign() {
  const { data, execute } = usePut(Api.taskUserBatchAssign, { taskUserIdList: [scanAssignMessage.value?.resultStr] })
  execute().then(() => {
    if (data.value?.code == 0) {
      showDialog({
        title: '提示',
        message: '派发成功',
      }).then(() => {
        // router.push({path: '/taskUserAssign',query: {taskUserId:scanAssignMessage.value?.resultStr}})
      });
    }
  })
}

function scan() {
  wx.scanQRCode({
    dec: 'scanQRCode desc',
    needResult: 1, // 默认为0，扫描结果由i国网处理，1则直接返回扫描结果，
    scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
    success: function (res) {
      scanAssignMessage.value = res
      const { data, execute } = useGet(Api.taskUserDetail, res.resultStr)
      execute().then(() => {
        let resData = data?.value?.data
        //taskExecuteStatus 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-取消中，161-已完成，162-已终结
        if (resData.task.taskExecuteStatus.executeStatus == '150') {
          // 取消通知派发状态：410-未派发，420-已派发，430-已反馈，440-不派发
          if (resData.cancelAssignStatus == '410') {
            if (state.value.engineersTeamId == '') {
              cancelAssign()
            } else {
              showDialog({
                title: '提示',
                message: '该用户没有派发权限',
              }).then(() => {

              });
            }
          } else if (resData.cancelAssignStatus == '420' || resData.cancelAssignStatus == '430') {
            router.push({ path: '/taskUserfeedback', query: { taskUserId: res.resultStr, taskExecuteStatus: '150' } })
          } else if (resData.cancelAssignStatus == '440') {
            showDialog({
              title: '提示',
              message: '该用户取消通知派发状态为不派发',
            }).then(() => {

            });
          }
        } else {
          //taskExecuteStatus 执行状态：110-待提交，120-待派发，130-执行中，140-已反馈，150-取消中，161-已完成，162-已终结
          if (resData.task.taskExecuteStatus.executeStatus == '120' || resData.task.taskExecuteStatus.executeStatus == '130' || resData.task.taskExecuteStatus.executeStatus == '140') {
            if (resData.task.taskExecuteStatus.executeStatus == '140') {
              //执行状态为已反馈140的说明所有用户已经派发和反馈过了 所以还能继续反馈
              router.push({ path: '/taskUserfeedback', query: { taskUserId: res.resultStr, taskExecuteStatus: '140' } })
            } else {
              // assignStatus派发状态：210-未派发，220-已派发，230-已反馈，240-不派发
              if (resData.assignStatus == '210') {
                if (state.value.engineersTeamId == '') {
                  showChangeAssignTeam.value = true;
                } else {
                  showDialog({
                    title: '提示',
                    message: '该用户没有派发权限',
                  }).then(() => {

                  });
                }
              } else if (resData.assignStatus == '220' || resData.assignStatus == '230') {
                router.push({ path: '/taskUserfeedback', query: { taskUserId: res.resultStr, taskExecuteStatus: resData.task.taskExecuteStatus.executeStatus } })
              } else if (resData.assignStatus == '240') {
                showDialog({
                  title: '提示',
                  message: '该用户停电通知派发状态为不派发',
                }).then(() => {

                });
              }
            }
          } else {
            showDialog({
              title: '提示',
              message: '通知单执行状态为待提交、已完成、已终结的不能进行操作',
            }).then(() => {

            });
          }

        }
      })
    },
    fail: function(err) {
      console.log('scan err',err); // 打印错误信息
    }
  });

}
function outLogin() {
  state.value.token = ''
  state.value.flag = false
  state.value.engineersTeamId = ''
  router.push({ path: '/temporaryLogin' })
}

</script>

<style  scoped>
::-webkit-scrollbar {
  display: none;
}
.notice-t1 {

  font-family: Noto Sans SC;
  font-size: 12px;
  font-weight: 6500;
  line-height: 16px;
  /* letter-spacing: 1px; */
  text-align: left;

}

.notice-t2 {
  font-family: Roboto;
  font-size: 12px;
  font-weight: 400;
  line-height: 16px;
  letter-spacing: 0em;
  text-align: right;
  color: #00000099;

}

.notice-b {
  height: 36px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2px;
}

.common-f {
  transform: scale(.9);
  margin-left: -5px;
}

.one {
  display: inline-block;
  transform: scale(.8);
}

.commonLogo {
  position: absolute;
  bottom: 0;
  right: 0;
}

.div-r {
  padding-left: 6px !important;
  position: relative;
}

.messageLogo {
  display: inline-block;
  transform: scale(1.2);
  margin-top: -4px;
}

.rp-wrap2 {
  padding: 1vw 0;
  height: 64px;
  border-radius: 0 4px 4px 0;
}

.rp-row {
  border-right: 1px solid #E8E8E8;
  height: 55px;
}

.rp-wrap1 {
  padding: 1vw 0;
  height: 64px;
  border-radius: 4px 0 0 4px;
}

.cancelLogo {
  display: inline-block;
  transform: scale(1.1);
  margin-top: -4px;
  margin-right: 6px;
}

.receiptLogo {
  display: inline-block;
  transform: scale(1.1);
  margin-top: -4px;
}

.f-all {
  font-weight: 700;
  font-size: 18px;
}

.f-all1 {
  font-weight: 700;
  font-size: 14px;
}

.pd-c-1 {
  padding: 2vw;
}

.pd-c-0 {
  padding: 2% 1%;
}

.noticeLogo {
  display: inline-block;
  transform: scale(1.2);
  margin-right: 6px;
  margin-top: -4px;
}

.h5-t {
  transform: scale(.7);
  margin-right: 9px;
}

.fs20 {
  font-size: 16px;
  margin-left: -8px;
  font-family: 'Noto Sans SC';
  font-weight: 400;
}

.box-title {
  text-align: left;
  padding-left: 2%;
  margin-left: 1%;
  font-weight: 500;
  font-size: 14px;
  color: white;
  font-family: 'Noto Sans SC';
}

.bt-1 {
  font-family: 'Noto Sans SC';
  line-height: 20px;
  letter-spacing: 0em;
  text-align: left;
  color: #011c17d0;
  font-weight: 600;
}

.box-notice {
  font-size: 12px;
  padding: 1%;
  border-radius: 8px;

}

.bs-1 {
  box-shadow: 0px 4px 0px 0px #7FC7BA;
}

.bs-2 {
  box-shadow: 0px 4px 0px 0px #1da4e3;
}

.bs-3 {
  box-shadow: 0px 4px 0px 0px #FFA434;
}

.bs-4 {
  box-shadow: 0px 4px 0px 0px #447DDC;
}

.bgc-0 {
  background-color: #FFFFFF;
}

.bgc-1 {
  background-color: #7FC7BA;
}

.bgc-2 {
  background-color: #048D74;
}

.bgc-3 {
  background-color: #FFA434;
}

.bgc-4 {
  background-color: #447DDC;
}

.bgc-5 {
  background-color: white;
}

.bgc-6 {
  background-color: #F9F9F9
}

.bgc-7 {
  background-color: #02A7F0
}

.bgc-8 {
  background-color: #00BFBF
}

.bgc-9 {
  background-color: #F59A23
}

.bgc-10 {
  background-color: #67CAF6
}

.bgc-11 {
  background-color: #7FDFDF
}

.bgc-12 {
  background-color: #F9C27B
}

.color-0 {
  color: white;
}

.color-1 {
  color: #F79A23;
}

.color-2 {
  color: #81DDFB;
}

.color-3 {
  color: #FF006B;
}

.color-4 {
  color: red;
}

.color-5 {
  color: #00000099;
}

.color-6 {
  color: #03715D;
}

.color-7 {
  color: #1da4e3;
}

.color-8 {
  color: #FFA434;
}

.color-9 {
  color: #447DDC;
}

.color-10 {
  color: #275FBB;
}

.color-11 {
  color: #F57F17;
}

.color-12 {
  color: #BC2F32;
}

.color-13 {
  color: #3D7329;
}

.receipt-box {
  margin: 1vw 0vw 1vw 0vw;
  padding-top: 1vw;
}

.rp-box {
  font-size: 12px;
  padding: 1vw;
}

.rp-box-num {
  font-size: 15px;
  font-weight: bold;
}

.mb-1 {
  margin-bottom: 1vw;
}

.pd-1 {
  padding: 1.5vw;
}

.h-30 {
  height: 40vw;
}

.cancel-box {
  margin: 1vw 0;
  padding-top: 1vw;
}

.text-box {
  margin: 1vw 0 3vw;
  padding-top: 1vw;
}

.text-content {
  font-size: 13px;
  padding: 1vw;
  text-align: left;
  height: 40vw;
  overflow: scroll;
}

.total-box {
  margin: 1vw 0;
  padding-top: 1vw;
  color: white;
  text-align: left;
  color: #00000099;
}

.t-box-item {
  font-size: 12px;
  padding: 1vw;
  border-radius: 4px;
}

.fs-large {
  font-size: large;
}

.arrow-up {
  transform: rotate(180deg);
  color: red;
}

.arrow-down {
  color: green;
}

.arrow-horizontal {
  color: transparent;
}</style>
