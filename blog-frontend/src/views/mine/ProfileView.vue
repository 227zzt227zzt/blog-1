<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="header">
        <div class="logo">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 22H5a3 3 0 0 1-3-3V3a1 1 0 0 1 1-1h14a1 1 0 0 1 1 1v12h4v4a3 3 0 0 1-3 3zm-1-5v2a1 1 0 0 0 2 0v-2h-2zM6 7v2h8V7H6zm0 4v2h8v-2H6zm0 4v2h5v-2H6z"/>
          </svg>
          <h1>个人中心</h1>
        </div>
      </div>

      <el-form 
        ref="profileForm" 
        :model="form" 
        :rules="rules" 
        label-width="100px"
        class="profile-form"
      >
        <div class="avatar-section">
          <el-avatar 
            :size="100" 
            :src="form.avatar ? 'http://localhost:8082/' + form.avatar : ''"
            @error="() => true"
          >
            <el-icon><User /></el-icon>
          </el-avatar>
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :http-request="handleAvatarUpload"
            accept="image/*"
          >
            <el-button type="primary" size="small">更换头像</el-button>
          </el-upload>
        </div>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" />
        </el-form-item>

        <el-form-item label="个人简介" prop="bio">
          <el-input 
            v-model="form.bio" 
            type="textarea" 
            :rows="4"
            placeholder="介绍一下自己吧..."
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { updateProfile,uploadAvatar } from '@/api/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const loading = ref(false)

const form = ref({
  id: '',
  username: '',
  email: '',
  nickname: '',
  bio: '',
  avatar: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ]
}

const profileForm = ref(null)

onMounted(async () => {
  // 从 store 中获取用户信息
  const userInfo = userStore.userInfo
  if (userInfo) {
    form.value = {
      id: userInfo.id,
      username: userInfo.username,
      email: userInfo.email,
      nickname: userInfo.nickname || '',
      bio: userInfo.bio || '',
      avatar: userInfo.avatar || ''
    }
    console.log('加载用户信息:', form.value)
  } else {
    ElMessage.warning('请先登录')
    router.push('/login')
  }
})

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('上传头像图片只能是图片格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
    return false
  }
  return true
}
const handleAvatarUpload = async (options) => {
  try {
    
    const file = options.file
    const formData = new FormData()
    formData.append('file', file)
    const result = await uploadAvatar(formData)
    if (result.code === 200) {
      form.value.avatar = result.data
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(result.message || '头像上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error(error.response?.data?.message || '头像上传失败')
  }
}

const handleSubmit = async () => {
  if (!profileForm.value) return
  
  try {
    await profileForm.value.validate()
    loading.value = true
    
    const result = await updateProfile(form.value)
    if (result.success) {
      ElMessage.success('个人信息更新成功')
      // 更新 store 中的用户信息
      userStore.userInfo = {
        ...userStore.userInfo,
        ...form.value
      }
    } else {
      ElMessage.error(result.message || '更新失败')
    }
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error(error.response?.data?.message || '更新失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 20px;
}

.profile-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
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

.profile-form {
  max-width: 600px;
  margin: 0 auto;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  margin-bottom: 30px;
}

.avatar-uploader {
  text-align: center;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #6e8efb inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #6e8efb inset;
}

:deep(.el-button--primary) {
  background: #6e8efb;
  border-color: #6e8efb;
}

:deep(.el-button--primary:hover) {
  background: #5a7de0;
  border-color: #5a7de0;
}
</style>
