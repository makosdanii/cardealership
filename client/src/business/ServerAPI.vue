<script>
import axios from "axios";

const SERVER_URL = "http://localhost:8080";

const instance = axios.create({
  baseURL: SERVER_URL,
  timeout: 1000,
});

export default {
  getLoggedIn: () => instance.get("whoami"),
  getLogout: () => instance.get("logout"),
  getAllUsers: async () =>
    await instance.get("users", {
      transformResponse: [
        function (data) {
          return data ? JSON.parse(data) : [];
        },
      ],
    }),
  postDealer: (email, name, driverslicense, password, roleId) =>
    instance.post("createDealer", {
      email,
      name,
      driverslicense,
      password,
      roleId,
    }),
  getDeletion: (id) => instance.get("deleteDealer", { params: { id } }),
  postAuth: (email, password) =>
    instance.post("authenticate", { email: email, password: password }),
  getAllRoles: async () => await instance.get("roles"),
  getAllStores: async () => await instance.get("stores"),
  getAvailableBrands: async () => await instance.get("brand"),
  getStore: (id, brand, model, quantity) =>
    instance.get("updateStore", {
      params: {
        id,
        brand,
        model,
        quantity,
      },
    }),
  getAllRegion: async () => await instance.get("region"),
  getRoleNew: (id, roleName) =>
    instance.get("addRoles", { params: { id, roleName } }),
  getRoleUpdate: (id, regionId) =>
    instance.get("updateRoles", { params: { id, regionId } }),
  getRoleDelete: (id, regionId) =>
    instance.get("deleteRoles", { params: { id, regionId } }),
};
</script>
