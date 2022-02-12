<template>
  <div class="w-full">
    <div v-if="!showCoupon">
      <p class="flex flex-col lg:flex-row">
        <span class="mr-2 flex items-center">
          <i class="bx bx-info-circle mr-2"></i>
          Have a coupon?
        </span>
        <span class="text-red-600 cursor-pointer" @click="showCoupon = true">
          Click here to enter your code
        </span>
      </p>
    </div>
    <template v-else>
      <div class="flex flex-col lg:flex-row lg:justify-between">
        <div class="border rounded px-6 py-8 w-full">
          <p class="mb-4">If you have a coupon code, please apply it below.</p>
          <div class="flex flex-col lg:flex-row lg:items-baseline lg:space-x-2">
            <base-input v-model.trim="couponCode" placeholder="Coupon Code" />
            <div @click="handleApplyCoupon">
              <base-button
                inverse
                on-hover
                small
                :loading="loading"
                :disabled="!couponCode.length"
                class="capitalize text-center"
              >
                Apply Coupon
              </base-button>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  name: 'CheckoutCoupon',

  data() {
    return {
      couponCode: '',
      isBusy: false,
      showCoupon: false,

      couponDetail: null,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  watch: {
    couponDetail: {
      handler(nv) {
        this.$emit('validCoupon', nv)
      },
    },
  },

  methods: {
    async handleApplyCoupon() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { data } = await this.$axios.get(
          `/auth/coupon/getByCouponCode/${this.couponCode}`
        )
        this.couponDetail = data
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
        this.couponCode = ''
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },
  },
}
</script>

<style></style>
