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
        stripe
        :row-class-name="tableRowClassName"
    >
      <!-- sortable -> 设置排序 -->
      <el-table-column prop="id" label="ID" sortable width="190px"/>
      <el-table-column prop="nickName" label="别名" width="190px"/>
      <el-table-column prop="age" label="年龄" sortable width="190px"/>
      <el-table-column prop="gender" label="性别" width="190px"/>

      <el-table-column prop="address" label="地址" width="190px"/>
      <el-table-column prop="role" label="角色" width="190px">
        <template #default="scope">
          <span v-if="scope.row.role == 1">管理员</span>
          <span v-else>普通用户</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="200px">
        <template #default="scope">
          <el-button type="success" plain @click="findBooks(scope.row)">查看图书列表</el-button>
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
        width="30%"
        :before-close="handleClose">

        <el-form :model="form" label-width="120px">
          <el-form-item label="username">
            <el-input v-model="form.username" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickName" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.gender" label="男">男</el-radio>
            <el-radio v-model="form.gender" label="女">女</el-radio>
            
          </el-form-item>
          <el-form-item label="地址">
            <el-input type="textarea" v-model="form.address" style="width: 80%"/>
          </el-form-item>
          
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确定</el-button>
          </span>
        </template>
      </el-dialog>

      <el-dialog v-model="bookDialogVisible" title="书籍信息">
        <el-table :data="bookInfo">
          <el-table-column property="name" label="Name" width="150" />
          <el-table-column property="price" label="Price" width="200" />
          <el-table-column property="author" label="Author" />
        </el-table>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
export default {
  name: 'User',
  components: {

  },
  data() {
    return {
      bookInfo: [],
      bookDialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 5,
      total: 0,
      form: {},
      dialogVisible: false,
      tableData: []
    }
  },
  created(){
    this.load()
  },
  methods:{
    findBooks(row){
      this.bookDialogVisible = true
      request.get("/userservice/book/find/" + row.id).then(res => {
        this.bookInfo = res.data.item
        //console.log(res)
        //console.log(this.bookInfo)
      })
      
    },
    // 注意：post请求可以直接把对象放在第二个参数里，而get请求需要加param
    load(){
      request.get("/userservice/user/findByPageNum",{
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
    },
    doConfirm(row){
      console.log(row)
      request.delete("/userservice/user/delete/" + row.id).then(res => {
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
      //console.log(this.form)
      // 需要判断是 更新保存 还是 新增保存
      if(this.form.id){
          request.put("/userservice/user/update", this.form).then(res => {
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
        request.post("/userservice/user/addUser", this.form).then(res => {
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