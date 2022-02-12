<template>
  <div>
    <validation-observer v-slot="{ handleSubmit }">
      <form
        @submit.prevent="handleSubmit(handleRegister)"
        class="border rounded px-6 py-8"
      >
        <div class="flex flex-col lg:flex-row lg:space-x-4">
          <validation-provider
            rules="required"
            v-slot="{ errors }"
            class="w-full lg:w-1/2"
          >
            <base-input v-model.trim="user.firstName" class="mb-6">
              <template #label>
                First Name <span class="text-red-600">*</span>
              </template>
              <template #error> {{ errors[0] }} </template>
            </base-input>
          </validation-provider>
          <validation-provider
            rules="required"
            v-slot="{ errors }"
            class="w-full lg:w-1/2"
          >
            <base-input v-model.trim="user.lastName" class="mb-6">
              <template #label>
                Last Name <span class="text-red-600">*</span>
              </template>
              <template #error> {{ errors[0] }} </template>
            </base-input>
          </validation-provider>
        </div>
        <validation-provider
          rules="required"
          v-slot="{ errors }"
          class="w-full lg:w-1/2"
        >
          <base-input
            v-model.trim="user.username"
            class="mb-6 w-full lg:w-1/2 pr-2"
          >
            <template #label>
              Username <span class="text-red-600">*</span>
            </template>
            <template #error> {{ errors[0] }} </template>
          </base-input>
        </validation-provider>
        <validation-provider rules="required|email" v-slot="{ errors }">
          <base-input v-model.trim="user.email" type="email" class="mb-6">
            <template #label>
              Email <span class="text-red-600">*</span>
            </template>
            <template #error> {{ errors[0] }} </template>
          </base-input>
        </validation-provider>
        <div class="flex flex-col lg:flex-row lg:space-x-4">
          <validation-provider
            rules="required"
            v-slot="{ errors }"
            class="w-full lg:w-1/2"
          >
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
          <validation-provider
            rules="required"
            v-slot="{ errors }"
            class="w-full lg:w-1/2"
          >
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
        </div>
        <base-button
          inverse
          on-hover
          type="submit"
          :loading="loading"
          class="capitalize mb-6"
        >
          Register
        </base-button>
      </form>
    </validation-observer>
  </div>
</template>

<script>
import { Notification } from 'element-ui'
import { ValidationObserver, ValidationProvider } from 'vee-validate'

import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'RegisterForm',

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
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        password: '',
        rePassword: '',
      },
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    async handleRegister() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.post('/secured/registration', {
          ...this.user,
          name: `${this.user.firstName} ${this.user.lastName}`,
        })
        if (status === 200)
          this.$store.dispatch('notification/setNotification', {
            message: 'Registered Successfully',
          })
        this.$emit('registerSuccess', this.user.email)
      } catch (err) {
        this.$store.dispatch('notification/setNotification', {
          type: 'error',
          title: 'Error',
          message: 'User with same email or username already exists!',
        })
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },
  },
}
</script>

<style></style>
