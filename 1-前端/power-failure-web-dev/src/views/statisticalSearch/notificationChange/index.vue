<template>
  <EchartsBasicCrud ref="echartsBasicCrudRef" :submit="submit" :reset="reset" :is-show-action="false" :columns="columns"
    :statistical-total-custom="true"
    :statisticalConfig="{ isHaveTotal: true, totalPosition: 'tableTop', tablePosition: 'left' }"
    name="analysis-alarm-service/api/v1/taskChangeStatistics" :crud-config="{ add: false, delete: false, edit: false }">
    <template #formTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        通知变更查询
      </div>
    </template>
    <template #statisticalTotalTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        通知变更统计
      </div>
    </template>
    <template #tableTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        通知变更列表
      </div>
    </template>
    <template #toolbar><n-button type="primary"
        @click="exportExcal('/analysis-alarm-service/api/v1/taskChangeStatistics/export', { condition: { ...formParams } }, '通知变更统计列表')">导出</n-button></template>
    <template #echartsTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        通知变更图表
      </div>
    </template>
    <!-- 统计card自定义 -->
    <template #statisticalTotalCustom>
      <div class="flex justify-between ">
        <div class="w-70 h-28  p-l-5 p-r-5 text-center boxShow flex flex-col items-center"
          style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4 w-42">当前通知变更数及通知总数</div>
          <div class="flex justify-between w-42">
            <div><span
                :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{ statisticalTotalCustomData.taskChangeQty }}</span><span>个</span>
            </div>
            <div><span
                :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{ statisticalTotalCustomData.taskSumQty }}</span><span>个</span>
            </div>
          </div>
        </div>
        <div class="w-70 h-28 p-l-5 p-r-5  text-center boxShow flex flex-col items-center"
          style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4 w-50">去年同期通知变更数及通知总数</div>
          <div class="flex justify-between w-50">
            <div><span
                :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{ statisticalTotalCustomData.preYearTaskChangeQty }}</span><span>个</span>
            </div>
            <div><span
                :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{ statisticalTotalCustomData.preYearTaskSumQty }}</span><span>个</span>
            </div>
          </div>
        </div>
        <div class="w-70 h-28 p-l-5 p-r-5  text-center boxShow" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4">通知变更数同比趋势</div>
          <div><span
              :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{ statisticalTotalCustomData.taskChangeTb }}</span><span>%</span>
          </div>
        </div>
      </div>
    </template>

    <template #echarts>
      <div>
        <div ref="reasonChartRef" id="reasonChart" class="h-90 w-100%"></div>
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
import { onMounted, Ref, ref, h } from "vue";
import { request } from "~/src/service/request";
import { useDicStore } from "~/src/store";
let dic = useDicStore();
import { exportExcal } from '~/src/utils/common/exportExcal'


// --------------------------------组件挂载-----------------------------
onMounted(async () => {
  await dic.loadData("task_reason,user,task_execute_status");
  reasonChart = init(reasonChartRef.value as HTMLElement);
  taskSourceTimeChart = init(taskSourceTimeRef.value as HTMLElement);
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
 * 公共弹窗抽屉的dom 和数据变量
 */
const transformData = ref({})
let taskDetailDrawerRef = ref(null)
/**
 * 各时长个数图表dom 和数据变量
 */
const reasonChartRef: Ref<HTMLElement | null> = ref(null);
let reasonChart: ECharts;

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
              height: '3rem',
              lineHeight: '3rem',
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
    field: "endTime",
    label: "送电时间",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 145,
    }
  },
  {
    field: "version",
    label: "版本",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 80,
    }
  },
  {
    field: "executeStatusList",
    label: "状态",
    formConfig: {
      hideSearch: false,
      component: "NSelect",
      componentProps: {
        multiple: true,
        options: dicDataArr(dic.getExecutionStatus),
      },
    },
    tableConfig: {
      width: 100,
      render(row) {
        return h(
          "span",
          {},
          dicDataArr(dic.getExecutionStatus)?.filter((item) => item.value == row.executeStatus)[0]
            ?.label
        );
      },
    },
  },
  {
    field: "deviceName",
    label: "设备名称",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 110,
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
      width: 130,
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
    field: "ranges",
    label: "停电范围",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 150,
    }
  },
  {
    field: "taskUserCount",
    label: "停电户数",
    formConfig: {
      hideSearch: true,
    },
    tableConfig: {
      width: 90,
    }
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
      width: 160,
    }
  },


];
/**
 * 颜色代码
 */
const colorArr = [
' #36A490',
"rgb(150,208,207)"
];
/**
 * 统计数据字体大小
 */
const statisticalFontSize = "40px";
/**
 * 统计数据字体粗细
 */
const statisticalFontWeight = 700;

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
    `analysis-alarm-service/api/v1/taskChangeStatistics/total`,
    { ...params }
  );
  statisticalTotalCustomData.value = res.data
  reasonChartOption(res);
  taskSourceTimeOption(res);
}
/**
 * 不同停电原因通知变更统计分析图数据处理
 */
async function reasonChartOption(res) {
  //图标数据
  let data1;
  let data2;
  let arr: any = [];
  let x1;
  let x2;
  
  if (res.data.taskReasonChangeMap) {
    data1 = Object.keys(res.data.taskReasonChangeMap);
    data2 = Object.values(res.data.taskReasonChangeMap);
    data1.forEach((item) => {
      arr.push(dicDataArr(dic.getPowerFailureReason)?.filter((aa) => aa.value == item)[0]?.label)
    })
    x1 = data2.map(item => item["Y"] ? item["Y"] : 0)
    x2 = data2.map(item => item["N"] ? item["N"] : 0)
  }


  const option: EChartsOption = {
    title: {
      text: "不同停电原因通知变更统计分析图",
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
      top: 25,
    },
    grid: {
      height: '75%',
      right: '3%',
      bottom: '5%',
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
      name: '个',
      // interval: 1,
      // axisLabel: {
      //   formatter: ''
      // }
    },
    xAxis: {
      type: 'category',
      data: arr
    },
    series: [
      {
        name: '有变更',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x1,
        itemStyle: {
          color: colorArr[0]
        }
      },
      {
        name: '未变更',
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
        itemStyle: {
          color: colorArr[1]
        }
      },

    ]
  };
  reasonChart.setOption(option);
}
/**
 * 不同来源通知变更统计分析图数据处理
 */
async function taskSourceTimeOption(res) {
  //图标数据
  let data1;
  let data2;
  let x1;
  let x2;
  if (res.data.taskSourceChangeMap) {
    data1 = Object.keys(res.data.taskSourceChangeMap);
    data2 = Object.values(res.data.taskSourceChangeMap);
    x1 = data2.map(item => item["Y"] ? item["Y"] : 0)
    x2 = data2.map(item => item["N"] ? item["N"] : 0)
  }

  const option: EChartsOption = {
    title: {
      text: "不同来源通知变更统计分析图",
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
      top: 25,
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '3%',
      bottom: '3%',
      containLabel: true
    },
    yAxis: {
      type: 'value',
      name: '个',
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
        name: '有变更',
        type: 'bar',
        stack: 'total',
        label: {
          // show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: x1,
        itemStyle: {
          color: colorArr[0]
        }
      },
      {
        name: '未变更',
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
        itemStyle: {
          color: colorArr[1]
        }
      },

    ]
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