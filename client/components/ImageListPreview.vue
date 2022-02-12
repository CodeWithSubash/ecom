<template>
  <div>
    <div v-if="fileList.length" class="my-4">
      <div class="text-gray-500">Preview Images</div>
      <div class="w-full flex flex-wrap">
        <div
          v-for="(file, index) in fileList"
          :key="`${index}-${file.name}`"
          class="relative h-40 w-44 m-2 group"
        >
          <div class="h-full w-full rounded overflow-hidden">
            <img
              :src="getImageUrl(file)"
              :alt="file.name"
              class="h-full w-full object-center object-contain transform group-hover:scale-110 transition-all duration-200 ease-in"
            />
          </div>
          <div
            class="absolute inset-0 rounded bg-gray-500 bg-opacity-5 group-hover:bg-opacity-50"
          >
            <div class="h-full w-full flex items-center justify-center">
              <div
                class="bg-white px-2 py-0.5 text-xs rounded-sm shadow-md opacity-0 z-auto transform translate-y-5 scale-50 group-hover:opacity-100 group-hover:scale-100 group-hover:translate-y-0 transition-all duration-200 ease-in"
              >
                {{ file.name }}
              </div>
            </div>
          </div>
          <span
            class="absolute -top-2 -right-2 opacity-0 group-hover:opacity-100 transition-all duration-200 ease-in"
          >
            <div
              class="p-0.5 flex items-center justify-center rounded-full bg-white text-red-600 shadow cursor-pointer"
              @click="remove(fileList.indexOf(file))"
            >
              <i class="bx bx-x-circle bx-sm bx-tada"></i>
            </div>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImageListPreview',

  props: {
    fileList: {
      type: Array,
      required: true,
    },
  },

  methods: {
    remove(index) {
      this.$emit('handleRemove', index)
    },

    getImageUrl(file) {
      if (file) {
        const src = URL.createObjectURL(file)
        return src
      }
      return file
    },
  },
}
</script>

<style></style>
