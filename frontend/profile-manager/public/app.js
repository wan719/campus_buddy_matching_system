import * as userModule from './modules/user.js';
import * as ui from './modules/ui.js';

let users = [];

const form = document.querySelector('#user-form');
const idInput = document.querySelector('#user-id');
const nameInput = document.querySelector('#name');
const emailInput = document.querySelector('#email');
const phoneInput = document.querySelector('#phone');
const bioInput = document.querySelector('#bio');
const submitBtn = document.querySelector('#submit-btn');
const cancelEditBtn = document.querySelector('#cancel-edit-btn');
const resetDataBtn = document.querySelector('#reset-data-btn');
const userList = document.querySelector('#user-list');

/**
 * 清空表单并退出编辑模式。
 */
const resetForm = () => {
  form.reset();
  idInput.value = '';
  submitBtn.textContent = '添加用户';
};

/**
 * 检查表单输入是否有效。
 * @returns {boolean}
 */
const validateForm = () => {
  const name = nameInput.value.trim();
  const email = emailInput.value.trim();
  const phone = phoneInput.value.trim();
  const bio = bioInput.value.trim();

  if (!name || !email || !phone || !bio) {
    ui.showNotification('请完整填写所有字段', 'error');
    return false;
  }

  return true;
};

async function init() {
  try {
    users = await userModule.getUsers();
    ui.renderUserList(users);
    bindEvents();
  } catch (error) {
    console.error('初始化失败:', error);
    ui.showNotification('加载数据失败', 'error');
  }
}

function bindEvents() {
  form.addEventListener('submit', async (event) => {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    const payload = {
      name: nameInput.value.trim(),
      email: emailInput.value.trim(),
      phone: phoneInput.value.trim(),
      bio: bioInput.value.trim(),
    };

    try {
      if (idInput.value) {
        users = await userModule.updateUser(Number(idInput.value), payload);
        ui.showNotification('用户信息已更新', 'success');
      } else {
        users = await userModule.addUser(payload);
        ui.showNotification('用户添加成功', 'success');
      }

      ui.renderUserList(users);
      resetForm();
    } catch (error) {
      console.error('保存用户失败:', error);
      ui.showNotification('保存失败，请重试', 'error');
    }
  });

  userList.addEventListener('click', async (event) => {
    const target = event.target;
    if (!(target instanceof HTMLElement)) {
      return;
    }

    const { id } = target.dataset;
    if (!id) {
      return;
    }

    if (target.classList.contains('js-delete-user')) {
      try {
        users = await userModule.deleteUser(Number(id));
        ui.renderUserList(users);
        ui.showNotification('用户已删除', 'success');
      } catch (error) {
        console.error('删除失败:', error);
        ui.showNotification('删除失败，请重试', 'error');
      }
      return;
    }

    if (target.classList.contains('js-edit-user')) {
      try {
        const user = await userModule.getUserById(Number(id));
        if (!user) {
          ui.showNotification('未找到该用户', 'error');
          return;
        }

        idInput.value = String(user.id);
        nameInput.value = user.name;
        emailInput.value = user.email;
        phoneInput.value = user.phone;
        bioInput.value = user.bio;
        submitBtn.textContent = '保存修改';
        ui.showNotification(`正在编辑：${user.name}`, 'info');
      } catch (error) {
        console.error('进入编辑失败:', error);
        ui.showNotification('编辑失败，请重试', 'error');
      }
    }
  });

  cancelEditBtn.addEventListener('click', () => {
    resetForm();
    ui.showNotification('已取消编辑', 'info');
  });

  resetDataBtn.addEventListener('click', async () => {
    try {
      users = await userModule.resetUsers();
      ui.renderUserList(users);
      resetForm();
      ui.showNotification('已重置为初始数据', 'success');
    } catch (error) {
      console.error('重置失败:', error);
      ui.showNotification('重置失败，请重试', 'error');
    }
  });
}

init();
