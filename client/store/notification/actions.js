import { Notification } from 'element-ui'

const actions = {
  setNotification: (_, { type = 'success', title = 'Success', message }) => {
    Notification({
      type,
      title,
      message,
    })
  },
}

export default actions
