<template>
  <Flicking :options="options" :plugins="plugins" class="h-full w-full">
    <div class="relative h-full w-full" v-for="slide in sliderImages">
      <el-image :src="slide.image" class="h-full w-full object-cover">
      </el-image>
      <div class="absolute inset-0 bg-gray-700 opacity-20"></div>
      <div class="absolute inset-0 flex justify-center items-center">
        <div class="text-white text-center">
          <div class="uppercase md:text-lg font-medium mb-2">
            {{ slide.subtitle }}
          </div>
          <div class="text-3xl md:text-5xl font-semibold mb-4">
            {{ slide.title }}
          </div>

          <template v-if="slide.categoryId">
            <nuxt-link :to="`/shop?categoryId=${slide.categoryId}`">
              <base-button>Shop Now</base-button>
            </nuxt-link>
          </template>
        </div>
      </div>
    </div>
    <div slot="viewport" class="flicking-pagination"></div>
  </Flicking>
</template>

<script>
import demoSlide from '~/assets/placeholders/wheelbarrow.jpg'
import demoSlide2 from '~/assets/placeholders/bolts.jpg'
import demoSlide3 from '~/assets/placeholders/shovel.jpg'

import { Flicking } from '@egjs/vue-flicking'
import { AutoPlay, Pagination } from '@egjs/flicking-plugins'

export default {
  name: 'HeroSlider',

  components: {
    Flicking,
  },

  props: {
    slides: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    return {
      options: {
        align: 'prev',
        circular: true,
        duration: 1500,
      },
      plugins: [new AutoPlay(), new Pagination({ type: 'bullet' })],

      demoSlides: [
        {
          title: 'Demo Slide',
          subtitle: 'Sales 20% off this week',
          image: demoSlide,
        },
        {
          title: 'Some good Heading',
          subtitle: 'Lorem Ipsum.',
          image: demoSlide2,
        },
        {
          title: 'Insert Title here',
          subtitle: 'Sales upto 25% off',
          image: demoSlide3,
        },
      ],
    }
  },

  computed: {
    sliderImages() {
      return this.slides.length ? this.slides : this.demoSlides
    },
  },
}
</script>

<style>
@import url('node_modules/@egjs/vue-flicking/dist/flicking.css');
@import url('node_modules/@egjs/flicking-plugins/dist/pagination.css');

.flicking-pagination-bullet {
  height: 1rem;
  width: 1rem;
}

.flicking-pagination-bullet-active {
  background-color: red;
}
</style>
