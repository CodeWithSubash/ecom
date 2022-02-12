<template>
  <div class="mt-5">
    <el-pagination
      v-if="pagination"
      :current-page.sync="paginationSync"
      :total="pagination.totalItems"
      :page-size="parseInt(pagination.size)"
      :page-sizes="[5, 10, 20, 50, 100]"
      layout="total,sizes, prev, pager, next "
      class="my-2"
      @current-change="$emit('pageChange', $event)"
      @size-change="$emit('sizeChange', $event)"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: 'TablePagination',

  props: {
    pagination: {
      type: Object,
      required: true,
    },
  },

  computed: {
    paginationSync: {
      get() {
        return this.pagination.currentPage + 1
      },
      set(val) {
        this.pagination.currentPage = val - 1
      },
    },
  },
}
</script>

<style></style>
