export default function ({ $auth, store, redirect }) {
  if ($auth.loggedIn && store.getters['user/hasCmsAccess'])
    return redirect('/cms/dashboard')
}
