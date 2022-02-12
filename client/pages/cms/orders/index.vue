<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
      </div>
    </template>

    <div class="flex justify-between">
      <table-search
        v-model="params.keyword"
        placeholder="Search by Invoice Number"
        :onSearch="$fetch"
      />
      <div class="flex space-x-4">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="Start date"
          end-placeholder="End date"
          value-format="yyyy-MM-dd 00:00:00"
        >
        </el-date-picker>
        <el-select v-model="status" @change="$fetch">
          <el-option
            v-for="status in deliveryStatuses"
            :key="status"
            :label="status"
            :value="status"
            size="small"
          >
          </el-option>
        </el-select>
      </div>
    </div>

    <el-card v-loading="$fetchState.pending">
      <el-table
        :data="orderList"
        stripe
        @sort-change="handleSortChange"
        style="width: 100%"
      >
        <el-table-column label="Invoice No">
          <template #default="{ row }">
            <span class="text-xs font-medium">
              {{ row.billNumber }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Billing Name">
          <template #default="{ row }">
            <div>{{ row.user.name }}</div>
            <div class="text-xs text-gray-400">
              {{ row.user.userRetailer.businessName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Date">
          <template #default="{ row }">
            <span v-if="row.createdAt">{{
              row.createdAt | moment('MMM Do, YYYY')
            }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="Total">
          <template #default="{ row }">
            {{ `$TT ${row.totalAmount}` }}
          </template>
        </el-table-column>
        <el-table-column label="Status">
          <template #default="{ row }">
            <div
              class="text-center text-xs rounded inline-block px-2 py-1"
              :class="`bg-${getStatusColor(
                row.deliveryStatus
              )}-400 text-${getStatusColor(row.deliveryStatus)}-50`"
            >
              {{ row.deliveryStatus }}
            </div>
          </template>
        </el-table-column>
        <el-table-column align="right" label="Actions">
          <template #default="{ row }">
            <el-button type="text" @click="handleOrderDetail(row)">
              <span>View Details</span>
            </el-button>
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
import pagination from '~/mixins/pagination'

export default {
  name: 'Orders',

  layout: 'cms',

  head: {
    title: 'CMS | Orders',
  },

  mixins: [pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Orders',
          path: '',
        },
      ],
      orders: null,

      status: 'ALL',
      deliveryStatuses: [
        'ALL',
        'PLACED',
        'REVIEWING',
        'PACKED',
        'SHIPPED',
        'DELIVERED',
      ],

      dateRange: null,
      startDate: null,
      endDate: null,
    }
  },

  computed: {
    orderList() {
      return this.orders?.data ?? []
    },
    pagination() {
      return this.orders?.paginationInfo ?? {}
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
    const { data } = await this.$axios.get('/auth/order', {
      params: {
        ...this.params,
        ...this.filterDate,
        deliveryStatus: this.status,
      },
    })
    this.orders = data
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/orders/create')
    },

    handleOrderDetail({ id }) {
      this.$router.push(`/cms/orders/${id}`)
    },

    handleStatusChange() {},

    getStatusColor(status) {
      const lookUp = {
        PLACED: 'gray',
        REVIEWING: 'yellow',
        PACKED: 'indigo',
        SHIPPED: 'blue',
        DELIVERED: 'green',
      }
      return lookUp[status]
    },
  },
}
</script>

<style></style>
