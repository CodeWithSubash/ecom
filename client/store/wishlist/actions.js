const actions = {
  async addToWishlist({ commit, dispatch }, productId) {
    try {
      const { data, status } = await this.$axios.post(
        `/auth/wishlist/${productId}`
      )
      if (status === 200) {
        commit('ADD_TO_WISHLIST', data)
        dispatch(
          'notification/setNotification',
          {
            message: 'Product added to wishlist',
          },
          { root: true }
        )
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
  async removeFromWishlist({ commit, dispatch }, productId) {
    try {
      commit('SET_LOADING', true, { root: true })
      const { status } = await this.$axios.delete(
        `/auth/wishlist/deleteByProduct/${productId}`
      )
      if (status === 200) {
        commit('REMOVE_FROM_WISHLIST', productId)
        dispatch(
          'notification/setNotification',
          {
            message: 'Product removed from wishlist',
          },
          { root: true }
        )
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
    } finally {
      commit('SET_LOADING', false, { root: true })
    }
  },
}

export default actions
