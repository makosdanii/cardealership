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
  postAuth: (email, password) =>
    instance.post("authenticate", { email: email, password: password }),

  getAllTodos: async (username) =>
    await instance.get("todos", { params: { username } }),
  getAllUsers: async () =>
    await instance.get("users", {
      transformResponse: [
        function (data) {
          return data ? JSON.parse(data) : [];
        },
      ],
    }),
  getAllRoles: async () => await instance.get("roles"),
  getAllRegion: async () => await instance.get("region"),
  getAllStores: async () => await instance.get("stores"),
  getAvailableBrands: async () => await instance.get("brand"),
  getAvailableBrands: async (role) =>
    await instance.get("brand", { params: { role } }),

  postDealer: (id, email, name, driverslicense, password, roleId) =>
    instance.post("createDealer", {
      id: parseInt(id) ? parseInt(id) : "",
      email,
      name,
      driverslicense,
      password,
      roleId,
    }),
  postDealer: (body) => instance.post("createDealer", body),

  getDeletion: (id) => instance.get("deleteDealer", { params: { id } }),

  getStore: (id, brand, model, quantity) =>
    instance.get("updateStore", {
      params: {
        id: parseInt(id) ? parseInt(id) : "",
        brand,
        model,
        quantity: parseInt(quantity) ? parseInt(quantity) : 0,
      },
    }),
  getStoreDelete: (id) => instance.get("deleteStore", { params: { id } }),

  getRoleNew: (roleName) => instance.get("addRoles", { params: { roleName } }),
  getRoleUpdate: (id, regionId) =>
    instance.get("updateRoles", { params: { id, regionId } }),
  getRoleDelete: (id, regionId) =>
    instance.get("deleteRoles", { params: { id, regionId } }),

  getTodo: (id) => instance.get("todo-step", { params: { id } }),
  getTodoCompleted: (idx) =>
    instance.get("todo-step", { params: { idx, completed: true } }),
};
</script>
