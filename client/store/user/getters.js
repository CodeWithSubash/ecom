const getters = {
  hasCmsAccess: (state, commit, rootState) =>
    rootState.auth.user &&
    (rootState.auth.user.role === 'SUPERADMIN' ||
      rootState.auth.user.role === 'ADMIN' ||
      rootState.auth.user.role === 'WHOLESALER' ||
      rootState.auth.user.role === 'SALES'),

  isAdmin: (state, commit, rootState) =>
    rootState.auth.user &&
    (rootState.auth.user.role === 'SUPERADMIN' ||
      rootState.auth.user.role === 'ADMIN'),

  isSuperAdmin: (state, commit, rootState) =>
    rootState.auth.user && rootState.auth.user.role === 'SUPERADMIN',

  isSalesPerson: (state, commit, rootState) =>
    rootState.auth.user && rootState.auth.user.role === 'SALES',

  isWholeSaler: (state, commit, rootState) =>
    rootState.auth.user && rootState.auth.user.role === 'WHOLESALER',

  isRetailer: (state, commit, rootState) =>
    rootState.auth.user && rootState.auth.user.role === 'RETAILER',

  kycStatus: (state, commit, rootState) => {
    if (rootState.auth.user && rootState.auth.user.userRetailer) {
      const retailer = rootState.auth.user.userRetailer
      const { deletedAt, status } = retailer

      if (deletedAt && status) return 'Incomplete'
      if (!deletedAt && !status) return 'Pending'
      if (deletedAt && !status) return 'Rejected'
      if (!deletedAt && status) return 'Verified'
    }
    return
  },
}

export default getters
