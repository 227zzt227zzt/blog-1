import request from '@/utils/request'

export const login = async (loginData) => {
    try {
      return await request.post(`/users/login`, {
        ...loginData,
      })  
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '请求出错，请稍后再试' 
      }
    }
  }
  
  export const register = async (registerData) => {
    try {
      const response = await request.post(`/users/register`, registerData)
      if (response.data && response.data.code === 200) {
        return { success: true, message: '注册成功' }
      } else {
        return { success: false, message: response.data.message || '注册失败' }
      }
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '请求出错，请稍后再试' 
      }
    }
  }
  
  export const getCaptcha1 = async () => {
    try {
        
        const response = await request.get(`/captcha/generate`)
        console.log('获取验证码响应:', response)
        // 如果响应数据包含 code 字段且值为 200，表示成功
        if (response.data && response.code === 200) {
            return { 
                success: true, 
                data: response.data, 
                sessionId: response.sessionId,
                message: '验证码获取成功' 
            }
        } else {
            return { 
                success: false, 
                message: response.data.message || '验证码获取失败' 
            }
        }
    } catch (error) {
      return { 
        success: false, 
        message: '获取验证码失败，请稍后再试' 
      }
    }
  }

// 更新个人信息
export const updateProfile = async (updateProfile) => {
  try {
    const response = await request.put(`/users/updateUser`, updateProfile)
    console.log('更新个人信息响应:', response)
    if (response.data && response.code === 200) {
      return { success: true,
              message: '更改成功',
              data: response.data,
              code: response.code 
             }
    } else {
      return { success: false, message: response.data.message || '更改失败22' }
    }
  } catch (error) {
    return { 
      success: false, 
      message: error.response?.data?.message || '请求出错，请稍后再试22' 
    }
  }
}

// 处理头像上传
export const uploadAvatar = async (formData) => {
  try {
    const response = await request.post(`/users/uploadAvatar`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    if (response.data && response.code === 200) {
      return { 
        success: true,
        code: 200,
        data: response.data,
        message: '头像上传成功'
      }
    } else {
      return { 
        success: false, 
        message: response.message || '头像上传失败' 
      }
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    return { 
      success: false, 
      message: error.response?.data?.message || '头像上传失败' 
    }
  }
}