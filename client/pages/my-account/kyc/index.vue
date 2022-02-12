<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <div class="default-container">
      <div class="bg-gray-100 px-6 py-2">
        <el-form
          v-loading="loading"
          :model="retailer"
          ref="retailerForm"
          label-position="top"
          style="width: 100%"
        >
          <el-row :gutter="20">
            <el-col :span="24" :lg="8">
              <el-form-item label="Logo">
                <cms-upload
                  directory="retailer"
                  fileType="logo"
                  :image="retailer.userRetailer.logo"
                  @imageUploaded="retailer.userRetailer.logo = $event"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="Business Name">
                <el-input
                  v-model="retailer.userRetailer.businessName"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="Date of Corporation">
                <el-date-picker
                  v-model="retailer.userRetailer.dateOfCorporation"
                  type="date"
                  placeholder="Pick corporation start date"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="Trading Status">
                <el-input
                  v-model="retailer.userRetailer.tradingStatus"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="Business Address">
                <el-input
                  v-model="retailer.userRetailer.businessAddress"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="24" :lg="8">
              <el-form-item label="Shareholders">
                <el-input
                  v-model="retailer.userRetailer.shareholders"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="Directors">
                <el-input v-model="retailer.userRetailer.directors"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" :lg="8">
              <el-form-item prop="userRetailer.landline" label="Landline">
                <el-input
                  v-model="retailer.userRetailer.landline"
                  type="tel"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item prop="userRetailer.mobile" label="Mobile">
                <el-input
                  v-model="retailer.userRetailer.mobile"
                  type="tel"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item prop="userRetailer.fax" label="Fax">
                <el-input
                  v-model="retailer.userRetailer.fax"
                  type="tel"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" :lg="8">
              <el-form-item label="Bank Name">
                <el-input v-model="retailer.userRetailer.bankName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="Bank Address">
                <el-input
                  v-model="retailer.userRetailer.bankAddress"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="Banker Contact Person">
                <el-input
                  v-model="retailer.userRetailer.bankerContactPerson"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item
                prop="userRetailer.bankerContactNumber"
                label="Banker Contact Number"
              >
                <el-input
                  v-model="retailer.userRetailer.bankerContactNumber"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" :lg="8">
              <el-form-item label="Document ID Certificate">
                <cms-upload
                  directory="retailer"
                  fileType="documentId"
                  :image="retailer.userRetailer.documentIdCertificate"
                  @imageUploaded="
                    retailer.userRetailer.documentIdCertificate = $event
                  "
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item
                prop="userRetailer.documentIdNumber"
                label="Document ID Number"
              >
                <el-input
                  v-model="retailer.userRetailer.documentIdNumber"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" :lg="8">
              <el-form-item label="Business Registration Certificate">
                <cms-upload
                  directory="retailer"
                  fileType="businessRegistration"
                  :image="retailer.userRetailer.businessRegistrationCertificate"
                  @imageUploaded="
                    retailer.userRetailer.businessRegistrationCertificate =
                      $event
                  "
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item
                prop="userRetailer.businessRegistrationNumber"
                label="Business Registration Number"
              >
                <el-input
                  v-model="retailer.userRetailer.businessRegistrationNumber"
                >
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" :lg="8">
              <el-form-item
                prop="userRetailer.vatRegistrationCertificate"
                label="VAT Registration Certificate"
              >
                <cms-upload
                  directory="retailer"
                  fileType="vatRegistration"
                  :image="retailer.userRetailer.vatRegistrationCertificate"
                  @imageUploaded="
                    retailer.userRetailer.vatRegistrationCertificate = $event
                  "
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" :lg="8">
              <el-form-item label="VAT Registration Number">
                <el-input
                  v-model="retailer.userRetailer.vatRegistrationNumber"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <div class="flex lg:justify-end mt-6">
        <div class="flex items-center justify-between space-x-4">
          <nuxt-link to="/my-account">
            <base-button inverse>Cancel</base-button>
          </nuxt-link>
          <div :loading="loading" @click="submitForm('retailerForm')">
            <base-button>Update</base-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Retailer from '~/models/retailer'
import crudActions from '~/mixins/crudActions'

import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'RetailerKyc',

  layout: 'retailer',

  middleware: ['auth', 'kyc'],

  head: {
    title: 'KMPL | Account',
  },

  mixins: [crudActions],

  components: {
    BreadCrumb,
  },

  data() {
    return {
      breadcrumbs: [
        {
          name: 'My Account',
          path: '/my-account',
        },
        {
          name: 'KYC',
          path: '',
        },
      ],

      retailer: new Retailer(),
    }
  },

  computed: {
    loading() {
      this.$store.state.isLoading
    },
  },

  watch: {
    retailerDetail: {
      immediate: true,
      handler(nv) {
        this.retailer = { ...nv }
      },
    },
  },

  async asyncData({ $axios }) {
    const { data } = await $axios.get('/auth/user')
    return { retailerDetail: data }
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.handleUpdate()
        }
      })
    },

    async handleUpdate() {
      const filterKeys = ['id', 'userId', 'deletedAt', 'acceptVat']
      const payload = Object.keys(this.retailer.userRetailer)
        .filter((key) => !filterKeys.includes(key))
        .reduce((obj, key) => {
          obj[key] = this.retailer.userRetailer[key]
          return obj
        }, {})

      const success = await this.create({
        url: `/auth/userRetailer`,
        payload: payload,
      })
      if (success) this.$router.push('/my-account')
    },
  },
}
</script>

<style></style>
