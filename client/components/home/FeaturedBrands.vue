<template>
  <div class="default-container">
    <Flicking :options="options" :plugins="plugins" class="h-full w-full">
      <div
        class="
          relative
          h-20
          w-full
          md:w-1/5
          space-x-4
          backdrop-filter backdrop-grayscale
          hover:backdrop-grayscale-0
          cursor-pointer
        "
        v-for="slide in slides"
      >
        <img
          :src="require(`~/assets/brands/${slide}.png`)"
          class="h-full w-full object-contain"
        />
      </div>
      <span slot="viewport" class="flicking-arrow-prev"></span>
      <span slot="viewport" class="flicking-arrow-next"></span>
    </Flicking>
  </div>
</template>

<script>
import { Flicking } from '@egjs/vue-flicking'
import { AutoPlay, Arrow } from '@egjs/flicking-plugins'
import '@egjs/flicking-plugins/dist/arrow.css'

export default {
  name: 'HeroSlider',

  components: {
    Flicking,
  },

  data() {
    return {
      options: {
        circular: true,
        duration: 1500,
      },
      plugins: [new AutoPlay(), new Arrow()],
      slides: [
        'brand-1',
        'brand-2',
        'brand-3',
        'brand-4',
        'brand-1',
        'brand-5',
      ],
    }
  },
}
</script>

<style>
.flicking-arrow-prev::before,
.flicking-arrow-prev::after,
.flicking-arrow-next::before,
.flicking-arrow-next::after {
  background-color: gray;
}
</style>
