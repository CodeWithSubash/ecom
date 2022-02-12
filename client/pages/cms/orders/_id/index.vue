<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
      </div>
    </template>

    <el-row :gutter="20">
      <el-col :span="24" :lg="18">
        <el-card v-if="orderDetail">
          <div slot="header">
            <div class="flex items-center justify-between">
              <div class="text-sm">
                Order No:
                <span class="tracking-wide text-red-400">
                  #{{ orderDetail.billNumber }}
                </span>
              </div>
              <el-select
                v-model="orderDetail.deliveryStatus"
                @change="handleStatusChange"
                :disabled="isSalesPerson"
                size="small"
              >
                <el-option
                  v-for="status in deliveryStatuses"
                  :key="status"
                  :label="status"
                  :value="status"
                ></el-option>
              </el-select>
            </div>
          </div>

          <el-row :gutter="20" class="mb-6">
            <el-col :span="6">
              <div class="font-medium text-sm mb-2">Created at</div>
              <div class="text-sm text-gray-600">
                <span>{{ orderDetail.createdAt | moment('YYYY/MM/DD') }}</span>
              </div>
            </el-col>
            <el-col :span="4">
              <div class="font-medium text-sm mb-2">Name</div>
              <div class="text-sm text-gray-600">
                {{ orderDetail.user.name }}
              </div>
            </el-col>
            <el-col :span="8">
              <div class="font-medium text-sm mb-2">Email</div>
              <div class="text-sm text-gray-600">
                {{ orderDetail.user.email ? orderDetail.user.email : '--' }}
              </div>
            </el-col>
            <el-col :span="6">
              <div class="font-medium text-sm mb-2">Contact No</div>
              <div class="text-sm text-gray-600">
                {{ orderDetail.user.mobile ? orderDetail.user.mobile : '--' }}
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24" :lg="12">
              <div
                class="border border-gray-200 rounded px-6 py-4 mb-6 lg:mb-0"
              >
                <div class="font-medium mb-2">Business Detail</div>
                <div class="text-sm">
                  <div class="mb-2 text-red-600">
                    {{ retailerDetail.businessName }}
                  </div>
                  <div class="mb-2">
                    {{
                      retailerDetail.businessAddress
                        ? retailerDetail.businessAddress
                        : '--'
                    }}
                  </div>
                  <div
                    v-if="retailerDetail.mobile"
                    class="flex items-center space-x-2"
                  >
                    <i class="bx bx-mobile-alt"></i>
                    <span>
                      {{ retailerDetail.mobile }}
                    </span>
                  </div>
                  <div
                    v-if="retailerDetail.landline"
                    class="flex items-center space-x-2"
                  >
                    <i class="bx bx-phone"></i>
                    <span>
                      {{ retailerDetail.landline }}
                    </span>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="24" :lg="12">
              <div class="border border-gray-200 rounded px-6 py-4">
                <div class="font-medium mb-2">Billing Address</div>
                <div class="text-sm">
                  <div class="mb-2">
                    {{
                      orderDetail.billingAddress
                        ? orderDetail.billingAddress
                        : '--'
                    }}
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-divider></el-divider>

          <el-row>
            <el-col :span="24" :lg="12">
              <div class="flex mb-2 space-x-2 text-sm">
                <div class="text-gray-600">Payment Method:</div>
                <div class="font-medium">{{ orderDetail.paymentMethod }}</div>
              </div>
            </el-col>
            <el-col :span="24" :lg="12">
              <div class="flex mb-2 space-x-2 text-sm">
                <div class="text-gray-600">Payment Status:</div>
                <div class="font-medium">{{ orderDetail.paymentStatus }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <div class="font-medium my-4">Order Items</div>

        <el-card class="mb-6">
          <el-table
            :data="orderDetail && orderDetail.orderDetails"
            v-loading="$fetchState.pending"
            border
          >
            <el-table-column
              prop="product.name"
              label="Product"
            ></el-table-column>
            <el-table-column label="Unit Price">
              <template #default="{ row }">
                TT$ {{ row.product.price }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="Quantity"></el-table-column>
            <el-table-column label="Total">
              <template #default="{ row }">
                TT$ {{ row.totalAmount }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="24" :lg="6">
        <template v-if="orderDetail">
          <el-card class="mb-6">
            <div slot="header">
              <div class="font-medium">Oder Summary</div>
            </div>
            <div class="text-sm">
              <div class="flex justify-between mb-2 space-x-2">
                <div class="text-gray-600">Subtotal:</div>
                <div class="font-medium">
                  TT$ {{ orderDetail.discountAmount + orderDetail.totalAmount }}
                </div>
              </div>
              <div class="flex justify-between mb-2 space-x-2">
                <div class="text-gray-600">Discount Amount:</div>
                <div class="font-medium">
                  - TT$ {{ orderDetail.discountAmount }}
                </div>
              </div>
              <div class="flex justify-between mb-2 space-x-2">
                <div class="text-gray-600">Total Amount:</div>
                <div class="font-medium">TT$ {{ orderDetail.totalAmount }}</div>
              </div>
              <div class="flex justify-between mb-2 space-x-2">
                <div class="text-gray-600">Paid Amount:</div>
                <div class="font-medium">TT$ {{ orderDetail.amountPaid }}</div>
              </div>
              <div class="flex justify-between space-x-2">
                <div class="text-gray-600">Remaining:</div>
                <div class="font-medium">
                  TT$ {{ orderDetail.amountRemaining }}
                </div>
              </div>
            </div>
          </el-card>
          <el-card>
            <div slot="header">
              <div class="font-medium">Invoice</div>
            </div>
            <div class="text-sm mb-4">
              Order No:
              <span class="text-red-600">#{{ orderDetail.billNumber }}</span>
            </div>
            <el-button type="plain" size="small" disabled
              >Download PDF
            </el-button>
          </el-card>
        </template>
      </el-col>
    </el-row>
  </with-header>
</template>

<script>
export default {
  name: 'OrderDetail',

  layout: 'cms',

  head: {
    title: 'Orders | Order Detail',
  },

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Orders',
          path: '/cms/orders',
        },
        {
          title: 'Order Detail',
          path: '',
        },
      ],

      status: 'PLACED',

      deliveryStatuses: [
        'PLACED',
        'REVIEWING',
        'PACKED',
        'SHIPPED',
        'DELIVERED',
      ],

      orderDetail: null,

      lastDeliveryStatus: null,
    }
  },

  computed: {
    orderId() {
      return this.$route.params.id
    },
    retailerDetail() {
      return this.orderDetail.user.userRetailer
    },
    isSalesPerson() {
      return this.$store.getters['user/isSalesPerson']
    },
  },

  watch: {
    'orderDetail.deliveryStatus': {
      handler(newValue, oldValue) {
        if (oldValue !== 'undefined') this.lastDeliveryStatus = oldValue
      },
    },
  },

  async fetch() {
    const { data } = await this.$axios.get(`/auth/order/${this.orderId}`)

    this.orderDetail = data
  },

  methods: {
    async handleStatusChange(deliveryStatus) {
      await this.$confirm(
        'Are you sure you want to change the delievery status?',
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
      )
        .then(() => {
          this.changeDeliveryStatus(deliveryStatus)
        })
        .catch(() => {
          if (this.lastDeliveryStatus)
            this.orderDetail.deliveryStatus = this.lastDeliveryStatus
        })
    },

    async changeDeliveryStatus(deliveryStatus) {
      try {
        this.$nuxt.$loading.start()
        const { status } = await this.$axios.put(
          `auth/order/${deliveryStatus}/${this.orderId}`
        )
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Order status changed',
          })
          this.$router.push('/cms/orders')
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          type: 'error',
          title: 'Error',
          message: error.response.data.message,
        })
      } finally {
        this.$nuxt.$loading.finish()
      }
    },
  },
}
</script>

<style></style>
