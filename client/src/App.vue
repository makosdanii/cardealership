<template>
  <div id="#app">
    <ContextProvider><Layout /></ContextProvider><br />
  </div>
</template>

<script>
import Layout from "./components/Layout.vue";
import ContextProvider from "./context/ContextProvider.vue";

export default {
  name: "app",
  components: {
    Layout,
    ContextProvider,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
