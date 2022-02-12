<template>
  <div class="fixed bottom-0 left-0">
    <div
      v-if="showKycInfo"
      class="p-4"
      :class="`bg-${infoDetail.background}-400`"
    >
      <span> {{ infoDetail.message }} </span>
      <span class="text-white">
        <nuxt-link to="/my-account/kyc">Update Now!</nuxt-link>
      </span>
    </div>
  </div>
</template>

<script>
import kyc from '~/mixins/kyc'

export default {
  name: 'KYCStatusInfo',

  mixins: [kyc],

  computed: {
    showKycInfo() {
      return (
        this.$auth.loggedIn &&
        this.$nuxt.$route.name !== 'my-account-kyc' &&
        (this.kycStatus === 'Rejected' || this.kycStatus === 'Incomplete')
      )
    },

    infoDetail() {
      const conditions = {
        rejected: {
          message:
            'Your KYC information have been rejected. Please resubmit your details with valid infomation.',
          background: 'red',
        },
        incomplete: {
          message:
            'Your KYC detail is incomplete. Please fill it to shop products.',
          background: 'blue',
        },
      }

      return this.kycStatus && conditions[this.kycStatus.toLowerCase()]
    },
  },
}
</script>

<style></style>
