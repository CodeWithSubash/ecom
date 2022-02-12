const actions = {
  async getFeaturedImages({ commit, dispatch }) {
    try {
      const {
        data: { data },
        status,
      } = await this.$axios.get('auth/featured')
      if (status === 200) {
        const validImages = data.filter((img) => img.files.fileDownloadUri)
        commit('SET_FEATURED_IMAGES', validImages)
      }
    } catch (error) {
      dispatch(
        'notification/setNotification',
        {
          title: 'Error',
          type: 'error',
          message: error.response.data.message,
        },
        { root: true }
      )
    }
  },
}

export default actions
