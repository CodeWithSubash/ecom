<template>
  <div class="px-6 py-2 flex items-center justify-between border-b-2">
    <div></div>
    <!-- <div class="font-medium border px-4 py-1 rounded">Admin Panel</div> -->
    <div class="flex items-center space-x-4">
      <div class="text-sm">
        <span class="text-gray-600"> Good Morning! </span>
        <span class="font -medium"> {{ $auth.user.role }} </span>
      </div>
      <el-dropdown trigger="click">
        <el-avatar
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        ></el-avatar>
        <div>
          <el-dropdown-menu slot="dropdown">
            <nuxt-link to="/cms/profile/change-password">
              <el-dropdown-item icon="el-icon-refresh">
                <span>Change Password</span>
              </el-dropdown-item>
            </nuxt-link>
            <div @click="$auth.logout()">
              <el-dropdown-item icon="el-icon-switch-button">
                <span>Logout</span>
              </el-dropdown-item>
            </div>
          </el-dropdown-menu>
        </div>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import AppLogo from '../ui/AppLogo.vue'

export default {
  name: 'AdminHeader',

  components: { AppLogo },
}
</script>

<style></style>
