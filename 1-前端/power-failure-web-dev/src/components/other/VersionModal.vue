<template>
    <!-- 测试modal -->
    <n-modal v-model:show="showVersionModal">
        <n-card title="版本记录" :bordered="false" size="huge" role="dialog" aria-modal="true" style="width: 600px">
            <template #header-extra>
                <n-button type="default" @click="showVersionModal = false">返回</n-button>
            </template>
            <!-- 内容 -->
            <div>
                <n-data-table :columns="columnsVersion" :data="versionData" :max-height="730" :bordered="false"
                    :single-line="false" />
            </div>
        </n-card>
    </n-modal>

    <!-- 关联通知单抽屉 -->
    <TaskDetailDrawer ref="modalRef" :transformId="transfromData?.taskExecuteStatus?.taskId" transformIdType="task">
    </TaskDetailDrawer>
</template>
<script setup lang="ts" name="VersionModal">
import { ref, nextTick, h } from 'vue'
import { detailAPI as getVersionDataAPI } from "~/src/service/api/common";

import { useDicStore } from "~/src/store";
let dic = useDicStore();
let userTypeList: any = [];


// test===================================================

// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)

const showVersionModal = ref(false)
let columnsVersion = ref([
    {
        title: '版本号',
        key: 'version',
        width:120,
        render(row) {
            return h(
                'span',
                {
                    style: {
                        color: '#0062E1',
                        cursor: 'pointer'
                    },
                    onClick: () => {
                        transfromData.value = row
                        nextTick(() => {
                            modalRef.value?.open()
                        })
                    }

                },
                { default: () => `V${row.version}.0` }
            )
        }
    },
    {
        title: '创建时间',
        key: 'createDate',

    },
    {
        title: '创建人',
        key: 'createBy',
        render(row) {
            return h(
                'span',
                {},
                { default: () => `${userTypeList[row.createBy-1]}` }
            )
        }
    },
])

let versionData = ref([
    {
        version: 1,
        createDate: 1,
        createBy: 1,
        taskExecuteStatus: {
            taskId: 1
        }
    },
    {
        version: 1,
        createDate: 1,
        createBy: 1,
        taskExecuteStatus: {
            taskId: 1
        }
    }
])
defineExpose({
    async versionPageInit(id) {
        await dic.loadData("user");
        //字典中value值为字符串 直接放在下拉框里用显示不出来，如果改dic文件字典里返回的值的话，影响到了停电通知页面，变动大
        dic.getUser?.forEach((item) => {
            userTypeList.push(item.name);
        });
        let { data } = await getVersionDataAPI('relay-task-service/api/v1/task/searchVersionList', id);
        versionData.value = data
        showVersionModal.value = true
    }
})


// test===================================================

</script>