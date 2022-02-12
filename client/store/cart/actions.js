const actions = {
  async getRetailerCartItems({ commit, dispatch }) {
    try {
      const {
        status,
        data: { cartDetail },
      } = await this.$axios.get('/auth/cart/getByUser')
      if (status === 200) {
        commit('SET_CART_ITEMS', cartDetail)
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
  async addToRetailerCart({ commit, dispatch }, product) {
    try {
      const { status, data } = await this.$axios.post('/auth/cart/addToCart', {
        productId: product.id,
        quantity: product.quantity,
      })
      if (status === 200) {
        commit('ADD_CART_ITEM', data)
        dispatch(
          'notification/setNotification',
          {
            message: 'Cart successfully updated',
          },
          { root: true }
        )
        return true
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
      return false
    }
  },
  async removeCartItem({ commit, dispatch }, item) {
    try {
      commit('SET_LOADING', true, { root: true })
      const { status } = await this.$axios.delete(`/auth/cart/${item.id}`)
      if (status === 200) {
        dispatch(
          'notification/setNotification',
          {
            message: 'Product successfully removed',
          },
          { root: true }
        )
        commit('REMOVE_CART_ITEM', item)
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

  async updateCartItem({ commit, dispatch }, cartItem) {
    try {
      const { status, data } = await this.$axios.post('/auth/cart/addToCart', {
        productId: cartItem.product.id,
        quantity: cartItem.quantity,
      })
      if (status === 200) {
        commit('UPDATE_CART', data)
        dispatch(
          'notification/setNotification',
          {
            message: 'Cart successfully updated',
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
}

export default actions
