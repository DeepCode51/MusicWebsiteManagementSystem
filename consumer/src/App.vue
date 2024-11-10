<template>
  <div id="app">
    <song-audio/>
    <the-header/>
    <router-view class="music-content"/>
    <div v-if="$route.meta.keepAlive">
      <the-aside/>
      <play-bar/>
    </div>
    <!-- <router-view v-if="!$route.meta.keepAlive"></router-view> -->
    <scroll-top/>
    <the-footer/>
  </div>
</template>

<script>
import ScrollTop from './components/ScrollTop'
import SongAudio from './components/SongAudio'
import TheHeader from './components/TheHeader'
import TheFooter from './components/TheFooter'
import PlayBar from './components/PlayBar'
import TheAside from './components/TheAside'

export default {
  name: 'App',
  components: {
    ScrollTop,
    SongAudio,
    TheHeader,
    TheFooter,
    TheAside,
    PlayBar
  }
}
</script>

<style  lang="scss" scoped>
@import './assets/css/app.scss';
</style>
