<template>
  <div class="admin-page">
    <div class="page-header">
      <h1>LLM 配置管理</h1>
      <a-button type="primary" @click="showAddModal">
        <template #icon><icon-plus /></template>
        新增配置
      </a-button>
    </div>

    <a-table
      :columns="columns"
      :data="data"
      :loading="loading"
      :pagination="pagination"
      @page-change="onPageChange"
    >
      <template #enabled="{ record }">
        <a-tag :color="record.enabled ? 'green' : 'red'">
          {{ record.enabled ? '启用' : '禁用' }}
        </a-tag>
      </template>
      <template #actions="{ record }">
        <a-button type="text" size="small" @click="handleEdit(record)">编辑</a-button>
        <a-button type="text" size="small" status="danger" @click="handleDelete(record)">删除</a-button>
      </template>
    </a-table>

    <a-modal v-model:visible="modalVisible" :title="isEdit ? '编辑配置' : '新增配置'" @ok="handleSubmit">
      <a-form :model="form" layout="vertical">
        <a-form-item label="配置名称" required>
          <a-input v-model="form.name" placeholder="请输入配置名称" />
        </a-form-item>
        <a-form-item label="供应商" required>
          <a-select v-model="form.provider" placeholder="请选择供应商">
            <a-option value="DASHSCOPE">阿里云百练 (DashScope)</a-option>
            <a-option value="OLLAMA">Ollama</a-option>
            <a-option value="OPENAI">OpenAI</a-option>
            <a-option value="DEEPSEEK">DeepSeek</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="模型名称" required>
          <a-input v-model="form.modelName" placeholder="如: qwen-plus, gpt-4o" />
        </a-form-item>
        <a-form-item label="API Key">
          <a-input v-model="form.apiKey" placeholder="API Key" />
        </a-form-item>
        <a-form-item label="API Base URL">
          <a-input v-model="form.apiBaseUrl" placeholder="留空使用默认" />
        </a-form-item>
        <a-form-item label="优先级">
          <a-input-number v-model="form.priority" :min="0" />
        </a-form-item>
        <a-form-item label="是否启用">
          <a-switch v-model="form.enabled" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getLlmConfigList, createLlmConfig, updateLlmConfig, deleteLlmConfig } from '@/services/api';
import { Message } from '@arco-design/web-vue';

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '配置名称', dataIndex: 'name' },
  { title: '供应商', dataIndex: 'provider' },
  { title: '模型名称', dataIndex: 'modelName' },
  { title: '优先级', dataIndex: 'priority', width: 80 },
  { title: '状态', slotName: 'enabled', width: 80 },
  { title: '操作', slotName: 'actions', width: 150 },
];

const loading = ref(false);
const data = ref([]);
const pagination = ref({ current: 1, pageSize: 10, total: 0 });
const modalVisible = ref(false);
const isEdit = ref(false);
const form = ref({
  id: null,
  name: '',
  provider: 'DASHSCOPE',
  modelName: '',
  apiKey: '',
  apiBaseUrl: '',
  priority: 0,
  enabled: true,
});

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getLlmConfigList({ pageNum: pagination.value.current, pageSize: pagination.value.pageSize });
    data.value = res.data.records;
    pagination.value.total = res.data.total;
  } catch (e) {
    Message.error('加载失败');
  } finally {
    loading.value = false;
  }
};

const onPageChange = (page) => {
  pagination.value.current = page;
  fetchData();
};

const showAddModal = () => {
  isEdit.value = false;
  form.value = { id: null, name: '', provider: 'DASHSCOPE', modelName: '', apiKey: '', apiBaseUrl: '', priority: 0, enabled: true };
  modalVisible.value = true;
};

const handleEdit = (record) => {
  isEdit.value = true;
  form.value = { ...record };
  modalVisible.value = true;
};

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateLlmConfig(form.value.id, form.value);
      Message.success('更新成功');
    } else {
      await createLlmConfig(form.value);
      Message.success('创建成功');
    }
    modalVisible.value = false;
    fetchData();
  } catch (e) {
    Message.error('操作失败');
  }
};

const handleDelete = async (record) => {
  try {
    await deleteLlmConfig(record.id);
    Message.success('删除成功');
    fetchData();
  } catch (e) {
    Message.error('删除失败');
  }
};

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.admin-page {
  padding: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
