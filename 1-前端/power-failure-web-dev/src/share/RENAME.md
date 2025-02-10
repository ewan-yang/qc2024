https://mp.weixin.qq.com/s/xvq_4zYUr1O3wWY9b6mA0Q

分别在主项目/新项目中执行的 git 命令：
添加 remote 以简化后续命令：
git remote add share http://git.tecpie.com/platform/2023/tecpie-share.git

添加 subtree：
git subtree add --prefix=src/share share master --squash
拉取 subtree：
git subtree pull --prefix=src/share share master --squash
推送 subtree：
git subtree push --prefix=src/share share master --squash
查看 subtree 的 id:
git ls-remote report
或
git ls-remote report | grep <sha-1>


__________________________________________________________________________________________

库依赖
naiveUI
lodash-es
vue-types
unoCss
———————————————————————————————————————————————————————————————————————————————————————————————
share禁止改动，多个项目影响引用git，通用业务通知陈前代码检查后合并,其他改动copy