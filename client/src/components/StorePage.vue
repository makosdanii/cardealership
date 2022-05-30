<template>
  <ContextConsumer>
    <DataTable
      ref="dataTable"
      slot-scope="{ getUser }"
      :headers="
        getUser() === 'Global Manager'
          ? [{ text: 'Dealer ID', value: 'userId' }, ...headers]
          : headers
      "
      :items="store"
      @closed="() => close()"
      @saved="(id) => save(id)"
      @onEdit="(idx) => onEdit(idx)"
      @deleted="(item) => deleteItem(item)"
      @failed="(idx) => failed(idx)"
    >
      <template #button-text>{{ buttonName }}</template>
      <template #card-name>{{ buttonName.split(" ")[1] }}</template>
      <template #card-container>
        <v-container>
          <v-row>
            <v-col cols="12" sm="6" md="4">
              <validation-provider
                v-slot="{ errors }"
                name="Quantity"
                :rules="{ required: true, regex: '^\\d+$' }"
              >
                <v-select
                  v-model="editedItem.brand"
                  :items="brands"
                  item-text="brandName"
                  item-value="id"
                  label="Select"
                  :error-messages="errors"
                ></v-select>
              </validation-provider>
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <validation-provider
                v-slot="{ errors }"
                name="Model"
                :rules="{ required: true }"
              >
                <v-text-field
                  v-model="editedItem.model"
                  label="Model"
                  :error-messages="errors"
                ></v-text-field>
              </validation-provider>
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <validation-provider
                v-slot="{ errors }"
                name="Quantity"
                :rules="{ required: true, regex: '^\\d+$' }"
              >
                <v-text-field
                  v-model="editedItem.quantity"
                  label="Quantity"
                  :error-messages="errors"
                ></v-text-field>
              </validation-provider>
            </v-col>
          </v-row>
        </v-container>
      </template>
      <template #table-name>{{ tableName }}</template>
    </DataTable>
  </ContextConsumer>
</template>

<script>
import ApiComponentVue from "../business/ServerAPI.vue";
import ContextConsumer from "../context/ContextConsumer.vue";
import DataTable from "./DataTable.vue";
import lodash from "lodash";
import { ValidationProvider, setInteractionMode } from "vee-validate";

setInteractionMode("eager");

export default {
  components: {
    ContextConsumer,
    DataTable,
    ValidationProvider,
  },
  data() {
    return {
      headers: [
        { text: "Brand", value: "brand" },
        { text: "Model", value: "model" },
        { text: "Quantity", value: "quantity" },
        { text: "Actions", value: "actions", sortable: false },
      ],
      store: [],
      brands: [],
      editedItem: {
        id: 0,
        brand: 1,
        model: "",
        quantity: 0,
      },

      tableName: "Items",
      componentName: "Storage",
      buttonName: "New Item",
    };
  },

  methods: {
    deleteItem(item) {
      if (confirm(`This item will be deleted ${item["name"]}`)) {
        ApiComponentVue.getStoreDelete(item["id"]).then((promise) => {
          if (promise.data === "Item deleted") {
            this.store = this.store.filter(
              (_item) => _item["id"] !== item["id"]
            );
          }
          this.$refs.dataTable.snackText = promise.data;
          this.$refs.dataTable.snack = true;
        });
      }
    },
    failed(id) {
      // const storeCopy = lodash.cloneDeep(this.store);

      // var myStr = "foo";
      // var newStr = (" " + myStr).slice(1);
      // console.log(`Old ${myStr} vs new ${newStr}`);
      // console.log("Comparing string with deep ", newStr == myStr);

      // var foo1 = new String("foo");
      // var foo2 = new String("foo");
      // console.log("Comparing string with deep ", foo1 === foo2);

      this.$refs.dataTable.snackText = `Not updated: invalid quantity for item with ID: ${id}`;
      this.$refs.dataTable.snack = true;
      this.$refs.dataTable.editDialog = true;

      const modified = this.store.filter((item) => item.id === id)[0];
      const input = parseInt(new String((" " + modified.quantity).slice(1)));
      modified.quantity = isNaN(input) ? 0 : input;

      //       const modifiedCopy = JSON.parse(JSON.stringify(modified));
      // //attempt no. 1
      //       const unmodified = this.store.filter((item) => item.id !== id);
      //       const store = [modifiedCopy, ...unmodified];

      //       const idx = this.store.findIndex((item) => item.id === id);
      // //attempt no. 2
      //       this.store[idx] = modifiedCopy
      // //attempt no. 3
      //       this.store[idx].quantity = isNaN(input) ? 0 : input;
      this.syncStore();
      this.editedItem = {
        id: 0,
        brand: 1,
        model: "",
        quantity: 0,
      };
    },
    onEdit(idx) {
      this.editedItem = this.store.filter((item) => item.id == idx)[0]; //pass by ref
    },
    save(id) {
      ApiComponentVue.getStore(
        this.editedItem.id === 0 ? "" : this.editedItem.id,
        this.editedItem.brand,
        this.editedItem.model,
        this.editedItem.quantity
      ).then(
        function (promise) {
          if (promise.data === "Store updated" && id === 0) {
            this.syncStore();
          }
          this.$refs.dataTable.snackText = promise.data;
          this.$refs.dataTable.snack = true;
        }.bind(this)
      );

      this.editedItem = {
        id: 0,
        brand: 1,
        model: "",
        quantity: 0,
      };
    },

    close() {
      this.$refs.dataTable.dialog = false;
      this.editedItem = {
        id: 0,
        brand: 1,
        model: "",
        quantity: 0,
      };
    },

    syncStore() {
      this.store = [];
      ApiComponentVue.getAllStores().then((promise) => {
        for (let i = 0; i < promise.data.length; i++) {
          this.store.push({
            id: promise.data[i]["id"],
            userId: promise.data[i]["user"],
            brand: promise.data[i]["brand"]["brandName"],
            model: promise.data[i]["model"],
            quantity: promise.data[i]["quantity"],
          });
        }
      });
    },
  },

  mounted() {
    this.syncStore();
    ApiComponentVue.getAvailableBrands().then((promise) => {
      this.brands = promise.data;
    });
  },
};
</script>
