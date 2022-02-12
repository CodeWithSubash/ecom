<template>
  <div>
    <div class="mb-2">
      <span v-show="!$slots.label">{{ label }}</span>
      <slot name="label" />
    </div>
    <input
      v-if="!textarea"
      v-bind:value="value"
      v-on:input="$emit('input', $event.target.value)"
      class="border-2 p-2 w-full"
      :placeholder="placeholder"
      :type="type"
      :readonly="readonly"
    />
    <textarea
      v-else
      v-bind:value="value"
      v-on:input="$emit('input', $event.target.value)"
      :rows="rows"
      class="border-2 p-2 w-full"
      :placeholder="placeholder"
    />
    <p class="text-red-600">
      <slot name="error" />
    </p>
  </div>
</template>

<script>
export default {
  name: 'BaseInput',

  props: {
    label: {
      type: String,
      default: '',
    },
    value: {
      type: String,
      default: '',
    },
    placeholder: {
      type: String,
      default: '',
    },
    rows: {
      type: Number,
      default: 3,
    },
    textarea: {
      type: Boolean,
      default: false,
    },
    type: {
      type: String,
      default: 'text',
    },
    readonly: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style></style>
