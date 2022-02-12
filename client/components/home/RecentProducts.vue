<template>
  <div class="default-container my-20">
    <section-header data-aos="fade-up" data-aos-anchor-placement="top-center">
      <template #title>Featured Products</template>
      <template #subtitle>Get construction equipement you neeed</template>
    </section-header>
    <div
      v-if="recentProducts.length"
      class="grid md:grid-cols-4 gap-8 md:gap-5"
    >
      <v-product-preview
        v-for="(product, index) in recentProducts"
        :key="product.name"
        :product="product"
        lg
        data-aos="fade-up"
        :data-aos-delay="(index + 1) * 100"
      />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

import VProductPreview from '../product/VProductPreview.vue'
import SectionHeader from './SectionHeader.vue'

export default {
  name: 'RecentProducts',

  components: { VProductPreview, SectionHeader },

  computed: {
    ...mapGetters({
      recentProducts: 'product/recentProducts',
    }),
  },

  async fetch() {
    const {
      data: { data, paginationInfo },
    } = await this.$axios.get('/auth/product')

    this.$store.dispatch('product/setProducts', data)
  },
}
</script>

<style></style>
