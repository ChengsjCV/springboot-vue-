<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/user" :route="{path: '/'}">用户管理</el-menu-item>
          <el-menu-item index="/book" :route="{path: '/book'}">书籍管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/news" :route="{path: '/news'}" v-if="user.role == 1">
          <el-icon><document /></el-icon>
          <span>新闻管理</span>
        </el-menu-item>
        <el-menu-item index="/category" :route="{path: '/category'}">
          <el-icon><document /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="3" disabled v-if="user.role == 2">
          <el-icon><setting /></el-icon>
          <span>导航三</span>
        </el-menu-item>
      </el-menu>
  </div>
</template>

<script>
import request from '@/utils/request'
export default {
  
  name: "Aside",
  components:{
    
  },
  // 根据页面路径设置默认高亮的菜单项
  data(){
    return{
      path: this.$route.path,
      user: {}
    }
  },
  created(){
    let user = sessionStorage.getItem("userInfo") || '{}'
    
    this.user = JSON.parse(user)
    // console.log(this.user)
    // 请求服务器，确认当前登陆用户的合法信息
    // this.$nextTick(() => {
    //   request.get('/userservice/user/find/' + this.user.id).then(res => {
    //     console.log(res)
    //   })
    // })
    request.get('/userservice/user/find/' + this.user.id).then(res => {
      if(res.code == 20000){
        this.user = res.data.item
      } 
    })
  },
}
</script>

<style scoped>

</style>