<template>
  <div class="default-container flex flex-col md:flex-row md:space-x-10">
    <!-- Content -->
    <div class="w-full md:w-2/3 mb-12 md:mb-0">
      <slot />
    </div>
    <!-- Aside Content -->
    <div class="w-full md:w-1/3">
      <!-- Search Products  -->
      <!-- <div class="border border-gray-200 p-6 mb-10">
        <div class="border border-gray-200 h-12 flex justify-end">
          <div class="h-full w-12 bg-red-400"></div>
        </div>
      </div> -->
      <!-- Recent/Similar Products List  -->
      <recent-products-list class="mb-12" />
      <!-- Category List   -->
      <product-category-list class="sticky top-0" />
    </div>
  </div>
</template>

<script>
import RecentProductsList from '@/components/product/RecentProductsList.vue'
import ProductCategoryList from '@/components/product/ProductCategoryList.vue'

export default {
  name: 'ProductAside',

  components: {
    RecentProductsList,
    ProductCategoryList,
  },
}
</script>

<style></style>
