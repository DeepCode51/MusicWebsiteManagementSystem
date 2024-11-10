<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      :default-active="onRoutes"
      :collapse="collapse"
      background-color="#f6d04d"
      text-color="#ffffff"
      active-text-color="#de4307"
      unique-opened
      router
    >
      <template v-for="item in items">
          <template>
            <el-menu-item :index="item.index" :key="item.index">
              <i style="font-size: 20px;" :class="item.icon"></i>
              <span slot="title" style="font-size: 18px;">{{ item.title }}</span>
            </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import bus from '../assets/js/bus'

export default {
  data () {
    return {
      collapse: false,
      items: [
        {
          icon: 'el-icon-location',
          index: 'info',
          title: '系统首页'
        },
        {
          icon: 'el-icon-location',
          index: 'user',
          title: '用户管理'
        },
        {
          icon: 'el-icon-location',
          index: 'singer',
          title: '歌手管理'
        },
        {
          icon: 'el-icon-location',
          index: 'song',
          title: '歌曲管理'
        },
        {
          icon: 'el-icon-location',
          index: 'songList',
          title: '歌单管理'
        },
        {
          icon: 'el-icon-location',
          index: 'orderList',
          title: '订单管理'
        },
        {
          icon: 'el-icon-location',
          index: 'comment',
          title: '评论管理'
        }
      ]
    }
  },
  computed: {
    onRoutes () {
      return this.$route.path.replace('/', '')
    }
  },
  created () {
    // 通过 Event Bus 进行组件间通信，来折叠侧边栏
    bus.$on('collapse', msg => {
      this.collapse = msg
    })
  }
}
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  background-color: #e9a54c;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
  width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
  width: 150px;
}
.sidebar > ul {
  height: 100%;
}
</style>
