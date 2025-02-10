<template>
  <EchartsBasicCrud ref="echartsBasicCrudRef" :submit="submit" :reset="reset" :is-show-action="false" :columns="columns"
    :statistical-total-custom="true" name="analysis-alarm-service/api/v1/powerCutStatistics"
    :crud-config="{ add: false, delete: false, edit: false }" :statisticalConfig="{
      isHaveTotal: true,
      totalPosition: 'echartTop',
      tablePosition: 'left',
    }">

    <!-- * totalPosition：值可以选择top、tableTop、echartTop
   * tablePosition：值可以选择left、right -->
    <template #formTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电查询
      </div>
    </template>
    <template #statisticalTotalTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电统计
      </div>
    </template>
    <template #tableTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电列表
      </div>
    </template>
    <template #echartsTitle>
      <div class="font-700 b-l-#044FE1 b-l-5 p-l-2 m-b-2" style="fontSize: 18px">
        停电图表
      </div>
    </template>
    <template #toolbar><n-button type="primary"
        @click="exportExcal('/analysis-alarm-service/api/v1/powerCutStatistics/export', { condition: { ...formParams } }, '停电统计列表')">导出</n-button></template>
    <!-- 111111111111111111111111111111111111111111111 -->

    <!-- 统计card自定义 -->
    <template #statisticalTotalCustom>
      <div class="flex justify-between ">
        <div class="w-70 h-28  p-l-5 p-r-5 text-center boxShow flex flex-col items-center"
          style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4 w-42" style="transform: scale(.8);">当期停电区域数</div>
          <div>
            <span :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">
              {{ statisticalTotalCustomData.powerCutRegionQty }}
            </span>
            <span>个</span>
          </div>
        </div>
        <div class="w-70 h-28 p-l-5 p-r-5  text-center boxShow flex flex-col items-center"
          style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4 w-50" style="transform: scale(.8);">去年同期停电区域数</div>
          <div>
            <div><span
                :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">{{
                  statisticalTotalCustomData.preYearPowerCutRegionQty }}</span><span>个</span>
            </div>
          </div>
        </div>
        <div class="w-70 h-28 p-l-5 p-r-5  text-center boxShow flex flex-col items-center" style="box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2)">
          <div class="p-t-2 m-b-4" style="white-space: nowrap;transform: scale(.8);">停电区域数同比趋势</div>
          <div style="width: 140%;">
            <span :style="{ fontSize: statisticalFontSize, color: colorArr[0], fontWeight: statisticalFontWeight }">
          {{
            statisticalTotalCustomData.powerCutRegionTb === null ? '--' : statisticalTotalCustomData.powerCutRegionTb
          }}
          </span><span>%</span>
          </div>
        </div>
      </div>
    </template>
    <!-- 111111111111111111111111111111111111111111111 -->

    <template #echarts>
      <div ref="regionChartRef" id="regionChart" class="h-90 w-100%"></div>
    </template>
  </EchartsBasicCrud>


  <!-- 关联通知单抽屉 -->
  <TaskDetailDrawer ref="modalRef" :transformId="transfromData.taskId" transformIdType="task"></TaskDetailDrawer>
</template>

<script lang="ts" setup>
import { EchartsBasicCrud } from "~/src/components/echartsBusiness/echartCrud";
import type { ECharts, EChartsOption } from "echarts";
import { init } from "echarts";
import { onMounted, onBeforeUnmount, Ref, ref, reactive, h, nextTick } from "vue";
import { request } from "~/src/service/request";
import { exportExcal } from '~/src/utils/common/exportExcal'



// 关联通知单数据定义
let transfromData = ref({})
let modalRef = ref(null)

// --------------------------------组件挂载-----------------------------
onMounted(async () => {
  regionChart = init(regionChartRef.value as HTMLElement);
  initApi();
});
// -------------------------------定义变量-----------------------------
/**
 * 查询表单数据 供给导出使用，统计数量和图表的不用formParams，initApi()时候直接传了
 */
/**
 * 统计数据变量定义
 */
const statisticalTotalCustomData = ref({})
const formParams = ref({})
/**
 * 表格列表
 */
const columns = [
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
              cursor: 'pointer',
              display: 'inline-block',
              height: '3rem',
              lineHeight: '3rem',
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
  {
    field: "region",
    label: "台区",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 130,
    }
  },
  {
    field: "regionUserCount",
    label: "台区户数",
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
      hideSearch: true,
    },
    tableConfig: {
      width: 120,
      render(row) {
        let arr = ['设备检修', '配合客户接入', '配合市政过程']
        return h(
          'span',
          {

          },
          { default: () => `${arr[row.reason - 1]}` }
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
    field: "stationName",
    label: "变电站",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 140,
    }
  },
  {
    field: "lineName",
    label: "线路名称",
    formConfig: {
      hideSearch: false,
    },
    tableConfig: {
      width: 140,
    }
  },



];

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
/**
 * 
 * 统计数据数组
 */
// const statisticalColumn = reactive([
//   {
//     label: "当前停电区域数",
//     field: "powerCutRegionQty",
//     unit: "个",
//     value: 0,
//     style: { color: colorArr[0], fontSize: statisticalFontSize, fontWeight: 700 },
//   },
//   {
//     label: "去年同期停电区域数",
//     field: "preYearPowerCutRegionQty",
//     unit: "个",
//     value: 0,
//     style: { color: colorArr[0], fontSize: statisticalFontSize, fontWeight: 700 },
//   },
//   {
//     label: "停电区域数同比趋势",
//     field: "powerCutRegionTb",
//     unit: "%",
//     value: 0,
//     style: { color: colorArr[0], fontSize: statisticalFontSize, fontWeight: 700 },
//   }
// ]);
/**
 * 区域数图表dom
 */
const regionChartRef: Ref<HTMLElement | null> = ref(null);
let regionChart: ECharts;


// --------------------------------处理图表数据 统计数据Method-------------------------------

// let timer = null;
/**
 * 获取统计数据 赋给statisticalColumn，同时赋予图标数据，渲染到页面上,
 */
// function initApi(params = {}) {
//   if (timer != null) clearInterval(timer)
//   timer = setInterval(async () => {
//     const res = await request.post(
//       `analysis-alarm-service/api/v1/powerCutStatistics/total`,
//       { ...params }
//     );
//     // statisticalColumn.forEach((item) => {
//     //   item.value = res.data[item.field];
//     // });
//     console.log('1111111111111111')
//     statisticalTotalCustomData.value = res.data
//     regionChartOption(res);
//   }, 1000)

// }

// onBeforeUnmount(()=>{
//   clearInterval(timer)
// })

async function initApi(params = {}) {
  const res = await request.post(
    `analysis-alarm-service/api/v1/powerCutStatistics/total`,
    { ...params }
  );
  // statisticalColumn.forEach((item) => {
  //   item.value = res.data[item.field];
  // });
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
  // 每个类别的数组
  let x1 = [];
  let x2 = [];
  let x3 = [];
  if (res.data.reasonThbMap) {
    data1 = Object.keys(res.data.reasonThbMap);
    data2 = Object.values(res.data.reasonThbMap);
    x1 = data2.map(item => item.currYearRegionQty ? item.currYearRegionQty : 0)
    x2 = data2.map(item => item.preYearRegionQty ? item.preYearRegionQty : 0)
    x3 = data2.map(item => item.regionQtyHb ? item.regionQtyHb : 0)
  }
  const option: EChartsOption = {
    title: {
      text: "不同停电原因的同环比分析图",
      left: "center",
      show:true,
      top: -4,
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
    legend: {
       top: 45,
    },
    grid: {
      height: '70%',
      right: '3%',
      bottom: '0',
      containLabel: true
    },
    yAxis: {
      type: 'value',
      name: '个',

    },
    xAxis: {
      type: 'category',
      data: data1
    },
    series: [
      {
        name: '本期',
        type: 'bar',
        data: x1,
        itemStyle: {
          color: colorArr[1]
        }
      },
      {
        name: '环比',
        type: 'bar',
        data: x3,
        itemStyle: {
          color: colorArr[2]
        }
      },
      {
        name: '同比',
        type: 'bar',
        data: x2,
        itemStyle: {
          color: colorArr[3]
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
  initApi(params);
}
//reset和submit同理
function reset() {
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