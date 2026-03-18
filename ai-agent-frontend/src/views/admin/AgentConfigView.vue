<template>
  <div class="admin-page">
    <div class="page-header">
      <h1>智能体管理</h1>
      <a-button type="primary" @click="showAddModal">
        <template #icon><icon-plus /></template>
        新增智能体
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
      <template #llmConfigId="{ record }">
        {{ getConfigName(record.llmConfigId, llmConfigs) || '未绑定' }}
      </template>
      <template #vectorDbConfigId="{ record }">
        {{ getConfigName(record.vectorDbConfigId, vectorDbConfigs) || '未绑定' }}
      </template>
      <template #actions="{ record }">
        <a-button type="text" size="small" @click="handleEdit(record)">编辑</a-button>
        <a-button type="text" size="small" status="danger" @click="handleDelete(record)">删除</a-button>
      </template>
    </a-table>

    <a-modal v-model:visible="modalVisible" :title="isEdit ? '编辑智能体' : '新增智能体'" @ok="handleSubmit" width="600px">
      <a-form :model="form" layout="vertical">
        <a-form-item label="智能体名称" required>
          <a-input v-model="form.name" placeholder="请输入智能体名称" />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model="form.description" placeholder="请输入智能体描述" :auto-size="{ minRows: 2, maxRows: 4 }" />
        </a-form-item>
        <a-form-item label="系统提示词">
          <a-textarea v-model="form.systemPrompt" placeholder="请输入系统提示词" :auto-size="{ minRows: 4, maxRows: 8 }" />
        </a-form-item>
        <a-form-item label="绑定的大模型配置">
          <a-select v-model="form.llmConfigId" placeholder="选择大模型配置" allow-clear>
            <a-option v-for="config in llmConfigs" :key="config.id" :value="config.id">
              {{ config.name }} ({{ config.provider }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="绑定的向量数据库配置">
          <a-select v-model="form.vectorDbConfigId" placeholder="选择向量数据库配置" allow-clear>
            <a-option v-for="config in vectorDbConfigs" :key="config.id" :value="config.id">
              {{ config.name }} ({{ config.provider }})
            </a-option>
          </a-select>
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
import { getAgentList, createAgent, updateAgent, deleteAgent, getEnabledLlmConfigs, getEnabledVectorDbConfigs } from '@/services/api';
import { Message } from '@arco-design/web-vue';

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '名称', dataIndex: 'name' },
  { title: '描述', dataIndex: 'description', ellipsis: true },
  { title: 'LLM配置', slotName: 'llmConfigId', width: 150 },
  { title: '向量库配置', slotName: 'vectorDbConfigId', width: 150 },
  { title: '状态', slotName: 'enabled', width: 80 },
  { title: '创建时间', dataIndex: 'createTime', width: 180 },
  { title: '操作', slotName: 'actions', width: 150 },
];

const loading = ref(false);
const data = ref([]);
const llmConfigs = ref([]);
const vectorDbConfigs = ref([]);
const pagination = ref({ current: 1, pageSize: 10, total: 0 });
const modalVisible = ref(false);
const isEdit = ref(false);
const form = ref({
  id: null,
  name: '',
  description: '',
  systemPrompt: '',
  llmConfigId: null,
  vectorDbConfigId: null,
  enabled: true,
});

const getConfigName = (id, configs) => {
  if (!id) return null;
  const config = configs.find(c => c.id === id);
  return config ? config.name : null;
};

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getAgentList({ pageNum: pagination.value.current, pageSize: pagination.value.pageSize });
    data.value = res.data.records;
    pagination.value.total = res.data.total;
  } catch (e) {
    Message.error('加载失败');
  } finally {
    loading.value = false;
  }
};

const fetchConfigs = async () => {
  try {
    const llmRes = await getEnabledLlmConfigs();
    llmConfigs.value = llmRes.data || [];
    const vectorRes = await getEnabledVectorDbConfigs();
    vectorDbConfigs.value = vectorRes.data || [];
  } catch (e) {
    console.error('Failed to load configs:', e);
  }
};

const onPageChange = (page) => {
  pagination.value.current = page;
  fetchData();
};

const showAddModal = () => {
  isEdit.value = false;
  form.value = { id: null, name: '', description: '', systemPrompt: '', llmConfigId: null, vectorDbConfigId: null, enabled: true };
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
      await updateAgent(form.value.id, form.value);
      Message.success('更新成功');
    } else {
      await createAgent(form.value);
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
    await deleteAgent(record.id);
    Message.success('删除成功');
    fetchData();
  } catch (e) {
    Message.error('删除失败');
  }
};

onMounted(() => {
  fetchData();
  fetchConfigs();
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
