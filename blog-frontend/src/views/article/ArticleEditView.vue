<template>
  <div class="edit-article-container">
    <div class="blog-header" @click="goToHome">
      <div class="blog-logo">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="currentColor"
        >
          <path
            d="M19 22H5a3 3 0 0 1-3-3V3a1 1 0 0 1 1-1h14a1 1 0 0 1 1 1v12h4v4a3 3 0 0 1-3 3zm-1-5v2a1 1 0 0 0 2 0v-2h-2zM6 7v2h8V7H6zm0 4v2h8v-2H6zm0 4v2h5v-2H6z"
          />
        </svg>
        <span class="welcome-text">欢迎来到227博客</span>
      </div>
    </div>

    <h2 class="page-title">{{ form.id ? '编辑文章' : '新建文章' }}</h2>
    <el-form :model="form" label-width="100px" class="article-form">
      <el-form-item label="标题">
        <el-input 
          v-model="form.title" 
          placeholder="请输入文章标题"
          class="custom-input"
        />
      </el-form-item>
      <el-form-item label="摘要">
        <el-input 
          v-model="form.summary" 
          placeholder="请输入文章摘要"
          class="custom-input"
        />
      </el-form-item>
      <el-form-item label="内容">
        <el-input 
          type="textarea" 
          v-model="form.content" 
          :rows="12"
          placeholder="请输入文章内容"
          class="custom-textarea"
        />
      </el-form-item>
      <el-form-item label="封面">
        <div class="cover-upload-container">
          <el-upload
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="handleCoverUpload"
            accept="image/*"
            class="cover-uploader"
          >
            <div class="upload-trigger">
              <el-icon class="upload-icon"><Plus /></el-icon>
              <span>上传封面</span>
            </div>
          </el-upload>
          <div v-if="form.coverImage" class="cover-preview">
            <img :src="form.coverImage" alt="封面预览" />
            <div class="cover-actions">
              <el-button type="danger" size="small" @click="form.coverImage = ''">删除</el-button>
            </div>
          </div>
        </div>
      </el-form-item>
      <el-form-item class="form-actions">
        <el-button type="primary" @click="handleSubmit" class="submit-btn">保存文章</el-button>
        <el-button @click="goBack" class="cancel-btn">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { uploadArticleCover, getArticleById, updateArticle } from '@/api/article'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const form = ref({
  id: null,
  title: '',
  summary: '',
  content: '',
  coverImage: ''
})

const goToHome = () => {
  router.push('/')
}

const loadArticle = async () => {
  try {
    const res = await getArticleById(route.params.id)
    if (res.success) {
      const article = res.data
      form.value = {
        id: article.id,
        title: article.title,
        summary: article.summary,
        content: article.content,
        coverImage:"http://localhost:8082/"+ article.coverImage
      }
    } else {
      ElMessage.error(res.message || '获取文章失败')
      router.push('/articles')
    }
  } catch (error) {
    console.error('获取文章失败:', error)
    ElMessage.error('获取文章失败')
    router.push('/articles')
  }
}

const beforeUpload = (file) => {
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片')
    return false
  }
  return true
}
const coverImage1 = ref('')
const handleCoverUpload = async (options) => {
  const formData = new FormData()
  formData.append('file', options.file)
  formData.append('articleId', form.value.id)
  const res = await uploadArticleCover(formData)
  if (res.success) {
    form.value.coverImage ="http://localhost:8082/"+ res.data
    coverImage1.value = res.data
    ElMessage.success('封面上传成功')
  } else {
    ElMessage.error(res.message)
  }
}

const handleSubmit = async () => {
  if (!form.value.title) {
    ElMessage.warning('请输入文章标题')
    return
  }
  if (!form.value.content) {
    ElMessage.warning('请输入文章内容')
    return
  }
  
  try {
    const res = await updateArticle(form.value.id, {
      title: form.value.title,
      summary: form.value.summary,
      content: form.value.content,
      coverImage: coverImage1.value
    })
    
    if (res.success) {
      ElMessage.success('文章更新成功')
      router.push(`/article/${form.value.id}`)
    } else {
      ElMessage.error(res.message || '更新失败')
    }
  } catch (error) {
    console.error('更新文章失败:', error)
    ElMessage.error('更新失败')
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.edit-article-container {
  max-width: 900px;
  margin: 40px auto;
  padding: 30px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.page-title {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 30px;
  font-weight: 600;
  text-align: center;
}

.article-form {
  padding: 20px;
}

.custom-input :deep(.el-input__inner) {
  height: 45px;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__inner:focus) {
  border-color: #6e8efb;
  box-shadow: 0 0 0 2px rgba(110, 142, 251, 0.1);
}

.custom-textarea :deep(.el-textarea__inner) {
  border-radius: 8px;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, Courier, monospace;
  line-height: 1.6;
  padding: 12px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
}

.custom-textarea :deep(.el-textarea__inner:focus) {
  border-color: #6e8efb;
  box-shadow: 0 0 0 2px rgba(110, 142, 251, 0.1);
}

.cover-upload-container {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.cover-uploader {
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cover-uploader:hover {
  border-color: #6e8efb;
}

.upload-trigger {
  width: 200px;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.upload-icon {
  font-size: 28px;
  margin-bottom: 8px;
}

.cover-preview {
  position: relative;
  width: 200px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 8px;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.cover-preview:hover .cover-actions {
  opacity: 1;
}

.form-actions {
  margin-top: 40px;
  text-align: center;
}

.submit-btn {
  padding: 12px 30px;
  font-size: 16px;
  background: #6e8efb;
  border-color: #6e8efb;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background: #5a7de0;
  border-color: #5a7de0;
  transform: translateY(-1px);
}

.cancel-btn {
  padding: 12px 30px;
  font-size: 16px;
  margin-left: 16px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #2c3e50;
}

@media (max-width: 768px) {
  .edit-article-container {
    margin: 20px;
    padding: 20px;
  }
  
  .cover-upload-container {
    flex-direction: column;
  }
  
  .cover-preview {
    margin-top: 20px;
  }
}

.blog-header {
  background: white;
  padding: 16px 0;
  margin-bottom: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
}

.blog-header:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.blog-logo {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.blog-logo svg {
  width: 32px;
  height: 32px;
  fill: #6e8efb;
}

.welcome-text {
  font-size: 20px;
  color: #333;
  font-weight: 500;
}
</style>
