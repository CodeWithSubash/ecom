<template>
  <div class="h-screen relative">
    <hooper
      :settings="hooperSettings"
      ref="carousel"
      @slide="updateCarousel"
      style="height: 100%"
    >
      <slide
        v-for="(slide, indx) in banners"
        :key="slide.title"
        :index="indx"
        class="relative"
        :style="{
          backgroundImage: `url(${slide.image})`,
          height: '100%',
          width: '100%',
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }"
      >
        <!--Slider Contents  -->
        <div
          class="
            absolute
            top-1/2
            left-1/2
            transform
            -translate-x-1/2 -translate-y-1/2
            h-2/3
            lg:h-1/2
            w-4/5
            lg:w-1/2
            bg-white bg-opacity-20
            backdrop-filter backdrop-blur-lg
            flex flex-col
            items-center
            justify-center
            p-10
          "
        >
          <div class="font-medium lg:text-xl uppercase mb-4">
            {{ slide.subtitle }}
          </div>
          <div class="text-center lg:text-3xl mb-6">
            {{ slide.title }}
          </div>
          <p class="font-thin text-center mb-6">
            {{ slide.description }}
          </p>
          <template v-if="slide.categoryId">
            <nuxt-link
              :to="
                $auth.loggedIn
                  ? `/shop?categoryId=${slide.categoryId}`
                  : '/login'
              "
            >
              <base-button>Shop Now</base-button>
            </nuxt-link>
          </template>
        </div>
      </slide>
    </hooper>
    <!-- Controls  -->
    <div
      class="
        absolute
        top-1/2
        left-0
        lg:left-20
        transform
        -translate-y-full
        rounded-sm
        cursor-pointer
        backdrop-filter backdrop-blur-sm
        text-white text-4xl
        lg:text-5xl
      "
      @click.prevent="slidePrev"
    >
      <i class="bx bx-chevron-left"></i>
    </div>

    <div
      class="
        absolute
        top-1/2
        right-0
        lg:right-20
        transform
        -translate-y-full
        rounded-sm
        cursor-pointer
        backdrop-filter backdrop-blur-sm
        text-white text-4xl
        lg:text-5xl
      "
      @click.prevent="slideNext"
    >
      <i class="bx bx-chevron-right"></i>
    </div>
  </div>
</template>

<script>
import backgroundImage from '~/assets/placeholders/shovel.jpg'
import backgroundImage2 from '~/assets/placeholders/wheelbarrow.jpg'
import backgroundImage3 from '~/assets/placeholders/window.jpg'
import backgroundImage4 from '~/assets/placeholders/bolts.jpg'

import { Hooper, Slide } from 'hooper'

export default {
  name: 'GuestBanner',

  components: { Hooper, Slide },

  props: {
    slides: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    return {
      hooperSettings: {
        autoPlay: true,
        infiniteScroll: true,
        playSpeed: '5000',
        wheelControl: false,
      },
      demoSlides: [
        {
          title: 'Demo Banner 1',
          subtitle: 'Demo Banner',
          description: 'This is a description',
          image: backgroundImage,
        },
        {
          title: 'Demo Banner 2',
          subtitle: 'Demo Banner',
          description: 'This is a description',
          image: backgroundImage2,
        },
        {
          title: 'Demo Banner 3',
          subtitle: 'Demo Banner',
          description: 'This is a description',
          image: backgroundImage3,
        },
        {
          title: 'Demo Banner 4',
          subtitle: 'Demo Banner',
          description: 'This is a description',
          image: backgroundImage4,
        },
      ],
    }
  },

  computed: {
    banners() {
      return this.slides.length > 0 ? this.slides : this.demoSlides
    },
  },

  async fetch() {
    await this.$store.dispatch('featured-image/getFeaturedImages')
  },

  methods: {
    slidePrev() {
      this.$refs.carousel.slidePrev()
    },
    slideNext() {
      this.$refs.carousel.slideNext()
    },
    updateCarousel(payload) {
      this.myCarouselData = payload.currentSlide
    },
  },
}
</script>

<style></style>
