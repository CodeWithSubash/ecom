<template>
  <div class="border p-4">
    <div
      class="flex items-center justify-between"
      :class="[!show && 'text-red-600']"
    >
      <div class="lg:text-lg">
        <slot name="title">
          Lorem ipsum dolor sit amet consectetur adipisicing elit. Illum,
          libero.
        </slot>
      </div>
      <div @click="show = !show" class="cursor-pointer">
        <i v-if="show" class="bx bx-plus bx-xs"></i>
        <i v-else class="bx bx-minus bx-xs"></i>
      </div>
    </div>
    <div
      class="
        transition-container
        ease-in-out
        duration-200
        overflow-hidden
        mt-4
        text-gray-600
      "
      :class="show ? 'hidden' : 'block'"
    >
      <slot>
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Debitis iste
        minus inventore sed voluptatem tempore reprehenderit modi, rem suscipit
        aliquid excepturi iure mollitia. Voluptates corrupti molestias voluptate
        dolor quae distinctio.
      </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CollapseItem',

  data() {
    return {
      show: true,
    }
  },
}
</script>

<style></style>
