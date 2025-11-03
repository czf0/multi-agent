<template>
  <div class="love-app">
    <div class="chat-container">
      <div class="chat-header">
        <div class="chat-title">
          <icon-heart class="chat-icon" />
          <h1>AI 恋爱大师</h1>
        </div>
        <a-button type="outline" @click="clearMessages">
          <template #icon>
            <icon-delete />
          </template>
          清除记录
        </a-button>
      </div>
        <div class="chat-description">
          我是你的恋爱大师，可以为你提供情感、约会和关系方面的建议与支持。
      </div>
      
      <div class="chat-content">
        <div class="welcome-message">
          <div class="welcome-message-content">
            <h2>你好，我是AI恋爱大师！</h2>
            <p>我可以帮助你:</p>
            <ul>
              <li>提供情感关系咨询，解答感情困惑</li>
              <li>给出约会建议，提高约会成功率</li>
              <li>分析关系问题，改善沟通技巧</li>
              <li>设计浪漫惊喜，增添感情乐趣</li>
            </ul>
            <p>请告诉我你的情感困扰，或直接提出你的问题！</p>
          </div>
        </div>
        
        <chat-message
          v-for="(message, index) in messages"
          :key="index"
          :text="message.content"
          :is-user="message.isUser"
          :sender-name="message.isUser ? '我' : '恋爱大师'"
          :timestamp="message.timestamp"
        />
        
        <div v-if="isLoading" class="typing-indicator">
          <a-spin dot />
          <span>恋爱大师正在回复...</span>
        </div>
      </div>
      
      <div class="chat-input">
        <a-input-search
          placeholder="告诉我你的情感疑问..."
          button-text="发送"
          search-button
          @search="sendMessage"
          v-model="inputValue"
          :loading="isLoading"
          :disabled="isLoading"
          @keydown="handleKeydown"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { onBeforeUnmount, ref, watch, nextTick } from 'vue';
import ChatMessage from '@/components/ChatMessage.vue';
import { connectToLoveAppSse } from '@/services/api';
import { getOrCreateChatId } from '@/utils/uuid';
import { IconHeart, IconDelete } from '@arco-design/web-vue/es/icon';

export default {
  name: 'LoveAppView',
  components: {
    ChatMessage,
    IconHeart,
    IconDelete
  },
  setup() {
    const chatId = ref('');
    const currentEventSource = ref(null);
    const inputValue = ref('');
    const isLoading = ref(false);
    const messages = ref([]);
    
    // 获取或创建聊天 ID
    chatId.value = getOrCreateChatId('love_app_chat_id');
    
    // 从localStorage加载历史消息
    const loadMessages = () => {
      const savedMessages = localStorage.getItem(`chat_messages_${chatId.value}`);
      if (savedMessages) {
        messages.value = JSON.parse(savedMessages);
      }
    };
    
    // 保存消息到localStorage
    const saveMessages = () => {
      localStorage.setItem(`chat_messages_${chatId.value}`, JSON.stringify(messages.value));
    };
    
    // 加载消息历史
    loadMessages();
    
    const clearMessages = () => {
      if (window.confirm('你确定要清除所有聊天记录吗？此操作不可恢复。')) {
        messages.value = [];
        localStorage.removeItem(`chat_messages_${chatId.value}`);
      }
    };
    
    // 自动滚动到底部
    const scrollToBottom = () => {
      nextTick(() => {
        const chatContent = document.querySelector('.chat-content');
        if (chatContent) {
          chatContent.scrollTo({
            top: chatContent.scrollHeight,
            behavior: 'smooth'
          });
        }
      });
    };

    // 监听消息列表变化，自动滚动
    watch(() => messages.value. gth, () => {
      scrollToBottom();
    });

    // 监听最后一条消息内容变化，自动滚动
    watch(() => messages.value[messages.value. gth - 1]?.content, () => {
      if (messages.value. gth > 0) {
        scrollToBottom();
      }
    });
    
    // 处理键盘事件
    const handleKeydown = (e) => {
      if (e.key === 'Enter') {
        if (e.shiftKey) {
          // Shift+Enter: 插入换行符
          inputValue.value += '\n';
          e.preventDefault();
        } else if (!isLoading.value) {
          // 单独的Enter: 发送消息
          sendMessage(inputValue.value);
          e.preventDefault();
        }
      }
    };
    
    // 发送消息给后端
    const sendMessage = (message) => {
      if (!message.trim() || isLoading.value) return;
      
      // 添加用户消息
      const userMessage = {
        content: message,
        isUser: true,
        timestamp: Date.now()
      };
      messages.value.push(userMessage);
      saveMessages();
      
      // 清空输入框
      inputValue.value = '';
      isLoading.value = true;
      
      // 如果有现存的连接，先关闭
      if (currentEventSource.value) {
        currentEventSource.value.close();
      }
      
      // 用于累积SSE消息的变量
      let accumulatedResponse = '';
      
      // 建立新的 SSE 连接
      const eventSource = connectToLoveAppSse(
        message,
        chatId.value,
        (data) => {
          // 累加新收到的消息片段
          accumulatedResponse += data;
          
          // 更新或创建AI回复消息
          const aiMessageIndex = messages.value.findIndex(
            msg => !msg.isUser && msg.timestamp > userMessage.timestamp
          );
          
          if (aiMessageIndex !== -1) {
            // 更新现有消息
            messages.value[aiMessageIndex].content = accumulatedResponse;
          } else {
            // 创建新消息
            const aiMessage = {
              content: accumulatedResponse,
              isUser: false,
              timestamp: Date.now()
            };
            messages.value.push(aiMessage);
          }
          
          saveMessages();
        },
        (error) => {
          console.error('SSE连接错误:', error);
          isLoading.value = false;
        }
      );
      
      // 当连接关闭时
      eventSource.addEventListener('done', () => {
        isLoading.value = false;
        eventSource.close();
      });
      
      // 保存当前连接，以便后续可以关闭
      currentEventSource.value = eventSource;
    };
    
    // 组件卸载前关闭SSE连接
    onBeforeUnmount(() => {
      if (currentEventSource.value) {
        currentEventSource.value.close();
        currentEventSource.value = null;
      }
    });
    
    return {
      chatId,
      inputValue,
      isLoading,
      messages,
      sendMessage,
      clearMessages,
      handleKeydown
    };
  }
};
</script>

<style scoped>
.love-app {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: var(--color-off-white);
  position: relative;
  overflow: hidden;
}

/* 添加爱情主题背景装饰 */
.love-app::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(circle at 10% 10%, rgba(255, 130, 150, 0.03) 0%, transparent 50%),
    radial-gradient(circle at 90% 90%, rgba(255, 217, 172, 0.04) 0%, transparent 50%);
  pointer-events: none;
  z-index: 0;
}

.chat-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
  padding: 32px;
  position: relative;
  z-index: 1;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--color-mid-gray);
  position: relative;
}

.chat-header::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100px;
  height: 2px;
  background: linear-gradient(90deg, rgb(255, 130, 150), transparent);
}

.chat-title {
  display: flex;
  align-items: center;
  margin-bottom: 0;
}

.chat-icon {
  font-size: 26px;
  color: rgb(255, 130, 150);
  margin-right: 14px;
  position: relative;
  z-index: 1;
}

/* 动态图标效果 */
.chat-icon::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 40px;
  height: 40px;
  background-color: rgba(255, 130, 150, 0.08);
  border-radius: 50%;
  z-index: -1;
  animation: pulse var(--transition-breathe);
}

/* 粒子效果 */
.chat-icon::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: radial-gradient(circle at center, rgba(255, 255, 255, 0.8), transparent 70%);
  filter: blur(2px);
  opacity: 0;
  animation: sparkle 3s ease-in-out infinite;
}

.chat-title h1 {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: 0.4px;
  position: relative;
}

.chat-title h1::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 40%;
  height: 2px;
  background: linear-gradient(90deg, rgba(255, 130, 150, 0.5), transparent);
  opacity: 0.7;
}

.chat-description {
  width: 100%;
  margin-bottom: 30px;
  font-size: 15px;
  color: var(--text-secondary);
  padding: 0 2px;
  line-height: 1.6;
}

.chat-content {
  flex: 1;
  overflow-y: auto;
  padding-right: 12px;
  scrollbar-width: thin;
  scrollbar-color: var(--color-mid-gray) transparent;
}

.chat-content::-webkit-scrollbar {
  width: 8px;
}

.chat-content::-webkit-scrollbar-track {
  background: transparent;
}

.chat-content::-webkit-scrollbar-thumb {
  background-color: var(--color-mid-gray);
  border-radius: 10px;
  border: 3px solid var(--color-off-white);
}

/* 层次化卡片 */
.welcome-message {
  background-color: rgba(255, 217, 172, 0.08);
  border-radius: 18px;
  padding: 28px 32px;
  margin-bottom: 30px;
  animation: fadeScale 0.8s ease-out;
  border: 1px solid rgba(255, 130, 150, 0.12);
  box-shadow: var(--shadow-soft);
  transition: all var(--transition-smooth);
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
}

/* 纸张折痕效果 */
.welcome-message::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(
    90deg, 
    transparent, 
    rgba(255, 130, 150, 0.2) 30%, 
    rgba(255, 130, 150, 0.2) 70%, 
    transparent
  );
}

/* 能量波纹效果 */
.welcome-message::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(
      circle at 10% 10%, 
      rgba(255, 130, 150, 0.04) 0%, 
      transparent 60%
    ),
    radial-gradient(
      circle at 90% 90%, 
      rgba(255, 217, 172, 0.06) 0%, 
      transparent 60%
    );
  pointer-events: none;
}

.welcome-message:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(255, 130, 150, 0.15);
}

.welcome-message-content h2 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-top: 0;
  margin-bottom: 18px;
  position: relative;
  display: inline-block;
}

.welcome-message-content h2::after {
  content: '';
  position: absolute;
  bottom: -6px;
  left: 0;
  width: 40%;
  height: 2px;
  background: linear-gradient(90deg, rgba(255, 130, 150, 0.5), transparent);
}

.welcome-message-content p {
  font-size: 15px;
  color: var(--text-secondary);
  margin-bottom: 14px;
  line-height: 1.6;
}

.welcome-message-content ul {
  padding-left: 20px;
  margin-bottom: 16px;
  list-style-type: none;
}

.welcome-message-content li {
  font-size: 15px;
  color: var(--text-secondary);
  margin-bottom: 10px;
  position: relative;
  padding-left: 24px;
  line-height: 1.5;
}

.welcome-message-content li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: rgba(255, 130, 150, 0.6);
  box-shadow: 0 0 0 4px rgba(255, 130, 150, 0.1);
}

.chat-input {
  margin-top: 24px;
  position: relative;
}

/* 呼吸感按钮 */
.chat-input :deep(.arco-btn-primary) {
  background-color: rgba(255, 130, 150, 0.9);
  border-color: rgba(255, 130, 150, 0.9);
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.1, 0.25, 1.4);
  border-radius: 8px;
  padding: 0 20px;
  backdrop-filter: blur(5px);
}

.chat-input :deep(.arco-btn-primary)::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg, 
    transparent, 
    rgba(255, 255, 255, 0.2), 
    transparent
  );
  transition: all 0.6s;
}

.chat-input :deep(.arco-btn-primary):hover {
  background-color: rgba(255, 110, 130, 0.95);
  box-shadow: 0 6px 15px rgba(255, 130, 150, 0.3);
  transform: translateY(-2px);
}

.chat-input :deep(.arco-btn-primary):hover::before {
  left: 100%;
}

.chat-input :deep(.arco-btn-primary):active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(255, 130, 150, 0.3);
}

.chat-input :deep(.arco-input-search) {
  box-shadow: var(--shadow-soft);
  border-radius: 12px;
  transition: all var(--transition-smooth);
  border: 1px solid var(--color-mid-gray);
  background-color: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
}

.chat-input :deep(.arco-input-wrapper) {
  border-radius: 12px;
  padding: 6px 8px;
}

.chat-input :deep(.arco-input-search):focus-within {
  box-shadow: var(--shadow-medium);
  border-color: rgba(255, 130, 150, 0.3);
  background-color: rgba(255, 255, 255, 0.9);
}

.chat-input :deep(.arco-input-inner-wrapper) {
  padding: 4px 8px;
}

.chat-input :deep(.arco-input) {
  font-size: 15px;
  padding: 8px 4px;
  color: var(--text-primary);
}

.chat-input :deep(.arco-input)::placeholder {
  color: var(--text-light);
  font-style: italic;
}

/* 清除记录按钮样式 */
.chat-header :deep(.arco-btn-outline) {
  border-color: rgba(255, 130, 150, 0.3);
  color: rgba(255, 130, 150, 0.9);
  background-color: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(5px);
  transition: all var(--transition-smooth);
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(255, 130, 150, 0.1);
}

.chat-header :deep(.arco-btn-outline):hover {
  border-color: rgba(255, 130, 150, 0.5);
  color: rgba(255, 130, 150, 1);
  background-color: rgba(255, 255, 255, 0.9);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 130, 150, 0.15);
}

.chat-header :deep(.arco-btn-outline):active {
  transform: translateY(0);
}

.typing-indicator {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  margin-bottom: 20px;
  gap: 12px;
  color: var(--text-secondary);
  font-size: 15px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 12px;
  box-shadow: var(--shadow-soft);
  backdrop-filter: blur(5px);
  animation: fadeScale 0.4s ease-out;
}

.typing-indicator :deep(.arco-spin-dot) {
  color: rgba(255, 130, 150, 0.8);
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes fadeScale {
  0% {
    opacity: 0;
    transform: scale(0.8);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes sparkle {
  0%, 100% {
    opacity: 0;
    transform: scale(0.8);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.2);
  }
}
</style> 