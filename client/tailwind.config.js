const colors = require('tailwindcss/colors')

module.exports = {
  purge: {
    content: [
      `components/**/*.{vue,js}`,
      `layouts/**/*.vue`,
      `pages/**/*.vue`,
      `plugins/**/*.{js,ts}`,
      `nuxt.config.{js,ts}`,
    ],
  },
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      backdropBlur: {
        xs: '2px',
      },
      colors: {
        emerald: colors.emerald,
        // gray: colors.trueGray,
      },
    },
  },
  variants: {
    extend: {
      backgroundColor: ['odd'],
      backdropGrayscale: ['hover'],
      display: ['group-hover'],
      padding: ['last'],
      scale: ['group-hover'],
      textColor: ['odd'],
      translate: ['group-hover'],
      visibility: ['group-hover'],
      width: ['group-hover'],
    },
  },
  plugins: [require('@tailwindcss/line-clamp')],
}
