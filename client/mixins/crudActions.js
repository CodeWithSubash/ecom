export default {
  methods: {
    async create({ url, payload }) {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.post(url, payload)
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Successfull created',
          })
          return Promise.resolve(true)
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },

    async update({ url, payload }) {
      try {
        this.$store.commit('SET_LOADING', true)
        const { status } = await this.$axios.put(url, payload)
        if (status === 200) {
          this.$store.dispatch('notification/setNotification', {
            message: 'Successfull updated',
          })
          return Promise.resolve(true)
        }
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },

    async delete(url) {
      try {
        this.$store.commit('SET_LOADING', false)
        const response = await this.$confirm(
          'Are you sure you want to delete?',
          'Warning',
          {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
          }
        )
          .then(async () => {
            this.$store.commit('SET_LOADING', true)
            const { status } = await this.$axios.delete(url)
            if (status === 200) {
              this.$store.dispatch('notification/setNotification', {
                message: 'Successfully deleted',
              })
              return Promise.resolve(true)
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: 'Delete canceled',
            })
            return Promise.resolve(false)
          })
        return response
      } catch (error) {
        this.$store.dispatch('notification/setNotification', {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        })
      } finally {
        this.$store.commit('SET_LOADING', false)
      }
    },
  },
}
