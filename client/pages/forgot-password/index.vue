<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <div class="flex flex-col lg:flex-row">
        <div class="mb-10 lg:mb-0">
          <div class="text-2xl lg:text-3xl font-semibold mb-4">
            Forgot Password
          </div>
          <p class="text-gray-600 mb-6">
            Please provide the same email you registerd with, so that we can
            send reset link.
          </p>
          <validation-observer
            v-slot="{ handleSubmit }"
            class="w-full lg:w-1/2"
          >
            <form
              @submit.prevent="handleSubmit(handleForgotPassword)"
              class="border rounded px-6 py-8"
            >
              <validation-provider rules="required|email" v-slot="{ errors }">
                <base-input v-model.trim="email" class="mb-6">
                  <template #label>
                    Email <span class="text-red-600">*</span>
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
                Send Mail
              </base-button>
            </form>
          </validation-observer>
        </div>
        <div class="flex flex-col items-center text-center w-full lg:w-1/2">
          <template v-if="!tokenSent">
            <div class="mb-4">Already Registered? Login from here</div>
            <nuxt-link to="/login"><base-button>Login</base-button></nuxt-link>
          </template>
          <template v-else>
            <p class="text-lg">Reset token successfully sent!</p>
            <p class="text-gray-600">
              Please check you mail and reset your password.
            </p>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ValidationObserver, ValidationProvider } from 'vee-validate'

import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'Signup',

  layout: 'retailer',

  middleware: 'auth',

  auth: 'guest',

  head: {
    title: 'KMPL | Forgot Password',
  },

  components: {
    BreadCrumb,
    ValidationObserver,
    ValidationProvider,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Forgot Password',
          path: '/forgot-password',
        },
      ],
      email: null,
      tokenSent: false,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    async handleForgotPassword() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.get(
          `/secured/generatePasswordToken?email=${this.email}`
        )
        if (status === 200) {
          this.tokenSent = true
          this.$store.dispatch('notification/setNotification', {
            message: 'Reset token sent to mail',
          })
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
