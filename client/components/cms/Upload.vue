<template>
  <div>
    <el-upload
      class="avatar-uploader"
      :action="uploadUrl"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      :on-progress="handleUploadProgress"
      :multiple="false"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
  </div>
</template>

<script>
export default {
  name: 'ServerUpload',

  props: {
    directory: {
      type: String,
      default: 'retailer',
    },
    fileType: {
      type: String,
      default: 'logo',
    },
    image: {
      type: String,
      default: '',
    },
  },

  data() {
    return {
      imageUrl: '',
    }
  },

  computed: {
    uploadUrl() {
      return `${$nuxt.context.env.baseUrl}/auth/uploadFile/${this.directory}/${this.fileType}`
    },
  },

  watch: {
    image: {
      immediate: true,
      handler(nv) {
        if (nv) this.imageUrl = nv
      },
    },
  },

  methods: {
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
      this.$emit('imageUploaded', res.fileDownloadUri)
    },
    beforeAvatarUpload(file) {
      const validImageTypes = ['image/jpg', 'image/jpeg', 'image/png']
      const isValidImage = validImageTypes.includes(file.type)

      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isValidImage) {
        this.$message.error('Must be a valid IMAGE format!')
      }
      if (!isLt2M) {
        this.$message.error('Image size can not exceed 2MB!')
      }
      return isValidImage && isLt2M
    },
    handleUploadProgress(event, file, fileList) {
      this.$store.commit('SET_LOADING', true)
      if (event.percent === 100) this.$store.commit('SET_LOADING', false)
    },
  },
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader {
  /* display: grid;
  place-content: center;
  background: red; */
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: '100%';
  height: '100%';
  display: block;
}
</style>
