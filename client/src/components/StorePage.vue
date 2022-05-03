<template>
  <div data-app id="store-page" class="scrollable-table">
    <v-dialog v-model="dialog" max-width="500px">
      <template v-slot:activator="{ on, attrs }">
        <ResponsiveButtons
          @filter="(filter) => fullTextSearch(filter)"
          :on="on"
          :attrs="attrs"
          >New Item</ResponsiveButtons
        ></template
      >
      <v-card>
        <v-card-title>
          <span class="text-h5">Item</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-alert v-if="alert != ''" type="error">{{ alert }}</v-alert>
            </v-row>
            <v-row>
              <v-col cols="12" sm="6" md="4">
                <v-select
                  v-model="editedItem.brand"
                  :items="brands"
                  item-text="brandName"
                  item-value="brandName"
                  label="Select"
                  required
                ></v-select>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-text-field
                  v-model="editedItem.model"
                  label="Model"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-text-field
                  v-model="editedItem.quantity"
                  label="Quantity"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="close"> Cancel </v-btn>
          <v-btn color="blue darken-1" text @click="save(0)"> Save </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-data-table
    class="grow-table"
      style="position: relative"
      :headers="headers"
      :items="store"
      :items-per-page="-1"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>{{ tableName }}</v-toolbar-title>
        </v-toolbar>
      </template>
      <template v-slot:[`item.quantity`]="props">
        <v-edit-dialog
          :return-value.sync="props.item.quantity"
          @save="save(props.item.id)"
        >
          {{ props.item.quantity }}
          <template #input>
            <v-text-field v-model="props.item.quantity" label="Edit" />
          </template>
        </v-edit-dialog>
      </template>
    </v-data-table>
    <v-snackbar v-model="snack" :timeout="2000">{{ snackText }}</v-snackbar>
  </div>
</template>
<script>
import ApiComponentVue from "../business/ServerAPI.vue";
import ResponsiveButtons from "../components/ResponsiveButtons.vue";
export default {
  components: {
    ResponsiveButtons,
  },
  data() {
    return {
      headers: [
        { text: "Brand", value: "brand" },
        { text: "Model", value: "model" },
        { text: "Quantity", value: "quantity" },
      ],
      store: [],
      brands: [],
      editedItem: {
        id: 0,
        brand: "",
        model: "",
        quantity: 0,
      },
      alert: "",
      dialog: false,
      snack: false,
      snackText: "",
      tableName: "Items",
      componentName: "Storage",
    };
  },

  methods: {
    fullTextSearch(filter) {
      document.querySelectorAll('tbody tr')
      .forEach(tr => { tr.hidden = ![...tr.querySelectorAll('td')]
      .reduce((prev, curr) => prev || curr.innerText.includes(filter), false) })
    },
    save(idx) {
      if (idx === 0) {
        ApiComponentVue.getStore(
          -1,
          this.editedItem.brand,
          this.editedItem.model,
          this.editedItem.quantity
        ).then((promise) => {
          if (promise.data === "Store updated") {
            this.syncStore();
          }
          this.snackText = promise.data;
          this.snack = true;
        });
      } else {
        this.editedItem = this.store.filter((item) => item["id"] === idx)[0];
        ApiComponentVue.getStore(
          this.editedItem.id,
          this.editedItem.brand,
          this.editedItem.model,
          this.editedItem.quantity
        ).then((promise) => {
          this.snackText = promise.data;
          this.snack = true;
        });
      }

      this.editedItem = {
        id: 0,
        brand: "",
        model: "",
        quantity: 0,
      };
    },
    close() {
      this.dialog = false;
      this.editedItem = {
        id: 0,
        brand: "",
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
