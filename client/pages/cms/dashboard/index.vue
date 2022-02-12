<template>
  <with-header>
    <component :is="currentDashboard" />
  </with-header>
</template>

<script>
import AdminDashboard from '~/components/dashboard/Admin'
import SalesDashboard from '~/components/dashboard/Sales'
import WholeSalerDashboard from '~/components/dashboard/WholeSaler'

export default {
  name: 'Dashboard',

  layout: 'cms',

  head: {
    title: 'CMS | Dashboard',
  },

  components: {
    AdminDashboard,
    SalesDashboard,
    WholeSalerDashboard,
  },

  data() {
    return {}
  },

  computed: {
    isAdmin() {
      return this.$store.getters['user/isAdmin']
    },

    isWholeSaler() {
      return this.$store.getters['user/isWholeSaler']
    },

    isSalesPerson() {
      return this.$store.getters['user/isSalesPerson']
    },

    currentDashboard() {
      if (this.isWholeSaler) return 'WholeSalerDashboard'
      else if (this.isSalesPerson) return 'SalesDashboard'
      else return 'AdminDashboard'
    },
  },
}
</script>
