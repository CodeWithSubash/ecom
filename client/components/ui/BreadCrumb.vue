<template>
  <div class="bg-gray-100 mb-14">
    <div class="default-container py-16">
      <div class="flex justify-center items-center">
        <div class="text-center font-semibold">
          <div class="text-3xl mb-5">
            {{ breadcrumbs[breadcrumbs.length - 1].name }}
          </div>
          <div class="flex items-center justify-center text-sm uppercase">
            <nuxt-link to="/home" class="">Home</nuxt-link>
            <template v-for="(crumb, index) in breadcrumbs">
              <div :key="crumb.name" class="flex items-center">
                <span class="mx-2 text-2xl font-thin">/</span>
                <nuxt-link
                  v-if="index !== breadcrumbs.length - 1"
                  :to="crumb.path"
                >
                  {{ crumb.name }}
                </nuxt-link>
                <div v-else class="text-red-600">{{ crumb.name }}</div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BreadCrumbs',

  props: {
    breadcrumbs: {
      type: Array,
      required: true,
    },
  },
}
</script>

<style></style>
