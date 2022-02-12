<template>
  <div
    class="flex flex-col md:flex-row"
    :style="{
      height: bannerContainerHeight,
    }"
  >
    <!-- Carousel  -->
    <div class="w-full md:w-1/2 h-1/3 md:h-full">
      <hero-slider :slides="bannerSlides" />
    </div>
    <!-- Promotions Column  -->
    <div class="w-full md:w-1/2 h-1/3 md:h-full">
      <div class="h-full md:h-1/2">
        <template v-for="promotion in promotions">
          <promo-card :key="promotion.title" :image="promotion.image">
            <template #subtitle>{{ promotion.subtitle }}</template>
            <template #title>{{ promotion.title }}</template>
            <template v-if="promotion.categoryId" #action>
              <nuxt-link :to="`/shop?categoryId=${promotion.categoryId}`">
                <base-button>Shop Now</base-button>
              </nuxt-link>
            </template>
          </promo-card>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import demoPromo from '~/assets/placeholders/wheelbarrow.jpg'
import demoPromo2 from '~/assets/placeholders/shovel.jpg'

import HeroSlider from '@/components/home/HeroSlider.vue'
import PromoCard from '@/components/home/PromoCard.vue'

export default {
  name: 'Banners',

  components: { HeroSlider, PromoCard },

  data() {
    return {
      demoPromotions: [
        {
          title: 'Demo Promotion',
          subtitle: 'Demo',
          image: demoPromo,
        },
        {
          title: 'Another Promotion',
          subtitle: 'Another Demo',
          image: demoPromo2,
        },
      ],
    }
  },

  computed: {
    bannerContainerHeight() {
      return window.screen.width > 768 ? '68vh' : '100vh'
    },

    bannerSlides() {
      return this.$store.getters['featured-image/bannerSlides']
    },

    fixedBanners() {
      return this.$store.getters['featured-image/fixedBanners'].slice(0, 2)
    },

    promotions() {
      return this.fixedBanners.length ? this.fixedBanners : this.demoPromotions
    },
  },
}
</script>
