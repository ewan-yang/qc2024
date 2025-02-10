<template>
  <EchartsBasicCrud ref="echartsBasicCrudRef" :submit="submit" :reset="reset" :is-show-action="false"
    :statistical-total-custom="true" :columns="columns" name="analysis-alarm-service/api/v1/powerCutRepeatStatistics"
    :crud-config="{ add: false, delete: false, edit: false }" :statisticalConfig="{
      isHaveTotal: true,
      totalPosition: 'echartTop',
      tablePosition: 'left',
    }">
    <!-- * totalPosition：值可以选择top、tableTop、echartTop
   * tablePosition：值可以选择left、right -->
    <template #formTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        重复停电查询
      </div>
    </template>
    <template #statisticalTotalTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        重复停电统计
      </div>
    </template>
    <template #tableTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        重复停电列表
      </div>
    </template>
    <template #echartsTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        重复停电图表
      </div>
    </template>
    <template #toolbar><n-button type="primary"
        @click="exportExcal('/analysis-alarm-service/api/v1/powerCutRepeatStatistics/export', { condition: { ...formParams } }, '重复停电统计列表')">导出</n-button></template>


    <!-- 111111111111111111111111111111111111111111111 -->

    <!-- 统计card自定义 -->
    <template #statisticalTotalCustom>
      <div class="flex justify-between ">
        <div class="w-70 h-28  p-l-5 p-r-5 text-center boxShow flex flex-col items-center"
          style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4 w-42" style="transform: scale(.8);">当期重复停电区域数</div>
          <div>
            <div><span
                :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{
                  statisticalTotalCustomData.powerCutRepeatRegionQty }}</span><span>个</span>
            </div>
          </div>
        </div>
        <div class="w-70 h-28 p-l-5 p-r-5  text-center boxShow flex flex-col items-center"
          style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4 w-50" style="transform: scale(.8);">去年同期重复停电区域数</div>
          <div>
            <div><span
                :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{
                  statisticalTotalCustomData.preYearPowerCutRepeatRegionQty }}</span><span>个</span>
            </div>
          </div>
        </div>
        <div class="w-70 h-28 p-l-5 p-r-5  text-center boxShow  flex flex-col items-center" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4" style="white-space: nowrap;transform: scale(.8);">停电区域重复数同比趋势
          </div>
          <div  style="width: 140%;"><span :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">
          {{
            statisticalTotalCustomData.powerCutRepeatRegionTb === null ? '--' :
            statisticalTotalCustomData.powerCutRepeatRegionTb
          }}
          </span><span>%</span>
          </div>
        </div>
      </div>
    </template>
    <!-- 111111111111111111111111111111111111111111111 -->

    <template #echarts>
      <!-- <div class="text-center font-700 fontSize-18 m-b-2" style="fontSize: 18px">重复停电次数的统计分析图</div> -->
      <div ref="regionChartRef" id="regionChart" class="h-90 w-100%"></div>
    </template>
  </EchartsBasicCrud>


 <!-- 测试modal -->
 <n-modal v-model:show="showVersionModal">
        <n-card title="详情" :bordered="false" size="huge" role="dialog" aria-modal="true" style="width: 400px">
            <template #header-extra>
                <n-button type="default" @click="showVersionModal = false">返回</n-button>
            </template>
            <!-- 内容 -->
            <div>
                <n-data-table :columns="columnsVersion" :data="detailData" :max-height="730" :bordered="false"
                    :single-line="false" />
            </div>
        </n-card>
    </n-modal>

    <!-- 关联通知单抽屉 -->
    <TaskDetailDrawer ref="modalRef" :transformId="transfromData?.id" transformIdType="task">
    </TaskDetailDrawer>

</template>

<script lang="ts" setup>
import { EchartsBasicCrud } from "~/src/components/echartsBusiness/echartCrud";
import type { ECharts, EChartsOption } from "echarts";
import { init } from "echarts";
import { onMounted, Ref, ref,nextTick, h } from "vue";
import { request } from "~/src/service/request";
import { exportExcal } from '~/src/utils/common/exportExcal'


// test===================================================

// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)

const showVersionModal = ref(false)
let columnsVersion = ref([
    {
        title: '停电计划编号',
        key: 'taskCode',
        width:140,
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
                { default: () => `${row.taskCode}` }
            )
        }
    },
    {
        title: '停电时间',
        key: 'startTime',

    }
])

let detailData = ref([])














// --------------------------------组件挂载-----------------------------
onMounted(async () => {
  regionChart = init(regionChartRef.value as HTMLElement);
  initApi();
});
// -------------------------------定义变量-----------------------------
/**
 * 查询表单数据 供给导出使用，统计数量和图表的不用formParams，initApi()时候直接传了
 */
const formParams = ref({})
/**
* 统计数据变量定义
*/
const statisticalTotalCustomData = ref({})
/**
 * 
 * 表格列表
 */
const columns = [
  {
    field: "region",
    label: "台区",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 140
    }
  },
  {
    field: "regionUserCount",
    label: "台区户数",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 70
    }
  },
  {
    field: "powerCutCount",
    label: "停电次数",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 70
    }
  },
  {
    field: "startTime",
    label: "停电时间",//停电时间
    formConfig: {
      hideSearch: false,
      component: "NDatePicker",
      componentProps: {
        type: "daterange",
        valueFormat: "yyyy-MM-dd",
      },
    },
    tableConfig: {
      width: 0,
      ifShow: false
    }
  },
  {
    field: "lineName",
    label: "线路名称",//线路名称
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 0,
      ifShow: false,
      render(row) {
        return h(
          'span',
          {
            style: {
              display: 'inline-block',
              height: '3rem',
              lineHeight: '3rem',
            },
          },
          { default: () => `${row.lineName ? row.lineName : ''}` }
        )
      }
    }
  },
  {
    field: "",
    label: "操作",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 100,
      render(row) {
        return h(
          'span',
          {
            style: {
              display: 'inline-block',
              height: '3rem',
              lineHeight: '3rem',
              color: '#007AFF',
              cursor: 'pointer'
            },
            onClick: () => detailFn(row)
          },
          { default: () => `详情` }
        )
      }
    }
  },
];

// 查询条件
let condition: any = {}

// 详情
async function detailFn(row) {
  console.log(row)
  showVersionModal.value =true
  condition.region = row.region
  console.log(condition)
  const res = await request.post(
    `analysis-alarm-service/api/v1/powerCutRepeatStatistics/getDetailByRegion`,
    { ...condition }
  );
  console.log(res.data)
  detailData.value = res.data
}

/**
 * 颜色代码
 */
const colorArr = [
  "#148C78",
  "#41B496",
  "#81D3F8",
  "#96D4CF",
];

/**
 * 
 * 统计数据字体大小
 */
const statisticalFontSize = "30px";
/**
 * 统计数据字体粗细
 */
const statisticalFontWeight = 700;

const regionChartRef: Ref<HTMLElement | null> = ref(null);
let regionChart: ECharts;


// --------------------------------处理图表数据 统计数据Method-------------------------------

/**
 * 获取统计数据 赋给statisticalColumn，同时赋予图标数据，渲染到页面上,
 */
async function initApi(params = {}) {
  const res = await request.post(
    `analysis-alarm-service/api/v1/powerCutRepeatStatistics/total`,
    { ...params }
  );
  statisticalTotalCustomData.value = res.data
  regionChartOption(res);
}

/**
 * 各区域平均接收时长统计分析图
 */
async function regionChartOption(res) {
  //图标数据
  let data1;
  let data2;
  if (res.data.powerCutRepeatCountMap) {
    data1 = Object.keys(res.data.powerCutRepeatCountMap);
    data2 = Object.values(res.data.powerCutRepeatCountMap);
  }

  const option: EChartsOption = {
    title: {
      text: "重复停电次数的统计分析图",
      left: "center",
      show:true,
      top: 0,
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    toolbox: {
      show: true,
      feature: {
        // dataZoom: {
        //   yAxisIndex: 'none'
        // },
        saveAsImage: {}
      },
      top: 0,
    },
    legend: {},
    grid: {
      height: '80%',
      right: '3%',
      bottom: '0',
      containLabel: true
    },
    yAxis: {
      type: 'value',
      name: '天',
      // interval: 1
    },
    xAxis: {
      type: 'category',
      data: data1
    },
    series: [
      {
        type: 'bar',
        data: data2,
        itemStyle: {
          color: colorArr[1]
        }
      }
    ]
  };
  regionChart.setOption(option);
}

//-----------------------------------------------------------------------------------------------
//---------------------------------查询和重置按钮触发事件-------------------------------------------
//虽然把查询表单的submit放了出来 但是只用执行统计修改statisticalColumn的活就行 我并没有阻止submit以前的功能执行
/**  这是useCurdEvents中 查询的代码 我只是识别了有传入submit方法就走自己传入的，
 *   但是没有return 所以params.value = unref(values)和reload（）都会继续执行
 *   所以我们自己的写的方法只用执行统计接口就可以
 * 
 *   async function submit(values: Recordable) {
      if (props.submit && isFunction(props.submit)) {
        await props.submit(values);
      }
      params.value = unref(values);
      reload();
    }
 */
function submit(params) {
  formParams.value = params
  let { startDateBegin, startDateEnd, lineName } = params
  condition = { startDateBegin, startDateEnd, lineName }
  initApi(params);
}
//reset和submit同理
function reset() {
  condition = {}
  formParams.value = {}
  initApi({});
}
//------------------------------------------------------
</script>

<style scoped>
.w-70 {
  width: 8.3rem;
}
</style>