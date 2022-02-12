export default function ({ $auth, store, redirect }) {
  if ($auth.loggedIn && !store.getters['user/isSalesPerson'])
    return redirect('/')
}
