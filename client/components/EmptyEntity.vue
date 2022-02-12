<template>
  <div class="flex items-center border-t border-red-600 bg-gray-100 p-4">
    <div class="text-red-600 mt-1 mr-2">
      <slot name="icon">
        <i class="bx bx-info-circle bx-sm" />
      </slot>
    </div>
    <div class="text-base">
      <slot> No data found </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EmptyEntity',
}
</script>

<style></style>
