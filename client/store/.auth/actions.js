const actions = {
  loginUser({ commit }, payload) {
    return this.$authRepository.loginUser(payload)
  },
}

export default actions
