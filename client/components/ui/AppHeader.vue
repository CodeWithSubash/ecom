<template>
  <div class="default-container">
    <div
      class="
        flex flex-col
        md:flex-row md:justify-between md:items-center
        space-y-4
        md:space-y-0 md:px-2
        py-4
        md:py-7
      "
    >
      <app-logo />
      <search-bar />
      <div class="flex justify-center space-x-6">
        <!-- User  -->
        <base-dropdown v-show="$auth.loggedIn">
          <template #trigger>
            <i class="bx bx-user bx-md"></i>
          </template>
          <template #dropdown-items>
            <ul class="divide-y w-44 shadow">
              <nuxt-link to="/my-account">
                <li class="px-6 py-4">Profile</li>
              </nuxt-link>
              <nuxt-link to="/my-account/change-password">
                <li class="px-6 py-4">Change Password</li>
              </nuxt-link>
              <li
                class="px-6 py-4 hover:text-red-600 cursor-pointer"
                @click="$auth.logout()"
              >
                Logout
              </li>
            </ul>
          </template>
        </base-dropdown>
        <!-- Wishlist  -->
        <nuxt-link to="/wishlist">
          <wishlist-icon />
        </nuxt-link>
      </div>
    </div>
  </div>
</template>

<script>
import AppLogo from '@/components/ui/AppLogo.vue'

export default {
  name: 'AppHeader',

  components: {
    AppLogo,
  },
}
</script>

<style></style>
