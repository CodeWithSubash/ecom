import Vue from 'vue'
import VueHtmlToPaper from 'vue-html-to-paper'

const options = {
  name: '_blank',
  specs: ['fullscreen=yes', 'titlebar=yes', 'scrollbars=yes'],
  styles: [
    'https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css',
    'https://unpkg.com/element-ui/lib/theme-chalk/index.css',
  ],
  timeout: 1000, // default timeout before the print window appears
  autoClose: true, // if false, the window will not close after printing
  windowTitle: 'KMPL',
}

Vue.use(VueHtmlToPaper, options)
