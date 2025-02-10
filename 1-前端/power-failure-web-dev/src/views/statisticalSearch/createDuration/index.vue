<template>
  <EchartsBasicCrud ref="echartsBasicCrudRef" :submit="submit" :reset="reset" :is-show-action="false"
    :statisticalColumn="statisticalColumn" :columns="columns"
    name="analysis-alarm-service/api/v1/createDurationStatistics"
    :crud-config="{ add: false, delete: false, edit: false }" :rowStyleClassMethod="rowStyleClassMethod">
    <template #formTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        创建时长查询
      </div>
    </template>
    <template #statisticalTotalTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        创建时长统计
      </div>
    </template>
    <template #tableTitle>
      <div class="font-700 w-100%  b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        创建时长列表

      </div>
    </template>
    <template #toolbar><n-button type="primary"
        @click="exportExcal('/analysis-alarm-service/api/v1/createDurationStatistics/export', { condition: { ...formParams } }, '创建时长统计列表')">导出</n-button></template>
    <template #echartsTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        创建时长图表
      </div>
    </template>

    <template #statisticalTotalEchart>
      <div class="w-45 h-28   text-center boxShow p-t-2" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
        <div>各时段占比</div>
        <div ref="timeChartPercentageRef" class="w-40 h-28 m-l-3 m-t--5"></div>
      </div>
    </template>

    <template #echarts>
      <div>
        <div ref="timeChartRef" id="timeChart" class="h-90 w-100%"></div>

        <div ref="taskSourceTimeRef" id="taskSourceTimeChart" class="h-90 w-100%">

        </div>
      </div>
    </template>
  </EchartsBasicCrud>
  <!-- 关联通知单抽屉 -->
  <TaskDetailDrawer ref="taskDetailDrawerRef" :transformId="transformData.id" transformIdType="task"></TaskDetailDrawer>
</template>

<script lang="ts" setup>
import { EchartsBasicCrud } from "~/src/components/echartsBusiness/echartCrud";
import type { ECharts, EChartsOption } from "echarts";
import { init } from "echarts";
import { onMounted, Ref, ref, reactive, h } from "vue";
import { request } from "~/src/service/request";
import { useDicStore } from "~/src/store";
let dic = useDicStore();
import { exportExcal } from '~/src/utils/common/exportExcal'
// --------------------------------组件挂载-----------------------------
onMounted(async () => {
  await dic.loadData("task_reason,user");
  timeChart = init(timeChartRef.value as HTMLElement);
  taskSourceTimeChart = init(taskSourceTimeRef.value as HTMLElement);
  timeChartPercentage = init(timeChartPercentageRef.value as HTMLElement);
  initApi();
});
// -------------------------------定义变量-----------------------------
/**
 * 查询表单数据 供给导出使用，统计数量和图表的不用formParams，initApi()时候直接传了
 */
const formParams = ref({})
/**
 * 公共弹窗抽屉的dom 和数据变量
 */
const transformData = ref({})
let taskDetailDrawerRef = ref(null)
/**
 * 各时长个数图表dom 和数据变量
 */
const timeChartRef: Ref<HTMLElement | null> = ref(null);
let timeChart: ECharts;

/**
 * 统计小图表dom 和数据变量
 */
const timeChartPercentageRef: Ref<HTMLElement | null> = ref(null);
let timeChartPercentage: ECharts;

/**
 * 不同来源的时长图表dom
 */
const taskSourceTimeRef: Ref<HTMLElement | null> = ref(null);
let taskSourceTimeChart: ECharts;
/**
 * 表格列表
 */
const columns = [
  {
    field: "taskCode",
    label: "停电计划编号",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 120,
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
              console.log(taskDetailDrawerRef.value, 'taskDetailDrawerRef.value')
              taskDetailDrawerRef.value?.open()
            }
          },
          row.taskCode
        );
      },
    },
  },
  {
    field: "taskSource",
    label: "通知来源",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 125,
      render(row) {
        return h(
          "span",
          {},
          dicDataArr(dic.getUser).filter((item) => item.value == row.taskSource)[0]?.label
        );
      },
    },
  },
  {
    field: "confirmTime",
    label: "确认时间",
    formConfig: {
      hideSearch: false,
      component: "NDatePicker",
      componentProps: {
        type: "daterange",
        valueFormat: "yyyy-MM-dd",
      },
    },
    tableConfig: {
      width: 165,
    }
  },
  {
    field: "startTime",
    label: "停电时间",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 150,
    }
  },
  {
    field: "endTime",
    label: "送电时间",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 150,
    }
  },
  {
    field: "days",
    label: "时长（天）",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 100,
    }
  },
  {
    field: "reason",
    label: "停电原因",
    formConfig: {
      hideSearch: false,
      component: "NSelect",
      componentProps: {
        options: dicDataArr(dic.getPowerFailureReason),
      },
    },
    tableConfig: {
      width: 120,
      render(row) {
        return h(
          "span",
          {},
          dicDataArr(dic.getPowerFailureReason)?.filter((item) => item.value == row.reason)[0]
            ?.label
        );
      },
    },
  },
  {
    field: "taskUserCount",
    label: "停电户数",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 100,
    }
  },
  {
    field: "ranges",
    label: "停电范围",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 130,
    }
  },

];
/**
 * 颜色代码
 */
const colorArr = [
  '#CFEEE8',
  "#81D3F8",
  '#129179',
  "#004A3C",
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
    label: "时长小于8天",
    field: "hourLt8Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长8至10天",
    field: "hour8Z10Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长11至15天",
    field: "hour11Z15Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长大于15天",
    field: "hourGt15Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[4], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "通知总数",
    field: "taskCountQty",
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


// --------------------------------处理图表数据 统计数据Method-------------------------------
/**
 * 时长小于阈值的行给予背景色
 */
function rowStyleClassMethod(row) {
  console.log(row, 'rowrow')
  if (row.days < row.alertDays) {
    return "selectedRowStyle2";
  }
}
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
  const res = await request.post(
    `analysis-alarm-service/api/v1/createDurationStatistics/total`,
    { ...params }
  );
  console.log(res.data.daysTimeMap, "天");
  statisticalColumn.forEach((item) => {
    item.value = res.data[item.field];
  });
  timeChartOption(res);
  taskSourceTimeOption(res);
  timeChartPercentageOption(res)
}
/**
 * 统计小图数据处理
 */
async function timeChartPercentageOption(res) {
  const data = res.data.timeIntervalMap
  let transformedData;
  if (data) {
    transformedData = Object.entries(data).map(([name, value], index) => ({
      name,
      value: parseFloat(value),
      itemStyle: {
        color: colorArr[index]
      }
    }));
  }

  const option: EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}%'
    },
    series: [
      {
        type: 'pie',
        radius: '50%',
        data: transformedData,
        labelLine: {
          show: false // 隐藏指出线
        },
        label: {
          show: false
        }
      }
    ]
  };
  timeChartPercentage.setOption(option);
}
/**
 * 各时长个数的统计分析图数据处理
 */
async function timeChartOption(res) {
  //图标数据
  let data1;
  let data2;
  if (res.data.daysTimeMap) {
    data1 = Object.keys(res.data.daysTimeMap);
    data2 = Object.values(res.data.daysTimeMap);
  }
  const option: EChartsOption = {
    title: {
      text: "各时长个数的统计分析图",
      subtext: "",
      left: "center",
    },
    toolbox: {
      show: true,
      feature: {
        saveAsImage: {}
      },
      top: 0,
    },

    xAxis: {
      type: "value",
      name: '个', // 设置单位名称
      nameLocation: 'end', // 将单位名称显示在最右侧
      nameGap: 10, // 单位名称与坐标轴之间的距离,
      axisLine: {
        show: true,
      },
    },
    yAxis: {
      type: "category",
      data: data1.map((item) => item + "天"),
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    series: [
      {
        data: data2,
        type: "bar",
        itemStyle: {
          color: (params) => {
            // 根据数据大小判断颜色
            const yAxisValue = Number(params.name.replace("天", ""));
            if (yAxisValue < 8) {
              return colorArr[0]; // 设置第一组颜色
            } else if (yAxisValue >= 8 && yAxisValue <= 10) {
              return colorArr[1]; // 设置第二组颜色
            } else if (yAxisValue >= 11 && yAxisValue <= 15) {
              return colorArr[2]; // 设置第三组颜色
            } else {
              return colorArr[3]; // 设置其它颜色
            }
          },
        },
      },
    ],
    graphic: [{
      type: 'rect',
      left: '0%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        // text: '时长小于8天', // 自定义的文字内容
        fill: colorArr[0],
        fontSize: '12px',
      },
    }, {
      type: 'text',
      left: '6%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        text: '时长小于8天', // 自定义的文字内容
        fontSize: '12px',
      },
    }, {
      type: 'rect',
      left: '25%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        // text: '时长小于8天', // 自定义的文字内容
        fill: colorArr[1],
        fontSize: '12px',
      },
    }, {
      type: 'text',
      left: '31%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        text: '时长8至10天', // 自定义的文字内容
        fontSize: '12px',
      },
    }, {
      type: 'rect',
      left: '50%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        // text: '时长小于8天', // 自定义的文字内容
        fill: colorArr[2],
        fontSize: '12px',
      },
    }, {
      type: 'text',
      left: '56%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        text: '时长11至15天', // 自定义的文字内容
        fontSize: '12px',
      },
    }, {
      type: 'rect',
      left: '76%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        // text: '时长小于8天', // 自定义的文字内容
        fill: colorArr[3],
        fontSize: '12px',
      },
    }, {
      type: 'text',
      left: '82%', // 文字的左边距
      top: '12%', // 文字的上边距
      shape: {
        width: 20,
        height: 10
      },
      style: {
        text: '时长大于15天', // 自定义的文字内容
        fontSize: '12px',
      },
    },],
  };
  timeChart.setOption(option);
}
/**
 * 不同来源的时长统计图数据处理
 */
async function taskSourceTimeOption(res) {
  //图标数据
  let data1: any = [];
  let data2 = [];
  let data3: any = [];
  let data4 = [];
  let data5 = []
  // res.data.taskSourceTimeMap = {'A工程队':{'时长小于8天': 2,'时长8至10天': 12,'时长11至15天':8,'时长大于15天': 10},'B工程队':{'时长小于8天': 2,'时长8至10天': 12,'时长11至15天':8,'时长大于15天': 10},'C工程队':{'时长小于8天': 2,'时长8至10天': 12,'时长11至15天':8,'时长大于15天': 10}}
  if (res.data.taskSourceTimeMap) {
    data1 = Object.keys(res.data.taskSourceTimeMap);
    data2 = Object.values(res.data.taskSourceTimeMap);
  }
  // res.data.taskSourceAvgTimeMap = {a: '0.5',b: '1.0',c:'2'}
  if (res.data.taskSourceAvgTimeMap) {
    data3 = Object.keys(res.data.taskSourceAvgTimeMap);
    data4 = Object.values(res.data.taskSourceAvgTimeMap);
    data1.forEach(item => {
      let index = data3.indexOf(item);
      data5.push(data4[index])
    })
  }
  const option: EChartsOption = {
    title: {
      text: "不同来源的时长统计图",
      left: 'center',
    },
    legend: {
      top: '12%'
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
      z: 10,
    },
    // grid: {
    //   top: '30%',
    //   left: '3%',
    //   right: '1%',
    //   bottom: '5%',
    //   containLabel: true
    // },
    grid: {
      height: '70%',
      right: '3%',
      bottom: '5%',
      containLabel: true
    },
    xAxis: {
      type: "category",
      data: data1,
    },
    yAxis: [
      {
        type: "value",
        name: '个', // 设置单位名称
        nameLocation: 'end', // 将单位名称显示在最右侧
        nameGap: 15, // 单位名称与坐标轴之间的距离,
        axisLine: {
          show: true,
        },
      }, // 第一个 Y 轴
      {
        type: "value",
        name: '天', // 设置单位名称
        nameLocation: 'end', // 将单位名称显示在最右侧
        nameGap: 15, // 单位名称与坐标轴之间的距离,
        axisLine: {
          show: true,
        },
      }, // 第二个 Y 轴
    ],
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    series: [
      {
        name: "时长小于8天",
        type: "bar",
        data: data2?.map((item) => item["时长小于8天"]), // Series 1 对应第一个 Y 轴
        itemStyle: { color: colorArr[0] },
        yAxisIndex: 0, // 指定使用第一个 Y 轴
      },
      {
        name: "时长8至10天",
        type: "bar",
        data: data2?.map((item) => item["时长8至10天"]), // Series 1 对应第一个 Y 轴
        itemStyle: { color: colorArr[1] },
        yAxisIndex: 0, // 指定使用第一个 Y 轴
      },
      {
        name: "时长11至15天",
        type: "bar",
        data: data2?.map((item) => item["时长11至15天"]), // Series 1 对应第一个 Y 轴
        itemStyle: { color: colorArr[2] },
        yAxisIndex: 0, // 指定使用第一个 Y 轴
      },
      {
        name: "时长大于15天",
        type: "bar",
        data: data2?.map((item) => item["时长大于15天"]), // Series 1 对应第一个 Y 轴
        itemStyle: { color: colorArr[3] },
        yAxisIndex: 0, // 指定使用第一个 Y 轴
      },
      {
        name: "平均时长（天）",
        type: "line",
        data: data5, // Series 2 对应第二个 Y 轴
        itemStyle: { color: colorArr[4] },
        lineStyle: {
          type: 'dashed', // 设置线型为虚线
          width: 2, // 设置线条宽度
          color: colorArr[4], // 设置线条颜色
        },
        yAxisIndex: 1, // 指定使用第二个 Y 轴
      },
    ],
  };
  taskSourceTimeChart.setOption(option);
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
const echartsBasicCrudRef = ref()
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

<style lang="scss">
.n-data-table-base-table {
  .n-data-table-table {
    .n-data-table-tbody {
      .selectedRowStyle1 {
        td {
          color: #FFA434 !important;
        }
      }
    }
  }
}
</style>