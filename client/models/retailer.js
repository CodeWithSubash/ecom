export default class User {
  constructor(
    username,
    email,
    role = 'RETAILER',

    businessName,
    logo = null,
    tradingStatus,
    businessAddress,
    landline,
    mobile,
    fax,
    dateOfCorporation = null,
    shareholders,
    directors,
    bankName,
    bankAddress,
    bankerContactPerson,
    bankerContactNumber,
    documentIdNumber,
    documentIdCertificate = null,
    businessRegistrationNumber,
    businessRegistrationCertificate = null,
    vatRegistrationNumber,
    vatRegistrationCertificate = null
  ) {
    this.username = username
    this.email = email
    this.role = role

    this.userRetailer = {
      businessName,
      logo,
      tradingStatus,
      businessAddress,
      landline,
      mobile,
      fax,
      dateOfCorporation,
      shareholders,
      directors,
      bankName,
      bankAddress,
      bankerContactPerson,
      bankerContactNumber,
      documentIdNumber,
      documentIdCertificate,
      businessRegistrationNumber,
      businessRegistrationCertificate,
      vatRegistrationNumber,
      vatRegistrationCertificate,
    }
  }

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
    'userRetailer.fax': [
      {
        max: 20,
        message: 'Max length is 20',
        trigger: 'change',
      },
    ],
    'userRetailer.landline': [
      {
        max: 20,
        message: 'Max length is 20',
        trigger: 'change',
      },
    ],
    'userRetailer.mobile': [
      {
        max: 20,
        message: 'Max length is 20',
        trigger: 'change',
      },
    ],
    'userRetailer.bankerContactNumber': [
      {
        max: 20,
        message: 'Max length is 20',
        trigger: 'change',
      },
    ],
    'userRetailer.documentIdNumber': [
      {
        max: 50,
        message: 'Max length is 50',
        trigger: 'change',
      },
    ],
    'userRetailer.businessRegistrationNumber': [
      {
        max: 50,
        message: 'Max length is 50',
        trigger: 'change',
      },
    ],
    'userRetailer.vatRegistrationNumber': [
      {
        max: 50,
        message: 'Max length is 50',
        trigger: 'change',
      },
    ],
  }
}
