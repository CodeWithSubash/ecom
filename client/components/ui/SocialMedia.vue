<template>
  <div class="bg-gray-800">
    <div
      class="
        default-container
        py-2
        text-white
        flex flex-col
        md:flex-row
        justify-between
        items-center
        space-y-2
        md:space-y-0
      "
    >
      <div class="text-xs md:text-sm">
        Get 50% Discount on all summar arrival
      </div>
      <div class="flex items-center space-x-2">
        <div
          v-for="sm in socialMedias"
          :key="sm.icon"
          class="
            transform
            duration-200
            ease-in
            hover:-translate-y-1 hover:text-red-600
            cursor-pointer
          "
        >
          <a :href="sm.path" target="_blank">
            <i :class="sm.icon" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SocialMedia',

  data() {
    return {
      socialMedias: [
        { icon: 'bx bxl-facebook', path: 'https://facebook.com' },
        { icon: 'bx bxl-twitter', path: 'https://twiter.com' },
        { icon: 'bx bxl-pinterest', path: 'https://pinterest.com' },
        { icon: 'bx bxl-youtube', path: 'https://youtube.com' },
        { icon: 'bx bxl-instagram', path: 'https://instagram.com' },
      ],
    }
  },
}
</script>

<style></style>
