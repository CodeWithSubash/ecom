<template>
  <div class="w-1/3 mb-5">
    <el-input
      v-model="keyword"
      @input="$emit('input', $event)"
      :placeholder="placeholder"
      clearable
      @clear="onSearch"
      @keyup.enter.native="onSearch"
    >
      <template #append>
        <el-button
          size="small"
          icon="el-icon-search"
          @click="onSearch"
        ></el-button>
      </template>
    </el-input>
  </div>
</template>

<script>
export default {
  name: 'TableSearch',

  props: {
    value: {
      type: String,
      required: true,
    },
    onSearch: {
      type: Function,
      required: true,
    },
    placeholder: {
      type: String,
      default: 'Type to Search',
    },
  },

  data() {
    return {
      keyword: this.value,
    }
  },
}
</script>

<style></style>
