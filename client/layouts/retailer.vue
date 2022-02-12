<template>
  <div class="min-h-screen relative">
    <client-only>
      <social-media class="hidden md:block" />
      <app-header class="hidden md:block" />
      <default-navbar />
      <div class="mb-14">
        <nuxt />
      </div>
      <app-footer />
      <scroll-to-top />
      <kyc-info />
    </client-only>
  </div>
</template>

<script>
import AppHeader from '@/components/ui/AppHeader.vue'
import AppFooter from '@/components/ui/AppFooter.vue'
import BreadCrumb from '@/components/ui/BreadCrumb.vue'
import DefaultNavbar from '@/components/ui/DefaultNavbar.vue'
import ScrollToTop from '~/components/ui/ScrollToTop.vue'
import SocialMedia from '@/components/ui/SocialMedia.vue'

export default {
  name: 'DefaultLayout',

  middleware: 'guest',

  components: {
    AppHeader,
    AppFooter,
    BreadCrumb,
    DefaultNavbar,
    ScrollToTop,
    SocialMedia,
  },

  computed: {
    isHomepage() {
      return this.$nuxt.$route.name === 'Home'
    },
  },
}
</script>

<style>
div {
  font-family: 'Poppins', 'san-serif';
}
</style>
