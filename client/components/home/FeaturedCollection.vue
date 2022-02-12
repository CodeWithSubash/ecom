<template>
  <div v-if="featuredCollections.length" class="default-container my-20">
    <div class="grid md:grid-cols-3 gap-8 md:gap-5">
      <collection-preview
        v-for="collection in featuredCollections"
        :key="collection.title"
        :collection="collection"
      />
    </div>
  </div>
</template>

<script>
import CollectionPreview from '~/components/collections/CollectionPreview.vue'

export default {
  name: 'FeaturedCollection',

  components: { CollectionPreview },

  data() {
    return {
      collections: [
        {
          category: 'Mini Studded',
          name: 'Ducebag Collection',
          image: 'collection-1',
        },
        {
          category: 'Jaccquard Print',
          name: 'Hairband Collection',
          image: 'collection-2',
        },
        {
          category: 'Special Price',
          name: 'Cordury Culottes',
          image: 'collection-3',
        },
      ],
    }
  },

  computed: {
    featuredCollections() {
      return this.$store.getters['featured-image/featuredCollections'].slice(
        0,
        3
      )
    },
  },
}
</script>

<style></style>
