<template>
  <div class="border-l">
    <div class="h-80 w-full bg-red-200">
      <img
        :src="require(`~/assets/placeholders/${blog.image}.jpg`)"
        alt="Blog Preview Image"
        class="h-full w-full object-cover"
      />
    </div>
    <div class="px-6 pt-6">
      <!-- Date  -->
      <div class="inline-flex items-center text-red-600">
        <div class="text-4xl pr-4 py-0.5 font-medium border-r border-red-600">
          {{ getDate(new Date()).day }}
        </div>
        <div class="pl-4 py-1 -mt-1">
          <div class="font-medium">{{ getDate(new Date()).month }}</div>
          <div class="text-sm leading-3">{{ getDate(new Date()).year }}</div>
        </div>
      </div>
      <div class="py-4 text-xl font-medium line-clamp-1">
        {{ blog.title }}
      </div>
      <div class="line-clamp-2 text-sm text-gray-600 mb-4">
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Sequi natus
        autem cum sapiente quasi id quidem et non dolores corrupti.
      </div>
      <read-more :path="`/blog/${blog.title}`" />
    </div>
  </div>
</template>

<script>
import splitDate from '~/mixins/splitDate'

export default {
  name: 'SmallBlogPreview',

  props: {
    blog: {
      type: Object,
      required: true,
    },
  },

  mixins: [splitDate],
}
</script>

<style></style>
