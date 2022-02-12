<template>
  <div>
    <guest-header />
    <client-only>
      <nuxt />
    </client-only>
    <app-footer />
    <scroll-to-top />
  </div>
</template>

<script>
import GuestHeader from '~/components/guest/GuestHeader.vue'
import AppFooter from '~/components/ui/AppFooter.vue'
import ScrollToTop from '~/components/ui/ScrollToTop.vue'

export default {
  name: 'GuestLayout',

  middleware: 'guest',

  components: { ScrollToTop, GuestHeader, AppFooter },
}
</script>

<style></style>
