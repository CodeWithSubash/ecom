<template>
  <div class="default-container mt-32 mb-40">
    <section-header data-aos="fade-up">
      <template #title> Frequrently Asked Questions</template>
      <template #subtitle
        >Have confusions? Try looking down some FAQs
      </template>
    </section-header>

    <div class="mx-auto w-full lg:w-5/6 mb-20">
      <collapse-item
        v-for="n in 6"
        :key="n"
        class="mb-4 last:mb-0"
        data-aos="fade-up"
        :data-aos-delay="n * 100"
      />
    </div>

    <div class="text-center" data-aos="fade-up">
      <div class="text-3xl mb-4">Still have a questions?</div>
      <div>
        If you cannot find answer to your question in our FAQ , you can always
        <span class="text-red-600">
          <nuxt-link to="/contact">contact us</nuxt-link> </span
        >. We will answer to you shortly.
      </div>
    </div>
  </div>
</template>

<script>
import CollapseItem from '../CollapseItem.vue'
import SectionHeader from '../home/SectionHeader.vue'

export default {
  name: 'FAQ',

  components: { SectionHeader, CollapseItem },

  data() {
    return {
      show: true,
    }
  },
}
</script>

<style></style>
