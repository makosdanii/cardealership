<template>
  <span>
    <slot
      v-bind="{
        getUser,
        setUser,
        getCurrent,
        setCurrent,
        getNotifications,
        setNotification,
      }"
    ></slot>
  </span>
</template>

<script>
export default {
  inject: [
    "getUser",
    "setUser",
    "getCurrent",
    "setCurrent",
    "getNotifications",
    "setNotification",
  ],
};
</script>
