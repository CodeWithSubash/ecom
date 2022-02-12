<template>
  <div
    class="default-container"
    :style="{
      backgroundImage: `linear-gradient(180deg, rgba(255,255,255,0.1), rgba(59,59,68,.9)), url(${backgroundImage})`,
      backgroundSize: 'cover',
      backgroundPosition: 'bottom',
    }"
  >
    <div class="grid lg:grid-cols-4 lg:px-20 lg:transform lg:-translate-y-1/3">
      <div
        v-for="(genre, index) in productGenres"
        :key="index"
        class="
          p-8
          bg-white
          flex flex-col
          items-center
          odd:bg-gray-700 odd:text-white
        "
        data-aos="fade-up"
        :data-aos-delay="(index + 1) * 200"
        data-aos-anchor-placement="top-bottom"
      >
        <div class="group flex flex-col items-center">
          <div class="h-48 w-48 relative rounded-full overflow-hidden bg-white">
            <div
              class="
                absolute
                w-72
                h-72
                top-0
                bg-gray-200 bg-opacity-40
                transform
                rotate-45
                -translate-x-full -translate-y-full
                group-hover:-translate-x-20 group-hover:-translate-y-20
                transition
                duration-300
                ease-in-out
                z-10
              "
            ></div>
            <img
              :src="require(`~/assets/product-genre/${genre.image}.jpg`)"
              class="
                h-full
                w-full
                object-center object-contain
                transform
                scale-105
                group-hover:scale-90
                transition
                duration-300
                ease-in-out
              "
              alt="Product Genre"
            />
          </div>
          <div class="my-4 text-xl font-medium">{{ genre.name }}</div>
          <div class="line-clamp-4 text-center mb-4 font-thin text-sm">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Blanditiis,
            delectus? Lorem ipsum dolor sit amet consectetur adipisicing elit.
            Quae, adipisci?
          </div>
        </div>
        <read-more path="/" />
      </div>
    </div>
    <div
      class="text-center text-white text-3xl lg:text-5xl font-medium lg:pb-20"
    >
      <div class="mt-10 lg:-mt-10 pb-10">Lorem ipsum dolor sit.</div>
    </div>
  </div>
</template>

<script>
import backgroundImage from '~/assets/placeholders/wheelbarrow.jpg'
import ReadMore from '../ReadMore.vue'

export default {
  components: { ReadMore },
  name: 'ProductGenres',

  data() {
    return {
      backgroundImage,
      productGenres: [
        { name: 'Shovels', image: 'shovels' },
        { name: 'Wheel Barrows', image: 'wheel-barrow' },
        { name: 'Door Frames', image: 'door-frame' },
        { name: 'Roof Bolts', image: 'wheel-barrow' },
      ],
    }
  },
}
</script>

<style></style>
