# Git 命令完全指南（按功能分类）
### 本文档整理了 Git 的所有常用命令，按使用场景分类，方便查阅学习。命令格式已适配 Git 最新版本，同时兼顾传统用法。
# 一， 基础配置与初始化
```bash
# 全局配置（当前用户的所有仓库）
git config --global user.name "你的名字"
git config --global user.email "你的邮箱@example.com"

# 仅当前仓库配置（覆盖全局）
git config user.name "项目专用名字"
git config user.email "项目专用邮箱"

# 查看所有配置
git config --list
git config --global --list

# 其他实用配置
git config --global color.ui auto        # 开启彩色输出
git config --global core.editor "vim"    # 设置默认编辑器
git config --global http.proxy 'http://127.0.0.1:1080'  # 设置HTTP代理
```
# 二，日常核心操作
## 2.1查看状态
```bash
git status        # 查看详细状态（红=未暂存，绿=已暂存）
git status -s     # 简洁模式（M=修改，A=新增，D=删除，??=未跟踪）
```
## 2.2 添加文件到暂存区
```bash
git add 文件名          # 添加单个文件
git add 目录/           # 添加指定目录
git add .              # 添加当前目录所有变更（含新文件和修改）
git add -u             # 只添加已跟踪文件的修改/删除（不含新文件）
git add -A             # 添加所有变更（等价于 git add . + git add -u）
```
## 2.3 提交到本地仓库
```bash
git commit -m "提交说明"        # 提交暂存区内容
git commit -am "提交说明"       # 跳过暂存区，直接提交已跟踪文件（相当于 add+commit）
git commit --amend -m "新说明"  # 修改最近一次提交的信息
git commit --amend --no-edit   # 追加文件到最近一次提交，不修改说明
```
## 2.4 查看提交历史
```bash
git log                          # 完整历史
git log --oneline                # 单行简洁显示
git log --graph --all --oneline  # 图形化显示所有分支
git log -p                       # 显示每次提交的差异详情
git log --stat                   # 显示文件改动统计
git log --author="用户名"         # 查看某人提交
git log --grep="关键词"           # 搜索提交信息
git log 文件名                    # 查看某文件的修改历史
git log -n 5                     # 查看最近5条记录
git log --since="2 weeks ago"    # 查看最近两周的提交
```
## 2.5 查看差异
```bash
git diff                 # 工作区 vs 暂存区（未暂存的修改）
git diff --staged        # 暂存区 vs 版本库（已暂存待提交的修改）
git diff --cached        # 同上
git diff HEAD            # 工作区 vs 最近提交
git diff 提交ID1 提交ID2  # 两个提交之间的差异
git diff 分支1 分支2      # 两个分支的差异
git diff -- 文件名        # 只查看特定文件的差异
```
# 三，分支管理
## 3.1 分支基本操作
```bash
# 查看分支
git branch               # 查看本地分支（*表示当前分支）
git branch -r            # 查看远程分支
git branch -a            # 查看所有分支（本地+远程）
git branch -v            # 查看分支及最近提交

# 创建分支
git branch 分支名        # 创建新分支（停留在当前分支）
git checkout -b 分支名   # 创建并切换（传统方式）
git switch -c 分支名     # 创建并切换（新版推荐）

# 切换分支
git checkout 分支名      # 切换分支（传统）
git switch 分支名        # 切换分支（新版推荐）

# 删除分支
git branch -d 分支名     # 删除已合并的分支
git branch -D 分支名     # 强制删除未合并的分支
git push origin --delete 分支名  # 删除远程分支
```
## 3.2 分支合并
```bash
git merge 分支名         # 合并指定分支到当前分支
git merge --no-ff 分支名 # 禁用快进模式（保留合并历史）
git merge --abort        # 合并遇到冲突时，取消合并
```
# 四，远程仓库协作
## 4.1 远程仓库管理
```bash
git remote -v                    # 查看远程仓库地址
git remote add origin 仓库地址    # 添加远程仓库（origin是默认别名）
git remote remove origin         # 删除远程连接
git remote rename 旧名 新名       # 重命名远程仓库
git remote set-url origin 新地址  # 修改远程仓库地址
git remote show origin           # 查看远程仓库详细信息
```
## 4.2 推送与拉取
```bash
# 推送
git push origin 分支名           # 推送本地分支到远程
git push -u origin 分支名        # 首次推送并建立跟踪关系（之后可直接git push）
git push origin --all           # 推送所有本地分支
git push --tags                 # 推送所有标签
git push origin --delete 分支名  # 删除远程分支
git push origin :分支名          # 同上（另一种写法）

# 拉取
git pull origin 分支名           # 拉取远程更新并合并（fetch + merge）
git pull --rebase origin 分支名  # 拉取后用变基替代合并（保持线性历史）

# 抓取（只下载不合并）
git fetch origin                # 抓取远程更新
git fetch --all                 # 抓取所有远程源
git fetch --prune               # 抓取并清理已删除的远程分支引用
```

