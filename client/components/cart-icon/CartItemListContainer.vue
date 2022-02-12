<template>
  <div
    class="
      absolute
      right-0
      z-10
      bg-white
      shadow-lg
      invisible
      group-hover:visible
      transform
      translate-y-8
      group-hover:translate-y-0
      opacity-0
      group-hover:opacity-100
      transition
      duration-200
      ease-in
    "
  >
    <slot />
  </div>
</template>

<script>
export default {
  name: 'CartListContainer',
}
</script>
