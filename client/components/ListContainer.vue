<template>
  <div class="border border-gray-200 p-6">
    <div class="border-l-4 border-red-600 px-3 text-2xl font-medium">
      <slot name="heading" />
    </div>
    <ul class="divide-y mt-4">
      <slot />
    </ul>
  </div>
</template>

<script>
export default {
  name: 'ListContainer',
}
</script>

<style></style>
