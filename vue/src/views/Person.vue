<template>
  <div>
      <el-card style="width: 40%; margin: 10px">
          <el-form ref="form" :model="form" label-width="80px">
              <el-form-item label="用户名">
                  <el-input v-model="form.username" disabled></el-input>
              </el-form-item>
              <el-form-item label="昵称">
                  <el-input v-model="form.nickName"></el-input>
              </el-form-item>
              <el-form-item label="年龄">
                  <el-input v-model="form.age"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                  <el-input v-model="form.gender"></el-input>
              </el-form-item>
              <el-form-item label="地址">
                  <el-input v-model="form.address"></el-input>
              </el-form-item>
              <el-form-item label="密码">
                  <el-input v-model="form.password"></el-input>
              </el-form-item>
          </el-form>
          <div style="text-align: center">
              <el-button type="primary" @click="update">保存</el-button>
          </div>
      </el-card>
  </div>
</template>

<script>
export default {
    name: 'Person',
    data(){
        return{
            form: {}
        }
    },
    created(){
        let info = sessionStorage.getItem("userInfo")
        if(info != null){
            this.form = JSON.parse(info)
        }
    },
    methods: {
        update(){
            request.put("/userservice/user/update", this.form).then(res => {
                if(res.code == 20000){
                    this.$message({
                        type: 'success',
                        message: "更新成功"
                    })
                }else{
                    this.$message({
                        type: 'error',
                        message: res.message
                    })
                }
            })        
        }
    }
}
</script>

<style>

</style>