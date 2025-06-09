<template>
  <div class="article-content-container">
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

    <div class="article-header">
      <div class="back-button" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回列表
      </div>
      <div class="article-actions" v-if="isAuthor">
        <el-button type="primary" @click="goToEdit">
          <el-icon><Edit /></el-icon>
          编辑文章
        </el-button>
        <el-button type="danger" @click="handleDelete">
          <el-icon><Delete /></el-icon>
          删除文章
        </el-button>
      </div>
    </div>

    <div class="article-main">
      <h1 class="article-title">{{ article.title }}</h1>
      
      <div class="article-meta">
        <span class="meta-item">
          <el-icon><User /></el-icon>
          {{ article.author }}
        </span>
        <span class="meta-item">
          <el-icon><Calendar /></el-icon>
          {{ formatDate(article.createTime) }}
        </span>
        <span class="meta-item">
          <el-icon><View /></el-icon>
          {{ article.viewCount || 0 }} 阅读
        </span>
      </div>

      <div class="article-cover" v-if="article.coverImage">
        <el-image 
          :src="'http://localhost:8082/'+article.coverImage" 
          fit="cover"
          :preview-src-list="[article.coverImage]"
        >
          <template #error>
            <div class="image-placeholder">
              <el-icon><Picture /></el-icon>
            </div>
          </template>
        </el-image>
      </div>

      <div class="article-body markdown-body" v-html="renderedContent"></div>
    </div>

    <div class="article-footer">
      <div class="article-tags" v-if="article.tags && article.tags.length">
        <el-tag 
          v-for="tag in article.tags" 
          :key="tag.id"
          class="tag"
        >
          {{ tag.name }}
        </el-tag>
      </div>
    </div>

    <!-- 评论区 -->
    <div class="comments-section">
      <h3 class="comments-title">评论 ({{ totalComments }})</h3>
      
      <!-- 评论输入框 -->
      <div class="comment-input" v-if="userStore.userInfo">
        <el-input
          v-model="commentContent"
          type="textarea"
          :rows="3"
          placeholder="写下你的评论..."
          :maxlength="500"
          show-word-limit
        />
        <div class="comment-actions">
          <el-button type="primary" @click="submitComment" :loading="submitting">
            发表评论
          </el-button>
        </div>
      </div>
      <div v-else class="login-tip">
        请<el-button type="text" @click="goToLogin">登录</el-button>后发表评论
      </div>

      <!-- 评论列表 -->
      <div class="comments-list">
        <div v-if="comments.length === 0" class="no-comments">
          暂无评论，快来发表第一条评论吧！
        </div>
        <div v-else v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-header">
            <span class="comment-author">{{ comment.userName }}</span>
            <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
          </div>
          <div class="comment-content">{{ comment.content }}</div>
          <div class="comment-actions">
            <el-button type="text" @click="replyToComment(comment)">回复</el-button>
          </div>
          
          <!-- 回复输入框 -->
          <div v-if="showReplyInput && replyingTo && replyingTo.id === comment.id" class="reply-input">
            <el-input
              v-model="replyContent"
              type="textarea"
              :rows="2"
              placeholder="写下你的回复..."
              :maxlength="500"
              show-word-limit
            />
            <div class="reply-actions">
              <el-button @click="cancelReply">取消</el-button>
              <el-button type="primary" @click="submitReply(comment)" :loading="submitting">
                发表回复
              </el-button>
            </div>
          </div>
          
          <!-- 回复列表 (只展示一级回复) -->
          <div v-if="comment.replies && comment.replies.length > 0" class="replies-list">
            <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
              <div class="reply-header">
                <span class="reply-author">{{ reply.userName }}</span>
                <span class="reply-time">{{ formatDate(reply.createTime) }}</span>
              </div>
              <div class="reply-content">{{ reply.content }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination" v-if="totalComments > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="totalComments"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup name="ArticleContentView">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Edit, Delete, User, Calendar, View, Picture } from '@element-plus/icons-vue'
import { getArticleById, deleteArticle } from '@/api/article'
import { getArticleComments, saveComment } from '@/api/comment'
import { marked } from 'marked'
import DOMPurify from 'dompurify'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const article = ref({})
const loading = ref(false)

const isAuthor = computed(() => {
  return userStore.userInfo && article.value.authorId === userStore.userInfo.id
})

const renderedContent = computed(() => {
  if (!article.value.content) return ''
  const rawHtml = marked(article.value.content)
  return DOMPurify.sanitize(rawHtml)
})

const comments = ref([])
const commentContent = ref('')
const submitting = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalComments = ref(0)
const replyingTo = ref(null)
const showReplyInput = ref(false)
const replyContent = ref('')

// Function to build the comment tree from a flat list
const buildCommentTree = (flatComments) => {
  const commentMap = {};
  const rootComments = [];

  flatComments.forEach(comment => {
    comment.replies = []; // Ensure each comment has a replies array
    commentMap[comment.id] = comment;
  });

  // Sort comments by creation time to ensure parents are generally processed before children
  flatComments.sort((a, b) => new Date(a.createTime).getTime() - new Date(b.createTime).getTime());

  flatComments.forEach(comment => {
    if (comment.parentId && comment.parentId !== 0) {
      const parent = commentMap[comment.parentId];
      if (parent) {
        parent.replies.push(comment);
      }
    } else {
      rootComments.push(comment);
    }
  });

  return rootComments;
};

const loadArticle = async () => {
  try {
    loading.value = true
    const result = await getArticleById(route.params.id)
    if (result.code === 200) {
      console.log('获取文章结果:', result)
      article.value = result.data
      console.log('获取文章:', article.value)
    } 
    else {
      ElMessage.error(result.message || '获取文章失败')
      router.push('/articles')
    }
  } catch (error) {
    console.error('获取文章失败:', error)
    ElMessage.error('获取文章失败')
    router.push('/articles')
  } finally {
    loading.value = false
  }
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const goBack = () => {
  router.push('/articles')
}

const goToEdit = () => {
  router.push(`/article/edit/${article.value.id}`)
}

const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这篇文章吗？此操作不可恢复。',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const result = await deleteArticle(article.value.id)
    if (result.code === 200) {
      ElMessage.success('删除成功')
      router.push('/articles')
    } else {
      ElMessage.error(result.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除文章失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const loadComments = async () => {
  try {
    console.log('加载评论:', currentPage.value, pageSize.value,route.params.id)
    const result = await getArticleComments({
      page: currentPage.value,
      size: pageSize.value,
      articleId: route.params.id
    });
    console.log('获取文章评论列表:', result)
    if (result.code === 200) {
      // Build the comment tree
      comments.value = buildCommentTree(result.data.users.records)
      totalComments.value = result.data.users.total
    } else {
      ElMessage.error(result.message || '获取评论失败')
    }
  } catch (error) {
    console.error('获取评论失败:', error)
    ElMessage.error('获取评论失败')
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  try {
    submitting.value = true
    const commentDTO = {
      articleId: route.params.id,
      content: commentContent.value,
      parentId: 0, // Top-level comment
      userId: userStore.userInfo.id,
      userName: userStore.userInfo.userName, // Add userName
      status: 1
    }
    const result = await saveComment(commentDTO)
    if (result.code === 200) {
      ElMessage.success('评论成功')
      commentContent.value = ''
      loadComments()
    } else {
      ElMessage.error(result.message || '评论失败')
    }
  } catch (error) {
    console.error('评论失败:', error)
    ElMessage.error('评论失败')
  } finally {
    submitting.value = false
  }
}

const submitReply = async (parentComment) => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    submitting.value = true
    const commentDTO = {
      articleId: route.params.id,
      content: replyContent.value,
      parentId: parentComment.id, // Parent ID for reply
      userId: userStore.userInfo.id,
      userName: userStore.userInfo.userName, // Add userName
      status: 1
    }
    const result = await saveComment(commentDTO)
    if (result.code === 200) {
      ElMessage.success('回复成功')
      replyContent.value = ''
      showReplyInput.value = false
      replyingTo.value = null
      loadComments() // Reload comments to show new reply
    } else {
      ElMessage.error(result.message || '回复失败')
    }
  } catch (error) {
    console.error('回复失败:', error)
    ElMessage.error('回复失败')
  } finally {
    submitting.value = false
  }
}

const replyToComment = (comment) => {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  replyingTo.value = comment
  showReplyInput.value = true
  replyContent.value = `@${comment.userName} ` // Use userName for mention
}

const cancelReply = () => {
  showReplyInput.value = false
  replyingTo.value = null
  replyContent.value = ''
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadComments()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadComments()
}

const goToLogin = () => {
  router.push('/login')
}

const goToHome = () => {
  router.push('/')
}

onMounted(() => {
  loadArticle()
  loadComments()
})
</script>

<style scoped>
.article-content-container {
  max-width: 900px;
  margin: 40px auto;
  padding: 0 20px;
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

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  cursor: pointer;
  font-size: 16px;
  transition: color 0.3s;
}

.back-button:hover {
  color: #6e8efb;
}

.article-actions {
  display: flex;
  gap: 12px;
}

.article-main {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
}

.article-title {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin-bottom: 20px;
  line-height: 1.4;
}

.article-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  color: #666;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.article-cover {
  margin: 30px -40px;
  height: 400px;
  overflow: hidden;
}

.article-cover .el-image {
  width: 100%;
  height: 100%;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 48px;
}

.article-body {
  margin-top: 40px;
  line-height: 1.8;
  color: #333;
}

.article-footer {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.article-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  background: #f5f7fa;
  border: none;
  color: #666;
}

:deep(.el-button--primary) {
  background: #6e8efb;
  border-color: #6e8efb;
}

:deep(.el-button--primary:hover) {
  background: #5a7de0;
  border-color: #5a7de0;
}

:deep(.el-button--danger) {
  background: #f56c6c;
  border-color: #f56c6c;
}

:deep(.el-button--danger:hover) {
  background: #e64242;
  border-color: #e64242;
}

/* Markdown 样式 */
:deep(.markdown-body) {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
}

:deep(.markdown-body h1),
:deep(.markdown-body h2),
:deep(.markdown-body h3),
:deep(.markdown-body h4),
:deep(.markdown-body h5),
:deep(.markdown-body h6) {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
  line-height: 1.25;
}

:deep(.markdown-body p) {
  margin-top: 0;
  margin-bottom: 16px;
}

:deep(.markdown-body code) {
  padding: 0.2em 0.4em;
  margin: 0;
  font-size: 85%;
  background-color: rgba(27,31,35,0.05);
  border-radius: 3px;
}

:deep(.markdown-body pre) {
  padding: 16px;
  overflow: auto;
  font-size: 85%;
  line-height: 1.45;
  background-color: #f6f8fa;
  border-radius: 3px;
}

:deep(.markdown-body blockquote) {
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
  margin: 0 0 16px 0;
}

:deep(.markdown-body img) {
  max-width: 100%;
  box-sizing: content-box;
  background-color: #fff;
}

/* 评论区样式 */
.comments-section {
  margin-top: 40px;
  padding-top: 40px;
  border-top: 1px solid #eee;
}

.comments-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 30px;
}

.comment-input {
  margin-bottom: 30px;
}

.comment-actions {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

.login-tip {
  text-align: center;
  color: #666;
  margin: 20px 0;
}

.comments-list {
  margin-top: 30px;
}

.no-comments {
  text-align: center;
  color: #999;
  padding: 30px 0;
}

.comment-item {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.comment-author {
  font-weight: 500;
  color: #333;
}

.comment-time {
  color: #999;
  font-size: 14px;
}

.comment-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.reply-input {
  margin: 10px 0;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.reply-actions {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.replies-list {
  margin-left: 20px;
  padding-left: 20px;
  border-left: 2px solid #eee;
}

.reply-item {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.reply-author {
  font-weight: 500;
  color: #333;
}

.reply-time {
  color: #999;
  font-size: 12px;
}

.reply-content {
  color: #666;
  line-height: 1.5;
}
</style>
