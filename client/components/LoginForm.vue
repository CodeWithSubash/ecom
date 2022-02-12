<template>
  <div class="w-full">
    <validation-observer v-slot="{ handleSubmit }">
      <form
        @submit.prevent="handleSubmit(handleLogin)"
        class="border rounded px-6 py-8"
      >
        <validation-provider name="E-mail" rules="required" v-slot="{ errors }">
          <base-input v-model.trim="user.username" class="mb-6">
            <template #label>
              Username <span class="text-red-600">*</span>
            </template>
            <template #error> {{ errors[0] }} </template>
          </base-input>
        </validation-provider>
        <validation-provider rules="required" v-slot="{ errors }">
          <base-input v-model.trim="user.password" type="password" class="mb-6">
            <template #label>
              Password <span class="text-red-600">*</span>
            </template>
            <template #error> {{ errors[0] }} </template>
          </base-input>
        </validation-provider>
        <base-button
          inverse
          on-hover
          type="submit"
          :loading="loading"
          class="capitalize mb-6"
        >
          Login
        </base-button>
        <nuxt-link to="/forgot-password">
          <div class="text-red-600">Forgot your password?</div>
        </nuxt-link>
      </form>
    </validation-observer>
  </div>
</template>

<script>
import { ValidationObserver, ValidationProvider } from 'vee-validate'

import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'LoginForm',

  components: {
    BreadCrumb,
    ValidationObserver,
    ValidationProvider,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Login',
          path: '/login',
        },
      ],
      user: {
        username: '',
        password: '',
      },
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    async handleLogin() {
      try {
        this.$store.commit('SET_LOADING', true)
        await this.$auth.loginWith('local', { data: this.user })
        this.$router.push('/home')
      } catch (err) {
        this.$store.dispatch('notification/setNotification', {
          type: 'error',
          title: 'Error',
          message: err.response.data.message,
        })
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },
  },
}
</script>

<style></style>
