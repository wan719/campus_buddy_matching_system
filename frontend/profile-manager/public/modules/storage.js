/**
 * storage.js
 * 负责数据加载与浏览器本地存储。
 */

/**
 * 从服务器加载 JSON 资源。
 * @param {string} resourceName - 资源路径，例如 data/users.json
 * @returns {Promise<any>} 解析后的 JSON 数据
 */
export const loadData = async (resourceName) => {
  try {
    const response = await fetch(`/${resourceName}`);
    if (!response.ok) {
      throw new Error(`请求失败: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('loadData 失败:', error);
    throw error;
  }
};

/**
 * 将数据序列化后保存到 localStorage。
 * @param {string} key - 本地存储键名
 * @param {any} data - 要保存的数据
 */
export const saveToLocalStorage = (key, data) => {
  try {
    localStorage.setItem(key, JSON.stringify(data));
  } catch (error) {
    console.error('saveToLocalStorage 失败:', error);
    throw error;
  }
};

/**
 * 读取 localStorage 并解析为对象。
 * @param {string} key - 本地存储键名
 * @returns {any|null} 返回解析后的数据或 null
 */
export const getFromLocalStorage = (key) => {
  try {
    const raw = localStorage.getItem(key);
    return raw ? JSON.parse(raw) : null;
  } catch (error) {
    console.error('getFromLocalStorage 失败:', error);
    return null;
  }
};
