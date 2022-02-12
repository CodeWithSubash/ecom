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
            @click="submitForm('couponForm')"
          >
            Save
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/coupons')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="loading"
        :rules="rules"
        ref="couponForm"
        :model="coupon"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="couponCode" label="Code">
              <el-input
                v-model="coupon.couponCode"
                placeholder="Coupon Code"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="couponType" label="Coupon Type">
              <el-select
                v-model="coupon.couponType"
                placeholder="Select coupon type"
              >
                <el-option
                  v-for="type in couponTypes"
                  :key="type"
                  :label="type"
                  :value="type"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="categoryId" label="Category Type">
              <el-select
                v-model="coupon.categoryId"
                placeholder="Select target category"
              >
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.value"
                  :value="category.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="discountValue" label="Discount Value">
              <el-input-number
                v-model="coupon.discountValue"
                :min="0"
                :step="5"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="minimumOrderValue" label="Min Order Value">
              <el-input-number
                v-model="coupon.minimumOrderValue"
                :min="0"
                :step="100"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              prop="maximumDiscountAmount"
              label="Max Discount Value"
            >
              <el-input-number
                v-model="coupon.maximumDiscountAmount"
                :min="0"
                :step="100"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="validFrom" label="Valid From">
              <el-date-picker
                v-model="coupon.validFrom"
                type="date"
                placeholder="Pick valid from"
                value-format="yyyy-MM-dd 00:00:00"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="validUntil" label="Valid Unitl">
              <el-date-picker
                v-model="coupon.validUntil"
                type="date"
                placeholder="Pick valid until"
                value-format="yyyy-MM-dd 00:00:00"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import Coupon from '~/models/coupon'

export default {
  name: 'CreateCoupon',

  layout: 'cms',

  middleware: 'wholesaler',

  head: {
    title: 'Create Coupon ',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Coupons',
          path: '/cms/coupons',
        },
        {
          title: 'Create Coupon',
          path: '',
        },
      ],

      coupon: new Coupon(),
      couponTypes: Coupon.couponTypes,
      rules: Coupon.validationRules,

      categories: [],
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('auth/category/list/parent')
    this.categories = data
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
        url: '/auth/coupon',
        payload: this.coupon,
      })
      if (success) this.$router.push('/cms/coupons')
    },
  },
}
</script>

<style></style>
