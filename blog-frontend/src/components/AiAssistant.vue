<template>
  <!-- AI助手悬浮按钮 -->
  <div 
    class="ai-assistant-button" 
    @mousedown="handleDragStart"
    :style="{
      left: `${buttonPosition.x}px`,
      top: `${buttonPosition.y}px`,
      right: 'auto',
      bottom: 'auto',
      cursor: isDragging ? 'grabbing' : 'grab'
    }"
  >
    <el-icon :size="30" color="#fff"><ChatDotRound /></el-icon>
  </div>

  <!-- AI对话弹窗 -->
  <el-dialog 
    v-model="aiDialogVisible" 
    title="AI助手" 
    width="500px"
    :close-on-click-modal="false"
    custom-class="ai-dialog"
  >
    <div class="chat-container">
      <div class="messages" ref="messagesContainer">
        <div 
          v-for="(message, index) in aiMessages" 
          :key="index"
          :class="['message', message.role]"
        >
          <div class="message-content">
            <div class="message-text">
              {{ message.content }}
              <span v-if="message.isLoading" class="loading-dots">{{ loadingDots }}</span>
            </div>
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
          </div>
        </div>
      </div>
      
      <div class="input-area">
        <el-input
          v-model="userInput"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          @keyup.enter.ctrl="sendToAi"
        />
        <div class="tips">按 Ctrl + Enter 发送</div>
        <div class="comment-actions">
          <el-button 
            type="primary" 
            @click="sendToAi"
            :loading="isAiLoading"
          >
            发送
          </el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { ChatDotRound } from '@element-plus/icons-vue'
import { getAiResponse } from '@/api/ai'

// AI对话相关状态
const aiDialogVisible = ref(false)
const userInput = ref('')
const aiMessages = ref([])
const isAiLoading = ref(false)
const messagesContainer = ref(null)
const loadingDots = ref('')

// 拖拽相关状态
const isDragging = ref(false)
const buttonPosition = ref({ x: window.innerWidth - 100, y: window.innerHeight - 100 })
const dragOffset = ref({ x: 0, y: 0 })
const dragStartTime = ref(0)

// 加载动画
const startLoadingAnimation = () => {
  let dots = 0
  loadingDots.value = ''
  const interval = setInterval(() => {
    dots = (dots + 1) % 4
    loadingDots.value = '.'.repeat(dots)
  }, 500)
  return interval
}

// 处理拖拽开始
const handleDragStart = (e) => {
  isDragging.value = true
  dragStartTime.value = Date.now()
  const rect = e.target.getBoundingClientRect()
  dragOffset.value = {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  }
}

// 处理拖拽中
const handleDrag = (e) => {
  if (!isDragging.value) return
  
  const x = e.clientX - dragOffset.value.x
  const y = e.clientY - dragOffset.value.y
  
  // 限制在窗口范围内
  const maxX = window.innerWidth - 60 // 按钮宽度
  const maxY = window.innerHeight - 60 // 按钮高度
  
  buttonPosition.value = {
    x: Math.max(0, Math.min(x, maxX)),
    y: Math.max(0, Math.min(y, maxY))
  }
}

// 处理拖拽结束
const handleDragEnd = () => {
  const dragDuration = Date.now() - dragStartTime.value
  isDragging.value = false
  
  // 如果拖拽时间小于200ms，认为是点击
  if (dragDuration < 200) {
    toggleAiDialog()
  }
}

// 打开/关闭AI对话框
const toggleAiDialog = () => {
  aiDialogVisible.value = !aiDialogVisible.value
  // 如果打开对话框且没有历史消息，添加欢迎语
  if (aiDialogVisible.value && aiMessages.value.length === 0) {
    addAiMessage({
      role: 'ai',
      content: '你好！我是AI助手，有什么问题都可以问我。',
      timestamp: new Date()
    })
  }
}

// 添加消息到对话
const addAiMessage = (message) => {
  aiMessages.value.push(message)
  scrollToBottom()
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 发送消息给AI
const sendToAi = async () => {
  if (!userInput.value.trim() || isAiLoading.value) return
  
  const userMessage = {
    role: 'user',
    content: userInput.value.trim(),
    timestamp: new Date()
  }
  
  addAiMessage(userMessage)
  console.log('用户消息:', userMessage.content)
  // 保存用户输入并清空输入框
  const message = userInput.value
  console.log('发送给AI的消息:', message)
  userInput.value = ''
  isAiLoading.value = true
  
  // 添加加载消息
  const loadingMessage = {
    role: 'ai',
    content: '思考中',
    timestamp: new Date(),
    isLoading: true
  }
  addAiMessage(loadingMessage)
  
  // 开始加载动画
  const loadingInterval = startLoadingAnimation()
  
  try {
    const response = await getAiResponse(message)
    
    // 移除加载消息
    aiMessages.value = aiMessages.value.filter(msg => !msg.isLoading)
    
    if (response.success) {
      addAiMessage({
        role: 'ai',
        content: response.data || response.message,
        timestamp: new Date()
      })
    } else {
      addAiMessage({
        role: 'ai',
        content: '抱歉，我暂时无法回答这个问题。请稍后再试。',
        timestamp: new Date()
      })
    }
  } catch (error) {
    // 移除加载消息
    aiMessages.value = aiMessages.value.filter(msg => !msg.isLoading)
    
    console.error('AI请求失败:', error)
    addAiMessage({
      role: 'ai',
      content: '服务连接异常，请检查网络后重试。',
      timestamp: new Date()
    })
  } finally {
    clearInterval(loadingInterval)
    isAiLoading.value = false
  }
}

// 格式化时间
const formatTime = (date) => {
  return new Date(date).toLocaleTimeString([], { 
    hour: '2-digit', 
    minute: '2-digit' 
  })
}

// 监听对话框打开状态，自动聚焦输入框
watch(aiDialogVisible, (val) => {
  if (val) {
    nextTick(() => {
      const input = document.querySelector('.input-area .el-input__inner')
      if (input) input.focus()
    })
  }
})

// 在组件挂载时添加全局事件监听
onMounted(() => {
  window.addEventListener('mousemove', handleDrag)
  window.addEventListener('mouseup', handleDragEnd)
})

// 在组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('mousemove', handleDrag)
  window.removeEventListener('mouseup', handleDragEnd)
})
</script>

<style scoped>
.ai-assistant-button {
  position: fixed;
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: grab;
  box-shadow: 0 4px 12px rgba(110, 142, 251, 0.3);
  z-index: 1000;
  transition: box-shadow 0.3s ease;
  user-select: none;
}

.ai-assistant-button:hover {
  box-shadow: 0 6px 16px rgba(110, 142, 251, 0.4);
}

.ai-assistant-button:active {
  cursor: grabbing;
}

/* 聊天容器 */
.chat-container {
  display: flex;
  flex-direction: column;
  height: 60vh;
  border-radius: 8px;
  overflow: hidden;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  background-color: #f9fafc;
  border-radius: 8px 8px 0 0;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message {
  display: flex;
  gap: 12px;
}

.message.ai {
  align-items: flex-start;
}

.message.user {
  flex-direction: row-reverse;
  align-items: flex-start;
}

.message-content {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
}

.message.ai .message-content {
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 18px 18px 18px 4px;
}

.message.user .message-content {
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  color: white;
  border-radius: 18px 18px 4px 18px;
}

.message-text {
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  text-align: right;
}

.input-area {
  padding: 15px 0 0;
}

.tips {
  font-size: 12px;
  color: #999;
  text-align: right;
  margin-top: 5px;
}

.comment-actions {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

.loading-dots {
  display: inline-block;
  min-width: 20px;
  animation: blink 1s infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
</style>

<style>
/* 覆盖Element UI样式 */
.ai-dialog .el-dialog__header {
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  margin: 0;
  padding: 15px 20px;
}

.ai-dialog .el-dialog__title {
  color: white;
}

.ai-dialog .el-dialog__headerbtn .el-dialog__close {
  color: white;
}
</style> 