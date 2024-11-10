import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: '/music-manage',
  routes: [
    {
      path: '/Home',
      component: resolve => require(['../components/Home.vue'], resolve),
      meta: { title: '自述文件', requireAuth: true },
      children: [
        {
          path: '/Info',
          component: resolve => require(['../pages/InfoPage.vue'], resolve),
          meta: { title: 'Info', requireAuth: true }
        },
        {
          path: '/Song',
          component: resolve => require(['../pages/SongPage.vue'], resolve),
          meta: { title: 'Song', requireAuth: true }
        },
        {
          path: '/Singer',
          component: resolve => require(['../pages/SingerPage.vue'], resolve),
          meta: { title: 'Singer', requireAuth: true }
        },
        {
          path: '/SongList',
          component: resolve => require(['../pages/SongListPage.vue'], resolve),
          meta: { title: 'SongList', requireAuth: true }
        },
        {
          path: '/ListSong',
          component: resolve => require(['../pages/ListSongPage.vue'], resolve),
          meta: { title: 'ListSong', requireAuth: true }
        },
        {
          path: '/Comment',
          component: resolve => require(['../pages/CommentPage.vue'], resolve),
          meta: { title: 'Comment', requireAuth: true }
        },
        {
          path: '/User',
          component: resolve => require(['../pages/UserPage.vue'], resolve),
          meta: { title: 'User', requireAuth: true }
        },
        {
          path: '/Collect',
          component: resolve => require(['../pages/CollectPage.vue'], resolve),
          meta: { title: 'Collect', requireAuth: true }
        },
        {
          path: '/OrderList',
          component: resolve => require(['../pages/OrderList.vue'], resolve),
          meta: { title: 'OrderList', requireAuth: true }
        },
        {
          path: '/Down',
          component: resolve => require(['../pages/DownReason.vue'], resolve),
          meta: { title: 'Down', requireAuth: true }
        }
      ]
    },
    {
      path: '/',
      component: resolve => require(['../pages/Login.vue'], resolve)
    }
  ]
})

//登录拦截
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {//requireAuth若为true则该路由需要判断是否登录
    if (localStorage.userName) {//判断当前的userName数据是否存在
      next()
    }
    else {
      next({//返回登录页面
        path: '/',
        query: { redirect: to.fullPath }
      })
    }
  }
  else {
    next()
  }
})

export default router