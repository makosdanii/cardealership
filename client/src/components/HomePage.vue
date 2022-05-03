
<template>
    <v-container class="pa-4 text-center tile-container">
      <Tile source="https://i.insider.com/5f1b0efcf34d051e1b3f0779?width=1000&format=jpeg&auto=webp" link="/storage">Storage</Tile>
      <Tile source="https://2684054.fs1.hubspotusercontent-na1.net/hub/2684054/hubfs/blog-images/carbuying.jpg?width=1200&name=carbuying.jpg" link="/dealers">Dealers</Tile>
      <Tile source="https://www.retirementliving.com/wp-content/uploads/2019/01/carshipping-1.jpg" link="/roles">Roles</Tile>
    </v-container>
</template>
<script>
import Tile from "./Tile.vue"

export default {
  components:{
    Tile
  }, 
  data() {
    return {
      componentName: "Home",
    };
  },
};
</script>

<style scoped>
.tile-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
  max-width: 100%;
}
</style>
