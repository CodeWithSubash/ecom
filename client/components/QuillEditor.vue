<template>
  <client-only>
    <quill-editor
      ref="editor"
      v-model="content"
      @input="$emit('input', $event)"
      :options="editorOption"
    />
  </client-only>
</template>

<script>
export default {
  name: 'QuillEditor',

  props: {
    value: {
      type: String,
      default: '',
    },
  },

  data() {
    return {
      content: '',
      editorOption: {
        // Some Quill options...
        theme: 'snow',
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'],
            // ['blockquote', 'code-block'],
          ],
        },
      },
    }
  },
}
</script>

<style lang="scss" scoped>
.quill-editor {
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
}
</style>
