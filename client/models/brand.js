export default class {
  constructor(name, logoFile = null) {
    this.name = name
    this.logoFile = logoFile
  }

  static validationRules = {
    name: {
      required: true,
      message: 'Please enter brand name',
      trigger: 'blur',
    },
  }
}
