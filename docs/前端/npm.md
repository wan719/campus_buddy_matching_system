#### npm是什么？

npm（Node Package Manager）是Node.js的默认包管理器，也是世界上最大的软件包仓库。

```
npm = Node.js的"应用商店"
     ↓
你可以从npm安装别人写好的代码包
你也可以把自己的代码发布到npm
```

#### package.json - 项目身份证

每个Node.js项目都有一个 `package.json` 文件，它是项目的配置文件。

**创建新项目：**

```bash
mkdir my-first-project
cd my-first-project
npm init -y  # -y 表示使用默认配置，跳过所有提问
```

**package.json 结构解析：**

```json
{
  "name": "my-first-project",           // 项目名称
  "version": "1.0.0",                    // 版本号（语义化版本）
  "description": "我的第一个Node.js项目", // 项目描述
  "main": "index.js",                    // 入口文件
  "scripts": {                           // 脚本命令
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "keywords": [],                        // 关键词
  "author": "Your Name",                 // 作者
  "license": "ISC",                      // 许可证
  "dependencies": {                      // 生产环境依赖
    // 运行项目时需要的包
  },
  "devDependencies": {                   // 开发环境依赖
    // 开发时需要的工具（如构建工具、测试工具）
  }
}
```

#### 依赖管理

**安装依赖包：**

```bash
# 安装单个包（保存到dependencies）
npm install lodash

# 安装包并保存到devDependencies
npm install eslint --save-dev

# 一次性安装多个包
npm install lodash axios dayjs

# 安装特定版本
npm install lodash@4.17.21

# 全局安装（安装到系统，可在任何地方使用）
npm install -g create-vite
```

**package-lock.json 文件：**

```bash
npm install 会自动生成 package-lock.json
作用：
- 锁定每个包的确切版本
- 确保团队成员安装相同的版本
- 提高安装速度
```

**node_modules 目录：**

```bash
node_modules/
├── lodash/          # lodash包及其依赖
├── axios/           # axios包及其依赖
└── ...

# ⚠️ 不要手动修改这个目录
# ⚠️ 通常将此目录添加到 .gitignore
```

#### npm scripts - 项目命令

scripts 字段定义了项目的快捷命令：

```json
{
  "scripts": {
    "start": "node index.js",           // 启动项目
    "dev": "vite",                      // 开发模式
    "build": "vite build",              // 构建
    "preview": "vite preview",          // 预览构建结果
    "lint": "eslint src/"               // 代码检查
  }
}
```

**使用方式：**

```bash
npm run dev      # 运行 dev 脚本
npm run build    # 运行 build 脚本
npm start        # start 可以省略 run
```

**常用npm命令：**

```bash
npm install              # 安装所有依赖（根据package.json）
npm install <package>    # 安装指定包
npm uninstall <package>  # 卸载包
npm update               # 更新所有依赖
npm run <script>         # 运行脚本命令
npm info <package>       # 查看包信息
npm search <keyword>     # 搜索包
```

---