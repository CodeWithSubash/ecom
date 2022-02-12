<template>
  <div
    class="
      w-full
      rounded
      bg-white
      flex
      justify-between
      border border-gray-100
      px-5
      py-3
      shadow
    "
  >
    <div class="flex items-center space-x-3.5">
      <div
        class="
          font-medium
          bg-red-50
          text-gray-500
          rounded-full
          h-12
          w-12
          grid
          place-content-center
        "
      >
        <slot name="icon" />
      </div>
      <div>
        <div class="text-xs font-medium text-gray-500 mb-0.5">
          {{ title }}
        </div>
        <div class="font-medium text-lg text-gray-600">
          <slot />
        </div>
      </div>
    </div>
    <div>
      <i class="bx bx-bar-chart bx-lg text-gray-200"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CountCard',

  props: {
    title: {
      type: String,
      default: 'Title here',
    },
  },
}
</script>

<style></style>
