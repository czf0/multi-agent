import axios from 'axios';
import { EventSourcePolyfill } from 'event-source-polyfill';
// 如果上面的导入方式不起作用，可以改用原生 EventSource
const EventSource = window.EventSource || EventSourcePolyfill;

const API_BASE_URL = 'http://localhost:8102/api';

// 创建axios实例
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// AI恋爱大师应用的SSE连接
export const connectToLoveAppSse = (message, chatId, onMessage, onError) => {
  const url = `${API_BASE_URL}/ai/love_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`;
  const eventSource = new EventSource(url);
  
  eventSource.onmessage = (event) => {
    if (onMessage && event.data) {
      onMessage(event.data);
    }
  };
  
  eventSource.onerror = (error) => {
    if (onError) {
      onError(error);
    }
    eventSource.close();
  };
  
  return eventSource;
};

// AI超级智能体应用的SSE连接
export const connectToManusChat = (message, onMessage, onError) => {
  const url = `${API_BASE_URL}/ai/manus/chat?message=${encodeURIComponent(message)}`;
  const eventSource = new EventSource(url);
  
  eventSource.onmessage = (event) => {
    if (onMessage && event.data) {
      onMessage(event.data);
    }
  };
  
  eventSource.onerror = (error) => {
    if (onError) {
      onError(error);
    }
    eventSource.close();
  };
  
  return eventSource;
};

// 智慧答题助手应用的SSE连接
export const connectToQuizChat = (message, onMessage, onError) => {
  const url = `${API_BASE_URL}/ai/quiz/chat?message=${encodeURIComponent(message)}`;
  const eventSource = new EventSource(url);
  
  eventSource.onmessage = (event) => {
    if (onMessage && event.data) {
      onMessage(event.data);
    }
  };
  
  eventSource.onerror = (error) => {
    if (onError) {
      onError(error);
    }
    eventSource.close();
  };
  
  return eventSource;
};

// 云医通健康助手应用的SSE连接
export const connectToHealthChat = (message, onMessage, onError) => {
  const url = `${API_BASE_URL}/ai/health/chat?message=${encodeURIComponent(message)}`;
  const eventSource = new EventSource(url);

  eventSource.onmessage = (event) => {
    if (onMessage && event.data) {
      onMessage(event.data);
    }
  };

  eventSource.onerror = (error) => {
    if (onError) {
      onError(error);
    }
    eventSource.close();
  };

  return eventSource;
};

// ==================== Admin API ====================

// LLM Config APIs
export const getLlmConfigList = (params) => {
  return apiClient.get('/admin/llm-config/list', { params });
};

export const getLlmConfig = (id) => {
  return apiClient.get(`/admin/llm-config/${id}`);
};

export const createLlmConfig = (data) => {
  return apiClient.post('/admin/llm-config', data);
};

export const updateLlmConfig = (id, data) => {
  return apiClient.put(`/admin/llm-config/${id}`, data);
};

export const deleteLlmConfig = (id) => {
  return apiClient.delete(`/admin/llm-config/${id}`);
};

export const testLlmConfig = (data) => {
  return apiClient.post('/admin/llm-config/test', data);
};

export const getEnabledLlmConfigs = () => {
  return apiClient.get('/admin/llm-config/enabled');
};

export const getLlmConfigsByAgent = (agentName) => {
  return apiClient.get(`/admin/llm-config/agent/${agentName}`);
};

export const disableLlmConfig = (id) => {
  return apiClient.patch(`/admin/llm-config/${id}/disable`);
};

export const enableLlmConfig = (id) => {
  return apiClient.patch(`/admin/llm-config/${id}/enable`);
};

// Vector DB Config APIs
export const getVectorDbConfigList = (params) => {
  return apiClient.get('/admin/vector-db-config/list', { params });
};

export const getVectorDbConfig = (id) => {
  return apiClient.get(`/admin/vector-db-config/${id}`);
};

export const createVectorDbConfig = (data) => {
  return apiClient.post('/admin/vector-db-config', data);
};

export const updateVectorDbConfig = (id, data) => {
  return apiClient.put(`/admin/vector-db-config/${id}`, data);
};

export const deleteVectorDbConfig = (id) => {
  return apiClient.delete(`/admin/vector-db-config/${id}`);
};

export const testVectorDbConfig = (data) => {
  return apiClient.post('/admin/vector-db-config/test', data);
};

export const getEnabledVectorDbConfigs = () => {
  return apiClient.get('/admin/vector-db-config/enabled');
};

// Agent Binding APIs
export const getAgentBindingList = (params) => {
  return apiClient.get('/admin/agent-binding/list', { params });
};

export const getAgentBinding = (id) => {
  return apiClient.get(`/admin/agent-binding/${id}`);
};

export const createAgentBinding = (data) => {
  return apiClient.post('/admin/agent-binding', data);
};

export const updateAgentBinding = (id, data) => {
  return apiClient.put(`/admin/agent-binding/${id}`, data);
};

export const deleteAgentBinding = (id) => {
  return apiClient.delete(`/admin/agent-binding/${id}`);
};

export const getAgentBindingsByAgentId = (agentId) => {
  return apiClient.get(`/admin/agent-binding/agent/${agentId}`);
};

export const getAgentBindingsByAgent = (agentName) => {
  return apiClient.get(`/admin/agent-binding/agent/${agentName}`);
};

export const getDefaultAgentBindingByAgentId = (agentId) => {
  return apiClient.get(`/admin/agent-binding/agent/${agentId}/default`);
};

export const getDefaultAgentBinding = (agentName) => {
  return apiClient.get(`/admin/agent-binding/agent/${agentName}/default`);
};

export const setDefaultAgentBindingByAgentId = (id, agentId) => {
  return apiClient.put(`/admin/agent-binding/${id}/set-default?agentId=${agentId}`);
};

export const setDefaultAgentBinding = (id, agentName) => {
  return apiClient.put(`/admin/agent-binding/${id}/set-default?agentName=${agentName}`);
};

// Agent APIs
export const getAgentList = (params) => {
  return apiClient.get('/admin/agent/list', { params });
};

export const getAgent = (id) => {
  return apiClient.get(`/admin/agent/${id}`);
};

export const createAgent = (data) => {
  return apiClient.post('/admin/agent', data);
};

export const updateAgent = (id, data) => {
  return apiClient.put(`/admin/agent/${id}`, data);
};

export const deleteAgent = (id) => {
  return apiClient.delete(`/admin/agent/${id}`);
};

export const getEnabledAgents = () => {
  return apiClient.get('/admin/agent/enabled');
};

export const disableAgent = (id) => {
  return apiClient.patch(`/admin/agent/${id}/disable`);
};

export const enableAgent = (id) => {
  return apiClient.patch(`/admin/agent/${id}/enable`);
};

// Unified Chat API
export const connectToAgentChat = (agentId, message, chatId, onMessage, onError) => {
  const url = `${API_BASE_URL}/ai/chat/${agentId}?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`;
  const eventSource = new EventSource(url);

  eventSource.onmessage = (event) => {
    if (onMessage && event.data) {
      onMessage(event.data);
    }
  };

  eventSource.onerror = (error) => {
    if (onError) {
      onError(error);
    }
    eventSource.close();
  };

  return eventSource;
};

// Cache APIs
export const refreshCache = (configId) => {
  return apiClient.post('/admin/refresh-cache', null, { params: { configId } });
}; 