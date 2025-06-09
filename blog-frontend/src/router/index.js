import {
  createRouter,
  createWebHistory
} from 'vue-router'

const routes = [{
    path: '/',
    name: 'home',
    component: () => import('@/views/home/HomeView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/LoginView.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('@/views/register/RegisterView.vue')
  },

  {
    path: '/tags',
    name: 'tags',
    component: () => import('@/views/tags/AllTags.vue')
  },
  {
    path: '/community',
    name: 'community',
    component: () => import('@/views/community/CommunityView.vue')
  },
  {
    path: '/community',
    name: 'section',
    component: () => import('@/views/community/SectionView.vue')
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('@/views/mine/ProfileView.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/articles',
    name: 'articles',
    component: () => import('@/views/article/ArticlesView.vue')
  },
  {
    path: '/article/:id',
    name: 'article',
    component: () => import('@/views/article/ArticleContentView.vue')
  },
  {
    path: '/article/create',
    name: 'articleCreate',
    component: () => import('@/views/article/ArticleCreateView.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/article/edit/:id',
    name: 'articleEdit',
    component: () => import('@/views/article/ArticleEditView.vue'),
    meta: {
      requiresAuth: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(''),
  routes
})

export default router