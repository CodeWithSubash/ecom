<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <!-- Cart Items  -->
      <div v-if="cartItems.length">
        <table v-loading="loading" class="w-full border-collapse mb-12">
          <thead class="bg-gray-100">
            <th class="w-10 font-medium">&nbsp;</th>
            <th class="w-40 font-medium">&nbsp;</th>
            <th class="text-left py-4 font-medium">Product</th>
            <th class="text-left py-4 font-medium">Price</th>
            <th class="text-left py-4 font-medium">Quantity</th>
            <th class="text-left py-4 font-medium">Subtotal</th>
          </thead>
          <tbody>
            <tr v-for="cartItem in cartItems" :key="cartItem.id">
              <td
                class="border border-gray-200 text-red-600 p-3 cursor-pointer"
              >
                <i class="bx bx-x bx-sm" @click="removeFromCart(cartItem)"></i>
              </td>
              <td class="border border-gray-200 py-8 hidden md:block">
                <div class="h-24 w-24 mx-auto">
                  <el-image
                    :src="getDisplayImage(cartItem)"
                    lazy
                    fit="cover"
                    class="h-full w-full"
                  >
                    <div slot="placeholder" class="h-full w-full">
                      <el-skeleton-item
                        variant="image"
                        animated
                        style="height: 100%; width: 100%"
                      />
                    </div>
                  </el-image>
                </div>
              </td>
              <td
                data-title="Product"
                class="border border-gray-200 p-4 md:w-1/3"
              >
                <div>
                  <p>{{ cartItem.product.name }}</p>
                  <el-tag
                    :type="
                      cartItem.product.availableStock > 0 ? 'success' : 'danger'
                    "
                    size="mini"
                  >
                    <p class="text-sm">
                      <span>Stock:</span>
                      {{ cartItem.product.availableStock }}
                    </p>
                  </el-tag>
                </div>
              </td>
              <td data-title="Price" class="border border-gray-200 p-4">
                {{ cartItem.product.price }}
              </td>
              <td data-title="Quantity" class="border border-gray-200 p-4">
                <el-input-number
                  :value="cartItem.quantity"
                  :min="1"
                  :max="cartItem.product.availableStock"
                  @change="updateCartItem(cartItem, parseInt($event))"
                ></el-input-number>
              </td>
              <td data-title="Subtotal" class="border border-gray-200 p-4">
                $ {{ cartItem.quantity * cartItem.product.price }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <empty-entity v-else> Your cart is currently empty </empty-entity>
      <!-- Total  -->
      <div v-if="cartItems.length" class="flex flex-col md:items-end">
        <div class="w-full md:w-1/2 mb-6">
          <div class="text-xl font-medium mb-6">Cart Total</div>
          <table class="w-full border-collapse">
            <tbody class="border">
              <tr class="border-b border-gray-200">
                <th class="hidden md:block text-left font-medium p-3">
                  Subtotal
                </th>
                <td data-title="Subtotal" class="p-3">$ {{ cartSubtotal }}</td>
              </tr>
              <tr class="">
                <th class="hidden md:block text-left font-medium p-3">Total</th>
                <td data-title="Total" class="p-3 font-semibold">
                  $ {{ cartSubtotal }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <nuxt-link to="/checkout">
          <base-button inverse small on-hover class="capitalize">
            Proceed to checkout
          </base-button>
        </nuxt-link>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

import EmptyEntity from '~/components/EmptyEntity.vue'
import BreadCrumb from '~/components/ui/BreadCrumb.vue'
import cart from '~/store/cart'

export default {
  name: 'Cart',

  layout: 'retailer',

  components: { EmptyEntity, BreadCrumb },

  scrollToTop: true,

  middleware: 'auth',

  head: {
    title: 'KMPL | Cart',
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Cart',
          path: '/cart',
        },
      ],
    }
  },

  computed: {
    ...mapState({
      cartItems: (state) => [...state.cart.cartItems],
      loading: (state) => state.isLoading,
    }),
    ...mapGetters({
      cartSubtotal: 'cart/cartSubtotal',
    }),
  },

  methods: {
    getDisplayImage(cartItem) {
      return cartItem.product.files && cartItem.product.files[0].fileDownloadUri
    },
    removeFromCart(cartItem) {
      this.$store.dispatch('cart/removeCartItem', cartItem)
    },
    updateCartItem: _.debounce(function (cartItem, quantity) {
      this.$store.dispatch('cart/updateCartItem', { ...cartItem, quantity })
    }, 1000),
  },
}
</script>

<style>
@media only screen and (max-width: 760px),
  (min-device-width: 768px) and (max-device-width: 1024px) {
  /* Force table to not be like tables anymore */
  table,
  thead,
  tbody,
  th,
  td,
  tr {
    display: block;
  }

  tr:nth-child(even) {
    background: rgba(0, 0, 0, 0.025);
  }

  /* Hide table headers */
  thead th {
    display: none;
  }

  td {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  td:before {
    content: attr(data-title);
    font-weight: 500;
    float: left;
  }
}
</style>
