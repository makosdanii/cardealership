<template>
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

<script>
import { ValidationProvider, setInteractionMode } from "vee-validate";
setInteractionMode("eager");

export default {
  components: {
    ValidationProvider,
  },

  props: {
    editedItem: Object,
    brands: Array,
  },
};
</script>
