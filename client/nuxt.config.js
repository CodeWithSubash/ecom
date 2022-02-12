import webpack from 'webpack'

export default {
  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    title: 'KPML',
    htmlAttrs: {
      lang: 'en',
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: ['~/assets/css/main.css'],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
    { src: '@/plugins/aos', ssr: false },
    { src: '@/plugins/boxicons', ssr: false },
    { src: '@/plugins/elementUI', ssr: false },
    { src: '@/plugins/html2Paper', ssr: false },
    { src: '@/plugins/html2Pdf', ssr: false },
    { src: '@/plugins/hooper', ssr: false },
    { src: '@/plugins/repository', ssr: false },
    { src: '@/plugins/vee-validate', ssr: false },
    { src: '@/plugins/vue-moment', ssr: false },
    { src: '@/plugins/vue-quill', ssr: false },
    // { src: '@/plugins/vuex-persist', ssr: false },
  ],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: {
    dirs: [
      '~/components',
      { path: '~/components/base/', prefix: 'base' },
      { path: '~/components/cms/', prefix: 'cms' },
      { path: '~/components/layouts/' },
      { path: '~/components/skeletons/', prefix: 'skeleton' },
    ],
  },

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/tailwindcss
    '@nuxtjs/tailwindcss',
    // https://auth.nuxtjs.org
    '@nuxtjs/google-fonts',
  ],

  // Google fonts
  googleFonts: {
    families: {
      Poppins: [300, 400, 500, 600, 700],
    },
    /* module options */
  },

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/axios
    '@nuxtjs/axios',
    '@nuxtjs/auth-next',
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  axios: {
    baseURL: process.env.VUE_APP_SERVER_API_URL,
  },

  // nuxt auth strategies
  auth: {
    strategies: {
      local: {
        token: {
          property: 'accessToken',
          global: true,
          type: 'Bearer',
        },
        user: {
          property: false,
        },
        endpoints: {
          login: { url: '/auth/login', method: 'post' },
          logout: false,
          user: { url: '/auth/user', method: 'get' },
        },
      },
    },
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    plugins: [
      new webpack.ProvidePlugin({
        _: 'lodash',
      }),
    ],
  },

  // loader
  loading: {
    color: 'red',
    height: '4px',
  },

  env: {
    baseUrl: process.env.VUE_APP_SERVER_API_URL,
  },
}
