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
            Update
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
                :precision="2"
                :step="100"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="price" label="Price">
              <el-input-number
                v-model="product.price"
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
          <el-col :span="8">
            <el-form-item prop="tags" label="Tags">
              <dynamic-input-tags v-model="product.tags" />
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
            <el-form-item label="Product Images">
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

              <div
                v-show="product.existingImages && product.existingImages.length"
                class="mt-4"
              >
                <div class="text-gray-500">Existing Images</div>
                <div class="flex flex-wrap">
                  <div
                    v-for="file in product.existingImages"
                    :key="file.fileName"
                    class="h-40 w-1/6 m-2 rounded overflow-hidden bg-gray-100"
                  >
                    <img
                      :src="file.fileDownloadUri"
                      :alt="file.fileName"
                      class="h-full w-full object-center object-contain"
                    />
                  </div>
                </div>
              </div>
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

export default {
  name: 'EditProduct',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'Edit Product ',
  },

  mixins: [crudActions],

  components: { ImageCropper },

  data() {
    let priceValidation = (rule, value, callback) => {
      if (value > this.product.mrp)
        callback(new Error('Price cannot exceed the MRP'))
      else callback()
    }
    return {
      breadcrumbs: [
        { title: 'Products', path: '/cms/products' },
        { title: 'Edit Product', path: '' },
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
  },

  async fetch() {
    const brands = await this.$axios.get('/auth/brand/list')
    const categories = await this.$axios.get('/auth/category/list/parent')
    const productDetail = await this.$axios.get(
      `/auth/product/${this.$route.params.id}`
    )

    if (this.isAdmin) {
      const wholesalers = await this.$axios.get('/auth/user/list/wholesaler')
      this.wholesalerList = wholesalers.data
    }

    this.brandList = brands.data
    this.categories = categories.data
    this.setProductDetail(productDetail.data)
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.handleCreate()
        }
      })
    },

    setProductDetail(detail) {
      this.product.id = detail.id
      this.product.name = detail.name
      this.product.brandId = detail.brandId
      this.product.categoryId = detail.categoryId
      this.product.mrp = detail.mrp
      this.product.price = detail.price
      this.product.description = detail.description
      this.product.shippingAndDelivery = detail.shippingAndDelivery
      this.product.paymentAndReturn = detail.paymentAndReturn
      this.product.productCondition = detail.productCondition
      this.product.deal = detail.deal
      this.product.tags = detail.tags
      this.product.existingImages = detail.files
      this.product.wholesalerId = detail.wholesalerId
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
      formData.append('deal', this.product.deal)
      this.product.images.forEach((image) => {
        formData.append('images', image)
      })
      this.product.tags.forEach((tag) => {
        formData.append('tags', tag)
      })

      const success = await this.update({
        url: `/auth/product/${this.product.id}?wholesalerId=${this.product.wholesalerId}`,
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
