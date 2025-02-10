# 一些简单的介绍

本地环境需要安装 pnpm 7.x+ 、Node.js 14.18+

组件库： https://www.naiveui.com/zh-CN/os-theme/docs/installation

## 工具配置

本项目推荐使用 VSCode 进行开发，项目里面已内置 VSCode 配置，包含推荐的插件和设置。

以下为推荐的插件：

Auto Close Tag - 自动添加 HTML/XML 结束标签
Auto Complete Tag - 为 HTML/XML 添加关闭标签和自动重命名成对的标签
Auto Import - 自动查找、解析和提供所有可用导入的代码操作和代码完成
Auto Rename Tag - 自动重命名成对的 HTML/XML 标签
Color Highlight - 颜色高亮插件
DotENV - 高亮.env 文件
ESLint - 代码检查
Git Graph - Git 图形化操作工具
GitLens — Git supercharged - 显示具体某行代码的 git 信息
Icônes - 搜索 iconify 图标的插件
Iconify IntelliSense - Iconify 图标实时显示的插件
javascript console utils - 提供快捷键 ctrl+l 直接输入 console.log()
Material Icon Theme - 图标主题，显示文件和文件多种图标
Path Intellisense - 智能显示导入的路径
Prettier - Code formatter - 代码格式化插件
UnoCSS - unocss 写法提示插件
Vue Language Features (Volar) - volar 插件， Language support for Vue 3
Vue VSCode Snippets - vue2、vue3 写法提示

# 插件配置

安装 Volar，禁用 Vetur#
Vue Language Features (Volar) - volar 插件， Language support for Vue 3
开启 Volar 的 takeover mode#
搜索插件 @builin typescript
鼠标右键 “JavaScript 和 TypeScript 的语言功能”
点击 “禁用工作区”1

# 图标

https://icones.netlify.app/collection/ant-design?s=Question

# 问题

```
Error: getaddrinfo ENOTFOUND 19000
    at GetAddrInfoReqWrap.onlookup [as oncomplete] (node:dns:71:26)
```

以管理员身份打开记事本

编辑 C:\WINDOWS\System32\drivers\etc
加上 0.0.0.0 http://172.16.0.25:19000

https://www.jianshu.com/p/6507518daf54
