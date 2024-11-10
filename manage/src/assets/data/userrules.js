const validatePhone = (rule, value, callback) => {
    // 使用正则表达式进行验证手机号码
    if (!/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/.test(value)) {
        callback(new Error('手机号格式不正确！'))
    } else {
        callback()
    }
}

const valiusername = (rule, value, callback) => {
    if (!/^[a-zA-Z0-9_-]{4,16}$/.test(value)) {
        callback(new Error('用户名格式不正确！'))
    } else {
        callback()
    }
}

const valipass = (rule, value, callback) => {
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[\s\S]{6,16}$/.test(value)) {
        callback(new Error('密码应为6-16位，至少1个大写字母，1个小写字母和1个数字！'))
    } else {
        callback()
    }
}

const cities = [{
    value: '北京',
    label: '北京'
}, {
    value: '天津',
    label: '天津'
}, {
    value: '河北',
    label: '河北'
}, {
    value: '山西',
    label: '山西'
}, {
    value: '内蒙古',
    label: '内蒙古'
}, {
    value: '辽宁',
    label: '辽宁'
}, {
    value: '吉林',
    label: '吉林'
}, {
    value: '黑龙江',
    label: '黑龙江'
}, {
    value: '上海',
    label: '上海'
}, {
    value: '江苏',
    label: '江苏'
}, {
    value: '浙江',
    label: '浙江'
}, {
    value: '安徽',
    label: '安徽'
}, {
    value: '福建',
    label: '福建'
}, {
    value: '江西',
    label: '江西'
}, {
    value: '山东',
    label: '山东'
}, {
    value: '河南',
    label: '河南'
}, {
    value: '湖北',
    label: '湖北'
}, {
    value: '湖南',
    label: '湖南'
}, {
    value: '广东',
    label: '广东'
}, {
    value: '广西',
    label: '广西'
}, {
    value: '海南',
    label: '海南'
}, {
    value: '重庆',
    label: '重庆'
}, {
    value: '四川',
    label: '四川'
}, {
    value: '贵州',
    label: '贵州'
}, {
    value: '云南',
    label: '云南'
}, {
    value: '西藏',
    label: '西藏'
}, {
    value: '陕西',
    label: '陕西'
}, {
    value: '甘肃',
    label: '甘肃'
}, {
    value: '青海',
    label: '青海'
}, {
    value: '宁夏',
    label: '宁夏'
}, {
    value: '新疆',
    label: '新疆'
}, {
    value: '香港',
    label: '香港'
}, {
    value: '澳门',
    label: '澳门'
}, {
    value: '台湾',
    label: '台湾'
}]

const rules = {
    username: [
        { required: true, trigger: 'blur', validator: valiusername }
    ],
    password: [
        { required: true, trigger: 'blur', validator: valipass }
    ],
    sex: [
        { required: true, message: '请选择性别', trigger: 'change' }
    ],
    phoneNum: [
        { message: '请输入正确的手机号', trigger: 'blur', validator: validatePhone }
    ],
    email: [
        // { message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ],
    birth: [
        { required: true, type: 'date', message: '请选择日期', trigger: 'change' }
    ],
    introduction: [
        { message: '请输入介绍', trigger: 'blur' }
    ],
    location: [
        { message: '请输入地区', trigger: 'change' }
    ]
}

export {
    rules,
    cities,
}