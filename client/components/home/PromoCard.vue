<template>
  <div class="w-full h-full promo">
    <img :src="image" />
    <div
      class="
        promo__content
        text-white
        flex flex-col
        justify-center
        items-end
        pr-4
        md:pr-24
      "
    >
      <div class="uppercase md:text-lg font-medium mb-2">
        <slot name="subtitle">Promo subtitle</slot>
      </div>
      <div class="text-3xl md:text-5xl font-semibold mb-4">
        <slot name="title">Promo title</slot>
      </div>
      <slot name="action" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'BannerImage',

  props: {
    image: {
      type: String,
      required: true,
    },
  },
}
</script>

<style lang="scss">
.promo {
  position: relative;
  overflow: hidden;

  img {
    height: 100%;
    width: 100%;
    object-fit: cover;
    transition: 300ms all ease-in;
  }

  &__content {
    position: absolute;
    inset: 0;
  }

  &:hover img {
    transform: scale(1.1);
  }
}
</style>
