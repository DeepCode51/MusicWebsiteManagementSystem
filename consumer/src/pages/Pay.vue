<template>
    <div class="container" :style='{ "margin": "0 200px 20px" }'>
        <el-alert title="确认支付前请先核对订单信息" type="success" :closable="false">
            订单编号:{{ this.orderList.code }}
        </el-alert>
        <div class="pay-type-content">
            <div class="pay-type-item">
                <el-radio v-model="type" label="微信支付"></el-radio>
                <img src="@/assets/img/pay/weixin.png" alt>
            </div>
            <div class="pay-type-item">
                <el-radio v-model="type" label="支付宝支付"></el-radio>
                <img src="@/assets/img/pay/zhifubao.png" alt>
            </div>
            <div class="pay-type-item">
                <el-radio v-model="type" label="建设银行"></el-radio>
                <img src="@/assets/img/pay/jianshe.png" alt>
            </div>
            <div class="pay-type-item">
                <el-radio v-model="type" label="农业银行"></el-radio>
                <img src="@/assets/img/pay/nongye.png" alt>
            </div>
            <div class="pay-type-item">
                <el-radio v-model="type" label="中国银行"></el-radio>
                <img src="@/assets/img/pay/zhongguo.png" alt>
            </div>
            <div class="pay-type-item">
                <el-radio v-model="type" label="交通银行"></el-radio>
                <img src="@/assets/img/pay/jiaotong.png" alt>
            </div>
        </div>
        <div class="button-content">
            <el-button @click="back()">返回</el-button>
            <el-button @click="submit" type="primary">确认支付</el-button>
        </div>
    </div>
</template>
<script>
import { getOrderOfId,setIspay } from '../api/index'
import { mixin } from '../mixins'

export default {
    name: 'pay',
    mixins: [mixin],
    data() {
        return {
            type: '',
            orderList: [],
            id: this.$route.path.split('/')[2],
        };
    },
    mounted() {
        this.getOrders(this.id)
    },
    methods: {
        getOrders(id) {
            getOrderOfId(id)
                .then(res => {
                    // console.log(res)
                    this.orderList = res
                }).catch(err => {
                    this.notify('订单信息加载失败', 'error')
                    console.log(err)
                })
        },
        submitTap() {

        },
        back() {
            this.$router.go(-1);
        },
        // 修改订单内支付信息
        submit() {
            if (!this.type) {
                this.notify('请选择支付方式', 'error')
            } else {
                this.$confirm(`确定支付?`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    let params = new URLSearchParams()
                    params.append('id', this.id)
                    params.append('ispay', 1)
                    params.append('ostatus', 0)
                    // console.log(params)
                    setIspay(params).then(res => {
                        if (res.code == 1) {
                            this.notify(res.msg, 'success')
                            setTimeout( () => this.$router.go(-1), 1000)
                        } else {
                            this.notify(res.msg, 'error')
                        }
                    })
                })
            }
        }
    }
}
</script>
<style lang="scss" scoped>
.container {
    margin: 10px;
    padding-top: 100px;
    font-size: 14px;
    background-color: white;

    span {
        width: 60px;
    }

    .pay-type-content {
        display: flex;
        align-items: center;
        margin-top: 20px;
        flex-wrap: wrap;

        span {
            width: 100px;
        }

        .pay-type-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 300px;
            margin: 20px;
            border: 1px solid #eeeeee;
            padding: 20px;
        }
    }

    .button-content {
        align-items: center;
        margin: 20px;
        margin-left: 560px;
    }
}
</style>