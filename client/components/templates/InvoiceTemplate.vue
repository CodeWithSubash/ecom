<template>
  <div>
    <el-card id="printInvoice">
      <div class="flex justify-between">
        <div>
          Invoice No:
          <span class="text-red-600">{{
            invoiceDetail ? `#${invoiceDetail.billNumber}` : '--'
          }}</span>
        </div>
        <div>
          Date:
          <span v-if="invoiceDetail">{{
            invoiceDetail | moment('YYYY/MM/DD')
          }}</span>
          <span v-else>'--/--/----'</span>
        </div>
      </div>
      <div class="flex items-center justify-between border-b pb-3 my-6">
        <div class="font-medium text-2xl">Invoice</div>
        <app-logo />
      </div>
      <div>
        <div class="font-medium mb-2 underline">Bill To</div>
        <div class="mb-2 text-red-600">
          {{ invoiceDetail ? invoiceDetail.retailerName : '--' }}
        </div>
        <div v-if="invoiceDetail">
          {{
            invoiceDetail
              ? invoiceDetail.billingAddress
                ? invoiceDetail.billingAddress
                : '--'
              : '--'
          }}
        </div>
      </div>
      <br />
      <div class="py-2 border-b border-black">
        <el-row :gutter="20">
          <el-col :span="3">
            <div class="text-sm uppercase">#</div>
          </el-col>
          <el-col :span="6">
            <div class="text-sm uppercase">Description</div>
          </el-col>
          <el-col :span="5">
            <div class="text-right text-sm uppercase">Quantity</div>
          </el-col>
          <el-col :span="5">
            <div class="text-right text-sm uppercase">Price</div>
          </el-col>
          <el-col :span="5">
            <div class="text-right text-sm uppercase">Total</div>
          </el-col>
        </el-row>
      </div>
      <div
        v-for="(item, index) in invoiceOrders"
        :key="item.id"
        class="py-2 border-b"
      >
        <el-row :gutter="20">
          <el-col :span="3">
            <div class="">{{ index + 1 }}</div>
          </el-col>
          <el-col :span="6">
            <div class="">{{ item.product.productName }}</div>
          </el-col>
          <el-col :span="5">
            <div class="text-right">{{ item.quantity }}</div>
          </el-col>
          <el-col :span="5">
            <div class="text-right">TT$ {{ item.product.price }}</div>
          </el-col>
          <el-col :span="5">
            <div class="text-right">
              TT$ {{ item.product.price * item.quantity }}
            </div>
          </el-col>
        </el-row>
      </div>

      <div class="flex justify-end mt-6">
        <div class="flex flex-col space-y-3">
          <div>
            <span>Subtotal: </span>
            <span>
              TT$ {{ invoiceDetail ? invoiceDetail.totalAmount : 0 }}
            </span>
          </div>
          <div>
            <span>VAT: </span>
            <span>TT$ {{ invoiceDetail ? invoiceDetail.totalVat : 0 }} </span>
          </div>
          <div>
            <span>Payment Method: </span>
            <span>
              {{ invoiceDetail ? invoiceDetail.paymentMethod : '--' }}
            </span>
          </div>
          <div class="font-medium text-2xl">
            <span>Total: </span>
            <span>
              TT$ {{ invoiceDetail ? invoiceDetail.totalAmount : 0 }}
            </span>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import AppLogo from '../ui/AppLogo.vue'

export default {
  name: 'InvoiceTemplate',

  components: { AppLogo },

  props: {
    invoiceDetail: {
      type: Object,
      required: true,
    },
  },

  computed: {
    invoiceOrders() {
      return this.invoiceDetail?.orderDetails
    },
  },
}
</script>

<style></style>
