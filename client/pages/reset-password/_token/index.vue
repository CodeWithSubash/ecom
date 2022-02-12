<template>
  <div class="default-container mt-24">
    <div class="w-full lg:w-1/2 mx-auto py-16">
      <div class="text-2xl lg:text-3xl font-semibold mb-10">Reset Password</div>
      <validation-observer v-slot="{ handleSubmit }">
        <form
          @submit.prevent="handleSubmit(handlePassswordReset)"
          class="border rounded px-6 py-8"
        >
          <validation-provider rules="required|email" v-slot="{ errors }">
            <base-input v-model.trim="user.email" class="mb-6">
              <template #label>
                Email <span class="text-red-600">*</span>
              </template>
              <template #error> {{ errors[0] }} </template>
            </base-input>
          </validation-provider>
          <validation-provider rules="required" v-slot="{ errors }">
            <base-input
              v-model.trim="user.password"
              type="password"
              class="mb-6"
            >
              <template #label>
                Password <span class="text-red-600">*</span>
              </template>
              <template #error> {{ errors[0] }} </template>
            </base-input>
          </validation-provider>
          <validation-provider rules="required" v-slot="{ errors }">
            <base-input
              v-model.trim="user.rePassword"
              type="password"
              class="mb-6"
            >
              <template #label>
                Confirm Password <span class="text-red-600">*</span>
              </template>
              <template #error> {{ errors[0] }} </template>
            </base-input>
          </validation-provider>
          <base-button
            inverse
            on-hover
            type="submit"
            :loading="loading"
            class="capitalize"
          >
            Reset Password
          </base-button>
        </form>
      </validation-observer>
    </div>
  </div>
</template>

<script>
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
  name: 'ResetPassword',

  middleware: 'guest',

  head: {
    title: 'KMPL | Reset Password',
  },

  components: {
    ValidationObserver,
    ValidationProvider,
  },

  mounted() {
    this.token = this.$route.params.token
  },

  data() {
    return {
      token: null,
      user: {},
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    async handlePassswordReset() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.post(
          `/secured/requestForgotPassword/${this.token}/changePassword`,
          this.user
        )
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Password reset successful!',
          })
          this.$router.push('/login')
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          type: 'error',
          title: 'Error',
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
