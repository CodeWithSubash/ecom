<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
      </div>
    </template>
    <el-card v-loading="$fetchState.pending">
      <template v-if="productDetail">
        <p>
          <span class="font-medium text-sm text-gray-500 mr-2">
            Product Name:
          </span>
          <span class=""> {{ productDetail.name }} </span>
        </p>
        <p>
          <span class="font-medium text-sm text-gray-500 mr-2">Category: </span>
          <span class=""> {{ productDetail.category.name }} </span>
        </p>
        <p>
          <span class="font-medium text-sm text-gray-500 mr-2">Brand: </span>
          <span class=""> {{ productDetail.brand.name }} </span>
        </p>
        <p>
          <span class="font-medium text-sm text-gray-500 mr-2">Price: </span>
          <span class=""> {{ productDetail.price }} </span>
        </p>
        <p>
          <span class="font-medium text-sm text-gray-500 mr-2">
            Closing Stock:
          </span>
          <span> -- </span>
        </p>
      </template>
    </el-card>

    <div class="w-full my-6">
      <p class="text-sm text-gray-500 mb-2">Filter Stocks</p>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="-"
        start-placeholder="Start date"
        end-placeholder="End date"
        :picker-options="pickerOptions"
        value-format="yyyy-MM-dd 00:00:00"
      >
      </el-date-picker>
    </div>

    <el-card>
      <el-table v-loading="$fetchState.pending" :data="stockList" stripe>
        <el-table-column type="index" label="S.N."> </el-table-column>
        <el-table-column
          prop="oldStock"
          label="Old Stock"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="changeStock"
          label="Change Stock"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="newStock"
          label="New Stock"
          align="center"
        ></el-table-column>
        <el-table-column prop="remarks" label="Remarks"></el-table-column>
        <el-table-column
          prop="createdAt"
          label="Date"
          align="center"
        ></el-table-column>
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
import pagination from '~/mixins/pagination'

export default {
  name: 'ProductStockDetail',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'Product Stock Detail ',
  },

  mixins: [pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Stocks',
          path: '/cms/stocks',
        },
        {
          title: 'Product Stock Detail',
          path: '',
        },
      ],
      productStocks: null,

      dateRange: null,

      startDate: null,
      endDate: null,

      pickerOptions: {
        // disabledDate: (time) => {
        //   if (this.dataRange && this.dataRange[0])
        //     return time.getTime() > this.dataRange[0]
        //   return time.getTime() > Date.now()
        // },
      },
    }
  },

  computed: {
    pagination() {
      return this.productStocks?.paginationInfo ?? {}
    },
    productDetail() {
      return this.stockList && this.stockList[0]?.product
    },
    stockList() {
      return this.productStocks && this.productStocks.data
    },

    filterDate() {
      if (this.startDate && this.endDate)
        return {
          startDate: this.startDate,
          endDate: this.endDate,
        }

      return {}
    },
  },

  watch: {
    dateRange: {
      handler(nv) {
        this.startDate = nv ? nv[0] : null
        this.endDate = nv ? nv[1] : null
      },
    },
    filterDate: {
      handler() {
        this.$fetch()
      },
    },
  },

  async fetch() {
    const { data } = await this.$axios.get(
      `auth/productstock/detail/${this.$route.params.productId}`,
      {
        params: {
          ...this.params,
          ...this.filterDate,
        },
      }
    )
    this.productStocks = data
  },
}
</script>

<style></style>
