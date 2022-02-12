<template>
  <div class="text-sm text-center font-medium divide-y divide-gray-400">
    <div class="pb-0.5">My Cart</div>
    <div>${{ subtotal.toFixed(2) }}</div>
  </div>
</template>

<script>
export default {
  name: 'CartSubtotal',

  props: {
    subtotal: {
      type: [String, Number],
      default: 0,
    },
  },
}
</script>
