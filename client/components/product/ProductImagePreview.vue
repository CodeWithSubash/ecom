<template>
  <div class="h-full w-full flex flex-col space-y-2">
    <el-image
      :src="activePreview"
      :preview-src-list="previewList"
      fit="contain"
      class="h-5/6 w-full bg-gray-100"
    >
    </el-image>
    <div class="flex flex-grow space-x-2 overflow-x-auto">
      <img
        v-for="(img, index) in previewList"
        :key="img"
        :src="img"
        alt="Preview Image"
        class="h-full w-20 object-center object-contain cursor-pointer"
        :class="activePreview === img && 'border-2 border-red-600 p-.5'"
        @click="activePreview = img"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductImagePreview',

  props: {
    images: {
      type: Array,
      required: true,
    },
  },

  data() {
    return {
      activePreview: '',
    }
  },

  computed: {
    previewList() {
      return this.images.map((img) => img.fileDownloadUri)
    },
  },

  watch: {
    previewList: {
      deep: true,
      immediate: true,
      handler(nv) {
        if (nv) this.activePreview = nv[0]
      },
    },
  },
}
</script>

<style></style>
