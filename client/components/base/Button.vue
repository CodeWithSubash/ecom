<template>
  <button
    class="
      inline-block
      text-white
      uppercase
      transition-all
      duration-500
      ease-in
    "
    :disabled="isDisabled"
    :class="dynamicStyles"
  >
    <div class="flex items-center justify-center">
      <slot>hehe</slot>
      <span
        class="ml-2 mt-1 transform transition-all duration-150 ease-in"
        :class="
          loading ? 'translate-x-0 opacity-100' : '-translate-x-2  opacity-0'
        "
      >
        <i v-if="loading" class="bx bx-loader-alt bx-spin"></i>
      </span>
    </div>
  </button>
</template>

<script>
export default {
  name: 'BaseButton',

  props: {
    inverse: {
      type: Boolean,
      default: false,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    onHover: {
      type: Boolean,
      default: false,
    },
    small: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },

  computed: {
    background() {
      return !this.inverse ? 'bg-red-500' : 'bg-black'
    },

    cursor() {
      return this.isDisabled && 'cursor-not-allowed'
    },

    isDisabled() {
      return this.disabled || this.loading
    },

    hover() {
      if (this.onHover) {
        return !this.inverse ? 'hover:bg-black' : 'hover:bg-red-500'
      }
      return ''
    },

    size() {
      return !this.small ? 'px-6 md:px-8 py-2  md:py-3' : 'px-4 py-2'
    },

    dynamicStyles() {
      return `${this.background} ${this.cursor} ${
        !this.isDisabled && this.hover
      } ${this.size}`
    },
  },
}
</script>

<style></style>
