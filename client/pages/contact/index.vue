<template>
  <div>
    <bread-crumb :breadcrumbs="breadcrumbs" />
    <!-- Map  -->
    <div class="bg-gray-400 h-96 -mt-14 mb-14">
      <iframe
        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3541.1809666870395!2d85.03202591498172!3d27.432470382906462!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x39eb499204617df5%3A0xccbec9de0b8e21b8!2sSoftweb%20Developers!5e0!3m2!1sen!2snp!4v1638782593308!5m2!1sen!2snp"
        style="border: 0"
        allowfullscreen=""
        loading="lazy"
        class="h-full w-full"
      ></iframe>
    </div>
    <!-- Contact Detail  -->
    <div class="default-container flex flex-col md:flex-row md:space-x-10">
      <div class="w-full lg:w-1/2 mb-12 md:mb-0">
        <div class="text-2xl font-semibold uppercase mb-10" data-aos="fade-up">
          Get in touch !
        </div>
        <div class="text-base tracking-wide" data-aos="fade-up">
          Kelton Pooran Manufacturers Ltd has been in operation for over 30
          years. With a small, dedicated team, we are committed to supplying
          durable and robust products at affordable prices.
        </div>
        <div data-aos="fade-up" class="my-6">
          <div class="text-base">
            <div class="text-red-600 tracking-wide">
              Kelton Pooran Manufacturers Ltd
            </div>
            <div class="mb-0">44 Bay Rd, Bamboo Village</div>
            <div class="mb-2">La Romaine</div>
            <div class="mb-2">868-657-4716</div>
            <div class="mb-2">keltonpooran.mltd@yahoo.com</div>
          </div>
        </div>
      </div>
      <!-- Contact Form  -->
      <div class="w-full lg:w-1/2">
        <feedback-form />
      </div>
    </div>
  </div>
</template>

<script>
import BreadCrumb from '~/components/ui/BreadCrumb.vue'

export default {
  name: 'Contact',

  layout: 'retailer',

  head: {
    title: 'KMPL | Contact Us',
  },

  components: { BreadCrumb },

  scrollToTop: true,

  data() {
    return {
      breadcrumbs: [
        {
          name: 'Contact Us',
          path: '/contact',
        },
      ],
    }
  },
}
</script>

<style></style>
