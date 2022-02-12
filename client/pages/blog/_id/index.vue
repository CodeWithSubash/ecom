<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <with-product-aside>
      <div class="flex-shrink-0 flex items-center space-x-4 pb-4 text-sm">
        <div class="h-8 w-8 rounded-full overflow-hidden bg-gray-100">
          <img
            src="https://i.pravatar.cc/300"
            alt="Blog Author"
            class="h-full w-full"
          />
        </div>
        <div>John Doe</div>
        <div class="text-gray-500">
          <i class="bx bx-file-blank"></i>
          Blog
        </div>
      </div>
      <div class="blog-preview-image relative mb-6">
        <img
          :src="require(`~/assets/blogs/blog-1.jpg`)"
          alt="Blog Thumbail"
          class="h-full w-full object-cover"
        />
        <div
          class="
            h-20
            w-20
            bg-red-500
            absolute
            top-4
            left-4
            md:top-8 md:left-8
            flex flex-col
            justify-center
            items-center
            text-white
            font-medium
          "
        >
          <div class="text-4xl">{{ getDate(new Date()).day }}</div>
          <div class="uppercase">{{ getDate(new Date()).month }}</div>
        </div>
      </div>
      <!-- Blog content  -->
      <div class="font-light border-b border-gray-300 pb-10">
        Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quis, fugiat?
        Esse nostrum rerum reiciendis expedita! Facere numquam sapiente
        veritatis incidunt eaque et! Tempora ullam natus, expedita voluptatibus
        libero quia pariatur voluptates ex cupiditate explicabo optio rerum
        numquam, mollitia tempore recusandae consectetur. Eligendi, quibusdam
        exercitationem! Debitis vel aut quas quisquam praesentium! Dolor saepe
        quas autem itaque necessitatibus consectetur repellat! Consequuntur
        dicta fuga quos molestias et recusandae. Laudantium similique nostrum
        fugit, inventore, vitae voluptatem quisquam explicabo, quae quo facere
        eligendi! Laboriosam nesciunt quidem ipsum ipsa numquam quam eos vel
        esse deserunt quasi culpa, dolorem quae obcaecati, voluptates odio a aut
        inventore atque!
      </div>

      <div
        class="
          flex flex-col
          space-y-4
          md:space-y-0 md:flex-row md:justify-between
          pb-14
        "
      >
        <!-- Previous Post  -->
        <div class="flex items-center justify-between space-x-4 pt-6">
          <div class="h-24 w-24 bg-red-200">
            <img
              :src="require('~/assets/blogs/blog-2.jpg')"
              alt="Next Blog"
              class="h-full w-full object-cover"
            />
          </div>
          <div class="flex flex-col items-end md:items-start">
            <div class="hover:text-red-600 cursor-pointer line-clamp-2">
              Lorem ipsum dolor sit.
            </div>
            <div class="flex items-center">
              <i class="bx bx-left-arrow-alt mr-2"></i>
              <span class="text-gray-500">Previous Post</span>
            </div>
          </div>
        </div>
        <!-- Next Post  -->
        <div class="flex items-center justify-between space-x-4 pt-6">
          <div class="flex flex-col md:items-end">
            <div class="hover:text-red-600 cursor-pointer line-clamp-2">
              Lorem ipsum dolor sit.
            </div>
            <div class="flex items-center">
              <span class="text-gray-500">Next Post</span>
              <i class="bx bx-right-arrow-alt ml-2"></i>
            </div>
          </div>
          <div class="h-24 w-24 bg-red-200">
            <img
              :src="require('~/assets/blogs/blog-1.jpg')"
              alt="Next Blog"
              class="h-full w-full object-cover"
            />
          </div>
        </div>
      </div>
    </with-product-aside>
  </div>
</template>

<script>
import splitDate from '~/mixins/splitDate'

import WithProductAside from '~/components/layouts/WithProductAside.vue'
import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'BlogDetail',

  layout: 'retailer',

  components: { BreadCrumb },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Blog',
          path: '/blog',
        },
        {
          name: 'Blog Title',
          path: '/blog/product',
        },
      ],
    }
  },

  scrollToTop: true,

  mixins: [splitDate],
}
</script>

<style></style>
