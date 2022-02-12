<template>
  <div class="px-6 py-2 max-h-96 w-96 overflow-y-auto divide-y">
    <cart-item v-for="item in itemList" :key="item.id" :cart-item="item" />
  </div>
</template>

<script>
import { mapState } from 'vuex'

import CartItem from '@/components/cart-icon/CartItem.vue'

export default {
  name: 'CartItemList',

  components: { CartItem },

  props: {
    itemList: {
      type: Array,
      required: true,
    },
  },
}
</script>
