<template>
  <div data-app class="scrollable-table">
    <v-dialog v-model="dialog" max-width="500px">
      <template v-slot:activator="{ on, attrs }">
        <ResponsiveButtons
          :on="on"
          :attrs="attrs"
          @filter="(filter) => fullTextSearch(filter)"
          ><slot name="button-text"></slot></ResponsiveButtons
      ></template>

      <validation-observer ref="observer" v-slot="{ invalid }">
        <!-- prettier-ignore -->
        <form
          @submit.prevent="onSave"
        >
          <v-card>
            <v-card-title>
              <span class="text-h5"><slot name="card-name"></slot></span>
            </v-card-title>
            <v-card-text>
              <slot name="card-container"></slot>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="() => close()">
                Cancel
              </v-btn>
              <v-btn
                color="blue darken-1"
                text
                :disabled="invalid"
                type="submit"
              >
                Save
              </v-btn>
            </v-card-actions>
          </v-card>
        </form>
      </validation-observer>
    </v-dialog>

    <v-data-table
      class="grow-table"
      style="position: relative"
      item-key="id"
      :headers="headers"
      :items="items"
      :items-per-page="-1"
      :single-expand="expand"
      :show-expand="expand"
    >
      <template #top>
        <v-toolbar flat>
          <v-toolbar-title><slot name="table-name"></slot></v-toolbar-title>
        </v-toolbar>
      </template>

      <template
        v-if="headers.filter((item) => item.value === 'quantity').length > 0"
        ref="editDialog"
        v-slot:[`item.quantity`]="{ item }"
      >
        <validation-observer v-slot="{ invalid }">
          <v-edit-dialog
            :return-value.sync="item.quantity"
            @save="$emit(invalid ? 'failed' : 'saved', item.id)"
          >
            {{ item.quantity }}
            <v-icon small class="mr-2" @click="$emit('onEdit', item.id)">
              mdi-pencil
            </v-icon>
            <template #input>
              <validation-provider
                v-slot="{ errors }"
                name="Quantity"
                :rules="{ required: true, regex: '^\\d+$' }"
                mode="aggressive"
              >
                <v-text-field
                  v-model="item.quantity"
                  label="Edit"
                  :error-messages="errors"
                />
              </validation-provider>
            </template>
          </v-edit-dialog>
        </validation-observer>
      </template>

      <template
        v-if="
          headers.filter((item) => item.value === 'data-table-expand').length >
          0
        "
        v-slot:expanded-item="{ item }"
      >
        <v-container
          class="px-0"
          fluid
          v-for="region in item.regions"
          :key="region.id"
        >
          <v-checkbox
            v-model="region.assigned"
            :label="region.regionName"
            @change="() => $emit('updated', item.id, region.id)"
          />
        </v-container>
      </template>

      <template v-slot:[`item.actions`]="{ item }">
        <v-icon
          v-if="
            headers.filter((header) => header.value === 'quantity').length ===
              0 && !expand
          "
          small
          class="mr-2"
          @click="$emit('onEdit', item.id)"
        >
          mdi-pencil
        </v-icon>

        <v-icon
          :disabled="
            (item.name && item.name === 'Global Manager') ||
            (item.roleName && item.roleName === 'global')
          "
          small
          @click="$emit('deleted', item)"
        >
          mdi-delete
        </v-icon>
      </template>

      <template #no-data>
        <p>No data</p>
      </template>
    </v-data-table>
    <v-snackbar v-model="snack" :timeout="2000">{{ snackText }}</v-snackbar>
  </div>
</template>

<script>
import ResponsiveButtons from "../components/ResponsiveButtons.vue";
import {
  ValidationObserver,
  ValidationProvider,
  setInteractionMode,
} from "vee-validate";

setInteractionMode("aggressive");

export default {
  components: {
    ResponsiveButtons,
    ValidationObserver,
    ValidationProvider,
  },
  props: {
    headers: Array,
    items: Array,
    expand: Boolean,
  },
  emits: ["closed", "saved", "updated", "deleted", "failed", "onEdit"],
  methods: {
    log: (value) => console.log(value),
    onSave() {
      this.$refs.observer.reset();
      this.$emit("saved", 0);
    },
    close() {
      this.dialog = false;
      this.$emit("closed");
    },
    fullTextSearch(filter) {
      document.querySelectorAll("tbody tr").forEach((tr) => {
        tr.hidden = ![...tr.querySelectorAll("td")].reduce(
          (prev, curr) =>
            prev || curr.innerText.toLowerCase().includes(filter.toLowerCase()),
          false
        );
      });
    },
  },

  data() {
    return {
      dialog: false,
      snack: false,
      snackText: "",
    };
  },
};
</script>
