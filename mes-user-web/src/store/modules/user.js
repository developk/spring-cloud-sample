import {getCurrentUser, login, logout, checkAccessToken, getRefresh_Token, saveLoginHistory} from "@/api/user";
import storage from "../../utils/storage";
import {CLIENT_INFO} from "@/config";
import store from "@/store";

const state = {
    token: storage.get("token"),
    refresh_token: storage.get("refresh_token"),
    username: storage.get("username"),
    name: "",
    avatar: "",
    roles: [],
    userOnlineInfo: null
};

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token;
        storage.save("token", token);
    },
    SET_REFRESH_TOKEN: (state, refresh_token) => {
        state.refresh_token = refresh_token
        storage.save("refresh_token", refresh_token);
    },
    SET_USERNAME: (state, username) => {
        state.username= username;
        storage.save("username", username);
    },
    CLEAR_TOKEN: (state) => {
        storage.remove("token");
        state.token = null;
    },
    CLEAR_REFRESH_TOKEN: (state) => {
        storage.remove("refresh_token");
        state.refresh_token = null;
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
    }
};

const actions = {
    // user login
    login({ commit }, loginModel) {
        return new Promise((resolve, reject) => {
            login(loginModel)
                .then((response) => {

                    commit("SET_TOKEN", response['access_token']);
                    commit("SET_REFRESH_TOKEN", response['refresh_token']);
                    commit("SET_USERNAME", loginModel.username);

                    // console.log('state: %o', this.state);

                    resolve(loginModel);
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },
    logout({ commit }) {
        return new Promise((resolve, reject) => {


            let requestData = {
                username: store.state.user.username,
                data: {
                    username: store.state.user.username,
                    clientId: CLIENT_INFO.client_id
                }
            };

            logout(requestData)
                .then(() => {
                    
                    // 저장된 토큰 제거
                    commit("CLEAR_TOKEN");
                    commit("CLEAR_REFRESH_TOKEN");

                    commit("SET_NAME", "");
                    commit("SET_AVATAR", "");
                    commit("SET_ROLES", "");

                    commit("SET_USER_ONLINE_INFO", null);

                    resolve();
                })
                .catch((err) => {
                    reject(err);
                });
        });
    },
    //获取当前用户
    getCurrent({ commit }) {
        return new Promise((resolve, reject) => {

            let requestData = {
                data: {
                    username: store.state.user.username,
                    clientId: CLIENT_INFO.client_id
                }
            };


            getCurrentUser(requestData)
                .then((response) => {

                    if (response.statusCode !== 200) {
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
    //token 유효성 검사
    checkToken() {
        return new Promise((resolve, reject) => {
            let tokenData = {
                token: storage.get('token')
            }

            checkAccessToken(tokenData)
                .then(() => {
                    resolve();
                })
                .catch((e) => {
                    reject(e);
                })
        })
    },
    //refresh token으로 token 갱신
    getRefreshToken({ commit }) {
        return new Promise((resolve, reject) => {
            let refresh_data = {
                grantType: 'refresh_token',
                refreshToken: storage.get('refresh_token')
            }

            getRefresh_Token(refresh_data)
                .then((res) => {
                    commit("SET_TOKEN", res['access_token']);
                    commit("SET_REFRESH_TOKEN", res['refresh_token']);

                    resolve(res);
                })
                .catch((e) => {
                    reject(e);
                })
        })
    },
    loginHistory() {
        return new Promise((resolve, reject) => {

            let requestData = {
                data: {
                    username: store.state.user.username,
                    clientId: CLIENT_INFO.client_id
                }
            };

            saveLoginHistory(requestData)
                .then((response) => {

                    if (response.statusCode !== 200) {
                        reject(response);
                    }

                    resolve(response);
                })
                .catch((e) => {
                    reject(e);
                })
        })
    }
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
};
