<template>
  <div class="flex flex-col items-center border-r relative">
    <app-logo
      class="-mx-4 py-2 transform transition ease-in-out duration-300"
      :class="isCollapse && 'scale-50'"
    />
    <div
      @click="isCollapse = !isCollapse"
      class="
        absolute
        top-12
        -right-4
        z-20
        rounded-full
        shadow
        bg-white
        flex
        items-center
        justify-center
        cursor-pointer
      "
    >
      <i
        class="
          bx bx-chevron-right bx-sm
          transform
          transition
          ease-in-out
          duration-300
        "
        :class="!isCollapse && 'rotate-180'"
      ></i>
    </div>
    <el-menu
      :default-active="$nuxt.$route.path"
      active-text-color="#FF0000"
      class="h-full w-52"
      style="border: none"
      :collapse="isCollapse"
    >
      <template v-for="(menu, idx1) in menus">
        <nuxt-link v-if="!menu.subMenus" :to="menu.path">
          <el-menu-item :key="idx1" :index="menu.path">
            <i :class="menu.icon"></i>
            <span slot="title">{{ menu.title }}</span>
          </el-menu-item>
        </nuxt-link>

        <el-submenu v-else :index="menu.path" :key="idx1">
          <template slot="title">
            <i :class="menu.icon"></i>
            <span slot="title">{{ menu.title }}</span>
          </template>
          <template v-for="(group, idx2) in menu.subMenus.groups">
            <el-menu-item-group :key="idx2.toString()">
              <span slot="title"> {{ group.name }} </span>
              <nuxt-link
                v-for="(menu, idx3) in group.menus"
                :key="`${idx2}-${idx3}`"
                :to="menu.path"
              >
                <el-menu-item :index="menu.path">
                  {{ menu.title }}
                </el-menu-item>
              </nuxt-link>
            </el-menu-item-group>
          </template>
          <template v-for="(menu, idx4) in menu.subMenus.menus">
            <nuxt-link :to="menu.path">
              <el-menu-item :index="menu.path" :key="`${idx1}-${idx4}`">
                {{ menu.title }}
              </el-menu-item>
            </nuxt-link>
          </template>
        </el-submenu>
      </template>
    </el-menu>
  </div>
</template>

<script>
import AppLogo from '~/components/ui/AppLogo.vue'

export default {
  name: 'VerticalNavMenus',

  components: {
    AppLogo,
  },

  props: {
    menus: {
      type: Array,
      required: true,
    },
  },

  data() {
    return {
      isCollapse: false,
    }
  },
}
</script>

<style></style>
