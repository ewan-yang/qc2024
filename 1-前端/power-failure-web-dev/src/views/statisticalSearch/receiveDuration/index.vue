<template>
  <EchartsBasicCrud ref="echartsBasicCrudRef" :submit="submit" :reset="reset" :is-show-action="false"
    :statisticalColumn="statisticalColumn" :columns="columns"
    name="analysis-alarm-service/api/v1/receiveDurationStatistics"
    :crud-config="{ add: false, delete: false, edit: false }"
    :rowStyleClassMethod="rowStyleClassMethod"
    >
    <template #formTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        接收时长查询
      </div>
    </template>
    <template #statisticalTotalTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        接收时长统计
      </div>
    </template>
    <template #tableTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        接收时长列表
      </div>
    </template>
    <template #echartsTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        接收时长图表
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
    <template #toolbar><n-button type="primary"  @click="exportExcal('/analysis-alarm-service/api/v1/receiveDurationStatistics/export',{condition:{...formParams}},'接收时长统计列表')">导出</n-button></template>

    <template #echarts>
      <div>
        <div ref="regionChartRef" id="regionChart" class="h-90 w-100% mb-20" style="height: 22rem;"></div>
        <div ref="userTypeTimeRef" id="userTypeChart" class="h-90 w-100%"></div>
      </div>
    </template>
  </EchartsBasicCrud>

  <!-- 回执单号抽屉 -->
  <TaskUserDetailDrawer ref="taskUserModalRef" :transformId="transformData?.id"></TaskUserDetailDrawer>

  <!-- 关联通知单抽屉 -->
  <TaskDetailDrawer ref="modalRef" :transformId="transfromData.taskId" transformIdType="task"></TaskDetailDrawer>
</template>

<script lang="ts" setup>
import { EchartsBasicCrud } from "~/src/components/echartsBusiness/echartCrud";
import type { ECharts, EChartsOption } from "echarts";
import { init } from "echarts";
import { onMounted, Ref, ref, reactive, h, nextTick } from "vue";
import { request } from "~/src/service/request";
import { useDicStore } from "~/src/store";
let dic = useDicStore();
import {exportExcal} from '~/src/utils/common/exportExcal'

let taskUserTypeList = [];

// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)

// 回执详情抽屉数据
let transformData = ref({})
let taskUserModalRef = ref(null)

// --------------------------------组件挂载-----------------------------
onMounted(async () => {
  await dic.loadData("task_user_type,user");
  //字典中value值为字符串 直接放在下拉框里用显示不出来，如果改dic文件字典里返回的值的话，影响到了停电通知页面，变动大
  dic.getTaskUserType?.forEach((item) => {
    taskUserTypeList.push({
      label: item.label,
      value: Number(item.value),
    });
  });
  regionChart = init(regionChartRef.value as HTMLElement);
  userTypeChart = init(userTypeTimeRef.value as HTMLElement);
  timeIntervalChart = init(timeIntervalChartRef.value as HTMLElement);
  initApi();
});
// -------------------------------定义变量-----------------------------
/**
 * 查询表单数据 供给导出使用，统计数量和图表的不用formParams，initApi()时候直接传了
 */
 const formParams = ref({})
/**
 * 表格列表
 */
const columns = [
  {
    field: "receiptCode",
    label: "回执单号",
    formConfig: {

      hideSearch: true,
    },
    tableConfig: {
      width: 120,
      render(row) {
        return h(
          'span',
          {
            style: {
              color: '#0062E1',
              cursor: 'pointer', 
              display: 'inline-block',
              height: '3rem',
              lineHeight: '3rem',
            },
            onClick: () => {
              transformData.value = row
              nextTick(() => {
                taskUserModalRef.value?.open()
              })
            }
          },
          { default: () => `${row.receiptCode}` }
        )
      }
    }

  },
  {
    field: "startTime",
    label: "停电时间",
    formConfig: {
      hideSearch: false,
      component: "NDatePicker",
      componentProps: {
        type: "daterange",
        valueFormat: "yyyy-MM-dd",
      },
    },
    tableConfig: {
      width: 145,
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
      width: 165,
    }
  },
  {
    field: "assignTime",
    label: "派发时间",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 165,
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
    field: "userType",
    label: "用户类型",
    formConfig: {
      hideSearch: false,
      component: "NSelect",
      componentProps: {
        options: taskUserTypeList,
      },
    },
    tableConfig: {
      width: 90,
      render(row) {
        return h(
          "span",
          {},
          taskUserTypeList?.filter((item) => item.value == row.userType)[0]
            ?.label
        );
      },
    },
  },
  {
    field: "region",
    label: "所属台区",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 130,
    }
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
  ' #36A490',
];

/**
 * 
 * 统计数据字体大小
 */
const statisticalFontSize = "40px";
/**
 * 统计数据数组
 */
const statisticalColumn = reactive([
  {
    label: "时长小于4天",
    field: "hourLt4Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长4至7天",
    field: "hour4Z7Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "时长大于7天",
    field: "hourGt7Days",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
  {
    label: "回执总数",
    field: "receiptCountQty",
    unit: "个",
    value: 0,
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },

  {
    label: "平均时长",
    field: "avgHour",
    unit: "天",
    value: "0",
    style: { color: colorArr[3], fontSize: statisticalFontSize, fontWeight: 700 },
  },
]);
/**
 * 区域数图表dom
 */
const regionChartRef: Ref<HTMLElement | null> = ref(null);
let regionChart: ECharts;

/**
 * 各时段统计图表dom
 */
const timeIntervalChartRef: Ref<HTMLElement | null> = ref(null);
let timeIntervalChart: ECharts;

/**
 * 各个用户图表dom
 */
const userTypeTimeRef: Ref<HTMLElement | null> = ref(null);
let userTypeChart: ECharts;
// --------------------------------处理图表数据 统计数据Method-------------------------------
/**
 * 时长小于阈值的行给予背景色
 */
function rowStyleClassMethod(row){
  // console.log(row,'rowrow')
  if (row.hourDays < row.alertDays) {
    return "selectedRowStyle2";
  }
}

/**
 * 获取统计数据 赋给statisticalColumn，同时赋予图标数据，渲染到页面上,
 */
async function initApi(params = {}) {
  const res = await request.post(
    `analysis-alarm-service/api/v1/receiveDurationStatistics/total`,
    { ...params }
  );
  // console.log(res.data, "天");
  statisticalColumn.forEach((item) => {
    item.value = res.data[item.field];
  });
  regionChartOption(res);
  userTypeTimeOption(res);
  timeIntervalOption(res);
}

/**
 * 各区域平均接收时长统计分析图
 */
async function regionChartOption(res) {
  //图标数据
  let data1;
  let data2;
  // 每个类别的数组
  let x1 = [];
  let x2 = [];
  let x3 = [];
  if(res.data.regionTimeMap){
    data1 = Object.keys(res.data.regionTimeMap);
    data2 = Object.values(res.data.regionTimeMap);
    x1 = data2.map(item => item["时长小于4天"] ? item["时长小于4天"] : 0)
    x2 = data2.map(item => item["时长4至7天"] ? item["时长4至7天"] : 0)
    x3 = data2.map(item => item["时长大于7天"] ? item["时长大于7天"] : 0)
  }
  const option: EChartsOption = {
    title: {
      text: "各区域平均接收时长统计分析图",
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
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },

    yAxis: {
      type: 'value',
      name: '天',
      // interval: 1,
      // axisLabel: {
      //   formatter: ''
      // }
    },
    xAxis: {
      type: 'category',
      data: data1
    },
    series: [
      {
        name: '时长小于4天',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x1,
        itemStyle:{
          color:colorArr[0]
        }
      },
      {
        name: '时长4至7天',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x2
        ,
        itemStyle:{
          color:colorArr[1]
        }
      },
      {
        name: '时长大于7天',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x3,
        itemStyle:{
          color:colorArr[2]
        }
      },

    ]
  };
  regionChart.setOption(option);
}
// window.addEventListener("resize", function() {
//     // 让我们的图表调用 resize这个方法
//     regionChart.resize();
//   });
/**
 * 各时段统计图数据处理
 */
async function timeIntervalOption(res) {
  //图标数据
  let map;
  let x1;
  let x2;
  let x3;
  if(res.data.timeIntervalMap){
    map = res.data.timeIntervalMap
    x1 = map["时长小于4天"] ? map["时长小于4天"] : 0
    x2 = map["时长4至7天"] ? map["时长4至7天"] : 0
    x3 = map["时长大于7天"] ? map["时长大于7天"] : 0
  }

  const option: EChartsOption = {
    xAxis: {
      type: 'category',
      data: ['<4', '4~7', '>7']
    },
    yAxis: {
      type: 'value',
      // interval: 2
    },
    series: [
      {
        data: [x1, x2, x3],
        type: 'bar',
        label: {
          formatter: '{b}: {c}%',
        },
        itemStyle: {
          color: (params) => {
            const dataIndex = params.dataIndex;
            return colorArr[dataIndex]
          }
        }
      }
    ],
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: function (params) {
        const dataIndex = params[0].dataIndex;
        const value = [x1, x2, x3][dataIndex];
        const label = ["时长小于4天", "时长4至7天", "时长大于7天"][dataIndex];
        return `${label}: ${value}`;
      }
    }
  };
  timeIntervalChart.setOption(option);
}
/**
 * 各用户类型平均接收时长统计分析图
 */
async function userTypeTimeOption(res) {
  //图标数据
  let data1;
  let data2;
  // 每个类别的数组
  let x1 = [];
  let x2 = [];
  let x3 = [];
  let x4 = [];
  if(res.data.userTypeTimeMap){
    data1 = Object.keys(res.data.userTypeTimeMap);
    data2 = Object.values(res.data.userTypeTimeMap);
    x1 = data2.map(item => item["时长小于4天"] ? item["时长小于4天"] : 0)
    x2 = data2.map(item => item["时长4至7天"] ? item["时长4至7天"] : 0)
    x3 = data2.map(item => item["时长大于7天"] ? item["时长大于7天"] : 0)
    x4 = data2.map(item => item["回执个数"] ? item["回执个数"] : 0)
  }

  const option: EChartsOption = {
    title: {
      text: "各用户类型平均接收时长统计分析图",
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
      data:['时长小于4天','时长4至7天','时长大于7天','回执个数']
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      height: '70%',
      right: '3%',
      bottom: '0',
      containLabel: true
    },
    yAxis: [{
      type: 'value',
      name: '天',
      // interval: 1,
    },
    {
      type: 'value',
      name:'个'
    }
    ],
    xAxis: {
      type: 'category',
      data: data1
    },
    series: [
      {
        name: '时长小于4天',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x1,
        itemStyle:{
          color:colorArr[0]
        }
      },
      {
        name: '时长4至7天',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x2,
        itemStyle:{
          color:colorArr[1]
        }
      },
      {
        name: '时长大于7天',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x3,
        itemStyle:{
          color:colorArr[2]
        }
      },
      {
        name: '回执个数',
        type: 'line',
        yAxisIndex: 1,
        data: x4,
        itemStyle:{
          color:'#02A7F0'
        }
      },
    ]
  };
  userTypeChart.setOption(option);
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

<style lang="scss">
.n-data-table-base-table {
  .n-data-table-table {
    .n-data-table-tbody {
      .selectedRowStyle2 {
        td {
          color:#DA433B !important;
        }
      }
    }
  }
}
</style>