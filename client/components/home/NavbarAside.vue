<template>
  <div
    class="
      lg:hidden
      box-border
      fixed
      inset-0
      bg-white
      z-10
      transform
      transition-transform
      duration-150
      ease-in
    "
    :class="[!openSidebar ? '-translate-x-full' : 'translate-x-0']"
  >
    <div class="flex justify-between px-4 py-2 shadow">
      <app-logo />
      <div class="p-2" @click="openSidebar = false">
        <i class="bx bx-x bx-md"></i>
      </div>
    </div>
    <slot />
  </div>
</template>

<script>
import AppLogo from '../ui/AppLogo.vue'

export default {
  components: { AppLogo },

  props: ['value'],

  data() {
    return {
      openSidebar: this.value,
    }
  },

  watch: {
    value: {
      handler(value) {
        this.openSidebar = value
      },
    },
  },
}
</script>

<style></style>
