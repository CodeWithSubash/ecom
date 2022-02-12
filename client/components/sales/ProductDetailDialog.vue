<template>
  <div>
    <el-dialog
      :title="product && product.name"
      :visible.sync="dialogVisible"
      width="80%"
      :fullscreen="fullScreen"
      center
    >
      <el-row v-if="product" :gutter="20">
        <el-col
          v-if="product.files && product.files.length"
          :span="24"
          class="mb-6"
        >
          <div class="flex flex-wrap">
            <div
              v-for="file in product.files"
              :key="file.fileName"
              class="h-40 w-1/6 m-2 rounded overflow-hidden"
            >
              <img
                :src="file.fileDownloadUri"
                :alt="file.fileName"
                class="h-full w-full object-center object-cover"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="mb-4">
            <div class="text-xs text-gray-500 underline">Brand</div>
            <div>{{ product.brand.name }}</div>
          </div>
          <div class="mb-6">
            <div class="text-xs text-gray-500 underline">Category</div>
            <div>{{ product.category.name }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="mb-4">
            <div class="text-xs text-gray-500 underline">MRP</div>
            <div>{{ product.mrp }}</div>
          </div>
          <div class="mb-6">
            <div class="text-xs text-gray-500 underline">Price</div>
            <div>{{ product.price }}</div>
          </div>
        </el-col>
        <el-col
          v-if="product.tags && product.tags.length"
          :span="12"
          class="mb-6"
        >
          <div class="text-xs text-gray-500 underline">Tags</div>
          <div class="flex flex-wrap">
            <el-tag
              v-for="tag in product.tags"
              :key="tag"
              size="mini"
              type="info"
              class="m-1"
            >
              {{ tag }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="24" class="mb-6">
          <div class="text-xs text-gray-500 underline">Description</div>
          <div v-html="product.description"></div>
        </el-col>
        <el-col :span="12">
          <div class="text-xs text-gray-500 underline">
            Shipping & Delievery Detail
          </div>
          <div v-html="product.shippingAndDelivery"></div>
        </el-col>
        <el-col :span="12">
          <div class="text-xs text-gray-500 underline">
            Payment & Return Detail
          </div>
          <div v-html="product.paymentAndReturn"></div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ProductDetailDialog',

  props: ['product'],

  data() {
    return {
      dialogVisible: false,
    }
  },

  computed: {
    fullScreen() {
      return window.screen.width <= 768
    },
  },

  watch: {
    product: {
      immediate: true,
      handler(nv) {
        if (nv) this.dialogVisible = true
      },
    },
    dialogVisible: {
      handler(nv) {
        if (!nv) this.$emit('dialogClosed', null)
      },
    },
  },
}
</script>

<style></style>
