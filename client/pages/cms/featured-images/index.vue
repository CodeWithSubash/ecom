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

    <el-table
      v-loading="$fetchState.pending || loading"
      :data="featuredImageList"
      stripe
      @sort-change="handleSortChange"
      style="width: 100%"
    >
      <el-table-column type="index" label="S.N."> </el-table-column>
      <el-table-column label="Image" width="100">
        <template #default="{ row }">
          <el-image
            v-if="row.files"
            :src="row.files.fileDownloadUri"
            :preview-src-list="[...row.files.fileDownloadUri]"
            class="h-12 w-12 object-center object-cover rounded"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="Title" sortable="custom">
      </el-table-column>
      <el-table-column prop="subTitle" label="Subtitle"> </el-table-column>
      <el-table-column prop="featuredType" label="Featured Type">
      </el-table-column>
      <el-table-column label="Status">
        <template #default="{ row }">
          <el-tag size="mini" :type="!row.deletedAt ? 'success' : 'danger'">
            {{ row.deletedAt ? 'Unpublished' : 'Published' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="Actions">
        <template #default="{ row }">
          <div class="flex space-x-4">
            <el-button type="text" @click="togglePublication(row)">
              <span :class="row.deletedAt ? 'text-green-500' : 'text-red-300'"
                >{{ row.deletedAt ? 'Publish' : 'Unpublish' }}
              </span>
            </el-button>
            <el-button type="text" @click="handleEdit(row)">Edit</el-button>
            <el-button type="text" @click="handleDelete(row)">
              <span class="text-red-600"> Delete </span>
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

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
  name: 'FeaturedImages',

  layout: 'cms',

  middleware: 'admin',

  head: {
    title: 'CMS | Featured Images',
  },

  mixins: [crudActions, pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Featured Images',
          path: '',
        },
      ],

      featuredImages: [],
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
    featuredImageList() {
      return this.featuredImages?.data ?? []
    },
    pagination() {
      return this.featuredImages?.paginationInfo ?? {}
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('auth/featured', {
      params: { ...this.params },
    })
    this.featuredImages = data
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/featured-images/create')
    },

    handleEdit({ id }) {
      this.$router.push(`/cms/featured-images/${id}/edit`)
    },

    async handleDelete({ id }) {
      const status = await this.delete(`/auth/featured/${id}`)
      if (status) this.$fetch()
    },

    async togglePublication({ id }) {
      try {
        const { status } = await this.$axios.put(
          `/auth/featured/published/${id}`
        )
        if (status === 200) {
          this.$store.dispatch('notification/setNotifcation', {
            message: 'Publication status updated',
          })
          this.$fetch()
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
      }
    },
  },
}
</script>

<style></style>
