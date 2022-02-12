<template>
  <div
    class="default-container py-24 backdrop-filter relative mb-20"
    :style="{
      backgroundImage: `linear-gradient(180deg, rgba(255,255,255,0.1), rgba(59,59,68,.9)), url(${backgroundImage})`,
      backgroundSize: 'cover',
      backgroundPosition: 'top',
    }"
  >
    <section-header
      inverse
      data-aos="fade-up"
      data-aos-anchor-placement="top-center"
    >
      <template #title> Client's testimonial </template>
      <template #subtitle> What our clients have to say about us </template>
    </section-header>

    <div class="flex justify-center items-center">
      <div
        class="w-full lg:w-1/2 relative"
        data-aos="fade-up"
        data-aos-anchor-placement="top-center"
      >
        <hooper
          :settings="hooperSettings"
          ref="carousel"
          @slide="updateCarousel"
          style="height: 280px"
        >
          <slide
            v-for="(review, index) in testimonials"
            :key="`${index}-${review.client}`"
            class="h-full"
          >
            <div
              class="
                h-full
                backdrop-filter backdrop-blur-md
                bg-white bg-opacity-60
                rounded-sm
                px-4
                lg:px-10
                py-2
                lg:py-4
              "
            >
              <div class="flex items-center space-x-6 mb-4">
                <img
                  :src="`https://i.pravatar.cc/300?u=${review.client}`"
                  alt="Cleint"
                  class="h-20 w-20 object-center object-cover rounded-full"
                />
                <div>
                  <div class="text-xl font-medium">{{ review.client }}</div>
                  <div>{{ review.of }}</div>
                </div>
              </div>

              <div class="line-clamp-6 font-thin">
                <i class="bx-pull-left bx bxs-quote-alt-left bx-lg" />
                Lorem, ipsum dolor sit amet consectetur adipisicing elit. Magnam
                sint ratione non iusto quis eum aliquam? Illum velit vitae
                tempore. Lorem ipsum dolor sit amet consectetur adipisicing
                elit. Esse in perferendis optio animi incidunt aliquam modi
                ullam doloribus, reiciendis porro. Lorem ipsum dolor sit amet,
                consectetur adipisicing elit. Illum, corrupti quia Lorem ipsum
                dolor sit amet, consectetur adipisicing elit. Itaque tempora cum
                nostrum laudantium impedit maxime alias soluta hic qui expedita?
              </div>
            </div>
          </slide>
        </hooper>
        <!-- Slider Controllers  -->
        <div
          class="
            absolute
            top-1/2
            -left-2
            lg:-left-40
            transform
            -translate-y-1/2
            h-10
            w-10
            text-white
            cursor-pointer
          "
          @click.prevent="slidePrev"
        >
          <i class="bx bx-left-arrow-alt bx-lg"></i>
        </div>
        <div
          class="
            absolute
            top-1/2
            -right-2
            lg:-right-40
            transform
            -translate-y-1/2
            h-10
            w-10
            text-white
            cursor-pointer
          "
          @click.prevent="slideNext"
        >
          <i class="bx bx-right-arrow-alt bx-lg"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import backgroundImage from '~/assets/placeholders/happy-clients.jpg'

import { Hooper, Slide } from 'hooper'
import SectionHeader from '../home/SectionHeader.vue'

export default {
  name: 'ClientTestimonial',

  components: {
    Hooper,
    Slide,
    SectionHeader,
  },

  data() {
    return {
      backgroundImage,
      hooperSettings: {
        autoPlay: true,
        infiniteScroll: true,
        playSpeed: '5000',
      },
      testimonials: [
        { client: 'John Doe', of: 'ABC EFG' },
        { client: 'Tom Hardy', of: 'Meta Bolism' },
        { client: 'Emma Hearth', of: 'Mac Pro' },
        { client: 'Ronald McLovin', of: 'Fre Nchfries' },
      ],
    }
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
