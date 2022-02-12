<template>
  <div class="relative flex box-content">
    <div class="default-container">
      <div class="h-full w-full lg:w-1/2 py-20">
        <div
          class="flex items-center space-x-2 mb-6 group"
          data-aos="slide-right"
          data-aos-anchor-placement="top-bottom"
        >
          <div class="text-7xl lg:text-9xl font-medium text-red-600">30</div>
          <div class="pr-4">
            <div class="text-xl lg:text-3xl lg:mb-2 text-gray-600">
              Years of
            </div>
            <div class="text-3xl lg:text-5xl font-medium">Experience</div>
          </div>
          <div class="hidden lg:block">
            <i class="bx bx-trophy bx-tada text-8xl"></i>
          </div>
        </div>
        <div class="lg:mr-10">
          <div
            class="mb-6"
            data-aos="slide-right"
            data-aos-anchor-placement="top-bottom"
          >
            Kelton Pooran Manufacturers Ltd has been in operation for over 30
            years.
          </div>
          <div
            class="mb-6"
            data-aos="slide-right"
            data-aos-anchor-placement="top-bottom"
          >
            Located in La Romaine, we manufacture and supply steel windows for
            Abel Building Solutions.
          </div>
          <div
            class="mb-6"
            data-aos="slide-right"
            data-aos-anchor-placement="top-bottom"
          >
            We also manufacture wheelbarrows, ridging, roof bolts, hold-down
            bolts, nipples, washers, hinges, sag rods, face basin brackets,
            piston hinges, shovels and spades.
          </div>
          <div data-aos="slide-right" data-aos-anchor-placement="top-bottom">
            With a small, dedicated team, we are committed to supplying durable
            and robust products at affordable prices.
          </div>
        </div>
      </div>
    </div>
    <!-- Side Image  -->
    <img
      :src="require('~/assets/placeholders/wheelbarrow.jpg')"
      alt="Intro"
      class="
        hidden
        lg:block
        absolute
        right-0
        top-0
        h-full
        lg:w-1/2
        object-cover object-center
      "
      data-aos="fade-up"
    />
  </div>
</template>

<script>
export default {
  name: 'CompanyIntro',
}
</script>

<style></style>
