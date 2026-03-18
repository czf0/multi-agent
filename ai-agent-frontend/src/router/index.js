import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/love-app',
    name: 'loveApp',
    component: () => import('../views/LoveAppView.vue')
  },
  {
    path: '/manus-app',
    name: 'manusApp',
    component: () => import('../views/ManusAppView.vue')
  },
  {
    path: '/exam-app',
    name: 'examApp',
    component: () => import('../views/ExamAppView.vue')
  },
  {
    path: '/health-app',
    name: 'healthApp',
    component: () => import('../views/HealthAppView.vue')
  },
  // Admin routes
  {
    path: '/admin/llm-config',
    name: 'llmConfig',
    component: () => import('../views/admin/LlmConfigView.vue')
  },
  {
    path: '/admin/vector-db-config',
    name: 'vectorDbConfig',
    component: () => import('../views/admin/VectorDbConfigView.vue')
  },
  {
    path: '/admin/agent-binding',
    name: 'agentBinding',
    component: () => import('../views/admin/AgentBindingView.vue')
  },
  {
    path: '/admin/agent',
    name: 'agentConfig',
    component: () => import('../views/admin/AgentConfigView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router 