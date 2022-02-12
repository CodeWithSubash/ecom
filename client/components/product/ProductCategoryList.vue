<template>
  <div>
    <list-container>
      <template #heading>Product Categories</template>
      <div
        v-for="category in categories"
        :key="category.id"
        class="
          py-5
          last:pb-0
          text-base
          font-medium
          tracking-wide
          hover:text-red-600
          transition
          duration-300
          ease-in
          cursor-pointer
        "
      >
        <div>{{ category.name }}</div>
      </div>
    </list-container>
  </div>
</template>

<script>
export default {
  name: 'ProductCategoryList',

  data() {
    return {}
  },

  computed: {
    categories() {
      return this.$store.getters['category/categoryPreview']
    },
  },
}
</script>

<style></style>
