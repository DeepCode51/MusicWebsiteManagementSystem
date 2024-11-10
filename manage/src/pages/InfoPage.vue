<template>
  <div>
    <el-row :gutter="20" class="mgb20">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-1">
            <div class="grid-cont-right">
              <div class="grid-num">{{ userCount }}</div>
              <div>用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-2">
            <div class="grid-cont-right">
              <div class="grid-num">{{ songCount }}</div>
              <div>歌曲总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-3">
            <div class="grid-cont-right">
              <div class="grid-num">{{ singerCount }}</div>
              <div>歌手数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-4">
            <div class="grid-cont-right">
              <div class="grid-num">{{ songListCount }}</div>
              <div>歌单数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <h3 style="margin-bottom: 20px">用户年龄比例</h3>
        <div style="background-color: white">
          <ve-pie :data="userAge" :theme="options"></ve-pie>
        </div>
      </el-col>
      <el-col :span="12">
        <h3 style="margin-bottom: 20px">歌单风格分布</h3>
        <div style="background-color: white">
          <ve-histogram :data="songStyle" :theme="options3"></ve-histogram>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <h3 style="margin: 20px 0">歌手类型比例</h3>
        <div style="background-color: white">
          <ve-pie :data="singerSex" :theme="options1"></ve-pie>
        </div>
      </el-col>
      <el-col :span="12">
        <h3 style="margin: 20px 0">歌手国籍分布</h3>
        <div style="background-color: white">
          <ve-histogram :data="country" :theme="options2"></ve-histogram>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mixin } from '../mixins/index'
import { getAllSinger, getSongList, getAllSong, getAllUser } from '../api/index'

export default {
  mixins: [mixin],
  data() {
    return {
      user: [],
      userAge: {
        columns: ['年龄段', '总数'],
        rows: [
          { '年龄段': '16-26岁', '总数': 0 },
          { '年龄段': '27-37岁', '总数': 0 },
          { '年龄段': '38-48岁', '总数': 0 },
          { '年龄段': '49-59岁', '总数': 0 },
          { '年龄段': '59岁以上', '总数': 0 },
        ]
      },
      singerSex: {
        columns: ['性别', '总数'],
        rows: [
          { '性别': '男', '总数': 0 },
          { '性别': '女', '总数': 0 },
          { '性别': '组合', '总数': 0 },
          { '性别': '不明', '总数': 0 }
        ]
      },
      country: {
        columns: ['国家', '总数'],
        rows: [
          { '国家': '中国', '总数': 0 },
          { '国家': '韩国', '总数': 0 },
          { '国家': '意大利', '总数': 0 },
          { '国家': '新加坡', '总数': 0 },
          { '国家': '美国', '总数': 0 },
          { '国家': '马来西亚', '总数': 0 },
          { '国家': '西班牙', '总数': 0 },
          { '国家': '日本', '总数': 0 },
          { '国家': '加拿大', '总数': 0 }
        ]
      },
      options: {
        color: ['#A7E8BD', '#FFC0CB', '#2694ab', '#a696c8', '#F9DEC9']
      },
      options1: {
        color: ['#FFCAD4', '#B0D0D3', '#C08497', '#F7AF9D']
      },
      options2: {
        color: ['#FFA544'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        }
      },
      options3: {
        color: ['#84CF96'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        }
      },
      songStyle: {
        columns: ['风格', '总数'],
        rows: [
          { '风格': '华语', '总数': 0 },
          { '风格': '粤语', '总数': 0 },
          { '风格': '欧美', '总数': 0 },
          { '风格': '日韩', '总数': 0 },
          { '风格': 'BGM', '总数': 0 },
          { '风格': '轻音乐', '总数': 0 },
          { '风格': '乐器', '总数': 0 }
        ]
      },
      userCount: 0,
      songCount: 0,
      singerCount: 0,
      songListCount: 0
    }
  },
  mounted() {
    this.getUser()
    this.getSinger()
    this.getSong()
    this.getSongList()
  },
  methods: {
    getUser() {
      getAllUser().then(res => {
        this.userCount = res.length
        // console.log(res)
        let curYear = new Date().getFullYear()
        for (let i in res) {
          let value = curYear - new Date(res[i].birth).getFullYear()
          if (value > 59) {
            this.userAge.rows[4]['总数']++
          } else if (value < 60 && value > 48) {
            this.userAge.rows[3]['总数']++
          } else if (value < 49 && value > 37) {
            this.userAge.rows[2]['总数']++
          } else if (value < 37 && value > 26) {
            this.userAge.rows[1]['总数']++
          } else {
            this.userAge.rows[0]['总数']++
          }
        }
        for (let j = 0; j <= 4; j++) {
          if (this.userAge.rows[j]['总数'] == 0) {
            this.userAge.rows[j] = ''
            console.log(this.userAge.rows[j])
          }
        }
      })

    },
    getCountry(val) {
      for (let item of this.country.rows) {
        if (val.includes(item['国家'])) {
          item['总数']++
          break
        }
      }
    },
    getStyle(val) {
      for (let item of this.songStyle.rows) {
        if (val.includes(item['风格'])) {
          item['总数']++
        }
      }
    },
    setSex(sex, arr) {
      let count = 0
      for (let item of arr) {
        if (sex === item.sex) {
          count++
        }
      }
      return count
    },
    getSinger() {
      getAllSinger().then(res => {
        this.singerCount = res.length
        this.singerSex.rows[0]['总数'] = this.setSex(1, res)
        this.singerSex.rows[1]['总数'] = this.setSex(0, res)
        this.singerSex.rows[2]['总数'] = this.setSex(2, res)
        this.singerSex.rows[3]['总数'] = this.setSex(3, res)
        for (let item of res) {
          this.getCountry(item.location)
        }
        for (let j = 0; j < 4; j++) {
        if (this.singerSex.rows[j]['总数'] == 0) {
          this.singerSex.rows[j] = ''
          console.log(this.singerSex.rows[j])
        }
      }
      }).catch(err => {
        console.log(err)
      })
    },
    getSong() {
      getAllSong().then(res => {
        this.songCount = res.length
      }).catch(err => {
        console.log(err)
      })
    },
    getSongList() {
      getSongList().then(res => {
        this.songListCount = res.length
        for (let item of res) {
          this.getStyle(item.style)
        }
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style>
.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}
</style>
