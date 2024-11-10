/* eslint-disable*/
import axios from 'axios'
import { get, post } from './http'

// =======================> 用户 API
// 返回所有用户
export const getAllUser = () => get(`user/allUser`)
// 返回指定ID的用户
export const getUserOfId = (id) => get(`user/selectByPrimaryKey?id=${id}`)
// 添加用户
export const setUser = (params) => post(`user/addUser`, params)
// 更新用户信息
export const updateUser = (params) => post(`user/updateUser`, params)
// 更新用户密码
export const updatePass = (params) => post(`user/updatePass`, params)
// 注册
export const SignUp = (params) => post(`user/addUser`, params)
// 登录
export const loginIn = (params) => post(`user/login`, params)


// =======================> 歌手 API
// 返回所有歌手
export const getAllSinger = () => get(`singer/allSinger`)
// 根据歌手类型查询
export const getSingerOfSex = (sex) => get(`singer/singerOfSex?sex=${sex}`)


// =======================> 歌曲 API
// 返回所有歌曲
export const getAllSong = () => get(`song/allSong`)
// 返回指定歌手ID的歌曲
export const getSongOfSingerId = (id) => get(`song/singer/detail?singerId=${id}`)
// 返回指定歌手名的歌曲
export const getSongOfSingerName = (name) => get(`song/singerName/detail?name=${name}`)
// 返回的指定用户ID收藏列表,根据歌曲id返回
export const getSongOfId = (id) => get(`song/detail?songId=${id}`)
// 返回指定歌名的歌曲
export const songOfSongName = (songName) => get(`song/songOfSongName?songName=${songName}`)
// 根据歌名模糊查询
export const likeSongName = (keywords) => get(`song/likeSongName?songName=${keywords}`)
// 更新歌曲播放次数
export const setSongClicks = (params) => post(`song/updateClick`,params)


// =======================> 歌单 API
// 获取全部歌单
export const getSongList = () => get(`songList/allSongList`)
// 返回标题包含文字的歌单列表
export const getSongListLikeTitle = (keywords) => get(`songList/likeTitle?title=${keywords}`)
// 根据类型查询
export const getSongListLikeStyle = (keywords) => get(`songList/likeStyle?style=${keywords}`)


// =======================> 歌单歌曲 API
// 返回歌单里指定歌单ID的歌曲
export const listSongDetail = (songListId) => get(`listSong/detail?songListId=${songListId}`)

// 下载音乐
export const download = (url) => axios({
    method: 'get',
    url: url,
    responseType: 'blob'
  })

// =======================评分====================
// 提交评分
export const setRanks = (params) => post(`ranks/add`,params)
// 获取指定歌单的平均分
export const getRanksOfSongListId = (songListId) => get(`ranks/score?songListId=${songListId}`)

// =======================评论====================
// 提交评论
export const setComment = (params) => post(`comment/add`,params)
// 点赞
export const setLike = (params) => post(`comment/like`,params)
// 返回当前歌单、歌曲下的评论
export const getShowComment = (type,id) =>{
  if (type == 0){ //歌曲
    return get(`comment/commentOfSongId?songId=${id}`)
  } else { //歌单
    return get(`comment/commentOfSongListId?songListId=${id}`)
  }
}
// 查询用户是否点赞/举报
export const getUdOfUserId = (userId) =>  get(`comment/updownOfUserId?userId=${userId}`)
// 举报
export const setDown = (params) => post(`comment/down`,params)

//==========================收藏=====================
// 添加收藏
export const setCollect = (params) => post(`collect/add`,params)
// 获取用户收藏
export const getCollectOfUserId = (userId) => get(`collect/collectOfUserId?userId=${userId}`)

// ====================订单=========================
export const setOrder = (params) => post(`orderList/add`,params)
// 获取用户订单
export const getOrderOfUserId = (userId) => get(`orderList/ByUserId?userId=${userId}`)
// 根据主键获取订单
export const getOrderOfId = (id) => get(`orderList/detail?id=${id}`)
// 提交付费
export const setIspay = (params) => post(`orderList/update`,params)