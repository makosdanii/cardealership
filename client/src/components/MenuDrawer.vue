<template>
  <ContextConsumer>
    <div slot-scope="{ getUser }">
      <v-navigation-drawer
        permanent
        expand-on-hover
        app
        :mini-variant.sync="miniDrawer"
      >
        <v-list nav dense>
          <v-list-item v-if="getUser() !== unauthenticated">
            <v-list-item-content>
              <v-list-item-icon>
                <v-icon>mdi-account-circle-outline</v-icon>
              </v-list-item-icon>
              <v-list-item-title v-if="!miniDrawer"
                >{{ `Welcome ${getUser()}!` }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-divider class="mx-4" inset horizontal></v-divider>
          <v-list-item
            v-if="getUser() !== unauthenticated"
            @click="$router.push('/home')"
            link
          >
            <v-list-item-icon>
              <v-icon>mdi-home</v-icon>
            </v-list-item-icon>
            <v-list-item-title> Home </v-list-item-title>
          </v-list-item>
          <v-list-item
            v-if="getUser() !== unauthenticated"
            link
            @click="$router.push('/storage')"
          >
            <v-list-item-icon>
              <v-icon>mdi-store</v-icon>
            </v-list-item-icon>
            <v-list-item-title> Storage </v-list-item-title>
          </v-list-item>
          <v-list-item
            v-if="getUser() === manager"
            link
            @click="$router.push('/dealers')"
          >
            <v-list-item-icon>
              <v-icon>mdi-account-group</v-icon>
            </v-list-item-icon>
            <v-list-item-title> Dealers </v-list-item-title>
          </v-list-item>
          <v-list-item
            v-if="getUser() === manager"
            link
            @click="$router.push('/roles')"
          >
            <v-list-item-icon>
              <v-icon>mdi-web</v-icon>
            </v-list-item-icon>
            <v-list-item-title> Roles </v-list-item-title>
          </v-list-item>
          <v-list-item link @click="$router.push('/login')">
            <v-list-item-icon>
              <v-icon>mdi-login</v-icon>
            </v-list-item-icon>
            <v-list-item-title
              >{{ getUser() === unauthenticated ? "Login" : "Logout" }}
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
    </div>
  </ContextConsumer>
</template>

<script>
import ContextConsumer from "../context/ContextConsumer.vue";
export default {
  components: {
    ContextConsumer,
  },
  data() {
    return {
      manager: "Global Manager",
      unauthenticated: "nobody",
      miniDrawer: true,
    };
  },
};
</script>
