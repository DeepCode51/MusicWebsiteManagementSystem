import Vue from 'vue'
import Router from 'vue-router'
import LoginIn from '@/pages/LoginIn'
import SignUp from '@/pages/SignUp'
import Home from '@/pages/Home'
import SongList from '@/pages/SongList'
import Singer from '@/pages/Singer'
import MyMusic from '@/pages/MyMusic'
import SongListAlbum from '@/pages/SongListAlbum'
import SingerAlbum from '@/pages/SingerAlbum'
import Search from '@/pages/Search'
import Setting from '@/pages/Setting'
import Lyric from '@/pages/Lyric'
import OrderList from '@/pages/OrderList'
import Pay from '@/pages/Pay'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base:'/music-user',
  routes: [
    {
      path: '*',
      redirect: '/404'
    },
    {
      path: '/404',
      component: resolve => require(['../pages/404.vue'], resolve)
    },
    {
      path: '/',
      name: 'login-in',
      component: LoginIn,
      meta:{
        keepAlive: false
      }
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: SignUp,
      meta:{
        keepAlive: false
      }
    },
    {
      path: '/home',
      name: 'home',
      component: Home,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/song-list',
      name: 'song-list',
      component: SongList,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/my-music',
      name: 'my-music',
      component: MyMusic,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/song-list-album/:id',
      name: 'song-list-album',
      component: SongListAlbum,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/singer',
      name: 'singer',
      component: Singer,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/singer-album/:id',
      name: 'singer-album',
      component: SingerAlbum,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/lyric/:id',
      name: 'lyric',
      component: Lyric,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/order-list',
      name: 'order-list',
      component: OrderList,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/pay/:id',
      name: 'pay',
      component: Pay,
      meta:{
        keepAlive: false,requireAuth:true
      }
    },
    {
      path: '/search',
      name: 'search',
      component: Search,
      meta:{
        keepAlive: true,requireAuth:true
      }
    },
    {
      path: '/setting',
      name: 'setting',
      component: Setting,requireAuth:true
    },
  ],
  scrollBehavior (to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
})

//登录拦截
router.beforeEach((to,from,next)=>{
  if (to.meta.requireAuth) {//requireAuth若为true则该路由需要判断是否登录
    if (localStorage.userName) {//判断当前的userName数据是否存在
      next();
    }
    else {
      next({//返回登录页面
        path: '/',
        query: {redirect: to.fullPath}
      })
    }
  }
  else {
    next();
  }
})

export default router