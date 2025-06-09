import {
    createApp
} from 'vue'
import App from './App.vue'
import router from './router'
import {
    createPinia
} from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(router)
app.use(createPinia())
app.use(ElementPlus)

app.mount('#app')
console.log('Vue app is initializing...')
console.log('Base URL:', process.env.BASE_URL)
console.log('Environment:', process.env.NODE_ENV)
console.log('API URL:', process.env.VUE_APP_API_URL)
console.log('Vue app is initialized.')
// src/main.js 或入口文件
console.log('当前环境变量:',
    import.meta.env)
