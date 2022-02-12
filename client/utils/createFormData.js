const createFormData = (object) => {
  const formData = new FormData()
  Object.keys(object).forEach((e) => {
    const makeString = (x) => (x ? 1 : 0)
    if (typeof object[e] === 'undefined' || object[e] === null) return
    const val =
      typeof object[e] === 'boolean' ? makeString(object[e]) : object[e]
    formData.append(e, val)
  })
  return formData
}

export default createFormData
