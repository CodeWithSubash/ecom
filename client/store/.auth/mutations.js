const mutations = {
  SET_LOGGED_IN_USER: (state, payload) => {
    state.loggedInUser = payload
    window && window.localStorage.setItem('ecom-user', payload)
  },

  SET_AUTH_TOKEN: (state, payload) => {
    state.authTokem = payload
    window && window.localStorage.setItem('auth-token', payload)
  },
}

export default mutations
