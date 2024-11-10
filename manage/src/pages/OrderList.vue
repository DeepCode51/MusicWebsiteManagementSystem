<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" size="small" placeholder="请输入订单信息" class="handle-input mr10"></el-input>
                <!-- 时间过滤框 -->
                <el-date-picker size="small" v-model="checkTime" type="daterange" align="right" unlink-panels
                    range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd"
                    format="yyyy-MM-dd" :picker-options="pickerOptions" @change="checkChange">
                </el-date-picker>
            </div>
            <el-table ref="multipleTable" size="mini" border style="width: 100%" height="550px" :data="data"
                @selection-change="handleSelectionChange" show-summary :summary-method="getSummaries">
                <el-table-column prop="code" label="订单号" width="280" align="center"></el-table-column>
                <el-table-column prop="user.username" label="用户名" width="120" align="center"></el-table-column>
                <el-table-column prop="song.name" label="歌名" width="120" align="center">
                    <template slot-scope="scope">
                        <div>{{ replaceFName(scope.row.song.name) }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="song.name" label="歌手" width="120" align="center">
                    <template slot-scope="scope">
                        <div>{{ replaceLName(scope.row.song.name) }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="price" label="价格（单位：元）" width="120" align="center">
                    <template slot-scope="scope">
                        <div>{{ scope.row.song.price }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="ispay" label="付费状态" width="120" align="center" :column-key="ispay"
                    :filters="[{ text: '已付费', value: '1' }, { text: '未支付', value: '0' }]" :filter-method="filterHandler"
                    :filter-multiple="false">
                    <template slot-scope="scope">
                        <div>{{ replacePay(scope.row.ispay) }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="ostatus" label="订单状态" width="120" align="center" :column-key="'ostatus'"
                    :filters="[{ text: '已完成', value: '1' }, { text: '未完成', value: '0' }]" :filter-method="filterHandler"
                    :filter-multiple="false">
                    <template slot-scope="scope">
                        <div>{{ replaceStatus(scope.row.ostatus) }}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="url" label="完整歌曲操作" width="120" align="center">
                    <template slot-scope="scope">
                        <el-upload class="upload-demo change"
                            :action="sendSongUrl(scope.row.id, scope.row.ispay, scope.row.ostatus)" :show-file-list="false"
                            :on-success="handleSongSuccess" :before-upload="beforeSongUpload">
                            <el-button size="mini" type="text"
                                :style="{ display: scope.row.ostatus == 1 ? 'none' : scope.row.ispay == 0 ? 'none' : '' }">发送歌曲</el-button>
                        </el-upload>
                        <el-button v-if="scope.row.url != null" type="text" size="small" @click="download(scope.row)">预览
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="下单时间" align="center" sortable
                    :default-sort="{ prop: 'createTime', order: 'ascending' }"></el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination @current-change="handleCurrentChange" background layout="total, prev, pager, next"
                    :current-page="currentPage" :page-size="pageSize" :total="tableData.length">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
import { mixin } from '../mixins/index'
import { getAllOrder, download } from '../api/index'

export default {
    name: 'order-list',
    mixins: [mixin],
    data() {
        return {
            tableData: [],
            tempDate: [],
            multipleSelection: [],
            delVisible: false,
            select_word: '',
            pageSize: 10, // 页数
            currentPage: 1, // 当前页
            idx: -1,
            checkTime: [],
            checkStartTime: '',
            checkEndTime: '',
            pickerOptions: {
                shortcuts: [
                    {
                        text: "昨天",
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24);
                            end.setTime(end.getTime() - 3600 * 1000 * 24);
                            picker.$emit("pick", [start, end]);
                        },
                    },
                    {
                        text: "最近一周",
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit("pick", [start, end]);
                        },
                    },
                    {
                        text: "最近一个月",
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit("pick", [start, end]);
                        },
                    },
                    {
                        text: "最近三个月",
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit("pick", [start, end]);
                        },
                    },
                ],
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
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
                this.tableData = this.tempDate
            } else {
                this.tableData = []
                for (let item of this.tempDate) {
                    if (item.user.username.includes(this.select_word)) {
                        this.tableData.push(item)
                    }
                    else if (item.song.name.includes(this.select_word)) {
                        this.tableData.push(item)
                    }
                    else if (item.code.includes(this.select_word)) {
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

        // 获取所有信息
        getData() {
            this.tableData = []
            this.tempDate = []
            getAllOrder().then((res) => {
                this.tableData = res
                this.tempDate = res
                this.currentPage = 1
            })
        },

        // 发送付费歌曲url
        sendSongUrl(id, ispay, ostatus) {
            return `${this.$store.state.HOST}/orderList/sendSongUrl?id=${id}&&ispay=${ispay}&&ostatus=${ostatus}`
        },
        handleSongSuccess(res, file) {
            if (res.code == 1) {
                this.getData()
                this.notify('发送成功', 'success')
            } else {
                this.notify('发送失败', 'error')
            }
        },
        beforeSongUpload(file) {
            var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
            const extension = testmsg === 'mp3'
            if (!extension) {
                this.$message({
                    message: '上传文件只能是mp3格式！',
                    type: 'error'
                })
            }
            return extension
        },

        // 下载预览
        download(row) {
            window.open(this.$store.state.HOST + row.url)
            // console.log(this.state.HOST + row.url)
        },

        // 计算总价
        getSummaries(param) {
            const { columns, data } = param;
            const sums = []
            var temp = 0
            var temp1 = 0
            columns.forEach((column, index) => {
                if (index == 1) {
                    sums[index] = <div><p>实际收益<br /><br />预计收益</p> </div>
                    return
                } else if (index == 5) {
                    //页面分别统计处理
                    for (let item of data) {
                        temp1 = Number(item.song.price) + temp1
                        if (item.ispay == 1) {
                            temp = Number(item.song.price) + temp
                        }
                    }
                    // const values = data.map(item => Number(item.song[column.property]))
                    // if (!values.every(value => isNaN(value))) {
                    //     temp1 = values.reduce((prev, curr) => {
                    //         const value = Number(curr)
                    //         if (!isNaN(value)) {
                    //             return prev + curr
                    //         } else {
                    //             return prev
                    //         }
                    //     }, 0)
                    // }
                    // console.log(temp1)
                    sums[index] = <div><p>{temp.toFixed(1)}元<br /><br />{temp1.toFixed(1)}元</p> </div>
                }
            })
            return sums
        },

        checkChange(e) {
            this.checkStartTime = e ? e[0] : "";
            this.checkEndTime = e ? e[1] : "";
            this.tableData = []
            for (let item of this.tempDate) {
                if (item.createTime > this.checkStartTime && item.createTime < this.checkEndTime + 1) {
                    this.tableData.push(item)
                }
            }
            if (e == null) {
                this.tableData = this.tempDate
            }
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

.singer-img {
    width: 100%;
    height: 80px;
    border-radius: 5px;
    margin-bottom: 5px;
    overflow: hidden;
}

.pagination {
    display: flex;
    justify-content: center;
}
</style>
