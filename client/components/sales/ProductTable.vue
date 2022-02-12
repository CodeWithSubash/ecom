<template>
  <div>
    <div class="flex items-center justify-between mb-4">
      <div class="text-sm underline">Products</div>
      <el-button size="small" @click="productsDrawer = true">
        Add Product
      </el-button>
    </div>

    <el-table
      :data="productList"
      size="medium"
      show-summary
      :summary-method="getSummaries"
    >
      <el-table-column type="index" width="30"> </el-table-column>
      <el-table-column label="Logo" width="100">
        <template #default="{ row }">
          <el-avatar
            shape="square"
            :src="
              row.files.length
                ? row.files[0].fileDownloadUri
                : 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
            "
          ></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="Name"></el-table-column>
      <el-table-column prop="availableStock" label="Stock"> </el-table-column>
      <el-table-column prop="price" label="Price"></el-table-column>
      <el-table-column label="Quantity">
        <template #default="{ row }">
          <el-input-number
            v-model="row.quantity"
            :min="1"
            :max="row.availableStock"
            size="mini"
            @change="handleCartUpdate(row)"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column label="Subtotal">
        <template #default="{ row }">
          {{ row.price * row.quantity }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="Actions" fixed="right" width="120">
        <template #default="{ row }">
          <div class="flex space-x-2">
            <el-button
              type="text"
              size="mini"
              @click="handleViewedProduct(row)"
            >
              View
            </el-button>
            <el-button
              type="text"
              size="mini"
              @click="handleRemoveOrderProduct(row)"
            >
              Remove
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <add-product
      v-model="productsDrawer"
      :loading="isBusy"
      :excludedProducts="productIDs"
      @onProductSelect="handleAddOrderProduct"
      @onProductShow="handleViewedProduct"
    />

    <product-detail-dialog
      :product="viewedProduct"
      @dialogClosed="handleViewedProduct"
    />
  </div>
</template>

<script>
import AddProduct from './AddProduct.vue'
import ProductDetailDialog from './ProductDetailDialog.vue'

export default {
  name: 'ProductTable',

  components: { ProductDetailDialog, AddProduct },

  props: {
    addToCart: {
      type: Function,
      default: null,
    },
    removeFromCart: {
      type: Function,
      default: null,
    },
    products: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    return {
      isBusy: false,

      productsDrawer: false,

      productList: [],

      viewedProduct: null,
    }
  },

  computed: {
    productIDs() {
      return this.productList.map((op) => op.id)
    },

    productSubtotal() {
      return this.productList.reduce(
        (acc, item) => (acc += item.quantity * item.price),
        0
      )
    },
  },

  watch: {
    products: {
      immediate: true,
      handler(nv) {
        this.productList = nv
      },
    },
    productList: {
      handler(nv) {
        this.$emit('onProductChange', nv)
      },
    },
  },

  methods: {
    async handleAddOrderProduct(product) {
      const productWithQuantity = {
        ...product,
        // image: product.files && product.files[0].fileDownloadUri,
        quantity: 1,
      }
      if (this.addToCart) {
        this.isBusy = true
        await this.addToCart(productWithQuantity)
        this.isBusy = false
      }
      this.productList.push(productWithQuantity)
    },

    handleRemoveOrderProduct({ id }) {
      this.productList = this.productList.filter((op) => op.id != id)
    },

    handleViewedProduct(product) {
      this.viewedProduct = product
    },

    getSummaries(param) {
      const sums = []
      sums[1] = 'Total'
      sums[5] = this.productSubtotal

      return sums
    },

    handleCartUpdate: _.debounce(function (cartItem) {
      this.addToCart(cartItem)
    }, 1000),
  },
}
</script>

<style></style>
