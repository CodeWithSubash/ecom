<template>
  <div class="flex items-center justify-between py-4">
    <div class="bg-gray-200 h-20 w-20">
      <el-image :src="displayImage" lazy fit="cover" class="h-full w-full">
        <div slot="placeholder" class="h-full w-full">
          <el-skeleton-item
            variant="image"
            animated
            style="height: 100%; width: 100%"
          />
        </div>
      </el-image>
    </div>
    <div class="text-center">
      <div class="font-medium text-base">{{ cartItem.product.name }}</div>
      <div class="text-gray-400">
        <span> {{ cartItem.quantity }} </span>
        <i class="bx bx-x bx-xs"></i>
        <span>${{ cartItem.product.price }} </span>
      </div>
    </div>
    <i
      class="bx bx-x bx-sm text-red-600 cursor-pointer"
      @click="removeFromCart"
    ></i>
  </div>
</template>

<script>
export default {
  name: 'CartItem',

  props: {
    cartItem: {
      type: Object,
      required: true,
    },
  },

  computed: {
    displayImage() {
      return (
        this.cartItem.product.files &&
        this.cartItem.product.files[0].fileDownloadUri
      )
    },
  },

  methods: {
    removeFromCart(product) {
      this.$store.dispatch('cart/removeCartItem', this.cartItem)
    },
  },
}
</script>
