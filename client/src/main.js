import Vue from "vue";
import VueRouter from "vue-router";
import Vuetify from "vuetify/lib";
import App from "./App.vue";
import HomePage from "./components/HomePage.vue";
import StorePage from "./components/StorePage.vue";
import DealerAccounts from "./components/DealerAccounts.vue";
import RegionalRoles from "./components/RegionalRoles.vue";
import LoginPage from "./components/LoginPage.vue";
import ToDoStepper from "./components/ToDoStepper.vue";
import "material-design-icons-iconfont/dist/material-design-icons.css";
import "@mdi/font/css/materialdesignicons.css";

Vue.config.productionTip = false;
Vue.use(VueRouter);
Vue.use(Vuetify);

const routes = [
  { path: "/storage", component: StorePage },
  { path: "/dealers", component: DealerAccounts },
  { path: "/roles", component: RegionalRoles },
  { path: "/login", component: LoginPage },
  { path: "/home", component: HomePage },
  { path: "/todos", component: ToDoStepper },
  { path: "*", redirect: "/home" },
];

const router = new VueRouter({
  routes,
});

const vuetify = new Vuetify();

new Vue({
  router,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
