<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <div class="text-2xl lg:text-3xl font-semibold mb-10">
        Change Password
      </div>

      <div class="w-full lg:w-1/2">
        <validation-observer v-slot="{ handleSubmit }">
          <form
            @submit.prevent="handleSubmit(handleChangePassword)"
            class="border rounded px-6 py-8"
          >
            <validation-provider rules="required" v-slot="{ errors }">
              <base-input
                v-model.trim="credentials.oldPassword"
                type="password"
                class="mb-6"
              >
                <template #label>
                  Old Password <span class="text-red-600">*</span>
                </template>
                <template #error> {{ errors[0] }} </template>
              </base-input>
            </validation-provider>
            <validation-provider rules="required" v-slot="{ errors }">
              <base-input
                v-model.trim="credentials.password"
                type="password"
                class="mb-6"
              >
                <template #label>
                  New Password <span class="text-red-600">*</span>
                </template>
                <template #error> {{ errors[0] }} </template>
              </base-input>
            </validation-provider>
            <validation-provider rules="required" v-slot="{ errors }">
              <base-input
                v-model.trim="credentials.rePassword"
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
              class="capitalize mb-6"
            >
              Change Password
            </base-button>
          </form>
        </validation-observer>
      </div>
    </div>
  </div>
</template>

<script>
import { ValidationObserver, ValidationProvider } from 'vee-validate'

import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'RetailerChangePassword',

  layout: 'retailer',

  components: {
    BreadCrumb,
    ValidationObserver,
    ValidationProvider,
  },

  middleware: 'auth',

  head: {
    title: 'Account | Change Password',
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'My Account',
          path: '/my-account',
        },
        {
          name: 'Password',
          path: '/change-password',
        },
      ],
      credentials: {},
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    async handleChangePassword() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.put(
          '/auth/user/changePassword',
          this.credentials
        )
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Password changed successfully',
          })
          this.$router.push('/my-account')
        }
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
