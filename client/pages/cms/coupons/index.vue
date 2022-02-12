<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
        <div v-if="isWholeSaler">
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
        :data="couponList"
        stripe
        @sort-change="handleSortChange"
        style="width: 100%"
      >
        <el-table-column type="index" label="S.N."> </el-table-column>
        <el-table-column prop="couponCode" label="Code" sortable="custom">
        </el-table-column>
        <el-table-column prop="couponType" label="Coupon Type">
        </el-table-column>
        <el-table-column prop="validFrom" label="Valid From"> </el-table-column>
        <el-table-column prop="validUntil" label="Valid Until">
        </el-table-column>
        <el-table-column label="Status">
          <template #default="{ row }">
            <el-tag size="mini" :type="row.redeemAllowed ? 'success' : 'info'">
              {{ row.redeemAllowed ? 'Redeemable' : 'Invalid' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="right" label="Actions" width="80">
          <template #default="{ row }">
            <div class="flex space-x-2">
              <el-button
                v-show="!row.deletedAt"
                type="text"
                :disabled="!row.redeemAllowed || !isWholeSaler"
                @click="handleDelete(row)"
              >
                <span class="text-red-600"> Dispose </span>
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
  name: 'Coupons',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'CMS | Coupons',
  },

  mixins: [crudActions, pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Coupons',
          path: '',
        },
      ],
      coupons: null,
    }
  },

  computed: {
    couponList() {
      return this.coupons?.data ?? []
    },
    pagination() {
      return this.coupons?.paginationInfo ?? {}
    },
    isWholeSaler() {
      return this.$store.getters['user/isWholeSaler']
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/coupon', {
      params: { ...this.params, orderBy: 'couponCode' },
    })
    this.coupons = data
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/coupons/create')
    },

    async handleDelete({ uuid }) {
      const status = await this.delete(`/auth/coupon/${uuid}`)
      if (status) this.$fetch()
    },
  },
}
</script>

<style></style>
