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
    <el-card>
      <el-table
        v-loading="$fetchState.pending"
        :data="userList"
        stripe
        class="w-full"
      >
        <el-table-column type="index" label="S.N."> </el-table-column>

        <el-table-column prop="name" label="Name"> </el-table-column>
        <el-table-column prop="username" label="Username"> </el-table-column>
        <el-table-column prop="email" label="Email" width="250">
        </el-table-column>
        <el-table-column prop="role" label="Role"> </el-table-column>
        <el-table-column label="Status">
          <template #default="{ row }">
            <el-tag v-if="!row.isEmailVerified" type="warning" size="mini"
              >Unverified</el-tag
            >
            <el-tag v-else :type="row.enabled ? 'success' : 'info'" size="mini">
              {{ row.enabled ? 'Active' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions">
          <template #default="{ row }">
            <div class="flex space-x-2">
              <el-button type="text" @click="handleEdit(row)">Edit</el-button>
              <el-button type="text" @click="handleDelete(row)">
                Delete
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'

export default {
  name: 'Users',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'CMS | Users',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Users',
          path: '',
        },
      ],
      userList: [],
    }
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/user/getAll')
    this.userList = data.filter((user) => user.id !== this.$auth.user.id)
  },

  methods: {
    handleCreate() {
      this.$router.push('/cms/users/create')
    },

    handleEdit({ id }) {
      this.$router.push(`/cms/users/${id}/edit`)
    },

    async handleDelete({ id }) {
      const status = await this.delete(`/auth/user/${id}`)
      if (status) this.$fetch()
    },
  },
}
</script>

<style></style>
