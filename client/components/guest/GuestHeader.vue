<template>
  <div>
    <div
      class="
        absolute
        top-0
        left-0
        right-0
        w-full
        z-10
        backdrop-filter backdrop-blur-xs
        hover:bg-white hover:bg-opacity-90
        transition
        ease-in
        duration-300
      "
    >
      <div
        class="
          default-container
          hidden
          py-5
          lg:flex
          justify-between
          items-center
        "
      >
        <app-logo />
        <ul class="flex space-x-10">
          <base-dropdown
            v-for="menu in navMenus"
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
          </base-dropdown>
        </ul>
        <div>
          <nuxt-link v-if="!$auth.loggedIn" to="/login"> Login </nuxt-link>
        </div>
      </div>
    </div>
    <!-- Small Screen  -->
    <div class="flex lg:hidden justify-between items-center p-4">
      <div class="text-red-600" @click="sidebar = !sidebar">
        <i class="bx bx-menu bx-md"></i>
      </div>
      <app-logo />
      <div></div>
    </div>
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
        <li><nuxt-link to="/login">Login</nuxt-link></li>
      </ul>
    </navbar-aside>
  </div>
</template>

<script>
import AppLogo from '../ui/AppLogo.vue'
import NavbarAside from '../home/NavbarAside.vue'

export default {
  name: 'GuestHeader',

  components: { AppLogo, NavbarAside },

  data() {
    return {
      sidebar: false,
      navMenus: [
        { name: 'Home', path: '/home' },
        { name: 'About', path: '/about' },
        { name: 'Products', path: '/shop' },
        { name: 'Contact', path: '/contact' },
      ],
    }
  },

  methods: {
    goto(path) {
      this.$router.push(path)
      this.sidebar = false
    },
  },
}
</script>

<style></style>
