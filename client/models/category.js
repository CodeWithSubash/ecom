export default class {
  constructor(name, parentId = null, bannerFile = null, logoFile = null) {
    this.name = name
    this.parentId = parentId
    this.bannerFile = bannerFile
    this.logoFile = logoFile
  }

  static validationRules = {
    name: {
      required: true,
      message: 'Please enter category name',
      trigger: 'blur',
    },
  }
}
