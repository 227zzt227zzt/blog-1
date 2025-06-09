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
      'Accept': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    },
    responseType: 'stream',
    signal
  });
}

/**
 * 使用ChatModel直接发送消息
 * @param {string} message - 发送给AI的消息
 * @param {AbortSignal} signal - 可选的AbortSignal用于取消请求
 * @returns {Promise<Response>} - 返回fetch响应
 */
export function chatWithModel(message, signal = null) {
  return request({
    url: '/ai/chat2',
    method: 'GET',
    params: { message },
    headers: {
      'Accept': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    },
    responseType: 'stream',
    signal
  });
}

/**
 * 处理流式响应
 * @param {Response} response - fetch响应对象
 * @returns {AsyncGenerator<string>} - 生成器函数，用于处理流式响应
 */
export async function* handleStreamResponse(response) {
  if (!response.ok || !response.body) {
    throw new Error('AI服务响应异常');
  }

  const reader = response.body.getReader();
  const decoder = new TextDecoder('utf-8');

  try {
    while (true) {
      const { done, value } = await reader.read();
      
      if (done) {
        break;
      }
      
      const chunk = decoder.decode(value, { stream: true });
      yield chunk;
    }
  } finally {
    reader.releaseLock();
  }
}