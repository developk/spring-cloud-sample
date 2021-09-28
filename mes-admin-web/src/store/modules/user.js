import {getCurrentUser, login, logout} from "@/api/user";
import storage from "../../utils/storage";

const state = {
  token: storage.get("token"),
  refresh_token: storage.get("refresh_token"),
  username: "",
  name: "",
  avatar: "",
  roles: [],
  userOnlineInfo: null,
};

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token;
    storage.save("token", token);
  },
  SET_REFRESH_TOKEN: (state, refresh_token) => {
    state.refresh_token = refresh_token
    storage.save("refresh_token", refresh_token)
  },
  SET_USERNAME: (state, username) => {
    state.username = username
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction;
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles;
  },
  SET_USER_ONLINE_INFO: (state, userOnlineInfo) => {
    state.userOnlineInfo = userOnlineInfo;
  },
};

const actions = {
  // user login
  login({ commit }, loginModel) {
    return new Promise((resolve, reject) => {
      login(loginModel)
        .then((response) => {
          console.log('response: %o', response);

          commit("SET_TOKEN", response['access_token']);
          commit("SET_REFRESH_TOKEN", response['refresh_token']);
          commit("SET_USERNAME", loginModel.username)

          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  logout({ commit }) {
    return new Promise((resolve, reject) => {
      logout()
        .then(() => {
          //全部置空
          commit("SET_TOKEN", null);
          commit("SET_NAME", "");
          commit("SET_AVATAR", "");
          commit("SET_ROLES", []);
          commit("SET_USER_ONLINE_INFO", null);
          resolve();
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  //获取当前用户
  getCurrent({ commit }, username) {
    return new Promise((resolve, reject) => {
      getCurrentUser(username)
        .then((response) => {
          if(response.statusCode !== 200){
            reject(response);
          }

          commit("SET_NAME", response.data.username);
          commit("SET_AVATAR", response.data.picture);
          commit("SET_USER_ONLINE_INFO", response.data);

          resolve();
        })
        .catch((reason) => {
          reject(reason);
        });
    });
  },
  //重置当前用户信息，一般是更新了用户信息后进行
  resetCurrent({ commit }, userInfo) {
    commit("SET_NAME", userInfo.username);
    commit("SET_AVATAR", userInfo.avatar);
    commit("SET_USER_ONLINE_INFO", userInfo);
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
