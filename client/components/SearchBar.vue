<template>
  <div
    class="
      flex flex-col
      md:flex-row md:items-center md:border-2 md:border-gray-200
      rounded
      text-sm
      space-y-4
      md:space-y-0
    "
  >
    <select
      v-model="selectedCategory"
      class="
        md:mx-2
        py-2
        w-full
        md:w-36
        rounded
        focus:outline-none focus:ring focus:border-blue-300
      "
    >
      <option
        v-for="category in categoryList"
        :key="category.id"
        :value="category.id"
        class="px-2"
      >
        {{ category.name }}
      </option>
    </select>
    <div class="hidden md:block text-3xl font-thin text-gray-400">|</div>
    <div class="flex border border-gray-400 md:border-0 md:rounded-none">
      <input
        id="searchBar"
        v-model="search"
        type="text"
        placeholder="Search Entire Store Here"
        class="px-2 md:py-1.5 placeholder-gray-500 w-full md:w-72"
      />
      <div
        class="bg-red-500 text-white px-4 py-1.5 md:py-2.5 cursor-pointer"
        @click="searchProduct"
      >
        <i class="bx bx-search text-2xl"></i>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'SearchBar',

  data() {
    return {
      search: '',
      selectedCategory: 0,
    }
  },

  computed: {
    ...mapState({
      categories: (state) => state.category.categories,
    }),
    keyword() {
      return this.$route.query.search
    },
    categoryId() {
      return this.$route.query.categoryId
    },
    categoryList() {
      return [{ id: 0, name: 'All Categories' }, ...this.categories]
    },
  },

  watch: {
    keyword: {
      immediate: true,
      handler(nv) {
        if (nv) this.search = nv
        else this.search = ''
      },
    },
    categoryId: {
      immediate: true,
      handler(nv) {
        this.selectedCategory = nv ?? 0
      },
    },
  },

  methods: {
    searchProduct() {
      this.$router.push(
        `/shop?search=${this.search}&categoryId=${this.selectedCategory}`
      )
    },
  },
}
</script>

<style></style>
