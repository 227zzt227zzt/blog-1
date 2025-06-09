import request from '@/utils/request'

// 保存评论
export function saveComment(commentDTO) {
  return request({
    url: '/comments/save',
    method: 'post',
    data: commentDTO
  })
}

// 获取所有评论
export function getAllComments() {
  return request.get('/comments')
}

// 获取文章评论列表
export const getArticleComments = async (params) => {
  try {
    const requestParams = {
      currentPage: params.page,
      size: params.size,
      articleId: params.articleId
    }
    const response = await request.get('/comments/article', { params: requestParams })
    if (response.data && response.code === 200) {
      return {
        success: true,
        code: 200,
        data: response.data,
        message: '获取评论列表成功'
      }
    } else {
      return {
        success: false,
        message: response.data?.message || '获取评论列表失败'
      }
    }
  } catch (error) {
    console.error('获取文章评论失败:', error)
    return {
      success: false,
      message: error.response?.data?.message || '获取评论列表失败'
    }
  }
}

// 获取评论的回复列表
export const getCommentReplies = async (commentId) => {
  try {
    const response = await request.get(`/comments/replies/${commentId}`)
    if (response.data && response.code === 200) {
      return {
        success: true,
        code: 200,
        data: response.data,
        message: '获取回复列表成功'
      }
    } else {
      return {
        success: false,
        message: response.data?.message || '获取回复列表失败'
      }
    }
  } catch (error) {
    console.error('获取评论回复失败:', error)
    return {
      success: false,
      message: error.response?.data?.message || '获取回复列表失败'
    }
  }
}