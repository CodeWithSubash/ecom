<template>
  <div>
    <div class="flex justify-end">
      <el-button-group>
        <el-button
          v-for="filter in counterFilters"
          :key="filter"
          type="danger"
          :plain="currentFilter !== filter"
          size="small"
          @click="currentFilter = filter"
        >
          {{ filter }}
        </el-button>
      </el-button-group>
    </div>
    <br />
    <!-- Counters  -->
    <el-row :gutter="20">
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Employees">
          <template #icon>
            <i class="bx bx-group text-2xl"></i>
          </template>
          <div>412</div>
        </count-card>
      </el-col>
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Retailers">
          <template #icon>
            <i class="bx bx-store-alt text-2xl"></i>
          </template>
          <div>23</div>
        </count-card>
      </el-col>
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Sales Representative">
          <template #icon>
            <i class="bx bx-user text-2xl"></i>
          </template>
          <div>52</div>
        </count-card>
      </el-col>
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Pending KYC">
          <template #icon>
            <i class="bx bx-file text-2xl"></i>
          </template>
          <div>7</div>
        </count-card>
      </el-col>
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Products">
          <template #icon>
            <i class="bx bx-package text-2xl"></i>
          </template>
          <div>37</div>
        </count-card>
      </el-col>
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Categories">
          <template #icon>
            <i class="bx bx-category text-2xl"></i>
          </template>
          <div>24</div>
        </count-card>
      </el-col>
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Orders">
          <template #icon>
            <i class="bx bx-clipboard text-2xl"></i>
          </template>
          <div>200</div>
        </count-card>
      </el-col>
      <el-col :span="12" :lg="6" class="mb-4">
        <count-card title="Total Sales">
          <template #icon>
            <i class="bx bx-dollar text-2xl"></i>
          </template>
          <div><span class="text-sm">TT$</span> 160,590</div>
        </count-card>
      </el-col>
    </el-row>
    <br />
    <shopping-status-chart />
    <br />
    <top-products-chart />
    <br />
    <recent-orders />
  </div>
</template>

<script>
import CountCard from './CountCard.vue'
import TopProductsChart from './TopProductsChart.vue'
import ShoppingStatusChart from './ShoppingStatusChart.vue'
import RecentOrders from './RecentOrders.vue'

export default {
  components: {
    CountCard,
    TopProductsChart,
    ShoppingStatusChart,
    RecentOrders,
  },

  name: 'AdminDashboard',

  data() {
    return {
      counterFilters: [
        'All Time',
        'Today',
        'Week',
        'Month',
        'Year',
        'Last Year',
      ],
      currentFilter: 'All Time',
    }
  },
}
</script>

<style></style>
