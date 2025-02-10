<template>
  <EchartsBasicCrud ref="echartsBasicCrudRef" :submit="submit" :reset="reset" :is-show-action="false"
    :statisticalColumn="statisticalColumn"
    :statistical-config="{ isHaveTotal: true, totalPosition: 'tableTop', tablePosition: 'left' }" :columns="columns"
    name="analysis-alarm-service/api/v1/planStatistics" :crud-config="{ add: false, delete: false, edit: false }">
    <template #formTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电计划查询
      </div>
    </template>
    <template #statisticalTotalTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电计划统计
      </div>
    </template>
    <template #tableTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电计划列表
      </div>
    </template>
    <template #toolbar><n-button type="primary"
        @click="exportExcal('/analysis-alarm-service/api/v1/planStatistics/export', { condition: { ...formParams } }, '停电计划统计列表')">导出</n-button></template>
    <template #echartsTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电计划图表
      </div>
    </template>

    <template #statisticalTotalEchart>
      <div class="w-45 h-28   text-center boxShow p-t-2" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
        <div>各类型占比</div>
        <div ref="timeChartPercentageRef" class="w-40 h-28 m-l-3 m-t--5"></div>
      </div>
    </template>

    <template #echarts>
      <div>
        <div ref="timeChartRef" id="timeChart" class="h-90 w-100%"></div>
        <div ref="taskSourceTimeRef" id="taskSourceTimeChart" class="h-90 w-100%">

        </div>
        <div ref="typeCancelChartRef" id="typeCancelChart" class="h-90 w-100%"></div>
      </div>
    </template>

  </EchartsBasicCrud>
  <!-- 停电计划详情抽屉框 -->
  <ProjectCodeDrawer ref="projectRef" />
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
  typeCancelChart = init(typeCancelChartRef.value as HTMLElement)
  initApi();
});
// -------------------------------定义变量-----------------------------
/**
 * 查询表单数据 供给导出使用，统计数量和图表的不用formParams，initApi()时候直接传了
 */
const formParams = ref({})
const projectRef = ref(null)
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
 * 不同来源的时长图表dom
 */
const typeCancelChartRef: Ref<HTMLElement | null> = ref(null);
let typeCancelChart: ECharts;

/**
 * 计划状态status
 */
let statusList = [
  {
    label: '未关联',
    value: '010'
  },
  {
    label: '已关联',
    value: '020'
  }
]
/**
 * 表格列表
 */
const columns = [
  {
    field: "projectCode",
    label: "工程编号",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 90,
      render(row) {
        return h(
          "span",
          {
            style: {
              color: 'rgb(0,122,255)',
              cursor: 'pointer',
              display: 'inline-block',
              height: '5rem',
              lineHeight: '5rem',
            },
            onClick: () => {
              projectRef.value.operateDrawer({ id: row.id })
            }
          },
          row.projectCode
        );
      },
    },

  },
  {
    field: "projectName",
    label: "工程名称",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 185,
    }
  },
  {
    field: "projectType",
    label: "项目类型",
    formConfig: {
      hideSearch: false,
      component: "NSelect",
      componentProps: {
        options: [
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
      }

    },
    tableConfig: {
      width: 80,
    }
  },
  {
    field: "executeStatus",
    label: "状态",
    formConfig: {
      hideSearch: true,
      component: "NSelect",
      componentProps: {
        options: statusList
      }

    },
    tableConfig: {
      width: 90,
      render(row) {
        return h(
          "span",
          {},
          statusList?.filter((item) => item.value == row.executeStatus)[0]?.label
        );
      },
    },
  },
  {
    field: "version",
    label: "版本",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 70,
    }
  },

  {
    field: "stationLineName",
    label: "变电站/线路名称",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 170,
    }
  },
  {
    field: "startTime",
    label: "停役时间",
    formConfig: {
      hideSearch: false,
      component: "NDatePicker",
      componentProps: {
        type: "daterange",
        valueFormat: "yyyy-MM-dd",
      },
    },
    tableConfig: {
      width: 120,
    }
  },
  {
    field: "endTime",
    label: "复役时间",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 120,
    }
  },
  {
    field: "constructionTeam",
    label: "施工班组",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 120,
    }
  },
  {
    field: "taskCode",
    label: "关联通知单",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 120,
    }
  },

];
/**
 * 颜色代码
 */
const colorArr = [
  "#B4E0D8",
  "#048D74",
  "rgb(150,208,207)",
  ' #36A490',
];
const pieColorArr = [
  '#36A490',
  '#225E79',
  '#A9D7CF',
  "#F89869",
  "#FFA434",
  '#7181F1',
  '#007AFF',
  '#8D9AF4',
  '#EE82EE',
  '#F5C03D',
]
/**
 * 统计数据字体大小
 */
const statisticalFontSize = "40px";
/**
 * 统计数据数组
 */
const statisticalColumn = reactive([
  {
    label: "新增计划数",
    field: "planItemAddQty",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "变更计划数",
    field: "planItemChangeQty",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "关联计划数",
    field: "planItemCancelQty",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "计划总数",
    field: "planItemSumQty",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
]);


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
  const res = await request.post(
    `analysis-alarm-service/api/v1/planStatistics/total`,
    { ...params }
  );
  // console.log(res.data.daysTimeMap, "天");
  statisticalColumn.forEach((item) => {
    item.value = res.data[item.field];
  });
  timeChartOption(res);
  taskSourceTimeOption(res);
  timeChartPercentageOption(res)
  typeCancelChartOption(res)
}
/**
 * 统计小图数据处理
 */
async function timeChartPercentageOption(res) {
  const data = res.data.projectTypeZbMap
  let transformedData;
  if (data) {
    transformedData = Object.entries(data).map(([name, value], index) => ({
      name,
      value: value.zb,
      itemStyle: {
        color: pieColorArr[index]
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
 * 不同项目类型计划的新增情况统计分析图数据处理
 */
async function timeChartOption(res) {
  //图标数据
  let data1;
  let data2;
  let x1 = [];
  let x2 = [];
  let total;
  let percentageArr;
  if (res.data.projectTypeAddMap) {
    data1 = Object.keys(res.data.projectTypeAddMap);
    data2 = Object.values(res.data.projectTypeAddMap);
    x1 = data2.map(item => item["qty"] ? item["qty"] : 0)
    x2 = data2.map(item => item["zb"] ? item["zb"] : 0)
    // 后端返回百分比有问题 前端自己计算百分比
    total = x1.reduce(function (acc, cur) {
      return acc + cur;
    }, 0);
    percentageArr = x1.map((value) => (value / total * 100).toFixed(1))
  }


  const option: EChartsOption = {
    title: {
      text: "不同项目类型计划的新增情况统计",
      left: "center",
    },
    toolbox: {
      show: true,
      feature: {
        // dataZoom: {
        //   yAxisIndex: 'none'
        // },
        saveAsImage: {}
      },
      top: 35,
      left: 35,
    },
    grid: {
      top: '25%',
      left: '3%',
      right: '3%',
      bottom: '5%',
      containLabel: true
    },
    xAxis: {
      type: "category",
      data: data1,
      axisLabel: {
        interval: 0, // 强制显示所有标签
        rotate: 0,   // 不旋转标签
        formatter: function (value) {
          // 缩写数据
          if (value.length > 3) {
            return value.substring(0, 3) + '...';
          }
          return value;
        },
      }
    },
    yAxis: {
      type: "value",

    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    series: [
      {
        data: x1,
        type: "bar",
        itemStyle: {
          color: (params) => {
            return pieColorArr[params.dataIndex]
          }
        }
      },
      {
        data: percentageArr,
        type: "pie",
        labelLine: {
          show: false // 隐藏指出线
        },
        itemStyle: {
          color: (params) => {
            return pieColorArr[params.dataIndex]
          }
        },
        radius: '17%', // 调整饼图的半径范围
        center: ['90%', '10%'], // 调整饼图的中心位置
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            var xAxisValue = option.xAxis.data[params.dataIndex]; // 获取对应x轴的值
            return xAxisValue + ': ' + params.value + '%'; // 拼接提示框内容
          }
        },
        label: {
          show: false
        }
      },
    ],
    graphic: [{
      type: 'text',
      // left: 'center', // 文字的左边距
      right: '20%',
      top: '15%', // 文字的上边距
      style: {
        text: '各类型新增率', // 自定义的文字内容
        fill: '#333',
        fontSize: '14px',
        fontWeight: 700,
      },
    }],
  };
  timeChart.setOption(option);
}
/**
 * 不同项目类型计划的变更情况统计图数据处理
 */
async function taskSourceTimeOption(res) {
  //图标数据
  let data1;
  let data2;
  let x1 = [];
  let x2 = [];
  let total;
  let percentageArr;
  if (res.data.projectTypeChangeMap) {
    data1 = Object.keys(res.data.projectTypeChangeMap);
    data2 = Object.values(res.data.projectTypeChangeMap);
    x1 = data2.map(item => item["qty"] ? item["qty"] : 0)
    x2 = data2.map(item => item["zb"] ? item["zb"] : 0)
    // 后端返回百分比有问题 前端自己计算百分比
    total = x1.reduce(function (acc, cur) {
      return acc + cur;
    }, 0);
    percentageArr = x1.map((value) => (value / total * 100).toFixed(1))
  }


  const option: EChartsOption = {
    title: {
      text: "不同项目类型计划的变更情况统计",
      left: "center",
    },
    toolbox: {
      show: true,
      feature: {
        // dataZoom: {
        //   yAxisIndex: 'none'
        // },
        saveAsImage: {}
      },
      top: 35,
      left: 35,
    },
    grid: {
      top: '25%',
      left: '3%',
      right: '3%',
      bottom: '5%',
      containLabel: true
    },
    xAxis: {
      type: "category",
      data: data1,
      axisLabel: {
        interval: 0, // 强制显示所有标签
        rotate: 0,   // 不旋转标签
        formatter: function (value) {
          // 缩写数据
          if (value.length > 3) {
            return value.substring(0, 3) + '...';
          }
          return value;
        },
      }
    },
    yAxis: {
      type: "value",

    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    series: [
      {
        data: x1,
        type: "bar",
        itemStyle: {
          color: (params) => {
            return pieColorArr[params.dataIndex]
          }
        }
      },
      {
        data: percentageArr,
        type: "pie",
        labelLine: {
          show: false // 隐藏指出线
        },
        itemStyle: {
          color: (params) => {
            return pieColorArr[params.dataIndex]
          }
        },
        radius: '17%', // 调整饼图的半径范围
        center: ['90%', '10%'], // 调整饼图的中心位置
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            var xAxisValue = option.xAxis.data[params.dataIndex]; // 获取对应x轴的值
            return xAxisValue + ': ' + params.value + '%'; // 拼接提示框内容
          }
        },
        label: {
          show: false
        }
      },
    ],
    graphic: [{
      type: 'text',
      // left: 'center', // 文字的左边距
      right: '20%',
      top: '15%', // 文字的上边距
      style: {
        text: '各类型变更率', // 自定义的文字内容
        fill: '#333',
        fontSize: '14px',
        fontWeight: 700,
      },
    }],
  };
  taskSourceTimeChart.setOption(option);
}
/**
 * 不同项目类型计划的关联情况统计图数据处理
 */
async function typeCancelChartOption(res) {
  //图标数据
  let data1;
  let data2;
  let x1 = [];
  let x2 = [];
  let total;
  let percentageArr;
  if (res.data.projectTypeCancelMap) {
    data1 = Object.keys(res.data.projectTypeCancelMap);
    data2 = Object.values(res.data.projectTypeCancelMap);
    x1 = data2.map(item => item["qty"] ? item["qty"] : 0)
    x2 = data2.map(item => item["zb"] ? item["zb"] : 0)
    // 后端返回百分比有问题 前端自己计算百分比
    total = x1.reduce(function (acc, cur) {
      return acc + cur;
    }, 0);
    percentageArr = x1.map((value) => (value / total * 100).toFixed(1))
  }


  const option: EChartsOption = {
    title: {
      text: "不同项目类型计划的关联情况统计",
      left: "center",
    },
    toolbox: {
      show: true,
      feature: {
        // dataZoom: {
        //   yAxisIndex: 'none'
        // },
        saveAsImage: {}
      },
      top: 35,
      left: 35,
    },
    grid: {
      top: '25%',
      left: '3%',
      right: '3%',
      bottom: '5%',
      containLabel: true
    },
    xAxis: {
      type: "category",
      data: data1,
      axisLabel: {
        interval: 0, // 强制显示所有标签
        rotate: 0,   // 不旋转标签
        formatter: function (value) {
          // 缩写数据
          if (value.length > 3) {
            return value.substring(0, 3) + '...';
          }
          return value;
        },
      }
    },
    yAxis: {
      type: "value",

    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    series: [
      {
        data: x1,
        type: "bar",
        itemStyle: {
          color: (params) => {
            return pieColorArr[params.dataIndex]
          }
        }
      },
      {
        data: percentageArr,
        type: "pie",
        labelLine: {
          show: false // 隐藏指出线
        },
        itemStyle: {

          color: (params) => {
            return pieColorArr[params.dataIndex]
          }
        },
        radius: '17%', // 调整饼图的半径范围
        center: ['90%', '10%'], // 调整饼图的中心位置
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            var xAxisValue = option.xAxis.data[params.dataIndex]; // 获取对应x轴的值
            return xAxisValue + ': ' + params.value + '%'; // 拼接提示框内容
          }
        },
        label: {
          show: false
        }
      },
    ],
    graphic: [{
      type: 'text',
      // left: 'center', // 文字的左边距
      right: '20%',
      top: '15%', // 文字的上边距
      style: {
        text: '各类型关联率', // 自定义的文字内容
        fill: '#333',
        fontSize: '14px',
        fontWeight: 700,
      },
    }],
  };
  typeCancelChart.setOption(option);
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

<style lang="scss"></style>