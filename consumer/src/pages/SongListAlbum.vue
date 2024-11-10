<template>
  <div class="song-list-album">
    <div class="album-slide">
      <div class="album-img">
        <img :src=attachImageUrl(tempList.pic) alt="">
      </div>
      <div class="album-info">
        <h2>简介：</h2>
        <span>
          {{tempList.introduction}}
        </span>
      </div>
    </div>
    <div class="album-content">
      <div class="album-title">
        <p>{{tempList.title}}</p>
      </div>
      <!--评分-->
      <div class="album-score">
        <div>
          <h3>歌单评分：</h3>
          <div>
            <el-rate v-model="average" disabled></el-rate>
          </div>
        </div>
        <span>{{average * 2}}</span>
        <div>
          <h3>评分：</h3>
          <div @click="setRanks()">
            <el-rate v-model="ranks" show-text allow-half></el-rate>
          </div>
        </div>
      </div>
      <!--歌曲-->
      <div class="songs-body">
        <album-content :songList="listOfSongs">
          <template slot="title">歌单</template>
        </album-content>
        <comment :playId="songListId" :type="1"></comment>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import AlbumContent from '../components/AlbumContent'
import Comment from '../components/Comment'
import { getSongOfId,listSongDetail,setRanks,getRanksOfSongListId } from '../api/index'

export default {
  name: 'song-list-album',
  components: {
    AlbumContent,
    Comment
  },
  data () {
    return {
      songLists: [],// 当前页面需要展示的歌曲列表
      count: 0, // 点赞数
      songListId: '', // 歌单ID
      average:0, //平均分
      ranks: 0 //评论分
    }
  },
  computed: {
    ...mapGetters([
      'loginIn', // 登录标识
      'tempList', // 单个歌单信息
      'listOfSongs', // 存放的音乐
      'userId', // 用户ID
      'avator' // 用户头像
    ])
  },
  created () {
    this.songListId = this.$route.params.id // 给歌单ID赋值
    // this.singers = this.tempList
    this.getSongId() // 获取歌单里面的歌曲ID
    this.getRanks(this.songListId) // 获取评分
  },
  mixins: [mixin],
  methods: {
    // 获取歌单里面的歌曲列表
    getSongId () {
      listSongDetail(this.songListId)
        .then(res => {
          // 获取歌单里的歌曲信息
          for (let item of res) {
            this.getSongList(item.songId)
          }
          this.$store.commit('setListOfSongs', this.songLists)
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 根据歌曲id获取歌曲信息
    getSongList (id) {
      getSongOfId(id)
        .then(res => {
          this.songLists.push(res)
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 获取评分
    getRanks (id) {
      getRanksOfSongListId(id)
        .then(res => {
          this.average = res / 2
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 提交评分
    setRanks () {
      // if (this.loginIn) {
        let params = new URLSearchParams()
        params.append('songListId', this.songListId)
        params.append('userId', this.userId)
        params.append('score', this.ranks * 2)
        setRanks(params)
          .then(res => {
            if (res.code == 1) {
              this.notify(res.msg, 'success')
              this.getRanks(this.songListId)
            } else {
              this.notify(res.msg, 'error')
            }
          })
          .catch(err => {
            this.notify("您已经评价过了", 'error')
            console.log(err)
          })
      // } else {
      //   this.ranks = null
        // this.notify('请先登录', 'warning')
      // }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/song-list-album.scss';
</style>
