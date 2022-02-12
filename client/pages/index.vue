<template>
  <div class="relative">
    <guest-banner :slides="bannerSlides" />

    <featured-products />

    <site-services />

    <product-types />

    <company-intro />

    <recent-products class="my-40" />

    <featured-banner />

    <client-testimonial />

    <faq />

    <recent-blogs />
  </div>
</template>

<script>
import AppFooter from '~/components/ui/AppFooter.vue'
import ClientTestimonial from '~/components/guest/ClientTestimonial.vue'
import CompanyIntro from '~/components/guest/CompanyIntro.vue'
import Faq from '~/components/guest/Faq.vue'
import FeaturedBanner from '~/components/home/FeaturedBanner.vue'
import FeaturedProducts from '~/components/guest/FeaturedProducts.vue'
import GuestBanner from '~/components/guest/GuestBanner.vue'
import RecentBlogs from '~/components/home/RecentBlogs.vue'
import RecentProducts from '~/components/home/RecentProducts.vue'
import ProductTypes from '~/components/guest/ProductTypes.vue'
import SiteServices from '~/components/SiteServices.vue'

export default {
  name: 'Guest',

  middleware: ['auth'],

  auth: 'guest',

  layout: 'guest',

  head: {
    title: 'KMPL | Welcome',
  },

  scrollToTop: true,

  components: {
    AppFooter,
    CompanyIntro,
    ClientTestimonial,
    Faq,
    FeaturedBanner,
    FeaturedProducts,
    GuestBanner,
    ProductTypes,
    RecentBlogs,
    RecentProducts,
    SiteServices,
  },

  computed: {
    bannerSlides() {
      return this.$store.getters['featured-image/bannerSlides']
    },
  },

  async fetch() {
    await this.$store.dispatch('featured-image/getFeaturedImages')
  },
}
</script>

<style>
.default-container {
  @apply container mx-auto px-4 md:px-20;
}
</style>
