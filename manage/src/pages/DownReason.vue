<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-tickets"></i> 举报原因
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" size="small" class="handle-del mr10" @click="delAll"
                    style="background-color: #de4307; border: 0px; font-size: 15px;">批量删除</el-button>
                <el-input v-model="select_word" size="small" placeholder="筛选关键词" class="handle-input mr10"></el-input>
            </div>
            <el-table :data="data" size="mini" border style="width: 100%" ref="multipleTable"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column prop="username" label="用户名"></el-table-column>
                <el-table-column prop="reason" label="举报原因" width="800" :filters="getfilterNameItem()"
                    :filter-method="filterHandler"></el-table-column>
                <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
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
import { getReason, getUserOfId, deleteReason } from '../api/index'

export default {
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
            pageSize: 10, // 页面显示数
            currentPage: 1, // 当前页
            commentId: '',
            optionType: ''
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
                    if (item.name.includes(this.select_word)) {
                        this.tableData.push(item)
                    }
                }
            }
        }
    },
    created() {
        this.commentId = this.$route.query.commentId
        this.optionType = this.$route.query.optionType
        this.getData()
    },
    methods: {
        // 获取当前页
        handleCurrentChange(val) {
            this.currentPage = val
        },

        // 获取原因
        getData() {
            this.tableData = []
            this.tempData = []
            getReason(this.commentId, this.optionType).then(res => {
                for (let item of res) {
                    this.getUsers(item.userId, item)
                }
            })
            this.currentPage = 1
        },

        getUsers(id, item) {
            getUserOfId(id)
                .then(res => {
                    let o = item
                    o.username = res.username
                    this.tableData.push(o)
                    this.tempData.push(o)
                })
                .catch(err => {
                    console.log(err)
                })
        },

        // 获取筛选的字段
        getfilterNameItem() {
            let apiArr = []
            let tempArr = []
            for (let j in this.tableData) {
                if (!tempArr.includes(this.tableData[j].reason)) {
                    tempArr.push(this.tableData[j].reason)
                }
            }
            for (var i = 0; i < tempArr.length; i++) {
                apiArr.push({
                    text: tempArr[i],
                    value: tempArr[i]
                })
            }
            return apiArr
        },

        // 确定删除
        deleteRow() {
            deleteReason(this.idx, this.commentId)
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
  