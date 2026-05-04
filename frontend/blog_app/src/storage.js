import { posts as defaultPosts } from './data/posts.js';

const STORAGE_KEY = 'blog_posts';

export function loadPosts() {
  const savedPosts = localStorage.getItem(STORAGE_KEY);

  if (!savedPosts) {
    savePosts(defaultPosts);
    return [...defaultPosts];
  }

  try {
    const parsedPosts = JSON.parse(savedPosts);
    return Array.isArray(parsedPosts) ? parsedPosts : [...defaultPosts];
  } catch {
    savePosts(defaultPosts);
    return [...defaultPosts];
  }
}

export function savePosts(posts) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(posts));
}
