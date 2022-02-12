<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
        <div class="flex space-x-2">
          <el-button
            type="success"
            size="medium"
            :disabled="loading"
            @click="submitForm('userForm')"
          >
            Save
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/users')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="loading"
        :rules="rules"
        ref="userForm"
        :model="user"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="name" label="Name">
              <el-input v-model="user.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="email" label="Email">
              <el-input v-model.trim="user.email" type="email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="username" label="Username">
              <el-input v-model.trim="user.username"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item prop="role" label="Role">
              <el-select v-model.trim="user.role" placeholder="Select Role">
                <el-option
                  v-for="item in roles"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import User from '~/models/user'

export default {
  name: 'CreateUser',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'Create User ',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Users',
          path: '/cms/users',
        },
        {
          title: 'Create User',
          path: '',
        },
      ],

      roles: User.roles,
      rules: User.validationRules,
      user: new User(),
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.handleCreate()
        }
      })
    },

    async handleCreate() {
      const success = await this.create({
        url: '/auth/user',
        payload: this.user,
      })
      if (success) this.$router.push('/cms/users')
    },
  },
}
</script>

<style></style>
