<template>
  <div class="h-full w-full bg-gray-50">
    <div v-if="$slots.header" class="px-10 py-6 bg-gray-50 sticky top-0 z-10">
      <slot name="header" />
    </div>
    <div class="px-10" :class="$slots.header ? 'pb-6' : 'py-6'">
      <slot />
    </div>
  </div>
</template>

<script>
export default {
  name: 'WithHeader',
}
</script>

<style></style>
