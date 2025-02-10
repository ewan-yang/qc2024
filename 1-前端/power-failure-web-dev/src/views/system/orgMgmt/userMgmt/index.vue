<template>
  <div class="justify-between flex flex-row">
    <n-space class="flex-1 p-10px pt-20px" vertical :size="12" :style="{ backgroundColor: bgThemeColor }">
      <n-input v-model:value="pattern" clearable placeholder="搜索">
        <template #prefix>
          <n-icon class="search-icon">
            <icon-ant-design:search-outlined />
          </n-icon>
        </template>
      </n-input>
      <n-tree :node-props="nodeProps" default-expand-all :pattern="pattern" :data="treeDate" block-line />
    </n-space>

    <!-- 11111111111111111111111111111111111111111111 -->
    <div class="w-85% ml-15px">
      <n-card>
        <div class="flex flex-justify-around">
          <n-form ref="searchFormRef" :model="model" inline class="flex flex-wrap">

            <n-form-item path="name" label="名称" label-placement="left" class="ml-12">
              <n-input v-model:value="model.name" @keydown.enter.prevent class="width-150" />
            </n-form-item>
            <n-form-item path="code" label="编码" label-placement="left" class="ml-12">
              <n-input v-model:value="model.code" @keydown.enter.prevent class="width-150" />
            </n-form-item>

          </n-form>
          <n-button-group class="btn-group">
            <n-space class="flex flex-nowrap">
              <n-button color="#BAD8FB" style="color: black;" @click="searchFn">
                查询
              </n-button>
              <n-button type="primary" ghost @click="resetFn">
                重置
              </n-button>
            </n-space>
          </n-button-group>
        </div>
      </n-card>
      <n-card>
        <n-card>
          <n-space class="flex flex-nowrap mb-4">

            <n-button type="primary" @click="showPlanModalFn('add')">
              新增
            </n-button>
            <n-popconfirm @positive-click="sureDeleteAllFn" @negative-click="cancelDeleteAllFn">
              <template #trigger>
                <n-button type="error">
                  批量删除
                </n-button>
              </template>
              是否删除所选用户？
            </n-popconfirm>

          </n-space>
          <!-- 表格 -->
          <n-spin :show="loading">
            <n-data-table :columns="pageColumns" :data="data" :max-height="730" :bordered="false" scroll-x="1000"
              :single-line="false" :scrollbar-props="scrollbarProps" @update:checked-row-keys="handleCheck"
              :row-key="rowKey" />
            <!-- 分页 -->
            <div class="pageSty">
              <n-pagination v-model:page="pageData.pageIndex" :page-count="pageCount" :pageSizes="pageSizes"
                show-quick-jumper show-size-picker :on-update:page="pageChangeFn"
                :on-update:pageSize="pageSizeChangeFn" />
            </div>
          </n-spin>
        </n-card>

      </n-card>
    </div>
    <!-- 11111111111111111111111111111111111111111111 -->

  </div>




  <!-- 抽屉 -->
  <n-drawer v-model:show="showPlanModal" width="70%" placement="right">
    <n-drawer-content>
      <template #header>
        <p>{{ title }}</p>
      </template>
      <n-card :bordered="false" size="huge" role="dialog" aria-modal="true">
        <!-- 内容 -->
        <div>
          <n-form ref="userFormRef" :rules="rules" :model="userModel" inline class="flex flex-wrap flex-justify-around">

            <n-form-item path="name" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">名称</div>
              </template>
              <n-input v-model:value="userModel.name" @keydown.enter.prevent class="w-300" />
            </n-form-item>

            <n-form-item path="code" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">编码</div>
              </template>
              <n-input v-model:value="userModel.code" @keydown.enter.prevent class="w-300" />
            </n-form-item>

            <n-form-item v-if="passTag" path="password" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">密码</div>
              </template>
              <n-input v-model:value="userModel.password" @keydown.enter.prevent class="w-300" />
            </n-form-item>
            <n-form-item v-if="!passTag" path="editPassword" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">密码</div>
              </template>
              <n-input v-model:value="userModel.editPassword" @keydown.enter.prevent class="ml-3 w-300" />
            </n-form-item>
            <n-form-item path="workNumber" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">工号</div>
              </template>
              <n-input v-model:value="userModel.workNumber" @keydown.enter.prevent class="w-300" />
            </n-form-item>

            <n-form-item path="telephone" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">手机号</div>
              </template>
              <n-input v-model:value="userModel.telephone" @keydown.enter.prevent class="w-300" />
            </n-form-item>


            <n-form-item path="sex" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">性别</div>
              </template>
              <n-select v-model:value="userModel.sex" :options="sexOptionList" @keydown.enter.prevent class="w-300" />
            </n-form-item>

            <n-form-item path="unitList" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">部门</div>
              </template>

              <n-tree-select v-model:value="userModel.unitList" multiple :options="treeDate" @keydown.enter.prevent
                class="w-300" />
            </n-form-item>

            <n-form-item path="roleList" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">角色</div>
              </template>
              <n-select v-model:value="userModel.roleList" multiple :options="roleOPtions" @keydown.enter.prevent
                class="w-300" />
            </n-form-item>
            <n-form-item path="engineersTeamId" label-placement="left" class="mb-5">
              <template #label>
                <div class="w-110">工程队</div>
              </template>
              <n-select v-model:value="userModel.engineersTeamId" :options="engineersTeamList" @keydown.enter.prevent
                class="ml-3 w-300" clearable/>
            </n-form-item>
          </n-form>
        </div>

      </n-card>
      <template #footer>
        <!-- 按钮组 -->
        <div style="display: flex;justify-content: center;">
          <n-button-group class="btn-group">
            <n-space class="flex flex-nowrap">
              <n-button type="primary" @click="showPlanModal = false" ghost>
                取消
              </n-button>
              <n-button type="primary" @click="submitFn">
                确认
              </n-button>
            </n-space>
          </n-button-group>
        </div>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>

<script lang="ts" setup>
import { onMounted, onBeforeMount, reactive, h, ref, computed, nextTick, watch, } from 'vue';
import type { TreeOption } from 'naive-ui';
import { NButton, NPopconfirm, FormInst, useMessage, FormItemRule, FormRules, NSpace } from 'naive-ui';
import { listAPI,addAPI } from '~/src/service';
import { sha1 } from '~/src/utils/crypto';
import { fetchUnitTreeData, initPageAPI, addUserApi, delUserAPI, stopUserAPI, delAllUserAPI, editUserAPI } from '~/src/service/api/userMgmt';
import { convertOptions } from '~/src/utils/common/common';
import { useThemeStore } from '~/src/store';

const engineersTeamList =ref()
const scrollbarProps = ref({
  XScrollable: true
})

// ========查询相关====================
interface SearchModelType {
  name: string | null
  code: string | null
}


// 是否是查询数据标记
const searchFlag = ref(false)
// 查询条件
let condition: any = ref({})


const searchFormRef = ref<FormInst | null>(null)

const searchModelRef = ref<SearchModelType>({
  name: null,
  code: null
})

const model = searchModelRef

function resetFn() {
  model.value = {
    name: null,
    code: null
  }
  searchFlag.value = false
  pageData.pageIndex = 1
  pageInit()
}


function searchFn() {
  condition = { ...model.value }
  pageData.pageIndex = 1
  searchFlag.value = true
  searchDataFn(condition)
}

// 查询函数
async function searchDataFn(condition) {
  const dataArr = await initPageAPI({ ...pageData, condition })
  if (dataArr.data.count === 0 && pageData.pageIndex !== 0) {
    pageData.pageIndex -= 1
  }
  pageCount.value = dataArr.data.pageCount
  data.value = dataArr.data.data
}
// ========查询相关====================

// ======================================
// 表格表头数据
const createColumns = () => [
  {
    type: 'selection'
  },
  {
    title: '名称',
    key: 'name',
    width: 120,
  },
  {
    title: '编码',
    key: 'code',
    width: 120,
  },
  {
    title: '工号',
    key: 'workNumber',
    width: 120,
  },
  {
    title: '手机号',
    key: 'telephone',
    width: 120,
  },
  {
    title: '性别',
    key: 'sex',
    width: 60,
  },
  {
    title: '工程队',
    key: 'engineersTeamId',
    width: 120,
    render: (row) => {
      let name='';
      engineersTeamList.value?.forEach((item)=>{
        if(row.engineersTeamId == item.value){
           name = item.label
        }
      })  
      return h(
        'span',
        {},
        {
          default: () => {return name}
        }
      )
    }
  },
  {
    title: '角色',
    key: 'roleList',
    width: 120,
    render: (row) => {
      return h(
        'span',
        {},
        {
          default: () => {
            let roleArr: any = []
            //角色
            row.roleList.forEach(item => {
              roleArr.push(item.name)
            })

            return roleArr.join(' ').replace('"', '  ')
          }
        }
      )
    }
  },
  {
    title: '创建时间',
    key: 'createDate',
    width: 180,
  },
  {
    title: '状态',
    key: 'status',
    width: 70,
    render(row) {
      let status = ''
      switch (row.status) {
        case 0:
          status = '停用'
          break
        case 1:
          status = '启用'
          break

      }
      return h(
        'span',
        {},
        { default: () => `${status}` }
      )
    }
  },
  {
    title: '操作',
    width: 200,
    fixed: 'right',
    render(row) {
      return [
        h(NSpace
          , {}, [
          h(
            NButton,
            {
              size: 'small',
              type: 'primary',
              onClick: () => nextTick(() => {
                showPlanModalFn('edit', row)
              })
            },
            { default: () => '编辑' }
          ),

          h(
            NPopconfirm,
            {
              negativeText: '取消', positiveText: '确认',
              onPositiveClick: () => cancelItemFn(row),
              onNegativeClick: () => message.info('取消')
            },
            {
              trigger: () => h(
                NButton,
                {
                  size: 'small',
                  type: 'error',

                },
                { default: () => '删除' },
              ),
              default: () => h(
                'span',
                {},
                { default: () => '是否删除所选用户？' }
              ),
            }),
          h(
            NPopconfirm,
            {
              negativeText: '取消', positiveText: '确认',
              onPositiveClick: () => stopItemFn(row),
              onNegativeClick: () => message.info('取消')
            },
            {
              trigger: () => h(
                NButton,
                {
                  size: 'small',
                  type: 'warning'
                },
                {
                  default: () => {
                    row.status != 0 ? tag = '停用' : tag = '启用'
                    return tag
                  }
                },
              ),
              default: () => h(
                'span',
                {},
                {
                  default: () => {
                    row.status != 0 ? tagTitle = '是否停用所选用户？' : tagTitle = '是否启用所选用户？'
                    return tagTitle
                  }
                }
              ),
            })
        ]
        ),

      ]
    }
  }
]
// 停用启用
let tag = '启用'
let tagTitle = '是否停用所选用户？'
//删除项
async function cancelItemFn(row) {
  const data = await delUserAPI(row.id)
  if (!data.error) {
    pageInit()
    message.success('删除成功')
  } else {
    message.error('删除失败')
  }
}

// 停用启用
async function stopItemFn(row) {
  console.log(row.status)
  let data: any = null
  if (row.status == 0) {
    data = await stopUserAPI(row.id, 1)
  } else {
    data = await stopUserAPI(row.id, 0)
  }
  if (!data.error) {
    message.success('切换成功')
    pageInit()
  } else {
    message.error('切换失败')
  }
}

// 批量删除
function cancelDeleteAllFn() {
  message.info('取消')
}

async function sureDeleteAllFn() {
  const data = await delAllUserAPI(deleteArr.value)
  if (!data.error) {
    message.success('删除成功')
    pageInit()
  } else {
    message.error('删除失败')
  }
}
let deleteArr = ref('')

// 多选函数
function handleCheck(rowKeys) {
  deleteArr.value = rowKeys.join(', ')
}

function rowKey(row) {
  return row.id
}


// 表格数据
let data = ref([])

let pageColumns: any = reactive([])

// 表格列数据
pageColumns = createColumns()

// 分页
let pageData = reactive({ pageIndex: 1, pageSize: 10 })
let pageSizes = reactive([10, 20, 30, 40, 50])
let pageCount = ref(10)



const loading = ref(true)

// 页面初始化函数
async function pageInit(condition = {}) {
  loading.value = true
  const params = { ...pageData, condition }
  const dataArr = await initPageAPI(params)
  let timer = setTimeout(() => {
    loading.value = false
    clearTimeout(timer)
  }, 300)
  if (dataArr.data.count === 0 && pageData.pageIndex !== 0) {
    pageData.pageIndex -= 1
  }
  pageCount.value = dataArr.data.pageCount
  data.value = dataArr.data.data
}
// 监听分页数据
watch(() => pageData, () => {
  if (searchFlag.value) {
    searchDataFn(condition)
  } else {
    pageInit()
  }
}, { deep: true })

// 当前页改变
function pageChangeFn(page) {
  pageData.pageIndex = page
}
// 分页数改变
function pageSizeChangeFn(pageSizeIndex) {
  pageData.pageSize = pageSizeIndex
}


// =======================================================


// 抽屉
const showPlanModal = ref(false)


const message = useMessage()

// 抽屉框==================================================
// 停电计划详情抽屉框
let title = ref('增加')
let isAdd = ref(false)
// 密码框
let passTag = ref(true)

async function showPlanModalFn(type = '', row: any = {}) {
  showPlanModal.value = true
  switch (type) {
    case 'add':
      title.value = '新增'
      isAdd.value = true
      passTag.value = true
      userModel.value = {
        code: null,
        name: null,
        password: null,
        roleList: null,
        unitList: null,
        telephone: null,
        workNumber: null,
        sex: null,
        editPassword: null,
        engineersTeamId: null,
      }
      break;
    case 'edit':
      let roleArr: any = []
      //角色
      row.roleList.forEach(item => {
        roleArr.push(item.id)
      })
      // row.roleList = roleArr
      // 树
      let treeArr: any = []
      //角色
      row.unitList.forEach(item => {
        treeArr.push(item.id)
      })
      // row.unitList = treeArr
      userModel.value = { ...row }
      userModel.value.engineersTeamId = userModel.value.engineersTeamId  ? "" +  userModel.value.engineersTeamId : null
      userModel.value.roleList = roleArr
      userModel.value.unitList = treeArr
      title.value = '编辑'
      isAdd.value = false
      passTag.value = false
      break;
  }
}


//提交=============================================
async function submitFn() {
  if (isAdd.value) {
    if (await handleValidateButtonClick()) {
      if (userModel.value.password) {
        userModel.value.password = sha1(userModel.value.password);
      }
      if (userModel.value.roleList) {
        userModel.value.roleList = userModel.value.roleList?.map(item => {
          return { id: item }
        })
      }

      if (userModel.value.unitList) {
        userModel.value.unitList = userModel.value.unitList?.map(item => {
          return { id: item }
        })
      }
      delete userModel.value.editPassword
      let params = userModel.value
      const data = await addUserApi(params)
      if (!data.error) {
        message.success('添加成功')
        pageInit()
      } else {
        message.error('添加失败')
      }
      showPlanModal.value = false
    }
  } else {
    if (await handleValidateButtonClick()) {
      let {
        code,
        name,
        roleList,
        unitList,
        telephone,
        workNumber,
        sex,
        editPassword,
        engineersTeamId
      } = userModel.value
      roleList = roleList?.map(item => {
        return { id: item }
      })
      unitList = unitList?.map(item => {
        return { id: item }
      })
      let params;
      if(userModel.value.editPassword){
        params = {
          code,
          name,
          roleList,
          unitList,
          telephone,
          workNumber,
          sex,
          password: sha1(editPassword),
          engineersTeamId,
        }
      }else{
        params = {
          code,
          name,
          roleList,
          unitList,
          telephone,
          workNumber,
          sex,
          engineersTeamId
        }
      }

      console.log(params)
      const data = await editUserAPI(params, userModel.value.id)
      if (!data.error) {
        message.success('修改成功')
        pageInit()
      } else {
        message.error('修改失败')
      }
      showPlanModal.value = false
    }
  }
}

// 用户===============================
interface UserModelType {
  code: string | null
  name: string | null
  password: string | null
  roleList: Array<object> | null
  unitList: Array<object> | null
  telephone: string | null
  workNumber: string | null
  sex: string | null
  editPassword:string | null,
  engineersTeamId:Number | null,
}

let userFormRef = ref<FormInst | null>(null)

let userModelRef = ref<UserModelType>({
  code: null,
  name: null,
  password: null,
  roleList: null,
  unitList: null,
  telephone: null,
  workNumber: null,
  sex: null,
  editPassword: null,
  engineersTeamId: null,
})

let userModel = userModelRef

// 表单验证规则
const rules: FormRules = {
  code: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '编码不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  name: [
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
  password: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        const pattern = /^(?=.*[a-z])(?=.*\d)(?=.*[A-Z])[a-zA-Z\d!@#$%^&*()\_+=-{}[\];:'",.<>/?]{8,}$/;
        if (!value) {
          rule.message = '密码不能为空'
          return false
        }else if(!pattern.test(value)){
          rule.message = '密码必须包含大小写字母和数字的组合，可以使用特殊符号，至少8位';
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  editPassword:[
  {
      required: false,
      validator(rule: FormItemRule, value: string) {
        const pattern = /^(?=.*[a-z])(?=.*\d)(?=.*[A-Z])[a-zA-Z\d!@#$%^&*()\_+=-{}[\];:'",.<>/?]{8,}$/;
        if(!value){
          return true
        }else if(!pattern.test(value)){
          rule.message = '密码必须包含大小写字母和数字的组合，可以使用特殊符号，至少8位';
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  roleList: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '角色不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  unitList: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '部门不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  telephone: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '电话不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  workNumber: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '工号不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  sex: [
    {
      required: true,
      validator(rule: FormItemRule, value: string) {
        if (!value) {
          rule.message = '性别不能为空'
          return false
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ]
}

//验证函数
function handleValidateButtonClick() {
  return new Promise((resolve, reject) => {
    userFormRef.value?.validate((errors) => {
      if (!errors) {
        resolve(true)
      } else {
        reject(false)
      }
    })
  })

}


const theme = useThemeStore();
const bgThemeColor = computed(() => (theme.darkMode ? '#101014' : 'white'));

// 树表搜索项
const pattern = ref('');
// 树结构数据
const treeDate = reactive<TreeOption[]>([]);
const roleOPtions: any = [];
// 性别下拉项
const sexOptionList = [
  {
    label: '男',
    value: '男'
  },
  {
    label: '女',
    value: '女'
  }
];


// 重置treeData以适应n-tree组件
const resetTreeData = data => {
  data.forEach(item => {
    item.label = item.name;
    item.key = item.id;
    // eslint-disable-next-line no-unused-expressions
    item.children?.length && resetTreeData(item.children);
  });
  return data;
};

// 获取组织单树结构
const getTreeData = async () => {
  const { data } = await fetchUnitTreeData();
  console.log(data)
  treeDate.push(...resetTreeData(data));
  console.log(treeDate)
};

// 获取角色列表数据
const getRoleData = async () => {
  const { data } = await listAPI('relay-task-service/api/v1/role');
  console.log(data)
  roleOPtions.push(...convertOptions({ sourceData: data }));
  console.log(roleOPtions)
};
//获取工程队角色
const getEngineersTeamList= async () => {
  const { data } = await addAPI({},'relay-task-service/api/v1/engineersTeam/list');
 console.log(data,'data getEngineersTeamList')
 engineersTeamList.value =  data?.map((item)=>{
  return {label : item.name,
  value : item.id }
 })
};
// tree节点属性配置
const nodeProps = ({ option }: { option: TreeOption }) => {
  return {
    // 点击事件
    onClick() {
      const unitId = option.id;
      pageInit({ unitId });
    }
  };
};

onBeforeMount(() => {
  getTreeData();
  getRoleData();
  getEngineersTeamList();
});

onMounted(() => {
  pageInit()
});

</script>

<style scoped>
.mb-10 {
  margin-bottom: 10px;
}

.mt-12 {
  margin-top: -12px;
  margin-right: 6px;
}

.width-150 {
  width: 300px !important;
}

.width-250 {
  width: 260px !important;
}

.btn-group {
  width: 150px;
  margin-left: 26px;
}

.pageSty {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.n-gradient-text) {
  font-weight: bold;
}

.font-18 {
  font-size: 18px;
}

.w-110 {
  width: 50px;
}

.w-120 {
  width: 120px;
}

.btn-group {
  width: 150px;
  margin-left: 26px;
}

.w-300 {
  width: 500px !important;
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