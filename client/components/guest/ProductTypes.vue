<template>
  <div class="default-container py-20">
    <section-header>
      <template #title>Our Products</template>
      <template #subtitle>Take a look at our prducts archieve</template>
    </section-header>
    <div class="grid lg:grid-cols-4 lg:w-11/12 mx-auto">
      <div
        v-for="(type, index) in productTypes"
        :key="type.name"
        data-aos="fade-up"
        :data-aos-delay="(index + 1) * 100"
      >
        <div
          class="flex flex-col items-center w-60 mx-auto mb-10 border-l group"
        >
          <img
            :src="type.image"
            alt="Product Type"
            class="h-52 w-full object-center"
          />
          <div
            class="
              text-sm
              font-thin
              py-2
              group-hover:bg-gray-100
              w-full
              text-center
            "
          >
            {{ type.name }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from '../home/SectionHeader.vue'
export default {
  components: { SectionHeader },
  name: 'ProductTypes',

  data() {
    return {
      productTypes: [
        {
          name: 'Nipples',
          image: require('~/assets/product-genre/door-frame.jpg'),
        },
        {
          name: 'Hinges',
          image: require('~/assets/product-genre/shovels.jpg'),
        },
        {
          name: 'Shovels',
          image: require('~/assets/product-genre/wheel-barrow.jpg'),
        },
        {
          name: 'Roof Bolts',
          image: require('~/assets/product-genre/door-frame.jpg'),
        },
        {
          name: 'Washers',
          image: require('~/assets/product-genre/shovels.jpg'),
        },
        {
          name: 'Sag Rods',
          image: require('~/assets/product-genre/wheel-barrow.jpg'),
        },
        {
          name: 'Spades, etc',
          image: require('~/assets/product-genre/door-frame.jpg'),
        },
        {
          name: 'Piston Hinges',
          image: require('~/assets/product-genre/shovels.jpg'),
        },
        {
          name: 'Steel Windows',
          image: require('~/assets/product-genre/wheel-barrow.jpg'),
        },
        {
          name: 'Face Basin Brackets',
          image: require('~/assets/product-genre/door-frame.jpg'),
        },
        {
          name: 'Aluminium Door Frames',
          image: require('~/assets/product-genre/shovels.jpg'),
        },
        {
          name: 'Wheel Barrows',
          image: require('~/assets/product-genre/wheel-barrow.jpg'),
        },
      ],
    }
  },
}
</script>

<style></style>
