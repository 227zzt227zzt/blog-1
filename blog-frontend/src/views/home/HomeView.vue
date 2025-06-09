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

   <!-- 修改后的AI聊天区域 -->
   <div class="ai-chat-section">
      <div class="chat-container">
        <div class="chat-messages" ref="chatMessages">
          <div v-for="(message, index) in chatMessages" :key="index" 
               :class="['message', message.type]">
            {{ message.content }}
          </div>
          <div v-if="isLoading" class="message ai">
            <span class="typing-indicator">
              <span></span><span></span><span></span>
            </span>
          </div>
        </div>
        <div class="chat-input">
          <el-input
            v-model="userMessage"
            placeholder="输入消息..."
            :disabled="isLoading"
            @keyup.enter="sendMessage"
          >
            <template #append>
              <el-button 
                @click="sendMessage"
                :disabled="isLoading || !userMessage.trim()"
                :loading="isLoading"
              >
                发送
              </el-button>
            </template>
          </el-input>
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
import { computed, ref, onMounted, onUnmounted,nextTick } from "vue";

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

function handleScroll() {
  navbarSolid.value = window.scrollY > 30;
}

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});

// AI聊天相关状态
const userMessage = ref("");
const chatMessages = ref([]);
const isLoading = ref(false);
const chatMessagesRef = ref(null);
const abortController = ref(null);

// 发送消息函数 - 使用原生fetch处理流
const sendMessage = async () => {
  if (!userMessage.value.trim() || isLoading.value) return;
  
  // 添加用户消息
  const userMsg = userMessage.value.trim();
  chatMessages.value.push({ type: 'user', content: userMsg });
  userMessage.value = "";
  
  // 设置加载状态
  isLoading.value = true;
  
  // 添加AI消息占位符
  chatMessages.value.push({ type: 'ai', content: '' });
  await scrollToBottom();
  
  try {
    // 创建AbortController以便取消请求
    abortController.value = new AbortController();
    
    // 使用原生fetch处理流式响应
    const response = await fetch(`/ai/chatStream?message=${encodeURIComponent(userMsg)}`, {
      method: 'GET',
      headers: {
        'Accept': 'text/event-stream',
        'Content-Type': 'application/json',
      },
      signal: abortController.value.signal
    });
    
    if (!response.ok || !response.body) {
      throw new Error("AI服务响应异常");
    }
    
    const reader = response.body.getReader();
    const decoder = new TextDecoder("utf-8");
    let aiMessage = "";
    let shouldContinue = true;
    
    // 修改循环条件
    while (shouldContinue) {
      const { done, value } = await reader.read();
      
      if (done) {
        shouldContinue = false;
        isLoading.value = false;
        break;
      }
      
      // 解码并处理数据
      const chunk = decoder.decode(value, { stream: true });
      aiMessage += chunk;
      
      // 更新AI消息内容
      const lastIndex = chatMessages.value.length - 1;
      chatMessages.value[lastIndex].content = aiMessage;
      
      // 滚动到底部
      await scrollToBottom();
    }
  } catch (error) {
    if (error.name !== 'AbortError') {
      console.error('AI请求错误:', error);
      
      // 更新最后一条消息为错误信息
      const lastIndex = chatMessages.value.length - 1;
      if (lastIndex >= 0) {
        chatMessages.value[lastIndex] = {
          type: 'error',
          content: 'AI服务暂时不可用，请稍后再试'
        };
      }
    }
    isLoading.value = false;
  } finally {
    abortController.value = null;
  }
};

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick();
  if (chatMessagesRef.value) {
    chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight;
  }
};


// 组件卸载时取消请求
onUnmounted(() => {
  if (abortController.value) {
    abortController.value.abort();
  }
});
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
  margin-top: 60px;
  max-width: none;
  width: 100%;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  max-width: none !important;
  width: 100%;
  margin: 0;
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

@keyframes blink-cursor {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}

.ai-chat-section {
  padding: 40px 20px;
  display: flex;
  justify-content: center;
  background: rgba(255, 255, 255, 0.9);
  margin: 40px auto;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  width: 90%;
}

.chat-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chat-messages {
  height: 400px;
  overflow-y: auto;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message {
  padding: 10px 16px;
  border-radius: 8px;
  max-width: 80%;
  word-wrap: break-word;
}

.message.user {
  background: #6e8efb;
  color: white;
  align-self: flex-end;
}

.message.ai {
  background: white;
  color: #333;
  align-self: flex-start;
  border: 1px solid #e4e7ed;
}

.message.error {
  background: #fef0f0;
  color: #f56c6c;
  align-self: center;
  width: 100%;
  text-align: center;
}

.chat-input {
  width: 100%;
}
.ai-chat-section {
  padding: 40px 20px;
  display: flex;
  justify-content: center;
  background: rgba(255, 255, 255, 0.95); /* 更透明的背景 */
  margin: 40px auto;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15); /* 增强阴影 */
  max-width: 800px;
  width: 90%;
}

.chat-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.chat-messages {
  height: 400px;
  overflow-y: auto;
  padding: 16px;
  background: #f8fafc; /* 更柔和的背景色 */
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  border: 1px solid #e2e8f0; /* 添加边框 */
}

.message {
  padding: 12px 16px;
  border-radius: 12px;
  max-width: 85%;
  word-wrap: break-word;
  line-height: 1.5;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message.user {
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  color: white;
  align-self: flex-end;
  border-bottom-right-radius: 4px;
}

.message.ai {
  background: white;
  color: #2d3748;
  align-self: flex-start;
  border: 1px solid #e4e7ed;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.message.error {
  background: #fff5f5;
  color: #e53e3e;
  align-self: center;
  width: 90%;
  text-align: center;
  border: 1px solid #fed7d7;
}

.chat-input {
  width: 100%;
  padding-top: 10px;
}

/* 打字动画 */
.typing-indicator {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background: #718096;
  border-radius: 50%;
  display: inline-block;
  animation: bounce 1.3s infinite ease;
}

.typing-indicator span:nth-child(1) { animation-delay: 0s; }
.typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
.typing-indicator span:nth-child(3) { animation-delay: 0.4s; }

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>