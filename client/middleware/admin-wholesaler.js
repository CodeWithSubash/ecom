export default function ({ $auth, store, redirect }) {
  if (store.getters['user/hasCmsAccess'] && store.getters['user/isSalesPerson'])
    return redirect('/')
}
