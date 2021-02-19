/*
 * @Description: 配置文件
 * @Author: hai-27
 * @Date: 2020-02-07 16:23:00
 * @LastEditors: hai-27
 * @LastEditTime: 2020-03-05 01:41:38
 */
module.exports = {
  //history模式下使用 '/'
  publicPath: '/',
  devServer: {
    open: true,
    proxy: {
      '/api': {
        target: 'http://47.98.145.198:7000', // 本地后端地址
        //target: 'http://47.115.85.237:3000/', // 线上后端地址
        changeOrigin: true, //允许跨域
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
