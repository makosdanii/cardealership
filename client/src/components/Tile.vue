<template>

          <v-hover v-slot="{ hover }">
            <v-card
              @click="$router.push(link)"
              :elevation="hover ? 12 : 2"
              :class="{ 'on-hover': hover }"
              style="margin: 10px;"
            >
              <v-img
                :src="source"
                height="150px" max-width="200px"
              >
                <v-expand-transition>
                  <div
                    v-if="hover"
                    class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal"
                  >
                    <v-card-title class="text-h6 white--text">
                        <slot></slot>
                    </v-card-title>
                  </div>
                </v-expand-transition>
              </v-img>
            </v-card>
          </v-hover>
</template>
<script>
export default{
  props:{
    link:String,
    source:String
  }
}
</script>

<style scoped>
.v-card {
  transition: opacity 0.4s ease-in-out !important;
}

.v-card:not(.on-hover) {
  opacity: 0.6 !important;
}

.v-card--reveal{
  position: absolute;
  bottom: 0;
  height: 20%;
  width: 100%;
  background-color: rgba(0,0,0,0.5);
  align-items: center;
}


</style>