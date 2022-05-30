<template>
  <DataTable
    ref="dataTable"
    expand
    :headers="headers"
    :items="roles"
    @closed="() => close()"
    @saved="() => save()"
    @deleted="(item) => deleteItem(item)"
    @updated="(idx, regionId) => update(idx, regionId)"
  >
    <template #button-text>{{ buttonName }}</template>
    <template #card-name>{{ buttonName.split(" ")[1] }}</template>
    <template #card-container>
      <v-container>
        <validation-provider
          v-slot="{ errors }"
          name="Role name"
          :rules="{
            required: true,
          }"
        >
          <v-row>
            <v-text-field
              label="Role name"
              v-model="editedItem.roleName"
              :error-messages="errors"
            />
          </v-row>
        </validation-provider>
      </v-container>
    </template>
    <template #table-name>{{ tableName }}</template>
  </DataTable>
</template>

<script>
import ApiComponentVue from "../business/ServerAPI.vue";
import DataTable from "./DataTable.vue";
import _ from "lodash";
import { ValidationProvider, setInteractionMode } from "vee-validate";

setInteractionMode("eager");

export default {
  components: {
    DataTable,
    ValidationProvider,
  },

  inject: ["syncRoles", "syncRegions", "getRoles", "getRegions"],

  methods: {
    close() {
      this.$refs.dataTable.dialog = false;
      this.editedItem = {
        id: 0,
        roleName: "",
      };
    },

    save() {
      ApiComponentVue.getRoleNew(this.editedItem["roleName"]).then(
        (promise) => {
          if (promise.data === "Roles updated") {
            this.syncRole();
            this.syncRegions();
          }
          this.$refs.dataTable.snackText = promise.data;
          this.$refs.dataTable.snack = true;
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
            this.$refs.dataTable.snackText = promise.data;
            this.$refs.dataTable.snack = true;
          })
        : ApiComponentVue.getRoleDelete(idx, regionId).then((promise) => {
            this.$refs.dataTable.snackText = promise.data;
            this.$refs.dataTable.snack = true;
          });

      this.editedItem = {
        id: 0,
        roleName: "",
      };

      this.concatRegions();
    },

    deleteItem(item) {
      if (confirm(`This role will be deleted ${item["name"]}`)) {
        ApiComponentVue.getRoleDelete(item["id"], "").then((promise) => {
          if (promise.data === "Role deleted") {
            this.roles = this.roles.filter((role) => role["id"] !== item["id"]);
            this.syncRoles();
          }
          this.$refs.dataTable.snackText = promise.data;
          this.$refs.dataTable.snack = true;
        });
      }
    },

    async syncRole() {
      this.roles = await this.syncRoles();

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

      const regions = await this.getRegions();

      //work-around for regions of a role
      for (let i = 0; i < regions.length; i++) {
        this.roles.forEach((role) =>
          role["regions"].push({
            id: regions[i]["id"],
            regionName: regions[i]["regionName"],
            assigned: false,
          })
        );

        regions[i].roles.forEach((role) => {
          this.roles
            .filter((_role) => _role["id"] === role["id"])[0]
            .regions.filter(
              (region) => region["regionName"] === regions[i]["regionName"]
            )[0]["assigned"] = true;
        });
      }

      this.concatRegions();
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

  data() {
    return {
      headers: [
        { text: "Role", value: "roleName" },
        { text: "Assigned regions", value: "regionConcat" },
        { text: "", value: "data-table-expand" },
        { text: "Actions", value: "actions", sortable: false },
      ],
      roles: [],
      editedItem: {
        id: 0,
        roleName: "",
      },

      tableName: "Region assignment",
      componentName: "Roles",
      buttonName: "New Role",
    };
  },

  mounted() {
    this.syncRole();
  },
};
</script>
