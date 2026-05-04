const CATEGORIES = ['全部', 'JavaScript', 'CSS', '工具'];

export class BlogList {
  render({ posts, allPosts, selectedCategory, searchTerm, editingPost }) {
    return `
      <section class="blog-list" id="articles">
        <div class="section-heading">
          <span>最新文章</span>
          <h2>技术文章管理</h2>
          <p id="resultsCount">共 ${allPosts.length} 篇文章，当前显示 ${posts.length} 篇</p>
        </div>

        ${this.renderForm(editingPost)}
        ${this.renderFilters(selectedCategory, searchTerm)}

        <div class="posts-grid" id="postsGrid">
          ${posts.map((post) => this.renderPostCard(post)).join('')}
        </div>

        <p class="empty-state" id="emptyState" ${posts.length === 0 ? '' : 'hidden'}>
          没有找到匹配的文章，请重置筛选条件后再试。
        </p>
      </section>
    `;
  }

  renderForm(post) {
    const isEditing = Boolean(post);

    return `
      <form class="post-form" id="postForm">
        <div class="form-title">
          <h3>${isEditing ? '编辑文章' : '添加文章'}</h3>
          <p>${isEditing ? '修改后会立即保存到 LocalStorage' : '新文章会立即保存到 LocalStorage'}</p>
        </div>

        <label>
          标题
          <input name="title" type="text" required maxlength="80" value="${this.escape(post?.title || '')}" />
        </label>

        <label>
          摘要
          <textarea name="excerpt" required rows="4" maxlength="260">${this.escape(post?.excerpt || '')}</textarea>
        </label>

        <div class="form-grid">
          <label>
            分类
            <select name="category">
              ${CATEGORIES.filter((category) => category !== '全部')
                .map(
                  (category) =>
                    `<option value="${category}" ${post?.category === category ? 'selected' : ''}>${category}</option>`
                )
                .join('')}
            </select>
          </label>

          <label>
            日期
            <input name="date" type="date" required value="${post?.date || new Date().toISOString().slice(0, 10)}" />
          </label>

          <label>
            阅读时间
            <input name="readTime" type="text" required maxlength="12" value="${this.escape(post?.readTime || '8分钟')}" />
          </label>
        </div>

        <div class="form-actions">
          <button type="submit">${isEditing ? '保存修改' : '发布文章'}</button>
          ${isEditing ? '<button type="button" id="cancelEdit" class="ghost-button">取消编辑</button>' : ''}
        </div>
      </form>
    `;
  }

  renderFilters(selectedCategory, searchTerm) {
    return `
      <div class="toolbar">
        <div class="category-filters">
          ${CATEGORIES.map(
            (category) => `
              <button
                type="button"
                data-category="${category}"
                class="${selectedCategory === category ? 'active' : ''}"
              >
                ${category}
              </button>
            `
          ).join('')}
        </div>

        <div class="search-group">
          <input
            id="searchInput"
            type="search"
            placeholder="搜索标题或摘要"
            value="${this.escape(searchTerm)}"
          />
          <button type="button" id="resetFilters">重置</button>
        </div>
      </div>
    `;
  }

  renderPostCard(post) {
    return `
      <article class="post-card">
        <div class="card-topline">
          <span class="category">${this.escape(post.category)}</span>
          <div class="card-actions">
            <button type="button" data-edit-id="${post.id}" aria-label="编辑文章">编辑</button>
            <button type="button" data-delete-id="${post.id}" aria-label="删除文章">删除</button>
          </div>
        </div>
        <h3>${this.escape(post.title)}</h3>
        <p>${this.escape(post.excerpt)}</p>
        <div class="meta">
          <span>${this.escape(post.date)}</span>
          <span>${this.escape(post.readTime)}</span>
        </div>
      </article>
    `;
  }

  escape(value) {
    const div = document.createElement('div');
    div.textContent = String(value);
    return div.innerHTML;
  }
}
