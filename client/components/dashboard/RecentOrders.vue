<template>
  <div>
    <div class="font-medium text-lg mb-4">Recent Orders</div>
    <div
      v-for="(group, index) in recentOrdersGroups"
      :key="`${group}-${index}`"
      class="mb-8 last:mb-0"
    >
      <el-card v-loading="$fetchState.pending" shadow="hover">
        <div slot="header">
          <div class="font-medium">{{ group.title }}</div>
        </div>
        <order-table :orders="group.data" />
      </el-card>
    </div>
  </div>
</template>

<script>
import moment from 'moment'

import OrderTable from '../templates/OrderTable.vue'

export default {
  name: 'RecentOrders',

  components: { OrderTable },

  data() {
    return {
      todayOrders: [],
      yesterdayOrders: [],
      weekOrders: [],
      monthOrders: [],
      yearOrders: [],
      lastYearOrders: [],
    }
  },

  computed: {
    recentOrdersGroups() {
      return [
        { title: 'Today', data: this.todayOrders },
        { title: 'Yesterday', data: this.yesterdayOrders },
        { title: 'This Week', data: this.weekOrders },
        { title: 'This Month', data: this.monthOrders },
        // { title: 'This Year', data: this.yearOrders },
        // { title: 'Past', data: this.lastYearOrders },
      ]
    },
  },

  async fetch() {
    let params = {
      pageNo: 0,
      pageSize: 5,
      orderType: 'desc',
    }

    await this.getTodayOrders(params)
    await this.getYesterdayOrders(params)
    await this.getWeekOrders(params)
    await this.getMonthOrders(params)
    await this.getYesterdayOrders(params)
  },

  methods: {
    async getOrders(params) {
      const { data } = await this.$axios.get('/auth/order', {
        params: {
          ...params,
        },
      })

      return data.data
    },

    async getTodayOrders(params) {
      const dateRange = {
        startDate: moment().format('YYYY-MM-DD 00:00:00'),
        endDate: moment().format('YYYY-MM-DD 00:00:00'),
      }
      this.todayOrders = await this.getOrders({ ...params, ...dateRange })
    },

    async getYesterdayOrders(params) {
      const dateRange = {
        startDate: moment().subtract(1, 'days').format('YYYY-MM-DD 00:00:00'),
        endDate: moment().subtract(1, 'days').format('YYYY-MM-DD 00:00:00'),
      }
      this.yesterdayOrders = await this.getOrders({ ...params, ...dateRange })
    },

    async getWeekOrders(params) {
      const dateRange = {
        startDate: moment().startOf('week').format('YYYY-MM-DD 00:00:00'),
        endDate: moment().endOf('week').format('YYYY-MM-DD 00:00:00'),
      }
      this.weekOrders = await this.getOrders({ ...params, ...dateRange })
    },

    async getMonthOrders(params) {
      const dateRange = {
        startDate: moment().startOf('month').format('YYYY-MM-DD 00:00:00'),
        endDate: moment().endOf('month').format('YYYY-MM-DD 00:00:00'),
      }
      this.monthOrders = await this.getOrders({ ...params, ...dateRange })
    },

    async getYesterdayOrders(params) {
      const dateRange = {
        startDate: moment().startOf('year').format('YYYY-MM-DD 00:00:00'),
        endDate: moment().endOf('year').format('YYYY-MM-DD 00:00:00'),
      }
      this.yearOrders = await this.getOrders({ ...params, ...dateRange })
    },
  },
}
</script>

<style></style>
