import request from "@/utils/request";
import {CLIENT_INFO} from "@/config";

// console.log(CLIENT_SECRET);

const API_PREFIX = '/api/user/users';

/**
 * 로그인
 * @param data 登录的表单数据
 */
export const login = (data) => {

  // form data 전송
  let _data = new FormData();
  
  // oauth2 인증방식
  _data.append('grant_type', data.grant_type);

  // 로그인 계정
  _data.append('username', data.username);
  _data.append('password', data.password);

  // oauth2 client 인증정보
  let _config = {
      auth: {
        username: CLIENT_INFO.client_id,
        password: CLIENT_INFO.client_secret
      },
      headers: {
         "Content-Type": "application/x-www-form-urlencoded"
      }
  };

  return request.post("/oauth/token", _data, _config);
};

/**
 * 로그아웃
 */
export const logout = (data) => {
  return request.post("/oauth/logout", data);
};

/**
 * 세션정보가져오기
 */
export const getCurrentUser = (data) => {
  return request.post('/oauth/login', data);
};

/**
 * 获取个人动态
 * @param {*} userId 用户ID
 */
// export const getAccessLogsByUserId = (userId) => {
//   return request.get("/api/access/log", {
//     params: {
//       userId: userId,
//       sortDesc: ["createTime"],
//       pageSize: 5,
//       resourceType: [0, 1, 2],
//     },
//   });
// };

/**
 * 사용자정보 변경
 */
export const saveUserProfile = (data) => {
  return request.post(API_PREFIX + "/profile", data);
};

/**
 * 비밀번호 변경
 */
export const updatePassword = (data) => {
  return request.post(API_PREFIX + "/password", data);
};

/**
 * token 유효성 확인
 */
export const checkAccessToken = (data) => {
  let _data = new FormData();
  _data.append('token', data.token);

  let _config = {
    auth: {
      username: CLIENT_INFO.client_id,
      password: CLIENT_INFO.client_secret
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  };

  return request.post("/oauth/check_token", _data, _config);
};

/**
 * refresh token으로 token 재발급
 */
export const getRefresh_Token = (data) => {
  let _data = new FormData();
  _data.append('grant_type', data.grantType);
  _data.append('refresh_token', data.refreshToken);

  let _config = {
    auth: {
      username: CLIENT_INFO.client_id,
      password: CLIENT_INFO.client_secret
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  };

  return request.post("/oauth/token", _data, _config);
};

/**
 * 로그인 기록 저장
 */
export const saveLoginHistory = (data) => {
  return request.post("/oauth/loginHistory", data);
}