<template>
  <div class="home" style="padding:15px">
    <div style="margin: 5px 0">
      <el-button type="primary" plain @click="add">新增</el-button>

      <el-button type="success" plain>导入</el-button>
      <el-button type="danger" plain>导出</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" clearable style="width: 20%; padding-right: 10px"/>
      <el-button type="primary" @click="load()" plain>搜索</el-button>
    </div>

    <el-table
        :data="tableData"
        style="width: 100%"
        :row-class-name="tableRowClassName"
    >
      <!-- sortable -> 设置排序 -->
      <el-table-column prop="id" label="ID" sortable width="220px"/>
      <el-table-column prop="title" label="标题" width="220px"/>
      <el-table-column prop="author" label="作者" width="220px"/>
      <el-table-column prop="createTime" label="发布时间" sortable width="250px"/>
      <el-table-column fixed="right" label="操作" width="200px">
        <template #default="scope">
          <el-button icon="Search" style="margin: 0 10px" circle @click="details(scope.row)"/>
          <el-button type="primary" icon="Edit" style="margin: 0 10px" circle @click="handleEdit(scope.row)"/>
          <!-- 气泡弹出框 -->
          <el-popconfirm title="您确定删除本条记录吗?" @confirm="doConfirm(scope.row)" @cancel="doCancel">
            <template #reference>
              <el-button type="danger" icon="Delete" circle />
            </template>
          </el-popconfirm>
        </template>

      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">

      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="[1, 5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
      <!-- 这个弹出框既可以用于新增用户，也可以用户修改用户 -->
      <el-dialog
        v-model="dialogVisible"
        title="提示"
        width="80%"
        :before-close="handleClose">

        <el-form :model="form" label-width="120px">
          <el-form-item label="标题">
            <el-input v-model="form.title" style="width: 50%"/>
          </el-form-item>
          <el-form-item>
            <div id="div1">
            </div>
          </el-form-item>
          <!-- <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="内容">
            <el-input v-model="form.content" style="width: 80%"/>
          </el-form-item> -->
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确定</el-button>
          </span>
        </template>
      </el-dialog>

      <el-dialog
        v-model="visible"
        title="详情"
        width="80%"
        >
        <el-card>
          <div v-html="detail.content" style="min-height: 100px"></div>
        </el-card>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import E from 'wangeditor'

// 设置全局变量editor
let editor;

export default {
  name: 'News',
  components: {

  },
  data() {
    return {
      visible: false,
      search: '',
      currentPage: 1,
      pageSize: 5,
      total: 0,
      form: {},
      dialogVisible: false,
      tableData: [],
      detail: {}
    }
  },
  created(){
    this.load()
  },
  methods:{
    details(row){
      this.detail = JSON.parse(JSON.stringify(row))
      this.visible = true
    },
    filesUploadSuccess(res){
      //console.log(res)
      this.form.cover = res.data.url
      
    },
    // 注意：post请求可以直接把对象放在第二个参数里，而get请求需要加param
    load(){
      request.get("/api/userservice/news/findByPageNum",{
        params:{
          currentPage: this.currentPage,
          limit: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.total = res.data.total
        this.tableData = res.data.row
      })
    },
    add(){
      this.form = {}
      this.dialogVisible = true
      //this.$refs['upload'].clearFiles()
      // 富文本编辑器(解决异步加载过程中div1对象不存在的问题)
      this.$nextTick(() => {
        // 如果editor不存在，则新建一个editor对象，否则就将其txt中的文本清除
        if(!editor){
          editor = new E("#div1")
          // 设置上传图片的接口地址
          editor.config.uploadImgServer = "http://localhost:9090/files/edit/upload"
          editor.config.uploadFileName = "file" // 设置上传参数名称（需要跟后端的参数名称一致）
          editor.create()
        }else{
          editor.txt.html("")
        }
        
      })
      
    },
    handle(){

    },
    // 改变页码大小的时候执行（比如将一页10个改成一页20个）
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    // 改变当前页的时候执行
    handleCurrentChange(currentPage){
      this.currentPage = currentPage
      this.load()
    },
    // 修改某一行的数据
    handleEdit(row){
      // 深拷贝，不影响原表格数据(这一步很重要)
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        if(!editor){
          editor = new E("#div1")
          editor.txt.html(row.content)
          // 设置上传图片的接口地址
          editor.config.uploadImgServer = "http://localhost:9090/files/edit/upload"
          editor.config.uploadFileName = "file" // 设置上传参数名称（需要跟后端的参数名称一致）
          editor.create()
        }else{
          editor.txt.html(row.content)
        }
      })
    },
    doConfirm(row){
      console.log(row)
      request.delete("/userservice/news/delete/" + row.id).then(res => {
        this.$message({
          type: 'success',
          message: '删除成功'
        })
      })
      this.load()
    },
    doCancel(){
      this.load()
    },
    save(){
      this.form.content = editor.txt.html()
      //console.log(this.form)
      // 需要判断是 更新保存 还是 新增保存
      if(this.form.id){
          request.put("/userservice/news/update", this.form).then(res => {
            if(res.code == 20000){
              this.$message({
                type: 'success',
                message: '更新成功'
              })
            }else{
              this.$message({
                type: 'error',
                message: res.message
              })
            }
            this.load()
          })
      }else{
        // 登陆时利用sessionStorage存储了user对象，这里取出该对象，并且获得其nickName
        let userStr = sessionStorage.getItem("userInfo") || "{}"
        let user = JSON.parse(userStr)
        this.form.author = user.nickName
        request.post("/userservice/news/addNews", this.form).then(res => {
          if(res.code == 20000){
            this.$message({
              type: 'success',
              message: '新增成功'
            })
          }else{
            this.$message({
              type: 'error',
              message: res.message
            })
          }
          this.load()
        })
      }
      // 新增/修改完数据后,刷新表格数据,关闭弹出框
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.el-table, .warning-row {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
}
.el-table, .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-9);
}
</style>