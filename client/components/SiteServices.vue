<template>
  <div
    class="
      default-container
      flex flex-col
      lg:flex-row
      divide-y
      lg:divide-y-0 lg:divide-x
      mt-20
    "
  >
    <div
      class="
        h-36
        lg:h-52
        w-full
        lg:w-1/3
        flex
        items-center
        justify-center
        space-x-4
      "
    >
      <i class="bx bxs-truck bx-lg text-red-600"></i>
      <div>
        <div class="text-lg uppercase font-medium">Free Shipping</div>
        <div class="text-sm text-gray-500 font-thin">On all orders of $150</div>
      </div>
    </div>
    <div
      class="h-36 lg:h-52 lg:w-1/3 flex items-center justify-center space-x-4"
    >
      <i class="bx bx-support bx-lg text-red-600"></i>
      <div>
        <div class="text-lg uppercase font-medium">24/7 Support</div>
        <div class="text-sm text-gray-500 font-thin">
          Get help when you need it
        </div>
      </div>
    </div>
    <div
      class="h-36 lg:h-52 lg:w-1/3 flex items-center justify-center space-x-4"
    >
      <i class="bx bx-refresh bx-lg text-red-600"></i>
      <div>
        <div class="text-lg uppercase font-medium">100% Money Back</div>
        <div class="text-sm text-gray-500 font-thin">
          30 day money back guarantee
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SiteServices',
}
</script>

<style></style>
