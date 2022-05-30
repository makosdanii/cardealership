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
      notifications: [],
      localFetch: [],
      roles: [],
      regions: [],
      brands: [],
    };
  },

  watch: {
    localFetch: {
      handler: function (newVal) {
        window.localStorage.setItem("todos", JSON.stringify(newVal));
      },
      deep: true,
    },
  },

  methods: {
    setUser(user) {
      this.user = user;
    },
    setCurrent(component) {
      this.currentComponent = component;
    },
    setNotification(data) {
      this.notifications = data;
    },
    setLocalFetch(id, step) {
      this.localFetch.progress = this.localFetch.progress.map((val, i) =>
        val.id === id ? { id, step } : val
      );
      console.log(this.localFetch);
    },
    getLocalFetch(idx) {
      return idx < 0
        ? this.localFetch?.progress
        : this.localFetch?.progress?.filter((i) => i.id === idx)?.[0]?.step;
    },
    async syncRoles() {
      await ServerAPI.getAllRoles().then((promise) => {
        this.roles = [];
        for (let i = 0; i < promise.data.length; i++) {
          this.roles.push({
            id: promise.data[i]["id"],
            roleName: promise.data[i]["roleName"],
          });
        }
      });
      return this.roles;
    },
    async syncRegions() {
      await ServerAPI.getAllRegion().then(
        (promise) => (this.regions = promise.data)
      );
      return this.regions;
    },
  },

  computed: {
    getUser() {
      return this.user;
    },
    getCurrent() {
      return this.currentComponent;
    },
    getNotifications() {
      return this.notifications;
    },
    getRoles() {
      return this.roles;
    },
    getRegions() {
      return this.regions;
    },
  },

  provide() {
    return {
      getUser: () => this.getUser,
      setUser: (user) => this.setUser(user),
      getCurrent: () => this.getCurrent,
      setCurrent: (component) => this.setCurrent(component),
      getNotifications: () => this.getNotifications,
      setNotification: (data) => this.setNotification(data),
      getLocalFetch: (idx) => this.getLocalFetch(idx),
      setLocalFetch: (idx, value) => this.setLocalFetch(idx, value),
      getRoles: () => this.getRoles,
      syncRoles: async () => await this.syncRoles(),
      getRegions: () => this.getRegions,
      syncRegions: async () => await this.syncRegions(),
    };
  },

  mounted() {
    ServerAPI.getLoggedIn().then((promise) => {
      this.user = promise.data;
      ServerAPI.getAllTodos(promise.data).then((_promise) => {
        this.notifications = _promise.data;

        this.localFetch = JSON.parse(window.localStorage.getItem("todos"));

        if (this.localFetch?.progress?.length !== this.notifications.length) {
          this.localFetch = {
            progress: this.notifications.map((todo) => ({
              id: todo.id,
              step: todo.isComplete ? 0 : 1,
            })),
          };
        }
      });
    });

    this.syncRoles();
    this.syncRegions();
  },
};
</script>
