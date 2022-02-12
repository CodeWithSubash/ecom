<template>
  <div
    class="border-2 border-gray-300 border-dashed rounded overflow-hidden"
    @click="chooseImage"
  >
    <div
      v-if="!imageData"
      class="h-full w-full flex items-center justify-center"
    >
      <i class="bx bx-plus bx-md text-gray-400"></i>
    </div>
    <img
      v-else
      :src="imageData"
      alt="Upload Image"
      class="h-full w-full object-center object-cover rounded"
    />
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      class="hidden"
      @input="onSelectFile"
    />
  </div>
</template>

<script>
export default {
  name: 'ImageUploader',

  props: {
    imageUrl: {
      type: String,
      default: '',
    },
  },

  data() {
    return {
      imageData: this.imageUrl,
    }
  },

  watch: {
    imageUrl: {
      immediate: true,
      handler(nv) {
        this.imageData = nv
      },
    },
  },

  methods: {
    chooseImage() {
      this.$refs.fileInput.click()
    },

    onSelectFile() {
      const input = this.$refs.fileInput
      const files = input.files
      if (files && files[0]) {
        const reader = new FileReader()
        reader.onload = (e) => {
          this.imageData = e.target.result
        }
        reader.readAsDataURL(files[0])
        this.$emit('onSelect', files[0])
      }
    },
  },
}
</script>

<style></style>
