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
            @click="submitForm('featuredImageForm')"
          >
            Save
          </el-button>
          <el-button
            size="medium"
            @click="$router.push('/cms/featured-images')"
          >
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="loading"
        :rules="rules"
        ref="featuredImageForm"
        :model="featuredImage"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="14">
            <el-form-item prop="logoFile" label="Featured Image">
              <image-selector
                @onSelect="featuredImage.logoFile = $event"
                class="h-80 w-full"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item prop="title" label="Title">
              <el-input v-model="featuredImage.title"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item prop="subTitle" label="Subtitle">
              <el-input v-model="featuredImage.subTitle"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="10">
            <el-form-item prop="description" label="Description">
              <el-input
                v-model="featuredImage.description"
                type="textarea"
                rows="4"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Featured Type">
              <el-select
                v-model="featuredImage.featuredType"
                placeholder="Select  category"
              >
                <el-option
                  v-for="item in featuredTypes"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Category">
              <el-select
                v-model="featuredImage.categoryId"
                placeholder="Select  category"
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
        </el-row>
      </el-form>
    </el-card>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import FeaturedImage from '~/models/featured-image'
import createFormData from '~/utils/createFormData'

export default {
  name: 'CreateFeatureImage',

  layout: 'cms',

  middleware: 'admin',

  head: {
    title: 'Create Featured Image ',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Featured Images',
          path: '/cms/featured-images',
        },
        {
          title: 'Create Feature Image',
          path: '',
        },
      ],
      featuredImage: new FeaturedImage(),
      rules: FeaturedImage.validationRules,
      featuredTypes: FeaturedImage.featuredTypes,

      categories: [],
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/category/list/parent')
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
      let formData = createFormData(this.featuredImage)
      const success = await this.create({
        url: '/auth/featured',
        payload: formData,
      })
      if (success) this.$router.push('/cms/featured-images')
    },
  },
}
</script>

<style></style>
