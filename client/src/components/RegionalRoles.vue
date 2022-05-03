<template>
  <div data-app id="regional-roles" class="scrollable-table">
    <v-dialog v-model="dialog" max-width="500px">
      <template v-slot:activator="{ on, attrs }">  
        <ResponsiveButtons :on="on" :attrs="attrs" 
        @filter="(filter) => fullTextSearch(filter)">New Role</ResponsiveButtons>
      </template>
      <v-card>
        <v-card-title>
          <span class="text-h5">Item</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-text-field label="Role name" v-model="editedItem.roleName" />
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
    <v-data-table
      :headers="headers"
      :items="roles"
      :single-expand="true"
      item-key="roleName"
      show-expand
      :items-per-page="-1"
    >
      <template #top>
        <v-toolbar flat>
          <v-toolbar-title>{{ tableName }}</v-toolbar-title>
        </v-toolbar>
      </template>
      <template v-slot:expanded-item="{ item }">
        <v-container
          class="px-0"
          fluid
          v-for="region in item.regions"
          :key="region.id"
        >
          <v-checkbox
            v-model="region.assigned"
            :label="region.regionName"
            @change="update(item.id, region.id)"
          />
        </v-container>
      </template>
    </v-data-table>
    <v-snackbar v-model="snack" :timeout="2000">{{ snackText }}</v-snackbar>
  </div>
</template>
<script>
import ApiComponentVue from "../business/ServerAPI.vue";
import ResponsiveButtons from "./ResponsiveButtons.vue";

export default {
  components: {
    ResponsiveButtons,
  },
  data() {
    return {
      headers: [
        { text: "Role", value: "roleName" },
        { text: "Assigned regions", value: "regionConcat" },
        { text: "", value: "data-table-expand" },
      ],
      roles: [],
      dialog: false,
      editedItem: {
        id: 0,
        roleName: "",
      },
      snack: false,
      snackText: "",
      tableName: "Region assignment",
      componentName: "Roles",
    };
  },

  methods: {
    fullTextSearch(filter) {
      document.querySelectorAll('tbody tr')
      .forEach(tr => { tr.hidden = ![...tr.querySelectorAll('td')]
      .reduce((prev, curr) => prev || curr.innerText.includes(filter), false) })
    },
    close() {
      this.dialog = false;
      this.editedItem = {
        id: 0,
        roleName: "",
      };
    },
    save() {
      ApiComponentVue.getRoleNew(-1, this.editedItem["roleName"]).then(
        (promise) => {
          if (promise.data == "Roles updated") {
            this.syncRoles();
          }
          this.snackText = promise.data;
          this.snack = true;
        }
      );

      this.editedItem = {
        id: 0,
        roleName: "",
      };
    },
    update(idx, regionId) {
      const checkbox = this.roles
        .filter((role) => role["id"] === idx)[0]
        .regions.filter((region) => region["id"] === regionId)[0]["assigned"];

      checkbox
        ? ApiComponentVue.getRoleUpdate(idx, regionId).then((promise) => {
            this.snackText = promise.data;
            this.snack = true;
          })
        : ApiComponentVue.getRoleDelete(idx, regionId).then((promise) => {
            this.snackText = promise.data;
            this.snack = true;
          });

      this.editedItem = {
        id: 0,
        roleName: "",
      };
      this.concatRegions();
    },
    syncRoles() {
      ApiComponentVue.getAllRoles().then((promise) => {
        this.roles = promise.data;
        this.roles.forEach((role) => {
          Object.defineProperty(role, "regions", {
            enumerable: true,
            value: [],
          });
          Object.defineProperty(role, "regionConcat", {
            value: "",
            writable: true,
          });
        });
      });

      ApiComponentVue.getAllRegion().then((promise) => {
        //work-around for regions of a role
        for (let i = 0; i < promise.data.length; i++) {
          this.roles.forEach((role) =>
            role["regions"].push({
              id: promise.data[i]["id"],
              regionName: promise.data[i]["regionName"],
              assigned: false,
            })
          );

          promise.data[i]["roles"].forEach((role) => {
            this.roles
              .filter((_role) => _role["id"] === role["id"])[0]
              .regions.filter(
                (region) =>
                  region["regionName"] === promise.data[i]["regionName"]
              )[0]["assigned"] = true;
          });
        }
        this.concatRegions();
      });
    },
    concatRegions() {
      this.roles.map(
        (role) =>
          (role["regionConcat"] = role["regions"].reduce(
            (prev, curr) =>
              prev.concat(
                curr["assigned"] ? " | " : "",
                curr["assigned"] ? curr["regionName"] : ""
              ),
            ""
          ))
      );
    },
  },

  computed: {},

  mounted() {
    this.syncRoles();
  },
};
</script>
