<template>
  <div class="default-container mt-24">
    <div class="flex flex-col h-96 items-center text-center justify-center">
      <p class="mb-2 font-medium text-lg">Confirm Registration!</p>
      <p class="mb-4 text-gray-600">
        Please confirm your email by clicking the button below.
      </p>
      <div @click="verifyEmail">
        <base-button inverse :loading="loading">Verify Email</base-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfrimRegistration',

  middleware: 'guest',

  head: {
    title: 'KMPL | Confirm Registration',
  },

  mounted() {
    this.token = this.$route.params.token
  },

  data() {
    return {
      token: null,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    async verifyEmail() {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.get(
          `/secured/registrationConfirm/${this.token}`
        )
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Email successfully verified!',
          })
          this.$router.push('/home')
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          type: 'info',
          title: 'Info',
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
