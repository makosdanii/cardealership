<template>
  <div>
    <v-row justify="center">
      <v-expansion-panels style="margin: 10px" stylepopout>
        <v-expansion-panel v-for="(item, i) in todos" :key="i">
          <v-expansion-panel-header
            @click="renderSteps(item.id)"
            :style="[todos[i].isComplete ? { color: 'green' } : {}]"
            >{{ todos[i].description }}</v-expansion-panel-header
          >
          <v-expansion-panel-content>
            <v-sheet
              v-if="todos[i].isComplete && selectedTodo"
              color="green lighten-3"
              ><span>Completed at: {{ selectedTodo.timeStamp }}</span></v-sheet
            >
            <v-stepper v-model="current" v-if="!todos[i].isComplete">
              <v-stepper-header>
                <!-- <v-stepper-step :complete="current > 1" step="1">
                  Role
                </v-stepper-step>
                <v-divider></v-divider> -->
                <v-stepper-step :complete="current > 1" step="1">
                  Dealers
                </v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="2"> Shipping </v-stepper-step>
              </v-stepper-header>
              <v-stepper-items>
                <v-stepper-content step="1">
                  <validation-observer ref="observer" v-slot="{ invalid }">
                    <v-card
                      class="mb-12"
                      height="200px"
                      style="margin-bottom: 10px"
                      v-if="
                        selectedTodo &&
                        selectedTodo.shipment.some((item) => item.user === 0)
                      "
                    >
                      <DealerCard :editedItem="editedItem" :roles="roles" />
                    </v-card>
                    <p
                      v-if="
                        !selectedTodo ||
                        !selectedTodo.shipment.some((item) => item.user === 0)
                      "
                    >
                      All items had been assigned to a dealer
                    </p>
                    <v-btn
                      color="primary"
                      :disabled="invalid"
                      @click="makeProgress(selectedTodo.id)"
                    >
                      Continue
                    </v-btn>
                    <v-btn text @click="cancelProgress(selectedTodo.id)">
                      Cancel
                    </v-btn>
                  </validation-observer>
                </v-stepper-content>
                <v-stepper-content step="2">
                  <validation-observer ref="observer" v-slot="{ invalid }">
                    <v-card
                      class="mb-12"
                      height="200px"
                      style="margin-bottom: 10px"
                      v-for="(store, j) in editedItem2"
                      :key="j"
                    >
                      <ItemCard :editedItem="store" :brands="[brands[j]]" />
                    </v-card>
                    <v-btn
                      color="primary"
                      :disabled="invalid"
                      @click="finishProgress(selectedTodo.id, j)"
                    >
                      Continue
                    </v-btn>
                    <v-btn text @click="deleteProgress(selectedTodo.id, j)">
                      Cancel
                    </v-btn>
                  </validation-observer>
                </v-stepper-content>
              </v-stepper-items>
            </v-stepper>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
      <v-container class="px-0" style="margin-left: 10px" fluid>
        <v-switch v-model="switch1" label="Hide completed"></v-switch>
      </v-container>
    </v-row>
    <v-snackbar v-model="snack" :timeout="2000">{{ snackText }}</v-snackbar>
  </div>
</template>

<script>
import ServerAPI from "../business/ServerAPI.vue";
import DealerCard from "../components/DealerCard.vue";
import ItemCard from "../components/ItemCard.vue";
import { ValidationObserver, setInteractionMode } from "vee-validate";
import _ from "lodash";

setInteractionMode("eager");

export default {
  components: { DealerCard, ItemCard, ValidationObserver },
  emits: ["notify"],
  inject: [
    "getNotifications",
    "getLocalFetch",
    "setLocalFetch",
    "getRoles",
    "getRegions",
  ],

  watch: {
    switch1: {
      handler: function (newVal) {
        this.todos = this.getNotifications().filter((todo) =>
          newVal ? !todo.isComplete : true
        );
      },
    },
  },
  methods: {
    async renderSteps(id) {
      await ServerAPI.getTodo(id).then((promise) => {
        this.selectedTodo = promise.data;
        console.log(this.selectedTodo);
      });
      this.current = this.getLocalFetch(id);

      //continue only if new account needs to be created by user
      if (!this.selectedTodo?.shipment?.some((item) => item.user === 0)) {
        for (let i = 0; i < this.selectedTodo.shipment.length; i++) {
          this.brands.push(this.selectedTodo.shipment[i].brand);
          this.editedItem2.push({
            brand: 1,
            ...this.selectedTodo.shipment[i],
          });
        }
        return;
      }

      const brandIds = this.selectedTodo.shipment.map((item) => item.brand.id);
      const regions = this.getRegions();

      const capableRolesforBrandReg = [];
      brandIds.forEach((brandId) =>
        regions
          .filter((region) =>
            region.brands.map((brand) => brand.id).includes(brandId)
          )
          .forEach((x) => capableRolesforBrandReg.push(x.roles))
      );
      this.roles = _.intersection(...capableRolesforBrandReg);
    },

    async makeProgress(id) {
      if (this.selectedTodo?.shipment?.some((item) => item.user === 0))
        await ServerAPI.postDealer({
          ...this.editedItem,
          id: "",
          roleId: this.editedItem.role,
          shipmentIdx2Assign: id,
        }).then((promise) => {
          console.log(promise.data);
          if (promise.data.includes("User added and assigned - id: ")) {
            this.selectedTodo = this.selectedTodo.shipment.map((ship) => ({
              ...ship,
              user: parseInt(
                promise.data.slice(
                  promise.data
                    .split("")
                    .reverse()
                    .findIndex((e) => e === " ") * -1
                )
              ),
            }));
            for (let i = 0; i < this.selectedTodo.shipment.length; i++) {
              this.brands.push(this.selectedTodo.shipment[i].brand);
              this.editedItem2.push({
                brand: 1,
                ...this.selectedTodo.shipment[i],
              });
            }
          } else if (promise.data === "Email is already registered") {
            this.current--;
          } else this.$router.push("/home");

          this.snackText = promise.data;
          this.snack = true;
          this.current++;
        });

      console.log(this.brands);
      console.log(this.editedItem2);
      this.editedItem = {
        id: 0,
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: 1,
      };
      this.current++;
    },

    cancelProgress(id) {
      this.editedItem = {
        id: 0,
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: 1,
      };
    },

    finishProgress(id, j) {
      ServerAPI.getStore(
        this.editedItem2[j].id,
        this.editedItem2[j].brand.id,
        this.editedItem2[j].model,
        this.editedItem2[j].quantity
      ).then((promise) => {
        this.snackText = promise.data;
        this.snack = true;
      });
      if (j === this.editedItem2.length - 1) {
        console.log(id);
        ServerAPI.getTodoCompleted(id);
      }
    },
    deleteProgress(id, j) {},
  },

  data() {
    return {
      current: 1,
      tableName: "Todo",
      componentName: "Your To-Do List",
      buttonName: "New Todo",
      todos: this.getNotifications(),
      selectedTodo: undefined,
      roles: [],
      brands: [],
      editedItem: {
        id: 0,
        email: "example@domain.com",
        name: "Firstname Lastname",
        driverslicense: false,
        password: "",
        role: 1,
      },
      editedItem2: [],
      snack: false,
      snackText: "",
      switch1: false,
    };
  },
};
</script>
