<template>
  <div v-if="product">
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <with-product-aside>
      <div
        class="flex flex-col md:flex-row space-y-6 md:space-y-0 space-x-0 md:space-x-6 pb-16"
      >
        <!-- Product Image  -->
        <div class="w-full md:w-1/2" style="height: 600px">
          <product-image-preview
            v-if="product.files.length"
            :images="product.files"
          />
          <div
            v-else
            class="h-full w-full flex items-center justify-center bg-gray-100"
          >
            <img :src="require('~/assets/placeholder.png')" />
          </div>
        </div>
        <!-- Product Info  -->
        <div class="w-full md:w-1/2 divide-y">
          <div class="pb-4">
            <div class="text-lg text-gray-600">
              <div v-html="product.description"></div>
            </div>
            <div
              v-show="$auth.loggedIn && verifiedKyc"
              class="mt-4 flex items-baseline space-x-2"
            >
              <span
                v-if="product.mrp > product.price"
                class="text-3xl font-thin text-gray-400 line-through"
              >
                ${{ product.mrp }}
              </span>
              <span class="text-4xl font-medium">
                ${{ product.mrp > product.price ? product.price : product.mrp }}
              </span>
            </div>
          </div>
          <template v-if="$auth.loggedIn && verifiedKyc">
            <div
              class="py-5"
              :class="inStock ? 'text-green-500' : 'text-red-600'"
            >
              {{
                inStock > 0
                  ? `${product.availableStock} in stock`
                  : 'Out of Stock'
              }}
            </div>
            <!-- Add to Cart  -->
            <div class="py-5 flex items-center justify-between">
              <div class="flex space-x-2">
                <template v-if="inStock && !alreadyInCart">
                  <icon-box>
                    <input
                      v-model.number="quantity"
                      type="number"
                      :max="product.availableStock"
                      class="w-8"
                      min="1"
                    />
                  </icon-box>
                  <div @click="addToCart">
                    <base-button
                      inverse
                      on-hover
                      small
                      :loading="isBusy"
                      class="capitalize"
                    >
                      Add to cart
                    </base-button>
                  </div>
                </template>
                <el-tooltip
                  effect="dark"
                  :content="
                    alreadyWishlisted ? 'In Wishlist' : 'Add to wishlist'
                  "
                  placement="top"
                >
                  <div @click="toggleWishlist">
                    <icon-box class="h-10">
                      <i
                        v-if="alreadyWishlisted"
                        class="bx bxs-heart text-xl text-red-600"
                      ></i>
                      <i v-else class="bx bx-heart text-xl"></i>
                    </icon-box>
                  </div>
                </el-tooltip>
                <el-tooltip
                  effect="dark"
                  content="Compare product"
                  placement="top"
                >
                  <div>
                    <icon-box class="h-10">
                      <i class="bx bx-refresh text-xl"></i>
                    </icon-box></div
                ></el-tooltip>
              </div>
              <div v-if="alreadyInCart">Added to Cart</div>
            </div>
          </template>
          <div class="py-5">
            <div>
              <span>Category:</span>
              <span class="text-gray-400"> {{ product.category.name }} </span>
            </div>
          </div>
          <div class="py-5">
            <div>
              <span>Brand:</span>
              <span class="text-gray-400"> {{ product.brand.name }} </span>
            </div>
          </div>
          <div class="py-5">
            <div>
              <span>Condition:</span>
              <span class="text-gray-400">
                {{ product.productCondition }}
              </span>
            </div>
          </div>
          <div class="py-5">
            <div>
              <span>Tags:</span>
              <span
                v-for="(tag, index) in product.tags"
                :key="`${tag}-${index}`"
                class="text-gray-400"
              >
                {{ tag }},
              </span>
            </div>
          </div>
        </div>
      </div>
      <!-- Product Descriptions  -->
      <el-tabs v-model="activeTab" class="mb-12">
        <el-tab-pane name="description">
          <span slot="label">
            <div class="text-black font-medium">Description</div>
          </span>
          <div class="text-lg text-gray-600">
            <div v-html="product.description"></div>
          </div>
        </el-tab-pane>
        <el-tab-pane v-if="product.paymentAndReturn" name="payment">
          <span slot="label">
            <div class="text-black font-medium">Payment & Return</div>
          </span>
          <div class="text-lg text-gray-600">
            <div v-html="product.paymentAndReturn"></div>
          </div>
        </el-tab-pane>
        <el-tab-pane v-if="product.shippingAndDelivery" name="shipping">
          <span slot="label">
            <div class="text-black font-medium">Shipping & Delievery</div>
          </span>
          <div class="text-lg text-gray-600">
            <div v-html="product.shippingAndDelivery"></div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <!-- Related Products  -->
      <div v-if="relatedProducts.length">
        <div class="text-2xl font-medium mb-8">Related Product</div>
        <div class="grid md:grid-cols-3 gap-8 md:gap-5">
          <v-product-preview
            v-for="product in relatedProducts"
            :key="product.name"
            :product="product"
          />
        </div>
      </div>
    </with-product-aside>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import kyc from '~/mixins/kyc'

import BreadCrumb from '~/components/ui/BreadCrumb.vue'
import VProductPreview from '@/components/product/VProductPreview.vue'

export default {
  name: 'ProductShow',

  layout: 'retailer',

  scrollToTop: true,

  head() {
    return {
      title: `KMPL | ${this.product && this.product.name}`,
      meta: [
        {
          hid: 'description',
          name: 'description',
          content: this.product.description,
        },
      ],
    }
  },

  mixins: [kyc],

  components: {
    BreadCrumb,
    VProductPreview,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Shop',
          path: '/shop',
        },
      ],
      quantity: 1,

      isBusy: false,
      activeTab: 'description',
    }
  },

  computed: {
    ...mapState({
      cartItems: (state) => state.cart.cartItems,
      wishlist: (state) => state.wishlist.wishlistItems,
    }),

    alreadyInCart() {
      return !!this.cartItems.find((ci) => ci.product.id === this.product.id)
    },

    alreadyWishlisted() {
      return !!this.wishlist.find((wi) => wi.product.id === this.product.id)
    },

    inStock() {
      return this.product.availableStock > 0
    },

    relatedProducts() {
      return this.$store.getters['product/relatedProducts']
    },
  },

  watch: {
    product: {
      immediate: true,
      handler(nv) {
        this.breadcrumbs[1] = {
          name: nv.name,
          path: '',
        }
      },
    },
  },

  async asyncData({ params, $axios }) {
    const { data } = await $axios.get(`/auth/product/${params.id}`)

    return { product: data }
  },

  methods: {
    async addToCart() {
      this.isBusy = true
      await this.$store.dispatch('cart/addToRetailerCart', {
        ...this.product,
        quantity: this.quantity,
      })
      this.isBusy = false
      this.quantity = 1
      this.$nuxt.refresh()
    },

    toggleWishlist() {
      console.log(this.alreadyWishlisted)
      if (!this.alreadyWishlisted)
        this.$store.dispatch('wishlist/addToWishlist', this.product.id)
      else this.$store.dispatch('wishlist/removeFromWishlist', this.product.id)
    },
  },
}
</script>

<style>
.el-tabs__active-bar {
  background: black;
}
</style>
