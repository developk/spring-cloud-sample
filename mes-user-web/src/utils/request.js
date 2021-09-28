import axios from 'axios'
import {API_BASE_PATH} from "@/config";
import qs from 'qs';
import Vue from "vue";
import store from "@/store";

axios.defaults.withCredentials = true;

const request = axios.create({
    baseURL: API_BASE_PATH, // url = base url + request url
    // send cookies when cross-domain requests
    // timeout: 5000 // request timeout
});

//request config 저장 변수
let _originalRequest = "";

/**
 * token 포함.
 * 요청 시 token 유효성 확인 후 전달
 */
request.interceptors.request.use(function (config) {
    config.headers = {
        ...(store.state.user.token ? {Authorization: 'Bearer ' + store.state.user.token} : {}),
        ...(config.headers || {})
    };

    if (config.method === 'get') {
        config.paramsSerializer = function (params) {
            return qs.stringify(params, {arrayFormat: 'repeat'})
        }
    }

    // /oauth/token, /oauth/check_token
    if (!config.headers['Content-Type']) {
        config.data.username = store.state.user.username;
    }

    //인증 관련 요청 외 모든 요청들은 token 유효성 검사 진행
    if(config.url.indexOf("oauth") === -1) {
        //인증 요청 전 요청된 request 저장
        _originalRequest = config;

        //token 유효성 인증 요청
        return store.dispatch("user/checkToken")
            .then(() => {
                return Promise.resolve(config);
            })
    }

    return config
}, function (err) {
    console.error('err: %o', err);
    return Promise.reject(err)
});

/**
 * 处理响应
 */
request.interceptors.response.use(function (res) {
    //如果是下载文件，则直接返回
    if (res.request.responseType === 'blob') {
        return res;
    }
    if (res.status !== 200) {
        return Promise.reject(res.data)
    }
    return res.data;

}, function (err) {

    //token 유효성 검사 중 오류가 발생했을 경우
    if(err.response.status === 401) {
        return new Promise(() => {
            let resData = err.response.data;

            //token이 유효하지 않은 경우 → logout 처리
            if(resData.statusCode && resData.statusCode === 601){
                Vue.$toast.error(resData.message, {position: 'top-right'});
                store.dispatch("user/logout")
                    .then(() => {
                        window.location = '/login';
                    })
                    .catch((e) => {
                        console.warn(e);
                        return Promise.reject(e);
                    });
            }
            //token이 만료 된 경우 → token 갱신
            else if (resData.statusCode && resData.statusCode === 602) {

                store.dispatch("user/getRefreshToken")
                    .then((res) => {
                        //갱신 완료 후 새롭게 받은 token으로 교체 후 재요청
                        _originalRequest.headers = {Authorization: 'Bearer ' + res["access_token"]};
                        return request(_originalRequest);
                    })
                    .catch((e) => {
                        console.warn(e);
                        return Promise.reject(e);
                    })
            }
        })
    }

    return Promise.reject(err);
});

export default request;

export const download = (url, filename = '') => {
    request.get(`${url}?fileName=${filename}`, {responseType: 'blob'}).then(response => {
        const blob = new Blob([response.data]);
        const tempLink = document.createElement('a');     // 创建a标签
        const href = window.URL.createObjectURL(blob);       // 创建下载的链接
        //filename
        const fileName = decodeURI(response.headers['content-disposition'].split('filename=')[1]);
        tempLink.href = href;
        tempLink.target = "_blank";
        tempLink.download = fileName;
        document.body.appendChild(tempLink);
        tempLink.click();     // 点击下载
        document.body.removeChild(tempLink); // 下载完成移除元素
        window.URL.revokeObjectURL(href) // 释放掉blob对象
    })
};