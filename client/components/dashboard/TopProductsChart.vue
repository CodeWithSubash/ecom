<template>
  <div class="w-full">
    <el-card shadow="hover">
      <div slot="header">
        <div class="font-medium text-lg">Top Selling Products</div>
      </div>
      <bar-chart
        :data="barChartData"
        :options="barChartOptions"
        :height="400"
      />
    </el-card>
  </div>
</template>

<script>
import BarChart from '~/components/charts/BarChart.vue'

export default {
  components: { BarChart },

  data() {
    return {
      barChartData: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr'],
        datasets: [
          {
            label: 'Product A',
            data: [200, 140, 900, 500],
            backgroundColor: 'rgb(230, 101, 101, 0.3)',
            borderColor: 'rgb(227, 83, 83)',
            borderWidth: 2,
          },
          {
            label: 'Product B',
            data: [300, 640, 400, 600],
            backgroundColor: 'rgba(20, 255, 0, 0.3)',
            borderColor: 'rgba(100, 255, 0, 1)',
            borderWidth: 2,
          },
          {
            label: 'Product C',
            data: [150, 399, 298, 700],
            backgroundColor: 'rgb(83, 124, 227, 0.3)',
            borderColor: 'rgb(35, 87, 219)',
            borderWidth: 2,
          },
          {
            label: 'Product D',
            data: [400, 300, 200, 300],
            backgroundColor: 'rgb(219, 220, 224, 0.3)',
            borderColor: 'rgb(201, 202, 206)',
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
