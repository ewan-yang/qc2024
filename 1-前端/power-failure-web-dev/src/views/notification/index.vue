<template>
  <n-card class="mb-4">
    <div class="font-700 m-b-2 b-l-#033FB4 b-l-5 p-l-2" style="font-size: 18px">
    停电计划查询
    </div>
    <BasicForm
      @register="register"
      @submit="handleSubmit"
      @reset="handleReset"
      style="margin-left: -2rem"
    >
    </BasicForm>
  </n-card>
  <n-card class="mb-4">
    <!-- *************************停电计划列表*************************** -->
    <BasicTable
      ref="actionRef"
      tableName="task"
      :columns="columns"
      :bordered="false"
      :single-line="false"
      :request="loadDataTable"
      :row-key="row => row.id"
      :action-column="actionColumn"
      :pagination='{
        pageSize:30,
      }'
      :max-height="500"
      :scroll-x="1200"
      :row-class-name="selectedRowStyleClass"
      :row-props="selectedRowMethod"
      :checked-row-keys="tableSelectKeys"
      @update:checked-row-keys="onCheckedRow"
      :on-update:sorter="handleSortChange"
    >
      <template #tableTitle>
        <n-button type="primary" class="mr-4" @click="openModal('addNew')">
          <template #icon>
            <n-icon size="18" class="top--10%">
              <icon-ant-design:plus-outlined />
            </n-icon>
          </template>
          新建
        </n-button>
        <n-button class="mr-4" type="primary" ghost @click="openModal('endTask')">
          <template #icon>
            <n-icon size="18" class="top--10%">
              <icon-ant-design:minus-circle-outlined />
            </n-icon>
          </template>
          取消
        </n-button>
    <n-button class="mr-4" type="primary" ghost @click="openModal('backEndTask')">
        <template #icon>
            <n-icon size="18" class="top--10%">
                <icon-ant-design:minus-circle-outlined />
            </n-icon>
        </template>
        撤销取消
    </n-button>
      </template>
    </BasicTable>
    <!-- *************************取消任务模态框*************************** -->

    <n-modal
      v-model:show="showCancelTaskModal"
      preset="dialog"
      title="Dialog"
      :style="{ width: '600px' }"
      :on-after-leave="cancelModalCancel"
    >
      <template #header>
        <div>取消任务</div>
      </template>
      <n-form
        ref="cancelModalRef"
        :model="cancelTaskModalData"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-grid x-gap="12" cols="1">
          <n-grid-item
            ><n-form-item label="派发取消通知：" path="boolAssignCancelNotice">
              <n-radio-group v-model:value="cancelTaskModalData.boolAssignCancelNotice" name="radiogroup">
                <n-space>
                  <n-radio
                    v-for="item in [
                      { value: 1, label: '是' },
                      { value: 0, label: '否' }
                    ]"
                    :key="item.value"
                    :value="item.value"
                    :disabled="cancelTaskModalExecuteStatus == '120'"
                  >
                    {{ item.label }}
                  </n-radio>
                </n-space>
              </n-radio-group>
            </n-form-item></n-grid-item
          >
        </n-grid>
        <n-grid x-gap="12" cols="1">
          <n-grid-item
            ><n-form-item label="取消理由：" path="cancelReason">
              <n-select
                v-model:value="cancelTaskModalData.cancelReason"
                placeholder="请选择取消理由"
                :options="cancelReasonList"
              /> </n-form-item
          ></n-grid-item>
        </n-grid>
      </n-form>
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="cancelTaskModalSubmit"> 确认 </n-button>
          <n-button class="m-l-4" @click="cancelModalCancel"> 取消 </n-button>
        </div>
      </template>
    </n-modal>
    <!-- 撤销取消任务模态框 -->
    <n-modal v-model:show="showBackCancelTaskModal" :on-after-leave="cancelModalCancel" preset="dialog"
      title="" :style="{ width: '600px' }">
      <template #header>
        <div>撤销取消</div>
      </template>
      <div>
      <div style="font-size: 16px;margin-left:5px">确认撤销取消任务吗？</div>
      </div>
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="backCancelTaskModalSubmit"> 确认 </n-button>
          <n-button class="m-l-4" @click="cancelModalCancel"> 取消 </n-button>
        </div>
      </template>
    </n-modal>
    <!-- *************************停电计划编辑 新增 详情抽屉*************************** -->
    <n-drawer ref="modalRef" v-model:show="showModal" :on-after-leave="cancelDrawerButton" style="width: 90%">
      <n-drawer-content>
        <template #header>
          <div class="flex justify-between items-center">
            <div class="font-700">
              {{
                modalType == 'isAdd'
                  ? '新增停电计划'
                  : modalType == 'isEdit'
                  ? '修改停电计划'
                  : modalType == 'isDetail'
                  ? '停电计划详情'
                  : ''
              }}
            </div>
            <div class="m-r-12">
              <n-button v-if="modalType != 'isDetail'" class="mr-3" type="primary" @click="modalSubmitMethod(1)"
                >确认</n-button
              >
              <n-button
                v-if="
                  modalType == 'isAdd' ||
                  (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus == '110')
                "
                class="mr-3"
                color="#BAD8FB"
                style="color: black"
                @click="modalSubmitMethod(0)"
                >保存</n-button
              >
              <n-button type="primary" ghost @click="cancelDrawerButton">取消</n-button>
            </div>
          </div>
        </template>
        <template #default>
          <n-card class="m-b-4">
            <div v-if="modalType == 'isDetail'" style="font-size: 18px">
              <div v-if="modelData?.taskExecuteStatus?.executeStatus != '150'" class="flex b-l-#033FB4 b-l-5 p-l-2">
                停电计划内容（

                <span style="color: #f59a86"
                  >未派发{{ modelData.unassignedQty == null ? 0 : modelData.unassignedQty }}</span
                >，<span style="color: #66a530"
                  >已派发{{ modelData.assignedQty == null ? 0 : modelData.assignedQty }}</span
                >，<span style="color: red"
                  >不派发{{ modelData.cancelledQty == null ? 0 : modelData.cancelledQty }}</span
                >

                ）
              </div>
              <div v-else class="b-l-#033FB4 b-l-5 p-l-2" style="font-size: 18px">
                取消停电计划内容（

                <span style="color: #f59a86"
                  >未派发{{ modelData.cancelUnassignedQty == null ? 0 : modelData.cancelUnassignedQty }}</span
                >，<span style="color: green"
                  >已派发{{ modelData.cancelAssignedQty == null ? 0 : modelData.cancelAssignedQty }}</span
                >

                ）
              </div>
            </div>
            <n-form
              ref="formRef"
              :model="modelData"
              :rules="rules"
              label-width="auto"
              class="py-4 max-h-2xl pr-1"
              label-placement="left"
            >
              <n-grid x-gap="12" cols="4">
                <n-grid-item
                  ><n-form-item label="关联停电计划:" path="planItemId">
                    <!-- <n-input
                      v-model:value="modelData.assPlan"
                      disabled
                      placeholder=""
                    />  -->
                    <n-select
                      v-model:value="modelData.planItemId"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请选择停电计划"
                      label-field="projectName"
                      value-field="id"
                      :options="planItemList"
                      @click="showPlanItemTable = true"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item v-if="modalType != 'isAdd'"
                  ><n-form-item label="停电计划编号:" path="taskCode">
                    <n-input v-model:value="modelData.taskCode" disabled placeholder="" /> </n-form-item
                ></n-grid-item>
                <n-grid-item>
                  <n-form-item label="通知来源:" path="taskSource">
                    <n-select
                      v-model:value="modelData.taskSource"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请选择通知来源"
                      :options="taskSourceUserList"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item>
                  <n-form-item label="是否通知媒体:" path="boolNotifyMedia">
                    <n-select
                      v-model:value="modelData.boolNotifyMedia"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请选择是否通知媒体"
                      :options="[
                        { label: '否', value: 0 },
                        { label: '是', value: 1 }
                      ]"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="停电时间:" path="startTime">
                    <n-date-picker
                      v-model:formatted-value="modelData.startTime"
                      class="w-75"
                      format="yyyy-MM-dd HH:mm"
                      value-format="yyyy-MM-dd HH:mm"
                      :disabled="
                        modalType == 'isDetail' ||
                        modelData?.taskExecuteStatus?.executeStatus == '161' ||
                        modelData?.taskExecuteStatus?.executeStatus == '162'
                      "
                      :is-date-disabled="
                        ts => {
                          return ts < new Date().setHours(0, 0, 0, 0);
                        }
                      "
                      type="datetime"
                    /> </n-form-item
                ></n-grid-item>

                <n-grid-item>
                  <n-form-item :label="`送电时间:`" path="endTime">
                    <n-date-picker
                      v-model:formatted-value="modelData.endTime"
                      class="w-75"
                      format="yyyy-MM-dd HH:mm"
                      value-format="yyyy-MM-dd HH:mm"
                      :disabled="
                        modalType == 'isDetail' ||
                        modelData?.taskExecuteStatus?.executeStatus == '161' ||
                        modelData?.taskExecuteStatus?.executeStatus == '162'
                      "
                      :is-date-disabled="
                        ts => {
                          return ts < new Date(modelData.startTime).setHours(0, 0, 0, 0);
                        }
                      "
                      :is-time-disabled="
                        ts => {
                          return {
                            isHourDisabled: hour => {
                              return (
                                new Date(ts).setHours(0, 0, 0, 0) ==
                                  new Date(modelData.startTime).setHours(0, 0, 0, 0) &&
                                hour <= new Date(modelData.startTime).getHours()
                              );
                            },
                            isMinuteDisabled: (minute, hour) => {
                              return false;
                            }
                          };
                        }
                      "
                      type="datetime"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item>
                  <n-form-item label="停电类型:" path="type">
                    <n-select
                      v-model:value="modelData.type"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请选择停电类型"
                      :options="powerFailureTypeList"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item>
                  <n-form-item label="停电原因:" path="reason">
                    <n-select
                      v-model:value="modelData.reason"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请选择停电原因"
                      :options="powerFailureReasonList"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="变电站名称:" path="stationName">
                    <n-input
                      v-model:value="modelData.stationName"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入变电站名称"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="设备名称:" path="deviceName">
                    <n-input
                      v-model:value="modelData.deviceName"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入设备名称"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="线路名称:" path="lineName">
                    <n-input
                      v-model:value="modelData.lineName"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入线路名称"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="停电范围:" path="ranges">
                    <n-input
                      v-model:value="modelData.ranges"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入停电范围"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="停电位置:" path="location">
                    <n-input
                      v-model:value="modelData.location"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入停电位置"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="工作内容:" path="jobContent">
                    <n-input
                      v-model:value="modelData.jobContent"
                      type="textarea"
                      rows="1"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入工作内容"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="备注:" path="remark">
                    <n-input
                      v-model:value="modelData.remark"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入备注"
                    /> </n-form-item
                ></n-grid-item>
                <n-grid-item
                  ><n-form-item label="配网办编号:" path="outId">
                    <n-input
                      v-model:value="modelData.outId"
                      :disabled="
                        modalType == 'isDetail' ||
                        (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                      "
                      placeholder="请输入配网办编号"
                    /> </n-form-item
                ></n-grid-item>
                <!-- 新建上传附件 -->
                <n-grid-item v-if="modalType == 'isAdd'"
                  ><n-form-item label="附件:" path="fujian">
                    <n-upload :on-change="onIsAddChangeUploadData" multiple>
                      <n-button>上传文件</n-button>
                    </n-upload>
                  </n-form-item></n-grid-item
                >
                <!-- 编辑上传附件 -->
                <n-grid-item v-if="modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus == '110'"
                  ><n-form-item label="附件:" path="fujian">
                    <n-upload
                      :headers="{
                        Authorization: `Bearer ${localStg.get('token')}`
                      }"
                      :on-change="onIsEditChangeUploadData"
                      :on-preview="onIsEditDownUploadData"
                      multiple
                      :default-file-list="fileListUpload"
                    >
                      <n-button>上传文件</n-button>
                    </n-upload>
                  </n-form-item>
                </n-grid-item>
                <!-- 详情上传附件 -->
                <n-grid-item
                  v-if="
                    modalType == 'isDetail' ||
                    (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
                  "
                  ><n-form-item label="附件:" path="fujian">
                    <n-upload abstract>
                      <div
                        v-for="(item, index) in fileListUpload"
                        :key="index"
                        style="cursor: pointer; height: 20px; padding-bottom: 2px; margin-right: 10px"
                        @click="onIsEditDownUploadData(item)"
                      >
                        <a :href="item.url" onclick="return false" style="color: green">{{ item.name }}、</a>
                      </div>
                    </n-upload>
                  </n-form-item>
                </n-grid-item>
              </n-grid>
            </n-form>
          </n-card>
          <n-card>
            <div class="flex justify-between h-12 items-center">
              <div>
                <h1 class="font-500" style="font-size: 18px">
                  <div v-if="modelData?.taskExecuteStatus?.executeStatus != '150'" class="b-l-#033FB4 b-l-5 p-l-2">
                    停电通知单
                    <!-- <span v-if="
                      modalType == 'isAdd' ||
                      (modalType == 'isEdit' &&
                        modelData?.taskExecuteStatus?.executeStatus == '110')
                    ">（黄色背景为重复停电用户）</span> -->
                    <span v-if="modalType == 'isDetail'"
                      >（<span style="color: red"
                        >拒签{{ modelData.rejectedQty == null ? 0 : modelData.rejectedQty }}</span
                      >，<span style="color: #66a530"
                        >同意{{ modelData.agreedQty == null ? 0 : modelData.agreedQty }}</span
                      >，<span style="color: #f59a86"
                        >未签{{ modelData.unsignedQty == null ? 0 : modelData.unsignedQty }}</span
                      >）</span
                    >
                  </div>
                  <div v-else class="b-l-#033FB4 b-l-5 p-l-2">
                    停电通知单
                    <!-- <span v-if="
                    modalType == 'isAdd' ||
                    (modalType == 'isEdit' &&
                      modelData?.taskExecuteStatus?.executeStatus == '110')
                  ">（黄色背景为重复停电用户）</span> -->
                    <span v-if="modalType == 'isDetail'"
                      >（<span style="color: #66a530"
                        >同意{{ modelData.cancelAgreedQty == null ? 0 : modelData.cancelAgreedQty }}</span
                      >，<span style="color: #f59a86"
                        >未签{{ modelData.cancelUnsignedQty == null ? 0 : modelData.cancelUnsignedQty }}</span
                      >）</span
                    >
                  </div>
                </h1>
              </div>
              <div
                v-if="
                  modalType == 'isAdd' ||
                  (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus == '110')
                "
                class="p-b-2 flex"
              >
                <!-- <n-button class="mr-3" type="info" @click="modalUserDownModal"
                  >模板下载</n-button
                > -->
                <n-upload
                  class="mr-3"
                  :action="`${baseUrl}/relay-task-service/api/v1/taskUser/import`"
                  accept=".xlsx"
                  :headers="{
                    Authorization: `Bearer ${localStg.get('token')}`
                  }"
                  :data="{
                    startTime: getTime(modelData.startTime, 'minute')
                  }"
                  :show-file-list="false"
                  response-type="'json'"
                  :on-change="modalUserListImport"
                >
                  <n-button type="primary">导入</n-button>
                </n-upload>
                <n-button class="mr-3" type="primary" color="#BAD8FB" style="color: black" @click="modalUserListAdd"
                  >增加</n-button
                >
                <n-button class="mr-3" type="primary" ghost @click="modalMutipleDelete">删除</n-button>
              </div>
            </div>
            <!-- 进度条 -->
            <template v-if="processShow">
              <n-progress
                type="line"
                :percentage="percentage"
                :indicator-placement="'inside'"
                processing
                style="width: 30%; margin: 0 auto; position: relative; top: -3vh"
              />
            </template>

            <!-- 不可编辑的表格 -->
            <n-data-table
              v-if="
                modalType == 'isDetail' ||
                (modalType == 'isEdit' && modelData?.taskExecuteStatus?.executeStatus != '110')
              "
              :scroll-x="2200"
              :columns="modalUserColumns.filter(val => val.key != 'selection')"
              :data="modalUserList"
              :bordered="false"
              :single-line="false"
              :max-height="500"
            />
            <!-- 可编辑的表格 -->

            <n-data-table
              v-else
              ref="modalUserDataTableRef"
              :scroll-x="2200"
              :columns="modalUserColumns"
              :data="modalUserList"
              :row-key="row => row.idDelete"
              :checked-row-keys="modalSelectKeys"
              :bordered="false"
              :single-line="false"
              :max-height="500"
              @update:checked-row-keys="onCheckedRow"
            />
          </n-card>
        </template>
      </n-drawer-content>
      <!-- 停电计划项列表抽屉 -->
      <PlanItemTable
        :type="modalType"
        :default-checked-row-keys="modelData.planItemId"
        :show-plan-item-table="showPlanItemTable"
        :table-data="planItemList"
        @close="showPlanItemTable = false"
        @confirm="
          value => {
            modelData.planItemId = value[0];
          }
        "
      ></PlanItemTable>
    </n-drawer>
    <!-- *************************导入模板信息错误提示 模态框*************************** -->
    <n-modal
      v-model:show="showImportErrorModal"
      preset="dialog"
      :on-after-leave="(showImportErrorModal = false)"
      style="width: 700px"
    >
      <template #header>
        <div>导入模板信息错误提示</div>
      </template>
      <div style="height: 200px; overflow-y: auto">
        <div v-for="(item, index) in importErrorData" :key="index" class="m-b-2">
          {{ item }}
        </div>
      </div>
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="showImportErrorModal = false"> 确认 </n-button>
        </div>
      </template>
    </n-modal>
  </n-card>
  <n-card>
    <!-- *************************停电用户列表*************************** -->
    <!-- <n-spin :loading="true"> -->
    <BasicTable
      ref="actionUserRef"
      tableName="user"
      :columns="userColumns"
      :bordered="false"
      :single-line="false"
      :request="loadUserDataTable"
      :row-key="row => row.id"
      :action-column="actionUserColumn"
      :scroll-x="1900"
      min-height="479"
      @update:checked-row-keys="onUserCheckedRow"
      :on-update:sorter="handleSortChange"
    >

      <template #tableTitle>
        <div class="w-100% reactive">
          <div class="w-100% flex justify-between">
            <div class="flex">
              <div class="font-700 b-l-#033FB4 b-l-5 p-l-2" style="font-size: 18px">
                停电通知单
                <span v-if="taskIdToReceiptCode">(停电计划编号：{{ taskCode }}；停电户数：{{ tableUserTotal }}户)</span>
              </div>
            </div>
          </div>
        </div>
      </template>
      <template #tableToolbarBottom>
        <div class="flex">
          <n-grid :cols="24">
            <n-gi :span="16">
              <n-form
                ref="formUserFilterRef"
                :model="temporaryFormUserParams"
                inline
                label-width="100"
                label-placement="left"
              >
                <n-grid :cols="3" style="margin-left: -2rem">
                  <n-grid-item>
                    <n-form-item label="客户名称" path="customerName">
                      <n-input
                        v-model:value="temporaryFormUserParams.customerName"
                        class="w-l-12"
                        placeholder="请输入客户名称"
                      />
                    </n-form-item>
                  </n-grid-item>
                  <n-grid-item>
                    <n-form-item label="客户地址" path="customerAddress">
                      <n-input
                        v-model:value="temporaryFormUserParams.customerAddress"
                        class="w-l-12"
                        placeholder="请输入客户地址"
                      />
                    </n-form-item>
                  </n-grid-item>
                  <n-grid-item>
                    <n-form-item label="所属台区" path="region">
                      <n-input
                        v-model:value="temporaryFormUserParams.region"
                        class="w-l-12"
                        placeholder="请输入所属台区"
                      />
                    </n-form-item>
                  </n-grid-item>
                  <n-grid-item>
                    <n-form-item label="用户类型" path="userTypeList">
                      <n-select
                        v-model:value="temporaryFormUserParams.userTypeList"
                        class="w-l-12"
                        placeholder="请选择用户类型"
                        :options="taskUserTypeList"
                        clearable
                        multiple
                        max-tag-count="2"
                      />
                    </n-form-item>
                  </n-grid-item>
                  <n-grid-item>
                    <n-form-item label="派发状态" path="assignStatusList">
                      <n-select
                        v-model:value="temporaryFormUserParams.assignStatusList"
                        class="w-l-12"
                        placeholder="请选择派发状态"
                        multiple
                        :options="taskUserAssignStatusList"
                      />
                    </n-form-item>
                  </n-grid-item>
                  <n-grid-item>
                    <n-form-item label="取消派发状态" path="cancelAssignStatusList">
                      <n-select
                        v-model:value="temporaryFormUserParams.cancelAssignStatusList"
                        class="w-l-12"
                        placeholder="请选择取消派发状态"
                        multiple
                        :options="taskUserCancelAssignStatusList"
                      />
                    </n-form-item>
                  </n-grid-item>
                </n-grid> </n-form
            ></n-gi>
            <n-gi :span="8">
              <div class="flex items-center" style="justify-content: flex-end !important">
                <n-button color="#BAD8FB" style="color: black" @click="userFilterFormSubmit">
                  <template #icon>
                    <n-icon><icon-fluent:search-12-regular /></n-icon>
                  </template>
                  查询
                </n-button>
                <n-button type="primary" ghost class="m-l-4" @click="userFilterReset">
                  <template #icon>
                    <n-icon><icon-grommet-icons:power-reset /></n-icon>
                  </template>
                  重置
                </n-button>
              </div>
            </n-gi>
          </n-grid>
        </div>
        <div>
          <div class="flex mb-4">
            <n-button type="primary" class="m-r-5" @click="openModal('allAssignUserModal')">
              <template #icon>
                <n-icon>
                  <svg xmlns="http://www.w3.org/2000/svg" width="1024" height="1024" viewBox="0 0 1024 1024">
                    <g transform="rotate(90 512 512)">
                      <path
                        fill="currentColor"
                        d="M521.7 82c-152.5-.4-286.7 78.5-363.4 197.7c-3.4 5.3.4 12.3 6.7 12.3h70.3c4.8 0 9.3-2.1 12.3-5.8c7-8.5 14.5-16.7 22.4-24.5c32.6-32.5 70.5-58.1 112.7-75.9c43.6-18.4 90-27.8 137.9-27.8c47.9 0 94.3 9.3 137.9 27.8c42.2 17.8 80.1 43.4 112.7 75.9c32.6 32.5 58.1 70.4 76 112.5C865.7 417.8 875 464.1 875 512c0 47.9-9.4 94.2-27.8 137.8c-17.8 42.1-43.4 80-76 112.5s-70.5 58.1-112.7 75.9A352.8 352.8 0 0 1 520.6 866c-47.9 0-94.3-9.4-137.9-27.8A353.84 353.84 0 0 1 270 762.3c-7.9-7.9-15.3-16.1-22.4-24.5c-3-3.7-7.6-5.8-12.3-5.8H165c-6.3 0-10.2 7-6.7 12.3C234.9 863.2 368.5 942 520.6 942c236.2 0 428-190.1 430.4-425.6C953.4 277.1 761.3 82.6 521.7 82zM395.02 624v-76h-314c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h314v-76c0-6.7 7.8-10.5 13-6.3l141.9 112a8 8 0 0 1 0 12.6l-141.9 112c-5.2 4.1-13 .4-13-6.3z"
                      />
                    </g>
                  </svg>
                </n-icon>
              </template>
              一键派发
            </n-button>
            <n-button type="primary" style="border: 0" class="m-r-5" @click="handleCancelModal({}, 'all')">
              <template #icon>
                <n-icon>
                  <icon-ant-design:close-circle-outlined />
                </n-icon>
              </template>
              一键取消
            </n-button>
            <n-button type="primary" style="border: 0" class="m-r-5" @click="outPrint('all')">
              <template #icon>
                <n-icon>
                  <icon-mingcute:print-line />
                </n-icon>
              </template>
              一键打印
            </n-button>
            <n-button type="primary" class="m-r-5" style="border: 0" @click="openModal('allChangeAssignUserModal')">
              <template #icon>
                <n-icon>
                  <icon-ant-design:user-switch-outlined />
                </n-icon>
              </template>
              一键更换
            </n-button>
            <n-button type="primary" class="m-r-5"   style="border: 0" @click="showOneTouchFeedback = true">
              <template #icon>
                <n-icon>
                  <icon-ant-design:profile-filled />
                </n-icon>
              </template>
              一键反馈
            </n-button>
            <n-button type="primary"   style="border: 0" @click="showUserListExport = true">
              <template #icon>
                <n-icon>
                  <icon-ant-design:export-outlined />
                </n-icon>
              </template>
              导出
            </n-button>
          </div>
        </div>
      </template>
    </BasicTable>
    <!-- </n-spin> -->

    <!-- *************************回执详情模态框*************************** -->
    <!-- 回执单号抽屉 -->
    <TaskUserDetailDrawer ref="taskUserDetailModalRef" :transform-id="transformData?.id"></TaskUserDetailDrawer>
    <!-- *************************停电通知单导出 可选字段模态框*************************** -->
    <n-modal
      v-model:show="showUserListExport"
      preset="dialog"
      title="Dialog"
      :style="{ width: '600px' }"
      :on-after-leave="()=>showUserListExport=false"
    >
      <template #header>
        <div>导出停电通知单</div>
      </template>
      <n-transfer
        ref="transfer"
        v-model:value="transferValue"
        virtual-scroll
        :options="transferOptions"
        source-filterable
      />
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="userListExport"> 确认 </n-button>
          <n-button class="m-l-4" @click="showUserListExport=false"> 取消 </n-button>
        </div>
      </template>
    </n-modal>
    <!-- *************************一键反馈模态框*************************** -->
    <n-modal
      v-model:show="showOneTouchFeedback"
      preset="dialog"
      title="Dialog"
      :style="{ width: '600px' }"
      :on-after-leave="()=>{showOneTouchFeedback = false;oneTouchFeedbackForm={}}"
    >
      <template #header>
        <div>一键反馈</div>
      </template>
      <n-form
        :model="oneTouchFeedbackForm"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
        :rules="oneTouchFeedbackRules"
        ref='oneTouchFeedbackRef'
      >
        <n-grid x-gap="12" cols="1">
          <n-grid-item
            ><n-form-item label="反馈状态："  path="feedbackStatus">
              <n-select
                v-model:value="oneTouchFeedbackForm.feedbackStatus"
                placeholder="请选择反馈状态"
                label-field="name"
                value-field="id"
                :options="[
                  {
                    name:'同意',
                    id: '320',
                  },
                  {
                    name:'拒签',
                    id: '330',
                  }
                ]"
              /> 
              </n-form-item></n-grid-item>
        </n-grid>
        <n-grid x-gap="12" cols="1">
          <n-grid-item
            ><n-form-item label="派发方式：" path="deliveryMethod">
              <n-select
                v-model:value="oneTouchFeedbackForm.deliveryMethod"
                placeholder="请选择派发方式"
                label-field="name"
                value-field="id"
                :options="[
                  {
                    name:'送达现场',
                    id: '1',
                  },
                  {
                    name:'微信通知',
                    id: '2',
                  },
                  {
                    name:'营销通知',
                    id: '3',
                  },
                  {
                    name:'电话通知',
                    id: '4',
                  },
                  {
                    name:'发送传真',
                    id: '5',
                  }
                ]"
              /> 
              </n-form-item></n-grid-item>
        </n-grid>
      </n-form>
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="oneTouchFeedbackSubmit"> 确认 </n-button>
          <n-button class="m-l-4" @click="()=>{showOneTouchFeedback = false;oneTouchFeedbackForm={}}"> 取消 </n-button>
        </div>
      </template>
    </n-modal>
    <!-- *************************派发负责方模态框*************************** -->
    <n-modal
      v-model:show="showChangeAssignUserModal"
      preset="dialog"
      title="Dialog"
      :style="{ width: '600px' }"
      :on-after-leave="cancelModalCancel"
    >
      <template #header>
        <div>请选择派发负责方</div>
      </template>
      <n-form
        :model="changeAssignUserModalData"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-grid x-gap="12" cols="1">
          <n-grid-item
            ><n-form-item label="派发负责方：">
              <n-select
                v-model:value="changeAssignUserModalData.engineersTeamId"
                placeholder="请选择派发方负责人"
                label-field="name"
                value-field="id"
                :options="engineersTeamList"
              /> </n-form-item
          ></n-grid-item>
        </n-grid>
      </n-form>
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="modalSubmit"> 确认 </n-button>
          <n-button class="m-l-4" @click="cancelModalCancel"> 取消 </n-button>
        </div>
      </template>
    </n-modal>
    <!-- **************************跟进回执模态框************************** -->
    <n-modal
      v-model:show="showFollowModal"
      preset="dialog"
      title="Dialog"
      :style="{ width: '600px' }"
      :on-after-leave="cancelModalCancel"
    >
      <template #header>
        <div>任务跟进情况</div>
      </template>
      <n-form
        ref="followModalRef"
        :model="followModalData"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
        :rules="rules1"
      >
        <n-grid x-gap="12" cols="2">
          <n-grid-item>
            <n-form-item label="跟进方式：" path="followMethod">
              <n-select
                v-model:value="followModalData.followMethod"
                placeholder="请选择跟进方式"
                :options="[
                  { label: '送达现场', value: 1 },
                  { label: '微信通知', value: 2 },
                  { label: '营销通知', value: 3 }
                ]"
              /> </n-form-item
          ></n-grid-item>
          <n-grid-item
            ><n-form-item label="对接人员：" path="dockUser">
              <n-input v-model:value="followModalData.dockUser" placeholder="请输入对接人员名称" /> </n-form-item
          ></n-grid-item>
        </n-grid>
        <n-grid x-gap="12" cols="2">
          <n-grid-item
            ><n-form-item label="联系电话：" path="telPhone">
              <n-input v-model:value="followModalData.telPhone" placeholder="请输入联系电话" /> </n-form-item
          ></n-grid-item>
          <n-grid-item>
            <n-form-item label="反馈状态：" path="feedbackStatus">
              <n-select
                v-model:value="followModalData.feedbackStatus"
                placeholder="请选择反馈状态"
                :options="[
                  { label: '未签', value: 310 },
                  { label: '同意', value: 320 },
                  { label: '拒签', value: 330 }
                ]"
              /> </n-form-item
          ></n-grid-item>
        </n-grid>
        <n-grid x-gap="12" cols="1">
          <n-grid-item
            ><n-form-item label="跟进情况说明：" path="followDesc">
              <n-input
                v-model:value="followModalData.followDesc"
                placeholder="请输入跟进情况说明"
                type="textarea"
                :autosize="{
                  minRows: 3,
                  maxRows: 8
                }"
              /> </n-form-item
          ></n-grid-item>
        </n-grid>
      </n-form>
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="followModalSubmit"> 确认 </n-button>
          <n-button class="m-l-4" @click="cancelModalCancel"> 取消 </n-button>
        </div>
      </template>
    </n-modal>
    <!-- **************************取消派发模态框************************** -->
    <n-modal
      v-model:show="showCancelModal"
      preset="dialog"
      title="Dialog"
      :style="{ width: '600px' }"
      :on-after-leave="cancelModalCancel"
    >
      <template #header>
        <div>取消当前用户</div>
      </template>
      <n-form
        ref="cancelModalRef"
        :model="cancelModalData"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-grid x-gap="12" cols="1">
          <n-grid-item
            ><n-form-item label="取消原因：" path="cancelReason">
              <n-select
                v-model:value="cancelModalData.cancelReason"
                placeholder="取消原因"
                :options="cancelUserReasonList"
              /> </n-form-item
          ></n-grid-item>
        </n-grid>
      </n-form>
      <template #action>
        <div class="flex">
          <n-button type="primary" @click="cancelModalSubmit"> 确认 </n-button>
          <n-button class="m-l-4" @click="cancelModalCancel"> 取消 </n-button>
        </div>
      </template>
    </n-modal>

  </n-card>
  <!-- **************************打印html模板************************** -->
  <div v-show="false">
    <div
      :id="'printDiv' + index"
      v-for="(item, index) in printDataList"
      :key="index"
      style="position:relative"
    >
      <div
        style="
          padding-left: 40px;
          padding-right: 40px;
          border-bottom: 2px dashed rgb(90, 83, 83);
          margin-bottom: 20px;
          font-size: 12px;
        "
      >
        <div>邮编：{{ item.postCode }}</div>
        <div
          style="
            margin-bottom: 15px;
            display: flex;
            justify-content: space-between;
          "
        >
          <div>
            <span style="margin-right: 2px">
              {{ item.address }}
            </span>
            <span>户号：{{ item.accountNumber }}</span>
          </div>
          <div style="width:180px">
            <span>联系方式：{{ item.phone }}</span>
          </div>
        </div>
        <div style="display: flex; justify-content: space-between;">
          <div>表号：{{item.BH}}</div>
          <div style="width:180px">备用联系方式：{{ item.backupPhone }}</div>
        </div>
        <div style="margin-bottom: 15px">{{ item.customerName }}</div>
        <div
          style="
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
          "
        >
          <div>{{ item.region }}</div>
          <div>
            <span style="font-weight: 700">No.</span>{{
               item.outId?item.outId: item.receiptCode
            }}
          </div>
        </div>
        <div
          style="
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
          "
        >
          <img width="200" src="../../assets/img/printImg.png" />
        </div>

        <div
          style="display: flex; justify-content: flex-end; margin-bottom: 15px"
        >
          回执在下面，请填妥寄回为盼
        </div>
      </div>
      <div
        style="
          padding-left: 40px;
          padding-right: 40px;
          font-size: 12px;
          border-bottom: 2px dashed rgb(90, 83, 83);
          margin-bottom: 20px;
        "
      >
        <div style="margin-bottom: 5px">
          <div style="font-size: 18px; font-weight: 700">
            <div style="text-align: center">国网上海市电力公司市南供电公司</div>
            <div style="line-height: 1; text-align: center">电网检修公告</div>
          </div>
        </div>
        <div
          style="
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
          "
        >
          <div>客户名称：{{ item.customerName }}</div>
          <div>
            <span style="font-weight: 700">No.</span>{{
              item.outId?item.outId: item.receiptCode
            }}
          </div>
        </div>
        <div>
          <div>
            因进行<input type="checkbox"/>35千伏、<input type="checkbox"/>10千伏、<input type="checkbox"/>380伏设备检修,期间供电将会暂时中断，由此带来的不便敬请谅解:
          </div>
          <div style="display: flex; margin-left: 40px">
            <div style="margin-right: 40px">
              检修作业时间为：{{ item.startTime }}（{{ item.startWeek }}）{{
                item.startHourTime
              }}
            </div>
            <div>起</div>
          </div>
          <div style="display: flex; margin-left: 112px">
            <div style="margin-right: 40px">
              至：{{ item.endTime }}（{{ item.endWeek }}）{{ item.endHourTime }}
            </div>
            <div>止</div>
          </div>
          <div style="margin-bottom: 15px">
            备注：电站/线路名称：{{ item.stationLineName }}，设备名称：{{
              item.region
            }}，电系编号：{{ item.electricalNumber }}
          </div>
        </div>
        <div style="margin-left: 60px">
          我们会安排足够的施工力量，力争按时完成工作任务并尽快恢复供电，请采取必要的应急措施
        </div>
        <div>
          温馨提示：1.检修作业期间，存在提前送电可能，遇天气原因或特殊情况可能顺延或取消。
        </div>
        <div style="margin-left: 60px; margin-bottom: 20px">
          2.感谢您一直以来对我们工作的支持与帮助，我们对检修工作给您生产、生活带来的不便深表歉意，请给与理解和支持，如有疑问请来电咨询。谢谢！
        </div>
        <div style="margin-left:70px">市南供电公司</div>
        <div
          style="
            display: flex;
            margin-left: 50px;
            justify-content: space-between;
          "
        >
          <div>
            地址：{{
              customerInfoConfigList?.filter((val) => val.code == "address")[0]
                ?.name
            }}
          </div>
          <div>
            邮编：{{
              customerInfoConfigList?.filter((val) => val.code == "postCode")[0]
                ?.name
            }}
          </div>
        </div>
        <div
          style="
            display: flex;
            margin-left: 50px;
            justify-content: space-between;
            margin-bottom: 15px;
          "
        >
          <div>
            联系电话:{{
              customerInfoConfigList?.filter((val) => val.code == "phone")[0]
                ?.name
            }}
          </div>
          <div>
            联系人：{{
              customerInfoConfigList?.filter((val) => val.code == "name")[0]
                ?.name
            }}
          </div>
        </div>
        <div style="margin-bottom: 30px; text-align: center">请以此线裁下</div>
      </div>

      <div
        style="
          padding-left: 40px;
          padding-right: 40px;
          padding-bottom:10px
          border-bottom: 2px dashed rgb(90, 83, 83);
          font-size: 12px;
        "
      >
        <div style="margin-bottom: 5px">
          <div style="font-size: 18px; font-weight: 700">
            <div style="text-align: center">国网上海市电力公司市南供电公司</div>
            <div style="line-height: 1; text-align: center">
              电网检修公告回执
            </div>
          </div>
        </div>
        <div style="margin-bottom: 10px;">
          <span style="margin-right: 4px">
            {{ item.address }}
          </span>
          <span style="margin-right: 4px">户号：{{ item.accountNumber }}</span>
          <span>表号：{{item.BH}}</span>
        </div>
         <div>
            备注：电站/线路名称：{{ item.stationLineName }}，设备名称：{{
              item.region
            }}，电系编号：{{ item.electricalNumber }}
          </div>
        <div
          style="
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
          "
        >
          <div>客户名称：{{ item.customerName }}</div>
          <div>
            <span style="font-weight: 700">No.</span>{{
             item.outId?item.outId: item.receiptCode
            }}
          </div>
        </div>

        <div style="display:flex;justify-content: space-between">
          <div style="margin-left: 30px">
            <div style="width: 450px;display:flex">
              <div style="margin-right: 20px">
                今接到你公司于：{{ item.startTime }}（{{ item.startWeek }}）{{
                  item.startHourTime
                }}
              </div>
              <div>起</div>
            </div>
            <div style="margin-left: 73px;display:flex">
              <div style="margin-right: 20px">
                至：{{ item.endTime }}（{{ item.endWeek }}）{{ item.endHourTime }}
              </div>
              <div>止</div>
            </div>
          </div>
          <div>
            <img :id="'qrCode' + index" style="width: 100px; height: 100px"   :src="'data:image/png;base64,'+item.qrCode"/>
          </div>
        </div>

        <div style="text-indent: 40px; margin-bottom: 5px">
          需要进行{{item.reason?item.reason:''}}，我处将停止供电，我处已做好安排，可按时停电。
        </div>
        <div
          style="
            display: flex;
            margin-left: 30px;
            margin-bottom: 5px;
          "
        >
          <div style="width:300px">客户单位（签署）：</div>
          <div>
            年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;盖章
          </div>
        </div>

        <div style="margin-bottom:5px">请客户收到本件后，尽快回寄：</div>
        <div
          style="
            display: flex;
            margin-left: 50px;
          "
        >
          <div style="width:300px">
            地址：{{
              customerInfoConfigList?.filter((val) => val.code == "address")[0]
                ?.name
            }}
          </div>
          <div>
            邮编：{{
              customerInfoConfigList?.filter((val) => val.code == "postCode")[0]
                ?.name
            }}
          </div>
        </div>
        <div style="position:absolute;bottom:0;width:88%;text-align:center">{{index+1}}</div>
      </div>
    </div>
  </div>
  <!-- **************************版本模态框************************** -->
  <VersionModal ref="versionRef" />
  <!-- 关联通知单抽屉 -->
  <TaskDetailDrawer
    ref="modalRef1"
    :transformId="transfromData.id"
    transformIdType="task"
  ></TaskDetailDrawer>
  <RepeatAlertModal v-model:repeatAlert="repeatAlert" :repeatAlertModalData="repeatAlertModalData" @close="repeatModalClose"></RepeatAlertModal>
</template>

<script lang="ts" setup>
import { h, reactive, ref, computed, onMounted, nextTick } from "vue";
import {
  useMessage,
  NSelect,
  NInput,
  NIcon,
  NButton,
  FormItemRule,
  FormRules,
} from "naive-ui";
import { BasicTable, TableAction } from "@/components/business/Table";

import type { FormSchema } from "@/components/business/Form/index";
import { BasicForm, useForm } from "@/components/business/Form/index";
import { getServiceEnvConfig } from "~/.env-config";
import { localStg } from "@/utils";
import { blobToFile } from "@/utils/common/blobToFile";
import getLodop from "@/utils/common/lodopFuncs";
import { advanceWarningMap } from "./map";
const message = useMessage();
import { useDicStore } from "@/store";
import { getToken } from "@/store/modules/auth/helpers";
import {
  addAPI,
  batchPutApi,
  batchPostApi,
  delAPI,
  detailAPI,
  editAPI,
  pageAPI,
  uploadApi,
} from "~/src/service/api/common";
import {
  ableParamslistAPI,
  backCancelUserApi,
  cancelTaskApi,
  revokeCancelTaskApi,
  printDataApi,
  putSubmitAPI,
  repeatModalAllApi,
  repeatModalTaskCodeApi,
  repeatModalUpdateApi,
} from "~/src/service/api/otherApi";
import { exportExcal } from '~/src/utils/common/exportExcal'
// =====================================重复停电 警告弹窗 相关======================
const repeatAlert = ref(false)
const repeatAlertModalData = ref([])
// setTimeout(() => {
//   let res;
//   res = {data:[{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   },{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   },{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   },{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   },{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   },{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   },{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   },{
//     taskCodeList:[
//       'RW20240375',
//       'RW20240375'
//     ],
//     accessPointList:[
//       '2024062010',
//       '2024062020',
//       '2024062030'
//     ],
//   }]}
//   if(res.data && res.data?.length > 0){
//     repeatAlertModalData.value = res.data
//     repeatAlert.value = true
//   }
  
// }, 5000)
async function repeatModalClose(){
  console.log('repeatModalClose===================')
  console.log(taskCode.value,'taskCode.value')
  if(!taskCode.value){
    await repeatModalUpdateApi({
      taskCode:''
    })
  }else{
    await repeatModalUpdateApi({
      taskCode:taskCode.value
    })
  }
  repeatAlert.value = false
}
// ===========================================================================
let dic = useDicStore();
const baseUrl = ref(getServiceEnvConfig(import.meta.env).url);
const versionRef = ref(null);

//任务跟进情况表单验证
const rules1: FormRules = {
  followMethod:[
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = "跟进方式不能为空";
          return false;
        }
        return true;
      },
      trigger: ["input", "blur"],
    },
  ],
  feedbackStatus: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = "状态不能为空";
          return false;
        }
        return true;
      },
      trigger: ["input", "blur"],
    },
  ],
};
//验证函数
function handleValidateButtonClick() {
  return new Promise((resolve, reject) => {
    followModalRef.value?.validate((errors) => {
      if (!errors) {
        resolve(true);
      } else {
        reject(false);
      }
    });
  });
}

// 关联通知单数据定义
let transfromData = ref({});
let modalRef1 = ref(null);

// 抽屉中停电通知单表的加载状态
let processShow = ref(false);
let percentage = ref(0);

onMounted(async () => {
  //初始执行状态不传已完成 已终结
  handleSubmit({ executeStatusList: ["110", "120", "130", "140", "150"] });
  //获取字典数据
  await dic.loadData(
    "user,task_execute_status,task_type,task_reason,noConcatUser,task_user_priority,task_user_voltage_level,task_cancel_reason,task_user_cancel_reason,task_user_feedback_status,task_assign_method,task_cancel_feedback_status,task_user_assign_status,task_cancel_assign_status,task_user_type,customer_info_config"
  );
  ableParamslistAPI("relay-task-service/api/v1/planItem", {
    executeStatus: "010",
  }).then((res: any) => {
    if (!res.error) {
      planItemList.value = res.data;
    }
  });
  ableParamslistAPI("relay-task-service/api/v1/engineersTeam", {}).then(
    (res: any) => {
      if (!res.error) {
        engineersTeamList.value = res.data;
      }
    }
  );
});
let taskUserTypeList = computed(() => {
  //用户类型下拉菜单
  return dic.getTaskUserType;
});
let taskSourceUserList = computed(() => {
  //通知来源下拉菜单
  //'user'
  return dic.getUser;
});
let powerFailureTypeList = computed(() => {
  //停电类型下拉菜单
  //'task_type'
  return dic.getPowerFailureType;
});
let powerFailureReasonList = computed(() => {
  //停电原因下拉菜单
  //'task_reason'
  return dic.getPowerFailureReason;
});
let cancelReasonList = computed(() => {
  //取消任务原因下拉菜单
  return dic.getCancelReason;
});
let cancelUserReasonList = computed(() => {
  //取消用户原因下拉菜单
  return dic.getCancelUserReason;
});
let taskUserFeedBackStatusList = computed(() => {
  //停电用户通知反馈状态下拉菜单
  return dic.getTaskUserFeedBackStatus;
});
let taskCancelFeedbackStatusList = computed(() => {
  //取消停电用户通知反馈状态下拉菜单
  return dic.getTaskCancelFeedbackStatus;
});

const taskAssignMethodList = computed(() => {
  // 派送方式下拉菜单
  return dic.getTaskAssignMethodList;
});
const userPriorityList = computed(() => {
  // 用户重要性下拉菜单
  // 'task_user_priority'
  return dic.getUserPriority;
});
const taskUserAssignStatusList = computed(() => {
  /// /停电任务-用户派发状态下拉菜单
  return dic.getTaskUserAssignStatus;
});
const taskUserCancelAssignStatusList = computed(() => {
  /// /停电任务-取消用户派发状态下拉菜单
  return dic.getTaskCancelAssignStatus;
});
const customerInfoConfigList = computed(() => {
  /// /打印模板 信息
  return dic.getCustomerInfoConfigList;
});
// 回执详情抽屉数据
const transformData = ref({});
const taskUserDetailModalRef = ref(null);

const engineersTeamList = ref([]);
const planItemList = ref([]);
const modalType = ref('');
const modalSubmitType = ref('');
const showPlanItemTable = ref(false);

const showModal = ref(false);
const showUserFilterModal = ref(false);
const showChangeAssignUserModal = ref(false);
const showFollowModal = ref(false);
const showCancelModal = ref(false);
const showCancelTaskModal = ref(false);
const showBackCancelTaskModal = ref(false)
const showTaskUserDetailModal = ref(false);
// =====================================停电通知单（新加的功能字段定义）=============================
// 一键反馈模态框
const showOneTouchFeedback = ref(false)
// 一键反馈表单
const oneTouchFeedbackForm = ref({})
// 一键反馈表单校验
const oneTouchFeedbackRules: FormRules = {
  feedbackStatus: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = "反馈状态不能为空";
          return false;
        }
        return true;
      },
      trigger: ["input", "blur"],
    },
  ],
  deliveryMethod:[
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = "派发方式不能为空";
          return false;
        }
        return true;
      },
      trigger: ["input", "blur"],
    },
  ],
};
// 一键反馈domRef
const oneTouchFeedbackRef = ref(null)

// 导出弹窗
const showUserListExport = ref(false)
//  导出穿梭框保存值
const transferValue = ref([])
// 导出穿梭框options数据
const transferOptions = ref([
  {
     label: '用户类型',
     value: 'userType'
  },
  {
     label: '地址',
     value: 'address'
  },
  {
     label: '户号',
     value: 'accountNumber'
  },
  {
     label: '客户名称',
     value: 'customerName'
  },
  {
     label: '客户地址',
     value: 'customerAddress'
  },
  {
     label: '联系方式',
     value: 'phone'
  },
  {
     label: '用户重要性',
     value: 'userPriority'
  },
  {
     label: '回执单号',
     value: 'receiptCode'
  },
  {
     label: '停电通知派发状态',
     value: 'assignStatus'
  },
  {
     label: '停电通知反馈情况',
     value: 'feedbackStatus'
  },
  {
     label: '取消通知派发状态',
     value: 'cancelAssignStatus'
  },
  {
     label: '取消通知反馈情况',
     value: 'cancelFeedbackStatus'
  },
  {
     label: '派发负责方',
     value: 'engineersTeamName'
  },
  {
     label: '是否短时停电',
     value: 'boolShortTime'
  },
  {
     label: '所属台区',
     value: 'region'
  },
  {
     label: '电系编号',
     value: 'electricalNumber'
  },
  {
     label: '电压等级',
     value: 'voltageLevel'
  },
  {
     label: '是否重复停电',
     value: 'boolRepeatPowerCut'
  },
  {
     label: '停电通知派发方式',
     value: 'assignMethod'
  },
  {
     label: '停电通知反馈时间',
     value: 'feedbackTime'
  },
  {
     label: '取消通知派发方式',
     value: 'cancelAssignMethod'
  },
  {
     label: '取消通知反馈时间',
     value: 'cancelFeedbackTime'
  },
  {
     label: '所属接入点',
     value: 'accessPoint'
  },
  {
     label: '所属接入点名称',
     value: 'accessPointName'
  },
  {
     label: '装接容量',
     value: 'capacity'
  },
  {
     label: '备注',
     value: 'remark'
  },
])
// ===================================
const showTaskUserFeedbackLog = ref(false);
const showCancelTaskUserFeedbackLog = ref(false);
const showTaskUserFollowLog = ref(false);
const showImportErrorModal = ref(false);

const taskIdToReceiptCode = ref('');
const taskExecuteStatus = ref('150');
const cancelTaskModalExecuteStatus = ref('');
const taskCode = ref('');
const tableUserTotal = ref('');
const cancelTaskModalType = ref('');

const modalUserList: any = ref([]);
const tableSelectKeys: any = ref([]);
const tableSelectRows: any = ref([]);
const tableUserSelectKeys: any = ref([]);
const tableUserSelectRows: any = ref([]);
const modalSelectKeys: any = ref([]);
const taskUserFeedbackLogData: any = ref([]);
const cancelTaskUserFeedbackLogData: any = ref([]);
const taskUserFeedbackImgList: any = ref([]);
const cancelTaskUserFeedbackImgList: any = ref([]);
const fujianImgList: any = ref([]);
const taskUserFollowLogData: any = ref([]);
const fileListUpload: any = ref([]);
const commonFileList: any = ref([]);
const printDataList: any = ref([]);
const importErrorData: any = ref([]);

const actionRef = ref();
const actionUserRef = ref();
const formRef: any = ref(null);
const formUserFilterRef: any = ref(null);
const modalRef: any = ref(null);
const followModalRef: any = ref(null);
const modalUserDataTableRef: any = ref(null);

const modelData: any = ref({});
const oldTime: any = ref({
  startTime: '',
  endTime: ''
});
const taskUserDetailData: any = ref({});
// const startTimeAndEndTime: any = ref({});
const followModalData: any = ref({});
const cancelModalData: any = ref({});
const changeAssignUserModalData: any = ref({});
const cancelTaskModalData: any = ref({});
const formParams: any = ref({});
const formUserParams: any = ref({});
const temporaryFormUserParams = reactive({});
// 停电计划查询表单
const schemas: FormSchema[] = [
  {
    field: 'startDate',
    component: 'NDatePicker',
    componentProps: {
      type: 'daterange',
      valueFormat: 'yyyy-MM-dd'
    },
    label: '停电时间',
    op: {}
  },
  {
    field: 'endDate',
    component: 'NDatePicker',
    componentProps: {
      type: 'daterange',
      valueFormat: 'yyyy-MM-dd'
    },
    label: '送电时间',
    op: {}
  },
  {
    field: 'executeStatusList',
    component: 'NSelect',
    label: '执行状态',
    componentProps: {
      multiple: true,
      defaultValue: ['110', '120', '130', '140', '150'],
      options: computed(() => dic.getExecutionStatus)
    },
    op: {}
  },
  {
    field: 'ranges',
    component: 'NInput',
    label: '停电范围',
    op: {}
  },
  {
    field: 'deviceName',
    component: 'NInput',
    label: '设备名称',
    op: {}
  }
];
const [register, {}] = useForm({
  gridProps: { cols: '1 s:1 m:2 l:3 xl:4 2xl:4' },
  labelWidth: 100,
  schemas,
  showAdvancedButton: true // 收起展开
});

//停电计划列表
const columns = [
  {
    type: 'selection',
    fixed: 'left'
  },
  {
    title: '序号',
    type: 'index',
    width: 60,
    align: 'center',
    fixed: 'left',
    render(row,index) {
      const pagination = actionRef.value.pagination
      return h(
        'span',
        {
          style: {
            color:getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
            cursor: 'pointer'
          },
        },
        (index+1) + (pagination.pageSize * (pagination.pageIndex - 1))
      )
    }
  },
  {
    title: '停电计划编号',
    key: 'taskCode',
    fixed: 'left',
    width: 160,
    render(row) {
      return h(
        'span',
        {
          style: {
            // color: 'rgba(0,122,255,0.7)',
            color:getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
            cursor: 'pointer'
          },
          onClick: () => {
            transfromData.value = row;
            nextTick(() => {
              modalRef1.value?.open();
            });
          }
        },
        row.taskCode
      );
    }
  },
  {
    title: '状态',
    key: 'executeStatus',
    fixed: 'left',
    width: 100,
    render(row) {
      return h(
        'div',
        {
          style:{
            background:getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
            color:getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).fontColor,
            borderRadius: '2px',
            boxShadow: '4px',
            paddingLeft:'5px',
            paddingRight:'5px',
          }
        },
        JSON.parse(JSON.stringify(dic.getExecutionStatus)).filter(
          item => item.value == row.taskExecuteStatus?.executeStatus
        )[0]?.label
      );
    }
  },
  {
    title: '停电时间',
    key: 'startTime',
    width: 160,
    sorter: 'default',
    render(row) {
      return h(
        'span',
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          }
        },
        row.startTime
      );
    }
  },
  {
    title: '送电时间',
    key: 'endTime',
    width: 160,
    render(row) {
      return h(
        'span',
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          }
        },
        row.endTime
      );
    }
  },
  {
    title: '设备名称',
    key: 'deviceName',
    width: 140,
    render(row) {
      return h(
        'span',
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          }
        },
        row.deviceName
      );
    }
  },
  {
    title: '工作内容',
    key: 'jobContent',
    width: 170,
    render(row) {
      return h(
        'span',
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          }
        },
        row.jobContent
      );
    }
  },
  {
    title: '停电类型',
    key: 'type',
    width: 150,
    render(row) {
      return h(
        'span',
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          }
        },
        JSON.parse(JSON.stringify(dic.getPowerFailureType)).filter(item => item.value == row.type)[0]?.label
      );
    }
  },
  {
    title: '停电原因',
    key: 'reason',
    width: 140,
    render(row) {
      return h(
        'span',
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          }
        },
        JSON.parse(JSON.stringify(dic.getPowerFailureReason)).filter(item => item.value == row.reason)[0]?.label
      );
    }
  },
  {
    title: '停电范围',
    key: 'ranges',
    width: 150,
    render(row) {
      return h(
        'span',
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          }
        },
        row.ranges
      );
    }
  },
  {
    title: '通知派发情况',
    key: 'unassignedQty',
    width: 300,
    children: [
      {
        title: '未派发',
        key: 'unassignedQty',
        width: 80,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              }
              // style: parseInt(row.unassignedQty) > 0 ? { color: '#D9001B' } : 'text'
            },
            row.unassignedQty
          );
        }
      },
      {
        title: '已派发',
        key: 'assignedQty',
        width: 80,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              }
              // style: parseInt(row.assignedQty) > 0 ? { color: 'green' } : 'text'
            },
            row.assignedQty
          );
        }
      },
      {
        title: '不派发',
        key: 'cancelledQty',
        width: 80,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              }
              // style: parseInt(row.cancelledQty) > 0 ? { color: '#D9001B' } : 'text'
            },
            row.cancelledQty
          );
        }
      }
    ]
  },
  {
    title: '用户反馈情况',
    key: 'rejectedQty',
    width: 300,
    children: [
      {
        title: '拒签',
        key: 'rejectedQty',
        width: 60,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              },
              // style: parseInt(row.rejectedQty) > 0 ? { color: '#D9001B' } : 'text'
            },
            row.rejectedQty
          );
        }
      },
      {
        title: '同意',
        key: 'agreedQty',
        width: 60,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              },
              // style: parseInt(row.agreedQty) > 0 ? { color: 'green' } : 'text'
            },
            row.agreedQty
          );
        }
      },
      {
        title: '未签',
        key: 'unsignedQty',
        width: 60,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              },
              // style: parseInt(row.unsignedQty) > 0 ? { color: '#F59A23' } : 'text'
            },
            row.unsignedQty
          );
        }
      }
    ]
  },
  {
    title: '取消通知时间',
    key: 'cancelTime',
    width: 170,
    render(row) {
      return h(
        "span",
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          },
        },
        row.cancelTime
      );
    },
  },
  {
    title: '取消派发情况',
    key: 'unassignedQty',
    width: 160,
    children: [
      {
        title: '已派发',
        key: 'cancelAssignedQty',
        width: 80,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              },
              // style: parseInt(row.cancelAssignedQty) > 0 ? { color: 'green' } : 'text'
            },
            row.cancelAssignedQty
          );
        }
      },
      {
        title: '未派发',
        key: 'cancelUnassignedQty',
        width: 80,

        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              },
              // style: parseInt(row.cancelUnassignedQty) > 0 ? { color: '#F59A23' } : 'text'
            },
            row.cancelUnassignedQty
          );
        }
      }
    ]
  },
  {
    title: '取消反馈情况',
    key: 'rejectedQty',
    width: 300,
    children: [
      {
        title: '同意',
        key: 'cancelAgreedQty',
        width: 80,
        render(row) {
          return h(
            'span',
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              },
              // style: parseInt(row.cancelAgreedQty) > 0 ? { color: 'green' } : 'text'
            },
            row.cancelAgreedQty
          );
        }
      },
      {
        title: "未签",
        key: "cancelUnsignedQty",
        width: 80,
        render(row) {
          return h(
            "span",
            {
              style: {
                color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
              },
              // style:
              //   parseInt(row.cancelUnsignedQty) > 0
              //     ? { color: "#F59A23" }
              //     : "text",
            },
            row.cancelUnsignedQty
          );
        },
      },
    ],
  },

  {
    title: "是否对媒体公布",
    key: "boolNotifyMedia",
    width: 140,
    render(row) {
      return h(
        "span",
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          },
        },
        row.boolNotifyMedia == 1? "是": row.boolNotifyMedia == 0? "否": "-"
      );
    },
  },
  {
    title: "通知来源",
    key: "taskSource",
    width: 140,
    render(row) {
      return h(
        "span",
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          },
        },
        row.taskSource
          ? JSON.parse(JSON.stringify(dic.getUser)).filter(
              (item) => row.taskSource == item.value
            )[0]?.name
          : ""
      );
    },
  },
  {
    title: "版本",
    key: "version",
    width: 80,
    fixed: "right",

    render(row) {
      return h(
        "span",
        {
          style: parseInt(row.version) > 1 ? { color: "red" } : {},
          onClick: () => {
            nextTick(() => {
              versionRef.value?.versionPageInit(row.id);
            });
          },
        },
        row.version
      );
    },
  },
  {
    title: "关联停电计划编号",
    key: "assTaskCode",
    width: 150,
    render(row) {
      return h(
        "span",
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          },
        },
        row.assTaskCode
      );
    },
  },
  {
    title: "任务预警状态",
    key: "advanceWarning",
    width: 140,
    render(row) {
      return h(
        "div",
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          },
          // style: advanceWarningMap.get(row.advanceWarning)?.style,
        },
        advanceWarningMap.get(row.advanceWarning)?.label
      );
    },
  },
  {
    title: "创建时间",
    key: "createDate",
    width: 170,
    render(row) {
      return h(
        "span",
        {
          style: {
            color: getTableExecuteStatusColor(row.taskExecuteStatus?.executeStatus).backgroundColor,
          },
        },
        row.createDate
      );
    },
  },
];
//停电用户列表
const userColumns = [
  // {
  //   type: "selection",
  //   fixed: "left",
  // },
    {
    title: '序号',
    type: 'index',
    width: 60,
    align: 'center',
    fixed: 'left',
    render(row,index) {
      const pagination = actionUserRef.value.pagination
      return h(
        'span',
        {
        },
        (index+1) + (pagination.pageSize * (pagination.pageIndex - 1))
      )
    }
  },
    {
    title: "用户类型",
    key: "userType",
    width: 100,
    render(row) {
      return h(
        "span",
        {},
        taskUserTypeList.value?.filter((item) => item.value == row.userType)[0]
          ?.label
      );
    },
  },
    {
    title: "地址",
    key: "address",
    width: 140,
    render(row) {
      return h("span", {}, row.address);
    },
  },
  {
    title: '户号',
    key: 'accountNumber',
    width: 100,
    sorter: 'default'
  },
  {
    title: '客户名称',
    key: 'customerName',
    width: 140
  },

  {
    title: '联系方式',
    key: 'phone',
    width: 100
  },

  {
    title: '停电通知派发状态',
    key: 'assignStatus',
    width: 110,
    render(row) {
      return h(
        'span',
        {
          style:
            row.assignStatus == 240
              ? { color: 'red' }
              : row.assignStatus == 210
              ? { color: '#F59A23' }
              : row.assignStatus == 220
              ? { color: 'green' }
              : row.assignStatus == 230
              ? { color: 'rgb(0,122,255)' }
              : {}
        },
        row.assignStatus == 210
          ? '未派发'
          : row.assignStatus == 220
          ? '已派发'
          : row.assignStatus == 230
          ? '已反馈'
          : '不派发'
      );
    }
  },
  {
    title: '停电通知反馈情况',
    key: 'feedbackStatus',
    width: 110,
    render(row) {
      return h(
        'span',
        {
          style:
            row.feedbackStatus == 310
              ? { color: '#f8a723' }
              : row.feedbackStatus == 330
              ? { color: 'red' }
              : row.feedbackStatus == 320
              ? { color: '#66a530' }
              : {}
        },
        row.feedbackStatus == 310
          ? '未签'
          : row.feedbackStatus == 320
          ? '同意'
          : row.feedbackStatus == 330
          ? '拒签'
          : ''
      );
    }
  },
  {
    title: '取消通知派发状态',
    key: 'cancelAssignStatus',
    width: 110,
    ifShow: () => {
      return taskExecuteStatus.value == '150';
    },
    render(row) {
      return h(
        'span',
        {
          style:
            row.cancelAssignStatus == 440
              ? { color: 'red' }
              : row.cancelAssignStatus == 410
              ? { color: '#f8a723' }
              : row.cancelAssignStatus == 420
              ? { color: '#66a530' }
              : row.cancelAssignStatus == 430
              ? { color: 'rgb(0,122,255)' }
              : {}
        },
        row.cancelAssignStatus == 410
          ? '未派发'
          : row.cancelAssignStatus == 420
          ? '已派发'
          : row.cancelAssignStatus == 430
          ? '已反馈'
          : row.cancelAssignStatus == 440
          ? '不派发'
          : ''
      );
    }
  },
  {
    title: '取消通知反馈情况',
    key: 'cancelFeedbackStatus',
    width: 110,
    ifShow: () => {
      return taskExecuteStatus.value == '150';
    },

    render(row) {
      return h(
        'span',
        {
          style: row.cancelFeedbackStatus == 510 ? { color: '#f8a723' } : { color: '#66a530' }
        },
        row.cancelFeedbackStatus == 510 ? '未签' : row.cancelFeedbackStatus == 520 ? '同意' : ''
      );
    }
  },

  {
    title: '派发负责方',
    key: 'engineersTeamName',
    width: 80
  },


  {
    title: '是否短时停电',
    key: 'boolShortTime',
    width: 110,
    render(row) {
      return h('span', {}, row.boolShortTime == 0 ? '否' : row.boolShortTime == 1 ? '是' : '');
    }
  },

  {
    title: '所属台区',
    key: 'region',
    width: 130
  },
  {
    title: '电系编号',
    key: 'electricalNumber',
    width: 80
  },
  {
    title: '电压等级',
    key: 'voltageLevel',
    width: 100,

    render(row) {
      return h('span', {}, row.voltageLevel);
    }
  },
  {
    title: '是否重复停电',
    key: 'boolRepeatPowerCut',
    width: 100,

    render(row) {
      return h('span', {}, row.boolRepeatPowerCut == 0 ? '否' : row.boolRepeatPowerCut == 1 ? '是' : '');
    }
  },

  {
    title: '停电通知派发方式',
    key: 'assignMethod',
    width: 120,
    render(row) {
      return h(
        'span',
        {},

        row.assignMethod == 1
          ? '送达现场'
          : row.assignMethod == 2
          ? '微信通知'
          : row.assignMethod == 3
          ? '营销通知'
          :row.assignMethod == 4
          ? '电话通知'
          :row.assignMethod == 5
          ? '发送传真'
          :''
      );
    }
  },
  {
    title: '停电通知反馈时间',
    key: 'feedbackTime',
    width: 120
  },
  {
    title: '取消通知派发方式',
    key: 'cancelAssignMethod',
    width: 120,
    ifShow: () => {
      return taskExecuteStatus.value == '150';
    },

    render(row) {
      return h(
        'span',
        {},
        row.cancelAssignMethod == 1
          ? '送达现场'
          : row.cancelAssignMethod == 2
          ? '微信通知'
          : row.cancelAssignMethod == 3
          ? '营销通知'
          :row.assignMethod == 4
          ? '电话通知'
          :row.assignMethod == 5
          ? '发送传真'
          :''
      );
    }
  },
  {
    title: "取消通知反馈时间",
    key: "cancelFeedbackTime",
    width: 120,
    ifShow: () => {
      return taskExecuteStatus.value == "150";
    },
  },

  {
    title: "所属接入点",
    key: "accessPoint",
    width: 100,
  },
  {
    title: "所属接入点名称",
    key: "accessPointName",
    width: 160,
  },
  {
    title: "装接容量",
    key: "capacity",
    width: 70,
  },
  {
    title: "备注",
    key: "remark",
    width: 140,
  },
      {
    title: '客户地址',
    key: 'customerAddress',
    width: 160
  },
  {
    title: "用户重要性",
    key: "userPriority",
    width: 100,
    render(row) {
      return h(
        "span",
        {},
        userPriorityList.value?.filter(
          (item) => item.value == row.userPriority
        )[0]?.label
      );
    },
  },
  {
    title: "回执单号",
    key: "receiptCode",
    width: 90,
    render(row) {
      return h(
        "span",
        {
          style: {
            color: "rgb(0,122,255)",
            cursor: "pointer",
          },
          onClick: () => {
            transformData.value = row;
            nextTick(() => {
              taskUserDetailModalRef.value?.open();
            });
          }
          // onClick: openModal.bind(null, "taskUserDetailModal", row),
        },
        row.receiptCode
      );
    }
  },
];
//停电计划详情新增、编辑、详情抽屉中的表单规则
const rules: any = {
  planItemId: {
    // required: true,
    message: '请选择关联停电计划'
  },
  startTime: {
    required: true,
    message: '请选择停电时间'
  },
  endTime: {
    required: true,
    message: '请选择送电时间'
  },
  type: {
    required: true,
    trigger: ['blur', 'change'],
    message: '请选择停电类型'
    // type: 'number',
  },

  reason: {
    required: true,
    trigger: ['blur', 'change'],
    message: '请选择停电原因'
    // type: 'number',
  },
  outId: {
    required: true,
    trigger: ['blur', 'change'],
    message: '请输入配网办编号'
    // type: 'number',
  },
  stationName: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入变电站名称'
  },
  lineName: {
    required: true,
    trigger: ['blur', 'input'],
    message: '请输入线路名称'
  }
};
// 停电计划详情新增、编辑、详情抽屉中的停电用户列表
const modalUserColumns = [
  {
    type: 'selection',
    key: 'selection',
    fixed: 'left'
  },
  {
    title: '用户类型',
    key: 'userType',
    render(row, index) {
      // if (row.isUseAddButton) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NSelect, {
          value: row.userType,
          options: dic.getTaskUserType,
          onUpdateValue(v) {
            modalUserList.value[index].userType = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, taskUserTypeList.value?.filter(item => item.value == row.userType)[0]?.label);
    },
    width: 120
  },
  {
    title: '户号',
    key: 'accountNumber',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.accountNumber,
          onUpdateValue(v) {
            modalUserList.value[index].accountNumber = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.accountNumber);
    },
    width: 150
  },
  {
    title: '客户名称',
    key: 'customerName',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.customerName,
          onUpdateValue(v) {
            modalUserList.value[index].customerName = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.customerName);
    },
    width: 120
  },
  {
    title: '客户地址',
    key: 'customerAddress',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.customerAddress,
          onUpdateValue(v) {
            modalUserList.value[index].customerAddress = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.customerAddress);
    },
    width: 150
  },
  {
    title: '联系方式',
    key: 'phone',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.phone,
          onUpdateValue(v) {
            modalUserList.value[index].phone = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.phone);
    },
    width: 130
  },
  {
    title: '邮编',
    key: 'postCode',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.postCode,
          onUpdateValue(v) {
            modalUserList.value[index].postCode = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.postCode);
    },
    width: 120
  },
  {
    title: '是否短时停电',
    key: 'boolShortTime',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NSelect, {
          value: row.boolShortTime,
          options: [
            { label: '否', value: 0 },
            { label: '是', value: 1 }
          ],
          onUpdateValue(v) {
            modalUserList.value[index].boolShortTime = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.boolShortTime == 1 ? '是' : '否');
    },
    width: 100
  },
  {
    title: '用户重要性',
    key: 'userPriority',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NSelect, {
          value: row.userPriority,
          options: dic.getUserPriority,
          onUpdateValue(v) {
            modalUserList.value[index].userPriority = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, userPriorityList.value?.filter(item => item.value == row.userPriority)[0]?.label);
    },
    width: 130
  },
  {
    title: '所属台区',
    key: 'region',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.region,
          onUpdateValue(v) {
            modalUserList.value[index].region = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.region);
    },
    width: 90
  },
  {
    title: '电系编号',
    key: 'electricalNumber',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.electricalNumber,
          onUpdateValue(v) {
            modalUserList.value[index].electricalNumber = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.electricalNumber);
    },
    width: 90
  },
  {
    title: '地址',
    key: 'address',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.address,
          onUpdateValue(v) {
            modalUserList.value[index].address = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.address);
    },
    width: 120
  },
  {
    title: '电压等级',
    key: 'voltageLevel',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NSelect, {
          value: row.voltageLevel,
          options: dic.getVoltageLevel,
          onUpdateValue(v) {
            modalUserList.value[index].voltageLevel = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.voltageLevel);
    },
    width: 120
  },
  {
    title: '所属接入点',
    key: 'accessPoint',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.accessPoint,
          onUpdateValue(v) {
            modalUserList.value[index].accessPoint = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.accessPoint);
    },
    width: 90
  },
  {
    title: '所属接入点名称',
    key: 'accessPointName',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.accessPointName,
          onUpdateValue(v) {
            modalUserList.value[index].accessPointName = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.accessPointName);
    },
    width: 120
  },
  {
    title: '装接容量',
    key: 'capacity',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.capacity,
          onUpdateValue(v) {
            modalUserList.value[index].capacity = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.capacity);
    },
    width: 90
  },
  {
    title: '备注',
    key: 'remark',
    render(row, index) {
      if (
        modalType.value == 'isAdd' ||
        (modalType.value == 'isEdit' && modelData.value?.taskExecuteStatus?.executeStatus == '110')
      ) {
        return h(NInput, {
          value: row.remark,
          onUpdateValue(v) {
            modalUserList.value[index].remark = v;
          }
        });
      }
      // 导入的数据 或者 详情
      return h('span', {}, row.remark);
    },
    width: 150
  }
];
// 反馈记录列表
const taskUserFeedbackLogColumns = [
  {
    title: '反馈时间',
    key: 'createDate',
    width: 130
  },
  {
    title: '派送方式',
    key: 'deliveryMethod',
    width: 100,
    render(row) {
      return h('div', {}, taskAssignMethodList.value.filter(item => item.value == `${row.deliveryMethod}`)[0]?.label);
    }
  },

  {
    title: '反馈状态',
    key: 'feedbackStatus',
    width: 100,
    render(row) {
      return h(
        'div',
        {},
        taskUserFeedBackStatusList.value.filter(item => item.value == `${row.feedbackStatus}`).length != 0
          ? taskUserFeedBackStatusList.value.filter(item => item.value == `${row.feedbackStatus}`)[0]?.label
          : taskCancelFeedbackStatusList.value.filter(item => item.value == `${row.feedbackStatus}`)[0]?.label
      );
    }
  },
  {
    title: '拒签理由',
    key: 'rejectedReason',
    width: 150
  },
  {
    title: '附件',
    key: 'rejectedReason',
    width: 80,
    render(row) {
      if (row.commonFileList.length != 0) {
        return h(
          'button',
          {
            onClick: () => {
              fujianImgList.value = row.commonFileList;
              // 当fujianImgList有值后会执行v-for 出来imgToken才能被获取到 所以先nextTick等待dom刷新后再获取
              nextTick(() => {
                const parentElement = document.getElementById('imgToken');
                const imgElement = parentElement.querySelector('img');
                console.log(imgElement, 'imgElementimgElement');
                // MutationObserver 对象来监听imgToken下面的img元素的变化。当src属性发生变化时，会触发回调函数，并在获取到src后执行相应的操作。
                const observer = new MutationObserver(mutationsList => {
                  console.log(mutationsList, 'mutationsList');
                  for (const mutation of mutationsList) {
                    if (mutation.type === 'attributes' && mutation.attributeName === 'src') {
                      console.log(imgElement.src, 'imgElementimgElement');
                      imgElement.click();
                      // 当imgElement.src检测为base64的时候说明 图片已经加载好 那么imgElement.click();就已经实现了，所以关闭监听
                      if (/^(data:)?[a-z0-9+\/]+={0,2}$/i.test(imgElement.src)) {
                        observer.disconnect();
                      }
                    }
                  }
                });

                observer.observe(imgElement, { attributes: true });
              });
            },
            style: {}
          },
          '查看附件'
        );
      }
      return '';
    }
  }
];
// 跟进记录列表
const taskUserFollowLogColumns = [
  {
    title: '跟进人员',
    key: 'dockUser',
    width: 130
  },
  {
    title: '跟进时间',
    key: 'createDate',
    width: 130
  },
  {
    title: '跟进方式',
    key: 'followMethod',
    width: 100,
    render(row) {
      return h('div', {}, taskAssignMethodList.value.filter(item => item.value == `${row.followMethod}`)[0]?.label);
    }
  },
  {
    title: '联系电话',
    key: 'telPhone',
    width: 130
  },
  {
    title: '反馈状态',
    key: 'feedbackStatus',
    width: 100,
    render(row) {
      return h(
        'div',
        {},
        taskUserFeedBackStatusList.value.filter(item => item.value == `${row.feedbackStatus}`).length != 0
          ? taskUserFeedBackStatusList.value.filter(item => item.value == `${row.feedbackStatus}`)[0]?.label
          : taskCancelFeedbackStatusList.value.filter(item => item.value == `${row.feedbackStatus}`)[0]?.label
      );
    }
  },
  {
    title: '情况说明',
    key: 'followDesc',
    width: 150
  }
];
// 停电计划列表操作栏
const actionColumn = reactive({
  width: 150,
  title: '操作',
  key: 'action',
  fixed: 'right',
  render(record) {
    return h(TableAction as any, {
      style: 'button',
      actions: [
        {
          label: '编辑',
          type: isDisableActionButton(record, 'bianji') ? 'primary' : '',
          style: isDisableActionButton(record, 'bianji')
            ? {
                color: '#033FB4',
                backgroundColor: 'transparent',
                borderWidth: '0px',
                borderColor: ''
              }
            : {
                color: 'gray',
                backgroundColor: 'transparent',
                borderWidth: 0,
                borderColor: 'gray',
                cursor: 'not-allowed'
              },
          color: 'white',
          onClick: isDisableActionButton(record, 'bianji') ? handleEdit.bind(null, record) : '',
          ifShow: () => {
            return true;
          },
          auth: ['basic_list']
        },
        {
          label: '删除',
          style: {
            color: 'rgba(0, 0, 0, 0.6)',
            backgroundColor: 'transparent',
            borderWidth: '0px',
            borderColor: ''
          },
          color: 'transparent',
          type: 'default',
          title: '确认删除吗',
          showConfirm: true,
          confirmHandle: handleDelete.bind(null, record),
          // 根据业务控制是否显示 isShow 和 auth 是并且关系
          // ifShow: () => {
          //   return record.taskExecuteStatus?.executeStatus == '110';
          // },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['basic_list']
        }
      ],
      select: () => {
        // window['$message'].info(`您点击了，${key} 按钮`);
      }
    });
  }
});
// 停电用户列表操作栏
const actionUserColumn = reactive({
  width: 190,
  title: '操作',
  key: 'action',
  fixed: 'right',
  render(record) {
    return h(TableAction as any, {
      style: 'text',
      actions: [
        {
          title: '打印',
          iconType: 'print',
          style: {
            color: '#033FB4'
          },
          onClick: outPrint.bind(null, 'one', record),
          // 根据业务控制是否显示 isShow 和 auth 是并且关系
          ifShow: () => {
            return true;
          },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['basic_list']
        },
        {
          title: '更换派发负责方',
          style: {
            color: isDisableActionButton(record, 'changeEngineersTeam') ? '#033FB4' : 'gray',
            cursor: isDisableActionButton(record, 'changeEngineersTeam') ? 'pointer' : 'not-allowed'
          },
          iconType: 'changeEngineersTeam',
          onClick: isDisableActionButton(record, 'changeEngineersTeam')
            ? openModal.bind(null, 'changeAssignUserModal', record)
            : '',
          // 根据业务控制是否显示 isShow 和 auth 是并且关系
          ifShow: () => {
            return true;
          },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['basic_list']
        },
        {
          title: '取消派发',
          style: {
            color: isDisableActionButton(record, 'cancel') ? '#033FB4' : 'gray',
            cursor: isDisableActionButton(record, 'cancel') ? 'pointer' : 'not-allowed'
          },
          iconType: 'cancel',
          onClick: isDisableActionButton(record, 'cancel') ? handleCancelModal.bind(null, record, 'one') : '',
          // 根据业务控制是否显示 isShow 和 auth 是并且关系
          ifShow: () => {
            return true;
          },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['basic_list']
        },
        {
          // label: "恢复派发",
          style: {
            color: isDisableActionButton(record, 'record') ? '#033FB4' : 'gray',
            cursor: isDisableActionButton(record, 'record') ? 'pointer' : 'not-allowed'
          },
          iconType: 'record',
          title: '恢复派发',
          onClick: isDisableActionButton(record, 'record') ? backHandle.bind(null, record) : '',
          // 根据业务控制是否显示 isShow 和 auth 是并且关系
          ifShow: () => {
            return true;
          },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['basic_list']
        },
        {
          title: '派发',
          iconType: 'assign',
          style: {
            color: isDisableActionButton(record, 'assign') ? '#033FB4' : 'gray',
            cursor: isDisableActionButton(record, 'assign') ? 'pointer' : 'not-allowed'
          },
          onClick: isDisableActionButton(record, 'assign') ? openModal.bind(null, 'assignUserModal', record) : '',
          // 根据业务控制是否显示 isShow 和 auth 是并且关系
          ifShow: () => {
            return true;
          },
          // 根据权限控制是否显示: 有权限，会显示，支持多个
          auth: ['basic_list']
        },

        {
          title: '跟进回执',
          iconType: 'follow',
          style: {
            color: isDisableActionButton(record, 'follow') ? '#033FB4' : 'gray',
            cursor: isDisableActionButton(record, 'follow') ? 'pointer' : 'not-allowed'
          },
          onClick: isDisableActionButton(record, 'follow') ? handleFollowModal.bind(null, record) : '',
          ifShow: () => {
            return true;
          },
          auth: ['basic_list']
        }
      ],
      select: () => {
        // window['$message'].info(`您点击了，${key} 按钮`);
      }
    });
  }
});

const base64Url = ref('');
// 获取任务状态对应颜色
function getTableExecuteStatusColor(status) {
  let color, fontColor;
  switch(status){
    case '110':
      color = '#B32317';
      break;
    case '120':
      color = '#9933CC';
      break;
    case '130':
      color = '#FFA500';
      break;
    case '140':
      color = '#007AFF';
      break;
    case '150':
      color = '#CCCCCC';
      break;
    case '161':
      color = '#006600';
      break;
    case '162':
      color = '#8B0000';
      break;
  }

  // Calculate brightness and set font color accordingly
  const brightness = ((parseInt(color.slice(1, 3), 16) * 299) +
                      (parseInt(color.slice(3, 5), 16) * 587) +
                      (parseInt(color.slice(5, 7), 16) * 114)) / 1000;

  fontColor = brightness > 128 ? '#000000' : '#FFFFFF';

  return { backgroundColor: color, fontColor };
}
function imageToBase64(imgUrl) {
  return new Promise((resolve, reject) => {
    if (!String(imgUrl).trim()) reject(new Error('URL为空'));
    window.URL = window.URL || window.webkitURL;
    const xhr = new XMLHttpRequest();
    xhr.responseType = 'blob';
    xhr.open('get', imgUrl, true);
    // 携带请求头
    xhr.setRequestHeader('Authorization', `Bearer ${getToken()}`);
    xhr.onload = function () {
      if (this.status === 200) {
        // 得到一个blob对象
        const blob = this.response;
        // 至关重要
        const oFileReader = new FileReader();
        oFileReader.onloadend = function (e) {
          // 此处拿到的已经是 base64的图片了
          const base64 = e.target.result;
          resolve(`${base64}`);
        };
        oFileReader.readAsDataURL(blob);
      } else {
        // 解析错误
        reject(new Error('解析错误'));
      }
    };
    // 异常处理
    xhr.onerror = function () {
      resolve(false);
    };
    xhr.send();
  });
}
/**
 * @description 一键反馈（通过筛选条件和弹窗选择来）
 */
function oneTouchFeedbackSubmit(){
  oneTouchFeedbackRef.value?.validate(async(errors) => {
    if (!errors) {
      //从停电通知单查询中拿查询条件
      const {userTypeList,region,customerAddress,customerName} = temporaryFormUserParams
      let params = {
        taskId: taskIdToReceiptCode.value,
        userTypeList,
        region,
        customerAddress,
        customerName,
        ...oneTouchFeedbackForm.value
      }
      console.log("🚀 ~ oneTouchFeedbackSubmit ~ params:", params)
      const res= await batchPostApi('relay-task-service/api/v1/taskUserFeedbackLog/oneTouch',params)
      console.log("🚀 ~ oneTouchFeedbackSubmit ~ res:", res)
      if (!res.error) {
        message.success('一键反馈成功');
        actionRef.value.reload();
        showOneTouchFeedback.value = false;
        oneTouchFeedbackForm.value={}
        reloadUserTable();
      }
    }
  })
  
}
/**
 * @description 导出停电通知单
 */
async function userListExport(){
  console.log(transferValue.value,'transferValue')
        //从停电通知单查询中拿查询条件
      const {userTypeList,region,customerAddress,customerName} = temporaryFormUserParams
      let params = {
        taskId: taskIdToReceiptCode.value,
        userTypeList,
        region,
        customerAddress,
        customerName,
        fieldNameList:transferValue.value
      }
      
  exportExcal('/relay-task-service/api/v1/taskUser/export',params,'停电用户',()=>{
       message.success('导出成功');
      actionRef.value.reload();
      showUserListExport.value = false;
      transferValue.value=[]
      reloadUserTable();
  })

}
// 打印方法
async function outPrint(pringType, row: any = {}) {
  let res;
  if (pringType == 'one') {
    res = await printDataApi('relay-task-service/api/v1/taskUser/getPrintData', [row.id]);
  } else {
    res = await addAPI(
      { ...formUserParams.value, taskId: taskIdToReceiptCode.value,taskUserIdList: tableUserSelectKeys.value, },
      'relay-task-service/api/v1/taskUser/getOneTouchPrintData'
    );
  }
  printDataList.value = res.data;
  console.log(printDataList.value, 'printDataList.value');

  const LODOP = getLodop();

  if (LODOP) {
    LODOP.PRINT_INIT(''); // 初始化
    console.log(LODOP.CVERSION, 'LODOP.CVERSION');
    if (LODOP.CVERSION) {
      // 定义回调函数，当选择打印机操作返回时被调用
      LODOP.On_Return = function (TaskID, Value) {
        console.log('🚀 ~ Value:', Value);
        console.log('🚀 ~ TaskID:', TaskID);
        if (Value >= 0) {
          nextTick(() => {
            for (const item of printDataList.value) {
                let strFormHtml = `<body>${
                  document.getElementById(
                    `printDiv${printDataList.value.indexOf(item)}`
                  ).innerHTML
                }</body>`;
                LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4");
                LODOP.ADD_PRINT_HTM("5%", "1%", "98%", "95%", strFormHtml);
                LODOP.SET_PREVIEW_WINDOW(2, 0, 0, 800, 600, "");
                // 设置LODOP的许可证
                LODOP.SET_LICENSES("","0ED3A03D878934D52D4F98A3E5CD1DD8","","");
                LODOP.NewPage();
              
            }
            // 预览
            // LODOP.PREVIEW();
            // // 所有打印内容准备完毕，执行最终打印
            LODOP.PRINT();
          })
        }
      };

      // 弹出打印机选择对话框
      LODOP.SELECT_PRINTER();
    } else {
      console.error('C-Lodop 版本不可用，请检查！');
    }
  }
}
// 操作栏按钮是否禁用判断
function isDisableActionButton(record, buttonType) {
  // false 代表禁用 true代表不禁用
  // 停电通知单
  if (buttonType == 'follow') {
    // 跟进回执按钮
    const taskExecuteStatus = record.task.taskExecuteStatus.executeStatus;
    if (taskExecuteStatus == 150 || taskExecuteStatus == 161 || taskExecuteStatus == 162) {
      return false;
    }
    if (record.feedbackStatus != 330) {
      return false;
    }

    return true;
  } else if (buttonType == 'assign') {
    // 派发按钮
    const taskExecuteStatus = record.task.taskExecuteStatus.executeStatus;
    if (taskExecuteStatus == 150) {
      if (record.cancelAssignStatus == 420 || record.cancelAssignStatus == 430 || record.cancelAssignStatus == 440) {
        return false;
      }
    } else if (taskExecuteStatus == 161 || taskExecuteStatus == 162) {
      return false;
    } else if (record.assignStatus == 220 || record.assignStatus == 230 || record.assignStatus == 240) {
      return false;
    }
    return true;
  } else if (buttonType == 'changeEngineersTeam') {
    // 更换工程队
    if (record.assignStatus == 210) {
      return false;
    }
    return true;
  } else if (buttonType == 'cancel') {
    // 取消派发
    const taskExecuteStatus = record.task.taskExecuteStatus.executeStatus;
    if (taskExecuteStatus == 150) {
      if (record.cancelAssignStatus == 440 || record.cancelAssignStatus == 430) {
        return false;
      }
    } else if (
      record.task.taskExecuteStatus.executeStatus == 161 &&
      record.task.taskExecuteStatus.executeStatus == 162
    ) {
      return false;
    } else if (record.assignStatus == 230 || record.assignStatus == 240) {
      return false;
    }
    return true;
  } else if (buttonType == 'record') {
    const taskExecuteStatus = record.task.taskExecuteStatus.executeStatus;
    if (taskExecuteStatus == 150) {
      if (record.assignStatus != 240) {
        if (record.cancelAssignStatus != 440) {
          return false;
        }
      } else {
        return false;
      }
    } else if (taskExecuteStatus == 161 || taskExecuteStatus == 162) {
      return false;
    } else if (record.assignStatus != 240) {
      return false;
    }
    return true;
  }
  // 停电计划
  if (buttonType == 'bianji') {
    if (record.taskExecuteStatus?.executeStatus == '161' || record.taskExecuteStatus?.executeStatus == '162') {
      return false;
    }
    return true;
  }
}
// 跟进回执打开模态框
function handleFollowModal(record) {
  showFollowModal.value = true;
  followModalData.value.taskUserId = record.id;
}

// 自定义上传事件
// const customRequest = async (options) => {
//   console.log(options,'optionsoptions')
//   const formData = new FormData()
//   formData.append('fileList', options?.file.file as File)
//   const res = await uploadApi("relay-task-service/api/v1/task/batchUpload",formData)
//   if(res.data){
//     const {id,fileName} = res.data[0]
//     commonFileList.value.push({id,fileName})
//     console.log({id,fileName},'{id,fileName}')
//     console.log(commonFileList.value,'commonFileList')
//     options.onFinish()
//   }else{
//     options.onError()
//   }
//   // console.log(res,'resres')
//   // commonFileList.value = []
// }

function onIsAddChangeUploadData(options) {
  fileListUpload.value = options.fileList.map(item => item);
}
function onIsEditChangeUploadData(options) {
  fileListUpload.value = options.fileList.map(item => item);
}
function onIsEditDownUploadData(item) {
  const xhr = new XMLHttpRequest();
  if (item.url) {
    xhr.open('GET', item.url, true);
  } else {
    xhr.open('GET', item.status, true);
  }
  xhr.setRequestHeader('Authorization', `Bearer ${getToken()}`);
  xhr.responseType = 'blob';

  xhr.onload = function (e) {
    // 如果请求执行成功
    if (this.status == 200) {
      const blob = this.response;

      const filename = item.name;
      const a = document.createElement('a'); // blob.type = "application/octet-stream"; //创键临时url对象
      const url = URL.createObjectURL(blob);

      a.href = url;

      a.download = filename;

      a.click(); // 释放之前创建的URL对象

      window.URL.revokeObjectURL(url);
    }
  }; // 发送请求
  xhr.send();
}
// 恢复派发按钮 方法
async function backHandle(record) {
  const res = await backCancelUserApi('relay-task-service/api/v1/taskUser/recoverCancel', record.id);
  if (!res.error) {
    message.success('操作成功');
    actionRef.value.reload();
    reloadUserTable();
  }
}
// 取消派发打开模态框
function handleCancelModal(record, abc = 'one') {
  if (abc == 'one') {
    cancelModalData.value.taskUserIdList = [record.id];
    cancelModalData.value.type = abc;
  } else if (abc == 'all') {
    cancelModalData.value.type = abc;
  }
  showCancelModal.value = true;
}

async function followModalSubmit() {
  const verify = await handleValidateButtonClick();
  if (verify) {
    const res = await putSubmitAPI('relay-task-service/api/v1/taskUserFollow/follow', {
      ...followModalData.value
    });
    if (!res.error) {
      message.success('操作成功');
      reloadUserTable();
      cancelModalCancel();
    }
  }
}
async function cancelModalSubmit() {
  let url;
  let params;
  const successMessage = cancelModalData.value.type === 'one' ? '取消派发成功' : '一键取消成功';
  if (cancelModalData.value.type == 'one') {
    delete cancelModalData.value.type;
    url = 'relay-task-service/api/v1/taskUser/cancel';
    params = {
      ...cancelModalData.value
    };
  } else {
    delete cancelModalData.value.type;
    url = 'relay-task-service/api/v1/taskUser/oneTouchCancel';
    params = {
      taskId: taskIdToReceiptCode.value,
      ...formUserParams.value,
      ...cancelModalData.value
    };
  }
  const res = await putSubmitAPI(url, params);
  if (!res.error) {
    message.success(successMessage);
    actionRef.value.reload();
    reloadUserTable();
    cancelModalCancel();
  }
}
// 撤销取消
async function backCancelTaskModalSubmit(){
  const res = await revokeCancelTaskApi('relay-task-service/api/v1/task/revokeCancelTask', {
    taskIdList:[...tableSelectKeys.value],
  });
  if (!res.error) {
    message.success('撤销取消任务成功');
    cancelModalCancel();
    cancelDrawerButton();
  }
}
// 取消任务
async function cancelTaskModalMethod() {
  const res = await cancelTaskApi('relay-task-service/api/v1/task/cancelTask', {
    ...cancelTaskModalData.value,
    executeStatus:cancelTaskModalExecuteStatus.value,
  });
  if (!res.error) {
    message.success('取消任务成功');
    cancelModalCancel();
    cancelDrawerButton();
  }
}
// 取消任务按钮掉不通方法（列表上的，编辑里的）
async function cancelTaskModalSubmit() {
  if (cancelTaskModalType.value === '1') {
    // 在列表里面取消
    cancelTaskModalMethod();
  } else if (cancelTaskModalType.value === '2') {
    // 在编辑里面，待提交之后的状态，时间发生改变，弹出取消任务
    const params = {
      id: modelData.value.id,
      planItemId: modelData.value.planItemId,
      type: modelData.value.type,
      reason: modelData.value.reason,
      startTime: modelData.value.startTime,
      endTime: modelData.value.endTime,
      stationName: modelData.value.stationName,
      lineName: modelData.value.lineName,
      deviceName: modelData.value.deviceName,
      ranges: modelData.value.ranges,
      location: modelData.value.location,
      jobContent: modelData.value.jobContent,
      boolNotifyMedia: modelData.value.boolNotifyMedia,
      taskSource: modelData.value.taskSource,
      remark: modelData.value.remark,
      taskUserList: modalUserList.value,
      outId: modelData.value.outId,
      submitType: 1
    };
    const res = await editAPI(params, 'relay-task-service/api/v1/task', modelData.value.id);
    if (!res.error) {
      const formData = new FormData();
      formData.append('id', res.data);
      fileListUpload.value.forEach(item => {
        formData.append('fileList', item.file);
      });
      const res1 = uploadApi('relay-task-service/api/v1/task/batchUpload', formData);
      if (!res1.error) {
        cancelTaskModalMethod();
      }
    }
  }
}
async function batchAssignSubmit() {
  let engineersTeamId;
  if (taskUserDetailData.value.task?.taskExecuteStatus?.executeStatus == 150) {
    engineersTeamId = taskUserDetailData.value.engineersTeamId;
  } else {
    engineersTeamId = changeAssignUserModalData.value.engineersTeamId;
  }
  const res = await batchPutApi('relay-task-service/api/v1/taskUser/batchAssign', {
    taskUserIdList: [taskUserDetailData.value.id],
    type: 0,
    engineersTeamId
  });
  if (!res.error) {
    message.success('派发成功');
    actionRef.value.reload();
    cancelModalCancel();
    reloadUserTable();
  }
}
async function modalSubmit() {
  if (modalSubmitType.value == 'changeAssign') {
    // 更换派发人确认
    const res = await batchPutApi('relay-task-service/api/v1/taskUser/changeEngineersTeam', {
      taskUserIdList: [taskUserDetailData.value.id],
      engineersTeamId: changeAssignUserModalData.value.engineersTeamId
    });
    if (!res.error) {
      message.success('更换派发人成功');
      actionRef.value.reload();
      cancelModalCancel();
      reloadUserTable();
    }
  } else if (modalSubmitType.value == 'allChangeAssign') {
    const res = await batchPutApi('relay-task-service/api/v1/taskUser/oneTouchChangeEngineersTeam', {
      ...formUserParams.value,
      engineersTeamId: changeAssignUserModalData.value.engineersTeamId,
      taskId: taskIdToReceiptCode.value
    });
    if (!res.error) {
      message.success('一键更换成功');
      actionRef.value.reload();
      cancelModalCancel();
      reloadUserTable();
    }
  } else if (modalSubmitType.value == 'assign') {
    // 派发确认
    batchAssignSubmit();
  } else if (modalSubmitType.value == 'allAssign') {
    // 一键派发确认
    const res = await batchPutApi('relay-task-service/api/v1/taskUser/oneTouchAssign', {
      ...formUserParams.value,
      engineersTeamId: changeAssignUserModalData.value.engineersTeamId,
      taskId: taskIdToReceiptCode.value
    });
    if (!res.error) {
      message.success('一键派发成功');
      actionRef.value.reload();
      cancelModalCancel();
      reloadUserTable();
    }
  }
}
function openModal(code, row: any = {}) {
  console.log(row,'row')
  if (code === 'changeAssignUserModal') {
    // 更换派发负责方
    // if (tableUserSelectKeys.value.length < 1) {
    //   return message.warning("请先勾选数据");
    // }
    taskUserDetailData.value = row;
    showChangeAssignUserModal.value = true;
    modalSubmitType.value = 'changeAssign';
  } else if (code == 'allChangeAssignUserModal') {
    // 一键更换
    showChangeAssignUserModal.value = true;
    modalSubmitType.value = 'allChangeAssign';
  } else if (code === 'assignUserModal') {
    // 派发
    taskUserDetailData.value = row;
    if (row.task?.taskExecuteStatus?.executeStatus == '150') {
      batchAssignSubmit();
    } else {
      showChangeAssignUserModal.value = true;
      modalSubmitType.value = 'assign';
    }
  } else if (code === 'addNew') {
    // 任务新建
    modelData.value = {};
    modalType.value = 'isAdd';
    showModal.value = true;
  } else if (code === 'endTask') {
    console.log('============================')
    // 任务取消
    if (tableSelectKeys.value.length < 1) {
      message.warning('请先勾选数据');
      return;
    }
    /* 勾选通知列表 选中的数据执行状态是否一致判断
     * 原因：可以选不同状态取消的话，待派发没办法实现想要的逻辑（如果是待派发弹出模态框，派发取消通知默认为否），
     *        会造成其他也是否，会直接终结掉单子
     * 所以先要判断是否相同数据
     * */
    let first = '';
    let flag = true;
    console.log(tableSelectRows.value,'tableSelectRows.value')
    tableSelectRows.value?.forEach(item => {
      const value = item.taskExecuteStatus?.executeStatus
       if (first == '') {
        first = value;
        return;
      }
      if (first != value) {
        flag = false;
        return;
      }
    });
    if (!flag) {
      message.error('选择的数据执行状态必须相同，请重新选择');
      return;
    }

    /**
     * 选中的执行状态为待派发时 弹窗里派发通知为否
     *
     */
    if (first == '120') {
      cancelTaskModalData.value.boolAssignCancelNotice = 0;
    }
    cancelTaskModalExecuteStatus.value  = first
    cancelTaskModalData.value.taskIdList = [...tableSelectKeys.value];
    cancelTaskModalType.value = '1';
    showCancelTaskModal.value = true;
  }else if(code ===  'backEndTask'){
    // 撤销取消
    if (tableSelectKeys.value?.length < 1) {
      message.warning('请先勾选数据');
      return;
    }
    /* 勾选通知列表 选中的数据执行状态是否一致判断
     * */
    let first = '';
    let flag = true;
    tableSelectRows.value?.forEach(item => {
      const value = item.taskExecuteStatus?.executeStatus
       if (first == '') {
        first = value;
        return;
      }
      if (first != value) {
        flag = false;
        return;
      }
    });
    if (!flag) {
      message.error('选择的数据执行状态必须相同，请重新选择');
      return;
    }

    /**
     * 选中的执行状态为已取消
     *
     */
    if (first == '150') {
      showBackCancelTaskModal.value = true;
    } else{
       message.error('选择的数据执行状态不为已取消，请重新选择');
    }  
  } else if (code === 'allAssignUserModal') {
    modalSubmitType.value = 'allAssign';
    if (taskExecuteStatus.value == '150' || taskExecuteStatus.value == '161' || taskExecuteStatus.value == '162') {
      modalSubmit();
    } else {
      // 一键派发
      showChangeAssignUserModal.value = true;
    }
  }
}
function cancelModalCancel() {
  modalSubmitType.value = '';

  // 任务取消关闭
  cancelTaskModalType.value = '';
  cancelTaskModalExecuteStatus.value = '';
  cancelTaskModalData.value = {};
  showCancelTaskModal.value = false;

  // 撤销取消任务
  showBackCancelTaskModal.value = false;

  // 取消派发
  cancelModalData.value = {};
  showCancelModal.value = false;

  // 跟进回执关闭
  followModalData.value = {};
  showFollowModal.value = false;
  // 更换派发人关闭
  changeAssignUserModalData.value = {};
  showChangeAssignUserModal.value = false;
  // 任务用户回执详情关闭
  taskUserDetailData.value = {};
  showTaskUserDetailModal.value = false;
  showTaskUserFollowLog.value = false;
  showTaskUserFeedbackLog.value = false;
  showCancelTaskUserFeedbackLog.value = false;
  taskUserFeedbackLogData.value = [];
  cancelTaskUserFeedbackLogData.value = [];
  taskUserFollowLogData.value = [];
}
// function cancelTaskModalCancel() {

// }

function cancelDrawerButton() {
  modelData.value = {};
  fileListUpload.value = [];
  modalUserList.value = [];
  modalType.value = '';
  modalSelectKeys.value = [];
  tableSelectKeys.value = [];
  tableSelectRows.value = [];
  commonFileList.value = [];
  showModal.value = false;
  reloadTable();
  reloadUserTable();
}
function cancelUserFilterModal() {
  showUserFilterModal.value = false;
}
function selectedRowMethod(row) {
  return {
    style: 'cursor: pointer;',
    onClick: async () => {
      console.log(row,'row')
      // 把用户表的查询条件清空 以防上一个用户表查询条件影响改成展示数据
      formUserParams.value = {};
      temporaryFormUserParams.customerName = '';
      temporaryFormUserParams.customerAddress = '';
      temporaryFormUserParams.region = '';
      temporaryFormUserParams.userTypeList = [];
      temporaryFormUserParams.assignStatusList = [];
      temporaryFormUserParams.cancelAssignStatusList = []; 
      taskCode.value = ''
      taskExecuteStatus.value = ''
      taskIdToReceiptCode.value = ''

      taskIdToReceiptCode.value = row?.id;
      taskCode.value = row?.taskCode;
      
      // if(tableSelectKeys.value.indexOf(row.id) > -1){
      //   tableSelectKeys.value.splice(tableSelectKeys.value.indexOf(row.id),1)
      // }else{
      //   tableSelectKeys.value = [...tableSelectKeys.value,row.id]
      // }
      taskExecuteStatus.value = row.taskExecuteStatus?.executeStatus;
      // ==重复停电 警告弹窗数据（查选择的任务）==
      let repeatRes = await repeatModalTaskCodeApi(row?.taskCode,{})
      console.log(repeatRes,'repeatRes')
      if(repeatRes.data && repeatRes.data?.length > 0){
        repeatAlertModalData.value = repeatRes.data
        repeatAlert.value = true
      }
      // ===
      reloadUserTable();
    }
  };
}
function selectedRowStyleClass(row) {
  if (row.id == taskIdToReceiptCode.value) {
    return 'selectedRowStyle';
  }
}
const loadDataTable = async res => {
  const data = await pageAPI(
    {
      ...res,
      condition: {
        ...formParams.value
      }
    },
    'relay-task-service/api/v1/task'
  );
  // ==重复停电 警告弹窗数据（查所有）==
  let repeatRes = await repeatModalAllApi({})
  console.log(repeatRes,'repeatRes')
  if(repeatRes.data && repeatRes.data?.length > 0){
    repeatAlertModalData.value = repeatRes.data
    repeatAlert.value = true
  }
  // ===
  return data
};

const loadUserDataTable = async res => {
  const data = await pageAPI(
    {
      ...res,
      condition: {
        taskId: taskIdToReceiptCode.value,
        ...formUserParams.value
      }
    },
    'relay-task-service/api/v1/taskUser'
  );
  tableUserTotal.value = data.data?.total;
  return data;
};

async function userFilterFormSubmit() {
  formUserParams.value = { ...temporaryFormUserParams };
  reloadUserTable();
  message.success('查询成功');
  cancelUserFilterModal();
}
function userFilterReset() {
  formUserParams.value = {};
  temporaryFormUserParams.customerName = '';
  temporaryFormUserParams.customerAddress = '';
  temporaryFormUserParams.region = '';
  temporaryFormUserParams.userTypeList = [];
  temporaryFormUserParams.assignStatusList = [];
  temporaryFormUserParams.cancelAssignStatusList = [];
  cancelUserFilterModal();
  reloadUserTable();
  message.success('重置成功');
}
async function handleDetail(record: Recordable) {
  const res = await detailAPI('relay-task-service/api/v1/task', record.id);
  if (!res.error) {
    modelData.value = { ...res?.data };
    modelData.value.type = modelData.value?.type != null ? `${modelData.value.type}` : null;
    modelData.value.reason = modelData.value.reason != null ? `${modelData.value.reason}` : null;
    modelData.value.planItemId = modelData.value.planItemId != null ? `${modelData.value.planItemId}` : null;
    modelData.value.commonFileList.forEach(item => {
      item.url = item.fileUrl;
      item.name = item.fileName.substring(0, item.fileName.indexOf('_'));
      item.status = 'pending';
    });
    fileListUpload.value = modelData.value.commonFileList;

    modalType.value = 'isDetail';
    // 详情停电用户列表
    const userListRes = await ableParamslistAPI('relay-task-service/api/v1/taskUser', {
      taskId: modelData.value.id
    });
    if (!userListRes.error) {
      modalUserList.value = [...userListRes.data];
      showModal.value = true;
    }
  }
}
async function handleEdit(record) {
  const res = await detailAPI('relay-task-service/api/v1/task', record.id);
  if (!res.error) {
    modelData.value = { ...res.data };
    modelData.value.type = modelData.value.type != null ? `${modelData.value.type}` : null;
    modelData.value.reason = modelData.value.reason != null ? `${modelData.value.reason}` : null;
    modelData.value.planItemId = modelData.value.planItemId != null ? `${modelData.value.planItemId}` : null;
    oldTime.value.startTime = modelData.value.startTime;
    oldTime.value.endTime = modelData.value.endTime;
    if (modelData.value?.taskExecuteStatus?.executeStatus == '110') {
      for (const item of modelData.value.commonFileList) {
        blobToFile(item).then(file => {
          item.file = file;
        });
        // item.url = item.fileUrl;
        // item.fileName.substring(0, item.fileName.indexOf("_"))
        item.name = item.fileName.substring(0, item.fileName.indexOf('_'));
        item.status = item.fileUrl;
      }
    } else {
      modelData.value.commonFileList.forEach(item => {
        item.url = item.fileUrl;
        item.name = item.fileName.substring(0, item.fileName.indexOf('_'));
        item.status = 'pending';
      });
    }
    fileListUpload.value = modelData.value.commonFileList;

    modalType.value = 'isEdit';
    const userListRes = await ableParamslistAPI('relay-task-service/api/v1/taskUser', {
      taskId: modelData.value.id
    });
    if (!userListRes.error) {
      const arr: any = [];
      userListRes.data.forEach((item, index) => {
        arr.push({
          idDelete: index,
          customerName: item.customerName,
          voltageLevel: item.voltageLevel,
          accountNumber: item.accountNumber,
          phone: item.phone,
          customerAddress: item.customerAddress,
          electricalNumber: item.electricalNumber,
          region: item.region,
          postCode: item.postCode,
          userPriority: `${item.userPriority}`,
          remark: item.remark,
          boolShortTime: item.boolShortTime,
          userType: `${item.userType}`,
          id: item.id,
          accessPoint: item.accessPoint,
          address: item.address,
          accessPointName: item.accessPointName,
          capacity: item.capacity
        });
      });
      modalUserList.value = arr;
      showModal.value = true;
    }
  }
}
async function handleDelete(record: Recordable) {
  const res = await delAPI('relay-task-service/api/v1/task', record.id);
  if (!res.error) {
    message.success('删除成功');
    actionRef.value.setPagination({ page: 1 });
    actionRef.value.reload();
  }
}

function onCheckedRow(rowKeys, rowData) {
  if (modalType.value) {
    modalSelectKeys.value = [...rowKeys];
  } else {
    tableSelectRows.value = [...rowData];
    tableSelectKeys.value = [...rowKeys];
  }
}
function handleSortChange(options){
  if(options.columnKey === 'startTime'){
    actionRef.value.sorterTableData(options)
  }else if(options.columnKey === 'accountNumber'){
    actionUserRef.value.sorterTableData(options)
  }
}
function onUserCheckedRow(rowKeys, rowData){
  console.log("🚀 ~ onUserCheckedRow ~ rowData:", rowData)
  console.log("🚀 ~ onUserCheckedRow ~ rowKeys:", rowKeys)
  
    tableUserSelectRows.value = [...rowData];
    tableUserSelectKeys.value = [...rowKeys];
}
async function reloadTable() {
  actionRef.value.setPagination({ page: 1 });
  actionRef.value.reload();
  if (taskIdToReceiptCode.value) {
    const res = await detailAPI('relay-task-service/api/v1/task', taskIdToReceiptCode.value);
    if (res.data) {
      taskExecuteStatus.value = res.data.taskExecuteStatus.executeStatus;
    }
  }
}

function reloadUserTable() {
  tableUserSelectKeys.value = [];
  tableUserSelectRows.value = [];
  nextTick(()=>{
    actionUserRef.value.setPagination({ page: 1 });
    actionUserRef.value.reload();
  })

}

// 停电计划新建、编辑（待提交）中停电用户列表的导入方法
async function modalUserListImport(options) {
  console.log(options.file);
  if (options.file.status == 'pending') {
    processShow.value = true;
  } else if (options.file.status == 'uploading') {
    percentage.value = options.file.percentage;
    if (percentage.value > 50) {
      percentage.value -= 30;
    }
  } else if (options.file.status == 'finished') {
    percentage.value = options.file.percentage;
    const timer = setTimeout(() => {
      processShow.value = false;
      percentage.value = 0;
      clearTimeout(timer);
    }, 500);
  }

  // if()
  if (options?.file.status == 'error') {
    if (!modelData.value.startTime) {
      percentage.value = 0;
      processShow.value = false;
      message.error('因导入会做用户重复停电校验，请选择停电时间再进行导入');
      return;
    }
  }

  const arr: any = [];
  if (options?.file && options?.file.status == 'finished' && options?.event.target?.response) {
    if (JSON.parse(options?.event.target?.response).data?.code != 0) {
      showImportErrorModal.value = true;
      importErrorData.value = JSON.parse(options?.event.target?.response).data?.msg;
      return;
    }
    JSON.parse(options?.event.target?.response).data?.data.forEach((item, index) => {
      arr.push({
        idDelete: modalUserList.value.length == 0 ? index : modalUserList.value.length + index,
        customerName: item.customerName,
        voltageLevel: item.voltageLevel,
        accountNumber: item.accountNumber,
        phone: item.phone,
        customerAddress: item.customerAddress,
        electricalNumber: item.electricalNumber,
        region: item.region,
        postCode: item.postCode,
        userPriority: `${item.userPriority}`,
        remark: item.remark,
        boolShortTime: item.boolShortTime,
        userType: `${item.userType}`,
        accessPoint: item.accessPoint,
        address: item.address,
        accessPointName: item.accessPointName,
        capacity: item.capacity
      });
    });
    if (modalUserList.value.length == 0) {
      modalUserList.value = arr;
    } else {
      modalUserList.value = modalUserList.value.concat(arr);
    }
  }
}

// 停电计划新建、编辑（待提交）中停电用户列表的新增一行方法
function modalUserListAdd() {
  const arr: any = [];
  modalUserList.value.forEach(item => {
    if (item.isUseAddButton) {
      arr.push(item);
    }
  });
  for (let i = 0; i < arr.length; i++) {
    if (
      arr[i].userType === '' ||
      arr[i].accountNumber === '' ||
      arr[i].customerName === '' ||
      arr[i].customerAddress === '' ||
      arr[i].phone === ''
    ) {
      message.error('请把新增的停电用户类型、户号、客户名称、客户地址、联系方式填写完整');
      return;
    }
  }
  modalUserList.value.push({
    idDelete: modalUserList.value.length,
    isUseAddButton: true,
    userType: '',
    accountNumber: '',
    customerName: '',
    boolShortTime: 0,
    userPriority: '',
    region: '',
    electricalNumber: '',
    voltageLevel: '',
    customerAddress: '',
    phone: '',
    postCode: '',
    remark: '',
    accessPoint: '',
    address: '',
    accessPointName: '',
    capacity: ''
  });
  nextTick(() => {
    modalUserDataTableRef.value?.scrollTo(modalUserList.value.length * 58);
  });
}
// 停电计划新建、编辑（待提交）中停电用户列表的删除方法
function modalMutipleDelete() {
  const arr: any = [];
  modalUserList.value.forEach(item => {
    // 挑选不是选中的数据 添加到新数组里
    if (!modalSelectKeys.value.includes(item.idDelete)) {
      arr.push(item);
    }
  });
  // 清空选中key值
  modalSelectKeys.value = [];
  // 重新给新数组赋id
  arr.forEach((item, index) => {
    item.idDelete = index;
  });
  modalUserList.value = arr;
}
/**
 * 停电计划编辑中判断是否为待提交状态（待提交状态可以修改所有数据）
 * 不为待提交状态只能修改停电时间和送电时间
 * */
async function editModalMethod(params, isDaiTiJiao) {
  let uploadRes = [];
  if (isDaiTiJiao) {
    uploadRes = await uploadMethod(params);
    params.commonFileList = uploadRes;
  }
  const res = await editAPI(params, 'relay-task-service/api/v1/task', modelData.value.id);
  if (!res.error) {
    message.success('操作成功');
    cancelDrawerButton();
  }
}
async function uploadMethod(params) {
  const formData = new FormData();
  fileListUpload.value.forEach(item => {
    formData.append('fileList', item.file);
  });
  const commonFileListRes = await uploadApi('relay-task-service/api/v1/task/batchUpload', formData);
  const arr: any = [];
  commonFileListRes.data.forEach(item => {
    const { id, fileName } = item;
    arr.push({ id, fileName });
  });
  return arr;
}
// 停电计划新建、编辑中所有数据的确认或保存方法
function modalSubmitMethod(submitType) {
  formRef.value.validate(async errors => {
    if (!errors) {
      if (modalUserList.value.length > 0) {
        modalUserList.value.forEach(item => {
          delete item.idDelete;
          delete item.isUseAddButton;
        });
        const params = {
          id: modelData.value.id,
          planItemId: modelData.value.planItemId,
          type: modelData.value.type,
          reason: modelData.value.reason,
          startTime: modelData.value.startTime,
          endTime: modelData.value.endTime,
          stationName: modelData.value.stationName,
          lineName: modelData.value.lineName,
          deviceName: modelData.value.deviceName,
          ranges: modelData.value.ranges,
          location: modelData.value.location,
          jobContent: modelData.value.jobContent,
          boolNotifyMedia: modelData.value.boolNotifyMedia,
          taskSource: modelData.value.taskSource,
          outId: modelData.value.outId,
          remark: modelData.value.remark,
          taskUserList: modalUserList.value,
          submitType
        };

        if (modalType.value == 'isAdd') {
          const uploadRes = await uploadMethod(params);
          params.commonFileList = uploadRes;
          const res = await addAPI(params, 'relay-task-service/api/v1/task');
          if (!res.error) {
            message.success('操作成功');
            cancelDrawerButton();
          }
        } else if (modalType.value == 'isEdit') {
          if (modelData.value?.taskExecuteStatus?.executeStatus == '110') {
            editModalMethod(params, true);
          } else if (
            oldTime.value.startTime === modelData.value.startTime &&
            oldTime.value.endTime === modelData.value.endTime
          ) {
            // 停电时间 送电时间没变
            editModalMethod(params, false);
          } else {
            // 停电时间 送电时间改变
            if (modelData.value?.taskExecuteStatus?.executeStatus == '120') {
              // 执行状态为待派发 取消任务框默认不派发取消通知 直接终结掉
              cancelTaskModalData.value.boolAssignCancelNotice = 0;
            }
            cancelTaskModalExecuteStatus.value = modelData.value?.taskExecuteStatus?.executeStatus
            // 执行状态为其他
            cancelTaskModalData.value.taskIdList = [modelData.value?.id];
            cancelTaskModalType.value = '2';
            showCancelTaskModal.value = true;
          }
        }
      } else {
        message.error('停电通知单为空，请先导入数据');
      }
    }
  });
}
// 把时间戳转化为yyyy-mm-dd的格式或yyyy-mm-dd hh:mm
function getTime(timestamp, type) {
  if (type == 'day') {
    // 到日
    var date = new Date(timestamp); // 时间戳为10位需*1000，时间戳为13位的话不需乘1000
    const Y = date.getFullYear();
    const M = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1;
    const D = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
    return `${Y}-${M}-${D}`;
  } else if (type == 'minute') {
    // 到分
    var date = new Date(timestamp); // 时间戳为10位需*1000，时间戳为13位的话不需乘1000
    const Y = date.getFullYear();
    const M = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1;
    const D = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
    const H = date.getHours() < 10 ? `0${date.getHours()}` : date.getHours();
    const m = date.getMinutes() < 10 ? `0${date.getMinutes()}` : date.getMinutes();
    return `${Y}-${M}-${D} ${H}:${m}`;
  }
}
// 停电计划表单查询
function handleSubmit(values: Recordable) {
  if (values.startDate) {
    values.startDateBegin = getTime(values.startDate[0], 'day');
    values.startDateEnd = getTime(values.startDate[1], 'day');
  }
  if (values.endDate) {
    values.endDateBegin = getTime(values.endDate[0], 'day');
    values.endDateEnd = getTime(values.endDate[1], 'day');
  }
  delete values.startDate;
  delete values.endDate;
  formParams.value = { ...values };
  if (!formParams.value.executeStatusList) {
    formParams.value.executeStatusList = ['110', '120', '130', '140', '150'];
  } else if (formParams.value.executeStatusList.length == 0) {
    formParams.value.executeStatusList = null;
  }
  reloadTable();
}
// 停电计划表单重置
function handleReset(values: Recordable) {
  formParams.value = { ...values };
  formParams.value.executeStatusList = ['110', '120', '130', '140', '150'];
  reloadTable();
}
</script>
<style lang="scss" scoped>
.btn {
  margin: 30px 0px 30px 32px;
}

.modal-test {
  .box {
    width: 100%;
    height: 708px;

    .content {
      line-height: 50px;
      height: 50px;
      border-bottom: 1px solid #f0f0f0;

      .head {
        color: rgb(31, 34, 37);
        font-size: 17px;
        margin: 0 auto;
        text-align: center;
        font-weight: 500;
      }
    }

    .container-other {
      display: flex;
      justify-content: space-between;

      .head {
        line-height: 50px;
        height: 50px;
        color: #d9001c;
        font-size: 16px;
        margin: 0 auto;
        text-align: center;
        font-weight: bold;
      }

      .other-left {
        width: 47%;

        .info-list {
          height: 50px;
          border: solid 1px #f0f0f0;
          margin-top: -3px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          position: relative;

          .info {
            display: flex;
            align-items: center;
            justify-content: center;

            span {
              color: rgb(31, 34, 37);
              font-size: 14px;
              line-height: 50px;
              height: 50px;
              font-weight: 500;
            }
          }

          .info:nth-of-type(1) {
            width: 40%;
            border-right: solid 1px #f0f0f0;
          }

          .info:nth-of-type(2) {
            width: 35%;
            border-right: solid 1px #f0f0f0;
          }

          .info:nth-of-type(3) {
            width: 25%;
          }
        }
      }

      .other-right {
        width: 48%;
      }
    }
  }
}
</style>
<style lang="scss">
a.file-link {
  cursor: pointer;
}

.n-drawer-header__main {
  width: 100%;
}

.n-data-table-base-table {
  .n-data-table-table {
    .n-data-table-tbody {
      .selectedRowStyle {
        td {
          background-color: gainsboro !important;
        }
      }

      .repeatRowStyle {
        td {
          background-color: yellow !important;
        }
      }
    }
  }
}

.modal-apiDetail {
  .n-dialog__title {
    padding-bottom: 20px;
    border-bottom: 1px solid #f0f0f0;
  }
}

.modalGWForm {
  .n-form-item-label {
    width: 140px !important;
  }

  .n-form-item-label--right-mark {
    width: 140px !important;
  }
}

.add-btn {
  font-weight: bold;
  border: 0px;
  line-height: 34px;
  margin-left: 20px;
  cursor: pointer;
  color: #333639;
}

.add-btn:hover {
  color: #1890ff;
}

.w-l-12 {
  width: 13rem !important;
  margin-left: 1rem !important;
}
</style>
