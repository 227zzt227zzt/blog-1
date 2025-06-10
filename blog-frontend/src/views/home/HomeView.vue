<template>
  <div class="home-container">
    <div :class="['navbar', { 'navbar-solid': navbarSolid }]">
      <div class="navbar-logo">博客系统</div>
      <div class="navbar-links">
        <span class="nav-link" @click="goToHome">首页</span>
        <span class="nav-link" @click="goToArticles">文章</span>
        <span class="nav-link" @click="goToTags">标签</span>
        <span class="nav-link" @click="goToAbout">关于我们</span>
        <span class="nav-link" v-if="!userInfo" @click="goToLogin">登录</span>
        <span class="nav-link" v-if="!userInfo" @click="goToRegister">注册</span>
        <el-dropdown v-if="userInfo" class="nav-avatar-dropdown">
          <span class="nav-avatar">
            <el-avatar
              :size="32"
              :src="'http://localhost:8082' +
                userInfo.avatar ||
                'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
              "
            ></el-avatar>
            <span class="nav-username">{{ userInfo.username }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToProfile">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div class="hero-section">
      <div class="hero-content">
        <h2>分享知识，连接思想</h2>
        <p class="subtitle">一个专注于技术分享与学习的平台</p>
        <div id="text-container">
          {{ currentText }}<span class="type-cursor"></span>
        </div>
        <div class="cta-buttons">
          <el-button type="primary" size="large" @click="goToArticles">浏览文章</el-button>
          <el-button size="large" @click="goToCreate" v-if="userInfo">写文章</el-button>
        </div>
      </div>
    </div>

    <div class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>关于我们</h4>
          <p>致力于打造高质量的技术分享社区</p>
        </div>
        <div class="footer-section">
          <h4>联系方式</h4>
          <p>邮箱: contact@blog.com</p>
        </div>
        <div class="footer-section">
          <h4>关注我们</h4>
          <div class="social-icons">
            <el-icon :size="24"><ChatDotRound /></el-icon>
            <el-icon :size="24"><Pointer /></el-icon>
            <el-icon :size="24"><Link /></el-icon>
          </div>
        </div>
      </div>
      <div class="copyright">© 2024 博客系统 - 保留所有权利</div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useUserStore } from "@/store/user";
import { computed, ref, onMounted, onUnmounted } from "vue";
import { ChatDotRound, Pointer, Link  } from '@element-plus/icons-vue';
const router = useRouter();
const userStore = useUserStore();

const userInfo = computed(() => userStore.userInfo);
const logout = () => {
  userStore.clearUserInfo();
  router.push("/login");
};

const goToLogin = () => {
  router.push("/login");
};

const goToRegister = () => {
  router.push("/register");
};

const goToArticles = () => {
  router.push("/articles");
};

const goToCreate = () => {
  router.push("/article/create");
};

const goToProfile = () => {
  router.push("/profile");
};

const goToTags = () => {
  router.push("/tags");
};

function goToHome() {
  router.push("/");
}

function goToAbout() {
  alert("关于我们：这是一个专注于技术分享的博客平台。");
}

const navbarSolid = ref(false);
const currentText = ref("");

function handleScroll() {
  navbarSolid.value = window.scrollY > 30;
}

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});

// // AI对话相关状态
// const aiDialogVisible = ref(false)
// const userInput = ref('')
// const aiMessages = ref([])
// const isAiLoading = ref(false)
// const messagesContainer = ref(null)

// 拖拽相关状态
const isDragging = ref(false)
const buttonPosition = ref({ x: window.innerWidth - 100, y: window.innerHeight - 100 })
const dragOffset = ref({ x: 0, y: 0 })

// // 处理拖拽开始
// const handleDragStart = (e) => {
//   isDragging.value = true
//   const rect = e.target.getBoundingClientRect()
//   dragOffset.value = {
//     x: e.clientX - rect.left,
//     y: e.clientY - rect.top
//   }
// }

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
  isDragging.value = false
}

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

// // 打开/关闭AI对话框
// const toggleAiDialog = () => {
//   aiDialogVisible.value = !aiDialogVisible.value
//   // 如果打开对话框且没有历史消息，添加欢迎语
//   if (aiDialogVisible.value && aiMessages.value.length === 0) {
//     addAiMessage({
//       role: 'ai',
//       content: '你好！我是AI助手，有什么问题都可以问我。',
//       timestamp: new Date()
//     })
//   }
// }

// // 添加消息到对话
// const addAiMessage = (message) => {
//   aiMessages.value.push(message)
//   scrollToBottom()
// }

// 滚动到底部
// const scrollToBottom = () => {
//   nextTick(() => {
//     if (messagesContainer.value) {
//       messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
//     }
//   })
// }

// // 发送消息给AI
// const sendToAi = async () => {
//   if (!userInput.value.trim() || isAiLoading.value) return
  
//   const userMessage = {
//     role: 'user',
//     content: userInput.value.trim(),
//     timestamp: new Date()
//   }
  
  // addAiMessage(userMessage)
  // console.log('用户消息:', userMessage.content)
  // // 保存用户输入并清空输入框
  // const message = userInput.value
  // console.log('发送给AI的消息:', message)
  // userInput.value = ''
  // isAiLoading.value = true
  
  // try {
  //   const response = await getAiResponse(message)
    
  //   if (response.success) {
  //     addAiMessage({
  //       role: 'ai',
  //       content: response.data || response.message,
  //       timestamp: new Date()
  //     })
  //   } else {
  //     addAiMessage({
  //       role: 'ai',
  //       content: '抱歉，我暂时无法回答这个问题。请稍后再试。',
  //       timestamp: new Date()
  //     })
  //   }
  // } catch (error) {
  //   console.error('AI请求失败:', error)
  //   addAiMessage({
  //     role: 'ai',
  //     content: '服务连接异常，请检查网络后重试。',
  //     timestamp: new Date()
  //   })
  // } finally {
  //   isAiLoading.value = false
  // }


// // 格式化时间
// const formatTime = (date) => {
//   return new Date(date).toLocaleTimeString([], { 
//     hour: '2-digit', 
//     minute: '2-digit' 
//   })
// }

// // 监听对话框打开状态，自动聚焦输入框
// watch(aiDialogVisible, (val) => {
//   if (val) {
//     nextTick(() => {
//       const input = document.querySelector('.input-area .el-input__inner')
//       if (input) input.focus()
//     })
//   }
// })

</script>

<style scoped>
.home-container {
  width: 100vw;
  min-width: 0;
  max-width: none;
  margin: 0;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: url("http://localhost:8082/upload/backgroud/backgroud.jpg") center center/cover no-repeat;
  position: relative;
  padding-bottom: 300px; /* Add padding to prevent content from being hidden behind footer */
}

.home-container::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.home-container > * {
  position: relative;
  z-index: 1;
}

.navbar {
  width: 100%;
  height: 60px;
  background: rgba(255, 255, 255, 0);
  box-shadow: 0 2px 8px rgba(110, 142, 251, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 40px;
  position: sticky;
  top: 0;
  z-index: 100;
  transition: background 0.4s cubic-bezier(0.4, 0, 0.2, 1),
    box-shadow 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.navbar-solid {
  background: #fff;
  box-shadow: 0 2px 8px rgba(110, 142, 251, 0.08);
}

.navbar-logo {
  font-size: 22px;
  font-weight: bold;
  color: #6e8efb;
  letter-spacing: 2px;
  position: absolute;
  left: 40px;
}

.navbar-links {
  display: flex;
  gap: 28px;
  justify-content: center;
}

.nav-link {
  font-size: 16px;
  color: #333;
  cursor: pointer;
  transition: color 0.2s;
  position: relative;
}

.nav-link:hover {
  color: #6e8efb;
}

.hero-section {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 60px 0;
  padding: 30px 0;
}

.hero-content {
  text-align: center;
  max-width: 800px;
  padding: 0 20px;
}

.hero-content h2 {
  font-size: 36px;
  color: #333;
  margin-bottom: 20px;
}

.subtitle {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.cta-buttons {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.footer {
  margin-top: auto;
  background: #f9fafc;
  border-radius: 0;
  padding: 40px 20px;
  width: 100%;
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 10;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 0 40px 0;
  box-sizing: border-box;
}

.footer-section {
  text-align: center;
}

.footer-section h4 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
}

.footer-section p {
  color: #666;
  line-height: 1.6;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 15px;
}

.copyright {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  color: #999;
  font-size: 14px;
}

.nav-avatar-dropdown {
  display: flex;
  align-items: center;
  position: absolute;
  right: 40px;
}

.nav-avatar {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 0 8px;
}

.nav-username {
  font-size: 15px;
  color: #333;
}

#text-container {
  font-size: 24px;
  color: #333;
  min-height: 32px;
  margin-bottom: 18px;
  letter-spacing: 1px;
  white-space: pre-wrap;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: auto;
  max-width: 100%;
  box-sizing: border-box;
  overflow-wrap: break-word;
  word-break: break-all;
  overflow: visible;
}

.type-cursor {
  display: inline-block;
  width: 2px;
  height: 1.2em;
  background: #6e8efb;
  margin-left: 2px;
  animation: blink-cursor 0.8s steps(1) infinite;
  vertical-align: middle;
}

/* AI助手悬浮按钮 */
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

/* 打字指示器 */
.typing-indicator {
  display: inline-flex;
  align-items: center;
  margin-right: 8px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background-color: #6e8efb;
  border-radius: 50%;
  display: inline-block;
  margin-right: 4px;
  animation: bounce 1.3s infinite ease;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

.loading-text {
  margin-left: 10px;
  color: #666;
  font-size: 14px;
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
  font-weight: 600;
}

.ai-dialog .el-dialog__headerbtn {
  top: 15px;
}

.ai-dialog .el-dialog__headerbtn .el-icon {
  color: white;
}
</style>