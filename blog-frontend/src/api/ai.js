import  request  from '@/utils/request.js'
export const getAiResponse = async (message) => {
  try {
    console.log('message:', message)
    const response = await request.get('/ai/chat', { params: { message } })
    if (response.data && response.code === 200) {
      return {
        success: true,
        code: 200,
        data: response.data,
        message: 'AI响应成功'
      }
    } else {
      return {
        success: false,
        message: response.data?.message || 'AI响应失败'
      }
    }
  } catch (error) {
    console.error('获取AI响应失败:', error)
    return {
      success: false,
      message: error.response?.data?.message || 'AI响应失败'
    }
  }
}