<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <!-- Form  -->
      <div class="flex flex-col lg:flex-row lg:space-x-10 mb-2">
        <div class="flex flex-col space-y-6 w-full md:w-1/2 mb-10">
          <div class="text-2xl lg:text-3xl font-semibold mb-2">
            Billing details
          </div>

          <div class="text-red-600 underline">
            {{ $auth.user.userRetailer.businessName }}
          </div>
          <div>
            <span class="text-gray-500">Proprietor: </span>
            <span class="font-medium">{{ $auth.user.name }}</span>
          </div>
          <div v-if="!showBillingAddress">
            <span class="text-gray-500">Billing Address: </span>
            <span class="font-medium">{{
              $auth.user.userRetailer.billingAddress
                ? $auth.user.userRetailer.billingAddress
                : '--'
            }}</span>
          </div>
          <div>
            <label class="inline-flex items-center">
              <input
                v-model="showBillingAddress"
                class="
                  text-red-500
                  mr-2
                  focus:ring-red-400 focus:ring-opacity-25
                  border border-red-300
                  rounded
                "
                type="checkbox"
              />
              Use another Billing Address
            </label>
            <base-input
              v-if="showBillingAddress"
              v-model="order.billingAddress"
              placeholder="Enter billing address"
            >
            </base-input>
          </div>
        </div>
        <div class="w-full md:w-1/2 mb-10">
          <div class="text-2xl lg:text-3xl font-semibold mb-6">
            Additional information
          </div>
          <base-input
            label="Order notes (Optional)"
            v-model="order.notes"
            placeholder="Notes about your order, e.g. special notes for delivery"
            textarea
          />
        </div>
      </div>

      <template v-if="cartItems.length">
        <!-- Orders  -->
        <div class="mb-10">
          <div class="text-2xl lg:text-3xl font-semibold mb-4">Your order</div>
          <!-- Coupon  -->
          <div class="w-full flex flex-col lg:flex-row lg:justify-between mb-8">
            <apply-coupon
              @validCoupon="handleValidCoupon"
              class="w-full lg:w-1/3"
            />
            <coupon-card
              v-if="couponDetail"
              :coupon="couponDetail"
              class="w-full lg:w-1/3"
            />
          </div>
          <table class="w-full">
            <thead class="bg-gray-100">
              <tr>
                <th class="border border-gray-200 text-left p-4 font-medium">
                  Product
                </th>
                <th class="border border-gray-200 text-left p-4 font-medium">
                  Subtotal
                </th>
              </tr>
            </thead>
            <tbody class="border border-gray-200">
              <tr v-for="item in cartItems" :key="item.id">
                <td data-title="Product" class="border border-gray-200 p-4">
                  <span>
                    {{ item.product.name }}
                    <span class="text-gray-400 text-sm">
                      {{ `(${item.quantity}x)` }}
                    </span>
                  </span>
                </td>
                <td data-title="Subtotal" class="border border-gray-200 p-4">
                  ${{ item.product.price * item.quantity }}
                </td>
              </tr>
              <tr class="font-semibold mt-4">
                <td class="p-4 text-right hidden lg:block">Subtotal</td>
                <td data-title="Subtotal" class="p-4 border border-gray-200">
                  TT$ {{ subtotal }}
                </td>
              </tr>
              <tr v-if="discountAmount > 0" class="font-semibold">
                <td class="p-4 text-right hidden lg:block">Discount</td>
                <td data-title="Total" class="p-4 border border-gray-200">
                  - TT$ {{ discountAmount }}
                </td>
              </tr>
              <tr class="font-semibold">
                <td class="p-4 text-right hidden lg:block">Total</td>
                <td data-title="Total" class="p-4 border border-gray-200">
                  TT$ {{ totalAmount }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="mb-8">
          <div class="mb-2">Payment Type</div>
          <div class="flex space-x-4">
            <div class="flex items-center space-x-2">
              <input
                v-model="order.paymentMethod"
                id="cod"
                type="radio"
                value="COD"
                checked
              />
              <label for="cod" class="flex items-center cursor-pointer">
                COD
              </label>
            </div>
            <div class="flex items-center space-x-2">
              <input
                v-model="order.paymentMethod"
                id="credit"
                type="radio"
                value="Credit"
                checked
              />
              <label for="credit" class="flex items-center cursor-pointer">
                Credit
              </label>
            </div>
          </div>
        </div>
        <div class="bg-gray-200 px-4 py-6">
          <p class="mb-6">
            Your personal data will be used to process your order, support your
            experience throughout this website, and for other purposes described
            in our
            <span class="text-red-600">
              <nuxt-link to="/">privacy policy</nuxt-link> </span
            >.
          </p>
          <div class="flex justify-end">
            <div @click="handleCreateOrder">
              <base-button
                inverse
                on-hover
                small
                :loading="loading"
                class="capitalize"
              >
                Place Order
              </base-button>
            </div>
          </div>
        </div>
      </template>
      <empty-entity v-else>
        Please add product in your cart to checkout!
      </empty-entity>
    </div>
  </div>
</template>

<script>
import ApplyCoupon from '~/components/checkout/ApplyCoupon.vue'
import CouponCard from '~/components/checkout/CouponCard.vue'
import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'Checkout',

  layout: 'retailer',

  components: { BreadCrumb, ApplyCoupon, CouponCard },

  middleware: 'auth',

  head: {
    title: 'KMPL | Checkout',
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Checkout',
          path: '/checkout',
        },
      ],

      showBillingAddress: false,

      billingAddress: '',

      order: {
        notes: '',
        totalAmount: 0,
        paymentMethod: 'COD',
      },

      couponDetail: '',
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
    cartItems() {
      return this.$store.state.cart.cartItems
    },
    subtotal() {
      return this.$store.getters['cart/cartSubtotal']
    },
    totalAmount() {
      return this.subtotal - this.discountAmount
    },

    couponDiscountVal() {
      return this.couponDetail && this.couponDetail.discountValue
    },

    discountAmount() {
      if (this.subtotal >= this.couponDetail.minimumOrderValue) {
        if (this.couponDetail.couponType === 'CURRENCY')
          return this.couponDiscountVal

        if (this.couponDetail.couponType === 'PERCENTAGE') {
          let amount = 0
          if (!this.couponDetail.category) {
            amount = (this.subtotal * this.couponDiscountVal) / 100
          } else {
            const productSubtotal = this.getDiscountAmountOnCategory(
              this.couponDetail.category
            )
            amount = (productSubtotal * this.couponDiscountVal) / 100
          }
          return amount > this.couponDetail.maximumDiscountAmount
            ? this.couponDetail.maximumDiscountAmount
            : amount
        }
      }

      return 0
    },
  },

  watch: {
    showBillingAddress: {
      handler(nv) {
        if (nv) {
          this.order = {
            ...this.order,
            billingAddress: this.billingAddress,
          }
        } else {
          this.order = Object.keys(this.order)
            .filter((key) => key !== 'billingAddress')
            .reduce((obj, key) => {
              obj[key] = this.order[key]
              return obj
            }, {})
        }
      },
    },
    totalAmount: {
      immediate: true,
      handler(nv) {
        this.order = {
          ...this.order,
          totalAmount: nv,
        }
      },
    },
    discountAmount: {
      immediate: true,
      handler(nv) {
        this.order = {
          ...this.order,
          discountAmount: nv,
        }
      },
    },
  },

  methods: {
    async handleCreateOrder() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.post('/auth/order', this.order)
        if (status === 200) {
          this.$router.push('/shop')
          this.$store.commit('cart/SET_CART_ITEMS', [])
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

    handleValidCoupon(couponDetail) {
      this.couponDetail = couponDetail
    },

    getDiscountAmountOnCategory(category) {
      const orderProduct = this.cartItems.find(
        (ci) => ci.product.categoryId === category.id
      )

      if (!orderProduct) return 0

      return orderProduct.quantity * orderProduct.product.price
    },
  },
}
</script>

<style></style>
