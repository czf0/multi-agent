# 智慧客 AI 多应用前端（ai-agent-frontend）

一个基于 Vue 3 与 Arco Design 的多 AI 应用前端，内置 4 个对话应用：
- AI 恋爱大师（Love App）
- AI 超级智能体（Manus App）
- 智慧答题助手（Exam/Quiz App）
- 云医通健康助手（Health App）

前端通过 Server-Sent Events（SSE）与后端进行流式通信，支持对话持久化、打字机效果与文档（PDF/HTML）结果查看按钮等交互体验。


## 技术栈
- Vue 3 + Vue Router 4
- Arco Design Vue 组件库（@arco-design/web-vue）
- Axios、EventSource（event-source-polyfill 兜底）
- Vue CLI 5 构建
- Nginx（生产部署示例）


## 目录结构
```
ai-agent-frontend/
├─ package.json
├─ vue.config.js
├─ nginx.conf                 # 生产部署 Nginx 示例（含 SSE 代理配置与 History 路由）
├─ public/
│  └─ index.html
└─ src/
   ├─ main.js                 # 入口，加载 Arco 样式与全局样式
   ├─ App.vue                 # 主布局（左侧菜单 + 路由内容区）
   ├─ router/
   │  └─ index.js            # 路由：/、/love-app、/manus-app、/exam-app、/health-app
   ├─ services/
   │  └─ api.js              # 与后端交互与 SSE 连接（默认 http://localhost:8102/api）
   ├─ utils/
   │  └─ uuid.js             # 会话 ID 工具（localStorage 持久化）
   ├─ components/
   │  ├─ ChatInterface.vue   # 通用聊天组件（含打字机效果、清空、输入框提示）
   │  └─ ChatMessage.vue     # 单条消息组件（支持 PDF/HTML 结果按钮）
   └─ views/
      ├─ HomeView.vue        # 首页入口卡片
      ├─ LoveAppView.vue     # AI 恋爱大师
      ├─ ManusAppView.vue    # AI 超级智能体
      ├─ ExamAppView.vue     # 智慧答题助手
      └─ HealthAppView.vue   # 云医通健康助手
```


## 功能特性
- 多应用/多路由：左侧菜单一键切换 4 个内置 AI 助手
- 流式对话（SSE）：消息逐步抵达、体验更顺滑
- 会话持久化：按聊天 ID 将消息保存到 localStorage
- 打字机效果：AI 回复逐字呈现（对文档类消息自动关闭打字机效果）
- 文档结果交互：识别 PDF/HTML 链接并提供“一键打开”按钮
- 生产路由与缓存优化：nginx.conf 已包含 History 路由、静态资源缓存与 SSE 关键配置


## 环境要求
- Node.js LTS（建议 v16+ 或 v18+）
- npm（或兼容的包管理器）


## 快速开始（开发）
1) 安装依赖
```cmd
npm install
```

2) 本地开发启动
```cmd
npm run serve
```

3) 后端联调说明
- 前端默认把 API 指向 `http://localhost:8102/api`，见 `src/services/api.js` 中 `API_BASE_URL`
- 你需要在本机 8102 端口提供后端服务，或者使用仓库内 `nginx.conf` 反向代理到你的真实后端
- 如果暂时没有 8102 端口服务，可临时修改 `src/services/api.js` 的 `API_BASE_URL` 指向你的后端 URL


## 构建与产物
- 生产构建
```cmd
npm run build
```
- 构建产物输出到 `dist/` 目录，可直接由 Nginx/静态服务器托管


## 生产部署（Nginx）
项目内置 `nginx.conf` 示例，包含：
- Vue Router History 路由（`try_files ... /index.html`）
- 静态缓存策略（js/css/字体/图片）
- SSE 反向代理关键配置（`proxy_http_version 1.1`、`proxy_buffering off`、`proxy_read_timeout` 等）

关键片段（已在 `nginx.conf` 中配置好）
```
location ^~ /api/ {
    proxy_pass https://<你的后端域名或地址>/api/;
    proxy_set_header Host <你的后端主机名>;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;

    # SSE 关键
    proxy_set_header Connection "";
    proxy_http_version 1.1;
    proxy_buffering off;
    proxy_cache off;
    chunked_transfer_encoding off;
    proxy_read_timeout 600s;
}
```

### Docker + Nginx 快速部署示例（可选）
1) 先构建前端
```cmd
npm run build
```

2) 使用官方 Nginx 镜像托管（Windows cmd 示例，注意用引号包裹包含空格的路径）
```cmd
docker run -d --name ai-agent-frontend -p 8102:8102 ^
  -v "%cd%\dist:/usr/share/nginx/html" ^
  -v "%cd%\nginx.conf:/etc/nginx/conf.d/default.conf:ro" ^
  nginx:alpine
```
- 访问 `http://localhost:8102` 即可（`nginx.conf` 中已在 8102 端口监听）
- 如需更换端口请同时修改 `nginx.conf` 的 `listen` 与容器端口映射


## 后端地址与环境配置
- 默认后端基地址：`src/services/api.js` 中的 `API_BASE_URL`
- 生产环境通常通过 Nginx 反代 `/api/` 到后端；如更换后端域名/路径，请同步更新：
  - `nginx.conf` 的 `proxy_pass` 与 `proxy_set_header Host`
  - 如果不走反代，直接在前端把 `API_BASE_URL` 指到完整的后端地址
- 文档结果打开逻辑位于 `src/components/ChatMessage.vue`：
  - 对 `localhost:8102` 进行了显式处理（将 `/api/files/...` 拼接为 `http://localhost:8102/...`）
  - 如果你的生产域名与端口不同，建议同步调整此组件里的域名拼接逻辑，或改造成从环境变量读取基础域名


## 常见问题（FAQ）
1) 路由在刷新后 404？
- 生产需使用 Nginx 的 `try_files $uri $uri/ /index.html;`，本仓库 `nginx.conf` 已配置

2) SSE 推流被中断或不连续？
- 反代务必开启：`proxy_http_version 1.1`、`proxy_buffering off`、`proxy_read_timeout`；
- 确认后端的 SSE 接口可长连接并正确发送 `text/event-stream`

3) PDF/HTML 文档按钮点击打不开？
- 组件通过 `localhost:8102` 拼接文档访问地址；确保你的生产域名/端口一致，或修改 `ChatMessage.vue` 中的域名拼接策略

4) 本地联调跨域问题？
- 默认走绝对地址 `http://localhost:8102/api`；若后端端口/域不同，请：
  - 修改 `src/services/api.js` 的 `API_BASE_URL`；或
  - 在开发服务器（vue-cli-service）配置 devServer 代理（未内置），或直接使用本仓库 `nginx.conf`

5) 样式或图标不生效？
- 确认 `src/main.js` 已引入 `@arco-design/web-vue/dist/arco.css`（已内置）


## NPM 脚本
```json
{
  "serve": "vue-cli-service serve",
  "build": "vue-cli-service build",
  "lint": "vue-cli-service lint"
}
```
- 开发：`npm run serve`
- 构建：`npm run build`
- 代码检查：`npm run lint`


## 代码风格与约定
- ESLint：`plugin:vue/vue3-essential` + `eslint:recommended`
- 组件命名与路由路径遵循现有约定，尽量保持统一
- 保持 `services/api.js` 中 API 定义的最小改动，便于环境切换

