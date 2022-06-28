<template>
  <div class="home" style="padding:15px">
    <div style="margin: 5px 0">
      <el-button type="primary" plain @click="add" v-if="user.role == 1">新增</el-button>
      <el-popconfirm
        confirm-button-text="OK"
        cancel-button-text="No, Thanks"
        @confirm="deleteBatch"
        title="Are you sure to delete this?"
      >
        <template #reference>
          <el-button type="danger" plain>批量删除</el-button>
        </template>
      </el-popconfirm>
      
    </div>

    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" clearable style="width: 20%; padding-right: 10px"/>
      <el-button type="primary" @click="load()" plain>搜索</el-button>
    </div>

    <el-table
        :data="tableData"
        style="width: 100%"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
    >
      <!-- sortable -> 设置排序 -->
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="id" label="ID" sortable width="190px"/>
      <el-table-column prop="name" label="名称" width="190px"/>
      <el-table-column prop="price" label="价格" sortable width="190px"/>
      <el-table-column prop="author" label="作者" width="190px"/>
      <el-table-column prop="createTime" label="发布时间" sortable width="190px"/>
      <el-table-column label="封面" sortable width="190px">

        <template #default="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.cover"
            :preview-src-list="[scope.row.cover]"
          />
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="200px">
        <template #default="scope">
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
          <el-form-item label="名称">
            <el-input v-model="form.name" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="封面">
            <el-upload ref="upload" action="/api/files/upload" :on-success="filesUploadSuccess">
              <el-button type="primary">Click to upload</el-button>
              <template #tip>
                <div class="el-upload__tip">
                  jpg/png files with a size less than 500KB.
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
export default {
  name: 'Book',
  components: {

  },
  data() {
    return {
      user: {},
      search: '',
      currentPage: 1,
      pageSize: 5,
      total: 0,
      form: {},
      dialogVisible: false,
      tableData: [],
      ids: []
    }
  },
  created(){
    this.load()
    let user = sessionStorage.getItem("userInfo") || '{}'
    this.user = JSON.parse(user);
    request.get('/userservice/user/find/' + this.user).then(res => {
      if(res.code == 20000){
        this.user = res.item
      }
    })
  },
  methods:{
    handleSelectionChange(val){
      // 将选中的书籍的id加到this.ids这个集合
      this.ids = val.map(v => v.id)
    },
    deleteBatch(){
      if(!this.ids.length){
        this.$message.warning("未选择书籍")
        return
      }
      request.post('/userservice/book/deleteBatch', this.ids).then(res => {
        if(res.code == 20000){  
          this.$message.success("批量删除成功")
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    filesUploadSuccess(res){
      //console.log(res)
      this.form.cover = res.data.url
      
    },
    // 注意：post请求可以直接把对象放在第二个参数里，而get请求需要加param
    load(){
      request.get("/userservice/book/findByPageNum",{
        params:{
          currentPage: this.currentPage,
          limit: this.pageSize,
          search: this.search
        }
      }).then(res => {
        // console.log(res)
        this.total = res.data.total
        this.tableData = res.data.row
      })
    },
    add(){
      this.form = {}
      this.dialogVisible = true
      //this.$refs['upload'].clearFiles()
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
      request.delete("/userservice/book/delete/" + row.id).then(res => {
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
      this.$refs['upload'].clearFiles()
      //console.log(this.form)
      // 需要判断是 更新保存 还是 新增保存
      if(this.form.id){
          request.put("/userservice/book/update", this.form).then(res => {
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
        request.post("/userservice/book/addBook", this.form).then(res => {
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