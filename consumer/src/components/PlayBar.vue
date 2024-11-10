<template>
  <div class="play-bar" :class="{ show: !toggle }" @mouseup="mouseup" @mousedown="mousedown">
    <div @click="toggle = !toggle" class="item-up" :class="{ turn: !toggle }">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-jiantou-xia-cuxiantiao"></use>
      </svg>
    </div>
    <div class="kongjian">
      <!--歌曲图片-->
      <div class="item-img" @click="goPlayerPage">
        <img :src=picUrl alt="">
      </div>
      <!--上一首-->
      <div class="item" @click="prev">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-ziyuanldpi"></use>
        </svg>
      </div>
      <!--播放-->
      <div class="item" @click="togglePlay">
        <svg class="icon" aria-hidden="true">
          <use :xlink:href="playButtonUrl"></use>
        </svg>
      </div>
      <!--下一首-->
      <div class="item" @click="next">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-ziyuanldpi1"></use>
        </svg>
      </div>
      <!--播放进度-->
      <div class="playing-speed">
        <!--播放开始时间-->
        <div class="current-time">{{ nowTime }}</div>
        <div class="progress-box">
          <div class="item-song-title">
            <div>{{ this.title }}</div>
            <div>{{ this.artist }}</div>
          </div>
          <div ref="progress" class="progress" @mousemove="mousemove">
            <!--进度条-->
            <div ref="bg" class="bg" @click="updatemove">
              <div ref="curProgress" class="cur-progress" :style="{ width: curLength + '%' }"></div>
            </div>
            <!--进度条 end -->
            <!--拖动的点点-->
            <div ref="idot" class="idot" :style="{ left: curLength + '%' }"></div>
            <!--拖动的点点 end -->
          </div>
        </div>
        <!--播放结束时间-->
        <div class="left-time">{{ songTime }}</div>
      </div>
      <!--音量-->
      <div class="item icon-volume">
        <svg v-if="volume !== 0" class="icon" aria-hidden="true">
          <use xlink:href="#icon-yinliang1"></use>
        </svg>
        <svg v-else class="icon" aria-hidden="true">
          <use xlink:href="#icon-yinliangjingyinheix"></use>
        </svg>
        <el-slider class="volume" v-model="volume" :vertical="true"></el-slider>
      </div>
      <!--收藏-->
      <div class="item" @click="collection">
        <svg :class="{ active: isActive }" class="icon" aria-hidden="true">
          <use xlink:href="#icon-xihuan-shi"></use>
        </svg>
      </div>
      <!--下载-->
      <div class="item" @click="download">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-xiazai"></use>
        </svg>
      </div>
      <!--歌曲列表-->
      <div class="item" @click="changeAside">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-liebiao"></use>
        </svg>
      </div>
    </div>
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
import { mapGetters } from 'vuex'
import { mixin } from '../mixins'
import { download, setCollect, setOrder } from '../api/index'

export default {
  name: 'play-bar',
  mixins: [mixin],
  data() {
    return {
      tag: false,
      nowTime: '00:00',
      songTime: '00:00',
      curLength: 0, // 进度条的位置
      progressLength: 0, // 进度条长度
      mouseStartX: 0, // 拖拽开始位置
      toggle: true, // 显示隐藏播放器界面
      volume: 50,
      payVisible: false,
      ispay: '',
      ostatus: ''
    }
  },
  computed: {
    ...mapGetters([
      'loginIn', // 用户登录状态
      'userId', // 用户 id
      'isPlay', // 播放状态
      'playButtonUrl', // 播放状态的图标
      'id', // 音乐id
      'url', // 音乐地址
      'title', // 歌名
      'artist', // 歌手名
      'picUrl', // 歌曲图片
      'curTime', // 当前音乐的播放位置
      'duration', // 音乐时长
      'listOfSongs', // 当前歌单列表
      'listIndex', // 当前歌曲在歌曲列表的位置
      'showAside', // 是否显示侧边栏
      'autoNext', // 用于触发自动播放下一首
      'isActive', //收藏图标状态
      'price', //价格
      'clickTimes'  //播放次数
    ])
  },
  watch: {
    // 切换播放状态的图标
    isPlay(val) {
      if (val) {
        this.$store.commit('setPlayButtonUrl', '#icon-zanting')
      } else {
        this.$store.commit('setPlayButtonUrl', '#icon-bofang')
      }
    },
    volume() {
      this.$store.commit('setVolume', this.volume / 100)
    },
    // 播放时间的开始和结束
    curTime() {
      this.nowTime = this.formatSeconds(this.curTime)
      this.songTime = this.formatSeconds(this.duration)
      // 移动进度条
      this.curLength = (this.curTime / this.duration) * 100
      // 处理歌词位置及颜色
    },
    // 自动播放下一首
    autoNext() {
      this.next()
    }
  },
  mounted() {
    this.progressLength = this.$refs.progress.getBoundingClientRect().width
    document.querySelector('.icon-volume').addEventListener('click', function (e) {
      document.querySelector('.volume').classList.add('show-volume')
      e.stopPropagation()
    }, false)
    document.querySelector('.volume').addEventListener('click', function (e) {
      e.stopPropagation()
    }, false)
    document.addEventListener('click', function () {
      document.querySelector('.volume').classList.remove('show-volume')
    }, false)
  },
  methods: {
    // 下载
    download() {
        if (this.price == 0.0) {
          download(this.url)
            .then(res => {
              let content = res.data
              let eleLink = document.createElement('a')
              eleLink.download = `${this.artist}-${this.title}.mp3`
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
        params.append('songId', this.id)
        params.append('userId', this.userId)
        params.append('ispay', 0)
        params.append('ostatus', 0)
        console.log(params)
        setOrder(params)
          .then(res => {
            if (res.code == 1) {
              this.notify(res.msg, 'success')
              this.payVisible = false
              setTimeout(() => this.$router.push({ path: '/order-list' }), 1000)
            } else {
              this.notify(res.msg, 'error')
            }
          }).catch(err => {
            console.log(err)
          })
      },

      changeAside() {
        let temp = !this.showAside
        this.$store.commit('setShowAside', temp)
      },
      // 控制音乐播放 / 暂停
      togglePlay() {
        if (this.isPlay) {
          this.$store.commit('setIsPlay', false)
        } else {
          this.$store.commit('setIsPlay', true)
        }
      },
      // 解析播放时间
      formatSeconds(value) {
        let theTime = parseInt(value)
        let theTime1 = 0
        let theTime2 = 0
        if (theTime > 60) {
          theTime1 = parseInt(theTime / 60) // 分
          theTime = parseInt(theTime % 60) // 秒
          // 是否超过一个小时
          if (theTime1 > 60) {
            theTime2 = parseInt(theTime1 / 60) // 小时
            theTime1 = 60 // 分
          }
        }
        // 多少秒
        if (parseInt(theTime) < 10) {
          var result = '0:0' + parseInt(theTime)
        } else {
          result = '0:' + parseInt(theTime)
        }
        // 多少分钟时
        if (theTime1 > 0) {
          if (parseInt(theTime) < 10) {
            result = '0' + parseInt(theTime)
          } else {
            result = parseInt(theTime)
          }
          result = parseInt(theTime1) + ':' + result
        }
        // 多少小时时
        if (theTime2 > 0) {
          if (parseInt(theTime) < 10) {
            result = '0' + parseInt(theTime)
          } else {
            result = parseInt(theTime)
          }
          result = parseInt(theTime2) + ':' + parseInt(theTime1) + ':' + result
        }
        return result
      },
      // 拖拽开始
      mousedown(e) {
        this.mouseStartX = e.clientX
        this.tag = true
      },
      // 拖拽结束
      mouseup() {
        this.tag = false
      },
      // 拖拽中
      mousemove(e) {
        if (!this.duration) {
          return false
        }
        if (this.tag) {
          let movementX = e.clientX - this.mouseStartX
          let curLength = this.$refs.curProgress.getBoundingClientRect().width
          //  计算出百分比
          this.progressLength = this.$refs.progress.getBoundingClientRect().width
          let newPercent = ((curLength + movementX) / this.progressLength) * 100
          if (newPercent > 100) {
            newPercent = 100
          }
          this.curLength = newPercent
          this.mouseStartX = e.clientX
          //  根据百分比推出对应的播放时间
          this.changeTime(newPercent)
        }
      },
      // 更改歌曲进度
      changeTime(percent) {
        let newCurTime = this.duration * (percent * 0.01)
        this.$store.commit('setChangeTime', newCurTime)
      },
      updatemove(e) {
        if (!this.tag) {
          let curLength = this.$refs.bg.offsetLeft
          this.progressLength = this.$refs.progress.getBoundingClientRect().width
          let newPercent = ((e.clientX - curLength) / this.progressLength) * 100
          if (newPercent < 0) {
            newPercent = 0
          } else if (newPercent > 100) {
            newPercent = 100
          }
          this.curLength = newPercent
          this.changeTime(newPercent)
        }
      },
      // 上一首
      prev() {
        if (this.listIndex !== -1 && this.listOfSongs.length > 1) {
          if (this.listIndex > 0) {
            this.$store.commit('setListIndex', this.listIndex - 1)
          } else {
            this.$store.commit('setListIndex', this.listOfSongs.length - 1)
          }
          this.toPlay(this.listOfSongs[this.listIndex].url)
        }
      },
      // 下一首
      next() {
        if (this.listIndex !== -1 && this.listOfSongs.length > 1) {
          if (this.listIndex < this.listOfSongs.length - 1) {
            this.$store.commit('setListIndex', this.listIndex + 1)
          } else {
            this.$store.commit('setListIndex', 0)//切换到第一首
          }
          this.toPlay(this.listOfSongs[this.listIndex].url)
        }
      },
      // 选中播放
      toPlay(url) {
        if (url && url !== this.url) {
          this.$store.commit('setId', this.listOfSongs[this.listIndex].id)
          this.$store.commit('setUrl', this.$store.state.configure.HOST + url)
          this.$store.commit('setpicUrl', this.$store.state.configure.HOST + this.listOfSongs[this.listIndex].pic)
          this.$store.commit('setTitle', this.replaceFName(this.listOfSongs[this.listIndex].name))
          this.$store.commit('setArtist', this.replaceLName(this.listOfSongs[this.listIndex].name))
          this.$store.commit('setLyric', this.parseLyric(this.listOfSongs[this.listIndex].lyric))
        }
      },
      // 跳转至歌词界面
      goPlayerPage() {
        if (this.openLyric == false) {
          this.$router.push({ path: `/lyric/${this.id}` });
          this.openLyric = true;
        } else {
          this.$router.go(-1);
          this.openLyric = false;
        }
        // this.$router.push({ path: `/lyric/${this.id}` })
      },
      // 收藏
      collection() {
        // if (this.loginIn) {
        let params = new URLSearchParams()
        params.append('songId', this.id)
        params.append('userId', this.userId)
        console.log(params)
        setCollect(params)
          .then(res => {
            if (res.code == 1) {
              this.$store.commit('setIsActive', true)
              this.notify(res.msg, 'success')
            } else if (res.code == 2) { //取消收藏
              this.$store.commit('setIsActive', false)
              this.notify(res.msg, 'success')
            } else {
              this.notify(res.msg, 'warning')
            }
          })
          .catch(err => {
            console.log(err)
          })
        // } else {
        //   this.notify('请先登录', 'warning')
        // }
      }
    }
  }
</script>

<style lang="scss" scoped>
@import '../assets/css/play-bar.scss';
</style>
