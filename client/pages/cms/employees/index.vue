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
        v-loading="$fetchState.pending || loading"
        :data="employeeList"
        stripe
        @sort-change="handleSortChange"
        style="width: 200%"
      >
        <el-table-column type="expand">
          <template #default="{ row }">
            <el-row :gutter="20">
              <el-col :span="12" class="mb-4">
                <div class="font-medium mb-2">Address Detail</div>
                <div class="p-4 rounded border border-gray-200 bg-gray-50">
                  <el-row>
                    <div class="mb-4">
                      <div>
                        Street:
                        <span>
                          {{
                            row.userEmployee.street
                              ? row.userEmployee.street
                              : '--'
                          }}
                        </span>
                      </div>
                    </div>
                    <div class="mb-4">
                      <div>
                        Street 2:
                        <span>
                          {{
                            row.userEmployee.street2
                              ? row.userEmployee.street2
                              : '--'
                          }}
                        </span>
                      </div>
                    </div>
                    <div class="mb-4">
                      <div>
                        State :
                        <span>
                          {{
                            row.userEmployee.state
                              ? row.userEmployee.state
                              : '--'
                          }}
                        </span>
                      </div>
                    </div>
                    <div class="mb-4">
                      <div>
                        City :
                        <span>
                          {{
                            row.userEmployee.city ? row.userEmployee.city : '--'
                          }}
                        </span>
                      </div>
                    </div>
                    <div>
                      <div>
                        Zip code :
                        <span>
                          {{
                            row.userEmployee.zipCode
                              ? row.userEmployee.zipCode
                              : '--'
                          }}
                        </span>
                      </div>
                    </div>
                  </el-row>
                </div>
              </el-col>
              <el-col :span="12" class="mb-4">
                <div class="font-medium mb-2">Contact Detail</div>
                <div class="p-4 rounded border border-gray-200 bg-gray-50">
                  <div class="mb-4">
                    <span
                      >Phone:
                      {{
                        row.userEmployee.phone ? row.userEmployee.phone : '--'
                      }}
                    </span>
                  </div>
                  <div class="mb-4">
                    <span
                      >Alternative Phone:
                      {{
                        row.userEmployee.alternativePhone
                          ? row.userEmployee.alternativePhone
                          : '--'
                      }}
                    </span>
                  </div>
                  <div class="mb-4">
                    <span
                      >Mobile:
                      {{
                        row.userEmployee.mobile ? row.userEmployee.mobile : '--'
                      }}
                    </span>
                  </div>
                  <div class="mb-4">
                    <span
                      >Alternative Mobile:
                      {{
                        row.userEmployee.alternativeMobile
                          ? row.userEmployee.alternativeMobile
                          : '--'
                      }}
                    </span>
                  </div>
                </div>
              </el-col>
            </el-row>
            <template v-if="row.assignedRetailers.length">
              <el-row :gutter="20">
                <el-col :span="24" :lg="12">
                  <div class="font-medium mb-2">Assigned Retailers</div>
                  <div class="border border-gray-200 rounded px-6 py-4">
                    <ol class="list-decimal">
                      <li
                        v-for="(retailer, index) in row.assignedRetailers"
                        :key="retailer.id"
                      >
                        <span class="font-medium text-red-500 ml-1">
                          {{ retailer.businessName }}
                        </span>
                      </li>
                    </ol>
                  </div>
                </el-col>
              </el-row>
            </template>
          </template>
        </el-table-column>
        <el-table-column type="index" label="S.N."> </el-table-column>
        <el-table-column prop="name" label="Name"> </el-table-column>
        <el-table-column prop="username" label="Username"> </el-table-column>
        <el-table-column prop="email" label="Email"> </el-table-column>
        <el-table-column prop="role" label="Role"> </el-table-column>
        <el-table-column prop="userEmployee.designation" label="Designation">
        </el-table-column>
        <el-table-column align="center">
          <template #default="{ row }">
            <el-button
              v-if="row.role === 'SALES'"
              type="success"
              size="mini"
              plain
              @click="selectedEmployee = row"
              >Assign Retailers
            </el-button>
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
              <el-button type="text" @click="handleEdit(row)">Edit</el-button>
              <el-button
                v-show="row.enabled"
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
      title="Assign Retailers"
      :visible.sync="showRetailerSelect"
      :show-close="false"
      width="30%"
    >
      <template v-if="selectedEmployee">
        <div class="mb-4">
          <span class="text-gray-400">Assign Retailers to </span>
          <span> {{ selectedEmployee.name }}</span>
        </div>
        <el-select
          v-model="selectedEmployee.retailers"
          multiple
          placeholder="Select"
          style="width: 100%"
        >
          <el-option
            v-for="item in retailerList"
            :key="item.id"
            :label="item.value"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </template>
      <span slot="footer" class="dialog-footer">
        <el-button
          size="mini"
          :disabled="loading"
          @click="selectedEmployee = null"
        >
          Cancel
        </el-button>
        <el-button
          type="success"
          size="mini"
          :loading="loading"
          @click="handleAssignRetailers(selectedEmployee)"
        >
          Assign
        </el-button>
      </span>
    </el-dialog>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import pagination from '~/mixins/pagination'

export default {
  name: 'Employees',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'CMS | Employees',
  },

  mixins: [crudActions, pagination],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Employees',
          path: '',
        },
      ],
      employees: null,

      retailerList: [],
      selectedEmployee: null,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },

    isWholeSaler() {
      return this.$store.getters['user/isWholeSaler']
    },

    allEmployees() {
      return this.employees?.data ?? []
    },

    salesPersonOnly() {
      return this.allEmployees.filter((e) => e.role === 'SALES')
    },

    employeeList() {
      return this.isWholeSaler ? this.salesPersonOnly : this.allEmployees
    },

    pagination() {
      return this.employees?.paginationInfo ?? {}
    },

    showRetailerSelect: {
      get() {
        return !!this.selectedEmployee
      },
      set() {
        this.selectedEmployee = null
      },
    },
  },

  async fetch() {
    const employeeRes = await this.$axios.get('/auth/user/getAll/EMPLOYEE', {
      params: { ...this.params },
    })
    const retailerRes = await this.$axios.get('/auth/user/list/retailer')

    this.employees = employeeRes.data
    this.retailerList = retailerRes.data
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/employees/create')
    },

    handleEdit({ id }) {
      this.$router.push(`/cms/employees/${id}/edit`)
    },

    async handleDelete({ id }) {
      const status = await this.delete(`/auth/user/${id}`)
      if (status) this.$fetch()
    },

    async handleAssignRetailers(payload) {
      try {
        this.$store.commit('SET_LOADING', false)
        const { status } = await this.$axios.put(
          `/auth/user/assignEmployeeRetailers/${payload.id}`,
          payload.retailers
        )
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Retailers successfully assigned',
          })
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
        this.selectedEmployee = null
      }
    },
  },
}
</script>

<style></style>
