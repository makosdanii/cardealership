<template>
  <v-app>
    <MenuDrawer />
    <v-main>
      <v-container fluid style="position: relative">
        <v-card class="overflow-hidden" style="position: relative">
          <v-app-bar elevate-on-scroll>
            <v-toolbar-title ref="title" />
          </v-app-bar>
          <v-sheet
            @scroll="handleScroll"
            class="overflow-y-auto"
            max-height="80vh"
            style="position: relative"
          >
            <v-container class="table-container">
              <router-view ref="component" />
            </v-container>
          </v-sheet>
        </v-card>
      </v-container>
    </v-main>
    <!-- <v-footer app>
      <a style="font-size: 10 px" href="https://www.freepik.com/vectors/set"
        >Set vector created by freepik - www.freepik.com</a
      >
    </v-footer> -->
  </v-app>
</template>

<script>
import MenuDrawer from "./MenuDrawer.vue";
import lodash from "lodash";

export default {
  components: {
    MenuDrawer,
  },
  methods: {
    wrap4Coloring: (char) => {
      return `<span style="color: rgba(0,0,0,0.5)">${char}</span>`;
    },
    navbarTitle(full) {
      const main = this.$refs?.component?.componentName;
      const sub = full ? this.$refs?.component?.tableName : "";
      return `${this.wrap4Coloring("/")} ${
        main
          ? main.concat(
              sub?.length > 0 ? ` ${this.wrap4Coloring("/")} ${sub}` : ``
            )
          : ""
      }`;
    },
    setChildren(parent, node, recursion) {
      if (recursion === -1) {
        return;
      }

      if (
        node?.getAttribute &&
        node.getAttribute("id") === "#responsive-buttons"
      ) {
        if (
          parent.scrollTop > 20 &&
          !node.classList.value.includes("scrolled")
        ) {
          node.classList.remove("topped");
          node.classList.add("scrolled");
          this.$refs.title.innerHTML = this.navbarTitle(true);
          return;
        }
        if (
          parent.scrollTop <= 20 &&
          node.classList.value.includes("scrolled")
        ) {
          node.classList.add("topped");
          node.classList.remove("scrolled");
          this.$refs.title.innerHTML = this.navbarTitle(false);
          return;
        }
      }

      node?.childNodes?.forEach((_node) =>
        this.setChildren(parent, _node, recursion > 5 ? -1 : recursion + 1)
      );
    },
  },

  mounted() {
    this.$refs.title.innerHTML = this.navbarTitle(false);

    this.$watch(
      () => {
        return this.$route.path;
      },
      () => {
        this.$refs.title.innerHTML = this.navbarTitle(false);
      }
    );
  },

  created() {
    this.handleScroll = lodash.debounce((event) => {
      this.setChildren(event.target, event.target, 0);
    }, 250);
  },

  beforeUnmount() {
    this.handleScroll.cancel();
  },
};
</script>
