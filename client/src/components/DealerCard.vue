<template>
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

<script>
import { ValidationProvider, setInteractionMode } from "vee-validate";
setInteractionMode("eager");

export default {
  components: {
    ValidationProvider,
  },

  props: {
    editedItem: Object,
    roles: Array,
  },
};
</script>
