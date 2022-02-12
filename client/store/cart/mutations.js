const mutations = {
  SET_CART_ITEMS: (state, items) => {
    state.cartItems = items
  },
  ADD_CART_ITEM: (state, item) => {
    state.cartItems = [...state.cartItems, item]
  },
  REMOVE_CART_ITEM: (state, item) => {
    state.cartItems = state.cartItems.filter((ci) => ci.id !== item.id)
  },
  UPDATE_CART: (state, item) => {
    state.cartItems = state.cartItems.map((ci) =>
      ci.id == item.id ? item : ci
    )
  },
}

export default mutations
