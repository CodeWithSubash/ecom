<template>
  <div
    class="
      w-12
      bg-white
      rounded-sm
      grid
      place-content-center
      cursor-pointer
      hover:text-red-600
      transition
      duration-200
      ease-in
      border border-gray-200
    "
  >
    <slot />
  </div>
</template>

<script>
export default {
  name: 'IconBox',
}
</script>

<style></style>
