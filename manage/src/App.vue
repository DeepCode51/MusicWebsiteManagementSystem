<template>
  <div id="app">
    <song-audio />
    <router-view></router-view>
  </div>
</template>

<script>
// 播放器
import SongAudio from './components/SongAudio'

export default {
  name: 'App',
  components: {
    SongAudio
  }
}
</script>

<style>
</style>
