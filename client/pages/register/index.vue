<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <div class="text-2xl lg:text-3xl font-semibold mb-10">Register</div>
      <div class="flex flex-col lg:flex-row">
        <register-form
          @registerSuccess="onRegistration"
          class="w-full lg:w-1/2 mb-10 lg:mb-0"
        />
        <div class="flex flex-col items-center w-full lg:w-1/2">
          <template v-if="!registered.status">
            <div class="mb-4">Already Registered? Login from here</div>
            <nuxt-link to="/login"><base-button>Login</base-button></nuxt-link>
          </template>
          <template v-else>
            <p class="text-lg">You have been successfully registered!</p>
            <p class="text-gray-600">
              Please check you mail and verify your account.
            </p>
            <form
              @submit.prevent="resendToken"
              class="mt-10 flex flex-col items-center"
            >
              <p class="mb-2">Didn't recived email? Send Again!</p>
              <base-button type="submit" :loading="loading">
                Resend Mail
              </base-button>
            </form>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'Signup',

  layout: 'retailer',

  middleware: 'auth',

  auth: 'guest',

  head: {
    title: 'KMPL | Register',
  },

  components: {
    BreadCrumb,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Register',
          path: '/register',
        },
      ],
      registered: { status: false, email: null },
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    onRegistration(email) {
      this.registered.status = true
      this.registered.email = email
    },

    async resendToken() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.get(
          `secured/resendRegistrationToken?email=${this.registered.email}`
        )
        if (status === 200)
          this.$store.dispatch('notification/setNotification', {
            message: 'Verification token resent to email! ',
          })
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
