<template>
  <div class="default-container mb-1">
    <!-- Default Navbar  -->
    <div class="hidden lg:flex justify-between items-center px-2">
      <base-dropdown>
        <template #trigger>
          <div class="flex items-center space-x-2 cursor-pointer">
            <i class="bx bx-menu text-xl"></i>
            <div>Browse Categories</div>
          </div>
        </template>
        <template #dropdown-items>
          <ul class="px-6 py-2 divide-y-2 space-y-2 w-60">
            <li
              v-for="category in categories"
              :key="category.id"
              class="pt-4 pb-2 hover:text-red-600 cursor-pointer"
            >
              <div class="text-sm text-gray-500 hover:text-red-600">
                <span>
                  <i class="bx bx-chevron-right"></i>
                </span>
                {{ category.name }}
              </div>
            </li>
          </ul>
        </template>
      </base-dropdown>
      <!-- Navmenus  -->
      <div class="flex space-x-10">
        <base-dropdown
          v-for="menu in authNavMenus"
          :key="menu.name"
          class="font-medium text-sm"
        >
          <template #trigger>
            <nuxt-link :to="menu.path">
              <div class="uppercase flex navlink">
                <span>{{ menu.name }}</span>
                <span v-show="menu.subMenus">
                  <i class="bx bx-chevron-down ml-1"> </i>
                </span>
              </div>
            </nuxt-link>
          </template>
          <template v-if="menu.subMenus" #dropdown-items>
            <div class="px-8">
              <nuxt-link
                v-for="subMenu in menu.subMenus"
                :key="subMenu.name"
                :to="subMenu.path"
              >
                <div class="py-3 hover:text-red-600 uppercase">
                  {{ subMenu.name }}
                </div>
              </nuxt-link>
            </div>
          </template>
        </base-dropdown>
      </div>
      <!-- Cart  -->
      <cart-info v-if="$auth.loggedIn" />
      <nuxt-link v-else to="/login">
        <base-dropdown>
          <template #trigger>
            <div class="navlink">Login/Register</div>
          </template>
        </base-dropdown>
      </nuxt-link>
    </div>
    <!-- Small Screen Nav  -->
    <div class="flex lg:hidden justify-between items-center py-4">
      <div class="text-red-600" @click="sidebar = !sidebar">
        <i class="bx bx-menu bx-md"></i>
      </div>
      <app-logo />
      <nuxt-link to="/cart"><cart-count /></nuxt-link>
    </div>
    <!-- Side Navbar -->
    <navbar-aside v-model="sidebar">
      <ul class="divide-y">
        <li
          v-for="menu in navMenus"
          :key="menu.name"
          class="p-4"
          @click="goto(menu.path)"
        >
          {{ menu.name }}
        </li>
      </ul>
    </navbar-aside>
  </div>
</template>

<script>
import AppLogo from './AppLogo.vue'
import CartInfo from '~/components/cart-icon/CartInfo.vue'
import NavbarAside from '../home/NavbarAside.vue'

export default {
  name: 'Navbar',

  components: {
    AppLogo,
    CartInfo,
    NavbarAside,
  },

  data() {
    return {
      sidebar: false,
      navMenus: [
        {
          name: 'Home',
          path: `${this.$auth.loggedIn ? '/home' : '/'}`,
          auth: false,
        },
        { name: 'Blog', path: '/blog', auth: false },
        {
          name: 'My Account',
          path: '/my-account',
          subMenus: [
            { name: 'Cart', path: '/cart' },
            { name: 'Checkout', path: '/checkout' },
          ],
          auth: true,
        },
        { name: 'Shop', path: '/shop', auth: false },
        {
          name: 'Contact Us',
          path: '/contact',
          auth: false,
        },
      ],
    }
  },

  computed: {
    authNavMenus() {
      if (!this.$auth.loggedIn)
        return this.navMenus.filter((menu) => menu.auth === false)
      else return this.navMenus
    },
    categories() {
      return this.$store.getters['category/categoryPreview']
    },
  },

  async fetch() {
    const {
      data: { data },
    } = await this.$axios.get('/auth/category/active')

    this.$store.commit('category/SET_CATEGORIES', data)
  },

  methods: {
    goto(path) {
      this.$router.push(path)
      this.sidebar = false
    },
  },
}
</script>

<style>
.navlink {
  &::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: 0;
    height: 1px;
    width: 0%;
    background-color: blue;
    transition: all 200ms ease-in;
  }

  &:hover {
    &::after {
      background-color: red;
      width: 100%;
    }
  }
}
</style>
