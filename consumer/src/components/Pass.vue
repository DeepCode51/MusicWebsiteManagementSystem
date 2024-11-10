<template>
    <div class="info">
        <p class="title">修改密码</p>
        <hr />
        <div class="personal">
            <el-form :model="registerForm" status-icon :rules="rules" class="demo-ruleForm" label-width="80px">
                <el-form-item prop="password" label="旧密码">
                    <el-input type="password" placeholder="旧密码" v-model="registerForm.password" show-password></el-input>
                </el-form-item>
                <el-form-item prop="newpassword" label="新密码">
                    <el-input type="password" placeholder="新密码" v-model="registerForm.newpassword" show-password></el-input>
                </el-form-item>
                <el-form-item prop="repassword" label="重复密码">
                    <el-input type="password" placeholder="重复密码" v-model="registerForm.repassword" show-password></el-input>
                </el-form-item>
            </el-form>
            <div class="btn">
                <div @click="goback">取消</div>
                <div @click="verifyPass()">修改</div>
            </div>
        </div>
    </div>
</template>
  
<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import { rules } from '../assets/data/form'
import { updatePass, getUserOfId } from '../api/index'

export default {
    name: 'info',
    mixins: [mixin],
    data: function () {
        return {
            registerForm: { // 注册
                password: '',
                newpassword: '',
                repassword: ''
            },
            rules: []
        }
    },
    computed: {
        ...mapGetters([
            'userId'
        ])
    },
    created() {
        this.rules = rules
    },
    mounted() {
        this.getMsg(this.userId)
    },
    methods: {
        getMsg(id) {
            getUserOfId(id)
                .then(res => {
                    this.pass = res.password
                })
                .catch(err => {
                    console.log(err)
                })
        },
        saveMsg() {
            let params = new URLSearchParams()
            params.append('id', this.userId)
            params.append('password', this.password)
            updatePass(params)
                .then(res => {
                    if (res.code == 1) {
                        // 修改缓存中用户名
                        //   this.$store.commit('setUsername', this.registerForm.username)
                        this.notify(res.msg, 'success')
                        this.registerForm = []
                    } else {
                        this.notify(res.msg, 'error')
                    }
                })
                .catch(err => {
                    this.notify(res.msg, 'error')
                    console.log(err)
                })
        },

        verifyPass() {
            console.log(this.registerForm.password)
            let flag = 1;
            if (this.registerForm.newpassword != this.registerForm.repassword) {
                this.notify('两次输入的密码不一致', 'error')
                flag = 0
            }
            if (this.registerForm.password != this.pass) {
                this.notify('旧密码输入错误', 'error')
                flag = 0
            }
            if (this.registerForm.newpassword == this.pass) {
                this.notify('新密码与旧密码一致', 'error')
                flag = 0
            }
            if (flag) {
                this.password = this.registerForm.newpassword
                console.log(this.password)
                this.saveMsg()
            }

        },

        goback(index) {
            this.$router.go(index)
        }
    }
}
</script>
  
<style lang="scss" scoped>
@import '../assets/css/info.scss';
</style>
  