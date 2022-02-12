export default class User {
  constructor(
    username,
    email,
    role = 'SALES',
    designation,

    firstname,
    lastname,
    phone,
    mobile,
    alternativePhone,
    alternativeMobile,
    street,
    street2,
    city,
    state,
    zipCode
  ) {
    this.username = username
    this.email = email
    this.role = role

    this.userEmployee = {
      firstname,
      lastname,
      phone,
      mobile,
      designation,
      alternativePhone,
      alternativeMobile,
      street,
      street2,
      city,
      state,
      zipCode,
    }
  }

  static roles = [
    {
      label: 'Admin',
      value: 'ADMIN',
    },
    {
      label: 'Sales',
      value: 'SALES',
    },
    {
      label: 'Wholesalers',
      value: 'WHOLESALER',
    },
  ]

  static designations = [
    'Admin',
    'Assistant',
    'Accountant',
    'Wholesaler',
    'Sales Representative',
  ]

  static validationRules = {
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
    'userEmployee.designation': [
      {
        required: true,
        message: 'Please select a designation',
        trigger: 'change',
      },
    ],
    // userEmployee.firstname: [
    //   {
    //     required: true,
    //     message: 'Please enter first name',
    //     trigger: 'blur',
    //   },
    // ],
    // userEmployee.lastname: [
    //   {
    //     required: true,
    //     message: 'Please enter last name',
    //     trigger: 'blur',
    //   },
    // ],
    // userEmployee.phone: [
    //   {
    //     max: 15,
    //     message: 'Max length is 15',
    //     trigger: 'change',
    //   },
    // ],
  }
}
