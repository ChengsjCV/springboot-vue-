import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/user',
    children: [
      {
        path: '/user',
        name: 'User',
        component: () => import("@/views/User"),
      },
      {
        path: '/person',
        name: 'Person',
        component: () => import("@/views/Person")
      },
      {
        path: '/book',
        name: 'Book',
        component: () => import("@/views/Book")
      },
      {
        path: '/news',
        name: 'News',
        component: () => import("@/views/News")
      },
      {
        path: '/category',
        name: 'Category',
        component: () => import("@/views/Category")
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
  

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
