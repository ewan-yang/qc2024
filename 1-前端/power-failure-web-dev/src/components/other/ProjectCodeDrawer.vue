<template>
  <n-drawer v-model:show="showPlanModal" width="55%" placement="right">
    <n-drawer-content>
      <template #header>
        <p>{{ title }}</p>
        <n-button type="default" @click="showPlanModal = false"
          style="position: absolute;right: 20px;top: 7px;">返回</n-button>
      </template>
      <n-card class="width-990" :bordered="false" size="huge" role="dialog" aria-modal="true">
        <!-- 内容 -->
        <div>
          <n-form ref="planFormRef" :rules="rules" :model="planModel" inline class="flex flex-wrap flex-justify-around">
            <n-form-item path="projectCode" label-placement="left">
              <template #label>
                <div class="w-110">工程编号</div>
              </template>
              <n-input v-model:value="planModel.projectCode" @keydown.enter.prevent class="w-300"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="projectAccount" label-placement="left">
              <template #label>
                <div class="w-110">工程账号</div>
              </template>
              <n-input v-model:value="planModel.projectAccount" @keydown.enter.prevent class="w-300"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="projectName" label-placement="left">
              <template #label>
                <div class="w-110">工程名称</div>
              </template>
              <n-input v-model:value="planModel.projectName" @keydown.enter.prevent class="width-748"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="projectType" label-placement="left">
              <template #label>
                <div class="w-110">项目类型</div>
              </template>
              <n-select v-model:value="planModel.projectType" @keydown.enter.prevent class="w-300" :options="optionsPro"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="constructionTeam" label-placement="left">
              <template #label>
                <div class="w-110">施工班组</div>
              </template>
              <n-input v-model:value="planModel.constructionTeam" @keydown.enter.prevent class="w-300"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="startTime" label-placement="left">
              <template #label>
                <div class="w-110">停役时间</div>
              </template>
              <n-date-picker type="date" v-model:value="planModel.startTime" @keydown.enter.prevent class="w-300"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="endTime" label-placement="left">
              <template #label>
                <div class="w-110">复役时间</div>
              </template>
              <n-date-picker type="date" v-model:value="planModel.endTime" @keydown.enter.prevent class="w-300"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="stationLineName" label-placement="left">
              <template #label>
                <div class="w-110">变电站/线路名称</div>
              </template>
              <n-input type="textarea" v-model:value="planModel.stationLineName" @keydown.enter.prevent
                class="width-748 h-170" :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="jobContent" label-placement="left">
              <template #label>
                <div class="w-110">主要工作内容</div>
              </template>
              <n-input type="textarea" v-model:value="planModel.jobContent" @keydown.enter.prevent class="width-748 h-170"
                :disabled="isDisabled" />
            </n-form-item>
            <n-form-item path="cosDevice" label-placement="left">
              <template #label>
                <div class="w-120">停役设备</div>
              </template>
              <n-input type="textarea" v-model:value="planModel.cosDevice" @keydown.enter.prevent class="width-748 h-170"
                :disabled="isDisabled" />
            </n-form-item>
          </n-form>
        </div>
        <!-- 按钮组 -->
        <div style="display: flex;justify-content: center;" v-show="showButton">
          <n-button-group class="btn-group">
            <n-space class="flex flex-nowrap">
              <n-button type="primary" @click="submitFn">
                确认
              </n-button>
              <n-button type="default" @click="showPlanModal = false">
                取消
              </n-button>
            </n-space>
          </n-button-group>
        </div>
      </n-card>
    </n-drawer-content>
  </n-drawer>
</template>
<script setup lang="ts" name="ProjectCodeDrawer">
import { ref } from 'vue'
import { NButton, FormItemRule, FormRules } from 'naive-ui'
import { FormInst, useMessage, } from 'naive-ui'
import { updatePlanItemApi, addPlanItemApi, getPlanItemAPI } from '../../service/api'

// 时间戳转换函数
function formatDate(timestamp, isMonth = false) {
  if (!timestamp) {
    return null
  }
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  let formattedDate
  if (isMonth) {
    formattedDate = `${year}-${month}`;
  } else {
    formattedDate = `${year}-${month}-${day}`;
  }
  return formattedDate;
}


// 抽屉
const showPlanModal = ref(false)

//选择框
let optionsPro = [
  {
    label: '代工',
    value: '代工'
  },
  {
    label: '更改',
    value: '更改'
  },
  {
    label: '基建',
    value: '基建'
  },
  {
    label: '基建(业扩)',
    value: '基建(业扩)'
  },
  {
    label: '检修',
    value: '检修'
  },
  {
    label: '平方米',
    value: '平方米'
  },
  {
    label: '消缺',
    value: '消缺'
  },
  {
    label: '业扩',
    value: '业扩'
  },
  {
    label: '销户',
    value: '销户'
  },
  {
    label: '用户',
    value: '用户'
  }
]

// 主页面传递的函数
const emit = defineEmits(['mainPageInit', 'init'])

const message = useMessage()

// 抽屉框==================================================
// 停电计划详情抽屉框
let title = ref('停电计划详情')
let showButton = ref(true)
let isDisabled = ref(false)
let isAdd = ref(false)

async function showPlanModalFn(id, type = '', row: any = {}) {
  showPlanModal.value = true
  let obj: any = {}
  switch (type) {
    case '':
      let { data } = await getPlanItemAPI(id)
      obj = { ...data }
      obj.startTime = new Date(data?.startTime).getTime()
      obj.endTime = new Date(data?.endTime).getTime()
      planModel.value = { ...obj }
      title.value = '停电计划详情'
      isDisabled.value = true
      showButton.value = false
      break;
    case 'view':
      obj = { ...row }
      obj.startTime = new Date(row?.startTime).getTime()
      obj.endTime = new Date(row?.endTime).getTime()
      planModel.value = { ...obj }
      title.value = '停电计划详情'
      isDisabled.value = true
      showButton.value = false
      break;
    case 'add':
      title.value = '新增停电计划'
      isDisabled.value = false
      showButton.value = true
      isAdd.value = true
      planModel.value = {
        projectCode: null,
        projectAccount: null,
        projectName: null,
        projectType: null,
        constructionTeam: null,
        startTime: null,
        endTime: null,
        stationLineName: null,
        jobContent: null,
        cosDevice: null
      }
      break;
    case 'edit':
      obj = { ...row }
      obj.startTime = new Date(row.startTime).getTime()
      obj.endTime = new Date(row.endTime).getTime()
      planModel.value = { ...obj }
      title.value = '修改停电计划'
      isDisabled.value = false
      showButton.value = true
      isAdd.value = false
      break;
  }
}

// 定义计划ID----planId
const planId = ref('')

//提交=============================================
async function submitFn() {
  if (isAdd.value) {
    if (await handleValidateButtonClick()) {
      let {
        projectCode,
        projectAccount,
        projectName,
        projectType,
        startTime,
        endTime,
        stationLineName,
        jobContent,
        cosDevice,
        constructionTeam
      } = planModel.value
      let params = {
        planId: planId.value,
        projectCode,
        projectAccount,
        projectName,
        projectType,
        startTime,
        endTime,
        stationLineName,
        jobContent,
        cosDevice,
        constructionTeam
      }
      params.startTime = formatDate(params.startTime)
      params.endTime = formatDate(params.endTime)
      const data = await addPlanItemApi(params)
      if (!data.error) {
        message.success('添加成功')
        emit('init')
        emit('mainPageInit')
      } else {
        message.error('添加失败')
      }
      showPlanModal.value = false
    }
  } else {
    if (await handleValidateButtonClick()) {
      let {
        projectCode,
        projectAccount,
        projectName,
        projectType,
        startTime,
        endTime,
        stationLineName,
        jobContent,
        cosDevice,
        constructionTeam
      } = planModel.value
      let params = {
        projectCode,
        projectAccount,
        projectName,
        projectType,
        startTime,
        endTime,
        stationLineName,
        jobContent,
        cosDevice,
        constructionTeam
      }
      params.startTime = formatDate(params.startTime)
      params.endTime = formatDate(params.endTime)
      const data = await updatePlanItemApi(params, planModel.value.id)
      if (!data.error) {
        message.success('修改成功')
        emit('init')
      } else {
        message.error('修改失败')
      }
      showPlanModal.value = false
    }
  }
}

// 停电计划表单===============================
interface PlanModelType {
  projectCode: string | null
  projectAccount: string | null
  projectName: string | null
  projectType: string | null
  constructionTeam: string | null
  startTime: number | null
  endTime: number | null
  stationLineName: string | null
  jobContent: string | null
  cosDevice: string | null
}

let planFormRef = ref<FormInst | null>(null)

let planModelRef = ref<PlanModelType>({
  projectCode: null,
  projectAccount: null,
  projectName: null,
  projectType: null,
  constructionTeam: null,
  startTime: null,
  endTime: null,
  stationLineName: null,
  jobContent: null,
  cosDevice: null
})

let planModel = planModelRef

const rules: FormRules = {
  projectAccount: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '账号不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  projectCode: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '编号不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  projectName: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '名称不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  projectType: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '类型不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  constructionTeam: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '施工班组不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  startTime: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '时间不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  endTime: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '时间不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  stationLineName: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '内容不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  jobContent: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '内容不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],

}

//验证函数
function handleValidateButtonClick() {
  return new Promise((resolve, reject) => {
    planFormRef.value?.validate((errors) => {
      if (!errors) {
        resolve(true)
      } else {
        reject(false)
      }
    })
  })

}

// 方法暴露
defineExpose({
  operateDrawer(params) {
    showPlanModal.value = true
    planId.value = params?.planId
    showPlanModalFn(params.id, params?.type, params?.row)
  }
})
</script>

<style lang="scss" scoped>
.w-110 {
  width: 110px;
}

.w-120 {
  width: 120px;
}

.btn-group {
  width: 150px;
  margin-left: 26px;
}

.w-300 {
  width: 300px !important;
}

:deep(.n-gradient-text) {
  font-weight: bold;
}

.width-748 {
  width: 748px !important;
}

.width-990 {
  width: 990px !important;
}

.h-170 {
  height: 170px;
}
</style>