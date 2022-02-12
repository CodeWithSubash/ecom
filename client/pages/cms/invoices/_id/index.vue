<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />

        <div class="flex space-x-2">
          <el-button
            type="primary"
            size="medium"
            icon="el-icon-printer"
            plain
            @click="printInvoice"
          >
            Print
          </el-button>
          <el-button
            type="success"
            size="medium"
            icon="el-icon-download"
            plain
            @click="downloadPDF"
          >
            Download PDF
          </el-button>
        </div>
      </div>
    </template>

    <div v-loading="$fetchState.pending">
      <invoice-template v-if="invoiceDetail" :invoice-detail="invoiceDetail" />
    </div>

    <generate-pdf
      ref="pdfGenerator"
      :fileName="invoiceNumber"
      :downloadPDF="downloadPDF"
    >
      <div class="p-6">
        <invoice-template
          v-if="invoiceDetail"
          :invoice-detail="invoiceDetail"
        />
      </div>
    </generate-pdf>
  </with-header>
</template>

<script>
import InvoiceTemplate from '~/components/templates/InvoiceTemplate.vue'

export default {
  name: 'InvoiceDetail',

  layout: 'cms',

  head: {
    title: 'Invoices | Invoice Detail',
  },

  components: {
    InvoiceTemplate,
  },

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Invoices',
          path: '/cms/invoices',
        },
        {
          title: 'Invoice Detail',
          path: '',
        },
      ],

      invoiceDetail: null,
    }
  },

  computed: {
    invoiceId() {
      return this.$route.params.id
    },
    invoiceNumber() {
      return this.invoiceDetail?.billNumber ?? '--'
    },
  },

  async fetch() {
    const { data } = await this.$axios.get(`/auth/invoice/${this.invoiceId}`)

    this.invoiceDetail = data
  },

  methods: {
    async printInvoice() {
      await this.$htmlToPaper('printInvoice')
    },

    downloadPDF() {
      const generator = this.$refs.pdfGenerator
      generator.downloadPDF()
    },
  },
}
</script>

<style></style>
