<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="small" class="handle-del mr10" @click="delAll"
          style="background-color: #de4307; border: 0px; font-size: 15px;">批量删除</el-button>
        <el-input v-model="select_word" size="small" placeholder="筛选关键词" class="handle-input mr10"></el-input>
      </div>
      <el-table :data="data" size="mini" border style="width: 100%" ref="multipleTable"
        @selection-change="handleSelectionChange" @filter-change="handleFilterChange">
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column prop="user.username" label="用户名"></el-table-column>
        <el-table-column prop="content" label="评论内容"></el-table-column>
        <el-table-column prop="song.name" label="歌名">
          <template slot-scope="scope" v-if="scope.row.song.name != null">
            <div>{{ replaceFName(scope.row.song.name) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="songList.title" label="歌单名"></el-table-column>
        <el-table-column prop="up" label="点赞数" sortable
          :default-sort="{ prop: 'up', order: 'ascending' }"></el-table-column>
        <el-table-column prop="down" label="举报数" sortable :default-sort="{ prop: 'down', order: 'ascending' }">
          <template slot-scope="scope">
            <el-button :style="setColor(scope.row.down)" type="text" size="mini" @click="downDetail(scope.row)">{{
              scope.row.down }}</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" prop="isshow" :column-key="'isshow'"
          :filters="[{ text: '显示', value: '1' }, { text: '隐藏', value: '0' }]" :filter-method="filterHandler"
          :filter-multiple="false">
          <template slot-scope="scope">
            <el-switch class="switchStyle" @change="setIsShow(scope.row)" active-color="green" :value="scope.row.isshow"
              :active-value="1" :inactive-value="0" inactive-color="#dadbdf"></el-switch>
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

    <!-- 删除提示框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
      <div class="del-dialog-cnt" align="center">删除不可恢复，是否确定删除？</div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="delVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="deleteRow">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { getAllComment, updateIsShow, deleteComment } from '../api/index'

export default {
  name: 'comment-page',
  mixins: [mixin],
  data() {
    return {
      tableData: [],
      tempData: [],
      multipleSelection: [],
      editVisible: false,
      delVisible: false,
      select_word: '',
      idx: -1,
      pageSize: 12, // 页面显示数
      currentPage: 1, // 当前页
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
          if (item.user.username.includes(this.select_word)) {
            this.tableData.push(item)
          }
          else if (item.song.name.includes(this.select_word)) {
            this.tableData.push(item)
          }
          else if (item.songList.title.includes(this.select_word)) {
            this.tableData.push(item)
          }
          else if (item.content.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created() {
    this.getData()
  },
  methods: {
    // 获取当前页
    handleCurrentChange(val) {
      this.currentPage = val
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

    // 设置颜色
    setColor(down) {
      if (down == 0) {
        return 'color:grey'
      }
    },

    // 获取评论
    getData() {
      this.tableData = []
      this.tempData = []
      getAllComment().then(res => {
        this.tableData = res
        this.tempData = res
        this.currentPage = 1
      })
    },

    // 跳转到举报信息详情内容
    downDetail(row) {
      if (row.down != 0) {
        this.$router.push({ path: `/Down`, query: { commentId: row.id, optionType: 1 } })
      }
    },

    // 修改评论是否显示
    setIsShow(row) {
      this.$confirm(`是否修改该评论显示状态？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let params = new URLSearchParams()
        params.append('id', row.id)
        if (row.isshow == 1) {
          row.isshow = 0
        } else {
          row.isshow = 1
        }
        params.append('isshow', row.isshow)
        updateIsShow(params).then(res => {
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
      deleteComment(this.idx)
        .then(res => {
          if (res) {
            this.getData()
            this.notify('删除成功', 'success')
          } else {
            this.notify('删除失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.delVisible = false
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
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
