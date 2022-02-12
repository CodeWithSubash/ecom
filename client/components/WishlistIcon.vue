<template>
  <div class="relative" v-show="$auth.loggedIn">
    <i class="bx bx-heart bx-md"></i>
    <div class="absolute -top-1 -right-1">
      <div
        class="
          bg-red-500
          text-white
          rounded-full
          h-6
          w-6
          grid
          place-content-center
          box-border
        "
      >
        <div class="text-sm">{{ wishlistCount }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WishlistIcon',

  computed: {
    wishlistCount() {
      return this.$store.getters['wishlist/wishlistCount']
    },
  },

  async created() {
    if (this.$auth.loggedIn) {
      const { data } = await this.$axios.get('/auth/wishlist/getByUser')
      if (data) this.$store.commit('wishlist/SET_WISHLIST', data)
    }
  },
}
</script>

<style></style>
