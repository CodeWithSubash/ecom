<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <template v-if="wishlist.length">
        <table class="w-full border-collapse mb-2">
          <thead class="bg-gray-100">
            <th class="w-10 font-medium">&nbsp;</th>
            <th class="w-40 font-medium">&nbsp;</th>
            <th class="text-left py-4 font-medium">Product</th>
            <th class="text-left py-4 font-medium">Price</th>
            <th class="text-left py-4 font-medium">Stock Status</th>
            <th class="py-4 font-medium">&nbsp;</th>
          </thead>
          <tbody v-loading="loading">
            <tr
              v-for="item in wishlist"
              :key="wishlist.id"
              :class="
                !item.product.availableStock && 'bg-gray-50 text-gray-400'
              "
            >
              <td
                class="border border-gray-200 text-red-600 p-3 cursor-pointer"
              >
                <i
                  class="bx bx-x bx-sm"
                  @click="handleRemoveFromWishlist(item)"
                ></i>
              </td>
              <td class="border border-gray-200 py-8 hidden md:block">
                <div class="h-24 w-24 mx-auto">
                  <el-image
                    :src="getDisplayImage(item.product)"
                    lazy
                    fit="cover"
                    class="h-full w-full"
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
                      <img :src="require('~/assets/placeholder.png')" />
                    </div>
                  </el-image>
                </div>
              </td>
              <td
                data-title="Product"
                class="border border-gray-200 p-4 md:w-1/3"
              >
                {{ item.product.name }}
              </td>
              <td data-title="Price" class="border border-gray-200 p-4">
                {{ item.product.price }}
              </td>
              <td data-title="Stock Status" class="border border-gray-200 p-4">
                <el-tag v-if="!item.product.availableStock" type="danger">
                  No Stock
                </el-tag>
                <el-tag v-else type="success">
                  In Stock: {{ item.product.availableStock }}
                </el-tag>
              </td>
              <td class="border border-gray-200 p-4 text-center md:w-1/4">
                <div>
                  <div class="text-sm text-gray-400 mb-2">
                    Added on
                    {{
                      `${getDate(new Date(item.createdAt)).month} ${
                        getDate(new Date(item.createdAt)).day
                      }`
                    }}
                  </div>
                  <div
                    v-if="!checkInCart(item.product.id)"
                    @click="handleAddToCart(item)"
                  >
                    <base-button small :disabled="!item.product.availableStock">
                      Add To Cart
                    </base-button>
                  </div>
                  <div v-else>Already in cart</div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </template>
      <empty-entity v-else> Your wishlist is currently empty </empty-entity>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import splitDate from '~/mixins/splitDate'

import BreadCrumb from '~/components/ui/BreadCrumb.vue'
import EmptyEntity from '~/components/EmptyEntity.vue'

export default {
  name: 'WishList',

  layout: 'retailer',

  components: { BreadCrumb, EmptyEntity },

  scrollToTop: true,

  middleware: 'auth',

  head: {
    title: 'KMPL | Wishlist',
  },

  mixins: [splitDate],

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Wishlist',
          path: '/cart',
        },
      ],
    }
  },

  computed: {
    ...mapState({
      loading: (state) => state.isLoading,
      wishlist: (state) => state.wishlist.wishlistItems,
      cartItems: (state) => state.cart.cartItems,
    }),
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/wishlist/getByUser')
    this.$store.commit('wishlist/SET_WISHLIST', data)
  },

  methods: {
    getDisplayImage(product) {
      return product.files.length && product.files[0].fileDownloadUri
    },

    checkInCart(productId) {
      return !!this.cartItems.find((ci) => ci.product.id == productId)
    },

    handleRemoveFromWishlist(item) {
      this.$store.dispatch('wishlist/removeFromWishlist', item.product.id)
    },

    async handleAddToCart(item) {
      const success = await this.$store.dispatch('cart/addToRetailerCart', {
        ...item.product,
        quantity: 1,
      })
      if (success) this.$fetch()
    },
  },
}
</script>

<style></style>
