<script setup>
defineOptions({
  name: 'taskUserAssign',
})
import { Api } from '../utils/api'
import {taskExecuteStatusMap} from '../composables/map'
import { showToast,showDialog } from 'vant';
// import '../../public/iscp/jweixin-1.2.0'

const router = useRouter()
const route = useRoute()
const taskReasonList = ref([]);
const userTypeStyle1 = ref('userType')
const userTypeStyle2 = ref('userType')
const userTypeStyle3 = ref('userType')
const assignStatus1 = ref('userType')
const assignStatus2 = ref('userType')
const activeName = ref('type');
const detailData = ref({
					unassignedQty: 20,
					type: -40199429,
					id: 1,
					ange: "elitsuntelitsuntelitsuntelitsuntelitsuntelitsuntelitsuntelit",
					boolNotifyMedia: 15374158,
					agreedQty: 210,
					updateBy: 72313525,
					jobContent: "consequat",
					createBy: -86648962,
					startTime: "1945-07-29 16:46:52",
					updateDate: "1945-07-29 16:46:52",
					endTime: "1994-12-10T18:20:10.459Z",
					attribute3: "laborum ex nulla elit cupidatat",
					taskCode: "1234122341",
					attribute1: "consectetur",
					status: -98435955,
					advanceWarning: 0,
					attribute2: "enim in",
					createDate: "1963-12-05T21:56:07.329Z",
					assignedBy: -93746527,
					unitName: "nulla",
					cancelledQty: 10,
          ranges: '先缴纳几年级擦可能性就按',
					remark: "Excepteur adipisicing qui pariatur",
					deviceName: "dolor enim aute",
					unsignedQty: 210,
					lineName: "laboris in magna",
					reason: -6140884,
					assPlan: "fugiat aliqua eiusmod",
					assignedQty: 100,
					location: "sed ut enim minim",
					rejectedQty: 120,
					executeStatusId: 1,
					version: -44206842,
					stationName: "exercitation sed do"
				})
const userDataList = ref([])
const stagingData = ref({})
const showFilter = ref(false)
onMounted(async () => {
  //请求详情
  init(true)
  initList()
})
const initList = async ()=>{
  const { data, execute } = usePost(Api.lovList, {
    code: "task_reason",
  });
  execute().then(() => {
    taskReasonList.value = data.value.data[0]?.lovLineList;
  });
}

const init = async(init,taskUserId='')=>{
  //请求用户详情
  const {data,execute } = useGet(Api.taskUserDetail, init ? route.query.taskUserId : taskUserId)
  execute().then(()=>{
    console.log(data.value.data,'data.value.data')
    if(init){
      // initTaskDetail(data.value?.data.taskId)
      detailData.value = data.value?.data.task
      userDataList.value.push(data.value?.data)
    }else{
      console.log('stagingData暂存详情数据')
      //stagingData暂存详情数据
      stagingData.value = data.value?.data
      if(data.value?.data.taskId == detailData.value.id){
        console.log('继续扫描扫出来的用户***是***现在的任务下面的')

        //继续扫描扫出来的用户***是***现在的任务下面的
        assignTaskUser(taskUserId)
      }else{
        console.log('继续扫描扫出来的用户***不是***现在的任务下面的，需要弹提示')
        //继续扫描扫出来的用户***不是***现在的任务下面的，需要弹提示
        showConfirmDialog({
          title: '提示',
          message:
            '当前扫的用户和之前的用户不是同一任务，是否继续执行',
        })
          .then(() => {
            userDataList.value = [];
            // initTaskDetail(stagingData.value.taskId)
            assignTaskUser(taskUserId)
          })
          .catch(() => {

          });
      }
    }
  })
}
const assignTaskUser = async(taskUserId)=>{
  const {data,execute } = usePut(Api.taskUserBatchAssign,{taskUserIdList: [taskUserId],type: detailData.value?.taskExecuteStatus.executeStatus == 150 ? 3 : 1})
  execute().then(()=>{
      if(data.value?.code == 0){
        showDialog({
          title: '提示',
          message: '派发成功',
        }).then(() => {
          console.log('派发成功')
          detailData.value = stagingData.value.task
          userDataList.value.push(stagingData.value)
        });
      }
  })
}
// const initTaskDetail = async(taskId)=>{
//   //请求任务详情
//   const {data,execute } = useGet(Api.taskDetail, taskId)
//   execute().then(()=>{
//     console.log(data.value.data,'data.value.data')
//     detailData.value = data.value?.data
//   })
// }
function scanAssignMethod(){
  wx.scanQRCode({
    dec: 'scanQRCode desc',
    needResult: 1, // 默认为0，扫描结果由i国网处理，1则直接返回扫描结果，
    scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
    success: function(res) {
      console.log(res,'scanAssignRes')
      init(false,res.resultStr)
    },
    error: function(res) {
      console.log(res,'errorRes')
        if (res.errMsg.indexOf('function_not_exist') > 0) {
            alert('版本过低请升级')
        }
    }
  });
}

console.log(route,'route')
console.log(route.query,'route====')
function backFn(){
  router.back()
}
</script>

<template>
  <div>
    <div flex items-center m-b-6>
      <van-icon name="arrow-left" color-white font-500 style="font-size:20px" @click="backFn"/>
      <div font-500 color-white m-l-5 style="font-size:20px">停电通知派发</div>
    </div>
    <div flex items-center m-b-4>
      <!-- <c-image   base64-url="../../public/img/stop.png"/> -->
      <div p-l-2 font-500 c-white>停电通知</div>
    </div>
    <div bg-white m-b-4  p-t-5 p-b-5>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27>通知编号：</div>
        <div class="valueText" w-70>{{detailData.taskCode}}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27>停电时间：</div>
        <div class="valueText" w-70>{{detailData.startTime}}至{{detailData.endTime}}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27>停电范围：</div>
        <div class="valueText" w-70>{{detailData.ranges}}</div>
      </div>
      <div flex m-b-2 justify-between>
        <div class="labelText" w-27>停电原因：</div>
        <div class="valueText" w-70>{{JSON.parse(JSON.stringify(taskReasonList)).filter(
                    (val) => detailData.reason == val.value
                  )[0]?.name}}</div>
      </div>
    </div>
  </div>
  <div>
    <div flex justify-between>
      <div flex items-center>
        <c-image   base64-url="../../public/img/userListLogo.png"/>
        <div style="color: #005A50;" font-500 p-l-2>停电用户</div>
      </div>
      <!-- <div flex items-center @click="openFilter">
        <van-icon name="filter-o" size="20"/>
        <div font-500> 筛选</div>
      </div> -->
    </div>
    <div style="font-size:14px;font-weight: 400;color:#646464;" m-l--30 m-b-4>
      <span>已派发{{detailData.assignedQty}}人</span>
      <span>|未派发{{detailData.unassignedQty}}人</span>
    </div>
  </div>
  <div v-for="item in userDataList" :key="item.id"  bg-white m-b-4 p-b-2 relative>
          <div h-10 b-b-1 b-b-gray-4 flex items-center justify-between m-b-2>

            <div flex m-l-3 w-65>
              <div class="labelText">编号：</div>
              <div class="valueText">{{item.receiptCode}}</div>
            </div>
            <div class="labelText" m-r-3  v-if="detailData.taskExecuteStatus?.executeStatus == 150">{{item.cancelAssignStatus == 410 ? '未派发' :item.cancelAssignStatus == 420 ? '已派发' : item.cancelAssignStatus == 430 ? '已取消' : '不派发'}}</div>
            <div class="labelText" m-r-3 v-else>已派发</div>

          </div>
          <div>
            <div flex m-b-2 justify-between>
              <div class="labelText" w-27>客户名称：</div>
              <div class="valueText" w-70>{{item.customerName}}</div>
            </div>
            <div flex m-b-2 justify-between>
              <div class="labelText" w-27>客户地址：</div>
              <div class="valueText" w-70>{{item.customerAddress}}</div>
            </div>
            <div flex m-b-2 p-l-3>
              <div flex>
                <div class="labelText" m-r-2>联系方式：</div>
                <div class="valueText" w-25>{{item.phone}}</div>
              </div>
              <div flex>
                <div class="labelText">用户类型：</div>
                <div class="valueText" >{{item.userType == 1 ? '低非' : item.userType == 2 ? '高大' : item.userType == 3 ? '居委' : '居委'}}</div>
              </div>
            </div>
            <div flex m-b-2 p-l-3>
              <div flex>
                <div class="labelText" m-r-2>电系编号：</div>
                <div class="valueText" w-10>{{item.electricalNumber}}</div>
              </div>
              <div flex>
                <div class="labelText">所属台区：</div>
                <div class="valueText" >{{item.region}}</div>
              </div>
            </div>
        </div>
  </div>
  <div fixed bottom-0 bg-white  h-8 w-85>
    <div flex justify-center>
      <n-button @click="scanAssignMethod" style="background-color:white;border:1px solid #005A50;color:#005A50" w-40 h-8 flex justify-center items-center>继续扫描</n-button>
      <!-- <n-button style="background-color:#005A50" color-white w-40 h-8 flex justify-center items-center>全部派发</n-button> -->
    </div>
  </div>


</template>
<style>
  .userType{
    display: block;
    width: 92px;
    height: 32px;
    font-size: 16px;
    line-height: 32px;
    margin-right: 12px;
    margin-bottom: 12px;
    background: #D9D9D9;
  }
  .userTypeActive{
    display: block;
    width: 92px;
    height: 32px;
    font-size: 16px;
    line-height: 32px;
    color: white;
    margin-right: 12px;
    margin-bottom: 12px;
    background: #005A50;
  }
  .labelText{
    font-size:14px;
    color: #858585;
  }
  .valueText{
    font-size:14px;
    color: #333333;
    text-align: left;
  }
</style>
