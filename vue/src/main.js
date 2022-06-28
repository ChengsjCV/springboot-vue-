import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 引入element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 把element-plus中的内容由英文转换为中文
import 'dayjs/locale/zh-cn'
// 引入国际化时间
import locale from 'element-plus/lib/locale/lang/zh-cn'

import '@/assets/css/global.css'
// 使用element-plus的icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


const app = createApp(App)
// 配置全局加载element-plus-ui的icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(store)
app.use(router)
app.use(ElementPlus, {locale})

app.mount('#app')

//操作cookie
// router.beforeEach((to, from, next) => {
//     if (to.meta.islogin) { //说明登录才能进
//         //判断是否登录
//         let name = cookie.get('name')
//         if (name) { //登录了才能进
//             next()
//         } else { //如果没有登录跳到登录页面
//             next({
//                 name: 'Login'
//             })
//         }
//     } else {
//         next()
//     }
// })
    