<template>
  <div>
    <div class="blog-preview-image">
      <img
        :src="require(`~/assets/placeholders/${blog.image}.jpg`)"
        alt="Blog Thumbail"
        class="h-full w-full object-cover"
      />
    </div>
    <div class="bg-gray-50 p-4 md:px-10 py-8 md:py-14">
      <div
        class="
          flex flex-col
          md:flex-row
          items-center
          md:items-start md:space-x-6
        "
      >
        <!-- Author  -->
        <div
          class="w-24 flex-shrink-0 flex flex-col items-center md:space-y-4 p-2"
        >
          <div class="h-11 w-11 rounded-full overflow-hidden bg-gray-100">
            <img
              src="https://i.pravatar.cc/300"
              alt="Blog Author"
              class="h-full w-full"
            />
          </div>
          <div class="font-medium text-sm">John Doe</div>
        </div>
        <!-- Blog Preview -->
        <div class="flex-grow text-center md:text-left">
          <div class="text-sm text-red-600 mb-4">Blog</div>
          <div class="text-3xl font-medium line-clamp-2 mb-6">
            {{ blog.title }}
          </div>
          <div class="line-clamp-2 tracking-wide mb-8">
            Blog Card Lorem ipsum dolor sit amet consectetur adipisicing elit.
            Possimus nisi alias illum laboriosam maxime optio inventore totam
            consectetur cum sint nemo, beatae eaque vero, atque magnam fuga
            assumenda autem voluptates odio dignissimos culpa? Sapiente nulla,
            id provident itaque totam et fugit aliquam beatae eum, tempora modi
            possimus vitae quidem sunt mollitia aliquid quaerat ab. Animi
            explicabo distinctio beatae vel ab repellendus, quae quisquam
            pariatur atque enim, mollitia nihil neque non vitae. Aliquam sed
            quisquam numquam rerum exercitationem vero omnis nesciunt impedit
            quis, perferendis ea facilis accusantium recusandae perspiciatis
            fugit iusto repudiandae? Excepturi sapiente quas sequi numquam
            consequuntur, possimus nam error!
          </div>
          <nuxt-link :to="`/blog/${blog.title}`">
            <base-animated-button>Continue Reading</base-animated-button>
          </nuxt-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BlogCard',

  props: {
    blog: {
      type: Object,
      required: true,
    },
  },
}
</script>
