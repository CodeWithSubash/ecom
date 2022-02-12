<template>
  <div class="text-center uppercase mb-10">
    <div
      class="font-medium mb-2"
      :class="[!inverse ? 'text-gray-400' : 'text-gray-600']"
    >
      <slot name="title" />
    </div>
    <div class="text-2xl font-semibold" :class="[inverse && 'text-white']">
      <slot name="subtitle" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'SectionHeader',

  props: {
    inverse: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style></style>
