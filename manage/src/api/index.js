/* eslint-disable*/
import axios from 'axios'
import { get, post, deletes } from './http'

// =======================> 管理员 API
// 是否登录成功
export const getLoginStatus = (params) => post(`admin/login/status`, params)


// =======================> 用户 API
// 返回所有用户
export const getAllUser = () => get(`user/allUser`)
// 返回指定ID的用户
export const getUserOfId = (id) => get(`user/detail?id=${id}`)
// 添加用户
export const setUser = (params) => post(`user/addUser`, params)
// 更新用户信息
export const updateUser = (params) => post(`user/updateUser`, params)
// 修改用户权限
export const updatePermission = (params) => post(`user/updatePermission`, params)
// 删除用户
export const deleteUser = (id) => get(`user/deleteUser?id=${id}`)


// =======================> 收藏列表 API
// 返回的指定用户ID收藏列表
export const getCollectionOfUser = (userId) => get(`collect/collectOfUserId?userId=${userId}`)
// 删除收藏的歌曲
export const deleteCollection = (userId, songId) => get(`collect/delete?userId=${userId}&&songId=${songId}`)

// =======================> 评论列表 API
// 获得指定歌曲ID的评论列表
export const getCommentOfSongId = (songId) => get(`comment/song/detail?songId=${songId}`)
// 获得指定歌单ID的评论列表
export const getCommentOfSongListId = (songListId) => get(`comment/songList/detail?songListId=${songListId}`)
// 更新评论
export const updateCommentMsg = (params) => post(`comment/update`, params)
// 删除评论
export const deleteComment = (id) => get(`comment/delete?id=${id}`)
// 获取所有评论
export const getAllComment = () => get(`comment/allComment`)
// 查询举报原因
export const getReason = (commentId,optionType) => get(`comment/updownOfComment?commentId=${commentId}&&optionType=${optionType}`)
// 删除举报原因
export const deleteReason = (id,commentId) => get(`comment/deleteDown?id=${id}&&commentId=${commentId}`)
// 修改评论是否显示
export const updateIsShow = (params) => post(`comment/updateIsShow`, params)

// =======================> 歌手 API
// 返回所有歌手
export const getAllSinger = () => get(`singer/allSinger`)
// 添加歌手
export const setSinger = (params) => post(`singer/addSinger`, params)
// 更新歌手信息
export const updateSingerMsg = (params) => post(`singer/updateSinger`, params)
// 删除歌手
export const deleteSinger = (id) => get(`singer/deleteSinger?id=${id}`)


// =======================> 歌曲 API
// 返回所有歌曲
export const getAllSong = () => get(`song/allSong`)
// 返回指定歌手ID的歌曲
export const getSongOfSingerId = (id) => get(`song/singer/detail?singerId=${id}`)
// 返回指定歌手名的歌曲
// export const getSongOfSingerName = (name) => get(`song/singerName/detail?name=${name}`)
// 更新歌曲信息
export const updateSong = (params) => post(`song/updateSong`, params)
// 删除歌曲
export const deleteSong = (id) => get(`song/delete?id=${id}`)
// 返回的指定用户ID收藏列表,根据歌曲id返回
export const getSongOfId = (id) => get(`song/detail?songId=${id}`)
// 返回指定歌名的歌曲
export const songOfSongName = (songName) => get(`song/songOfSongName?songName=${songName}`)
// 查询对应歌名和歌手是否存在
// export const songOfdetails = (songName,singerName) => get(`song/songOfdetails?songName=${songName}&singerName=${singerName}`)
// 下载音乐
export const download = (url) => axios({
    method: 'get',
    url: url,
    responseType: 'blob'
  })
// 根据歌手删除歌曲
export const deleteSongBySinger = (singerId) => get(`song/deleteBySingerId?singerId=${singerId}`)


// =======================> 歌单 API
// 添加歌单
export const setSongList = (params) => post(`songList/add`, params)
// 获取全部歌单
export const getSongList = () => get(`songList/allSongList`)
// 更新歌单信息
export const updateSongList = (params) => post(`songList/update`, params)
// 删除歌单
export const deleteSongList = (id) => get(`songList/delete?id=${id}`)
// 返回指定歌单id
export const getSongListOfId = (id) => get(`songList/detail?songListId=${id}`)

// 获取指定歌单的平均分
export const getRanksOfSongListId = (songListId) => get(`ranks/score?songListId=${songListId}`)


// =======================> 歌单歌曲 API
// 给歌单添加歌曲
export const listSongAdd = (params) => post(`listSong/add`, params)
// 返回歌单里指定歌单ID的歌曲
export const listSongDetail = (songListId) => get(`listSong/detail?songListId=${songListId}`)
// 删除歌单里的歌曲
export const delListSong = (songId,songListId) => get(`listSong/delete?songId=${songId}&songListId=${songListId}`)

// ==========订单API
// 返回所有订单
export const getAllOrder = () => get(`orderList/all`)