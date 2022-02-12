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
        :data="productList"
        stripe
        @sort-change="handleSortChange"
        style="width: 200%"
      >
        <el-table-column type="expand">
          <template #default="{ row }">
            <el-row :gutter="20">
              <el-col :span="16">
                <div class="font-medium mb-2">Description</div>
                <div class="border border-gray-200 bg-gray-50 p-4 rounded mb-4">
                  <div v-html="row.description"></div>
                </div>
                <div class="font-medium mb-2">Shipping & Delievery Detail</div>
                <div class="border border-gray-200 bg-gray-50 p-4 rounded mb-6">
                  <div v-html="row.shippingAndDelivery"></div>
                </div>
                <div class="font-medium mb-2">Payment & Return Detail</div>
                <div class="border border-gray-200 bg-gray-50 p-4 rounded mb-4">
                  <div v-html="row.paymentAndReturn"></div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="border border-gray-200 bg-gray-50 p-4 rounded mb-4">
                  <div class="flex mb-6">
                    <div>Conditon:</div>
                    <div class="font-medium ml-2">
                      {{ row.productCondition }}
                    </div>
                  </div>
                  <div class="flex mb-6">
                    <div>In Stock:</div>
                    <div class="font-medium ml-2">{{ row.availableStock }}</div>
                  </div>
                  <div class="flex mb-6">
                    <div>MRP:</div>
                    <div class="font-medium ml-2">TT$ {{ row.mrp }}</div>
                  </div>
                  <div class="flex">
                    <div>Wholesaler:</div>
                    <div class="font-medium ml-2">{{ row.wholesalerName }}</div>
                  </div>
                </div>

                <div class="font-medium mb-2">Tags</div>
                <div v-if="row.tags && row.tags.length" class="flex mb-6">
                  <div class="flex flex-wrap">
                    <el-tag
                      v-for="tag in row.tags"
                      :key="tag"
                      size="mini"
                      type="info"
                      class="m-1"
                    >
                      {{ tag }}
                    </el-tag>
                  </div>
                </div>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column type="index" label="S.N."> </el-table-column>
        <el-table-column label="Image" width="100">
          <template #default="{ row }">
            <el-image
              v-if="row.files"
              :src="getProductImages(row.files).preview"
              :preview-src-list="getProductImages(row.files).list"
              fit="cover"
              class="h-10 w-10 rounded border"
            >
            </el-image>
            <el-avatar
              v-else
              shape="square"
              src=" https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
            ></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Name" sortable="custom" width="200">
        </el-table-column>
        <el-table-column label="Category">
          <template #default="{ row }">
            {{ row.category.name }}
          </template>
        </el-table-column>
        <el-table-column label="Price">
          <template #default="{ row }">
            {{ `TT$ ${row.price}` }}
          </template>
        </el-table-column>
        <el-table-column label="Stock">
          <template #default="{ row }">
            <el-tag
              size="mini"
              :type="row.availableStock > 0 ? 'success' : 'danger'"
            >
              {{ row.availableStock > 0 ? 'In stock' : 'Out of Stock' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="Created on"></el-table-column>
        <el-table-column
          align="right"
          fixed="right"
          label="Actions"
          width="100"
        >
          <template #default="{ row }">
            <div class="flex space-x-2">
              <el-button type="text" @click="handleEdit(row)">Edit</el-button>
              <el-button
                v-show="!row.deletedAt"
                type="text"
                @click="handleDelete(row)"
              >
                <span class="text-red-600"> Delete </span>
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
  name: 'Products',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'CMS | Products',
  },

  mixins: [crudActions, pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Products',
          path: '',
        },
      ],
      products: null,
    }
  },

  computed: {
    productList() {
      return this.products?.data ?? []
    },
    pagination() {
      return this.products?.paginationInfo ?? {}
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/product/active', {
      params: { ...this.params },
    })
    this.products = data
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/products/create')
    },

    handleEdit({ id }) {
      this.$router.push(`/cms/products/${id}/edit`)
    },

    async handleDelete({ id }) {
      const status = await this.delete(`/auth/product/${id}`)
      if (status) this.$fetch()
    },

    getProductImages(files) {
      if (files) {
        const images = files && files.map((f) => f.fileDownloadUri)
        return { preview: images[0], list: images }
      }
    },
  },
}
</script>

<style></style>
