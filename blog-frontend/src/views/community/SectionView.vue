<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";

const articleId = ref(123); // 从路由参数获取
const comments = ref([]);
const newComment = ref("");
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0,
});

// 提交评论的表单数据
const commentForm = reactive({
  articleId: articleId.value,
  content: "",
  parentId: null, // 用于二级评论
  status: "PUBLISHED",
});

// 加载评论
const fetchComments = async () => {
  try {
    const response = await axios.get(`/comments/article/${articleId.value}`, {
      params: {
        page: pagination.page,
        size: pagination.size,
      },
    });

    if (response.data.code === 200) {
      comments.value = response.data.data.list;
      pagination.total = response.data.data.total;
    }
  } catch (error) {
    console.error("获取评论失败:", error);
  }
};

// 提交评论
const submitComment = async () => {
  if (!commentForm.content.trim()) return;

  try {
    const response = await axios.post("/comments", commentForm);

    if (response.data.code === 200) {
      // 清空表单并刷新评论
      commentForm.content = "";
      fetchComments();
    }
  } catch (error) {
    console.error("提交评论失败:", error);
  }
};

// 删除评论
const deleteComment = async (commentId) => {
  try {
    await axios.delete(`/comments/${commentId}`);
    fetchComments();
  } catch (error) {
    console.error("删除评论失败:", error);
  }
};

// 回复评论
const replyTo = (comment) => {
  commentForm.parentId = comment.id;
  newComment.value = `@${comment.username} `;
};

onMounted(fetchComments);
</script>

<template>
  <div class="comment-section">
    <h3>评论 ({{ pagination.total }})</h3>

    <!-- 评论表单 -->
    <div class="comment-form">
      <textarea
        v-model="commentForm.content"
        placeholder="写下您的评论..."
      ></textarea>
      <button @click="submitComment">提交</button>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <span class="username">{{ comment.username }}</span>
          <span class="time">{{
            new Date(comment.createdAt).toLocaleString()
          }}</span>
        </div>
        <div class="comment-content">{{ comment.content }}</div>
        <div class="comment-actions">
          <button @click="replyTo(comment)">回复</button>
          <button @click="deleteComment(comment.id)">删除</button>
        </div>

        <!-- 二级评论 -->
        <div v-if="comment.replies" class="replies">
          <div
            v-for="reply in comment.replies"
            :key="reply.id"
            class="reply-item"
          >
            <span class="reply-username">@{{ reply.username }}</span>
            <span>{{ reply.content }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <div class="pagination">
      <button
        @click="
          pagination.page--;
          fetchComments();
        "
        :disabled="pagination.page <= 1"
      >
        上一页
      </button>
      <span>第 {{ pagination.page }} 页</span>
      <button
        @click="
          pagination.page++;
          fetchComments();
        "
        :disabled="pagination.page * pagination.size >= pagination.total"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<style scoped>
.comment-section {
  margin-top: 2rem;
  border-top: 1px solid #eee;
  padding-top: 1rem;
}

.comment-form {
  margin-bottom: 2rem;
}

.comment-form textarea {
  width: 100%;
  height: 80px;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
}

.comment-item {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 0.9rem;
  color: #666;
}

.comment-content {
  margin-bottom: 10px;
}

.comment-actions button {
  margin-right: 10px;
  background: none;
  border: none;
  color: #1890ff;
  cursor: pointer;
}

.replies {
  margin-top: 10px;
  padding-left: 20px;
  border-left: 2px solid #eee;
}

.reply-item {
  padding: 5px 0;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 10px;
}
</style>