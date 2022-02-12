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
            @click="submitForm('brandForm')"
          >
            Save
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/brands')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="loading"
        :rules="rules"
        ref="brandForm"
        :model="brand"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Logo">
              <div
                class="h-40 w-40 border-2 border-gray-300 border-dashed rounded overflow-hidden"
                @click="showCropImage = true"
              >
                <div
                  v-if="!previewImage"
                  class="h-full w-full flex items-center justify-center cursor-pointer"
                >
                  <i class="bx bx-plus bx-md text-gray-400"></i>
                </div>
                <img
                  v-else
                  :src="previewImage"
                  alt="Brand Logo"
                  class="h-full w-full object-center object-cover rounded"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="name" label="Name">
              <el-input v-model="brand.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <image-cropper
        v-if="showCropImage"
        :aspectRatio="1"
        :cHeight="200"
        :cWidth="200"
        @handleCropImage="setBrandLogo"
        @handleCancel="showCropImage = false"
      />
    </el-card>
  </with-header>
</template>

<script>
import createFormData from '~/utils/createFormData'
import crudActions from '~/mixins/crudActions'
import brand from '~/models/brand'

export default {
  name: 'CreateBrand',

  layout: 'cms',

  middleware: 'admin',

  head: {
    title: 'Create Brand ',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Brands',
          path: '/cms/brands',
        },
        {
          title: 'Create Brand',
          path: '',
        },
      ],
      brand: new brand(),
      rules: brand.validationRules,

      showCropImage: false,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },

    previewImage() {
      if (this.brand.logoFile) {
        if (typeof this.brand.logoFile == 'string') return this.brand.logoFile
        const src = URL.createObjectURL(this.brand.logoFile)
        return src
      }

      if (this.brand.logoPath) return this.brand.logoPath

      return
    },
  },

  methods: {
    setBrandLogo(croppedImage) {
      this.brand.logoFile = croppedImage
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.handleCreate()
        }
      })
    },

    async handleCreate() {
      let formData = createFormData(this.brand)
      const success = await this.create({
        url: '/auth/brand',
        payload: formData,
      })
      if (success) this.$router.push('/cms/brands')
    },
  },
}
</script>

<style></style>
