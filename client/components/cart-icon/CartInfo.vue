<template>
  <div class="relative group">
    <div class="flex space-x-6 items-center">
      <nuxt-link to="/cart">
        <cart-count />
      </nuxt-link>
      <cart-subtotal :subtotal="subtotal" />
    </div>
    <cart-item-list-container>
      <cart-item-list :item-list="cartItems" />
      <div v-if="cartItems.length" class="px-6 pb-4">
        <div class="flex justify-between py-2 font-medium">
          <div>Subtotal</div>
          <div>${{ subtotal.toFixed(2) }}</div>
        </div>
        <div class="flex justify-between space-x-4">
          <base-button small on-hover class="w-1/2 text-center">
            <nuxt-link to="/cart">View Cart</nuxt-link>
          </base-button>
          <base-button inverse on-hover small class="w-1/2 text-center">
            <nuxt-link to="/checkout">Checkout</nuxt-link>
          </base-button>
        </div>
      </div>
      <div v-else class="px-6 pb-4">No Cart Item</div>
    </cart-item-list-container>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

import CartCount from '@/components/cart-icon/CartCount.vue'
import CartItemList from '@/components/cart-icon/CartItemList.vue'
import CartSubtotal from './CartSubtotal.vue'
import CartItemListContainer from './CartItemListContainer.vue'

export default {
  name: 'CartStatus',

  components: {
    CartCount,
    CartItemList,
    CartItemListContainer,
  },

  computed: {
    ...mapState({
      cartItems: (state) => state.cart.cartItems,
    }),
    ...mapGetters({
      subtotal: 'cart/cartSubtotal',
    }),
  },

  created() {
    this.$store.dispatch('cart/getRetailerCartItems')
  },
}
</script>
