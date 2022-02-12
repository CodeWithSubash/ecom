<template>
  <div class="h-full w-full">
    <div
      class="h-full w-full p-12 bg-gray-50 border-2 border-gray-300 border-dashed rounded hover:border-blue-300"
      @dragover="dragover"
      @dragleave="dragleave"
      @drop="drop"
    >
      <input
        ref="file"
        type="file"
        id="assetsFieldHandle"
        multiple
        accept="image/*"
        class="w-px h-px hidden overflow-hidden absolute"
        @change="onChange"
      />

      <label for="assetsFieldHandle" class="block cursor-pointer">
        <div class="flex flex-col items-center justify-center">
          <i class="bx bx-cloud-upload bx-lg text-gray-400"></i>
          <div class="text-center">
            Drop file here or
            <span class="text-blue-500">click here</span> to upload files
          </div>
        </div>
      </label>
    </div>

    <image-list-preview
      v-if="fileList.length"
      :fileList="fileList"
      @handleRemove="remove"
    />
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Array,
      required: true,
    },
  },

  data() {
    return {
      fileList: this.value,
    }
  },

  watch: {
    value: {
      handler(nv) {
        this.fileList = [...nv]
      },
    },
  },

  methods: {
    onChange() {
      this.fileList = [...this.$refs.file.files, ...this.fileList]
      this.$emit('input', this.fileList)
    },
    remove(i) {
      this.fileList.splice(i, 1)
    },
    dragover(event) {
      event.preventDefault()
      // Add some visual fluff to show the user can drop its files
      if (!event.currentTarget.classList.contains('bg-green-50')) {
        event.currentTarget.classList.remove('bg-gray-100')
        event.currentTarget.classList.add('bg-green-50')
      }
    },
    dragleave(event) {
      // Clean up
      event.currentTarget.classList.add('bg-gray-100')
      event.currentTarget.classList.remove('bg-green-50')
    },
    drop(event) {
      event.preventDefault()
      this.$refs.file.files = event.dataTransfer.files
      this.onChange() // Trigger the onChange event manually
      // Clean up
      event.currentTarget.classList.add('bg-gray-100')
      event.currentTarget.classList.remove('bg-green-50')
    },
  },
}
</script>

<style></style>
