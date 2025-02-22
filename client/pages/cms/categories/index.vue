<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
        <div>
          <el-button type="primary" size="medium" @click="handleCreate">
            <i class="el-icon-plus mr-2"></i>
            Add New
          </el-button>
        </div>
      </div>
    </template>

    <table-search v-model="params.keyword" :onSearch="$fetch" />

    <el-card>
      <el-table
        v-loading="$fetchState.pending"
        :data="categoryList"
        stripe
        @sort-change="handleSortChange"
        style="width: 100%"
      >
        <el-table-column type="index" label="S.N."> </el-table-column>

        <el-table-column label="Logo" width="100">
          <template #default="{ row }">
            <el-avatar
              shape="square"
              :src="
                row.logoPath
                  ? row.logoPath
                  : 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
              "
            ></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Name" sortable="custom">
        </el-table-column>
        <el-table-column label="Parent Category">
          <template #default="{ row }">
            {{ row.parentName ? row.parentName : '--' }}
          </template>
        </el-table-column>
        <el-table-column label="Status">
          <template #default="{ row }">
            <el-tag :type="!row.deletedAt ? 'success' : 'info'" size="mini">
              {{ !row.deletedAt ? 'Active' : 'Inactive' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="right" label="Actions" width="100">
          <template #default="{ row }">
            <div class="flex space-x-2">
              <el-button type="text" @click="handleEdit(row)">Edit</el-button>
              <el-button
                v-show="!row.deletedAt"
                type="text"
                @click="handleDelete(row)"
              >
                Delete
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <table-pagination
      :pagination="pagination"
      @pageChange="handlePageChange"
      @sizeChange="handleSizeChange"
    />
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import pagination from '~/mixins/pagination'

export default {
  name: 'Categories',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'CMS | Categories',
  },

  mixins: [crudActions, pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Categories',
          path: '',
        },
      ],
      categories: null,
    }
  },

  computed: {
    categoryList() {
      return this.categories?.data ?? []
    },
    pagination() {
      return this.categories?.paginationInfo ?? {}
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/category/active', {
      params: { ...this.params },
    })
    this.categories = data
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/categories/create')
    },

    handleEdit({ id }) {
      this.$router.push(`/cms/categories/${id}/edit`)
    },

    async handleDelete({ id }) {
      const status = await this.delete(`/auth/category/${id}`)
      if (status) this.$fetch()
    },

    getParentCategory(parentId) {
      return this.categoryList.find((cat) => cat.id == parentId)?.name ?? '--'
    },
  },
}
</script>

<style></style>
