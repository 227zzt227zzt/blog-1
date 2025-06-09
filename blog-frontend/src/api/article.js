// 获取文章列表
import request from '@/utils/request'

export const getArticles = async (params) => {
  try {
    // 将参数转换为后端需要的格式
    const requestParams = {
      currentPage: params.page,  // 映射为后端需要的currentPage
      size: params.size          // 保持size不变
    }
    
    const response = await request.get('/articles', { params: requestParams })
    
    if (response.data && response.code === 200) {
      return {
        success: true,
        code: 200,
        data: response.data,  // 注意这里改为response.data.data
        message: '获取文章列表成功'
      }
    } else {
      return {
        success: false,
        message: response.data?.message || '获取文章列表失败'
      }
    }
  } catch (error) {
    console.error('获取文章列表失败:', error)
    return {
      success: false,
      message: error.response?.data?.message || '获取文章列表失败'
    }
  }
}
  
  // 获取文章详情
  export const getArticleById = async (id) => {
    try {
      const response = await request.get(`/articles/${id}`)
      if (response.data && response.code === 200) {
        return {
          success: true,
          code: 200,
          data: response.data,
          message: '获取文章详情成功'
        }
      } else {
        return {
          success: false,
          message: response.data.message || '获取文章详情失败'
        }
      }
    } catch (error) {
      console.error('获取文章详情失败:', error)
      return {
        success: false,
        message: error.response?.data?.message || '获取文章详情失败'
      }
    }
  }
  
  // 删除文章
  export const deleteArticle = async (id) => {
    try {
      const response = await request.delete(`/articles/${id}`)
      if ( response.code === 200) {
        return {
          success: true,
          code: 200,
          message: '删除文章成功'
        }
      } else {
        return {
          success: false,
          message: response.message || '删除文章失败'
        }
      }
    } catch (error) {
      console.error('删除文章失败:', error)
      return {
        success: false,
        message: error.response?.data?.message || '删除文章失败'
      }
    }
  }
  
  // 上传文章封面图
  export const uploadArticleCover = async (formData) => {
    try {
      const response = await request.post(`/articles/uploadCover`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      if (response.data && response.code === 200) {
        return {
          success: true,
          code: 200,
          data: response.data,
          message: '封面图上传成功'
        }
      } else {
        return {
          success: false,
          message: response.message || '封面图上传失败'
        }
      }
    } catch (error) {
      console.error('封面图上传失败:', error)
      return {
        success: false,
        message: error.response?.data?.message || '封面图上传失败'
      }
    }
  }
  
  // 创建文章
  export const createArticle = async (articleData) => {
    try {
      const response = await request.post('/articles', articleData)
      if (response.data && response.code === 200) {
        return {
          success: true,
          code: 200,
          message: '创建文章成功'
        }
      } else {
        return {
          success: false,
          message: response.message || '创建文章失败'
        }
      }
    } catch (error) {
      console.error('创建文章失败:', error)
      return {
        success: false,
        message: error.response?.data?.message || '创建文章失败'
      }
    }
  }
  
  // 更新文章
  export const updateArticle = async (id, articleData) => {
    try {
      const response = await request.put('/articles', { ...articleData, id })
      if (response.data && response.code === 200) {
        return {
          success: true,
          code: 200,
          message: '更新文章成功'
        }
      } else {
        return {
          success: false,
          message: response.message || '更新文章失败'
        }
      }
    } catch (error) {
      console.error('更新文章失败:', error)
      return {
        success: false,
        message: error.response?.data?.message || '更新文章失败'
      }
    }
  }
  