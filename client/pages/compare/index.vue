<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />

    <div class="default-container">
      <template v-if="compareList.length">
        <div class="max-w-full overflow-x-auto mb-12">
          <table class="table-fixed" v-loading="isBusy">
            <tbody>
              <tr>
                <td class=""></td>
                <td
                  v-for="product in compareList"
                  :key="product.id"
                  class="p-4 border-b border-l border-gray-200"
                >
                  <div
                    class="flex flex-col items-center justify-start h-96"
                    style="width: 300px"
                  >
                    <div
                      class="flex items-center mb-4 cursor-pointer"
                      @click="handleRemoveProduct(product)"
                    >
                      <span> Remove </span>
                      <i class="bx bx-x bx-sm text-red-600"></i>
                    </div>
                    <el-image
                      :src="
                        product.files.length && product.files[0].fileDownloadUri
                      "
                      :alt="product.name"
                      fit="cover"
                      class="h-80 w-full"
                    >
                      <div
                        slot="placeholder"
                        class="h-full w-full grid place-content-center"
                      >
                        <img :src="require('~/assets/placeholder.png')" />
                      </div>
                      <div
                        slot="error"
                        class="h-full w-full grid place-content-center"
                      >
                        <img :src="require('~/assets/placeholder.png')" /></div
                    ></el-image>
                    <div class="my-3">
                      {{ product.name }}
                    </div>
                    <div
                      v-if="
                        $auth.loggedIn &&
                        product.availableStock > 0 &&
                        !checkIfInCart(product)
                      "
                      @click="handleAddToCart(product)"
                    >
                      <base-button small> Add to Cart </base-button>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td
                  class="border-b border-t border-r border-gray-200 text-center p-4 sticky left-0 bg-white"
                >
                  Price
                </td>
                <td
                  v-for="product in compareList"
                  :key="product.id"
                  class="p-4 border-b border-l border-gray-200"
                >
                  <div class="text-center">${{ product.price }}</div>
                </td>
              </tr>
              <tr>
                <td
                  class="border-r border-gray-200 text-center p-4 sticky left-0 bg-white"
                >
                  Availability
                </td>
                <td
                  v-for="product in compareList"
                  :key="product.id"
                  class="p-4 border-l border-gray-200"
                >
                  <div
                    class="text-center"
                    :class="
                      product.availableStock > 0
                        ? 'text-green-600'
                        : 'text-red-600'
                    "
                  >
                    {{
                      product.availableStock > 0 ? 'Instock' : 'Out of Stock'
                    }}
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </template>
      <empty-entity v-else>
        No products added to compare.
        <span class="text-red-600">
          <nuxt-link to="/shop">Click here</nuxt-link>
        </span>
        to browse products.
      </empty-entity>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'CompareProducts',

  layout: 'retailer',

  head: {
    title: 'KMPL | Compare Products',
  },

  components: {
    BreadCrumb,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Compare',
          path: '',
        },
      ],
      isBusy: false,
    }
  },

  computed: {
    ...mapState({
      cartItems: (state) => state.cart.cartItems,
      compareList: (state) => state.compare.compareList,
    }),
  },

  methods: {
    handleRemoveProduct(product) {
      this.$store.dispatch('compare/removeFromCompareList', product)
    },

    async handleAddToCart(product) {
      this.isBusy = true
      await this.$store.dispatch('cart/addToRetailerCart', {
        ...product,
        quantity: 1,
      })
      this.isBusy = false
    },

    checkIfInCart(product) {
      return !!this.cartItems.find((ci) => ci.product.id === product.id)
    },
  },
}
</script>

<style scoped>
@media only screen and (max-width: 760px),
  (min-device-width: 768px) and (max-device-width: 1024px) {
  table,
  thead,
  tbody,
  th,
  td,
  tr {
    all: revert;
  }
}
</style>
