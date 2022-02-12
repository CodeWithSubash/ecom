<template>
  <div>
    <el-table :data="orders" stripe style="width: 100%">
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
  </div>
</template>

<script>
export default {
  name: 'OrderTable',

  props: {
    orders: {
      type: Array,
      default: () => [],
    },
  },

  methods: {
    handleOrderDetail({ id }) {
      this.$router.push(`/cms/orders/${id}`)
    },

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
