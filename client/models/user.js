export default class User {
  constructor(name, username, email, role = 'ADMIN') {
    this.name = name
    this.username = username
    this.email = email
    this.role = role
  }

  static roles = [
    {
      label: 'Admin',
      value: 'ADMIN',
    },
    {
      label: 'Retailer',
      value: 'RETAILER',
    },
    {
      label: 'Sales',
      value: 'SALES',
    },
  ]

  static validationRules = {
    name: [
      {
        required: true,
        message: 'Please enter full name',
        trigger: 'blur',
      },
    ],
    username: [
      {
        required: true,
        message: 'Please enter username',
        trigger: 'blur',
      },
    ],
    email: [
      {
        required: true,
        email: true,
        message: 'Please enter email address',
        trigger: 'blur',
      },
      {
        required: true,
        pattern: /\S+@\S+\.\S+/,
        message: 'Invalid email address',
        trigger: 'blur',
      },
    ],
    role: [
      {
        required: true,
        message: 'Please select a role',
        trigger: 'change',
      },
    ],
  }
}
