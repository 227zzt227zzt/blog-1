import {
  defineStore
} from 'pinia'
import {
  ref
} from 'vue'
import CryptoJS from 'crypto-js'

// 加密密钥，建议放在环境变量中
const SECRET_KEY = 'your-secret-key'

// 加密函数
const encrypt = (data) => {
  return CryptoJS.AES.encrypt(JSON.stringify(data), SECRET_KEY).toString()
}

// 解密函数
const decrypt = (encryptedData) => {
  try {
    const bytes = CryptoJS.AES.decrypt(encryptedData, SECRET_KEY)
    return JSON.parse(bytes.toString(CryptoJS.enc.Utf8))
  } catch (e) {
    console.error('解密失败:', e)
    return null
  }
}

export const useUserStore = defineStore('user', () => {
  const token = ref('')
  const userInfo = ref(null)

  // 初始化时从 sessionStorage 获取数据
  const initFromStorage = () => {
    const storedUserInfo = sessionStorage.getItem('userInfo')
    // const storedRefreshToken = sessionStorage.getItem('refreshToken')

    if (storedUserInfo) {
      try {
        const decryptedData = decrypt(storedUserInfo)
        if (decryptedData && decryptedData.expires > Date.now()) {
          userInfo.value = decryptedData.data
          token.value = decryptedData.token
        } else {
          // 数据已过期，清除存储
          clearUserInfo()
        }
      } catch (e) {
        console.error('初始化用户数据失败:', e)
        clearUserInfo()
      }
    }
  }

  // 初始化
  initFromStorage()

  const setUserInfo = (userData) => {
    // 设置 token
    token.value = userData.token

    // 设置用户信息，添加过期时间（例如24小时）
    const userDataWithExpiry = {
      data: userData.user,
      token: userData.token,
      expires: Date.now() + 24 * 60 * 60 * 1000
    }

    // 加密后存储
    const encryptedData = encrypt(userDataWithExpiry)
    sessionStorage.setItem('userInfo', encryptedData)

    // 存储refresh token
    if (userData.refreshToken) {
      sessionStorage.setItem('refreshToken', userData.refreshToken)
    }

    userInfo.value = userData.user
    
  }

  const clearUserInfo = () => {
    token.value = ''
    userInfo.value = null
    sessionStorage.removeItem('userInfo')
    sessionStorage.removeItem('refreshToken')
  }

  // 检查用户信息是否有效
  const isUserInfoValid = () => {
    return userInfo.value !== null && token.value !== ''
  }

  return {
    token,
    userInfo,
    setUserInfo,
    clearUserInfo,
    isUserInfoValid
  }
})