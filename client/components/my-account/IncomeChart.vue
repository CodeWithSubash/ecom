<template>
  <div class="w-full max-w-full my-4">
    <div class="font-medium text-lg">Income by Month</div>
    <line-chart :data="barChartData" :options="barChartOptions" :height="400" />
  </div>
</template>

<script>
import LineChart from '~/components/charts/LineChart.vue'

export default {
  components: { LineChart },

  data() {
    return {
      barChartData: {
        labels: [
          'Jan',
          'Feb',
          'Mar',
          'Apr',
          'May',
          'Jun',
          'July',
          'Aug',
          'Sep',
          'Oct',
          'Nov',
          'Dec',
        ],
        datasets: [
          {
            label: 'Sales',
            data: [
              609, 1001, 888, 1234, 3210, 789, 400, 1600, 200, 140, 900, 500,
            ],
            backgroundColor: 'rgb(230, 101, 101, 0.3)',
            borderColor: 'red',
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
