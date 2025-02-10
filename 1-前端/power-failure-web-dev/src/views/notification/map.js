import { useDicStore } from '@/store';
const dic = useDicStore();
dic.loadData('task_execute_status,task_type,task_reason,noConcatUser,task_user_priority,task_user_voltage_level');
//取字典中的数据并进行加工 转成map 方便在表格中读取显示
const executionStatusRes =  JSON.parse(JSON.stringify(dic.getExecutionStatus))
const powerFailureReasonRes =  JSON.parse(JSON.stringify(dic.getPowerFailureReason))
const powerFailureTypeRes =  JSON.parse(JSON.stringify(dic.getPowerFailureType))
const assignedByRes =  JSON.parse(JSON.stringify(dic.getNoConcatUser))
const userPriorityRes = JSON.parse(JSON.stringify(dic.getUserPriority))
const voltageLevelRes = JSON.parse(JSON.stringify(dic.getVoltageLevel))



//执行状态map
export const executeStatusMap = new Map(executionStatusRes?.map(item=>[item.value,item]))
//停电原因map
export const powerFailureReasonMap = new Map(powerFailureReasonRes?.map(item=>[item.value,item]))
//停电类型map
export const powerFailureTypeMap = new Map(powerFailureTypeRes?.map(item=>[item.value,item]))
//任务派发人员map
export const assignedByMap = new Map(assignedByRes?.map(item=>[item.value,item]))
//用户重要性map
export const userPriorityMap = new Map(userPriorityRes?.map(item=>[item.value,item]))
//电压等级map
export const voltageLevelMap = new Map(voltageLevelRes?.map(item=>[item.value,item]))


//预告警map
export const advanceWarningMap = new Map([
  [1, { label: "正常", style: { display: 'block',width: '110px',height: '30px',lineHeight: '30px',borderRadius: '5px',textAlign: 'center',color: 'white',backgroundColor: "#70B603", } }],
  [0, { label: "下达超时告警", style: { display: 'block',width: '110px',height: '30px',lineHeight: '30px',borderRadius: '5px',textAlign: 'center',color: 'white',backgroundColor: "#D9001D", } }],
  [2, { label: "下达风险预警", style: { display: 'block',width: '110px',height: '30px',lineHeight: '30px',borderRadius: '5px',textAlign: 'center',color: 'white',backgroundColor: "#F59A23",  } }],
])
