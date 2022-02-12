<template>
  <div>
    <div class="mb-2 group border-l">
      <div
        class="relative w-full mb-1 box-border"
        :style="{
          height: lg ? '380px' : '288px',
        }"
      >
        <!-- Product Image Previews  -->
        <el-image
          :ref="`product${product.id}`"
          :preview-src-list="productImages"
          style="display: none"
        >
        </el-image>
        <nuxt-link :to="`/shop/${product.id}`">
          <el-image :src="displayImage" lazy fit="cover" class="h-full w-full">
            <div
              slot="placeholder"
              class="h-full w-full grid place-content-center"
            >
              <img :src="require('~/assets/placeholder.png')" />
            </div>
            <div slot="error" class="h-full w-full grid place-content-center">
              <img :src="require('~/assets/placeholder.png')" />
            </div>
          </el-image>
        </nuxt-link>
        <!-- Sales tag  -->
        <img
          v-if="product.deal"
          :src="require(`~/assets/sale-tag.png`)"
          class="absolute -top-2 -left-2 h-10 w-10"
        />
        <!-- Card actions  -->
        <div
          class="absolute top-1 right-1 flex flex-col space-y-1 transform lg:translate-x-8 lg:group-hover:translate-x-0 lg:opacity-0 group-hover:opacity-100 transition duration-200 ease-in"
        >
          <el-tooltip
            v-if="$auth.loggedIn"
            effect="dark"
            :content="alreadyWishlisted ? 'In Wishlist' : 'Add to wishlist'"
            placement="left"
          >
            <div
              class="h-12 w-12 bg-white rounded-sm grid place-content-center cursor-pointer hover:text-red-600 transition duration-200 ease-in"
              @click="toggleWishlist"
            >
              <i
                v-if="alreadyWishlisted"
                class="bx bxs-heart text-xl text-red-600"
              ></i>
              <i v-else class="bx bx-heart text-xl"></i>
            </div>
          </el-tooltip>

          <el-tooltip
            effect="dark"
            :content="
              alreadyInCompareList ? 'Compare products' : 'Add to compare'
            "
            placement="left"
          >
            <div
              class="h-12 w-12 bg-white rounded-sm grid place-content-center cursor-pointer hover:text-red-600 transition duration-200 ease-in"
            >
              <div v-if="!alreadyInCompareList" @click="handleAddToCompare">
                <i class="bx bx-refresh text-xl"></i>
              </div>
              <nuxt-link v-else to="/compare">
                <div class="text-green-600">
                  <i class="bx bx-check text-xl"></i>
                </div>
              </nuxt-link>
            </div>
          </el-tooltip>
          <el-tooltip
            effect="dark"
            content="Click image to zoom"
            placement="left"
          >
            <div
              class="h-12 w-12 bg-white rounded-sm grid place-content-center cursor-pointer hover:text-red-600 transition duration-200 ease-in"
              @click="handleZoom"
            >
              <i class="bx bx-zoom-in text-xl"></i>
            </div>
          </el-tooltip>
        </div>
        <div
          class="absolute bottom-2 right-2 bg-white text-xs px-1.5 py-0.5 rounded-sm shadow transform transition-all duration-200 ease-in"
          :class="
            alreadyInCart
              ? 'opacity-100 translate-y-0'
              : 'opacity-0 translate-y-4'
          "
        >
          <div class="flex items-center space-x-1">
            <span class="text-green-500">
              <i class="bx bx-check-double bx-xs"></i>
            </span>
            <span>Added to Cart</span>
          </div>
        </div>
      </div>
      <nuxt-link
        :to="`/shop/${product.name}`"
        class="pt-1.5 px-2 group-hover:text-red-600 transition duration-300 ease-in cursor cursor-pointer"
      >
        {{ product.name }}
      </nuxt-link>
      <div v-if="$auth.loggedIn && verifiedKyc" class="relative px-2">
        <div
          class="font-medium group-hover:opacity-0 transition duration-300 ease-in"
        >
          <span
            v-if="product.mrp > product.price"
            class="line-through text-gray-400 font-thin"
            >${{ product.mrp }}
          </span>
          <span>
            ${{ product.mrp > product.price ? product.price : product.mrp }}
          </span>
        </div>
        <div
          class="lg:absolute top-0 left-0 font-medium transform lg:-translate-x-6 lg:opacity-0 group-hover:translate-x-0 group-hover:opacity-100 transition duration-200 ease-in lg:px-2"
        >
          <div class="hover:text-red-600 transition duration-100 ease-in">
            <div
              v-if="!alreadyInCart && !outOfStock"
              class="flex items-center cursor-pointer"
              :disabled="isBusy"
              @click="addToCart"
            >
              <span class="mr-2">Add to cart</span>
              <i v-if="isBusy" class="bx bx-loader bx-spin"></i>
            </div>
            <div v-else-if="outOfStock" class="text-red-600">Out Of Stock</div>
            <div v-else>
              <nuxt-link to="/cart">View Cart</nuxt-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import kyc from '~/mixins/kyc'
import { mapState } from 'vuex'

export default {
  name: 'VerticalProductCard',

  mixins: [kyc],

  props: {
    product: {
      type: Object,
      required: true,
    },
    lg: {
      type: Boolean,
      default: false,
    },
  },

  data() {
    return {
      isBusy: false,
    }
  },

  computed: {
    ...mapState({
      cartItems: (state) => state.cart.cartItems,
      wishlist: (state) => state.wishlist.wishlistItems,
      compareList: (state) => state.compare.compareList,
    }),

    alreadyInCart() {
      return !!this.cartItems.find((ci) => ci.product.id === this.product.id)
    },

    alreadyWishlisted() {
      return !!this.wishlist.find((wi) => wi.product.id === this.product.id)
    },

    alreadyInCompareList() {
      return !!this.compareList.find((cp) => cp.id === this.product.id)
    },

    outOfStock() {
      return !this.product.availableStock > 0
    },

    displayImage() {
      return this.product.files.length
        ? this.product.files[0].fileDownloadUri
        : ''
    },

    productImages() {
      return this.product.files.length
        ? this.product.files.map((img) => img.fileDownloadUri)
        : []
    },
  },

  methods: {
    async addToCart() {
      this.isBusy = true
      await this.$store.dispatch('cart/addToRetailerCart', {
        ...this.product,
        quantity: 1,
      })
      this.isBusy = false
    },

    handleZoom() {
      const clickedProduct = `product${this.product.id}`
      this.$refs[clickedProduct].clickHandler()
    },

    toggleWishlist() {
      console.log(this.alreadyWishlisted)
      if (!this.alreadyWishlisted)
        this.$store.dispatch('wishlist/addToWishlist', this.product.id)
      else this.$store.dispatch('wishlist/removeFromWishlist', this.product.id)
    },

    handleAddToCompare() {
      this.$store.dispatch('compare/addToCompareList', this.product)
    },
  },
}
</script>

<style></style>
