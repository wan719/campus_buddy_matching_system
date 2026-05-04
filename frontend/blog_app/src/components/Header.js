export class Header {
  render() {
    return `
      <header class="header">
        <nav class="nav">
          <div class="logo">My Blog</div>
          <ul class="nav-links">
            <li><a href="#">首页</a></li>
            <li><a href="#articles">文章</a></li>
            <li><a href="#postForm">发布</a></li>
            <li><a href="#footer">联系</a></li>
          </ul>
        </nav>
      </header>
    `;
  }
}
