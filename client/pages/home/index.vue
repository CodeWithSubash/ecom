<template>
  <div>
    <hero-banner />
    <site-services />
    <featured-collection />
    <recent-products />
    <featured-banner />
    <recent-blogs />
    <featured-brands />
  </div>
</template>

<script>
import HeroBanner from '@/components/home/HeroBanner.vue'
import RecentBlogs from '~/components/home/RecentBlogs.vue'
import RecentProducts from '~/components/home/RecentProducts.vue'
import FeaturedCollection from '~/components/home/FeaturedCollection.vue'
import FeaturedBanner from '~/components/home/FeaturedBanner.vue'
import FeaturedBrands from '~/components/home/FeaturedBrands.vue'
import SiteServices from '~/components/SiteServices.vue'

export default {
  name: 'Home',

  layout: 'retailer',

  middleware: 'auth',

  head: {
    title: 'KMPL | Home',
  },

  components: {
    HeroBanner,
    FeaturedBanner,
    FeaturedCollection,
    RecentBlogs,
    RecentProducts,
    FeaturedBrands,
    SiteServices,
  },

  scrollToTop: true,

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
