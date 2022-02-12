<template>
  <div class="bg-gray-50 rounded-sm p-4">
    <template v-if="retailerDetail">
      <div class="flex flex-row justify-between items-center">
        <div class="font-medium text-lg">KYC Detail</div>
        <div
          class="px-2 py-1 rounded-sm text-sm"
          :class="`bg-${getStatusColor()}-200 text-${getStatusColor()}-800`"
        >
          <span> {{ kycStatus }} </span>
        </div>
        <nuxt-link v-if="!verifiedKyc" to="/my-account/kyc">
          <base-button small inverse>Edit</base-button>
        </nuxt-link>
      </div>
      <el-divider></el-divider>
      <el-row v-if="retailerDetail" :gutter="20">
        <el-col :span="24" :lg="8" class="mb-8">
          <div class="text-gray-500 underline">Business Detail</div>
          <div>
            <span>
              Name:
              {{
                retailerDetail.businessName ? retailerDetail.businessName : '--'
              }}
            </span>
          </div>
          <div>
            <span>
              Address:
              {{
                retailerDetail.businessAddress
                  ? retailerDetail.businessAddress
                  : '--'
              }}
            </span>
          </div>
          <div>
            <span>
              Trading Status:
              {{
                retailerDetail.tradingStatus
                  ? retailerDetail.tradingStatus
                  : '--'
              }}
            </span>
          </div>
        </el-col>

        <el-col :span="24" :lg="8" class="mb-8">
          <div class="text-gray-500 underline">Contact Detail</div>
          <div>
            <span>
              Landline:
              {{ retailerDetail.landline ? retailerDetail.landline : '--' }}
            </span>
          </div>
          <div>
            <span>
              Mobile:
              {{ retailerDetail.mobile ? retailerDetail.mobile : '--' }}
            </span>
          </div>
          <div>
            <span>
              Fax:
              {{ retailerDetail.fax ? retailerDetail.fax : '--' }}
            </span>
          </div>
        </el-col>

        <el-col :span="24" :lg="8" class="mb-8">
          <div class="text-gray-500 underline">Bank Detail</div>
          <div>
            <span>
              Name:
              {{ retailerDetail.bankName ? retailerDetail.bankName : '--' }}
            </span>
          </div>
          <div>
            <span>
              Number:
              {{
                retailerDetail.bankerContactNumber
                  ? retailerDetail.bankerContactNumber
                  : '--'
              }}
            </span>
          </div>
          <div>
            <span>
              Address:
              {{
                retailerDetail.bankAddress ? retailerDetail.bankAddress : '--'
              }}
            </span>
          </div>
          <div>
            <span>
              Contact Person:
              {{
                retailerDetail.bankerContactNumber
                  ? retailerDetail.bankerContactNumber
                  : '--'
              }}
            </span>
          </div>
        </el-col>
        <el-col :span="24" :lg="8" class="mb-8">
          <div class="mb-8">
            <div class="text-gray-500 underline">
              Business Registration Number:
            </div>
            {{
              retailerDetail.businessRegistrationNumber
                ? retailerDetail.businessRegistrationNumber
                : '--'
            }}
          </div>
          <div v-if="retailerDetail.logo">
            <div class="flex space-x-6">
              <div class="text-gray-500 underline">Logo</div>
              <a :href="retailerDetail.logo">
                <i class="bx bx-download bx-sm text-green-500"></i>
              </a>
            </div>
            <div class="h-40 w-full">
              <el-image
                :src="retailerDetail.logo"
                :preview-src-list="[retailerDetail.logo]"
                alt="Business Logo"
                fit="contain"
                class="h-full w-full bg-white rounded my-2"
              >
                <div
                  slot="placeholder"
                  class="h-full w-full grid place-content-center"
                >
                  <div class="text-sm text-gray-500">Loading...</div>
                </div>
              </el-image>
            </div>
          </div>
        </el-col>
        <el-col :span="24" :lg="8" class="mb-8">
          <div class="mb-8">
            <div class="text-gray-500 underline">Document ID Number:</div>
            {{
              retailerDetail.documentIdNumber
                ? retailerDetail.documentIdNumber
                : '--'
            }}
          </div>
          <div v-if="retailerDetail.documentIdCertificate">
            <div class="flex space-x-6">
              <div class="text-gray-500 underline">Document ID Certificate</div>
              <a :href="retailerDetail.documentIdCertificate">
                <i class="bx bx-download bx-sm text-green-500"></i>
              </a>
            </div>
            <div class="h-40 w-full">
              <el-image
                :src="retailerDetail.documentIdCertificate"
                :preview-src-list="[retailerDetail.documentIdCertificate]"
                fit="contain"
                alt="Document ID Certificate"
                class="h-full w-full bg-white rounded my-2"
              >
                <div
                  slot="placeholder"
                  class="h-full w-full grid place-content-center"
                >
                  <div class="text-sm text-gray-500">Loading...</div>
                </div>
              </el-image>
            </div>
          </div>
        </el-col>
        <el-col :span="24" :lg="8" class="mb-8">
          <div class="mb-8">
            <div class="text-gray-500 underline">VAT Registration Number:</div>
            {{
              retailerDetail.vatRegistrationNumber
                ? retailerDetail.vatRegistrationNumber
                : '--'
            }}
          </div>
          <div v-if="retailerDetail.vatRegistrationCertificate">
            <div class="flex space-x-6">
              <div class="text-gray-500 underline">
                Vat Registration Certificate
              </div>
              <a :href="retailerDetail.vatRegistrationCertificate">
                <i class="bx bx-download bx-sm text-green-500"></i>
              </a>
            </div>

            <div class="h-40 w-full">
              <el-image
                :src="retailerDetail.vatRegistrationCertificate"
                :preview-src-list="[retailerDetail.vatRegistrationCertificate]"
                alt="VAT Registration Certificate"
                fit="contain"
                class="h-full w-full bg-white rounded my-2"
              >
                <div
                  slot="placeholder"
                  class="h-full w-full grid place-content-center"
                >
                  <div class="text-sm text-gray-500">Loading...</div>
                </div>
              </el-image>
            </div>
          </div>
        </el-col>
        <el-col v-if="retailerDetail.notes">
          <div class="text-gray-500 underline">Notes:</div>
          <p>{{ retailerDetail.notes }}</p>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<script>
import kyc from '~/mixins/kyc'

export default {
  name: 'KYCDetails',

  mixins: [kyc],

  props: {
    retailerDetail: {
      type: Object,
      required: true,
    },
  },

  created() {
    this.getStatusColor()
  },

  methods: {
    getStatusColor() {
      const statuses = {
        verified: 'green',
        pending: 'yellow',
        rejected: 'red',
        incomplete: 'gray',
      }

      return this.kycStatus && statuses[this.kycStatus.toLowerCase()]
    },
  },
}
</script>

<style></style>
