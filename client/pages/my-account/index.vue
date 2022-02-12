<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <el-row :gutter="20">
        <el-col :span="24" :lg="6" class="mb-6">
          <template v-if="userDetail">
            <div class="flex items-center py-4 space-x-4">
              <img
                src="https://i.pravatar.cc/300"
                alt="User Avatar"
                class="h-20 w-20 rounded-full object-center object-cover border-2 border-red-600 p-0.5"
              />
              <div>
                <div class="text-lg font-medium">
                  {{ userDetail.name }}
                </div>
                <div class="text-gray-500 text-sm my-2">
                  {{ userDetail.role }}
                </div>
                <div
                  class="text-sm text-red-500 cursor-pointer hover:underline transition ease-in duration-200"
                >
                  Edit
                </div>
              </div>
            </div>
          </template>
        </el-col>
        <el-col :span="24" :lg="6" class="mb-6">
          <div class="flex items-center space-x-4 bg-red-50 p-6 rounded">
            <i class="bx bx-package bx-lg text-red-600"></i>
            <div>
              <div class="text-black text-lg font-medium">24</div>
              <div class="text-black text-base text-red-600">My Orders</div>
            </div>
          </div>
        </el-col>
        <el-col :span="24" :lg="6" class="mb-6">
          <div class="flex items-center space-x-4 bg-red-50 p-6 rounded">
            <i class="bx bx-heart bx-lg text-red-600"></i>
            <div>
              <div class="text-black text-lg font-medium">
                {{ wishlistCount }}
              </div>
              <nuxt-link to="/wishlist">
                <div class="text-black text-base text-red-600">My Wishlist</div>
              </nuxt-link>
            </div>
          </div></el-col
        >
        <el-col :span="24" :lg="6" class="mb-6">
          <div class="flex items-center space-x-4 bg-red-50 p-6 rounded">
            <i class="bx bx-dollar bx-lg text-red-600"></i>
            <div>
              <div class="text-black text-lg font-medium">TT$ 1400</div>
              <div class="text-black text-base text-red-600">My Profits</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <income-chart class="w-full mb-16" />

      <el-tabs v-model="activeTab">
        <el-tab-pane name="kyc">
          <span slot="label">
            <div class="text-black text-lg font-medium">My Information</div>
          </span>
          <kyc-details
            v-if="userDetail"
            :retailerDetail="userDetail.userRetailer"
          />
          <div v-else-if="$fetchState.pending">Loading...</div>
        </el-tab-pane>
        <el-tab-pane name="orders">
          <span slot="label">
            <div class="text-black text-lg font-medium">My Orders</div>
          </span>
          <div>Orders will appear here</div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import BreadCrumb from '~/components/ui/BreadCrumb.vue'
import KycDetails from '~/components/my-account/KycDetails.vue'
import IncomeChart from '~/components/my-account/IncomeChart.vue'

export default {
  name: 'MyAccount',

  layout: 'retailer',

  middleware: 'auth',

  head: {
    title: 'KMPL | Account',
  },

  components: {
    BreadCrumb,
    KycDetails,
    IncomeChart,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'My Account',
          path: '/my-account',
        },
      ],

      activeTab: 'kyc',

      userDetail: null,
    }
  },

  computed: {
    wishlistCount() {
      return this.$store.getters['wishlist/wishlistCount']
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/user')

    this.userDetail = data
  },
}
</script>

<style>
.el-tabs__active-bar {
  background: black;
}
</style>
