UI 框架
UnoCSS - 高性能且极具灵活性的即时原子化 CSS 引擎
Icons
Iconify - 使用任意的图标集，浏览：🔍Icônes
UnoCSS 的纯 CSS 图标方案
插件
Vue Router
vite-plugin-pages - 以文件系统为基础的路由
unplugin-auto-import - 直接使用 Composition API 等，无需导入
unplugin-vue-components - 自动加载组件
unplugin-vue-macros - 探索并扩展更多的宏和语法糖到 Vue 中
VueUse - 实用的 Composition API 工具合集


爱国网
vpn地址： https://igw.isgcc.net:18022/
//Vant 中有个别组件是以函数的形式提供的，包括 Toast，Dialog，Notify 和 ImagePreview 组件。在使用函数组件时，unplugin-vue-components 无法自动引入对应的样式，因此需要手动引入样式。


打包生产
.env.production文件中VITE_BASE_URL为''
VITE_BASE_URL=''
src/utils/config.ts中换成生产环境的agentid、appid、authorizeUrl、serviceTicketUrl
