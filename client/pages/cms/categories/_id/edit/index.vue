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
            @click="submitForm('categoryForm')"
          >
            Update
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/categories')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="loading || $fetchState.pending"
        :rules="rules"
        ref="categoryForm"
        :model="category"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Logo">
              <!-- <image-selector
                :image-url="category.logoPath"
                @onSelect="category.logoFile = $event"
                class="h-40 w-40"
              /> -->
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
                  alt="category Logo"
                  class="h-full w-full object-center object-cover rounded"
                />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="Banner">
              <image-selector
                :image-url="category.bannerPath"
                @onSelect="category.bannerFile = $event"
                class="h-40 w-full"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="name" label="Name">
              <el-input v-model="category.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Parent Category">
              <el-select
                v-model.trim="category.parentId"
                placeholder="Select Parent Category"
              >
                <el-option
                  v-for="item in [
                    { label: 'None', value: null },
                    ...parentCategories,
                  ]"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <image-cropper
        v-if="showCropImage"
        :aspectRatio="1"
        :cHeight="200"
        :cWidth="200"
        @handleCropImage="setCategoryLogo"
        @handleCancel="showCropImage = false"
      />
    </el-card>
  </with-header>
</template>

<script>
import createFormData from '~/utils/createFormData'
import crudActions from '~/mixins/crudActions'
import Category from '~/models/category'

export default {
  name: 'EditCategory',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'Edit Category ',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Categories',
          path: '/cms/categories',
        },
        {
          title: 'Edit Category',
          path: '',
        },
      ],
      category: new Category(),
      parentCategories: [],
      rules: Category.validationRules,

      showCropImage: false,
    }
  },

  async fetch() {
    const detailRes = await this.$axios.get(
      `/auth/category/${this.$route.params.id}`
    )

    this.category = {
      ...this.category,
      id: detailRes.data.id,
      name: detailRes.data.name,
      parentId: detailRes.data.parentId,
      bannerPath: detailRes.data.bannerPath,
      logoPath: detailRes.data.logoPath,
    }

    const categoriesRes = await this.$axios.get('auth/category/list/parent')
    this.parentCategories = categoriesRes.data?.map((pcat) => ({
      label: pcat.value,
      value: pcat.id,
    }))
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },

    previewImage() {
      if (this.category.logoFile) {
        if (typeof this.category.logoFile == 'string')
          return this.category.logoFile

        const src = URL.createObjectURL(this.category.logoFile)
        return src
      }

      if (this.category.logoPath) return this.category.logoPath

      return
    },
  },

  methods: {
    setCategoryLogo(croppedImage) {
      this.category.logoFile = croppedImage
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.handleUpdate()
        }
      })
    },

    async handleUpdate() {
      let formData = await createFormData(this.category)
      const success = await this.update({
        url: `/auth/category/${this.category.id}`,
        payload: formData,
      })
      if (success) this.$router.push(`/cms/categories`)
    },

    doSth(file) {
      console.log(file)
    },
  },
}
</script>

<style></style>
