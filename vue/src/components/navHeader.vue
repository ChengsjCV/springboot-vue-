<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex">
    <div style="width: 200px; padding-left: 30px; font-weight: bold; color: dodgerblue">后台管理</div>
    <div style="flex: 1"></div>
    <div style="width: 100px;padding-top: 18px">
        <el-dropdown>
          <span class="el-dropdown-link">
            {{userInfo.nickName}}
            <el-icon class="el-icon--right">
              <arrow-down />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="signOut">退出登陆</el-dropdown-item>

            </el-dropdown-menu>
          </template>
        </el-dropdown>
    </div>
  </div>
</template>

<script>
//import { ArrowDown } from '@element-plus/icons-vue'
export default {
  name: "navHeader",
  components:{
    //ArrowDown
  },
  created(){
    let info = sessionStorage.getItem("userInfo")
    if(info != null){
      this.userInfo = JSON.parse(info)
    }
  },
  data(){
    return{
      userInfo: {}
    }
  },
  methods:{
    signOut(){
      sessionStorage.clear()
      this.$router.push({
        path: "/login",
      })
    }
  }
}
</script>

<style scoped>
.example-showcase, .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>