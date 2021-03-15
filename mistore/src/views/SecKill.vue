<!--
 * @Description: 商品秒杀页面组件
 * @Author: liuxianchun
 -->
<template>
  <div id="seckill_details">
    <!-- 头部 -->
    <div class="page-header">
      <div class="title">
        <p>{{secgood.detail.product_name}}</p>
        <div class="list">
          <ul>
            <li>
              <router-link to>概述</router-link>
            </li>
            <li>
              <router-link to>参数</router-link>
            </li>
            <li>
              <router-link to>用户评价</router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!-- 头部END -->

    <!-- 主要内容 -->
    <div class="main">
      <!-- 左侧商品轮播图 -->
      <div class="block">
        <el-carousel height="560px" v-if="productPicture.length>1">
          <el-carousel-item v-for="item in productPicture" :key="item.id">
            <img style="height:560px;" :src="$target + item.product_picture" :alt="item.introduction" />
          </el-carousel-item>
        </el-carousel>
        <div v-if="productPicture.length==1">
          <img
            style="height:560px;"
            :src="$target + productPicture[0].product_picture"
            :alt="productPicture[0].introduction"
          />
        </div>
      </div>
      <!-- 左侧商品轮播图END -->

      <!-- 右侧内容区 -->
      <div class="content">
        <h1 class="name">{{secgood.detail.product_name}}</h1>
        <p class="intro">{{secgood.detail.product_intro}}</p>
        <p class="store">小米自营</p>
        <div class="price" id="seckill">
          <span class="seckill">秒杀价</span>
          <span>  ￥{{secgood.seckill_price}}</span> <br>
          <span class="pre_price">市场价</span>
          <span class="del">  ￥{{secgood.detail.product_price}}</span>
          <div class="timeout">
            <div v-show="status===0">距离秒杀开始还有{{remainTime}}</div>
            <div v-show="status===1">秒杀中</div>
            <div v-show="status===-1">秒杀已结束</div>
          </div>
        </div>
        <div class="pro-list">
          <span class="pro-name">{{secgood.detail.product_name}}</span>
          <span class="pro-price">
            <span>{{secgood.seckill_price}}元</span>
            <span class="pro-del">{{secgood.detail.product_price}}元</span>
          </span>
          <p class="price-sum">秒杀价 : {{secgood.seckill_price}}元</p>
        </div>
        <!-- 内容区底部按钮 -->
        <div class="button">
          <el-button class="shop-cart" :disabled="status!==1" @click="addSecKillOrder">立即抢购</el-button>
          <el-button class="like" @click="addCollect">喜欢</el-button>
        </div>
        <!-- 内容区底部按钮END -->
        <div class="pro-policy">
          <ul>
            <li>
              <i class="el-icon-circle-check"></i> 小米自营
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 小米发货
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 7天无理由退货
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 7天价格保护
            </li>
          </ul>
        </div>
      </div>
      <!-- 右侧内容区END -->
    </div>
    <!-- 主要内容END -->
  </div>
</template>
<script>
import { mapActions } from "vuex";
export default {
  data() {
    return {
      dis: false, // 控制“立即抢购按钮是否可用”
      productID: '', // 商品id
      secgood: '', // 秒杀商品详细信息
      productPicture: '', // 商品图片
      status: '',   //秒杀活动状态：0未开始，1进行中，-1已结束
      remain: 0 ,  //距离秒杀开始时间
    };
  },
  // 通过路由获取商品id
  activated() {
    if (this.$route.query.productID != undefined) {
      this.productID = this.$route.query.productID;
    }
  },
  watch: {
    // 监听商品id的变化，请求后端获取商品数据
    productID: function(val) {
      this.getDetails(val);
      this.getDetailsPicture(val);
    }
  },
  computed:{
    //倒计时
    remainTime(){
      let day = parseInt(this.remain/3600/24);
      let hour = parseInt(this.remain/3600%24);
      let minute = parseInt(this.remain/60%60);
      let second = parseInt(this.remain%60);
      if(hour<10){
        hour = '0'+hour;
      }
      if(minute<10){
        minute = '0'+minute;
      }
      if(second<10){
        second = '0'+second;
      }
      if(day===0){
        return hour+':'+minute+':'+second;
      }else{
        return day+'天'+hour+':'+minute+':'+second;
      }
    }
  },
  methods: {
    ...mapActions(["unshiftShoppingCart", "addShoppingCartNum"]),
    //设置秒杀倒计时
    seckill(){
      clearInterval(this.timer);  //先清除定时器
      this.timer = setInterval(()=>{
        if(this.remain>0){
            this.remain = this.remain-1;
        }else if(parseInt(new Date(this.secgood.end_date).getTime()-new Date().getTime())>0){
          this.status = 1;
        }else{
          this.status = -1;
        }
      },1000);
    },

// 获取商品详细信息
    getDetails(val) {
      this.$axios
        .post("seckill/getDetails", {
          productID: val
        })
        .then(res => {
          this.secgood = res.data.secgood;
          this.remain = parseInt((new Date(this.secgood.start_date).getTime()-new Date().getTime())/1000);
          this.seckill();
          if(this.remain>0){
            this.status = 0;   //秒杀未开始
          }else if(new Date()-this.secgood.end_date>0){
            this.status = -1;   //秒杀已结束
          }else{
            this.status = 1;   //秒杀进行中
          }
        })
        .catch(err => {
          return Promise.reject(err);
        });
    },
    // 获取商品图片
    getDetailsPicture(val) {
      this.$axios
        .post("product/getDetailsPicture", {
          productID: val
        },{withCredentials : true})
        .then(res => {
          this.productPicture = res.data.ProductPicture;
        })
        .catch(err => {
          return Promise.reject(err);
        });
    },
    // 生成秒杀订单
    addSecKillOrder() {
      // 判断是否登录,没有登录则显示登录组件
      if (!this.$store.getters.getUser) {
        this.$store.dispatch("setShowLogin", true);
        return;
      }
      console.log(this.$store.getters.getUser.user_id);
      console.log(this.productID);
      this.$axios
        .post("seckill/addSecKillOrder", {
          user_id: this.$store.getters.getUser.user_id,
          product_id: this.productID,
          secgoods_id: this.secgood.id
        },{withCredentials : true})
        .then(res => {
          switch (res.data.code) {
            case "001":
              // 抢购成功
             // this.unshiftShoppingCart(res.data.shoppingCartData[0]);
              this.notifySucceed(res.data.msg);
              this.timer = setInterval(() => {
                this.$axios
                    .post("seckill/getSecKillResult",{
                      user_id:this.$store.getters.getUser.user_id,
                      product_id: this.productID
                    },{withCredentials:true})
                    .then(res=>{
                      if(res.data.code==="001"){
                        this.notifySucceed(res.data.msg);
                        //请求成功，关闭定时器
                        clearInterval(this.timer);
                        this.timer = null;
                      }
                    })
                    .catch(err => {
                      return Promise.reject(err);
                    })
              },1000);
              break;
              // 抢购失败
            default:
              this.notifyError(res.data.msg);
          }
        })
        .catch(err => {
          return Promise.reject(err);
        });
    },
    getSecKillResult(){

    },
    addCollect() {
      // 判断是否登录,没有登录则显示登录组件
      if (!this.$store.getters.getUser) {
        this.$store.dispatch("setShowLogin", true);
        return;
      }
      this.$axios
        .post("collect/addCollect", {
          user_id: this.$store.getters.getUser.user_id,
          product_id: this.productID
        },{withCredentials : true})
        .then(res => {
          if (res.data.code == "001") {
            // 添加收藏成功
            this.notifySucceed(res.data.msg);
          } else {
            // 添加收藏失败
            this.notifyError(res.data.msg);
          }
        })
        .catch(err => {
          return Promise.reject(err);
        });
    }
  }
};
</script>
<style>
/* 头部CSS */
#seckill_details .page-header {
  height: 64px;
  margin-top: -20px;
  z-index: 4;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  -webkit-box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
  box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
}
#seckill_details .page-header .title {
  width: 1225px;
  height: 64px;
  line-height: 64px;
  font-size: 18px;
  font-weight: 400;
  color: #212121;
  margin: 0 auto;
}
#seckill_details .page-header .title p {
  float: left;
}
#seckill_details .page-header .title .list {
  height: 64px;
  float: right;
}
#seckill_details .page-header .title .list li {
  float: left;
  margin-left: 20px;
}
#seckill_details .page-header .title .list li a {
  font-size: 14px;
  color: #616161;
}
#seckill_details .page-header .title .list li a:hover {
  font-size: 14px;
  color: #ff6700;
}
/* 头部CSS END */

/* 主要内容CSS */
#seckill_details .main {
  width: 1225px;
  height: 560px;
  padding-top: 30px;
  margin: 0 auto;
}
#seckill_details .main .block {
  float: left;
  width: 560px;
  height: 560px;
}
#seckill_details .el-carousel .el-carousel__indicator .el-carousel__button {
  background-color: rgba(163, 163, 163, 0.8);
}
#seckill_details .main .content {
  float: left;
  margin-left: 25px;
  width: 640px;
}
#seckill{
  margin-top: 15px;
  margin-left:8px;
  font-size:14px;
  background-color: #FF5207;
  color:white;
}
#seckill .seckill{
  font-size:16px;
}
#seckill .pre_price{
  font-size:14px;
}
#seckill .timeout{
  margin-bottom: 28px;
  float:right;
  font-size:13px;
  position: relative;
  top:-8px;
}
#seckill_details .main .content .name {
  height: 30px;
  line-height: 30px;
  font-size: 24px;
  font-weight: normal;
  color: #212121;
}
#seckill_details .main .content .intro {
  color: #b0b0b0;
  padding-top: 10px;
}
#seckill_details .main .content .store {
  color: #ff6700;
  padding-top: 10px;
}
/*价格，秒杀倒计时区域*/
#seckill_details .main .content .price {
  font-size: 18px;
  border-bottom: 1px solid #e0e0e0;
  height:50px;
  padding: 10px;
}
#seckill_details .main .content .price .del {
  font-size: 14px;
  margin-left: 10px;
  text-decoration: line-through;
}
#seckill_details .main .content .pro-list {
  background: #f9f9fa;
  padding: 30px 60px;
  margin: 50px 0 50px;
}
#seckill_details .main .content .pro-list span {
  line-height: 30px;
  color: #616161;
}
#seckill_details .main .content .pro-list .pro-price {
  float: right;
}
#seckill_details .main .content .pro-list .pro-price .pro-del {
  margin-left: 10px;
  text-decoration: line-through;
}
#seckill_details .main .content .pro-list .price-sum {
  color: #ff6700;
  font-size: 24px;
  padding-top: 20px;
}
#seckill_details .main .content .button {
  height: 55px;
  margin: 10px 0 20px 0;
}
#seckill_details .main .content .button .el-button {
  float: left;
  height: 55px;
  font-size: 16px;
  color: #fff;
  border: none;
  text-align: center;
}
#seckill_details .main .content .button .shop-cart {
  width: 340px;
  background-color: #ff6700;
}
#seckill_details .main .content .button .shop-cart:hover {
  background-color: #f25807;
}

#seckill_details .main .content .button .like {
  width: 260px;
  margin-left: 40px;
  background-color: #b0b0b0;
}
#seckill_details .main .content .button .like:hover {
  background-color: #757575;
}
#seckill_details .main .content .pro-policy li {
  float: left;
  margin-right: 20px;
  color: #b0b0b0;
}
/* 主要内容CSS END */
</style>