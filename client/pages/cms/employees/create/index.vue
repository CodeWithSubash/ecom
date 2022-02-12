<template>
  <with-header>
    <template #header>
      <div class="flex items-center justify-between">
        <cms-breadcrumbs :items="breadcrumbs" />
        <div class="flex space-x-2">
          <el-button
            type="success"
            size="medium"
            :disabled="loading"
            @click="submitForm('employeeForm')"
          >
            Save
          </el-button>
          <el-button size="medium" @click="$router.push('/cms/employees')">
            Cancel
          </el-button>
        </div>
      </div>
    </template>
    <el-card>
      <el-form
        v-loading="loading"
        :model="employee"
        :rules="rules"
        ref="employeeForm"
        label-position="top"
        style="width: 100%"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="username" label="Username">
              <el-input v-model.trim="employee.username"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="email" label="Email">
              <el-input
                v-model.trim="employee.email"
                type="email"
                placeholder="example@mail.com"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="role" label="Role">
              <el-select v-model="employee.role" placeholder="Select Role">
                <el-option
                  v-for="item in filteredRoles"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="userEmployee.designation" label="Designation">
              <el-select
                v-model="employee.userEmployee.designation"
                placeholder="Select Designation"
              >
                <el-option
                  v-for="item in filteredDesignations"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="firstname" label="First Name">
              <el-input v-model="employee.userEmployee.firstname"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="lastname" label="Last Name">
              <el-input v-model="employee.userEmployee.lastname"></el-input>
            </el-form-item>
          </el-col>
          <el-col v-if="employee.role === 'SALES'" :span="8">
            <el-form-item label="Commission">
              <el-input-number
                v-model="employee.userEmployee.commission"
                :min="0"
                :max="100"
                :step="5"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Phone">
              <el-input
                v-model="employee.userEmployee.phone"
                placeholder="Primary phone number"
                type="tel"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Mobile">
              <el-input
                v-model="employee.userEmployee.mobile"
                placeholder="Primary mobile number"
                type="tel"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Alternative Phone">
              <el-input
                v-model="employee.userEmployee.alternativePhone"
                placeholder="Secondary phone number"
                type="tel"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Alternative Mobile">
              <el-input
                v-model="employee.userEmployee.alternativeMobile"
                placeholder="Secondary mobile number"
                type="tel"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Street Address">
              <el-input
                v-model="employee.userEmployee.street"
                placeholder="Street Address 1"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Street Address 2">
              <el-input
                v-model="employee.userEmployee.street2"
                placeholder="Street Address 2"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="City">
              <el-input v-model="employee.userEmployee.city"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="State">
              <el-input v-model="employee.userEmployee.state"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Zip Code">
              <el-input v-model="employee.userEmployee.zipCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </with-header>
</template>

<script>
import crudActions from '~/mixins/crudActions'
import Employee from '~/models/employee'

export default {
  name: 'CreateEmployee',

  layout: 'cms',

  middleware: 'admin-wholesaler',

  head: {
    title: 'Create Employee',
  },

  mixins: [crudActions],

  data() {
    return {
      breadcrumbs: [
        {
          title: 'Employees',
          path: '/cms/employees',
        },
        {
          title: 'Create Employee',
          path: '',
        },
      ],

      employee: new Employee(),
      roles: Employee.roles,
      designations: Employee.designations,
      rules: Employee.validationRules,
    }
  },

  computed: {
    loading() {
      return this.$store.state.isLoading
    },

    isSuperAdmin() {
      return this.$store.getters['user/isSuperAdmin']
    },

    isWholsaler() {
      return this.$store.getters['user/isWholeSaler']
    },

    filteredRoles() {
      const superAccessRoles = ['ADMIN', 'WHOLESALER']
      if (this.isSuperAdmin) return this.roles
      return this.roles.filter((r) => !superAccessRoles.includes(r.value))
    },

    filteredDesignations() {
      const wholesalerAccess = [
        'Assistant',
        'Accountant',
        'Sales Representative',
      ]
      if (!this.isWholsaler) return this.designations
      return this.designations.filter((d) => wholesalerAccess.includes(d))
    },
  },

  watch: {
    'employee.role': {
      immediate: true,
      handler(nv) {
        if (nv !== 'SALES') {
          this.employee.userEmployee = Object.keys(this.employee.userEmployee)
            .filter((key) => key !== 'commission')
            .reduce((obj, key) => {
              obj[key] = this.employee.userEmployee[key]
              return obj
            }, {})
        } else {
          this.employee.userEmployee = {
            ...this.employee.userEmployee,
            commission: 0,
          }
        }
      },
    },
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.handleCreate()
        }
      })
    },

    async handleCreate() {
      const success = await this.create({
        url: '/auth/user/employee',
        payload: this.employee,
      })
      if (success) this.$router.push('/cms/employees')
    },
  },
}
</script>

<style></style>
