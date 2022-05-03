<template>
  <span class="topped" id="#responsive-buttons">
    <v-btn color="primary" style="margin: 5px" dark v-bind="attrs" v-on="on">
      <v-icon>mdi-plus</v-icon>
      <span id="add"><slot></slot></span>
    </v-btn>
    <v-btn
      color="primary"
      @click="() => (this.searchDialog = !this.searchDialog)"
    >
      <v-edit-dialog v-model="searchDialog">
        <v-icon>mdi-magnify</v-icon>
        <span id="search">Search</span>
        <template #input>
          <v-text-field
            @input="function(input){$emit('filter', input)}"
            label="Search"
          />
        </template>
      </v-edit-dialog>
    </v-btn>
  </span>
</template>
<script>
export default {
  props: {
    attrs: Object,
    on: Object,
  },
  data() {
    return {
      searchDialog: false,
    };
  },
  emits: ["filter"],
};
</script>
