<template>
  <div class="table">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-tickets"></i> 歌单歌曲信息
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="small" class="handle-del mr10" @click="delAll"
          style="background-color: #de4307; border: 0px; font-size: 15px;">批量删除</el-button>
        <el-input v-model="select_word" size="small" placeholder="请输入关键字" class="handle-input mr10"></el-input>
        <el-button type="primary" size="small" @click="centerDialogVisible = true"
          style="background-color: #8bc24c; border: 0px; font-size: 15px;">添加歌曲</el-button>
      </div>
      <el-table :data="tableData" border size="mini" style="width: 100%" ref="multipleTable"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column prop="name" label="歌名"> <template slot-scope="scope">
            <div>{{ replaceFName(scope.row.name) }}</div>
          </template></el-table-column>
        <el-table-column prop="name" label="歌手"> <template slot-scope="scope">
            <div>{{ replaceLName(scope.row.name) }}</div>
          </template></el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--添加歌曲-->
    <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" status-icon ref="registerForm" label-width="80px" class="demo-ruleForm">
        <el-form-item prop="singerName" label="歌手" size="mini">
          <el-select v-model="registerForm.singerName" placeholder="歌手名字" @change="getSingerSongs">
            <el-option v-for="item in singerList" :key="item.id" :label="item.name" :value="item.name"></el-option>
          </el-select>
          <!-- <el-input v-model="registerForm.singerName" placeholder="歌手名字"></el-input> -->
        </el-form-item>
        <el-form-item prop="songName" label="歌名" size="mini">
          <el-select v-model="registerForm.songName" placeholder="歌曲名字">
            <el-option v-for="item in songOfSinger" :key="item.id" :label="item.name" :value="item.name"></el-option>
          </el-select>
          <!-- <el-input v-model="registerForm.songName" placeholder="歌曲名字"></el-input> -->
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="getSongId">确 定</el-button>
      </span>
    </el-dialog>

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
import { mixin } from '../mixins/index'
import { listSongDetail, getSongOfId, songOfSongName, delListSong, listSongAdd, getAllSinger, getAllSong } from '../api/index'

export default {
  mixins: [mixin],
  data() {
    return {
      registerForm: {
        singerName: '',
        songName: ''
      },
      tableData: [],
      tempData: [],
      multipleSelection: [], //已经打钩的选项
      centerDialogVisible: false, //添加弹窗是否显示
      delVisible: false,
      select_word: '',
      idx: -1,
      songListId: '',
      songLists: [],
      singerList: [],
      songOfSinger: []
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
    this.songListId = this.$route.query.id
    this.getData()
    this.getSList()
  },

  methods: {
    // 获取歌单
    getData() {
      this.tableData = []
      this.tempData = []
      listSongDetail(this.songListId).then(res => {
        // console.log(res)
        for (let item of res) {
          this.getSong(item.songId)
        }
      })
        .catch(err => {
          console.log(err)
        })
    },

    getSList() {
      getAllSinger().then(res => {
        for (let i in res) {
          this.singerList.push({
            id: res[i].id,
            name: res[i].name,
          })
        }
      }),
        getAllSong().then(res => {
          for (let i in res) {
            this.songLists.push({
              id: res[i].id,
              name: res[i].name.split('-')[1],
              singerId: res[i].singerId
            })
          }
        })
    },

    getSingerSongs() {
      const roles = []
      this.songOfSinger = []
      var _this = this
      var obj = {}
      obj = this.singerList.find(function (i) {
        return i.name == _this.registerForm.singerName
      });
      //在change中获取到整条对象数据
      // console.log(obj.id)
      for (let i in this.songLists) {
        if (this.songLists[i].singerId == obj.id) {
          roles.push({
            id: this.songLists[i].id,
            name: this.songLists[i].name,
            singerId: this.songLists[i].singerId
          })
        }
      }
      this.songOfSinger = roles
    },

    // 获取歌单里对应的音乐 歌曲id将数据放到tableData和tempData
    getSong(id) {
      getSongOfId(id)
        .then(res => {
          this.tableData.push(res)
          this.tempData.push(res)
        })
        .catch(err => {
          console.log(err)
        })
    },

    // 获取要添加歌曲的ID
    getSongId() {
      let _this = this
      var songName = _this.registerForm.singerName + '-' + _this.registerForm.songName
      // var songName = _this.registerForm.songName
      songOfSongName(songName).then(res => {
        _this.addSong(res[0].id)
      }).catch(err => {
        this.notify('歌手歌名不配', 'error')
        console.log(err)
      })
    },
    // 添加歌曲
    addSong(id) {
      let _this = this
      let params = new URLSearchParams()
      params.append('songId', id)
      params.append('songListId', this.$route.query.id)
      listSongAdd(params)
        .then(res => {
          if (res.code === 1) {
            this.getData()
            this.notify(res.msg, 'success')
          } else {
            this.notify(res.msg, 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      _this.registerForm = []
      _this.centerDialogVisible = false
    },

    // 删除一首歌
    deleteRow() {
      delListSong(this.idx, this.songListId)
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
</style>
