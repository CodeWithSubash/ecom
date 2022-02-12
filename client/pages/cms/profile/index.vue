<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
        <div>
          <el-button
            type="success"
            size="medium"
            plain
            @click="$router.push('/cms/profile/change-password')"
          >
            Update Password
          </el-button>
        </div>
      </div>
    </template>

    <div
      v-loading="$fetchState.pending"
      class="flex flex-col lg:flex-row space-x-2"
    >
      <div
        class="w-1/3 bg-white rounded-tl-lg rounded-bl-lg p-8 flex flex-col items-center justify-center space-y-4"
      >
        <div class="bg-gray-100 h-32 w-32 rounded-full"></div>
        <div class="font-medium text-2xl text-center">
          {{ userDetail && userDetail.name }}
        </div>
        <div class="text-gray-400 text-sm">
          {{ userDetail && userDetail.email }}
        </div>
        <div class="bg-red-300 px-2 rounded text-red-50 text-sm">
          {{ userDetail && userDetail.role }}
        </div>
      </div>
      <div class="w-2/3 bg-white rounded-tr-lg rounded-br-lg p-6">
        <div class="grid grid-cols-3 gap-5">
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">Username</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.username
                  ? userDetail.userEmployee.username
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">User Type</div>
            <div>
              {{
                userDetail && userDetail.userEmployee && userDetail.userType
                  ? userDetail.userType
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">Designation</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.designation
                  ? userDetail.userEmployee.designation
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">Phone</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.phone
                  ? userDetail.userEmployee.phone
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">Mobile</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.mobile
                  ? userDetail.userEmployee.mobile
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">Street</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.street
                  ? userDetail.userEmployee.street
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">City</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.city
                  ? userDetail.userEmployee.city
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">State</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.state
                  ? userDetail.userEmployee.state
                  : '--'
              }}
            </div>
          </div>
          <div class="p-4 border-b">
            <div class="text-gray-400 text-sm mb-2">Zip Code</div>
            <div>
              {{
                userDetail &&
                userDetail.userEmployee &&
                userDetail.userEmployee.zipCode
                  ? userDetail.userEmployee.zipCode
                  : '--'
              }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </with-header>
</template>

<script>
export default {
  name: 'Profile',

  layout: 'cms',

  head: {
    title: 'CMS | Profile',
  },

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Profile',
          path: '',
        },
      ],

      userDetail: null,
    }
  },

  async fetch() {
    const { data } = await this.$axios.get('/auth/user')

    this.userDetail = data
  },
}
</script>

<style></style>
