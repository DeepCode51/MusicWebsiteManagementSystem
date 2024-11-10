<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="small" class="handle-del mr10" @click="delAll"
          style="background-color: #de4307; border: 0px; font-size: 15px;">批量删除</el-button>
        <el-input v-model="select_word" size="small" placeholder="请输入用户名" class="handle-input mr10"></el-input>
        <el-button type="primary" size="small" @click="centerDialogVisible = true"
          style="background-color: #8bc24c; border: 0px; font-size: 15px;">添加新用户</el-button>
      </div>
      <el-table :data="data" border size="mini" style="width: 100%" ref="multipleTable" height="550px"
        @selection-change="handleSelectionChange" @filter-change="handleFilterChange">
        <el-table-column type="selection" width="40" align="center"></el-table-column>
        <el-table-column label="用户图片" width="102" align="center">
          <template slot-scope="scope">
            <img :src="getUrl(scope.row.avator)" alt="" style="width: 80px;" />
            <el-upload class="upload-demo" :action="uploadUrl(scope.row.id)" :show-file-list="false"
              :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
              <el-button size="mini">更新图片</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column label="用户名" prop="username" width="80" align="center"></el-table-column>
        <el-table-column label="密码" prop="password" width="80" align="center">
          <template slot-scope="scope">
            <div type="password">{{ scope.row.password }}</div>
          </template>
        </el-table-column>
        <el-table-column label="性别" width="70" align="center" prop="sex" :column-key="'sex'"
          :filters="[{ text: '男', value: '1' }, { text: '女', value: '0' }]" :filter-method="filterHandler"
          :filter-multiple="false">
          <template slot-scope="scope">
            <div>{{ changeSex(scope.row.sex) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="手机号" prop="phoneNum" width="120" align="center"></el-table-column>
        <el-table-column label="邮箱" prop="email" width="180" align="center"></el-table-column>
        <el-table-column label="生日" width="120" align="center" sortable prop="birth"
          :default-sort="{ prop: 'birth', order: 'ascending' }">
          <template slot-scope="scope">
            <div>{{ attachBirth(scope.row.birth) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="introduction" label="签名" align="center"></el-table-column>
        <el-table-column prop="permission" label="评论权限" align="center" width="100" :column-key="'permission'"
          :filters="[{ text: '允许', value: '1' }, { text: '不允许', value: '0' }]" :filter-method="filterHandler"
          :filter-multiple="false">
          <template slot-scope="scope">
            <el-switch class="switchStyle" @change="setPermission(scope.row)" active-color="orange"
              :value="scope.row.permission" :active-value="1" :inactive-value="0" inactive-color="#dadbdf"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="地区" width="80" align="center" :column-key="'location'"
          :filters="getfilterNameItem()" :filter-method="filterHandler" :filter-multiple="false"></el-table-column>
        <el-table-column label="收藏" width="80" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="getCollect(data[scope.$index].id)">收藏</el-button>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination @current-change="handleCurrentChange" background layout="total, prev, pager, next"
          :current-page="currentPage" :page-size="pageSize" :total="tableData.length">
        </el-pagination>
      </div>
    </div>

    <!--添加新用户-->
    <el-dialog title="添加新用户" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="70px"
        class="demo-ruleForm">
        <el-form-item label="用户名" prop="username" size="mini">
          <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" size="mini">
          <el-input type="password" placeholder="密码" v-model="registerForm.password"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNum" size="mini">
          <el-input placeholder="手机号" v-model="registerForm.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" size="mini">
          <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="生日" prop="birth" size="mini">
          <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.birth" style="width: 100%;"
            :picker-options="expireTimeOPtion"></el-date-picker>
        </el-form-item>
        <el-form-item label="签名" prop="introduction" size="mini">
          <el-input type="textarea" placeholder="签名" v-model="registerForm.introduction"></el-input>
        </el-form-item>
        <el-form-item label="地区" prop="location" size="mini">
          <el-select v-model="registerForm.location" placeholder="地区">
            <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="addPeople">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="60px">
        <el-form-item label="用户名" size="mini">
          <el-input v-model="form.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="密码" size="mini">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="form.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机" size="mini">
          <el-input v-model="form.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" size="mini">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="生日" prop="birth" size="mini">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width: 100%;"
            :picker-options="expireTimeOPtion"></el-date-picker>
        </el-form-item>
        <el-form-item label="签名" size="mini">
          <el-input type="textarea" v-model="form.introduction"></el-input>
        </el-form-item>
        <el-form-item label="地区" size="mini">
          <el-select v-model="form.location" placeholder="地区">
            <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="saveEdit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 删除提示框 -->
    <el-dialog title="删除用户" :visible.sync="delVisible" width="300px" center>
      <div class="del-dialog-cnt" align="center">删除不可恢复，是否确定删除？</div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="delVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="deleteRow">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from '../mixins/index'
import { cities, rules } from '../assets/data/userrules'
import { deleteUser, getAllUser, setUser, updatePermission, updateUser } from '../api/index'

export default {
  name: 'user-page',
  mixins: [mixin],
  data() {
    return {
      registerForm: { // 注册
        username: '',
        password: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: '',
        introduction: '',
        location: ''
      },
      tableData: [], // 记录用户信息，用于显示
      tempData: [], // 记录用户信息，用于搜索时能临时记录一份用户信息
      is_search: false,
      multipleSelection: [], // 记录要删除的用户信息
      centerDialogVisible: false,
      editVisible: false, // 显示编辑框
      delVisible: false, // 显示删除框
      select_word: '', // 记录输入框输入的内容
      form: { // 记录编辑的信息
        id: '',
        username: '',
        password: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: '',
        introduction: '',
        location: '',
        createTime: '',
        updateTime: ''
      },
      rules: {},
      cities: [],
      pageSize: 4, // 页数
      currentPage: 1, // 当前页
      idx: -1, // 记录当前点中的行

      // 日期选择限制
      expireTimeOPtion: {
        disabledDate(time) {
          let curDate = (new Date()).getTime()
          let years = 16 * 365 * 24 * 3600 * 1000;
          let adult = curDate - years;
          return time.getTime() > adult
        }
      }

    }
  },

  computed: {
    // 计算当前表格中的数据
    data() {
      return this.tableData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },

  watch: {
    select_word: function () {
      if (this.select_word === '') {
        this.tableData = this.tempData
      } else {
        this.tableData = []
        for (let item of this.tempData) {
          if (item.username.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },

  created() {
    this.rules = rules
    this.cities = cities
    this.getData()
  },

  methods: {
    // 获取当前页
    handleCurrentChange(val) {
      this.currentPage = val
    },

    // 更新头像
    uploadUrl(id) {
      return `${this.$store.state.HOST}/user/updateUserPic?id=${id}`
    },

    // 获取用户信息
    getData() {
      this.tableData = []
      this.tempData = []
      getAllUser().then((res) => {
        this.tableData = res
        this.tempData = res
        this.currentPage = 1
      })
    },
    getCollect(id) {
      this.$router.push({ path: '/collect', query: { id } })
    },

    // 设置筛选
    handleFilterChange(filters) {
      const keys = Object.keys(filters)
      const key = keys[0]
      if (filters[key].length > 0) {
        this.tableData = []
        for (let i of this.tempData) {
          if (i[key] == filters[key][0]) {
            this.tableData.push(i)
          }
        }
      } else {
        this.tableData = this.tempData
      }
      return this.tableData
    },
    // 获取筛选的字段
    getfilterNameItem() {
      let apiArr = []
      for (let i in cities) {
        for (let j in this.tableData) {
          if (this.tableData[j].location == cities[i].value) {
            apiArr.push({
              text: cities[i].value,
              value: cities[i].value
            })
            break
          }
        }
      }
      return apiArr
    },

    // 添加用户
    addPeople() {
      this.$refs['registerForm'].validate(valid => {
        // console.log(valid)
        if (valid) {
          let d = this.registerForm.birth
          let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
          let params = new URLSearchParams()
          params.append('username', this.registerForm.username)
          params.append('password', this.registerForm.password)
          params.append('sex', this.registerForm.sex)
          params.append('phoneNum', this.registerForm.phoneNum)
          params.append('email', this.registerForm.email)
          params.append('birth', datetime)
          params.append('introduction', this.registerForm.introduction)
          params.append('location', this.registerForm.location)
          params.append('avator', '/img/user.jpeg')
          setUser(params)
            .then(res => {
              if (res.code === 1) {
                this.getData()
                this.registerForm = []
                this.notify('添加成功', 'success')
                this.$nextTick(() => {
                  this.$refs.form.clearValidate()
                })
              } else {
                this.notify('添加失败', 'error')
              }
            })
            .catch(err => {
              this.notify('用户相关信息已存在', 'error')
              console.log(err)
            })
          this.centerDialogVisible = false
        }
      })

    },
    // 编辑
    handleEdit(row) {
      this.idx = row.id
      this.form = {
        id: row.id,
        username: row.username,
        password: row.password,
        sex: row.sex,
        phoneNum: row.phoneNum,
        email: row.email,
        birth: row.birth,
        introduction: row.introduction,
        location: row.location,
        avator: row.avator
      }
      this.editVisible = true
    },

    // 保存编辑
    saveEdit() {
      let d = new Date(this.form.birth)
      let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('username', this.form.username)
      params.append('password', this.form.password)
      params.append('sex', this.form.sex)
      params.append('phoneNum', this.form.phoneNum)
      params.append('email', this.form.email)
      params.append('birth', datetime)
      params.append('introduction', this.form.introduction)
      params.append('location', this.form.location)
      updateUser(params).then(res => {
        if (res.code === 1) {
          this.getData()
          this.notify('修改成功', 'success')
        } else {
          this.notify('修改失败', 'error')
        }
      }).catch(err => {
        console.log(err)
      })
      this.editVisible = false
    },

    // 修改用户权限
    setPermission(row) {
      this.$confirm(`是否变更用户评论权限？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let params = new URLSearchParams()
        params.append('id', row.id)
        if (row.permission == 1) {
          row.permission = 0
        } else {
          row.permission = 1
        }
        params.append('permission', row.permission)
        updatePermission(params).then(res => {
          if (res.code == 1) {
            this.notify(res.msg, 'success')
          } else {
            this.notify(res.msg, 'error')
          }
        }).catch(err => {
          console.log(err)
        })
      })
    },

    // 确定删除
    deleteRow() {
      deleteUser(this.idx)
        .then(res => {
          if (res) {
            this.getData()
            this.notify('删除成功', 'success')
          } else {
            this.notify('删除失败', 'error')
          }
        })
        .catch(failResponse => { })
      this.delVisible = false
    }
  }
}

</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
  font-size: 12px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
