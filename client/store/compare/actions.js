const actions = {
  addToCompareList: ({ commit }, product) => {
    commit('ADD_PRODUCT', product)
  },
  removeFromCompareList: ({ commit }, product) => {
    commit('REMOVE_PRODUCT', product)
  },
}

export default actions
