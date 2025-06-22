<template>
  <div class="exam-app-view">
    <div class="chat-container">
      <div class="chat-header">
        <div class="chat-title">
          <icon-book class="chat-icon" />
          <h1>智慧答题助手</h1>
        </div>
        <a-button type="outline" @click="clearMessages">
          <template #icon>
            <icon-delete />
          </template>
          清除记录
        </a-button>
      </div>
        <div class="chat-description">
          我是你的智慧答题助手，可以解答各种学科问题，分析知识点，提供考试技巧。
      </div>
      
      <div class="chat-content">
        <div class="welcome-message">
          <div class="welcome-message-content">
            <h2>你好，我是智慧答题助手！</h2>
            <p>我可以帮助你:</p>
            <ul>
              <li>解答各类学科问题，从数学、英语到物理、化学等</li>
              <li>分析复杂题目，提供解题思路和步骤</li>
              <li>讲解知识点，帮助你更好地理解概念</li>
              <li>提供考试技巧和学习方法建议</li>
            </ul>
            <p>请告诉我你想了解什么，或者直接提出你的问题！</p>
          </div>
        </div>
        
        <chat-message
          v-for="(message, index) in messages"
          :key="index"
          :text="message.content"
          :is-user="message.isUser"
          :sender-name="message.isUser ? '我' : '答题助手'"
          :timestamp="message.timestamp"
        />
        
        <div v-if="isLoading" class="typing-indicator">
          <a-spin dot />
          <span>答题助手正在回复...</span>
        </div>
      </div>
      
      <div class="chat-input">
        <a-input-search
          placeholder="输入你的问题..."
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
import { connectToQuizChat } from '@/services/api';
import { getOrCreateChatId } from '@/utils/uuid';
import { IconBook, IconDelete } from '@arco-design/web-vue/es/icon';

export default {
  name: 'ExamAppView',
  components: {
    ChatMessage,
    IconBook,
    IconDelete
  },
  setup() {
    const chatId = ref('');
    const currentEventSource = ref(null);
    const inputValue = ref('');
    const isLoading = ref(false);
    const messages = ref([]);
    const currentSteps = ref([]);

    chatId.value = getOrCreateChatId('exam_app_chat_id');

    const loadMessages = () => {
      const savedMessages = localStorage.getItem(`chat_messages_${chatId.value}`);
      if (savedMessages) {
        messages.value = JSON.parse(savedMessages);
      }
    };

    const saveMessages = () => {
      localStorage.setItem(`chat_messages_${chatId.value}`, JSON.stringify(messages.value));
    };

    loadMessages();
    
    const parseSteps = (text) => {
      const stepPattern = /Step \d+: 工具\[\w+\].*?(?=Step \d+:|$)/gs;
      
      let matches;
      try {
        matches = Array.from(text.matchAll(stepPattern));
      } catch (e) {
        console.error('正则匹配错误:', e);
        return [text];
      }
      
      if (matches.length === 0) {
        if (text.includes('Step') && text.includes('工具[')) {
          return [text];
        }
        
        if (text.trim()) {
          return [text];
        }
        
        return [];
      }
      
      const steps = matches.map(match => match[0].trim());
      return steps;
    };
    
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
    
    const sendMessage = (message) => {
      if (!message.trim() || isLoading.value) return;
      
      const userMessage = {
        content: message,
        isUser: true,
        timestamp: Date.now()
      };
      messages.value.push(userMessage);
      saveMessages();

      inputValue.value = '';
      isLoading.value = true;
      
      if (currentEventSource.value) {
        currentEventSource.value.close();
      }
      
      currentSteps.value = [];
      let accumulatedResponse = '';
      
      const eventSource = connectToQuizChat(
        message,
        (data) => {
          accumulatedResponse += data;
          
          const steps = parseSteps(accumulatedResponse);
          
          if (steps.length > currentSteps.value.length) {
            for (let i = currentSteps.value.length; i < steps.length; i++) {
              const stepMessage = {
                content: steps[i],
              isUser: false,
                timestamp: Date.now() + i
            };
              messages.value.push(stepMessage);
            }
            currentSteps.value = steps;
          } else if (steps.length > 0) {
            const lastStepIndex = messages.value.findIndex(
              msg => !msg.isUser && msg.content.includes(currentSteps.value[currentSteps.value.length - 1].substring(0, 20))
            );
            
            if (lastStepIndex !== -1) {
              messages.value[lastStepIndex].content = steps[steps.length - 1];
            }
            
            currentSteps.value = steps;
          }
          
          saveMessages();
        },
        (error) => {
          console.error('SSE连接错误:', error);
          isLoading.value = false;
        }
      );
      
      eventSource.addEventListener('done', () => {
        isLoading.value = false;
        eventSource.close();
      });
      
      currentEventSource.value = eventSource;
    };

    const clearMessages = () => {
      if (window.confirm('你确定要清除所有聊天记录吗？此操作不可恢复。')) {
        messages.value = [];
        localStorage.removeItem(`chat_messages_${chatId.value}`);
      }
    };
    
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

    watch(() => messages.value.length, () => {
      scrollToBottom();
    });

    watch(() => messages.value[messages.value.length - 1]?.content, () => {
      if (messages.value.length > 0) {
        scrollToBottom();
      }
    });
    
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
}
</script>

<style scoped>
.exam-app-view {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: var(--color-off-white);
}

.chat-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
  padding: 32px;
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
  background: linear-gradient(90deg, var(--color-accent), transparent);
}

.chat-title {
  display: flex;
  align-items: center;
  margin-bottom: 0;
}

.chat-icon {
  font-size: 26px;
  color: rgb(97, 0, 205);
  margin-right: 14px;
  position: relative;
  z-index: 1;
}

.chat-icon::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 40px;
  height: 40px;
  background-color: rgba(97, 0, 205, 0.06);
  border-radius: 50%;
  z-index: -1;
  animation: pulse var(--transition-breathe);
}

.chat-title h1 {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: 0.4px;
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

.welcome-message {
  background-color: rgba(97, 0, 205, 0.03);
  border-radius: 18px;
  padding: 28px 32px;
  margin-bottom: 30px;
  animation: fadeScale 0.8s ease-out;
  border: 1px solid rgba(97, 0, 205, 0.1);
  box-shadow: var(--shadow-soft);
  transition: all var(--transition-smooth);
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
}

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
    rgba(97, 0, 205, 0.15) 30%, 
    rgba(97, 0, 205, 0.15) 70%, 
    transparent
  );
}

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
      rgba(97, 0, 205, 0.03) 0%, 
      transparent 60%
    ),
    radial-gradient(
      circle at 90% 90%, 
      rgba(97, 0, 205, 0.05) 0%, 
      transparent 60%
    );
  pointer-events: none;
}

.welcome-message:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(97, 0, 205, 0.12);
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
  background: linear-gradient(90deg, rgba(97, 0, 205, 0.4), transparent);
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
  background-color: rgba(97, 0, 205, 0.5);
  box-shadow: 0 0 0 4px rgba(97, 0, 205, 0.08);
}

.chat-input {
  margin-top: 24px;
  position: relative;
}

.chat-input :deep(.arco-btn-primary) {
  background-color: rgb(97, 0, 205);
  border-color: rgb(97, 0, 205);
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.1, 0.25, 1.4);
  border-radius: 8px;
  padding: 0 20px;
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
  background-color: rgb(107, 20, 215);
  box-shadow: 0 6px 15px rgba(97, 0, 205, 0.3);
  transform: translateY(-2px);
}

.chat-input :deep(.arco-btn-primary):hover::before {
  left: 100%;
}

.chat-input :deep(.arco-btn-primary):active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(97, 0, 205, 0.3);
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
  border-color: rgba(97, 0, 205, 0.3);
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
  color: rgba(97, 0, 205, 0.7);
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
</style> 