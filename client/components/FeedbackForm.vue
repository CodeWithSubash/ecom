<template>
  <div class="px-6 py-4 bg-gray-50">
    <div class="text-2xl font-semibold uppercase" data-aos="fade-up">
      Feedback
    </div>
    <div class="mt-6 mb-6" data-aos="fade-up">
      We would love to hear your thoughts, suggestions, concerns, problems with
      anything so we can improve
    </div>
    <div class="flex flex-col space-y-4" data-aos="fade-up">
      <base-input class="w-full lg:w-2/3">
        <template #label>
          Full Name
          <span class="text-red-600">*</span>
        </template>
      </base-input>
      <base-input class="w-full lg:w-2/3">
        <template #label>
          Email Address
          <span class="text-red-600">*</span>
        </template>
      </base-input>
      <base-input class="w-full lg:w-2/3">
        <template #label>
          Subject
          <span class="text-red-600">*</span>
        </template>
      </base-input>
      <base-input textarea>
        <template #label>
          Message
          <span class="text-red-600">*</span>
        </template>
      </base-input>
      <div>
        <base-button onHover>
          <span class="flex items-center">
            Send
            <i class="bx bx-paper-plane ml-2"></i>
          </span>
        </base-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FeedbackForm',
}
</script>

<style></style>
