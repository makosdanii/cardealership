<template>
  <div id="context-provider">
    <slot></slot>
  </div>
</template>
<script>
import ServerAPI from "../business/ServerAPI.vue";
export default {
  data() {
    return {
      user: "",
      currentComponent: "",
    };
  },
  methods: {
    setUser(user) {
      this.user = user;
    },
    setCurrent(component) {
      this.currentComponent = component;
    },
  },
  computed: {
    getUser() {
      return this.user;
    },
    getCurrent() {
      return this.currentComponent;
    },
  },
  provide() {
    return {
      getUser: () => this.getUser,
      setUser: (user) => this.setUser(user),
      getCurrent: () => this.getCurrent,
      setCurrent: (component) => this.setCurrent(component),
    };
  },
  mounted() {
    ServerAPI.getLoggedIn().then((promise) => (this.user = promise.data));
  },
};
</script>
