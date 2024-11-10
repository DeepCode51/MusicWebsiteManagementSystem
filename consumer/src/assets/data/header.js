// 左侧导航栏
const navMsg = [{
  name: '首页',
  path: '/home'
}, {
  name: '歌单',
  path: '/song-list'
}, {
  name: '歌手',
  path: '/singer'
}, {
  name: '我的音乐',
  path: '/my-music'
}, {
  name: '订单管理',
  path: '/order-list'
},
]

// 右侧导航栏
const loginMsg = [{
  name: '登录',
  path: '/'
}, {
  name: '注册',
  path: '/sign-up'
}]

// 用户下拉菜单项
const menuList = [{
  name: '设置',
  path: '/setting'
}, {
  name: '退出',
  path: 0,
  isPlay:false
}]

export {
  navMsg,
  loginMsg,
  menuList
}
