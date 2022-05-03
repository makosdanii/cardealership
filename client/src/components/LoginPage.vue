<template>
  <ContextConsumer>
    <div class="container" slot-scope="{ getUser, setUser }">
      <v-btn
        @click.native="submit(getUser, setUser)"
        v-if="getUser() !== 'nobody'"
        type="submit"
        class="scrolled"
        >Logout</v-btn
      >

      <validation-observer ref="observer" v-slot="{ invalid }">
        <form @submit.prevent="submit(getUser, setUser)">
          <validation-provider
            v-slot="{ errors }"
            name="email"
            :rules="{ required: true, email: true }"
          >
            <v-text-field
              v-model="email"
              :error-messages="errors"
              label="Email"
              required
            ></v-text-field>
          </validation-provider>
          <validation-provider
            v-slot="{ errors }"
            name="password"
            :rules="{
              required: true,
              min: 8,
              regex: '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$',
            }"
          >
            <v-text-field
              type="password"
              counter
              v-model="password"
              :error-messages="errors"
              label="Password"
              required
            ></v-text-field>
          </validation-provider>
          <v-btn class="mr-4" type="submit" :disabled="invalid"> Login </v-btn>
        </form>
      </validation-observer>

      <v-snackbar v-model="snack" :timeout="2000">{{ snackText }}</v-snackbar>
    </div>
  </ContextConsumer>
</template>

<script>
import { required, email, regex, min } from "vee-validate/dist/rules";
import {
  extend,
  ValidationObserver,
  ValidationProvider,
  // eslint-disable-next-line no-unused-vars
  setInteractionMode,
} from "vee-validate";

import ApiComponent from "../business/ServerAPI.vue";
import ContextConsumer from "../context/ContextConsumer.vue";

setInteractionMode("eager");

extend("email", { ...email, message: "Email not valid" });
extend("required", { ...required, message: "{_field_} can not be empty" });
extend("regex", {
  ...regex,
  message: "{_field_} does not match {regex}",
});
extend("min", {
  ...min,
  message: "{_field_} must be {length} long",
});

export default {
  components: {
    ValidationObserver,
    ValidationProvider,
    ContextConsumer,
  },
  data: () => ({
    email: "",
    password: "",
    snack: false,
    snackText: "",
    componentName: "Login",
  }),

  methods: {
    submit(getUser, setUser) {
      if (getUser() !== "nobody") {
        ApiComponent.getLogout().then((promise) => setUser(promise.data));
      } else {
        this.$refs.observer.validate();
        ApiComponent.postAuth(this.email, this.password).then((promise) => {
          if (promise.data === "Success") {
            ApiComponent.getLoggedIn().then((promise) => setUser(promise.data));
            this.$router.push("/");
          } else {
            this.snackText = promise.data;
            this.snack = true;
          }
        });
      }
    },
  },
};
</script>
