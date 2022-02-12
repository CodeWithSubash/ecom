<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <with-product-aside>
      <!-- Product Sorting  -->
      <div
        class="border border-gray-200 flex flex-col lg:flex-row lg:justify-between lg:items-center mb-14 p-4 space-y-2 lg:space-y-0"
      >
        <div class="flex flex-col divide-y">
          <p
            v-if="totalProductCount"
            class="text-base font-medium text-gray-400"
          >
            Result shown for
            <span class="text-red-600">{{ totalProductCount }}</span>
            <span>product<span v-if="totalProductCount > 1">s</span></span>
          </p>
          <nuxt-link v-if="keyword || categoryId" to="/shop">
            <div class="text-red-600">Clear Filters</div>
          </nuxt-link>
        </div>
        <div class="w-full lg:w-1/3 flex items-center space-x-2">
          <div>Sort by</div>
          <select
            v-model="params.orderBy"
            class="p-2 flex-grow rounded-sm bg-gray-100 focus:outline-none focus:ring focus:border-red-300"
          >
            <option
              v-for="type in sortingTypes"
              :key="type.name"
              :value="type.value"
              class="px-2"
            >
              {{ type.name }}
            </option>
          </select>
        </div>
      </div>

      <!-- Product List  -->
      <template v-if="!$fetchState.pending">
        <div v-if="products.length" class="grid md:grid-cols-3 gap-8 md:gap-5">
          <v-product-preview
            v-for="product in products"
            :key="product.name"
            :product="product"
          />
        </div>
        <empty-entity v-else>No Products found</empty-entity>
      </template>
      <template v-else>
        <div class="grid md:grid-cols-3 gap-8 md:gap-5">
          <skeleton-product-card v-for="n in 5" :key="n" />
        </div>
      </template>
    </with-product-aside>
  </div>
</template>

<script>
import VProductPreview from '@/components/product/VProductPreview.vue'
import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'Shop',

  layout: 'retailer',

  head: {
    title: 'KMPL | Shop',
  },

  components: {
    VProductPreview,
    BreadCrumb,
  },

  scrollToTop: true,

  data() {
    return {
      breadcrumbs: [{ name: 'Shop', path: '/shop' }],

      sortingTypes: [
        { name: 'Default', value: 'id' },
        { name: 'Popularity', value: 'POPULARITY' },
        { name: 'Latest', value: 'LATEST' },
        { name: 'Price low to high', value: 'PRICE_LOW_TO_HIGH' },
        { name: 'Price high to low', value: 'PRICE_HIGH_TO_LOW' },
      ],

      products: [],

      params: {
        orderBy: 'id',
      },

      paginationInfo: null,
    }
  },

  computed: {
    keyword() {
      return this.$route.query.search
    },
    categoryId() {
      return this.$route.query.categoryId
    },
    totalProductCount() {
      return this.paginationInfo && this.paginationInfo.totalItems
    },
  },

  watch: {
    keyword: {
      immediate: true,
      handler(nv) {
        this.params = { ...this.params, keyword: nv }
      },
    },
    categoryId: {
      immediate: true,
      handler(nv) {
        this.params = { ...this.params, categoryId: nv }
      },
    },
    params: {
      deep: true,
      handler() {
        this.$fetch()
      },
    },
  },

  async fetch() {
    this.$nuxt.$loading.start()
    const {
      data: { data, paginationInfo },
    } = await this.$axios.get('/auth/product/getByCategory', {
      params: this.params,
    })
    this.$nuxt.$loading.finish()

    this.products = data
    this.$store.dispatch('product/setProducts', this.products)

    this.paginationInfo = paginationInfo
  },
}
</script>

<style></style>
