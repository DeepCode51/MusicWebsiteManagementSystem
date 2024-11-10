<template>
  <div class="content">
    <h1 class="title">
      <slot name="title"></slot>
    </h1>
    <hr>
    <ul>
      <li class="list-title">
        <div class="song-item">
          <span class="item-index"></span>
          <span class="item-collection"></span>
          <span class="item-download"></span>
          <span class="item-operation"></span>
          <span class="item-title">歌名</span>
          <span class="item-name">歌手</span>
          <span class="item-price">价格</span>
          <span class="item-intro">专辑</span>
          <span class="item-ctimes">播放量</span>
        </div>
      </li>
      <li class="list-content" v-for="(item, index) in songList" :key="index" v-bind:style="setStyle(item.price)">
        <div class="song-item" :class="{ 'is-play': id === item.id }"
          @dblclick="toplay(item.id, item.url, item.pic, index, item.name, item.lyric, item.clickTimes)">
          <span class="item-index">
            <span v-if="id != item.id">{{ index + 1 }}</span>
            <svg v-if="id == item.id" class="icon" aria-hidden="true">
              <use xlink:href="#icon-yinliang"></use>
            </svg>
          </span>
          <span class="item-collection">
            <div ref="collect" class="item" @click="collection(item,index)" v-bind:style="setColor(index)">
              <svg class="icon-collection" aria-hidden="true">
                <use xlink:href="#icon-xihuan-shi"></use>
              </svg>
            </div>
          </span>
          <span class="item-download">
            <!--下载-->
            <div class="item" @click="download(item)">
              <svg class="icon-download" aria-hidden="true">
                <use xlink:href="#icon-xiazai"></use>
              </svg>
            </div>
          </span>
          <span class="item-title">{{ replaceFName(item.name) }}</span>
          <span class="item-name">{{ replaceLName(item.name) }}</span>
          <span class="item-price">{{ item.price }}</span>
          <span class="item-intro">{{ item.introduction }}</span>
          <span class="item-ctimes">{{ item.clickTimes }}</span>
        </div>
      </li>
    </ul>
    <!-- 付费下载提示框 -->
    <el-dialog title="提示" :visible.sync="payVisible" width="300px" center>
      <div class="del-dialog-cnt" align="center">该歌曲需要付费下载，确认付费后将跳转到订单界面？</div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="payVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="addOrder">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import { download, getCollectOfUserId, setCollect, setOrder } from '../api'
// import { download,setOrder } from '../api'

export default {
  name: 'album-content',
  mixins: [mixin],
  data() {
    return {
      payVisible: false,
      tempId:''
    }
  },
  props: [
    'songList'
  ],
  computed: {
    ...mapGetters([
      'id', // 音乐ID
      'isActive', //收藏图标状态
    ])
  },

  methods: {
    setStyle(str) {
      // console.log(str)
      if (str != 0.0 && str != null) {
        return 'color:red'
      }
    },

    setColor(index) {
      getCollectOfUserId(this.userId)
        .then(res => {
          for (let j of res) {
            if (j.songId == this.songList[index].id) {
              // console.log(index)
              return this.$refs.collect[index].children[0].style.color = 'red'
            }
          }
        })
        .catch(err => {
          console.log(err)
        })
    },

    collection(item,index) {
      console.log(item)
      let params = new URLSearchParams()
      params.append('songId', item.id)
      params.append('userId', this.userId)
      // console.log(params)
      setCollect(params)
        .then(res => {
          if (res.code == 1) {
            this.notify(res.msg, 'success')
            this.$refs.collect[index].children[0].style.color = 'red'
            this.$store.commit('setIsActive', true)
          } else if (res.code == 2) { //取消收藏
            this.notify(res.msg, 'success')
            this.$refs.collect[index].children[0].style.color = 'black'
            this.$store.commit('setIsActive', false)
          } else {
            this.notify(res.msg, 'warning')
          }
        })
        .catch(err => {
          console.log(err)
        })
    },

    // 下载
    download(item) {
      let artist = item.name.split('-')[0]
      let title = item.name.split('-')[1]
      this.tempId = item.id
      if (item.price == 0.0) {
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
      } else { // 付费提示窗口
        this.payVisible = true
      }
    },

    // 生成订单
    addOrder() {
      let params = new URLSearchParams()
      params.append('songId', this.tempId)
      params.append('userId', this.userId)
      // params.append('code', )
      params.append('ispay', 0)
      params.append('ostatus', 0)
      console.log(params)
      setOrder(params)
        .then(res => {
          if (res.code == 1) {
            this.notify(res.msg, 'success')
            setTimeout(() => this.$router.push({ path: '/order-list' }), 1000)
          } else {
            this.notify(res.msg, 'error')
          }
          this.payVisible = false
        }).catch(err => {
          console.log(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/album-content.scss';
</style>
