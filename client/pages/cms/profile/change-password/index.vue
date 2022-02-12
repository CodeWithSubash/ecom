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
            @click="submitForm('passwordForm')"
          >
            Update
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/profile')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>

    <el-card>
      <el-form
        v-loading="loading"
        :rules="rules"
        ref="passwordForm"
        :model="credentials"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item prop="oldPassword" label="Old Password">
              <el-input
                v-model.trim="credentials.oldPassword"
                show-password
              ></el-input>
            </el-form-item>
            <el-form-item prop="password" label="New Password">
              <el-input
                v-model.trim="credentials.password"
                show-password
              ></el-input>
            </el-form-item>
            <el-form-item prop="rePassword" label="Confirm Password">
              <el-input
                v-model.trim="credentials.rePassword"
                show-password
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </with-header>
</template>

<script>
export default {
  name: 'CMSChangePassword',

  layout: 'cms',

  head: {
    title: 'Profile | Change Password',
  },

  data() {
    return {
      breadcrumbs: [
        { title: 'Profile', path: '/cms/profile' },
        { title: 'Change Password', path: '' },
      ],

      credentials: {
        oldPassword: '',
        password: '',
        rePassword: '',
      },

      rules: {
        oldPassword: [
          {
            required: true,
            message: 'Please enter old password',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: 'Please enter new name',
            trigger: 'blur',
          },
        ],
        rePassword: [
          {
            required: true,
            message: 'Please confrirm new password',
            trigger: 'blur',
          },
        ],
      },
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
          this.handleChangePassword()
        }
      })
    },

    async handleChangePassword() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.put(
          '/auth/user/changePassword',
          this.credentials
        )
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Password successfully changed',
          })
          this.$router.push('/')
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
  },
}
</script>

<style></style>
