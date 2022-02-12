export default class {
  constructor(
    categoryId,
    couponCode,
    couponType = 'PERCENTAGE',
    discountValue,
    minimumOrderValue,
    maximumDiscountAmount,
    validFrom,
    validUntil
  ) {
    this.categoryId = categoryId
    this.couponCode = couponCode
    this.couponType = couponType
    this.discountValue = discountValue
    this.minimumOrderValue = minimumOrderValue
    this.maximumDiscountAmount = maximumDiscountAmount
    this.validFrom = validFrom
    this.validUntil = validUntil
  }

  static couponTypes = ['PERCENTAGE', 'CURRENCY', 'FREE_SHIPPING']

  static validationRules = {
    couponCode: [
      {
        required: true,
        pattern: /^[a-zA-Z0-9_-]+$/,
        message: 'Please enter valid code',
        trigger: 'blur',
      },
    ],
    couponType: [
      {
        required: true,
        message: 'Please select coupon type',
        trigger: 'change',
      },
    ],
    // discountValue: [
    //   {
    //     required: true,
    //     message: 'Please input discount value',
    //     trigger: 'blur',
    //   },
    // ],
    minimumOrderValue: [
      {
        required: true,
        message: 'Please input min order value',
        trigger: 'blur',
      },
    ],
    // maximumDiscountAmount: [
    //   {
    //     required: true,
    //     message: 'Please input max discount value',
    //     trigger: 'blur',
    //   },
    // ],
    validFrom: [
      {
        required: true,
        message: 'Please select start date',
        trigger: 'change',
      },
    ],
    validUntil: [
      {
        required: true,
        message: 'Please select expiry date',
        trigger: 'change',
      },
    ],
  }
}
