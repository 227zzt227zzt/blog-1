import axios from 'axios';
import {
    ElMessage
} from 'element-plus';
import {
    useUserStore
} from '@/store/user';
import router from '@/router';
import { API_BASE_URL } from '@/utils/config';

const service = axios.create({
    baseURL: API_BASE_URL, 
    timeout: 5000,
    withCredentials: true // 允许跨域请求携带凭证
});

// 是否正在刷新token
let isRefreshing = false;
// 重试队列
let retryRequests = [];

// 请求拦截器
service.interceptors.request.use(
    config => {
        const userStore = useUserStore();
        const token = userStore.token;
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        console.error('Request error:', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data;
        if (res.code !== 200) {
            ElMessage.error(res.message || '请求失败');
            return Promise.reject(new Error(res.message || '请求失败'));
        }
        return res;
    },
    async error => {
        const {
            response
        } = error;
        const userStore = useUserStore();

        if (response && response.status === 401) {
            if (!isRefreshing) {
                isRefreshing = true;

                try {
                    // 尝试刷新token
                    const refreshToken = sessionStorage.getItem('refreshToken');
                    if (refreshToken) {
                        const res = await service.post('/users/refresh-token', {
                            refreshToken
                        });
                        if (res.code === 200) {
                            userStore.setUserInfo({
                                token: res.data.token,
                                user: userStore.userInfo
                            });

                            // 重试队列中的请求
                            retryRequests.forEach(cb => cb(res.data.token));
                            retryRequests = [];

                            // 重试当前请求
                            error.config.headers['Authorization'] = `Bearer ${res.data.token}`;
                            return service(error.config);
                        }
                    }
                } catch (refreshError) {
                    console.error('Token refresh failed:', refreshError);
                } finally {
                    isRefreshing = false;
                }
            } else {
                // 将请求加入重试队列
                return new Promise(resolve => {
                    retryRequests.push(token => {
                        error.config.headers['Authorization'] = `Bearer ${token}`;
                        resolve(service(error.config));
                    });
                });
            }

            // token刷新失败，清除用户信息并跳转到登录页
            userStore.clearUserInfo();
            router.push('/login');
            ElMessage.error('登录已过期，请重新登录');
        } else if (response && response.status === 403) {
            ElMessage.error('没有权限，请重新登录');
            userStore.clearUserInfo();
            router.push('/login');
        } else {
            ElMessage.error(error.message || '请求失败');
        }
        return Promise.reject(error);
    }
);

export default service;