<template>
  <client-only>
    <div class="flex">
      <slot name="sidebar" />
      <slot />
    </div>
  </client-only>
</template>

<script>
export default {
  name: 'WithSideMenus',
}
</script>

<style></style>
