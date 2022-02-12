<template>
  <div class="default-container mb-20 md:mb-28">
    <section-header data-aos="fade-up">
      <template #title>News blog</template>
      <template #subtitle>Whats new in KPML</template>
    </section-header>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-10 md:gap-5">
      <s-blog-preview
        v-for="(blog, index) in blogs"
        :key="blog.id"
        :blog="blog"
        data-aos="fade-up"
        :data-aos-delay="(index + 1) * 100"
      />
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

import SBlogPreview from '~/components/blog/SBlogPreview.vue'
import SectionHeader from './SectionHeader.vue'

export default {
  name: 'RecentBlogs',

  components: { SBlogPreview, SectionHeader },

  computed: {
    ...mapState({
      blogs: (state) => state.blog.allBlogs,
    }),
  },
}
</script>

<style></style>
