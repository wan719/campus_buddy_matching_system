/**
 * renderUserList
 * 根据用户数组渲染卡片列表。
 * @param {Array} users - 用户数据
 */
export const renderUserList = (users) => {
  const userList = document.querySelector('#user-list');

  if (!Array.isArray(users) || users.length === 0) {
    userList.innerHTML = '<p class="user-meta">暂无用户数据。</p>';
    return;
  }

  userList.innerHTML = users
    .map(
      (user) => `
        <article class="user-card" data-id="${user.id}">
          <h3>${user.name}</h3>
          <p class="user-meta">邮箱：${user.email}</p>
          <p class="user-meta">电话：${user.phone}</p>
          <p class="user-meta">简介：${user.bio}</p>
          <div class="card-actions">
            <button class="btn btn-primary js-edit-user" data-id="${user.id}">编辑</button>
            <button class="btn btn-danger js-delete-user" data-id="${user.id}">删除</button>
          </div>
        </article>
      `,
    )
    .join('');
};

/**
 * showNotification
 * 在顶部区域显示通知，并自动消失。
 * @param {string} message - 提示文本
 * @param {'success'|'error'|'info'} type - 提示类型
 */
export const showNotification = (message, type = 'info') => {
  const box = document.querySelector('#notification');
  box.innerHTML = `<div class="notification ${type}">${message}</div>`;

  setTimeout(() => {
    box.innerHTML = '';
  }, 2200);
};
