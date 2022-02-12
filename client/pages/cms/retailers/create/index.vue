<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
        <div class="flex space-x-2">
          <el-button
            type="success"
            size="medium"
            :disabled="loading"
            @click="submitForm('retailerForm')"
          >
            Save
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/retailers')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="loading"
        :model="retailer"
        :rules="rules"
        ref="retailerForm"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="username" label="Username">
              <el-input v-model.trim="retailer.username"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="email" label="Email">
              <el-input
                v-model.trim="retailer.email"
                type="email"
                placeholder="example@mail.com"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="role" label="Role">
              <el-input v-model="retailer.role" readonly> </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="flex items-center">
          <div class="text-gray-500 text-xs mr-2">KYC</div>
          <el-divider></el-divider>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Logo">
              <cms-upload
                directory="retailer"
                fileType="logo"
                @imageUploaded="retailer.userRetailer.logo = $event"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Business Name">
              <el-input v-model="retailer.userRetailer.businessName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Date of Corporation">
              <el-date-picker
                v-model="retailer.userRetailer.dateOfCorporation"
                type="date"
                placeholder="Pick corporation start date"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Trading Status">
              <el-input
                v-model="retailer.userRetailer.tradingStatus"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Business Address">
              <el-input
                v-model="retailer.userRetailer.businessAddress"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="Shareholders">
              <el-input v-model="retailer.userRetailer.shareholders"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Directors">
              <el-input v-model="retailer.userRetailer.directors"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="userRetailer.landline" label="Landline">
              <el-input
                v-model="retailer.userRetailer.landline"
                type="tel"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="userRetailer.mobile" label="Mobile">
              <el-input
                v-model="retailer.userRetailer.mobile"
                type="tel"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="userRetailer.fax" label="Fax">
              <el-input
                v-model="retailer.userRetailer.fax"
                type="tel"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Bank Name">
              <el-input v-model="retailer.userRetailer.bankName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Bank Address">
              <el-input v-model="retailer.userRetailer.bankAddress"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Banker Contact Person">
              <el-input
                v-model="retailer.userRetailer.bankerContactPerson"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
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
          <el-col :span="8">
            <el-form-item label="Document ID Certificate">
              <cms-upload
                directory="retailer"
                fileType="documentId"
                @imageUploaded="
                  retailer.userRetailer.documentIdCertificate = $event
                "
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
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
          <el-col :span="8">
            <el-form-item label="Business Registration Certificate">
              <cms-upload
                directory="retailer"
                fileType="businessRegistration"
                @imageUploaded="
                  retailer.userRetailer.businessRegistrationCertificate = $event
                "
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
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
          <el-col :span="8">
            <el-form-item
              prop="userRetailer.vatRegistrationCertificate"
              label="VAT Registration Certificate"
            >
              <cms-upload
                directory="retailer"
                fileType="vatRegistration"
                @imageUploaded="
                  retailer.userRetailer.vatRegistrationCertificate = $event
                "
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="VAT Registration Number">
              <el-input
                v-model="retailer.userRetailer.vatRegistrationNumber"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import Retailer from '~/models/retailer'

export default {
  name: 'CreateRetailer',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'Create Retailer ',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Retailers',
          path: '/cms/retailers',
        },
        {
          title: 'Create Retailer',
          path: '',
        },
      ],

      retailer: new Retailer(),
      rules: Retailer.validationRules,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.handleCreate()
        }
      })
    },

    async handleCreate() {
      const success = await this.create({
        url: '/auth/user/retailer',
        payload: { ...this.retailer, dateOfCorporation: null },
      })
      if (success) this.$router.push('/cms/retailers')
    },
  },
}
</script>

<style></style>
