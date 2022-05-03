<template>
  <div data-app id="dealer-accounts" class="scrollable-table">
    <v-dialog v-model="dialog" max-width="500px">
      <template v-slot:activator="{ on, attrs }">
        <ResponsiveButtons :on="on" :attrs="attrs" 
        @filter="(filter) => fullTextSearch(filter)"
          >New Dealer</ResponsiveButtons
        >
      </template>
      <v-card>
        <v-card-title>
          <span class="text-h5">Dealer</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="6" md="4">
                <v-text-field
                  v-model="editedItem.email"
                  label="Email"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-text-field
                  v-model="editedItem.name"
                  label="Name"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-text-field
                  type="password"
                  v-model="editedItem.password"
                  label="Password"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-select
                  v-model="editedItem.role"
                  :items="roles"
                  item-text="roleName"
                  item-value="id"
                  label="Select"
                  required
                ></v-select>
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
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="close"> Cancel </v-btn>
          <v-btn color="blue darken-1" text @click="save"> Save </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-data-table :headers="headers" :items="dealers" :items-per-page="-1">
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>{{ tableName }}</v-toolbar-title>
        </v-toolbar>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <!-- <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon> -->
        <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
      </template>
      <template v-slot:no-data>
        <p>No data</p>
      </template>
    </v-data-table>
    <v-snackbar v-model="snack" :timeout="2000">{{ snackText }}</v-snackbar>
  </div>
</template>

<script>
import ApiComponent from "../business/ServerAPI.vue";
import ResponsiveButtons from "./ResponsiveButtons.vue";

const headers = [
  { text: "Email", value: "email" },
  { text: "Name", value: "name" },
  { text: "Password", value: "password" },
  { text: "Role", value: "role" },
  { text: "Actions", value: "actions", sortable: false },
];

const editedIndex = -1;

export default {
  components: {
    ResponsiveButtons,
  },
  data() {
    return {
      dialog: false,
      headers,
      dealers: [],
      roles: [],
      editedIndex,
      editedItem: {
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: -1,
      },
      snack: false,
      snackText: "",
      tableName: "Accounts",
      componentName: "Dealers",
    };
  },

  computed: {
    formTitle() {
      return editedIndex === -1 ? "New Item" : "Edit Item";
    },
  },

  methods: {
    fullTextSearch(filter) {
      document.querySelectorAll('tbody tr')
      .forEach(tr => { tr.hidden = ![...tr.querySelectorAll('td')]
      .reduce((prev, curr) => prev || curr.innerText.includes(filter), false) })
    },
    save() {
      ApiComponent.postDealer(
        this.editedItem.email,
        this.editedItem.name,
        this.editedItem.driverslicense,
        this.editedItem.password,
        this.editedItem.role.toString()
      ).then((promise) => {
        if (promise.data === "User added") {
          this.syncUsers();
        }
        this.snackText = promise.data;
        this.snack = true;
      });
      this.editedItem = {
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: -1,
      };
    },
    close: function () {
      this.dialog = false;
      this.editedItem = {
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: -1,
      };
    },
    deleteItem(item) {
      if (confirm(`This dealer will be deleted ${item["name"]}`)) {
        ApiComponent.getDeletion(item["id"]).then((promise) => {
          if (promise.data === "User deleted") {
            this.dealers = this.dealers.filter(
              (dealer) => dealer["id"] !== item["id"]
            );
          }
          this.snackText = promise.data;
          this.snack = true;
        });
      }
    },
    editItem(item) {
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
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
            role: promise.data[i]["role"]["roleName"],
          });
        }
      });
    },
  },

  mounted() {
    this.syncUsers();

    ApiComponent.getAllRoles().then((promise) => {
      for (let i = 0; i < promise.data.length; i++) {
        this.roles.push({
          id: promise.data[i]["id"],
          roleName: promise.data[i]["roleName"],
        });
      }
    });
  },
};
</script>
