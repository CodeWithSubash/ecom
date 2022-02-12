<template>
  <div>
    <el-dialog
      title="Select & Crop Image"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="handleClose"
    >
      <template>
        <el-row :gutter="20">
          <el-col :span="cropPreview ? 16 : 24">
            <vue-anka-cropper
              :options="cropperOptions"
              @cropper-error="someAction(errorMessage)"
              @cropper-file-selected="setImageFile"
              @cropper-preview="setCropPreview"
              @cropper-saved="saveCropImage"
              @cropper-cancelled="cancelCrop"
            ></vue-anka-cropper>
          </el-col>
          <el-col v-if="cropPreview" :span="8">
            <div class="flex items-end justify-between mb-4">
              <div class="font-medium text-lg">Options</div>
              <el-button size="small" @click="resetOptions">Reset</el-button>
            </div>
            <div class="border border-dashed rounded-md p-4 mb-4">
              <div>
                <span>Aspect Ratio: </span>
                <span class="font-medium">
                  {{ cropperOptions.aspectRatio }}
                </span>
              </div>
              <el-slider
                v-model="cropperOptions.aspectRatio"
                :min="0.2"
                :max="5"
                :step="0.01"
                show-stops
              >
              </el-slider>
            </div>
            <div class="border border-dashed rounded-md p-4 mb-4">
              <div>
                <span>Cropped Width: </span>
                <span class="font-medium">
                  {{ cropperOptions.croppedWidth }}px
                </span>
              </div>
              <el-slider
                v-model="cropperOptions.croppedWidth"
                :max="800"
                :min="40"
                :step="1"
                show-stops
              >
              </el-slider>
            </div>
            <div class="border border-dashed rounded-md p-4">
              <div>
                <span>Cropped Height: </span>
                <span class="font-medium">
                  {{ cropperOptions.croppedHeight }}px
                </span>
              </div>
              <el-slider
                v-model="cropperOptions.croppedHeight"
                :max="800"
                :min="40"
                :step="1"
                show-stops
              >
              </el-slider>
            </div>
          </el-col>
        </el-row>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import vueAnkaCropper from 'vue-anka-cropper'

export default {
  components: { vueAnkaCropper },

  props: {
    aspectRatio: {
      type: [String, Number],
      default: 1,
    },
    cHeight: {
      type: [String, Number],
      default: 400,
    },
    cWidth: {
      type: [String, Number],
      default: 400,
    },
  },

  data() {
    return {
      dialogVisible: true,
      cropPreview: '',

      cropperOptions: {
        aspectRatio: 1,
        closeOnSave: true,
        cropArea: 'box',
        croppedHeight: 400,
        croppedWidth: 400,
        cropperHeight: false,
        dropareaMessage: 'Drop file here or use the button below.',
        frameLineDash: [5, 3],
        frameStrokeColor: 'rgba(255, 255, 255, 0.8)',
        handleFillColor: 'rgba(255, 255, 255, 0.2)',
        handleHoverFillColor: 'rgba(255, 255, 255, 0.4)',
        handleHoverStrokeColor: 'rgba(255, 255, 255, 1)',
        handleSize: 10,
        handleStrokeColor: 'rgba(255, 255, 255, 0.8)',
        layoutBreakpoint: 850,
        maxCropperHeight: 768,
        maxFileSize: 8000000,
        overlayFill: 'rgba(0, 0, 0, 0.5)',
        previewOnDrag: true,
        previewQuality: 0.65,
        resultQuality: 0.8,
        resultMimeType: 'image/jpeg',
        selectButtonLabel: 'Select Image',
        showPreview: true,
        skin: 'light',
        uploadData: {},
        uploadTo: false,
      },
    }
  },

  computed: {
    initialProps() {
      return {
        aspectRatio: this.aspectRatio,
        croppedHeight: this.cHeight,
        croppedWidth: this.cWidth,
      }
    },
  },

  watch: {
    initialProps: {
      deep: true,
      immediate: true,
      handler(nv) {
        this.cropperOptions = {
          ...this.cropperOptions,
          ...this.initialProps,
        }
      },
    },
  },

  methods: {
    setImageFile(file) {
      //   console.log('image file', file)
      this.cropPreview = file
    },
    setCropPreview(imgSrc) {
      // console.log(imgSrc)
    },
    saveCropImage(cropData) {
      this.$emit('handleCropImage', cropData.croppedFile)
      this.cancelCrop()
    },
    cancelCrop() {
      this.$emit('handleCancel')
    },

    handleClose(done) {
      this.$emit('handleCancel')
      done()
    },

    resetOptions() {
      this.cropperOptions = { ...this.cropperOptions, ...this.initialProps }
    },
  },
}
</script>

<style>
@import '../node_modules/vue-anka-cropper/dist/VueAnkaCropper.css';

.ankaCropper__navButton {
  min-height: 2rem;
  min-width: 2rem;
}

.ankaCropper__saveButton {
  display: flex;
  height: 2.2rem;
}
</style>
