<!--
 * @Description: 秒杀组件，用于首页、秒杀商品页面的商品列表
 * @Author: liuxianchun
 -->
<template>
  <div id="SeckillList" class="SeckillList">
    <ul>
      <div v-for="(item,index) in list" :key="item.product_id">
        <!--showLength为展示的产品个数-->
        <li v-show="index<showLength">
            <el-popover placement="top">
              <p>确定删除吗？</p>
              <div style="text-align: right; margin: 10px 0 0">
                <el-button type="primary" size="mini" @click="deleteCollect(item.product_id)">确定</el-button>
              </div>
              <i class="el-icon-close delete" slot="reference" v-show="isDelete"></i>
            </el-popover>
            <router-link :to="{ path: 'seckill/details', query: {productID:item.product_id} }">
              <img :src="$target +item.detail.product_picture" alt />
              <h2>{{item.detail.product_name}}</h2>
              <h3>{{item.detail.product_title}}</h3>
              <p>
                <span>{{item.seckill_price}}元</span>
                <span
                    v-show="item.seckill_price != item.detail.product_price"
                    class="del"
                >{{item.detail.product_price}}元</span>
              </p>
            </router-link>
        </li>
      </div>
    </ul>
  </div>
</template>
<script>
export default {
  name: "SeckillList",
  // list为父组件传过来的商品列表
  // isMore为是否显示“浏览更多”
  // showLength为父组件传过来的展示的产品个数
  props: ["list", "isMore", "isDelete","showLength"],
  data() {
    return {};
  },
  methods: {
    deleteCollect(product_id) {
      this.$axios
        .post("collect/deleteCollect", {
          user_id: this.$store.getters.getUser.user_id,
          product_id: product_id
        },{withCredentials : true})
        .then(res => {
          switch (res.data.code) {
            case "001":
              // 删除成功
              // 删除删除列表中的该商品信息
              for (let i = 0; i < this.list.length; i++) {
                const temp = this.list[i];
                if (temp.product_id == product_id) {
                  this.list.splice(i, 1);
                }
              }
              // 提示删除成功信息
              this.notifySucceed(res.data.msg);
              break;
            default:
              // 提示删除失败信息
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
<style scoped>
.SeckillList ul li {
  z-index: 1;
  float: left;
  width: 234px;
  height: 280px;
  padding: 10px 0;
  margin: 0 0 14.5px 13.7px;
  background-color: white;
  -webkit-transition: all 0.2s linear;
  transition: all 0.2s linear;
  position: relative;
}
.SeckillList ul li:hover {
  z-index: 2;
  -webkit-box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  -webkit-transform: translate3d(0, -2px, 0);
  transform: translate3d(0, -2px, 0);
}
.SeckillList ul li img {
  display: block;
  width: 160px;
  height: 160px;
  background: url(../assets/imgs/placeholder.png) no-repeat 50%;
  margin: 0 auto;
}
.SeckillList ul li h2 {
  margin: 25px 10px 0;
  font-size: 14px;
  font-weight: 400;
  color: #333;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.SeckillList ul li h3 {
  margin: 5px 10px;
  height: 18px;
  font-size: 12px;
  font-weight: 400;
  color: #b0b0b0;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.SeckillList ul li p {
  margin: 10px 10px 10px;
  text-align: center;
  color: #ff6700;
}
.SeckillList ul li p .del {
  margin-left: 0.5em;
  color: #b0b0b0;
  text-decoration: line-through;
}
.SeckillList #more {
  text-align: center;
  line-height: 280px;
}
.SeckillList #more a {
  font-size: 18px;
  color: #333;
}
.SeckillList #more a:hover {
  color: #ff6700;
}
.SeckillList ul li .delete {
  position: absolute;
  top: 10px;
  right: 10px;
  display: none;
}
.SeckillList ul li:hover .delete {
  display: block
}
.SeckillList ul li .delete:hover {
  color: #ff6700;
}
</style>