<template>
  <span>
    <slot v-bind="{ getUser, setUser, getCurrent, setCurrent }"></slot>
  </span>
</template>

<script>
export default {
  inject: ["getUser", "setUser", "getCurrent", "setCurrent"],
};
</script>
