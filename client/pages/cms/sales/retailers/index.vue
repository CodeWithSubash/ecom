<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
      </div>
    </template>

    <!-- <table-search v-model="params.keyword" :onSearch="$fetch" /> -->

    <el-card>
      <el-table v-loading="$fetchState.pending" :data="retailerList" stripe>
        <!-- <el-table-column type="expand">
          <template #default="{ row }">
            <el-row :gutter="20">
              <el-col :span="8" class="mb-4">
                <div class="text-xs text-gray-500 underline">
                  Business Detail
                </div>
                <div>
                  <span>
                    Name:
                    {{
                      row.userRetailer.businessName ? row.userRetailer.businessName : '--'
                    }}
                  </span>
                </div>
                <div>
                  <span>
                    Address:
                    {{
                      row.userRetailer.businessAddress
                        ? row.userRetailer.businessAddress
                        : '--'
                    }}
                  </span>
                </div>
                <div>
                  <span>
                    Trading Status:
                    {{
                      row.userRetailer.tradingStatus
                        ? row.userRetailer.tradingStatus
                        : '--'
                    }}
                  </span>
                </div>
              </el-col>

              <el-col :span="8" class="mb-4">
                <div class="text-xs text-gray-500 underline">
                  Contact Detail
                </div>
                <div>
                  <span>
                    Landline:
                    {{ row.userRetailer.landline ? row.userRetailer.landline : '--' }}
                  </span>
                </div>
                <div>
                  <span>
                    Mobile:
                    {{ row.userRetailer.mobile ? row.userRetailer.mobile : '--' }}
                  </span>
                </div>
                <div>
                  <span>
                    Fax:
                    {{ row.userRetailer.fax ? row.userRetailer.fax : '--' }}
                  </span>
                </div>
              </el-col>

              <el-col :span="8" class="mb-4">
                <div class="text-xs text-gray-500 underline">Bank Detail</div>
                <div>
                  <span>
                    Name:
                    {{ row.userRetailer.bankName ? row.userRetailer.bankName : '--' }}
                  </span>
                </div>
                <div>
                  <span>
                    Number:
                    {{
                      row.userRetailer.bankerContactNumber
                        ? row.userRetailer.bankerContactNumber
                        : '--'
                    }}
                  </span>
                </div>
                <div>
                  <span>
                    Address:
                    {{
                      row.userRetailer.bankAddress ? row.userRetailer.bankAddress : '--'
                    }}
                  </span>
                </div>
                <div>
                  <span>
                    Contact Person:
                    {{
                      row.userRetailer.bankerContactNumber
                        ? row.userRetailer.bankerContactNumber
                        : '--'
                    }}
                  </span>
                </div>
              </el-col>
              <el-col :span="8" class="mb-4">
                <div class="mb-4">
                  <div class="text-xs text-gray-500 underline">
                    Business Registration Number:
                  </div>
                  {{
                    row.userRetailer.businessRegistrationNumber
                      ? row.userRetailer.businessRegistrationNumber
                      : '--'
                  }}
                </div>
                <div v-if="row.userRetailer.logo">
                  <div class="text-xs text-gray-500 underline">Logo</div>
                  <img
                    :src="row.userRetailer.logo"
                    alt="Business Logo"
                    class="h-40 w-full object-center object-cover rounded my-2"
                  />
                </div>
              </el-col>
              <el-col :span="8" class="mb-4">
                <div class="mb-4">
                  <div class="text-xs text-gray-500 underline">
                    Document ID Number:
                  </div>
                  {{
                    row.userRetailer.documentIdNumber
                      ? row.userRetailer.documentIdNumber
                      : '--'
                  }}
                </div>
                <div v-if="row.userRetailer.documentIdCertificate">
                  <div class="text-xs text-gray-500 underline">
                    Document ID Certificate
                  </div>
                  <img
                    :src="row.userRetailer.documentIdCertificate"
                    alt="Document ID Certificate"
                    class="h-40 w-full object-center object-cover rounded my-2"
                  />
                </div>
              </el-col>
              <el-col :span="8" class="mb-4">
                <div class="mb-4">
                  <div class="text-xs text-gray-500 underline">
                    VAT Registration Number:
                  </div>
                  {{
                    row.userRetailer.vatRegistrationNumber
                      ? row.userRetailer.vatRegistrationNumber
                      : '--'
                  }}
                </div>
                <div v-if="row.userRetailer.vatRegistrationCertificate">
                  <div class="text-xs text-gray-500 underline">
                    Vat Registration Certificate
                  </div>
                  <img
                    :src="row.userRetailer.vatRegistrationCertificate"
                    alt="VAT Registration Certificate"
                    class="h-40 w-full object-center object-cover rounded my-2"
                  />
                </div>
              </el-col>
              <el-col v-if="row.userRetailer.notes">
                <div class="text-xs text-gray-500 underline">Notes:</div>
                <p>{{ row.userRetailer.notes }}</p>
              </el-col>
            </el-row>
          </template>
        </el-table-column> -->
        <el-table-column type="index" label="S.N."> </el-table-column>
        <el-table-column prop="businessName" label="Retailer">
        </el-table-column>
        <el-table-column prop="email" label="Email"> </el-table-column>
        <el-table-column align="right" fixed="right" label="Actions">
          <template #default="{ row }">
            <el-button type="text" @click="handleRetailerCart(row)"
              >Manage Cart
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- <table-pagination
      :pagination="pagination"
      @pageChange="handlePageChange"
      @sizeChange="handleSizeChange"
    /> -->
  </with-header>
</template>

<script>
export default {
  name: 'SalesRetailers',

  layout: 'cms',

  middleware: 'sales',

  head: {
    title: 'CMS | Sales | Retailers',
  },

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Retailers',
          path: '',
        },
      ],
      retailers: null,
    }
  },

  computed: {
    retailerList() {
      return this.retailers?.assignedRetailers ?? []
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/user')
    this.retailers = data
  },

  methods: {
    handleRetailerCart({ userId }) {
      this.$router.push(`/cms/sales/retailers/${userId}/cart`)
    },
  },
}
</script>

<style></style>
