import request from '@/utils/request'

/**
 * 发送消息到AI聊天并获取流式响应
 * @param {string} message - 发送给AI的消息
 * @param {AbortSignal} signal - 可选的AbortSignal用于取消请求
 * @returns {Promise<Response>} - 返回fetch响应
 */
export function chatWithAI(message, signal = null) {
  return request({
    url: '/ai/chatStream',
    method: 'GET',
    params: { message },
    headers: {
      'Accept': 'text/event-stream'
    },
    signal
  });
}