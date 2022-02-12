const mutations = {
  SET_WISHLIST: (state, wishlist) => {
    state.wishlistItems = wishlist
  },
  ADD_TO_WISHLIST: (state, item) => {
    state.wishlistItems = [...state.wishlistItems, item]
  },
  REMOVE_FROM_WISHLIST: (state, productId) => {
    state.wishlistItems = state.wishlistItems.filter(
      (wi) => wi.product.id !== productId
    )
  },
}

export default mutations
