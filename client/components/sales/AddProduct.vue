<template>
  <div>
    <el-drawer
      title="Add Product"
      :visible.sync="drawer"
      direction="rtl"
      :before-close="handleClose"
      :size="drawerSize"
    >
      <div :disabled="loading" class="px-4 pb-4 space-y-4">
        <el-input
          v-model="params.keyword"
          placeholder="Search for products"
          clearable
          @keyup.enter.native="$fetch"
          @clear="$fetch"
        >
          <template #append>
            <el-button
              size="small"
              icon="el-icon-search"
              @click="$fetch"
            ></el-button>
          </template>
        </el-input>

        <template v-if="!$fetchState.pending">
          <template v-if="products.length">
            <div
              v-for="product in products"
              :key="product.id"
              class="h-44 w-full border border-gray-200 rounded p-2 flex space-x-4"
            >
              <img
                v-if="product.files.length && product.files[0].fileDownloadUri"
                :src="product.files[0].fileDownloadUri"
                :alt="product.name"
                class="h-full w-44 rounded"
              />
              <div class="flex-grow">
                <div>
                  <div class="flex justify-between">
                    <p class="text-base">{{ product.name }}</p>
                    <div class="flex items-center justify-between space-x-2">
                      <el-button
                        v-if="!excludedProducts.includes(product.id)"
                        size="mini"
                        :loading="loading"
                        :disabled="!product.availableStock > 0"
                        class="inline-block"
                        @click="$emit('onProductSelect', product)"
                      >
                        Add
                      </el-button>
                      <div v-else>
                        <i class="bx bx-check-circle bx-sm text-green-500"></i>
                      </div>
                      <el-button
                        size="mini"
                        class="inline-block"
                        @click="$emit('onProductShow', product)"
                      >
                        View
                      </el-button>
                    </div>
                  </div>
                  <p>
                    <span class="text-gray-500">Category:</span>
                    {{ product.category.name }}
                  </p>
                  <p>
                    <span class="text-gray-500">Brand:</span>
                    {{ product.brand.name }}
                  </p>
                  <p>
                    <span class="text-gray-500">MRP:</span> ${{ product.mrp }}
                  </p>
                  <p>
                    <span class="text-gray-500">Price:</span> ${{
                      product.price
                    }}
                  </p>
                  <el-tag
                    :type="product.availableStock > 0 ? 'success' : 'danger'"
                  >
                    <span>Stock: </span>
                    <span>{{ product.availableStock }}</span>
                  </el-tag>
                </div>
              </div>
            </div>
          </template>
          <div v-else>No Products found</div>
        </template>
        <div v-else>Loading ...</div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
export default {
  props: {
    excludedProducts: {
      type: Array,
      default: () => [],
    },
    value: {
      type: Boolean,
      default: false,
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },

  data() {
    return {
      drawer: this.value,
      params: {
        keyword: '',
      },
      products: null,
    }
  },

  watch: {
    value: {
      immediate: true,
      handler(nv) {
        this.drawer = nv
      },
    },
  },

  computed: {
    drawerSize() {
      return window.screen.width > 768 ? '40%' : '100%'
    },
  },

  async fetch() {
    const {
      data: { data, paginationInfo },
    } = await this.$axios.get('/auth/product', {
      params: this.params,
    })

    this.products = data
  },

  methods: {
    handleClose(done) {
      this.drawer = false
      this.$emit('input', false)
    },
  },
}
</script>

<style></style>
