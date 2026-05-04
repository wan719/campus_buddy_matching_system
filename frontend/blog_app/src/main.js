import './styles/main.css';
import { Header } from './components/Header.js';
import { Hero } from './components/Hero.js';
import { BlogList } from './components/BlogList.js';
import { Footer } from './components/Footer.js';
import { loadPosts, savePosts } from './storage.js';

class App {
  constructor() {
    this.header = new Header();
    this.hero = new Hero();
    this.blogList = new BlogList();
    this.footer = new Footer();
    this.posts = loadPosts();
    this.selectedCategory = '全部';
    this.searchTerm = '';
    this.editingPostId = null;
  }

  render() {
    const app = document.getElementById('app');

    app.innerHTML = `
      ${this.header.render()}
      <main>
        ${this.hero.render()}
        ${this.blogList.render({
          posts: this.getFilteredPosts(),
          allPosts: this.posts,
          selectedCategory: this.selectedCategory,
          searchTerm: this.searchTerm,
          editingPost: this.getEditingPost()
        })}
      </main>
      ${this.footer.render()}
    `;

    this.bindEvents();
  }

  updateResults() {
    const filteredPosts = this.getFilteredPosts();
    const resultsCount = document.querySelector('#resultsCount');
    const postsGrid = document.querySelector('#postsGrid');
    const emptyState = document.querySelector('#emptyState');

    if (resultsCount) {
      resultsCount.textContent = `共 ${this.posts.length} 篇文章，当前显示 ${filteredPosts.length} 篇`;
    }

    if (postsGrid) {
      postsGrid.innerHTML = filteredPosts.map((post) => this.blogList.renderPostCard(post)).join('');
    }

    if (emptyState) {
      emptyState.hidden = filteredPosts.length !== 0;
    }

    this.bindCardEvents();
  }

  getFilteredPosts() {
    return this.posts.filter((post) => {
      const matchesCategory =
        this.selectedCategory === '全部' || post.category === this.selectedCategory;
      const keyword = this.searchTerm.trim().toLowerCase();
      const matchesSearch =
        !keyword ||
        post.title.toLowerCase().includes(keyword) ||
        post.excerpt.toLowerCase().includes(keyword);

      return matchesCategory && matchesSearch;
    });
  }

  getEditingPost() {
    return this.posts.find((post) => post.id === this.editingPostId) || null;
  }

  saveAndRender() {
    savePosts(this.posts);
    this.render();
  }

  addPost(formData) {
    const post = this.createPostFromForm(formData);
    this.posts = [post, ...this.posts];
    this.saveAndRender();
  }

  updatePost(id, formData) {
    this.posts = this.posts.map((post) =>
      post.id === id ? { ...post, ...this.createPostFromForm(formData, id) } : post
    );
    this.editingPostId = null;
    this.saveAndRender();
  }

  deletePost(id) {
    this.posts = this.posts.filter((post) => post.id !== id);
    if (this.editingPostId === id) {
      this.editingPostId = null;
      savePosts(this.posts);
      this.render();
      return;
    }
    savePosts(this.posts);
    this.updateResults();
  }

  createPostFromForm(formData, existingId = null) {
    return {
      id: existingId ?? Date.now(),
      title: formData.get('title').trim(),
      excerpt: formData.get('excerpt').trim(),
      date: formData.get('date'),
      category: formData.get('category'),
      readTime: formData.get('readTime').trim()
    };
  }

  bindEvents() {
    document.querySelector('.cta-button')?.addEventListener('click', () => {
      document.querySelector('.blog-list')?.scrollIntoView({ behavior: 'smooth' });
    });

    document.querySelectorAll('[data-category]').forEach((button) => {
      button.addEventListener('click', (event) => {
        this.selectedCategory = event.currentTarget.dataset.category;
        document.querySelectorAll('[data-category]').forEach((item) => {
          item.classList.toggle('active', item.dataset.category === this.selectedCategory);
        });
        this.updateResults();
      });
    });

    document.querySelector('#searchInput')?.addEventListener('input', (event) => {
      this.searchTerm = event.target.value;
      this.updateResults();
    });

    document.querySelector('#resetFilters')?.addEventListener('click', () => {
      this.selectedCategory = '全部';
      this.searchTerm = '';
      const searchInput = document.querySelector('#searchInput');
      if (searchInput) {
        searchInput.value = '';
      }
      document.querySelectorAll('[data-category]').forEach((item) => {
        item.classList.toggle('active', item.dataset.category === '全部');
      });
      this.updateResults();
    });

    document.querySelector('#postForm')?.addEventListener('submit', (event) => {
      event.preventDefault();
      const formData = new FormData(event.currentTarget);

      if (this.editingPostId) {
        this.updatePost(this.editingPostId, formData);
      } else {
        this.addPost(formData);
      }
    });

    document.querySelector('#cancelEdit')?.addEventListener('click', () => {
      this.editingPostId = null;
      this.render();
    });

    this.bindCardEvents();
  }

  bindCardEvents() {
    document.querySelectorAll('[data-edit-id]').forEach((button) => {
      button.addEventListener('click', (event) => {
        this.editingPostId = Number(event.currentTarget.dataset.editId);
        this.render();
        document.querySelector('#postForm')?.scrollIntoView({ behavior: 'smooth' });
      });
    });

    document.querySelectorAll('[data-delete-id]').forEach((button) => {
      button.addEventListener('click', (event) => {
        const id = Number(event.currentTarget.dataset.deleteId);
        const post = this.posts.find((item) => item.id === id);
        if (post && window.confirm(`确定删除《${post.title}》吗？`)) {
          this.deletePost(id);
        }
      });
    });
  }
}

new App().render();
