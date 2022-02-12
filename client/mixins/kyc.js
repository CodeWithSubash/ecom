export default {
  computed: {
    kycStatus() {
      return this.$store.getters['user/kycStatus']
    },

    verifiedKyc() {
      return this.kycStatus && this.kycStatus === 'Verified'
    },
  },
}
