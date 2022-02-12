<template>
  <div
    class="
      group
      fixed
      bottom-4
      right-4
      lg:bottom-10 lg:right-5
      h-14
      w-14
      bg-red-600
      text-white
      hover:text-red-600
      flex
      justify-center
      items-center
      cursor-pointer
      transition-all
      duration-300
      ease-in-out
    "
    :class="[extraClasses]"
    @click="scrollTop"
  >
    <div
      class="
        absolute
        bg-white
        transform
        scale-0
        group-hover:scale-100
        transition
        duration-300
        ease-in-out
      "
      style="height: 98%; width: 98%"
    ></div>
    <i class="bx bx-chevron-up bx-sm z-20"></i>
  </div>
</template>

<script>
export default {
  name: 'ScrollToTop',

  data() {
    return {
      isVisible: false,
    }
  },

  mounted() {
    window.addEventListener('scroll', this.scrollListener)
  },

  destroyed() {
    window.removeEventListener('scroll', this.scrollListener)
  },

  computed: {
    extraClasses() {
      return this.isVisible ? 'visible opacity-100' : 'invisible opacity-0'
    },
  },

  methods: {
    scrollListener() {
      this.isVisible = window.scrollY > 300
    },

    scrollTop() {
      window.scroll({ top: 0, behavior: 'smooth' })
    },
  },
}
</script>

<style></style>
