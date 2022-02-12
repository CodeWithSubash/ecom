export default class {
  constructor(
    title,
    subTitle,
    description = null,
    logoFile,
    featuredType = 'BANNER_SLIDER',
    categoryId = null
  ) {
    this.title = title
    this.subTitle = subTitle
    this.description = description
    this.featuredType = featuredType
    this.logoFile = logoFile
    this.categoryId = categoryId
  }

  static featuredTypes = [
    'BANNER_SLIDER',
    'BANNER_FIXED',
    'FEATURED_TOP',
    'FEATURED_AD',
  ]

  static validationRules = {
    title: {
      required: true,
      message: 'Please enter featured image title',
      trigger: 'blur',
    },
    subTitle: {
      required: true,
      message: 'Please enter featured image subtitle',
      trigger: 'blur',
    },
    logoFile: {
      required: true,
      message: 'Please select a featured image',
      trigger: 'change',
    },
  }
}
