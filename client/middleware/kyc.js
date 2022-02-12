export default function ({ $auth, store, redirect }) {
  if ($auth.loggedIn && store.getters['user/kycStatus'] === 'Verified')
    return redirect('/my-account')
}
