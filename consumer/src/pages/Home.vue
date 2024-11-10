<template>
  <div class="home">
    <!--轮播图-->
    <swiper />
    <!--推荐歌单/歌手/歌曲-->
    <div class="section" v-for="(item, index) in songsList" :key="index">
      <div class="section-title">{{ item.name }}</div>
      <content-list :contentList="item.list" :contentList1="item.list1"></content-list>
    </div>
    <!-- <div class="songs">
      <div class="title">最新歌曲</div>
      <ul>
        <li class="list-title">
          <div class="song-item">
            <span class="item-title">歌名</span>
            <span class="item-name">歌手</span>
            <span class="item-intro">专辑</span>
          </div>
        </li>
        <li class="list-content" v-for="(item, index) in songList" :key="index">
          <div class="song-item"
            @click="toplay(item.id, item.url, item.pic, index, item.name, item.lyric, item.clickTimes)">
            <span class="item-title">{{ replaceFName(item.name) }}</span>
            <span class="item-name">{{ replaceLName(item.name) }}</span>
            <span class="item-intro">{{ item.introduction }}</span>
          </div>
        </li>
      </ul>
    </div>
    <div class="songs">
      <div class="title">最热歌曲</div>
      <ul>
        <li class="list-title">
          <div class="song-item">
            <span class="item-title">歌名</span>
            <span class="item-name">歌手</span>
            <span class="item-intro">专辑</span>
          </div>
        </li>
        <li class="list-content" v-for="(item, index) in songList1" :key="index">
          <div class="song-item"
            @click="toplay(item.id, item.url, item.pic, index, item.name, item.lyric, item.clickTimes)">
            <span class="item-title">{{ replaceFName(item.name) }}</span>
            <span class="item-name">{{ replaceLName(item.name) }}</span>
            <span class="item-intro">{{ item.introduction }}</span>
          </div>
        </li>
      </ul>
    </div> -->
  </div>
</template>

<script>
import Swiper from '../components/Swiper'
import ContentList from '../components/ContentList.vue'
import { mixin } from '../mixins'
import { getSongList, getAllSinger, getRanksOfSongListId, getAllSong } from '../api/index'

export default {
  name: 'home',
  mixins: [mixin],
  components: {
    Swiper,
    ContentList
  },
  data() {
    return {
      songsList: [
        { name: '推荐歌单', list: [] },
        { name: '推荐歌手', list: [] },
        { name: '最新歌曲', list1: [] },
        { name: '最热歌曲', list1: [] },
      ],
      songsList1: []
    }
  },
  created() {
    // 获取歌单列表
    this.getSongList('songList')
    // 获取歌手列表
    this.getSinger('singer')
    // 获取歌曲列表
    this.getSong('song')
  },
  methods: {
    getSongList() {
      // var arr = []
      getSongList()
        .then(res => {
          // for (let item of res) {
          //   // 获取评分
          //   getRanksOfSongListId(item.id)
          //     .then(res1 => {
          //       item['ranks'] = res1
          //     })

          // } 
          // console.log(res)
          // res = res.sort(function (a, b) { // 对sort方法进行重写，本质是冒泡排序
          //   return b['ranks'] - a['ranks']
          // })
          // console.log(res)
          this.songsList[0].list = res.slice(0, 10)
        })
        .catch(err => {
          console.log(err)
        })
    },
    getSinger() {
      getAllSinger().then(res => {
        this.songsList[1].list = res.slice(0, 10)
      }).catch(err => {
        console.log(err)
      })
    },
    getSong() {
      getAllSong().then(res => {
        this.songsList[2].list1 = res.slice(0, 10)
        res = res.sort(function (a, b) { // 对sort方法进行重写，本质是冒泡排序
          return b['clickTimes'] - a['clickTimes']
        })
        this.songsList[3].list1 = res.slice(0, 10)
      }).catch(err => {
        console.log(err)
      })
    }
  },
}
</script>

<style lang="scss" scoped>
@import '../assets/css/home.scss';
</style>
