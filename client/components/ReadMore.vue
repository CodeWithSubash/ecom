<template>
  <nuxt-link
    :to="path"
    class="
      group
      flex
      items-center
      hover:text-red-600
      transition
      duration-200
      ease-in
    "
    :class="[!inverse ? 'text-gray-500' : 'text-white']"
  >
    <span class="text-sm font-medium">Read More</span>
    <div class="flex items-center ml-3">
      <div
        class="
          border-t-2 border-red-600
          w-0
          group-hover:w-6
          trasition
          duration-200
          ease-in
        "
      ></div>
      <i class="bx bx-chevron-right bx-sm -ml-3.5"></i>
    </div>
  </nuxt-link>
</template>

<script>
export default {
  name: 'ReadMore',

  props: {
    inverse: {
      type: Boolean,
      default: false,
    },
    path: {
      type: String,
      required: true,
    },
  },
}
</script>

<style></style>
