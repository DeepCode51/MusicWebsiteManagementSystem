<template>
  <div class="setting">
    <div class="leftCol">
      <div class="settingsMainHeader">设置</div>
      <ul class="setting-aside">
        <li v-for="(item, index) in settingList" :key="index" :class="{activeColor: activeName === item.name}" @click="handleClick(item)">
          {{item.name}}
        </li>
      </ul>
    </div>
    <div class="contentCol">
      <component :is="componentSrc"></component>
    </div>
  </div>
</template>

<script>
import Info from '../components/Info'
import Upload from '../components/Upload'
import Pass from '../components/Pass'

export default {
  name: 'setting',
  components: {
    Info,
    Upload,
    Pass
  },
  data () {
    return {
      activeName: '个人信息',
      componentSrc: 'Info',
      settingList: [{
        icon: '',
        name: '个人信息',
        path: 'Info'
      }, {
        icon: '',
        name: '修改头像',
        path: 'Upload'
      }, {
        icon: '',
        name: '修改密码',
        path: 'Pass'
      }]
    }
  },
  mounted() {
    this.changeIndex('个人信息')
  },
  methods: {
    changeIndex(value) {
      this.$store.commit('setActiveName', value)
    },
    handleClick (item) {
      this.activeName = item.name
      this.componentSrc = item.path
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/setting.scss';
</style>
