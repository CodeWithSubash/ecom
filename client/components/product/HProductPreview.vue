<template>
  <div class="group">
    <nuxt-link
      :to="`/shop/${product.id}`"
      class="flex space-x-4 cursor-pointer"
    >
      <div class="h-20 w-20">
        <el-image :src="displayImage" lazy fit="cover" class="h-full w-full">
          <div slot="placeholder">
            <img :src="require('~/assets/placeholder.png')" />
          </div>
          <div slot="error">
            <img :src="require('~/assets/placeholder.png')" />
          </div>
        </el-image>
      </div>
      <div>
        <div class="font-medium uppercase group-hover:text-red-600">
          {{ product.name }}
        </div>
        <div v-if="$auth.loggedIn && verifiedKyc" class="text-gray-500">
          ${{ product.price }}
        </div>
      </div>
    </nuxt-link>
  </div>
</template>

<script>
import kyc from '~/mixins/kyc'

export default {
  name: 'HorizontalProductCard',

  mixins: [kyc],

  props: {
    product: {
      type: Object,
      required: true,
    },
  },

  computed: {
    displayImage() {
      return this.product.files.length
        ? this.product.files[0].fileDownloadUri
        : ''
    },
  },
}
</script>

<style></style>
