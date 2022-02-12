<template>
  <div>
    <el-breadcrumb
      separator-class="el-icon-arrow-right"
      class="px-4 py-2 bg-white rounded"
    >
      <el-breadcrumb-item
        v-for="crumb in breadcrumbs"
        :key="crumb.title"
        :to="{ path: crumb.path }"
      >
        <span class="text-base" :class="[crumb.path && 'font-medium']">
          {{ crumb.title }}
        </span>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
export default {
  name: 'CMSBreadcrumbs',

  props: {
    items: { type: Array, required: true },
  },

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Dashboard',
          path: '/cms/dashboard',
        },
        ...this.items,
      ],
    }
  },

  computed: {
    isAdmin() {
      return this.$store.getters['user/isAdmin']
    },
  },
}
</script>

<style></style>
