<template>
  <div>
    <div
      class="
        h-16
        px-10
        inline-block
        border border-gray-300
        relative
        group
        overflow-hidden
        cursor-pointer
        tracking-wide
      "
    >
      <div
        class="
          h-full
          flex
          justify-center
          items-center
          transform
          group-hover:-translate-y-full
          transition
          duration-500
          ease-out
        "
      >
        <slot />
      </div>
      <div
        class="
          absolute
          inset-0
          bg-red-500
          text-white
          flex
          justify-center
          items-center
          transform
          translate-y-full
          group-hover:translate-y-0
          transition
          duration-500
          ease-out
        "
      >
        <span
          class="
            transform
            translate-y-4
            group-hover:translate-y-0
            transition
            duration-100
            delay-150
            ease-in
          "
        >
          <slot />
        </span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BaseAnimatedButton',
}
</script>

<style></style>
