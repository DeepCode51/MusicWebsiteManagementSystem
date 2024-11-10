<template>
  <div class="table">
    <div class="crumbs" v-if="this.singerId != null">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-tickets"></i> 歌曲信息
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="small" class="handle-del mr10" @click="delAll"
          style="background-color: #de4307; border: 0px; font-size: 15px;">批量删除</el-button>
        <el-input v-model="select_word" size="small" placeholder="请输入歌名" class="handle-input mr10"></el-input>
        <el-button type="primary" size="small" @click="centerDialogVisible = true"
          style="background-color: #8bc24c; border: 0px; font-size: 15px;">添加歌曲</el-button>
        <!-- <el-button type="primary" size="small" class="handle-del mr10" @click="addAll"
          style="background-color: #de4307; border: 0px; font-size: 15px;">添加至歌单</el-button> -->
      </div>
      <el-table :data="data" size="mini" border style="width: 100%" ref="multipleTable" height="550px"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <!-- <el-table-column label="时间" prop="createTime" v-if="false"  sortable :default-sort="{ prop: 'createTime', order: 'descending' }"></el-table-column> -->
        <el-table-column label="歌曲图片" width="110" align="center">
          <template slot-scope="scope">
            <div style="width: 80px; height: 80px; overflow: hidden">
              <img :src="getUrl(scope.row.pic)" alt="" style="width: 100%;" />
            </div>
            <div class="play" @click="setSongUrl(scope.row.url, scope.row.name)">
              <div v-if="toggle !== scope.row.name">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-bofanganniu"></use>
                </svg>
              </div>
              <div v-if="toggle === scope.row.name">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-zanting"></use>
                </svg>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="歌名" prop="name" width="200" align="center">
          <template slot-scope="scope">
            <div>{{ replaceFName(scope.row.name) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="歌手" prop="name" width="150" align="center" v-if="this.singerId == null">
          <template slot-scope="scope">
            <div>{{ replaceLName(scope.row.name) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="价格/(元)" prop="price" width="150" align="center" sortable
          :default-sort="{ prop: 'price', order: 'ascending' }"></el-table-column>
        <el-table-column label="专辑" prop="introduction" width="150" align="center"></el-table-column>
        <el-table-column label="主题" prop="theme" width="100" align="center"></el-table-column>
        <el-table-column label="歌词" align="center">
          <template slot-scope="scope">
            <ul style="height: 100px; overflow: scroll">
              <li v-for="(item, index) in parseLyric(scope.row.lyric)" :key="index">
                {{ item }}
              </li>
            </ul>
          </template>
        </el-table-column>
        <el-table-column label="播放量" prop="clickTimes" width="100" align="center" sortable
          :default-sort="{ prop: 'clickTimes', order: 'ascending' }"></el-table-column>

        <el-table-column label="资源更新" width="100" align="center">
          <template slot-scope="scope">
            <el-upload class="upload-demo" :action="uploadUrl(scope.row.id)" :show-file-list="false"
              :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
              <el-button size="mini">更新图片</el-button>
            </el-upload>
            <br>
            <el-upload class="upload-demo change" :action="uploadSongUrl(scope.row.id)" :show-file-list="false"
              :on-success="handleSongSuccess" :before-upload="beforeSongUpload">
              <el-button size="mini">更新歌曲</el-button>
            </el-upload>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
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

    <!--添加歌曲弹出框-->
    <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form action="" :model="registerForm" id="tf">
        <div>
          <label>歌名</label>
          <el-input type="text" name="name"></el-input>
        </div>
        <div v-if="this.singerId == null">
          <label>歌手名</label>
          <el-select v-model="registerForm.singerName" placeholder="歌手名字">
            <el-option v-for="item in singerList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </div>
        <div>
          <label>专辑</label>
          <el-input type="text" name="introduction"></el-input>
        </div>
        <div>
          <label>价格</label>
          <br>
          <el-input type="text" name="price" style="width: 200px; line-height: 50px;" :min="0"
            onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,1})?).*$/g, '$1')"></el-input>&nbsp;元
        </div>
        <div>
          <label>主题</label>
          <el-input type="text" name="theme"></el-input>
        </div>
        <div>
          <label>歌词</label>
          <el-input type="textarea" name="lyric"></el-input>
        </div>
        <div>
          <label>歌曲上传</label>
          <br>
          <input type="file" name="file" id="upadte-file-input">
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addSong">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" :visible.sync="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="歌名" size="mini">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="专辑" size="mini">
          <el-input v-model="form.introduction"></el-input>
        </el-form-item>
        <el-form-item label="主题" size="mini">
          <el-input v-model="form.theme"></el-input>
        </el-form-item>
        <el-form-item label="价格/(元)" size="mini">
          <el-input v-model="form.price" style="width: 200px;"></el-input>&nbsp;元
        </el-form-item>
        <el-form-item label="歌词" size="mini">
          <el-input type="textarea" v-model="form.lyric"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="editSave">确 定</el-button>
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

    <!-- 添加提示框
   <el-dialog title="添加歌曲" :visible.sync="addToSongList" width="400px" center>
      <el-form action="" :model="songList" id="tf">
        <el-form-item prop="songList" label="歌单名" size="mini">
          <el-select v-model="songList.songListId" placeholder="请选择歌单">
            <el-option v-for="item in songLists" :key="item.id" :label="item.title" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addToSongList = false">取 消</el-button>
        <el-button type="primary" @click="addSongToList">确 定</el-button>
      </span> -->
    <!-- </el-dialog> -->
  </div>
</template>

<script>
import { mixin } from '../mixins/index'
import { mapGetters } from 'vuex'
import '@/assets/js/iconfont.js'
import { deleteSong, getAllSinger, getAllSong, getSongList, getSongOfSingerId, updateSong } from '../api/index'

export default {
  mixins: [mixin],
  data() {
    return {
      toggle: false, // 控制播放图标状态
      singerId: '',
      singerName: '',
      singerList: [],
      registerForm: {
        name: '',
        singerName: '',
        price: '',
        introduction: '',
        lyric: '',
        theme: ''
      },
      tableData: [],
      tempDate: [],
      is_search: false,
      multipleSelection: [], // 记录要删除的歌曲
      centerDialogVisible: false,
      editVisible: false,
      delVisible: false,
      addToSongList: false,
      select_word: '',
      form: {
        id: '',
        singerId: '',
        name: '',
        introduction: '',
        createTime: '',
        updateTime: '',
        price: '',
        pic: '',
        lyric: '',
        url: '',
        theme: '',
        singerName: ''
      },
      // songList:{
      //   songListId:'',
      // },
      songLists: [],
      pageSize: 5, // 页数
      currentPage: 1, // 当前页
      idx: -1
    }
  },
  computed: {
    ...mapGetters([
      'isPlay' // 播放状态
    ]),

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
          if (item.name.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created() {
    this.singerId = this.$route.query.id
    this.singerName = this.$route.query.name
    this.getData()
  },
  // 离开当前页自动关闭播放
  destroyed() {
    this.$store.commit('setIsPlay', false)
  },
  methods: {
    // 获取所有歌曲
    getData() {
      this.tableData = []
      this.tempDate = []
      // console.log(this.singerId)
      if (this.singerId != null) {
        getSongOfSingerId(this.singerId).then((res) => {
          this.tableData = res
          this.tempDate = res
          this.currentPage = 1
        }).catch(err => {
          console.log(err)
        })
      } else {
        getAllSong().then((res) => {
          this.tableData = res
          this.tempDate = res
          this.currentPage = 1
        }).catch(err => {
          console.log(err)
        })
      }
      this.singerList = []
      getAllSinger().then(res => {
        for (let i in res) {
          this.singerList.push({
            id: res[i].id,
            name: res[i].name,
          })
        }
      })

      this.songLists = []
      getSongList().then(res => {
        for (let j in res) {
          this.songLists.push({
            id: res[j].id,
            title: res[j].title,
          })
        }
      })

    },
    setSongUrl(url, name) {
      this.$store.commit('setUrl', this.$store.state.HOST + url)
      this.toggle = name
      if (this.isPlay) {
        this.$store.commit('setIsPlay', false)
      } else {
        this.$store.commit('setIsPlay', true)
      }
    },
    // 获取当前页
    handleCurrentChange(val) {
      this.currentPage = val
    },
    handleSongSuccess(res, file) {
      if (res.code === 1) {
        this.getData()
        this.notify('上传成功', 'success')
      } else {
        this.notify('上传失败', 'error')
      }
    },

    //添加音乐
    addSong() {
      let _this = this
      var form = new FormData(document.getElementById('tf'))
      if (this.singerId == null) {
        form.append('singerId', this.registerForm.singerName)
        for (let i in this.singerList) {
          if (this.singerList[i].id == this.registerForm.singerName) {
            var singerName = this.singerList[i].name
          }
        }
        var name1 = form.get('name')
        form.set('name', singerName + '-' + form.get('name'))
      } else {
        form.append('singerId', this.singerId)
        var name1 = form.get('name')

        form.set('name', this.singerName + '-' + form.get('name'))
      }
      if (!form.get('introduction')) {
        form.set('introduction', name1)
      }
      if (!form.get('price')) {
        form.set('price', '0')
      }
      if (!form.get('lyric')) {
        form.set('lyric', '[00:00:00]暂无歌词')
      }
      var req = new XMLHttpRequest()
      req.onreadystatechange = function () {
        if (req.readyState === 4 && req.status === 200) {
          let res = JSON.parse(req.response)
          if (res.code == 1) {
            _this.getData()
            _this.registerForm = {}
            _this.notify(res.msg, 'success')
          } else {
            _this.notify(res.msg, 'error')
          }
        }
      }
      req.open('post', `${_this.$store.state.HOST}/song/add`, false)
      req.send(form)
      _this.centerDialogVisible = false
    },

    // 批量添加
    // addAll() {
    //   this.addToSongList = true
    //   for (let item of this.multipleSelection) {
    //     // this.addSongToList(item.id)
    //   }
    //   this.multipleSelection = []
    // },

    // addSongToList(id){
    //   console.log(id)
    //   console.log(this.songList.songListId)
    //   let params = new URLSearchParams()
    //   params.append('songId', id)
    //   params.append('songListId',this.songList.songListId)
    //   listSongAdd(params)
    //     .then(res => {
    //       if (res.code === 1) {
    //         this.getData()
    //         this.notify(res.msg, 'success')
    //         this.addToSongList = false
    //       } else {
    //         this.notify(res.msg, 'error')
    //       }
    //     })
    //     .catch(err => {
    //       console.log(err)
    //     })
    // },

    // 弹出编辑
    handleEdit(row) {
      this.idx = row.id
      this.form = {
        id: row.id,
        name: row.name.split('-')[1],
        theme: row.theme,
        price: row.price,
        introduction: row.introduction,
        lyric: row.lyric,
        singerId: row.singerId
      }
      this.singerName = row.name.split('-')[0]
      this.editVisible = true
    },

    // 保存修改
    editSave() {
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('singerId', this.form.singerId)
      // console.log(this.singerName)
      params.append('name', this.singerName + '-' + this.form.name)
      params.append('price', this.form.price)
      params.append('introduction', this.form.introduction)
      params.append('lyric', this.form.lyric)
      params.append('theme', this.form.theme)

      updateSong(params)
        .then(res => {
          if (res.code == 1) {
            this.getData()
            this.notify(res.msg, 'success')
          } else {
            this.notify(res.msg, 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.editVisible = false
    },

    // 更新歌曲图片
    uploadUrl(id) {
      return `${this.$store.state.HOST}/song/updateSongPic?id=${id}`
    },

    // 更新歌曲url
    uploadSongUrl(id) {
      return `${this.$store.state.HOST}/song/updateSongUrl?id=${id}`
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

    // 确定删除
    deleteRow() {
      deleteSong(this.idx)
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
    },

    // 解析歌词
    parseLyric(text) {
      let lines = text.split('\n')
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
      let result = []

      // 对于歌词格式不对的特殊处理
      if (!(/\[.+\]/.test(text))) {
        return [text]
      }
      for (let item of lines) {
        if (pattern.test(item)) {
          let value = item.replace(pattern, '') // 存歌词
          result.push(value)
        }
      }
      return result
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

.el-input__inner {
  background-color: aqua
}

.play {
  position: absolute;
  z-index: 100;
  width: 80px;
  height: 80px;
  top: 18px;
  left: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.icon {
  width: 2em;
  height: 2em;
  color: #de4307;
  fill: currentColor;
  overflow: hidden;
}

.pagination {
  display: flex;
  justify-content: center;
}

::-webkit-scrollbar {
  /*隐藏滚轮*/
  display: none;
}
</style>
