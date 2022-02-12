export default {
  data() {
    return {
      params: {
        pageNo: 0,
        keyword: '',
        pageSize: 10,
        orderType: 'desc',
      },
    }
  },

  computed: {
    paginationSync: {
      get() {
        return this.pagination.pageNo + 1
      },
      set(val) {
        this.params.pageNo = val - 1
      },
    },
  },

  watch: {
    'params.pageNo': {
      deep: true,
      handler() {
        this.$fetch()
      },
    },
    'params.pageSize': {
      deep: true,
      handler() {
        this.$fetch()
      },
    },
    'params.orderType': {
      deep: true,
      handler() {
        this.$fetch()
      },
    },
  },

  methods: {
    handlePageChange(val) {
      this.params.pageNo = parseInt(val - 1)
    },
    handleSizeChange(val) {
      this.pageNo = 0
      this.params.pageSize = val
    },
    handleSortChange({ order }) {
      let orderType = order === 'ascending' ? 'asc' : 'desc'
      this.params.orderType = orderType
    },
  },
}
