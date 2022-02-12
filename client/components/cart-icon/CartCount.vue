<template>
  <div class="relative block">
    <i class="bx bx-cart bx-md"></i>
    <div
      class="
        absolute
        -top-1
        -right-2
        bg-red-500
        text-white text-sm
        rounded-full
        px-2
        py-0.5
      "
    >
      {{ itemCount }}
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'CartIcon',

  computed: {
    ...mapGetters({
      itemCount: 'cart/cartItemCount',
    }),
  },
}
</script>
