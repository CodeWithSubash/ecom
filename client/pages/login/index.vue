<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <div class="text-2xl lg:text-3xl font-semibold mb-10">Login</div>
      <div class="flex flex-col lg:flex-row">
        <login-form class="w-full lg:w-1/2 mb-10 lg:mb-0" />
        <div class="flex flex-col items-center w-full lg:w-1/2">
          <div class="mb-4">
            Don't have an account yet? Register your account
          </div>
          <nuxt-link to="/register">
            <base-button>Register</base-button>
          </nuxt-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'Login',

  layout: 'retailer',

  middleware: 'auth',

  auth: 'guest',

  head: {
    title: 'KMPL | Login',
  },

  components: {
    BreadCrumb,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Login',
          path: '/login',
        },
      ],
    }
  },
}
</script>

<style></style>
