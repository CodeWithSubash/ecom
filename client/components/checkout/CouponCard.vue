<template>
  <div class="w-full p-4 rounded-sm bg-gray-100">
    <div class="font-medium text-lg">
      <span class="">COUPON CODE: </span>
      <span class="text-gray-500">
        {{ coupon.couponCode }}
      </span>
    </div>
    <div class="mb-2">
      <span>
        Redeemable for:
        <span class="font-medium text-red-600">{{ discountValue }}</span>
      </span>
    </div>
    <div class="border-2 border-white p-2 rounded-sm">
      <div class="underline text-sm">Applicable For</div>
      <div>
        <span class="text-gray-500 text-sm">Min Order Value: </span> TT$
        {{ coupon.minimumOrderValue }}
      </div>
      <div>
        <span class="text-gray-500 text-sm">Max Discount Amount: </span> TT$
        {{ coupon.maximumDiscountAmount }}
      </div>
      <div v-if="coupon.category">
        <span class="text-gray-500 text-sm">On category: </span>
        {{ coupon.category.name }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CouponCard',

  props: {
    coupon: {
      type: Object,
      required: true,
    },
  },

  computed: {
    discountValue() {
      if (this.coupon.couponType === 'FREE_SHIPPING') return 'Free Shipping'
      if (this.coupon.couponType === 'CURRENCY')
        return `TT$ ${this.coupon.discountValue} off`
      if (this.coupon.couponType === 'PERCENTAGE')
        return `${this.coupon.discountValue}% off`
    },
  },
}
</script>

<style></style>
