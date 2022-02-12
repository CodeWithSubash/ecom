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
            @click="submitForm('productForm')"
          >
            Save
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/products')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="$fetchState.pending || loading"
        :rules="rules"
        ref="productForm"
        :model="product"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="name" label="Name">
              <el-input v-model="product.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="productCondition" label="Product Condition">
              <el-select
                v-model="product.productCondition"
                placeholder="Select Product Condition"
              >
                <el-option
                  v-for="item in productConditions"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="mrp" label="MRP">
              <el-input-number
                v-model="product.mrp"
                :min="0"
                :precision="2"
                :step="100"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="price" label="Price">
              <el-input-number
                v-model="product.price"
                :min="0"
                :precision="2"
                :step="100"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="brandId" label="Brand">
              <el-select
                v-model="product.brandId"
                placeholder="Select Product Status"
              >
                <el-option
                  v-for="item in brandList"
                  :key="item.id"
                  :label="item.value"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="categoryId" label="Category">
              <el-select
                v-model="product.categoryId"
                placeholder="Select Product Status"
              >
                <el-option
                  v-for="item in categories"
                  :key="item.id"
                  :label="item.value"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col v-show="subcategoryList && subcategoryList.length" :span="8">
            <el-form-item label="Sub Category">
              <el-select
                v-model="product.sub_categoryId"
                placeholder="Select Product Status"
              >
                <el-option
                  v-for="item in subcategoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item prop="openingStock" label="Opening Stock">
              <el-input-number
                v-model="product.openingStock"
                controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="tags" label="Tags">
              <dynamic-input-tags v-model="product.tags" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Deal">
              <el-switch
                v-model="product.deal"
                active-color="#13ce66"
                active-text="Active"
                inactive-text="Inactive"
              >
              </el-switch>
            </el-form-item>
          </el-col>
          <el-col v-if="isAdmin" :span="8">
            <el-form-item prop="wholesalerId" label="Wholesaler">
              <el-select
                v-model="product.wholesalerId"
                placeholder="Select Wholesaler Status"
              >
                <el-option
                  v-for="item in wholesalerList"
                  :key="item.id"
                  :label="item.value"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="description" label="Description">
              <quill-editor v-model="product.description" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              prop="shippingAndDelivery"
              label="Shipping & Delievery Detail"
            >
              <quill-editor v-model="product.shippingAndDelivery" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              prop="paymentAndReturn"
              label="Payment & Return Detail"
            >
              <quill-editor v-model="product.paymentAndReturn" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item prop="images" label="Product Images">
              <!-- <div class="flex items-start space-x-4">
                <drag-and-drop v-model="product.images" class="h-40 w-2/3" />
                <div class="font-medium">OR</div>
              </div> -->
              <el-button @click="showCropImage = true">
                Select & Crop Image
              </el-button>
              <image-list-preview
                v-if="product.images"
                :fileList="product.images"
                @handleRemove="remove"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <image-cropper
        v-if="showCropImage"
        :aspectRatio="3 / 4"
        :cHeight="488"
        :cWidth="428"
        @handleCropImage="addCroppedImage"
        @handleCancel="showCropImage = false"
      />
    </el-card>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import category from '~/models/category'
import Product from '~/models/product'

import ImageCropper from '~/components/ImageCropper.vue'
import ImageListPreview from '~/components/ImageListPreview.vue'

export default {
  name: 'CreateProduct',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'Create Product ',
  },

  mixins: [crudActions],

  components: { ImageCropper, ImageListPreview },

  data() {
    let priceValidation = (rule, value, callback) => {
      if (value > this.product.mrp)
        callback(new Error('Price cannot exceed the MRP'))
      else callback()
    }
    return {
      breadcrumbs: [
        { title: 'Products', path: '/cms/products' },
        { title: 'Create Product', path: '' },
      ],
      brandList: null,
      categories: null,

      product: new Product(),
      rules: {
        ...Product.validationRules,
        price: [
          {
            required: true,
            message: 'Please enter product price',
            trigger: 'blur',
          },
          { validator: priceValidation, trigger: 'change' },
        ],
      },
      productConditions: Product.productConditions,

      wholesalerList: [],
      selectedWholesaler: null,

      showCropImage: false,
    }
  },

  computed: {
    isAdmin() {
      return this.$store.getters['user/isAdmin']
    },
    loading() {
      return this.$store.state.isLoading
    },

    // categoryList() {
    //   return this.categories?.filter((cat) => !cat.parentId) ?? []
    // },

    // subcategoryList() {
    //   return (
    //     this.product.categoryId &&
    //     this.categories?.filter(
    //       (cat) => cat.parentId == this.product.categoryId
    //     )
    //   )
    // },
  },

  async fetch() {
    const brands = await this.$axios.get('/auth/brand/list')
    const categories = await this.$axios.get('/auth/category/list/parent')

    if (this.isAdmin) {
      const wholesalers = await this.$axios.get('/auth/user/list/wholesaler')
      this.wholesalerList = wholesalers.data
    }

    this.brandList = brands.data
    this.categories = categories.data
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
      let formData = new FormData()
      formData.append('name', this.product.name)
      formData.append('categoryId', this.product.categoryId)
      formData.append('brandId', this.product.brandId)
      formData.append('mrp', this.product.mrp)
      formData.append('price', this.product.price)
      formData.append('description', this.product.description)
      formData.append('shippingAndDelivery', this.product.shippingAndDelivery)
      formData.append('paymentAndReturn', this.product.paymentAndReturn)
      formData.append('productCondition', this.product.productCondition)
      formData.append('openingStock', this.product.openingStock)
      formData.append('deal', this.product.deal)
      formData.append('openingStock', 10)
      this.product.images.forEach((image) => {
        formData.append('images', image)
      })
      this.product.tags.forEach((tag) => {
        formData.append('tags', tag)
      })

      console.log('Wholesaler ID :: ', this.product.wholesalerId)

      const success = await this.create({
        url: `/auth/product?wholesalerId=${
          this.product.wholesalerId != null ? this.product.wholesalerId : 0
        }`,
        payload: formData,
      })
      if (success) this.$router.push('/cms/products')
    },

    addCroppedImage(croppedImage) {
      this.product.images = [...this.product.images, croppedImage]
    },

    remove(i) {
      this.product.images.splice(i, 1)
    },
  },
}
</script>

<style></style>
