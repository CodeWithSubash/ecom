const mutations = {
  ADD_PRODUCT: (state, product) => {
    state.compareList = [...state.compareList, product]
  },
  REMOVE_PRODUCT: (state, product) => {
    state.compareList = state.compareList.filter((cp) => cp.id !== product.id)
  },
}

export default mutations
