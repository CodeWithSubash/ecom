export default function ({ $auth, store, redirect }) {
  if ($auth.loggedIn && !store.getters['user/isWholeSaler'])
    return redirect('/')
}
