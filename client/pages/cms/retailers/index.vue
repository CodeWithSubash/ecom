<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
        <el-button type="primary" size="medium" @click="handleCreate">
          <i class="el-icon-plus mr-2"></i>
          Add New
        </el-button>
      </div>
    </template>

    <table-search v-model="params.keyword" :onSearch="$fetch" />

    <el-card>
      <el-table
        v-loading="$fetchState.pending"
        :data="retailerList"
        stripe
        @sort-change="handleSortChange"
      >
        <el-table-column type="expand">
          <template #default="{ row }">
            <el-row :gutter="20">
              <el-col :span="8" class="mb-4">
                <div class="font-medium mb-2">Business Detail</div>
                <div class="border border-gray-200 p-4 rounded bg-gray-50 h-32">
                  <div>
                    <span>
                      Name:
                      <span class="font-medium">
                        {{
                          row.userRetailer.businessName
                            ? row.userRetailer.businessName
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Established:
                      <span>
                        {{
                          row.userRetailer.dateOfCorporation
                            ? row.userRetailer.dateOfCorporation
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Address:
                      <span class="font-medium">
                        {{
                          row.userRetailer.businessAddress
                            ? row.userRetailer.businessAddress
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Trading Status:
                      <span class="font-medium">
                        {{
                          row.userRetailer.tradingStatus
                            ? row.userRetailer.tradingStatus
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                </div>
              </el-col>

              <el-col :span="8" class="mb-4">
                <div class="font-medium mb-2">Contact Detail</div>
                <div class="border border-gray-200 p-4 rounded bg-gray-50 h-32">
                  <div>
                    <span>
                      Landline:
                      <span class="font-medium">
                        {{
                          row.userRetailer.landline
                            ? row.userRetailer.landline
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Mobile:
                      <span class="font-medium">
                        {{
                          row.userRetailer.mobile
                            ? row.userRetailer.mobile
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Fax:
                      <span class="font-medium">
                        {{ row.userRetailer.fax ? row.userRetailer.fax : '--' }}
                      </span>
                    </span>
                  </div>
                </div>
              </el-col>

              <el-col :span="8" class="mb-4">
                <div class="font-medium mb-2">Bank Detail</div>
                <div class="border border-gray-200 p-4 rounded bg-gray-50 h-32">
                  <div>
                    <span>
                      Name:
                      <span class="font-medium">
                        {{
                          row.userRetailer.bankName
                            ? row.userRetailer.bankName
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Number:
                      <span class="font-medium">
                        {{
                          row.userRetailer.bankerContactNumber
                            ? row.userRetailer.bankerContactNumber
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Address:
                      <span class="font-medium">
                        {{
                          row.userRetailer.bankAddress
                            ? row.userRetailer.bankAddress
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                  <div>
                    <span>
                      Contact Person:
                      <span class="font-medium">
                        {{
                          row.userRetailer.bankerContactNumber
                            ? row.userRetailer.bankerContactNumber
                            : '--'
                        }}
                      </span>
                    </span>
                  </div>
                </div>
              </el-col>
              <el-col :span="24" :lg="8" class="mb-8">
                <div class="mb-8">
                  <div class="font-medium">Business Registration Number:</div>
                  {{
                    row.userRetailer.businessRegistrationNumber
                      ? row.userRetailer.businessRegistrationNumber
                      : '--'
                  }}
                </div>
                <div v-if="row.userRetailer.logo">
                  <div class="flex items-center space-x-6">
                    <div class="font-medium">Logo</div>
                    <a :href="row.userRetailer.logo">
                      <i class="bx bx-download bx-xs mt-1 text-green-500"></i>
                    </a>
                  </div>
                  <div class="h-40 w-full">
                    <el-image
                      :src="row.userRetailer.logo"
                      :preview-src-list="[row.userRetailer.logo]"
                      alt="Business Logo"
                      fit="contain"
                      class="h-full w-full bg-white rounded my-2"
                    >
                      <div
                        slot="placeholder"
                        class="h-full w-full grid place-content-center"
                      >
                        <div class="text-sm text-gray-500">Loading...</div>
                      </div>
                    </el-image>
                  </div>
                </div>
              </el-col>
              <el-col :span="24" :lg="8" class="mb-8">
                <div class="mb-8">
                  <div class="font-medium">Document ID Number:</div>
                  {{
                    row.userRetailer.documentIdNumber
                      ? row.userRetailer.documentIdNumber
                      : '--'
                  }}
                </div>
                <div v-if="row.userRetailer.documentIdCertificate">
                  <div class="flex items-center space-x-6">
                    <div class="font-medium">Document ID Certificate</div>
                    <a :href="row.userRetailer.documentIdCertificate">
                      <i class="bx bx-download bx-xs mt-1 text-green-500"></i>
                    </a>
                  </div>
                  <div class="h-40 w-full">
                    <el-image
                      :src="row.userRetailer.documentIdCertificate"
                      :preview-src-list="[
                        row.userRetailer.documentIdCertificate,
                      ]"
                      fit="contain"
                      alt="Document ID Certificate"
                      class="h-full w-full bg-white rounded my-2"
                    >
                      <div
                        slot="placeholder"
                        class="h-full w-full grid place-content-center"
                      >
                        <div class="text-sm text-gray-500">Loading...</div>
                      </div>
                    </el-image>
                  </div>
                </div>
              </el-col>
              <el-col :span="24" :lg="8" class="mb-8">
                <div class="mb-8">
                  <div class="font-medium">VAT Registration Number:</div>
                  {{
                    row.userRetailer.vatRegistrationNumber
                      ? row.userRetailer.vatRegistrationNumber
                      : '--'
                  }}
                </div>
                <div v-if="row.userRetailer.vatRegistrationCertificate">
                  <div class="flex items-center space-x-6">
                    <div class="font-medium">Vat Registration Certificate</div>
                    <a :href="row.userRetailer.vatRegistrationCertificate">
                      <i class="bx bx-download bx-xs mt-1 text-green-500"></i>
                    </a>
                  </div>

                  <div class="h-40 w-full">
                    <el-image
                      :src="row.userRetailer.vatRegistrationCertificate"
                      :preview-src-list="[
                        row.userRetailer.vatRegistrationCertificate,
                      ]"
                      alt="VAT Registration Certificate"
                      fit="contain"
                      class="h-full w-full bg-white rounded my-2"
                    >
                      <div
                        slot="placeholder"
                        class="h-full w-full grid place-content-center"
                      >
                        <div class="text-sm text-gray-500">Loading...</div>
                      </div>
                    </el-image>
                  </div>
                </div>
              </el-col>
              <el-col v-if="row.userRetailer.notes">
                <div class="font-medium mb-2">Notes:</div>
                <p>{{ row.userRetailer.notes }}</p>
              </el-col>
            </el-row>
            <div
              v-if="!row.userRetailer.deletedAt && !row.userRetailer.status"
              class="flex justify-end"
            >
              <el-button
                size="mini"
                type="success"
                @click="handleKycStatusUpdate(row.userRetailer.id, true)"
              >
                Aprrove
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="rejectableKyc = row.userRetailer"
              >
                Reject
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column type="index" label="S.N."> </el-table-column>
        <el-table-column prop="username" label="Username"> </el-table-column>
        <el-table-column prop="email" label="Email"> </el-table-column>
        <el-table-column prop="userRetailer.businessName" label="Business Name">
        </el-table-column>
        <el-table-column label="KYC Status">
          <template #default="{ row }">
            <el-tag
              size="mini"
              :type="getStatusColor(getKycStatus(row.userRetailer))"
            >
              {{ getKycStatus(row.userRetailer) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="right"
          fixed="right"
          label="Actions"
          width="100"
        >
          <template #default="{ row }">
            <div class="flex space-x-2">
              <el-button
                type="text"
                :disabled="
                  isWholeSaler &&
                  (row.ownerUserId == null || row.ownerUserId == 0)
                "
                @click="handleEdit(row)"
                >Edit</el-button
              >
              <el-button
                v-show="!row.userRetailer.deletedAt"
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

    <el-dialog
      title="Reject KYC"
      :visible.sync="showRejectDialog"
      width="30%"
      :show-close="false"
    >
      <div class="mb-1">Notes:</div>
      <el-input
        v-model="rejectNote"
        type="textarea"
        rows="4"
        placeholder="Leave a note about the reason for rejection of the KYC"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="rejectableKyc = null">
          Cancel
        </el-button>
        <el-button
          type="danger"
          size="mini"
          :disabled="!rejectNote || !rejectNote.length"
          @click="handleKycStatusUpdate(rejectableKyc.id, false, rejectNote)"
        >
          Confirm
        </el-button>
      </span>
    </el-dialog>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import pagination from '~/mixins/pagination'

export default {
  name: 'Retailers',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'CMS | Retailers',
  },

  mixins: [crudActions, pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Retailers',
          path: '',
        },
      ],
      retailers: null,

      rejectNote: null,
      rejectableKyc: null,
    }
  },

  computed: {
    retailerList() {
      return this.retailers?.data ?? []
    },
    pagination() {
      return this.retailers?.paginationInfo ?? {}
    },

    showRejectDialog: {
      get() {
        return !!this.rejectableKyc
      },
      set() {
        this.rejectableKyc = null
      },
    },

    isWholeSaler() {
      return this.$store.getters['user/isWholeSaler']
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('auth/user/getAll/RETAILER', {
      params: { ...this.params },
    })
    this.retailers = data
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/retailers/create')
    },

    handleEdit({ id }) {
      this.$router.push(`/cms/retailers/${id}/edit`)
    },

    async handleDelete({ id }) {
      const status = await this.delete(`/auth/user/${id}`)
      if (status) this.$fetch()
    },

    async handleKycStatusUpdate(id, status, notes = null) {
      this.rejectableKyc = null

      try {
        this.$store.commit('SET_LOADING', true)
        const res = await this.$axios.put(`/auth/userRetailer/status/${id}`, {
          status,
          notes,
        })
        if (res.status === 200) {
          this.$fetch()
          this.$store.dispatch('notification/setNotification', {
            message: 'KYC status updated',
          })
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

    getKycStatus({ deletedAt, status }) {
      if (deletedAt && status) return 'Incomplete'
      if (!deletedAt && !status) return 'Pending'
      if (deletedAt && !status) return 'Rejected'
      if (!deletedAt && status) return 'Verified'
      return
    },

    getStatusColor(status) {
      const statuses = {
        verified: 'success',
        pending: 'warning',
        rejected: 'danger',
        incomplete: 'info',
      }

      return statuses[status.toLowerCase()]
    },
  },
}
</script>

<style></style>
