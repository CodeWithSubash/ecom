<template>
  <div>
    <list-container>
      <template #heading>Products</template>
      <h-product-preview
        v-for="product in products"
        :key="product.name"
        :product="product"
        class="py-5 last:pb-0"
      />
    </list-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

import HProductPreview from '~/components/product/HProductPreview.vue'

export default {
  name: 'RecentProductList',

  components: { HProductPreview },

  computed: {
    products() {
      return this.$store.getters['product/productList']
    },
  },
}
</script>

<style></style>
