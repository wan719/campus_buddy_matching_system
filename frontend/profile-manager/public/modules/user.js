import {
  getFromLocalStorage,
  loadData,
  saveToLocalStorage,
} from './storage.js';

const STORAGE_KEY = 'profile_manager_users';

/**
 * getUsers
 * 优先读取 localStorage，若不存在则回退到 data/users.json。
 * @returns {Promise<Array>} 用户数组
 */
export const getUsers = async () => {
  const localUsers = getFromLocalStorage(STORAGE_KEY);
  if (Array.isArray(localUsers)) {
    return localUsers;
  }

  const users = await loadData('data/users.json');
  saveToLocalStorage(STORAGE_KEY, users);
  return users;
};

/**
 * getUserById
 * 根据 id 查找单个用户。
 * @param {number} id - 用户 ID
 * @returns {Promise<Object|undefined>} 用户对象或 undefined
 */
export const getUserById = async (id) => {
  const users = await getUsers();
  return users.find((user) => user.id === Number(id));
};

/**
 * deleteUser
 * 删除指定用户并持久化。
 * @param {number} id - 用户 ID
 * @returns {Promise<Array>} 删除后的用户数组
 */
export const deleteUser = async (id) => {
  const users = await getUsers();
  const nextUsers = users.filter((user) => user.id !== Number(id));
  saveToLocalStorage(STORAGE_KEY, nextUsers);
  return nextUsers;
};

/**
 * addUser
 * 新增用户，自动分配自增 ID。
 * @param {{name:string,email:string,phone:string,bio:string}} user - 新用户数据
 * @returns {Promise<Array>} 新增后的用户数组
 */
export const addUser = async (user) => {
  const users = await getUsers();
  const maxId = users.reduce((max, item) => Math.max(max, item.id), 0);
  const newUser = {
    id: maxId + 1,
    ...user,
  };

  const nextUsers = [...users, newUser];
  saveToLocalStorage(STORAGE_KEY, nextUsers);
  return nextUsers;
};

/**
 * updateUser
 * 更新指定用户信息并持久化。
 * @param {number} id - 用户 ID
 * @param {Object} updates - 需要更新的字段
 * @returns {Promise<Array>} 更新后的用户数组
 */
export const updateUser = async (id, updates) => {
  const users = await getUsers();
  const nextUsers = users.map((user) => {
    if (user.id !== Number(id)) {
      return user;
    }

    return {
      ...user,
      ...updates,
      id: user.id,
    };
  });

  saveToLocalStorage(STORAGE_KEY, nextUsers);
  return nextUsers;
};

/**
 * resetUsers
 * 清空 localStorage 后重新加载初始 JSON 数据。
 * @returns {Promise<Array>} 重置后的用户数组
 */
export const resetUsers = async () => {
  const users = await loadData('data/users.json');
  saveToLocalStorage(STORAGE_KEY, users);
  return users;
};
