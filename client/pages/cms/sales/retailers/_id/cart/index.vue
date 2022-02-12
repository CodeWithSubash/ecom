<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
      </div>
    </template>

    <template v-if="retailerDetail">
      <div>
        <span class="text-gray-500"> Name: </span>
        <span> {{ retailerDetail.name }} </span>
      </div>
      <div>
        <span class="text-gray-500"> Business Name: </span>
        <span> {{ retailerDetail.userRetailer.businessName }} </span>
      </div>
      <div>
        <span class="text-gray-500"> Business Address: </span>
        <span>
          {{
            retailerDetail.userRetailer.businessAddress
              ? retailerDetail.userRetailer.businessAddress
              : '--'
          }}
        </span>
      </div>
    </template>

    <div class="my-4" v-loading="$fetchState.pending || loading">
      <product-table
        :products="cartProducts"
        :add-to-cart="handleAddToCart"
        @onProductChange="cartProducts = $event"
      />
    </div>
    <el-card
      v-loading="$fetchState.pending"
      :disabled="$fetchState.pending || loading || !cartProducts.length"
    >
      <el-row>
        <el-col :span="12">
          <div class="mb-2">
            <el-checkbox v-model="showBillingAddress" class="mb-2"
              >Use another Billing Address
            </el-checkbox>
            <el-input
              v-if="showBillingAddress"
              v-model="order.billingAddress"
              placeholder="Enter another billing address"
              class="mb-2"
            ></el-input>
          </div>
          <div class="flex justify-between mb-4">
            <div>
              <div class="text-sm underline mb-2">Payment Method</div>
              <el-radio-group v-model="order.paymentMethod">
                <el-radio
                  v-for="method in paymentMethods"
                  :label="method"
                  :key="method"
                >
                  {{ method }}
                </el-radio>
              </el-radio-group>
            </div>
            <div>
              <div class="text-sm underline mb-2">Delievery Type</div>
              <el-radio-group v-model="order.deliveryType">
                <el-radio
                  v-for="type in deliveryTypes"
                  :label="type"
                  :key="type"
                >
                  {{ type }}
                </el-radio>
              </el-radio-group>
            </div>
          </div>
          <div>
            <div class="text-sm underline my-2">Notes</div>
            <el-input
              v-model="order.notes"
              type="textarea"
              rows="4"
              placeholder="Additional Information for the order"
            ></el-input>
          </div>
        </el-col>
        <el-col :span="10" :offset="2" class="space-y-2">
          <div class="text-sm underline">Order Summary</div>
          <div class="flex justify-between">
            <div class="text-sm text-gray-500">
              Subtotal ({{ `${cartProducts.length}x` }})
            </div>
            <div>$TT {{ cartSubtotal }}</div>
          </div>
          <div class="flex justify-between space-x-4" disabled>
            <el-input placeholder="Promo code" size="medium"></el-input>
            <el-button size="medium" type="primary">Apply Code</el-button>
          </div>
          <div class="flex justify-between">
            <div class="text-sm text-gray-500">Discount:</div>
            <div>0</div>
          </div>
          <div class="flex justify-between">
            <div class="text-sm text-gray-500">Total:</div>
            <div>$TT {{ totalAmount }}</div>
          </div>
          <div class="flex justify-end">
            <el-button
              size="medium"
              type="success"
              :loading="loading"
              @click="handlePlaceOrder"
              >Place Order
            </el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </with-header>
</template>

<script>
import ProductTable from '~/components/sales/ProductTable.vue'

export default {
  name: 'RetailerCart',

  components: { ProductTable },

  layout: 'cms',

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Retailers',
          path: '/cms/sales/retailers',
        },
        {
          title: "Retailer's Cart",
          path: '',
        },
      ],

      retailerDetail: null,

      cartProducts: [],

      order: {
        notes: '',
        totalAmount: 0,
        paymentMethod: 'COD',
        deliveryType: 'Collection',
      },
      paymentMethods: ['COD', 'Credit'],
      deliveryTypes: ['Collection', 'Delievered'],
      showBillingAddress: false,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
    retailerId() {
      return this.$route.params.id
    },
    cartSubtotal() {
      return this.cartProducts.reduce(
        (acc, cartItem) => (acc += cartItem.price * cartItem.quantity),
        0
      )
    },
    totalAmount() {
      return this.cartSubtotal
    },
  },

  watch: {
    totalAmount: {
      immediate: true,
      handler(nv) {
        this.order = {
          ...this.order,
          totalAmount: nv,
        }
      },
    },
  },

  async fetch() {
    const {
      data: { cartDetail },
    } = await this.$axios.get(`/auth/cart/getByUser/${this.retailerId}`)
    let mappedProducts = []

    if (cartDetail) {
      mappedProducts = cartDetail.map((ci) => ({
        ...ci.product,
        quantity: ci.quantity,
      }))
    }

    this.cartProducts = mappedProducts
  },

  async created() {
    const { data } = await this.$axios.get(
      `/auth/user/${this.$route.params.id}`
    )
    this.retailerDetail = data
  },

  methods: {
    async handleAddToCart({ id, quantity }) {
      try {
        const { status } = await this.$axios.post('/auth/cart/addToCart', {
          userId: this.retailerId,
          productId: id,
          quantity,
        })
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: "Retailer's cart updated",
          })
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
      }
    },

    async handlePlaceOrder() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.post(
          `auth/order/retailer/${this.retailerId}`,
          this.order
        )
        if (status === 200) {
          this.$router.push('/cms/sales/retailers')
          this.$store.dispatch('notification/setNotification', {
            message: 'Order successfully placed',
          })
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },
  },
}
</script>

<style></style>
