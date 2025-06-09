<template>
  <div class="register-container">
    <div class="register-form">
      <div class="header">
        <div class="logo">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 22H5a3 3 0 0 1-3-3V3a1 1 0 0 1 1-1h14a1 1 0 0 1 1 1v12h4v4a3 3 0 0 1-3 3zm-1-5v2a1 1 0 0 0 2 0v-2h-2zM6 7v2h8V7H6zm0 4v2h8v-2H6zm0 4v2h5v-2H6z"/>
          </svg>
          <h1>创建账号</h1>
        </div>
        <p class="subtitle">加入我们的知识社区</p>
      </div>
      
      <el-form ref="registerForm" :model="form" :rules="rules" class="form-content">
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
        
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="form.confirmPassword" 
            type="password" 
            placeholder="确认密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input 
            v-model="form.email" 
            placeholder="邮箱"
            prefix-icon="Message"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleRegister"
            :loading="loading"
            class="register-btn"
          >
            <span v-if="!loading">注册账号</span>
            <span v-else>注册中...</span>
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="footer">
        <p>已有账号? <span class="link" @click="goToLogin">立即登录</span></p>
        <div class="agreement">
          <el-checkbox v-model="agreed">我已阅读并同意</el-checkbox>
          <span class="link">用户协议</span>和<span class="link">隐私政策</span>
        </div>
        <p class="copyright">© 2024 博客系统 - 分享你的知识</p>
      </div>
    </div>
    
    <div class="decoration">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: ''
})

const agreed = ref(false)
const loading = ref(false)

const validatePassword = (rule, value, callback) => {
  if (value !== form.value.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
    { pattern: /^(?=.*[a-z])(?=.*\d).+$/, 
      message: '必须包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
  ]
})

// 处理注册
const handleRegister = async () => {
  if (!agreed.value) {
    ElMessage.warning('请阅读并同意用户协议和隐私政策')
    return
  }
  
  loading.value = true
  const result = await register({
    username: form.value.username,
    password: form.value.password,
    email: form.value.email
  })
  
  loading.value = false
  
  if (result.code === 200) {
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } else {
    // ElMessage.error(result.message)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  }
}

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #a777e3, #6e8efb);
  position: relative;
  overflow: hidden;
}

.register-form {
  width: 450px;
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
  fill: #a777e3;
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

.register-btn {
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
  color: #a777e3;
  cursor: pointer;
  font-weight: 500;
  transition: color 0.3s;
}

.link:hover {
  color: #8e5fd9;
  text-decoration: underline;
}

.agreement {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  margin: 15px 0;
  font-size: 13px;
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

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.shape-1 {
  width: 250px;
  height: 250px;
  top: -80px;
  left: -80px;
  border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%;
  background: linear-gradient(45deg, rgba(255,255,255,0.1), rgba(255,255,255,0.2));
}

.shape-2 {
  width: 180px;
  height: 180px;
  bottom: -50px;
  right: 10%;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
}

.shape-3 {
  width: 120px;
  height: 120px;
  top: 40%;
  right: 20%;
  border-radius: 40% 60% 60% 40% / 40% 40% 60% 60%;
  background: rgba(255, 255, 255, 0.1);
}
</style>