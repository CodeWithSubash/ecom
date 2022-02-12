<template>
  <div class="w-full my-4">
    <el-card shadow="hover">
      <div slot="header">
        <div class="font-medium text-lg">Shopping Status</div>
      </div>
      <line-chart
        :data="barChartData"
        :options="barChartOptions"
        :height="400"
      />
    </el-card>
  </div>
</template>

<script>
import LineChart from '~/components/charts/LineChart.vue'

export default {
  components: { LineChart },

  data() {
    return {
      barChartData: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr'],
        datasets: [
          {
            label: 'Online',
            data: [200, 140, 900, 500],
            backgroundColor: 'rgb(230, 101, 101, 0.3)',
            borderColor: 'rgb(227, 83, 83)',
            borderWidth: 2,
          },
          {
            label: 'Offline',
            data: [150, 399, 298, 700],
            backgroundColor: 'rgb(83, 124, 227, 0.3)',
            borderColor: 'rgb(35, 87, 219)',
            borderWidth: 2,
          },
        ],
      },
      barChartOptions: {
        maintainAspectRatio: false,
      },
    }
  },
}
</script>

<style></style>
