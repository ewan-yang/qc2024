import { defineStore } from 'pinia';
import { listAPI, getListAPI } from "@/service/api/common";
import { getLovList } from '~/src/service/api/otherApi';
// import { isNull } from '../../../utils/common/typeof';
export const useDicStore = defineStore('dic-store', {
  state: () => ({
    permissionList: null,
    userList: null,
    noConcatUserList: null,
    operationList: null,
    executionStatusList: null,
    powerFailureTypeList: null,
    powerFailureReasonList: null,
    userPriorityList: null,
    voltageLevelList: null,
    cancelReasonList: null,
    cancelUserReasonList: null,
    taskUserFeedBackStatusList: null,
    taskCancelFeedbackStatusList: null,
    taskAssignMethodList: null,
    taskUserAssignStatusList: null,
    taskCancelAssignStatusList: null,
    taskUserTypeList: null,
    customerInfoConfigList: null,
  }),
  getters: {
    getPermission: state => state.permissionList,
    getUser: state => state.userList,
    getNoConcatUser: state => state.noConcatUserList,
    getOperation: state => state.operationList,
    getExecutionStatus: state => state.executionStatusList,
    getPowerFailureType: state => state.powerFailureTypeList,
    getPowerFailureReason: state => state.powerFailureReasonList,
    getUserPriority: state => state.userPriorityList,
    getVoltageLevel: state => state.voltageLevelList,
    getCancelReason: state => state.cancelReasonList,
    getCancelUserReason: state => state.cancelUserReasonList,
    getTaskUserFeedBackStatus: state => state.taskUserFeedBackStatusList,
    getTaskCancelFeedbackStatus: state => state.taskCancelFeedbackStatusList,
    getTaskAssignMethodList: state => state.taskAssignMethodList,
    getTaskUserAssignStatus: state => state.taskUserAssignStatusList,
    getTaskCancelAssignStatus: state => state.taskCancelAssignStatusList,
    getTaskUserType: state => state.taskUserTypeList,
    getCustomerInfoConfigList: state => state.customerInfoConfigList
  },
  actions: {
    loadData(types) {
      types.split(',').map(async type => {
        // if (type === 'permission' && !isNull(this.permissionList)) return;
        // if (type === 'user' && !isNull(this.userList)) return;
        let res;
        if (
          type !== "noConcatUser" &&
          type !== "user" &&
          type !== "permission" &&
          type !== "operation" &&
          type !== "roleUser"
        ) {
          res = await getLovList("relay-task-service/api/v1/lov", type);
        } else {
          if (type === "noConcatUser") {
            res = await listAPI("relay-task-service/api/v1/user");
          } else if (type === "roleUser") {
            //系统管理 角色管理页面
            res = await getListAPI("relay-task-service/api/v1/user");
          } else {
            res = await listAPI("relay-task-service/api/v1/" + type);
          }
        }
        if (type === 'permission') {
          this.permissionList = res.data.map(_item => {
            _item.label = `${_item.sysResource.name} (${_item.sysOperation.name})`;
            _item.value = _item.id;
            _item.code = `${_item.sysResource.code}:${_item.sysOperation.code}`;
            return _item;
          });
        } else if (type === 'user') {
          this.userList = res.data.map(_item => {
            _item.label = `${_item.name} (${_item.telephone || '-'})`;
            _item.value = _item.id;
            return _item;
          });
        } else if (type === "roleUser") {
          this.userList = res.data.map((_item) => {
            _item.label = `${_item.name} (${_item.telephone || "-"})`;
            _item.value = _item.id;
            return _item;
          });
        } else if (type === "noConcatUser") {
          this.noConcatUserList = res.data.map((_item) => {
            _item.label = `${_item.name}`;
            _item.value = _item.id;
            return _item;
          });
        } else if (type === "operation") {
          this.operationList = res.data.map((_item) => {
            _item.label = `${_item.name}`;
            _item.value = _item.id;
            return _item;
          });
        } else {
          const arr = res.data[0]?.lovLineList.map((_item) => {
            _item.label = `${_item.name}`;
            _item.value = _item.value;
            return _item;
          });
          if (type === "task_execute_status") {
            //执行状态
            this.executionStatusList = arr;
          } else if (type === "task_type") {
            //停电类型
            this.powerFailureTypeList = arr;
          } else if (type === "task_reason") {
            //停电原因
            this.powerFailureReasonList = arr;
          } else if (type === "task_user_priority") {
            //用户重要性
            this.userPriorityList = arr;
          } else if (type === "task_user_voltage_level") {
            //电压等级
            this.voltageLevelList = arr;
          } else if (type === "task_cancel_reason") {
            //停电任务取消原因
            this.cancelReasonList = arr;
          } else if (type === "task_user_cancel_reason") {
            //停电任务取消原因
            this.cancelUserReasonList = arr;
          } else if (type === "task_user_feedback_status") {
            //停电任务-用户反馈状态
            this.taskUserFeedBackStatusList = arr;
          } else if (type === "task_cancel_feedback_status") {
            //停电任务-取消通知反馈状态
            this.taskCancelFeedbackStatusList = arr;
          } else if (type === "task_assign_method") {
            arr.push({ name: "", value: "null" });
            //停电用户派发方式
            this.taskAssignMethodList = arr;
          } else if (type === "task_user_assign_status") {
            //停电任务-用户派发状态
            this.taskUserAssignStatusList = arr;
          } else if (type === "task_cancel_assign_status") {
            //停电任务-取消通知派发状态
            this.taskCancelAssignStatusList = arr;
          } else if (type === "task_user_type") {
            //停电任务-用户类型
            this.taskUserTypeList = arr;
          } else if (type === "customer_info_config") {
            //打印模板 固定信息
            this.customerInfoConfigList = arr;
          }
        }
      });
    }
  }
});
