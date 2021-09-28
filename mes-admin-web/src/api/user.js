import request from "@/utils/request";
import {CLIENT_INFO} from "@/config";

/**
 * 登录
 * @param data 登录的表单数据
 */
export const login = (data) => {

  let _data = new FormData();
  _data.append("grant_type", data.grant_type);
  _data.append("username", data.username);
  _data.append("password", data.password);

  let _config = {
      auth: {
        username: CLIENT_INFO.client_id,
        password: CLIENT_INFO.client_secret
      }
  };

  return request.post("/oauth/token", _data, _config);
};

/**
 * 登出
 */
export const logout = () => {
  return request.post("/api/logout");
};

/**
 * 获取个人信息
 */
export const getCurrentUser = (username) => {
  return request.get(`/api/user/name/${username}`);
};

/**
 * 获取个人动态
 * @param {*} userId 用户ID
 */
export const getAccessLogsByUserId = (userId) => {
  return request.get("/api/access/log", {
    params: {
      userId: userId,
      sortDesc: ["createTime"],
      pageSize: 5,
      resourceType: [0, 1, 2],
    },
  });
};

/**
 * 保存个人配置
 */
export const saveUserProfile = (data) => {
  return request.post("/api/users/profile", data);
};

/**
 * 修改密码
 */
export const updatePassword = (data) => {
  return request.post("/api/users/password", data);
};
