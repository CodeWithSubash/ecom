export default class {
  constructor(
    name,
    productCondition = 'New',
    price,
    mrp,
    openingStock,
    deal = false,
    categoryId,
    brandId,
    description,
    shippingAndDelivery,
    paymentAndReturn,
    tags = [],
    images = [],
    wholesalerId = null
  ) {
    this.name = name
    this.productCondition = productCondition
    this.price = price
    this.mrp = mrp
    this.openingStock = openingStock
    this.deal = deal
    this.categoryId = categoryId
    this.brandId = brandId
    this.description = description
    this.shippingAndDelivery = shippingAndDelivery
    this.paymentAndReturn = paymentAndReturn
    this.tags = tags
    this.images = images
    this.wholesalerId = wholesalerId
  }

  static productConditions = ['New', 'Used', 'Refurbished']

  static validationRules = {
    name: {
      required: true,
      message: 'Please enter product name',
      trigger: 'blur',
    },
    productCondition: {
      required: true,
      message: 'Please select product productCondition',
      trigger: 'change',
    },
    mrp: {
      required: true,
      message: 'Please enter product MRP',
      trigger: 'blur',
    },
    // price: {
    //   required: true,
    //   message: 'Please enter product price',
    //   trigger: 'blur',
    // },
    openingStock: {
      required: true,
      message: 'Please specify available stock',
      trigger: 'blur',
    },
    categoryId: {
      required: true,
      message: 'Please select product category',
      trigger: 'change',
    },
    brandId: {
      required: true,
      message: 'Please select product brand',
      trigger: 'change',
    },
    description: {
      required: true,
      message: 'Please enter product description',
      trigger: 'change',
    },
    shippingAndDelivery: {
      required: true,
      message: 'Please enter shipping detail',
      trigger: 'blur',
    },
    paymentAndReturn: {
      required: true,
      message: 'Please enter payment detail',
      trigger: 'blur',
    },
    tags: {
      required: true,
      message: 'Please enter atleast one tag',
      trigger: 'blur',
    },
    images: {
      required: true,
      message: 'Please select atleast one image',
      trigger: 'blur',
    },
    wholesalerId: {
      required: true,
      message: 'Please select wholesaler',
      trigger: 'blur',
    },
  }
}
