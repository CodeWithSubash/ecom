<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
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
        <el-table-column type="index" label="S.N."> </el-table-column>
        <el-table-column
          prop="name"
          label="Product Name"
          sortable="custom"
          width="200"
        >
        </el-table-column>
        <el-table-column label="Category">
          <template #default="{ row }">
            {{ row.category.name }}
          </template>
        </el-table-column>
        <el-table-column label="Brand">
          <template #default="{ row }">
            {{ row.brand.name }}
          </template>
        </el-table-column>
        <el-table-column prop="mrp" label="MRP"> </el-table-column>
        <el-table-column prop="price" label="Price"> </el-table-column>
        <el-table-column
          prop="availableStock"
          label="Available Stock"
        ></el-table-column>
        <el-table-column
          align="right"
          fixed="right"
          label="Actions"
          width="180"
        >
          <template #default="{ row }">
            <div class="flex space-x-2">
              <el-button type="text" size="mini" @click="handleViewDetail(row)">
                View Detail
              </el-button>
              <el-button
                v-show="!row.deletedAt"
                type="text"
                size="mini"
                @click="showUpdateDialog(row)"
              >
                <span class="text-green-600"> Update Stock </span>
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

    <el-dialog
      title="Update Product Stock"
      :visible.sync="showStockUpdateDialog"
      width="30%"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <el-form v-if="productStock" label-position="top" v-loading="loading">
        <el-row>
          <el-col :span="24">
            <el-form-item label="Product">
              <el-input v-model="productStock.productName" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Stock Change">
              <el-input-number
                v-model="productStock.stockChange"
                :min="0"
                :step="10"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Change Type">
              <el-select v-model="productStock.changeType">
                <el-option
                  v-for="item in types"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="showStockUpdateDialog = false"
          size="small"
          :disabled="loading"
        >
          Cancel
        </el-button>
        <el-button
          type="success"
          @click="handleUpdateStock"
          size="small"
          :loading="loading"
        >
          Update
        </el-button>
      </span>
    </el-dialog>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import pagination from '~/mixins/pagination'

export default {
  name: 'Stocks',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'CMS | Stocks',
  },

  mixins: [crudActions, pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Stocks',
          path: '',
        },
      ],

      products: null,

      showStockUpdateDialog: false,
      productStock: null,
      types: ['INCREMENT', 'DECREMENT'],
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
    productList() {
      return this.products?.data ?? []
    },
    pagination() {
      return this.products?.paginationInfo ?? {}
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/product', {
      params: { ...this.params },
    })
    this.products = data
  },

  methods: {
    handleViewDetail({ id }) {
      this.$router.push(`/cms/stocks/${id}`)
    },

    showUpdateDialog(product) {
      this.productStock = {
        productId: product.id,
        productName: product.name,
        stockChange: 10,
        changeType: 'INCREMENT',
      }
      this.showStockUpdateDialog = true
    },

    async handleUpdateStock() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.post('/auth/productstock', {
          productId: this.productStock.productId,
          changeStock: this.productStock.stockChange,
          changeType: this.productStock.changeType,
        })
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Stock successfully updated',
          })
          this.showStockUpdateDialog = false
          this.$fetch()
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },
  },
}
</script>

<style></style>
