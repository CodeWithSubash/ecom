<template>
  <div v-if="featuredAd" class="default-container mb-20">
    <div class="relative h-96">
      <img :src="featuredAd.image" class="h-full w-full object-cover object-bottom"></img>
      <div class="absolute inset-0 flex flex-col justify-center items-center backdrop-filter backdrop-blur-sm">
        <div class="text-white text-center">
          <div class="text-xl">{{featuredAd.subtitle}}</div>
          <div class="text-2xl md:text-4xl font-bold uppercase my-2">
           {{featuredAd.title}}
          </div>
          <div class="text-2xl">
            {{featuredAd.description}}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FeaturedBanner',

  computed: {
    featuredAd(){
      return this.$store.getters['featured-image/featuredAd'][0]
    }
  }
}
</script>

<style></style>
