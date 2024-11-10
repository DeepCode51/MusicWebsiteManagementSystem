<template>
  <div>
    <div class="comment">
      <h2>评论</h2>
      <div class="comment-msg">
        <div class="comment-img">
          <img :src=attachImageUrl(avator) alt="">
        </div>
        <el-input class="comment-input" type="textarea" :rows="2" placeholder="请输入内容" v-model="textarea">
        </el-input>
      </div>
      <el-button type="primary" class="sub-btn" @click="postComment()">评论</el-button>
    </div>
    <p>共 {{ commentList.length }} 条评论</p>
    <!-- <div class="pagination">
      <el-pagination @current-change="handleCurrentChange" background layout="total, prev, pager, next"
        :current-page="currentPage" :page-size="pageSize" :total="commentList.length">
      </el-pagination>
    </div> -->
    <!-- <nav class="comment-nav" ref="change">
      <span :class="{ 'active': isActive }" @click="getComment()">最新</span>
      |
      <span :class="{ 'active': isActive }" @click="getHotComment()">最热</span>
    </nav> -->
    <ul class="popular" v-for="(item, index) in commentList" :key="index">
      <li>
        <div class="popular-img">
          <img :src=attachImageUrl(userPic[index]) alt="">
        </div>
        <div class="popular-msg">
          <ul>
            <li class="name">{{ userName[index] }}</li>
            <li class="time">{{ item.createTime }}</li>
            <li class="content">{{ item.content }}</li>
          </ul>
        </div>
        <div class="operation">
          <div class="down" ref="down" @click="setDowVis(index)">
            <svg class="icon-jinzhi" aria-hidden="true">
              <use xlink:href="#icon-zan"></use>
            </svg>
          </div>
          <div class="up" ref="up" @click="postUp(item.id, index)">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-zan" v-bind:style="setUpStyle(item.id, index)"></use>
            </svg>
            {{ item.up }}
          </div>
        </div>
        <!-- 举报提示框 -->
        <el-dialog title="请选择举报原因" :visible.sync="downVisible" width="300px" center>
          <el-radio-group v-model="reason">
            <el-radio :label="'段子或无意义的评论'">段子或无意义的评论</el-radio>
            <br>
            <el-radio :label="'恶意攻击谩骂'">恶意攻击谩骂</el-radio>
            <br>
            <el-radio :label="'营销广告'">营销广告</el-radio>
            <br>
            <el-radio :label="'淫秽色情'">淫秽色情</el-radio>
            <br>
            <el-radio :label="'政治反动'">政治反动</el-radio>
            <br>
            <el-radio :label="'虚假信息'">虚假信息</el-radio>
            <br>
          </el-radio-group>
          <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click="downVisible = false">取 消</el-button>
            <el-button type="primary" size="mini" @click="postDown(i)">确 定</el-button>
          </span>
        </el-dialog>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import { getShowComment, getUdOfUserId, getUserOfId, setComment, setDown, setLike } from '../api/index'

export default {
  name: 'comment',
  mixins: [mixin],
  props: [
    'playId', // 歌曲ID或歌单ID
    'type' // 歌单（1）/歌曲（0）
  ],
  data() {
    return {
      commentList: [], // 存放评论列表
      userPic: [], // 保存评论用户头像
      userName: [], // 保存评论用户名字
      textarea: '', // 存放输入内容
      reason: '',
      downVisible: false,
      i: '',
      // pageSize: 5, // 页数
      // currentPage: 1, // 当前页
    }
  },
  computed: {
    ...mapGetters([
      'id',
      'userId', // 用户ID
      'index', // 列表中的序号
      'loginIn', // 用户是否登录
      'avator' // 用户头像
    ]),
    // data() {
    //   return this.commentList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    // }
  },
  watch: {
    id() {
      this.getComment()
    }
  },
  mounted() {
    this.getComment()
  },
  methods: {
    // // 获取当前页
    // handleCurrentChange(val) {
    //   this.currentPage = val
    // },

    // 获取所有评论(最新)
    getComment() {
      getShowComment(this.type, this.playId)
        .then(res => {
          this.commentList = res
          for (let item of res) {
            this.getUsers(item.userId)
          }
        })
        .catch(err => {
          this.notify('评论加载失败', 'error')
          console.log(err)
        })
      // this.currentPage = 1
    },
    // 获取评论用户的昵称和头像
    getUsers(id) {
      getUserOfId(id)
        .then(res => {
          this.userPic.push(res.avator)
          this.userName.push(res.username)
        })
        .catch(err => {
          this.notify('用户获取失败', 'error')
          console.log(err)
        })
    },
    //获取用户点赞状态
    setUpStyle(id, index) {
      getUdOfUserId(this.userId)
        .then(res => {
          // console.log(index)
          for (let item of res) {
            if (item.commentId == id) {
              // console.log(item)
              if (item.optionType == 0) {
                return this.$refs.up[index].children[0].style.color = '#2796dd'
              }
            }
          }
        })
        .catch(err => {
          console.log(err)
        })
    },

    // 提交评论
    postComment() {
      // if (this.loginIn) {
      // 0 代表歌曲， 1 代表歌单
      let params = new URLSearchParams()
      if (this.type == 1) {
        params.append('songListId', this.playId)
      } else if (this.type == 0) {
        params.append('songId', this.playId)
      }
      params.append('userId', this.userId)
      params.append('type', this.type)
      params.append('content', this.textarea)
      setComment(params)
        .then(res => {
          if (res.code == 1) {
            this.textarea = ''
            this.getComment()
            this.notify(res.msg, 'success')
          } else {
            this.notify(res.msg, 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      // } else {
      //   this.notify('请先登录', 'warning')
      // }
    },
    // 点赞
    postUp(id, index) {
      // if (this.loginIn) {
      let params = new URLSearchParams()
      params.append('id', id)
      params.append('userId', this.userId)
      params.append('optionType', 0)
      setLike(params)
        .then(res => {
          if (res.code == 1) {
            this.$refs.up[index].children[0].style.color = '#2796dd'
            this.getComment()
          } else if (res.code == 2) {
            // console.log(this.commentList[index].up)
            this.commentList[index].up = res.up
            this.$refs.up[index].children[0].style.color = 'black'
          } else {
            this.notify(res.msg, 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      // } else {
      //   this.notify('请先登录', 'warning')
      // }
    },

    setDowVis(index) {
      if (this.$refs.down[index].children[0].style.color == 'red') {
        this.notify('您已经举报过了', 'error')
        this.downVisible = false
      } else {
        this.i = index
        this.downVisible = true
      }
    },

    // 举报
    postDown(i) {
      let params = new URLSearchParams()
      params.append('id', this.commentList[i].id)
      params.append('userId', this.userId)
      params.append('optionType', 1)
      params.append('reason', this.reason)
      setDown(params)
        .then(res => {
          if (res.code == 1) {
            this.notify(res.msg, 'success')
            this.getComment()
            this.reason = ''
          } else {
            this.notify(res.msg, 'error')
          }
          this.downVisible = false
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/comment.scss';
</style>
