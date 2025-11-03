<template>
  <div class="app-container">
    <div class="background-decoration"></div>
    <a-layout class="layout-container">
      <!-- 侧边栏 -->
      <a-layout-sider
        class="layout-sider"
        width="220"
        hide-trigger
      >
        <div class="sider-header">
          <div class="logo">
            <img src="@/assets/logo.svg" alt="AI应用" />
            <span class="logo-text">智慧客</span>
          </div>
        </div>
        
        <div class="sider-content">
          <div class="menu-container">
            <div class="menu-header">
              <span>主页</span>
            </div>
            
            <a-menu
              :default-selected-keys="selectedKeys"
              @menu-item-click="onMenuItemClick"
            >
              <a-menu-item key="home" class="menu-item">
                <template #icon>
                  <div class="menu-icon-wrapper">
                    <icon-home />
                  </div>
                </template>
                <span class="menu-label">主页</span>
              </a-menu-item>
              <a-menu-item key="love-app" class="menu-item">
                <template #icon>
                  <div class="menu-icon-wrapper love-icon">
                    <icon-heart />
                  </div>
                </template>
                <span class="menu-label">AI 恋爱大师</span>
              </a-menu-item>
              <a-menu-item key="manus-app" class="menu-item">
                <template #icon>
                  <div class="menu-icon-wrapper manus-icon">
                    <icon-robot />
                  </div>
                </template>
                <span class="menu-label">AI 超级智能体</span>
              </a-menu-item>
              <a-menu-item key="exam-app" class="menu-item">
                <template #icon>
                  <div class="menu-icon-wrapper exam-icon">
                    <icon-book />
                  </div>
                </template>
                <span class="menu-label">智慧答题助手</span>
              </a-menu-item>
              <a-menu-item key="health-app" class="menu-item">
                <template #icon>
                  <div class="menu-icon-wrapper health-icon">
                    <icon-heart-fill />
                  </div>
                </template>
                <span class="menu-label">云医通健康助手</span>
              </a-menu-item>
            </a-menu>
          </div>
        </div>
      </a-layout-sider>

      <!-- 内容区域 -->
      <a-layout>
        <a-layout-content class="layout-content">
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { 
  IconHeart, 
  IconRobot, 
  IconBook,
  IconHome,
  IconHeartFill
} from '@arco-design/web-vue/es/icon';

export default {
  name: 'App',
  components: {
    IconHeart,
    IconRobot,
    IconBook,
    IconHome,
    IconHeartFill
  },
  setup() {
    const route = useRoute();
    const router = useRouter();
    
    const selectedKeys = computed(() => {
      const path = route.path;
      if (path === '/') return ['home'];
      if (path === '/love-app') return ['love-app'];
      if (path === '/manus-app') return ['manus-app'];
      if (path === '/exam-app') return ['exam-app'];
      if (path === '/health-app') return ['health-app'];
      return ['home'];
    });
    
    const onMenuItemClick = (key) => {
      switch(key) {
        case 'home':
          router.push('/');
          break;
        case 'love-app':
          router.push('/love-app');
          break;
        case 'manus-app':
          router.push('/manus-app');
          break;
        case 'exam-app':
          router.push('/exam-app');
          break;
        case 'health-app':
          router.push('/health-app');
          break;
      }
    };
    
    return {
      selectedKeys,
      onMenuItemClick
    };
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'SF Pro Display', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: var(--color-off-white);
  color: var(--text-primary);
  overflow: hidden;
}

.app-container {
  height: 100vh;
  width: 100%;
  position: relative;
}

.background-decoration {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;
  background-image: 
    radial-gradient(circle at 10% 10%, rgba(118, 156, 255, 0.03) 0%, transparent 40%),
    radial-gradient(circle at 90% 90%, rgba(255, 217, 172, 0.04) 0%, transparent 40%);
  opacity: 0.8;
}

.layout-container {
  height: 100%;
  width: 100%;
}

.layout-sider {
  background-color: rgba(255, 255, 255, 0.85);
  border-right: 1px solid var(--color-light-gray);
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: var(--shadow-soft);
  position: relative;
  z-index: 10;
  backdrop-filter: blur(10px);
  transition: all var(--transition-smooth);
}

.layout-sider:hover {
  box-shadow: var(--shadow-medium);
}

.layout-sider::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(
      circle at 20% 20%, 
      rgba(118, 156, 255, 0.03) 0%, 
      transparent 70%
    );
  pointer-events: none;
  opacity: 0.8;
  z-index: -1;
}

.sider-header {
  padding: 20px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--color-light-gray);
  position: relative;
}

.sider-header::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 80px;
  height: 1px;
  background: linear-gradient(90deg, var(--color-primary), transparent);
}

.logo {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: 0.3px;
}

.logo-text {
  background: linear-gradient(90deg, var(--text-primary), var(--text-secondary));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: 700;
}

.logo img {
  width: 28px;
  height: 28px;
  margin-right: 12px;
  filter: drop-shadow(0 2px 5px rgba(118, 156, 255, 0.3));
  transition: transform var(--transition-quick);
}

.logo:hover img {
  transform: scale(1.1) rotate(5deg);
}

.collapse-btn {
  display: none;
}

.sider-content {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  scrollbar-width: thin;
  scrollbar-color: var(--color-mid-gray) transparent;
}

.sider-content::-webkit-scrollbar {
  width: 4px;
}

.sider-content::-webkit-scrollbar-track {
  background: transparent;
}

.sider-content::-webkit-scrollbar-thumb {
  background-color: var(--color-mid-gray);
  border-radius: 10px;
}

.menu-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 10px 0;
}

.menu-header {
  padding: 16px 20px 8px 20px;
  font-size: 13px;
  color: var(--text-light);
  display: flex;
  align-items: center;
  font-weight: 500;
}

.menu-item {
  margin: 4px 10px;
  border-radius: 12px !important;
  overflow: hidden;
  transition: all var(--transition-quick) !important;
  height: 44px !important;
}

.menu-icon-wrapper {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  background-color: rgba(118, 156, 255, 0.1);
  transition: all var(--transition-quick);
  position: relative;
  overflow: hidden;
}

.menu-icon-wrapper::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at center, rgba(255, 255, 255, 0.8), transparent);
  opacity: 0;
  transition: opacity var(--transition-quick);
}

.love-icon {
  background-color: rgba(255, 130, 150, 0.1);
  color: rgb(255, 130, 150);
}

.manus-icon {
  background-color: rgba(15, 198, 194, 0.1);
  color: rgb(15, 198, 194);
}

.exam-icon {
  background-color: rgba(97, 0, 205, 0.1);
  color: rgb(97, 0, 205);
}

.health-icon {
  background-color: rgba(15, 198, 194, 0.1);
  color: rgb(15, 198, 194);
}

.menu-label {
  font-weight: 500;
  letter-spacing: 0.2px;
  transition: all var(--transition-quick);
}

.arco-menu-light .arco-menu-item {
  color: var(--text-secondary);
}

.arco-menu-light .arco-menu-item.arco-menu-selected {
  background-color: rgba(118, 156, 255, 0.1);
  color: var(--color-primary);
  font-weight: 500;
  position: relative;
}

.arco-menu-light .arco-menu-item.arco-menu-selected::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background-color: var(--color-primary);
  border-radius: 0 2px 2px 0;
}

.arco-menu-light .arco-menu-item.arco-menu-selected .menu-icon-wrapper {
  background-color: var(--color-primary);
  color: white;
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(118, 156, 255, 0.3);
}

.arco-menu-light .arco-menu-item.arco-menu-selected .menu-icon-wrapper.love-icon {
  background-color: rgb(255, 130, 150);
  box-shadow: 0 2px 8px rgba(255, 130, 150, 0.3);
}

.arco-menu-light .arco-menu-item.arco-menu-selected .menu-icon-wrapper.manus-icon {
  background-color: rgb(15, 198, 194);
  box-shadow: 0 2px 8px rgba(15, 198, 194, 0.3);
}

.arco-menu-light .arco-menu-item.arco-menu-selected .menu-icon-wrapper.exam-icon {
  background-color: rgb(97, 0, 205);
  box-shadow: 0 2px 8px rgba(97, 0, 205, 0.3);
}

.arco-menu-light .arco-menu-item.arco-menu-selected .menu-icon-wrapper.health-icon {
  background-color: rgb(15, 198, 194);
  box-shadow: 0 2px 8px rgba(15, 198, 194, 0.3);
}

.arco-menu-light .arco-menu-item:hover {
  background-color: rgba(118, 156, 255, 0.05);
  color: var(--color-primary);
}

.arco-menu-light .arco-menu-item:hover .menu-icon-wrapper {
  transform: scale(1.05);
}

.arco-menu-light .arco-menu-item:hover .menu-icon-wrapper::before {
  opacity: 0.4;
}

.arco-menu-light .arco-menu-item:hover .menu-label {
  transform: translateX(2px);
}

.layout-content {
  padding: 0;
  height: 100%;
  background-color: var(--color-off-white);
  position: relative;
  overflow: hidden;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>
