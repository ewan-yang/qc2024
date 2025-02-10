<template>
  <EchartsBasicCrud ref="echartsBasicCrudRef" :submit="submit" :reset="reset" :is-show-action="false"
    :statisticalColumn="statisticalColumn" :columns="columns"
    name="analysis-alarm-service/api/v1/releaseDurationStatistics"
    :crud-config="{ add: false, delete: false, edit: false }">
    <template #formTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        下发时长查询
      </div>
    </template>
    <template #statisticalTotalTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2 flex" style="fontSize: 18px">
        各工程队下发时长统计<n-select class="w-30 m-l-2" label-field="name" value-field="id"
          v-model:value="engineersTeamIdSelectVal" :options="engineersTeamOptions" />
      </div>
    </template>
    <template #tableTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        下发时长列表
      </div>
    </template>

    <template #echartsTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        下发时长图表
      </div>
    </template>

    <template #statisticalTotalEchart>
      <div class="w-45 h-28   text-center boxShow"
        style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);width: 220px;padding-left: 2px;">
        <p style="margin-bottom: -28px;">各时段统计图</p>
        <div ref="timeIntervalChartRef" id="timeIntervalChart"
          style="width: 220px;height: 200px;position: relative;top: -30px;left: 0px;"></div>

      </div>
    </template>
    <template #toolbar><n-button type="primary"
        @click="exportExcal('/analysis-alarm-service/api/v1/releaseDurationStatistics/export', { condition: { ...formParams } }, '下发时长统计列表')">导出</n-button></template>

    <template #echarts>
      <div>
        <div ref="timeChartRef" class="h-90 w-100%"></div>
        <div ref="taskUserTypeTimeRef" class="h-90 w-100%"></div>
      </div>
    </template>
  </EchartsBasicCrud>
  <!-- 回执单号抽屉 -->
  <TaskUserDetailDrawer ref="taskUserDetailDrawerRef" :transformId="transformData?.id"></TaskUserDetailDrawer>

  <!-- 关联通知单抽屉 -->
  <TaskDetailDrawer ref="taskDetailDrawerRef" :transformId="transformData.taskId" transformIdType="task">
  </TaskDetailDrawer>
</template>

<script lang="ts" setup>
import { EchartsBasicCrud } from "~/src/components/echartsBusiness/echartCrud";
import type { ECharts, EChartsOption } from "echarts";
import { init } from "echarts";
import { onMounted, Ref, ref, reactive, h, watch, } from "vue";
import { request } from "~/src/service/request";
import { useDicStore } from "~/src/store";
import { exportExcal } from '~/src/utils/common/exportExcal'
let dic = useDicStore();


// -------------------------------定义变量-----------------------------
/**

 */
const formParams = ref({})
/**
 * 公共弹窗抽屉的dom 和数据变量
 */
const taskDetailDrawerRef = ref(null)
const taskUserDetailDrawerRef = ref(null)
const transformData = ref({})
/**
 * 各区域下达时长对比分析图图表dom
 */
const timeChartRef: Ref<HTMLElement | null> = ref(null);
let timeChart: ECharts;

/**
 * 不同来源的时长图表dom
 */
const taskUserTypeTimeRef: Ref<HTMLElement | null> = ref(null);
let taskUserTypeTimeChart: ECharts;
/**
 * 统计小图表dom
 */
const timeIntervalChartRef: Ref<HTMLElement | null> = ref(null);
let timeIntervalChart: ECharts;

const engineersTeamIdSelectVal = ref(0)
const engineersTeamOptions = ref([])
/**
 * 表格列表
 * 
 */
const columns = [
  {
    field: "receiptCode",
    label: "回执单号",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 110,
      render(row) {
        return h(
          "span",
          {
            style: {
              color: 'rgb(0,122,255)',
              cursor: 'pointer', 
              display: 'inline-block',
              height: '4rem',
              lineHeight: '4rem',

            },
            onClick: () => {
              transformData.value = row
              taskUserDetailDrawerRef.value?.open()
            }
          },
          row.receiptCode
        );
      },
    },
  },
  {
    field: "engineersTeamName",
    label: "派发方",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 90,
    }
  },

  {
    field: "startTime",
    label: "停电时间",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 145,
    }
  },
  {
    field: "assignTime",
    label: "派发时间",
    formConfig: {
      hideSearch: false,
      component: "NDatePicker",
      componentProps: {
        type: "daterange",
        valueFormat: "yyyy-MM-dd",
      },
    },
    tableConfig: {
      width: 160,
    }
  },
  {
    field: "feedbackTime",
    label: "反馈时间",
    formConfig: {
      hideSearch: false,
      component: "NDatePicker",
      componentProps: {
        type: "daterange",
        valueFormat: "yyyy-MM-dd",
      },
    },
    tableConfig: {
      width: 160,
    }
  },
  {
    field: "hourDays",
    label: "时长（天）",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 100,
    }
  },
  {
    field: "region",
    label: "所属台区",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 120,
    }
  },
  {
    field: "userType",
    label: "用户类型",
    formConfig: {
      hideSearch: false,
      component: "NSelect",
      componentProps: {
        options: dicDataArr(dic.getTaskUserType),
      },
    },
    tableConfig: {
      width: 90,
      render(row) {

        return h(
          "span",
          {},
          dicDataArr(dic.getTaskUserType).filter((item) => item.value == row.userType)[0]?.label
        );
      },
    },
  },
  {
    field: "accountNumber",
    label: "户号",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 145,
    }
  },
  {
    field: "customerName",
    label: "用户名称",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 130,
    }
  },


  {
    field: "taskCode",
    label: "关联通知单",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 120,
      render(row) {
        return h(
          "span",
          {
            style: {
              color: 'rgb(0,122,255)',
              cursor: 'pointer'
            },
            onClick: () => {
              transformData.value = row
              taskDetailDrawerRef.value?.open()
            }
          },
          row.taskCode
        );
      },
    },
  },


];
/**
 * 颜色代码
 */
const colorArr = [
  "#004A3C",
  '#129179',
  "#81D3F8",
  '#CFEEE8',
  ' #36A490',
];
/**
 * 统计数据字体大小
 */
const statisticalFontSize = "40px";
/**
 * 统计数据数组
 */
const statisticalColumn = reactive([
  {
    label: "时长大于7天",
    field: "hourGt7Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长5至6天",
    field: "hour5Z6Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长3至4天",
    field: "hour3Z4Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长小于2天",
    field: "hourLt2Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "回执总数",
    field: "receiptCountQty",
    unit: "个",
    value: 0,
    style: { color: "", fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "平均时长",
    field: "avgHour",
    unit: "天",
    value: "0",
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
]);

// --------------------------------组件挂载-----------------------------
onMounted(async () => {
  await dic.loadData("task_user_type");
  const res = await request.post(
    `relay-task-service/api/v1/engineersTeam/list`, {});
  console.log(res, 'resres')
  engineersTeamOptions.value = res.data
  engineersTeamOptions.value.push({ id: 0, name: '全部' })
  timeChart = init(timeChartRef.value as HTMLElement);
  taskUserTypeTimeChart = init(taskUserTypeTimeRef.value as HTMLElement);
  timeIntervalChart = init(timeIntervalChartRef.value as HTMLElement);
  initApi();

});

// --------------------------------监听engineersTeamIdSelectVal选择工程队的值-----------------------------
watch(() => engineersTeamIdSelectVal.value, (newVal, oldVal) => {
  initApi()
})
// -----------------------------------------------------------------

// --------------------------------处理图表数据 统计数据Method-------------------------------
/**
 * 处理字典返回的数组数据 value变为数字,只留label和val 格式[{label: ,value: },{label: ,value: }]
 */
function dicDataArr(dicType) {
  return dicType?.map((item) => { return { value: Number(item.value), label: item.label } })
}
/**
 * 获取统计数据 赋给statisticalColumn，同时赋予图标数据，渲染到页面上,
 */
async function initApi(params = {}) {
  console.log(engineersTeamIdSelectVal.value, 'engineersTeamIdSelectVal')
  const res = await request.post(
    `analysis-alarm-service/api/v1/releaseDurationStatistics/total`,
    { ...params, ...(engineersTeamIdSelectVal.value && { engineersTeamId: engineersTeamIdSelectVal.value }), ...formParams.value }
  );
  console.log(res.data.daysTimeMap, "天");
  statisticalColumn.forEach((item) => {
    item.value = res.data[item.field];
  });
  timeChartOption(res);
  taskUserTypeTimeOption(res);
  timeIntervalOption(res)
}
/**
 * 各区域下达时长对比分析图数据处理
 */
async function timeChartOption(res) {
  //图标数据
  let data1;
  let data2;
  // res.data.regionTimeMap = {'92050繁兴五号-1号配变' : {'时长大于7天': 2,'时长5至6天':5,'时长3至4天':5,'时长小于2天':3,},'92050繁兴五号-2号配变': {'时长大于7天': 2,'时长5至6天':5,'时长3至4天':5,'时长小于2天':3,},'92050繁兴五号-3号配变': {'时长大于7天': 2,'时长5至6天':5,'时长3至4天':5,'时长小于2天':3,}}
  if (res.data.regionTimeMap) {
    data1 = Object.keys(res.data.regionTimeMap);
    data2 = Object.values(res.data.regionTimeMap);
  } else {
    data1 = ['92050繁兴五号-1号配变', '92050繁兴五号-2号配变', '92050繁兴五号-3号配变'];
    data2 = [{ '时长大于7天': 0, '时长5至6天': 0, '时长3至4天': 0, '时长小于2天': 0, }, { '时长大于7天': 0, '时长5至6天': 0, '时长3至4天': 0, '时长小于2天': 0, }, { '时长大于7天': 0, '时长5至6天': 0, '时长3至4天': 0, '时长小于2天': 0, }];
  }
  const option: EChartsOption = {
    title: {
      text: "各区域下达时长对比分析图",
      subtext: "",
      left: "center",
      top: -4,
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
    legend: {
      top: 40,
    },
    grid: {
      height: '70%',
      right: '3%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: data1,
        axisLabel: {
          formatter: function (value) {
            // 缩写数据
            if (value.length > 5) {
              return value.substring(0, 5) + '...';
            }
            return value;
          },
        },
      }
    ],
    yAxis: {
      type: "value",
      name: '天'
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    series: [
      {
        name: '大于7天',
        type: 'bar',
        stack: 'Ad',
        emphasis: {
          focus: 'series'
        },
        data: data2?.map((item) => item["时长大于7天"]),
        itemStyle: {
          color: colorArr[0], // 设置柱状图的颜色
        },
      },
      {
        name: '5至6天',
        type: 'bar',
        stack: 'Ad',
        emphasis: {
          focus: 'series'
        },
        data: data2?.map((item) => item["时长5至6天"]),
        itemStyle: {
          color: colorArr[1], // 设置柱状图的颜色
        },
      },
      {
        name: '3至4天',
        type: 'bar',
        stack: 'Ad',
        emphasis: {
          focus: 'series'
        },
        data: data2?.map((item) => item["时长3至4天"]),
        itemStyle: {
          color: colorArr[2], // 设置柱状图的颜色
        },
      },
      {
        name: '小于2天',
        type: 'bar',
        stack: 'Ad',
        emphasis: {
          focus: 'series'
        },
        data: data2?.map((item) => item["时长小于2天"]),
        itemStyle: {
          color: colorArr[3], // 设置柱状图的颜色
        },
      },
    ],
  };
  timeChart.setOption(option);
}
/**
 * 不同用户类型下达时长对比分分析图数据处理
 */
async function taskUserTypeTimeOption(res) {
  //图标数据
  let data1: any = [];
  let data2 = [];
  if (res.data.userTypeTimeMap) {
    data1 = Object.keys(res.data.userTypeTimeMap);
    data2 = Object.values(res.data.userTypeTimeMap);
  }
  const option: EChartsOption = {
    title: {
      text: "不同用户类型下达时长对比分分析图",
      subtext: "",
      left: "center",
      top: -4,
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
    legend: {
      top: 45,
    },
    grid: {
      height: '70%',
      right: '3%',
      bottom: '0',
      containLabel: true
    },
    xAxis: {
      type: "category",
      data: data1,
    },
    yAxis: { type: "value", name: '天' },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    series: [
      {
        name: '大于7天',
        type: 'bar',
        data: data2?.map((item) => item["时长大于7天"]),
        itemStyle: {
          color: colorArr[0], // 设置柱状图的颜色
        },
      },
      {
        name: '5至6天',
        type: 'bar',
        data: data2?.map((item) => item["时长5至6天"]),
        itemStyle: {
          color: colorArr[1], // 设置柱状图的颜色
        },
      },
      {
        name: '3至4天',
        type: 'bar',
        data: data2?.map((item) => item["时长3至4天"]),
        itemStyle: {
          color: colorArr[2], // 设置柱状图的颜色
        },
      },
      {
        name: '小于2天',
        type: 'bar',
        data: data2?.map((item) => item["时长小于2天"]),
        itemStyle: {
          color: colorArr[3], // 设置柱状图的颜色
        },
      },
    ],
  };
  taskUserTypeTimeChart.setOption(option);
}
/**
 * 统计小图数据处理
 */
async function timeIntervalOption(res) {
  //图标数据
  let map = res.data.timeIntervalMap
  let x1 = map["时长大于7天"] ? map["时长大于7天"] : 0
  let x2 = map["时长5至6天"] ? map["时长5至6天"] : 0
  let x3 = map["时长3至4天"] ? map["时长3至4天"] : 0
  let x4 = map["时长小于2天"] ? map["时长小于2天"] : 0


  const option: EChartsOption = {
    xAxis: {
      type: 'category',
      data: ['<7', '5~6', '3~4', '<2']
    },
    tooltip: {
      trigger: "item",
      axisPointer: {
        type: "shadow",
      },
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [x1, x2, x3, x4],
        type: 'bar',
        itemStyle: {
          color: (params) => {
            if (params.name === '<7') {
              return colorArr[0];
            } else if (params.name === '5~6') {
              return colorArr[1];
            } else if (params.name === '3~4') {
              return colorArr[2];
            } else if (params.name === '<2') {
              return colorArr[3];
            }

          }
        }
      }
    ]

  };
  timeIntervalChart.setOption(option);
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
  initApi(params);
}
//reset和submit同理
function reset() {
  formParams.value = {}
  initApi({});
}
//------------------------------------------------------
</script>

<style></style>