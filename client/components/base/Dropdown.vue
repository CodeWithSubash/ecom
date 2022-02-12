<template>
  <div>
    <div class="group relative">
      <div class="hover:text-red-600 transition-colors duration-200 ease-in">
        <slot name="trigger" />
      </div>
      <div
        class="
          absolute
          bg-white
          transform
          translate-y-10
          group-hover:translate-y-0
          opacity-10
          group-hover:opacity-100
          invisible
          group-hover:visible
          transition
          duration-200
          ease-in
          z-10
        "
      >
        <slot name="dropdown-items" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BaseDropdown',
}
</script>

<style></style>
