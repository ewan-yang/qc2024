<template>
  <div>
    <BasicList
      modal-type="d"
      :columns="columns"
      name="lov"
      :show-detail-button="true"
      :format-edit="formatEdit"
      @open-detail="toggleDetail(0)"
      @close-detail="toggleDetail(1)"
    >
      <template #lovLineList="slotScope: slotType">
        <n-data-table
          :columns="isEdit ? editColumns(slotScope) : editColumns1(slotScope)"
          :data="slotScope.model[slotScope.field]"
        />
        <n-button
          v-if="isEdit"
          class="absolute right-1 top--9"
          type="primary"
          ghost
          round
          size="small"
          @click="addcolumn(slotScope)"
        >
          <template #icon>
            <n-icon>
              <icon-ant-design:plus-outlined />
            </n-icon>
          </template>
          新建
        </n-button>
      </template>
    </BasicList>
  </div>
</template>
<script lang="ts" setup>
import { h, ref } from 'vue';
import { NInput, NButton, NInputNumber } from 'naive-ui';
import type { slotType } from '@/components/business/Form/index';
import { detailAPI } from '~/src/service';
import { isArray, isNullOrUnDef } from '../../../utils/common/typeof';
const columns = [
  {
    title: '名称',
    key: 'name',
    showSearch: true
  },
  {
    title: '编码',
    key: 'code',
    showSearch: true
  },
  {
    title: '备注',
    key: 'remark',
    skip: true
  },
  // {
  //   title: '测试',
  //   key: 'createDate2',
  //   showSearch: true,
  //   component: 'NDatePicker',
  //   componentProps: {
  //     type: 'datetime',
  //     valueFormat: 'yyyy.MM.dd HH:mm:ss'
  //   }
  // },
  {
    ifShow: false,
    slot: 'lovLineList',
    title: '值列表',
    key: 'lovLineList',
    giProps: {
      span: 2
    }
  },
  {
    title: '状态',
    key: 'status',
    render(row) {
      return h('text', null, row.status ? '启用' : '禁用');
    }
  }
];
const isEdit = ref(true);

const editColumns = (slotScope: slotType) => [
  {
    title: '编码',
    key: 'code',
    render(row, index) {
      return h(NInput, {
        value: row.code,
        onUpdateValue(v) {
          slotScope.model[slotScope.field][index].code = v;
        }
      });
    }
  },
  {
    title: '名称',
    key: 'name',
    render(row, index) {
      return h(NInput, {
        value: row.name,
        onUpdateValue(v) {
          slotScope.model[slotScope.field][index].name = v;
        }
      });
    }
  },
  {
    title: '值',
    key: 'value',
    render(row, index) {
      return h(NInput, {
        value: row.value,
        onUpdateValue(v) {
          slotScope.model[slotScope.field][index].value = v;
        }
      });
    }
  },
  {
    title: '排序',
    key: 'sort',
    render(row, index) {
      return h(NInputNumber, {
        value: row.sort,
        onUpdateValue(v) {
          slotScope.model[slotScope.field][index].sort = v;
        }
      });
    }
  },
  {
    title: '备注',
    key: 'remark',
    render(row, index) {
      return h(NInput, {
        value: row.remark,
        onUpdateValue(v) {
          slotScope.model[slotScope.field][index].remark = v;
        }
      });
    }
  },
  {
    title: '操作',
    key: 'actions',
    render(_row, index) {
      return h(
        NButton,
        {
          size: 'small',
          ghost: true,
          type: 'error',
          onClick: () => {
            slotScope.model[slotScope.field].splice(index, 1);
          }
        },
        { default: () => '删除' }
      );
    }
  }
];

const editColumns1 = (_slotScope: slotType) => [
  {
    title: '编码',
    key: 'code'
  },
  {
    title: '名称',
    key: 'name'
  },
  {
    title: '值',
    key: 'value'
  },
  {
    title: '排序',
    key: 'sort'
  },
  {
    title: '备注',
    key: 'remark'
  },
  {
    title: '操作',
    key: 'actions'
  }
];

function addcolumn(slotScope: slotType) {
  if (!isArray(slotScope.model[slotScope.field])) slotScope.model[slotScope.field] = [];
  if (slotScope.model[slotScope.field].length > 0) {
    const lastColumns = slotScope.model[slotScope.field].at(-1);
    if (isNullOrUnDef(lastColumns.code) || isNullOrUnDef(lastColumns.name) || isNullOrUnDef(lastColumns.sort)) {
      window.$message?.warning('编码，名称，排序为必填项，请填写完整');
      return;
    }
  }
  // if (slotScope.model[slotScope.field].length === 1) {
  //   window.$message?.warning('只能添加一条');
  //   return;
  // }
  slotScope.model[slotScope.field].push({ lovId: slotScope.model[slotScope.field][0]?.lovId || '' });
}
async function formatEdit(values) {
  const res = await detailAPI('relay-task-service/api/v1/lov', values.id);
  values.lovLineList = res.data.lovLineList;
  values.lovLineList.forEach((item)=>{
    delete item.status
    delete item.createBy
    delete item.createDate
    delete  item.updateBy
    delete  item.updateDate
  })
   
  return values;
}

function toggleDetail(type) {
  isEdit.value = Boolean(type);
}
</script>
