<template>
  <div class="login-container">
    <div class="login-form">
      <div class="header">
        <div class="logo">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 22H5a3 3 0 0 1-3-3V3a1 1 0 0 1 1-1h14a1 1 0 0 1 1 1v12h4v4a3 3 0 0 1-3 3zm-1-5v2a1 1 0 0 0 2 0v-2h-2zM6 7v2h8V7H6zm0 4v2h8v-2H6zm0 4v2h5v-2H6z"/>
          </svg>
          <h1>博客系统</h1>
        </div>
        <p class="subtitle">分享你的知识与见解</p>
      </div>
      
      <el-form ref="loginForm" :model="form" :rules="rules" class="form-content">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="用户名"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="captcha">
          <div class="captcha-container">
            <el-input 
              v-model="form.captcha" 
              placeholder="验证码" 
              prefix-icon="Key"
              class="captcha-input"
            />
            <div class="captcha-image" @click="refreshCaptcha">
              <img v-if="captchaImage" :src="captchaImage" alt="验证码" />
              <div v-else class="captcha-placeholder">
                <el-icon><Refresh /></el-icon>
              </div>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleLogin"
            :loading="loading"
            class="login-btn"
          >
            <span v-if="!loading">登录</span>
            <span v-else>登录中...</span>
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="footer">
        <p>还没有账号? <span class="link" @click="goToRegister">立即注册</span></p>
        <p class="copyright">© 2024 博客系统 - 分享你的知识</p>
      </div>
    </div>
    
    <div class="decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'


import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import {  Refresh } from '@element-plus/icons-vue'
import {getCaptcha1, login} from '@/api/user'
const router = useRouter()
const userStore = useUserStore()

const form = ref({
  username: '',
  password: '',
  captcha: '',
  sessionId: ''
})

const captchaImage = ref('')
const loading = ref(false)

const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
})

// 获取验证码
const getCaptcha = async () => {
  const result = await getCaptcha1()
  captchaImage.value = result.data
  form.value.sessionId = result.sessionId
}
// 刷新验证码
const refreshCaptcha = () => {
  getCaptcha()
}

// 处理登录
const handleLogin = async () => {
  loading.value = true
  try {
    const result = await login({
      username: form.value.username,
      password: form.value.password,
      captcha: form.value.captcha,
      sessionId: form.value.sessionId
    })
    if (result.code === 200) {
      ElMessage.success('登录成功')
      // 保存用户信息到 store
      userStore.setUserInfo({
        token: result.data.token,
        user: result.data.user
      })
      router.push('/')
    } else {
      ElMessage.error(result.message)
      refreshCaptcha()
    }
  } catch (error) {
    ElMessage.error('登录失败，请稍后重试')
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register')
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  position: relative;
  overflow: hidden;
}

.login-form {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  z-index: 10;
  position: relative;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 10px;
}

.logo svg {
  width: 36px;
  height: 36px;
  fill: #6e8efb;
}

.logo h1 {
  font-size: 28px;
  color: #333;
  font-weight: 600;
}

.subtitle {
  color: #666;
  font-size: 16px;
  margin-top: 5px;
}

.form-content {
  margin-top: 20px;
}

.captcha-container {
  display: flex;
  gap: 10px;
}

.captcha-input {
  flex: 1;
}

.captcha-image {
  width: 120px;
  height: 40px;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: #f5f7fa;
  transition: all 0.3s;
}

.captcha-image:hover {
  border-color: #6e8efb;
  transform: translateY(-2px);
}

.captcha-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.captcha-placeholder {
  color: #999;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  margin-top: 10px;
}

.footer {
  margin-top: 25px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.link {
  color: #6e8efb;
  cursor: pointer;
  font-weight: 500;
  transition: color 0.3s;
}

.link:hover {
  color: #5a7de0;
  text-decoration: underline;
}

.copyright {
  margin-top: 15px;
  font-size: 12px;
  color: #999;
}

.decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  left: 10%;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 30%;
  left: 20%;
}
</style>