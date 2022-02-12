<template>
  <with-side-menus>
    <template #sidebar>
      <v-navmenus :menus="menus" />
    </template>
    <div class="flex-grow h-screen max-h-screen overflow-y-auto flex flex-col">
      <cms-header />
      <div class="flex-grow">
        <nuxt />
      </div>
    </div>
  </with-side-menus>
</template>

<script>
import sales from '~/middleware/sales'
export default {
  name: 'Admin',

  middleware: ['auth', 'cms'],

  data() {
    return {
      sideMenus: [
        // {
        //   title: 'Navigator One',
        //   icon: 'el-icon-location',
        //   path: '',
        //   subMenus: {
        //     groups: [
        //       {
        //         name: 'Group One',
        //         menus: [
        //           { title: 'Item One', path: '' },
        //           { title: 'Item Two', path: '' },
        //         ],
        //       },
        //       {
        //         name: 'Group Two',
        //         menus: [{ title: 'Item Three', path: '' }],
        //       },
        //     ],
        //     menus: [{ title: 'Item Four', path: '' }],
        //   },
        // },
        // {
        //   title: 'Navigator Two',
        //   icon: 'el-icon-menu',
        //   path: '',
        // },
        {
          title: 'Dashboard',
          icon: 'el-icon-odometer',
          path: '/cms/dashboard',
        },
        {
          title: 'Brands',
          icon: 'el-icon-price-tag',
          path: '/cms/brands',
        },
        {
          title: 'Categories',
          icon: 'el-icon-folder-opened',
          path: '/cms/categories',
        },
        {
          title: 'Products',
          icon: 'el-icon-box',
          path: '/cms/products',
        },
        {
          title: 'Stocks',
          icon: 'bx bx-line-chart text-lg ml-0.5 mr-2',
          path: '/cms/stocks',
        },
        {
          title: 'Featured Images',
          icon: 'el-icon-picture-outline',
          path: '/cms/featured-images',
        },
        {
          title: 'Coupons',
          icon: 'el-icon-discount',
          path: '/cms/coupons',
        },
        // {
        //   title: 'Reports',
        //   icon: 'el-icon-document',
        //   path: '/cms/reports',
        //   subMenus: {
        //     menus: [
        //       {
        //         title: 'Invoice',
        //         path: '/cms/reports/invoice',
        //       },
        //       {
        //         title: 'Retailer',
        //         path: '/cms/reports/retailer',
        //       },
        //     ],
        //   },
        // },
        {
          title: 'Retailers',
          icon: 'el-icon-user',
          path: '/cms/sales/retailers',
        },
        {
          title: 'Orders',
          icon: 'el-icon-document-checked',
          path: '/cms/orders',
        },
        {
          title: 'Invoices',
          icon: 'el-icon-s-ticket',
          path: '/cms/invoices',
        },
        {
          title: 'Users',
          icon: 'el-icon-user',
          path: '/cms',
          subMenus: {
            menus: [
              {
                title: 'Employees',
                path: '/cms/employees',
              },
              {
                title: 'Retailers',
                path: '/cms/retailers',
              },
              // {
              //   title: 'Roles',
              //   path: '/users/roles',
              // },
              // {
              //   title: 'Permissions',
              //   path: '/users/permission',
              // },
            ],
          },
        },
        {
          title: 'Time Log',
          icon: 'el-icon-watch',
          path: '/cms/sales',
          subMenus: {
            menus: [
              {
                title: 'Clock In',
                path: '/cms/sales',
              },
              {
                title: 'Clock Out',
                path: '/cms/sales',
              },
            ],
          },
        },
      ],
    }
  },

  computed: {
    isWholeSaler() {
      return this.$store.getters['user/isWholeSaler']
    },

    isSalesPerson() {
      return this.$store.getters['user/isSalesPerson']
    },

    menus() {
      const adminFilters = ['Time Log', 'Retailers']
      const wholeSalerFilter = [
        'Brands',
        'Featured Images',
        'Time Log',
        'Retailers',
      ]
      const salesPersonFilter = [
        'Brands',
        'Featured Images',
        'Categories',
        'Products',
        'Stocks',
        'Coupons',
        'Users',
      ]

      if (this.isWholeSaler)
        return this.sideMenus.filter(
          (sm) => !wholeSalerFilter.includes(sm.title)
        )

      if (this.isSalesPerson)
        return this.sideMenus.filter(
          (sm) => !salesPersonFilter.includes(sm.title)
        )

      return this.sideMenus.filter((sm) => !adminFilters.includes(sm.title))
    },
  },
}
</script>

<style></style>
