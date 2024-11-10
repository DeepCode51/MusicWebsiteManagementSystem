<template>
  <div class="orders">
    <h1 class="title">
      订单信息
    </h1>
    <!-- 时间过滤框 -->
    <el-date-picker style="display: flex;" v-model="checkTime" type="daterange" align="right" unlink-panels
      range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" format="yyyy-MM-dd"
      :picker-options="pickerOptions" @change="checkChange">
    </el-date-picker>

    <hr>
    <el-table :data="data" height="550px" show-summary :summary-method="getSummaries" @filter-method="handleFilterChange" >
      <el-table-column prop="code" label="订单编号" width="200" align="center"></el-table-column>
      <el-table-column prop="song.name" label="歌名" width="200" align="center">
        <template slot-scope="scope">
          <div>{{ replaceFName(scope.row.song.name) }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="song.name" label="歌手" width="120" align="center">
        <template slot-scope="scope">
          <div>{{ replaceLName(scope.row.song.name) }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格（单位：元）" width="200" align="center">
        <template slot-scope="scope">
          <div>{{ scope.row.song.price }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="ispay" label="付费状态" width="120" align="center"
        :filters="[{ text: '已付费', value: '1' }, { text: '未支付', value: '0' }]" :filter-method="filterHandler"
        :filter-multiple="false">
        <template slot-scope="scope">
          <div>{{ replaceispay(scope.row.ispay) }}
            <el-button v-if="scope.row.ispay == 0" type="text" size="small" @click="toPay(scope.row.id)">支付</el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="ostatus" label="订单状态" width="120" align="center"
        :filters="[{ text: '已完成', value: '1' }, { text: '未完成', value: '0' }]" :filter-method="filterHandler"
        :filter-multiple="false">
        <template slot-scope="scope">
          <div>
            {{ replaceStatus(scope.row.ostatus) }}
            <el-button type="text" size="small"
              v-if="scope.row.ispay == 1 && scope.row.url != null && scope.row.ostatus == 0"
              @click="toFinish(scope.row.id)">确认完成</el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="完整歌曲" width="120" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.url == null"
            :style='{ "border": "0", "cursor": "pointer", "padding": "0 15px", "margin": "0 ", "outline": "none", "color": "#000", "borderRadius": "0" }'>无</el-button>
          <el-button v-if="scope.row.url != null" type="text" size="small" @click="download(scope.row)">下载
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="200" align="center" sortable
        :default-sort="{ prop: 'createTime', order: 'ascending' }"></el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination @current-change="handleCurrentChange" background layout="total, prev, pager, next"
        :current-page="currentPage" :page-size="pageSize" :total="orderList.length">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import { getOrderOfUserId, setIspay, download } from '../api/index'

export default {
  name: 'order-list',
  mixins: [mixin],
  data() {
    return {
      tempData: [],
      orderList: [],
      pageSize: 10, // 页数
      currentPage: 1, // 当前页
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
    ...mapGetters([
      'userId',
      'id' // 音乐ID
    ]),
    // 计算当前表格中的数据
    data() {
      return this.orderList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  watch: {
    id() {
      this.getOrders(this.userId)
    }
  },
  mounted() {
    this.getOrders(this.userId)
    this.changeIndex('订单管理')
  },
  methods: {
    changeIndex(value) {
      this.$store.commit('setActiveName', value)
    },
    // 获取当前页
    handleCurrentChange(val) {
      this.currentPage = val
    },
    getOrders(userId) {
      getOrderOfUserId(userId)
        .then(res => {
          // console.log(res)
          this.orderList = res
          this.tempData = res
          this.currentPage = 1
        }).catch(err => {
          this.notify('订单信息加载失败', 'error')
          console.log(err)
        })
    },
    // 修改付费状态
    replaceispay(value) {
      if (value == 1) {
        return '已付费'
      } else {
        return '未付费'
      }
    },
    // 修改订单状态
    replaceStatus(value) {
      if (value == 1) {
        return '已完成'
      } else {
        return '未完成'
      }
    },
    toPay(id) {
      console.log(id)
      setTimeout(() => this.$router.push({ path: `/pay/${id}` }), 2000)
    },
    toFinish(id) {
      this.$confirm(`确定订单已完成?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let params = new URLSearchParams()
        params.append('id', id)
        params.append('ispay', 1)
        params.append('ostatus', 1)
        setIspay(params).then(res => {
          if (res.code == 1) {
            this.notify('订单已完成', 'success')
            this.getOrders(this.userId)
          } else {
            this.notify('订单更新失败', 'error')
          }
        })
      })
    },

    // 下载歌曲
    download(item) {
      console.log(item)
      let artist = item.song.name.split('-')[0]
      let title = item.song.name.split('-')[1]
      download(item.url)
        .then(res => {
          let content = res.data
          let eleLink = document.createElement('a')
          eleLink.download = `${artist}-${title}.mp3`
          eleLink.style.display = 'none'
          // 字符内容转变成blob地址
          let blob = new Blob([content])
          eleLink.href = URL.createObjectURL(blob)
          // 触发点击
          document.body.appendChild(eleLink)
          eleLink.click()
          // 然后移除
          document.body.removeChild(eleLink)
        })
        .catch(err => {
          console.log(err)
        })
    },

    // 筛选
    filterHandler(value, row, column) {
      const property = column['property']
      return row[property] == value
    },

    checkChange(e) {
      this.checkStartTime = e ? e[0] : "";
      this.checkEndTime = e ? e[1] : "";
      this.orderList = []
      for (let item of this.tempData) {
        if (item.createTime > this.checkStartTime && item.createTime < this.checkEndTime + 1) {
          this.orderList.push(item)
        }
      }
      if (e == null) {
        this.orderList = this.tempData
      }
    },


    // 计算总价
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      const data1 = []
      var temp = 0
      var temp1 = 0
      columns.forEach((column, index) => {
        if (index == 0) {
          sums[index] = <div><p>实际支付<br /><br />预计支付</p> </div>
          return
        } else if (index == 3) {
          //页面分别统计处理
          for (let item of data) {
            if (item.ispay == 1) {
              data1.push(item)
            }
          }
          const values1 = data1.map(item => Number(item.song[column.property]))
          if (!values1.every(value => isNaN(value))) {
            temp = values1.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
          }
          const values = data.map(item => Number(item.song[column.property]))
          if (!values.every(value => isNaN(value))) {
            temp1 = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
          }
          sums[index] = <div><p>{temp.toFixed(1)}元<br /><br />{temp1.toFixed(1)}元</p> </div>
        }
      })
      return sums
    },

    // 设置筛选
    handleFilterChange(filters) {
      const keys = Object.keys(filters)
      const key = keys[0]
      if (filters[key].length > 0) {
        this.orderList = []
        for (let i of this.tempData) {
          if (i[key] == filters[key][0]) {
            this.orderList.push(i)
          }
        }
      } else {
        this.orderList = this.tempData
      }
      return this.orderList
    },
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/order-list.scss';
</style>
