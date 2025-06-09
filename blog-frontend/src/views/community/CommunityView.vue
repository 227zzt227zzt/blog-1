<template>
  <div class="comment-page">
    <h2>社区评论区</h2>

    <el-form :model="form" class="comment-form" @submit.prevent="submitComment">
      <el-form-item>
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="3"
          placeholder="说点什么吧..."
          :disabled="!isLoggedIn"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="submitComment"
          :disabled="!isLoggedIn || loading"
          :loading="loading"
        >
          发表评论
        </el-button>
        <span v-if="!isLoggedIn" class="login-tip">
          请先<a href="/login">登录</a>后发表评论
        </span>
      </el-form-item>
    </el-form>

    <el-divider />

    <div class="comment-list">
      <div v-if="loadingComments" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>

      <div v-else-if="comments.length === 0" class="empty">
        <el-empty description="暂无评论，快来抢沙发吧！" />
      </div>

      <div v-else>
        <el-card
          v-for="(item, idx) in comments"
          :key="item.id || idx"
          class="comment-item"
          shadow="hover"
        >
          <div class="comment-header">
            <el-avatar :size="36" :src="item.avatar || defaultAvatar" />
            <div class="user-info">
              <span class="comment-username">{{
                item.username || "匿名用户"
              }}</span>
              <el-tag v-if="item.isAuthor" size="small" type="success"
                >作者</el-tag
              >
            </div>
            <span class="comment-time">{{ formatTime(item.createTime) }}</span>
          </div>

          <div class="comment-content">{{ item.content }}</div>

          <div class="comment-footer">
            <el-button
              v-if="isLoggedIn"
              size="small"
              type="text"
              @click="likeComment(item)"
            >
              <el-icon><Star /></el-icon> 点赞 ({{ item.likes || 0 }})
            </el-button>
          </div>
        </el-card>

        <div class="pagination">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            layout="prev, pager, next"
            @current-change="fetchComments"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { Star } from "@element-plus/icons-vue";
import { saveComment, getAllComments } from "@/api/comment";
import { useUserStore } from "@/store/user";
import dayjs from "dayjs";

// 用户状态管理
const userStore = useUserStore();
const isLoggedIn = computed(() => userStore.isLoggedIn);
const currentUser = computed(() => userStore.user);

// 默认头像
const defaultAvatar = ref(
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
);

// 表单数据
const form = ref({
  content: "",
});

// 评论列表
const comments = ref([]);
const loading = ref(false);
const loadingComments = ref(false);

// 分页信息
const pagination = ref({
  current: 1,
  size: 10,
  total: 0,
});

// 假设从路由获取文章ID
const articleId = ref(123); // 实际应从路由参数获取

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format("YYYY-MM-DD HH:mm");
};

// 获取评论列表
const fetchComments = async () => {
  try {
    loadingComments.value = true;
    const response = await getAllComments({
      articleId: articleId.value,
      page: pagination.value.current,
      size: pagination.value.size,
    });

    if (response.code === 200) {
      comments.value = response.data.records || [];
      pagination.value.total = response.data.total || 0;
    } else {
      ElMessage.error("获取评论失败: " + response.message);
    }
  } catch (error) {
    console.error("获取评论错误:", error);
    ElMessage.error("获取评论失败，请稍后重试");
  } finally {
    loadingComments.value = false;
  }
};

// 提交评论
const submitComment = async () => {
  if (!form.value.content.trim()) {
    ElMessage.warning("评论内容不能为空！");
    return;
  }

  if (!isLoggedIn.value) {
    ElMessage.warning("请先登录后再评论");
    return;
  }

  try {
    loading.value = true;

    const commentData = {
      articleId: articleId.value,
      userId: currentUser.value.id,
      content: form.value.content,
      parentId: 0, // 一级评论
      status: "PUBLISHED", // 默认发布状态
    };

    const response = await saveComment(commentData);

    if (response.code === 200) {
      ElMessage.success("评论发表成功！");
      form.value.content = "";

      // 添加新评论到列表顶部
      const newComment = {
        id: response.data.id,
        username: currentUser.value.username,
        avatar: currentUser.value.avatar,
        content: commentData.content,
        createTime: new Date().toISOString(),
        likes: 0,
        isAuthor: true, // 标记为当前用户
      };

      comments.value.unshift(newComment);
      pagination.value.total += 1;
    } else {
      ElMessage.error("发表评论失败: " + response.message);
    }
  } catch (error) {
    console.error("评论提交错误:", error);
    ElMessage.error("评论发表失败，请稍后重试");
  } finally {
    loading.value = false;
  }
};

// 点赞评论
const likeComment = (comment) => {
  console.log("点赞评论:", comment.id);
  // 实际应调用点赞API
  ElMessage.success("点赞成功！");
  comment.likes = (comment.likes || 0) + 1;
};

// 初始化获取评论
onMounted(() => {
  fetchComments();
});
</script>

<style scoped>
.comment-page {
  max-width: 800px;
  margin: 40px auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(110, 142, 251, 0.08);
  padding: 32px 24px 40px 24px;
}

.comment-form {
  margin-bottom: 24px;
}

.login-tip {
  margin-left: 15px;
  color: #666;
  font-size: 14px;
}

.login-tip a {
  color: #6e8efb;
  text-decoration: none;
  margin: 0 3px;
}

.login-tip a:hover {
  text-decoration: underline;
}

.comment-list {
  margin-top: 24px;
}

.comment-item {
  margin-bottom: 18px;
  transition: all 0.3s;
}

.comment-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-username {
  font-weight: bold;
  color: #6e8efb;
}

.comment-time {
  color: #aaa;
  font-size: 13px;
  margin-left: auto;
}

.comment-content {
  font-size: 16px;
  color: #333;
  line-height: 1.7;
  margin-bottom: 10px;
  padding-left: 48px; /* 头像宽度 + 间隙 */
}

.comment-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 10px;
  border-top: 1px dashed #eee;
  margin-top: 10px;
}

.empty {
  color: #bbb;
  text-align: center;
  margin: 32px 0;
}

.loading-container {
  padding: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-card {
  border-radius: 10px;
}
</style>