<template>
  <div>
    <template v-if="!$fetchState.pending">
      <el-row v-if="record" :gutter="20">
        <el-col :span="24" :lg="8" class="mb-6">
          <div
            class="rounded-md border bg-white hover:shadow-lg transition-shadow ease-in duration-200 mb-6"
          >
            <div class="relative h-28 bg-red-50 p-4">
              <div class="text-md font-medium text-red-600">Welcome Back!</div>
              <div class="text-xs text-red-300">{{ record.email }}</div>
              <div class="absolute top-4 right-4">
                <img
                  :src="require('~/assets/placeholders/data-points.svg')"
                  alt="Data Report"
                  class="h-20 object-center object-contain"
                />
              </div>
            </div>
            <div class="p-4 relative">
              <div class="flex space-x-6">
                <div class="w-1/3">
                  <div
                    class="absolute -top-1/2 transform translate-y-full border-4 border-white bg-red-300 h-14 w-14 rounded-full"
                  >
                    <!-- <img src="" class="" /> -->
                  </div>
                  <div class="mt-10 font-medium text-sm mb-2">
                    {{ $auth.user.name }}
                  </div>
                  <div class="text-xs">Sales Person</div>
                </div>
                <div class="w-2/3">
                  <div class="grid grid-cols-2 gap-1">
                    <div class="p-2">
                      <div class="font-medium mb-2">
                        {{ record.currentOrder }}
                      </div>
                      <div class="text-xs">Current Orders</div>
                    </div>
                    <div class="p-2">
                      <div class="font-medium mb-2">
                        TT$ {{ record.currentRevenue }}
                      </div>
                      <div class="text-xs">Revenue</div>
                    </div>
                    <div class="p-2">
                      <div class="font-medium mb-2">
                        TT$ {{ record.commission }}
                      </div>
                      <div class="text-xs">Commission</div>
                    </div>
                    <div class="grid place-content-center">
                      <el-button
                        type="danger"
                        size="small"
                        @click="$router.push('/cms/profile')"
                      >
                        View Profile
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <el-card shadow="hover">
            <div slot="header">
              <div class="font-medium text-lg">Monthly Earnings</div>
            </div>
            <div class="text-sm">This month</div>
            <div class="text-xl font-medium my-2">
              TT$ <span>{{ record.currentEarning }}</span>
            </div>
            <template v-if="earningDiff !== 0">
              <div class="flex items-center space-x-2 text-sm text-gray-400">
                <div
                  class="flex items-center"
                  :class="[earningDiff > 0 ? 'text-green-500' : 'text-red-500']"
                >
                  <div>
                    {{ ((earningDiff / currentEarning) * 100).toFixed(2) }} %
                  </div>
                  <i v-if="earningDiff > 0" class="bx bx-up-arrow-alt"></i>
                  <i v-else class="bx bx-down-arrow-alt"></i>
                </div>
                <span>From previous month</span>
              </div>
            </template>
          </el-card>
        </el-col>
        <el-col :span="24" :lg="16">
          <el-row :gutter="20">
            <el-col :span="12" :lg="8" class="mb-4">
              <count-card title="Orders">
                <template #icon>
                  <i class="bx bx-clipboard text-2xl"></i>
                </template>
                <div>{{ record.totalOrder }}</div>
              </count-card>
            </el-col>
            <el-col :span="12" :lg="8" class="mb-4">
              <count-card title="Revenue">
                <template #icon>
                  <i class="bx bx-user text-2xl"></i>
                </template>
                <div>
                  <span class="text-sm">TT$</span> {{ record.totalRevenue }}
                </div>
              </count-card>
            </el-col>
            <el-col :span="12" :lg="8">
              <count-card title="Average Price">
                <template #icon>
                  <i class="bx bx-user text-2xl"></i>
                </template>
                <div>
                  <span class="text-sm">TT$</span>
                  {{ record.totalAveragePrice }}
                </div>
              </count-card>
            </el-col>
          </el-row>
          <top-products-chart class="my-6" />
        </el-col>
      </el-row>
    </template>
    <div v-else>Loading...</div>
  </div>
</template>

<script>
import CountCard from './CountCard.vue'
import TopProductsChart from './TopProductsChart.vue'

export default {
  name: 'SalesDashboard',

  components: { TopProductsChart, CountCard },

  data() {
    return {
      record: null,
    }
  },

  computed: {
    prevEarning() {
      return this.record ? this.record.previousEarning : 0
    },
    currentEarning() {
      return this.record ? this.record.currentEarning : 0
    },
    earningDiff() {
      return this.currentEarning - this.prevEarning
    },
  },

  async fetch() {
    const { data } = await this.$axios.get(
      '/auth/userinformation/employeeRecord'
    )
    this.record = data
  },
}
</script>

<style></style>
