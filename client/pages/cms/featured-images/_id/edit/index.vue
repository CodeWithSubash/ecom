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
            Update
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
        v-loading="$fetchState.pending || loading"
        :rules="rules"
        ref="featuredImageForm"
        :model="featuredImage"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="14">
            <el-form-item label="Featured Image">
              <image-selector
                :image-url="
                  featuredImage.files && featuredImage.files.fileDownloadUri
                "
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
  name: 'EditFeatureImage',

  layout: 'cms',

  middleware: 'admin',

  head: {
    title: 'Edit Featured Image ',
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
          title: 'Edit Feature Image',
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
    const categoryRes = await this.$axios.get('/auth/category/list/parent')

    const featuredImageDetail = await this.$axios.get(
      `auth/featured/${this.$route.params.id}`
    )

    this.categories = categoryRes.data

    this.featuredImage = featuredImageDetail.data
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
      const allowedKeys = [
        'id',
        'title',
        'subTitle',
        'description',
        'logoFile',
        'categoryId',
        'featuredType',
      ]
      const payload = Object.keys(this.featuredImage)
        .filter((key) => allowedKeys.includes(key))
        .reduce((obj, key) => {
          obj[key] = this.featuredImage[key]
          return obj
        }, {})

      let formData = createFormData(payload)
      const success = await this.update({
        url: `/auth/featured/${this.featuredImage.id}`,
        payload: formData,
      })
      if (success) this.$router.push('/cms/featured-images')
    },
  },
}
</script>

<style></style>
