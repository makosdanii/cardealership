<template>
  <DataTable
    ref="dataTable"
    :headers="headers"
    :items="dealers"
    @closed="() => close()"
    @saved="() => save()"
    @deleted="(item) => deleteItem(item)"
    @onEdit="(idx) => onEdit(idx)"
  >
    <template #button-text>{{ buttonName }}</template>
    <template #card-name>{{ buttonName.split(" ")[1] }}</template>
    <template #card-container>
      <v-container>
        <v-row>
          <v-col cols="12" sm="6" md="4">
            <validation-provider
              v-slot="{ errors }"
              name="Email"
              :rules="{ required: true, email: true }"
            >
              <v-text-field
                v-model="editedItem.email"
                label="Email"
                :error-messages="errors"
                :disabled="editedItem.id !== 0"
              ></v-text-field>
            </validation-provider>
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <validation-provider
              v-slot="{ errors }"
              name="Name"
              :rules="{ required: true }"
            >
              <v-text-field
                v-model="editedItem.name"
                label="Name"
                :error-messages="errors"
              ></v-text-field>
            </validation-provider>
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <validation-provider
              v-slot="{ errors }"
              name="Password"
              :rules="{
                required: true,
                min: 8,
                regex: '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$',
              }"
            >
              <v-text-field
                type="password"
                v-model="editedItem.password"
                label="Password"
                :error-messages="errors"
              ></v-text-field>
            </validation-provider>
          </v-col>
          <v-col cols="12" sm="6" md="4">
            <validation-provider
              v-slot="{ errors }"
              name="Rolename"
              :rules="{ required: true }"
            >
              <v-select
                v-model="editedItem.role"
                :items="roles"
                item-text="roleName"
                item-value="id"
                label="Select"
                :error-messages="errors"
              ></v-select>
            </validation-provider>
          </v-col>
        </v-row>
        <v-row>
          <v-container class="px-0" fluid>
            <v-checkbox
              v-model="editedItem.driverslicense"
              label="Drivers license"
            ></v-checkbox>
          </v-container>
        </v-row>
      </v-container>
    </template>
    <template #table-name>{{ tableName }}</template>
  </DataTable>
</template>

<script>
import ApiComponent from "../business/ServerAPI.vue";
import DataTable from "./DataTable.vue";
import { ValidationProvider, setInteractionMode } from "vee-validate";

setInteractionMode("eager");

const headers = [
  { text: "Email", value: "email" },
  { text: "Name", value: "name" },
  { text: "Password", value: "password" },
  { text: "Role", value: "roleName" },
  { text: "Actions", value: "actions", sortable: false },
];

export default {
  components: {
    DataTable,
    ValidationProvider,
  },

  inject: ["getRoles"],

  methods: {
    save() {
      ApiComponent.postDealer(
        this.editedItem.id == 0 ? "" : this.editedItem.id,
        this.editedItem.email,
        this.editedItem.name,
        this.editedItem.driverslicense,
        this.editedItem.password,
        this.editedItem.role.toString()
      ).then((promise) => {
        if (promise.data === "User added") {
          this.syncUsers();
        }
        this.$refs.dataTable.snackText = promise.data;
        this.$refs.dataTable.snack = true;
      });
      this.editedItem = {
        id: 0,
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: 1,
      };
    },
    close() {
      this.$refs.dataTable.dialog = false;
      this.editedItem = {
        id: 0,
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: 1,
      };
    },
    onEdit(idx) {
      this.editedItem = JSON.parse(
        JSON.stringify(this.dealers.filter((dealer) => dealer.id == idx)[0])
      );
      this.$refs.dataTable.dialog = true;
    },
    deleteItem(item) {
      if (confirm(`This dealer will be deleted ${item["name"]}`)) {
        ApiComponent.getDeletion(item["id"]).then((promise) => {
          if (promise.data === "User deleted") {
            this.dealers = this.dealers.filter(
              (dealer) => dealer["id"] !== item["id"]
            );
          }
          this.$refs.dataTable.snackText = promise.data;
          this.$refs.dataTable.snack = true;
        });
      }
    },
    syncUsers() {
      this.dealers = [];
      ApiComponent.getAllUsers().then((promise) => {
        for (let i = 0; i < promise.data.length; i++) {
          this.dealers.push({
            id: promise.data[i]["id"],
            email: promise.data[i]["email"],
            name: promise.data[i]["name"],
            password: promise.data[i]["password"],
            roleName: promise.data[i]["role"]["roleName"],
            role: promise.data[i]["role"]["id"],
          });
        }
      });
    },
  },

  data() {
    return {
      headers,
      dealers: [],
      roles: [],
      editedItem: {
        id: 0,
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: 1,
      },

      tableName: "Accounts",
      componentName: "Dealers",
      buttonName: "New Dealer",
    };
  },

  mounted() {
    this.syncUsers();
    this.roles = this.getRoles();
  },
};
</script>
