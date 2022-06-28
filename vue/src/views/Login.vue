<template>
  <div style="width: 100%; height: 100vh;">
    <div>
        <!-- <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="#0099ff" fill-opacity="1" d="M0,192L48,197.3C96,203,192,213,288,229.3C384,245,480,267,576,250.7C672,235,768,181,864,181.3C960,181,1056,235,1152,234.7C1248,235,1344,181,1392,154.7L1440,128L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg> -->
        <!-- <div class="user">
            <i class="fa fa-user"></i>
            <div class="head">account login</div>
        </div> -->
        <div class="container">
            <div class="box login" ref="doLogin">
                <div class="form-content">
                    <div class="avtar">
                        <div class="pic"><img src="@/assets/img/1.jpg" alt=""></div>
                    </div>
                    <h1>Welcome back</h1>
                    <el-form class="form" ref="loginForm" :model="form" :rules="rules">
                        <el-form-item prop="username">
                            <i class="fa fa-user-o"></i>
                            <input type="text" placeholder="username" v-model="form.username">
                        </el-form-item>
                        <el-form-item prop="password">
                            <i class="fa fa-key"></i>
                            <input type="password" placeholder="password" v-model="form.password">
                        </el-form-item>
                        <el-form-item>
                            <div style="display: flex">
                                <el-input prefix-icon="el-icon-key" placeholder="验证码" v-model="form.validCode" style="width: 45%;margin-left:35px">
                                </el-input>
                                <valid-code @input="createValidCode" />
                            </div>
                        </el-form-item>
                        <div class="btn">
                            <button type="button" @click="login">login</button>
                        </div>
                    </el-form>
                    <p class="btn-something">
                        Don't have an account ? <span class="signupbtn" @click="changeSignUp">signup</span>
                    </p>
                </div>
            </div>
            <div class="box signup" ref="doSignUp">
                <div class="form-content">
                    <div class="avtar">
                        <div class="pic"><img src="@/assets/img/2.jpg" alt=""></div>
                    </div>
                    <h1>Let's get started</h1>
                    <el-form class="form" ref="SignUpForm" :model="form" :rules="rules">
                        <el-form-item prop="username">
                            <i class="fa fa-user-o"></i>
                            <input type="text" placeholder="username" v-model="form.username">
                        </el-form-item>
                        <el-form-item prop="password">
                            <i class="fa fa-key"></i>
                            <input type="password" placeholder="password" v-model="form.password">
                        </el-form-item>
                        <el-form-item prop="passwordAgain">
                            <i class="fa fa-key"></i>
                            <input type="password" placeholder="write password again" v-model="form.passwordAgain">
                        </el-form-item>
                        <el-form-item>
                            <!-- <div style="display: flex"> -->
                                <el-input prefix-icon="el-icon-key" placeholder="identifying code" v-model="form.validCode" style="width: 50%; margin-right:300px">
                                </el-input>
                                <ValidCode @input="createValidCode"/>
                            <!-- </div> -->
                        </el-form-item>
                        <div class="btn">
                            <button type="button" @click="signUp">signup</button>
                        </div>
                    </el-form>
                    <p class="btn-something">
                        Already have an account ? <span class="loginbtn" @click="changeLogin">login</span>
                    </p>
                </div>
            </div>
        </div>
    </div>
    
  </div>
</template>

<script>
import request from '@/utils/request'
import ValidCode from '@/components/ValidCode'
export default {
    name: "Login",
    components: {
        ValidCode
    },
    created(){
        sessionStorage.removeItem("userInfo")
    },
    data(){
        return{
            validCode: '',
            form: {},
            rules: {
                username: [
                    { required: true, message: 'Please input username', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: 'Please input password', trigger: 'blur' },
                ],
                passwordAgain: [
                    { required: true, message: 'Please check password again', trigger: 'blur' },
                ]
            }
        }
    },
    methods:{
        createValidCode(data){
            this.validCode = data
        },
        login(){
            this.$refs['loginForm'].validate((valid) => {
                if(valid){
                    if(!this.form.validCode){
                        this.$message.error("请填写验证码")
                        return
                    }
                    if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()){
                        this.$message.error("验证码填写错误")
                        return
                    }
                    request.post('/userservice/user/login', this.form).then(res => {
                        if(res.code == 20000){
                            this.$message({
                                type: 'success',
                                message: '登陆成功'
                            })
                            //console.log(res)
                            sessionStorage.setItem("userInfo", JSON.stringify(res.data.items));
                            this.$router.push({
                                path: "/",
                            })  // 登陆成功后页面跳转
                        }else{
                            this.$message({
                                type: 'error',
                                message: res.message
                            })
                        }
                    })
                }
            })
        },
        signUp(){
            console.log(this.form)
            this.$refs['SignUpForm'].validate((valid) => {
                if(valid){
                    if(this.form.password == this.form.passwordAgain){
                        request.post('/userservice/user/register', this.form).then(res => {
                            if(res.code == 20000){
                                this.$message({
                                    type: 'success',
                                    message: "注册成功"
                                })
                                this.$router.push("/")
                            }else{
                                this.$message({
                                    type: 'error',
                                    message: res.message
                                })
                            }
                        })
                    }else{
                        this.$message({
                            type: 'error',
                            message: "两次输入的密码不一致或者用户名已经存在"
                        })
                    }
                }else{
                    this.$message({
                        type: 'error',
                        message: "请按需填好全部数据再进行注册操作"
                    })
                }
            })
            
        },
        changeSignUp(){
            this.$refs.doLogin.style.transform = "rotateY(180deg)"
            this.$refs.doSignUp.style.transform = "rotateY(0deg)";
        },
        changeLogin(){
            this.$refs.doSignUp.style.transform = "rotateY(180deg)"
            this.$refs.doLogin.style.transform = "rotateY(0deg)";
        }
    }
}
</script>

<style scoped src="../assets/font-awesome-4.7.0/css/font-awesome.min.css"></style>
<style scoped src="../assets/css/index.css"></style>