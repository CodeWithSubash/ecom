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
            v-for="status in paymentStatus"
            :key="status"
            :label="status"
            :value="status"
            size="small"
          >
          </el-option>
        </el-select>
      </div>
    </div>

    <el-card>
      <el-table
        v-loading="$fetchState.pending"
        :data="invoiceList"
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
        <el-table-column prop="retailerName" label="Customer">
        </el-table-column>
        <el-table-column label="Address">
          <template #default="{ row }">
            {{ !row.billingAddress ? '--' : row.billingAddress }}
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
                row.paymentStatus
              )}-400 text-${getStatusColor(row.paymentStatus)}-50`"
            >
              {{ row.paymentStatus }}
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
        <el-table-column align="right" label="Actions">
          <template #default="{ row }">
            <el-button type="text" @click="handleInvoiceDetail(row)">
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
  name: 'Invoices',

  layout: 'cms',

  head: {
    title: 'CMS | Invoices',
  },

  mixins: [pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Invoices',
          path: '',
        },
      ],
      invoices: null,

      status: 'ALL',
      paymentStatus: ['ALL', 'PENDING', 'PAID'],

      dateRange: null,
      startDate: null,
      endDate: null,
    }
  },

  computed: {
    invoiceList() {
      return this.invoices?.data ?? []
    },
    pagination() {
      return this.invoices?.paginationInfo ?? {}
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
    const { data } = await this.$axios.get('/auth/invoice', {
      params: {
        ...this.params,
        ...this.filterDate,
        paymentStatus: this.status,
      },
    })
    this.invoices = data
  },

  methods: {
    handleInvoiceDetail({ id }) {
      this.$router.push(`/cms/invoices/${id}`)
    },

    getStatusColor(status) {
      const lookUp = {
        PENDING: 'yellow',
        PAID: 'green',
      }
      return lookUp[status]
    },
  },
}
</script>

<style></style>
