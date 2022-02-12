<template>
  <div class="relative">
    <vue-html2pdf
      :show-layout="controlValue.showLayout"
      :float-layout="controlValue.floatLayout"
      :enable-download="controlValue.enableDownload"
      :preview-modal="controlValue.previewModal"
      :filename="controlValue.filename"
      :paginate-elements-by-height="controlValue.paginateElementsByHeight"
      :pdf-quality="controlValue.pdfQuality"
      :pdf-format="controlValue.pdfFormat"
      :pdf-orientation="controlValue.pdfOrientation"
      :pdf-content-width="controlValue.pdfContentWidth"
      :manual-pagination="controlValue.manualPagination"
      @progress="onProgress($event)"
      ref="html2Pdf"
    >
      <section slot="pdf-content">
        <slot />
      </section>
    </vue-html2pdf>

    <div
      v-if="showProgress"
      class="fixed bottom-10 right-8 w-1/3 p-4 border bg-white shadow-lg rounded"
    >
      <div>Generating PDF</div>
      <el-progress :percentage="progress"></el-progress>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GeneratePDF',

  props: {
    fileName: {
      type: String,
      default: 'PDF',
    },
  },

  data() {
    return {
      controlValue: {
        showLayout: false,
        floatLayout: true,
        enableDownload: true,
        previewModal: false,
        paginateElementsByHeight: 1100,
        manualPagination: false,
        filename: '',
        pdfQuality: 2,
        pdfFormat: 'a4',
        pdfOrientation: 'portrait',
        pdfContentWidth: '800px',
      },

      progress: 0,
    }
  },

  computed: {
    showProgress() {
      return this.progress > 0 && this.progress < 100
    },
  },

  watch: {
    fileName: {
      immediate: true,
      handler(nv) {
        this.controlValue = {
          ...this.controlValue,
          filename: nv,
        }
      },
    },
  },

  methods: {
    downloadPDF() {
      this.$refs.html2Pdf.generatePdf()
    },

    onProgress(progress) {
      this.progress = progress
    },
  },
}
</script>

<style></style>
