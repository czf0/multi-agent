<template>
  <div class="admin-page">
    <div class="page-header">
      <h1>智能体模型绑定</h1>
      <a-button type="primary" @click="showAddModal">
        <template #icon><icon-plus /></template>
        新增绑定
      </a-button>
    </div>

    <a-table
      :columns="columns"
      :data="data"
      :loading="loading"
      :pagination="pagination"
      @page-change="onPageChange"
    >
      <template #isDefault="{ record }">
        <a-tag :color="record.isDefault ? 'blue' : 'gray'">
          {{ record.isDefault ? '默认' : '-' }}
        </a-tag>
      </template>
      <template #actions="{ record }">
        <a-button v-if="!record.isDefault" type="text" size="small" @click="handleSetDefault(record)">设为默认</a-button>
        <a-button type="text" size="small" @click="handleEdit(record)">编辑</a-button>
        <a-button type="text" size="small" status="danger" @click="handleDelete(record)">删除</a-button>
      </template>
    </a-table>

    <a-modal v-model:visible="modalVisible" :title="isEdit ? '编辑绑定' : '新增绑定'" @ok="handleSubmit">
      <a-form :model="form" layout="vertical">
        <a-form-item label="智能体名称" required>
          <a-select v-model="form.agentName" placeholder="请选择智能体">
            <a-option value="Manus">Manus</a-option>
            <a-option value="LoveApp">LoveApp</a-option>
            <a-option value="QuizAssistant">QuizAssistant</a-option>
            <a-option value="HealthAssistant">HealthAssistant</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="LLM 配置" required>
          <a-select v-model="form.llmConfigId" placeholder="请选择LLM配置">
            <a-option v-for="c in llmConfigs" :key="c.id" :value="c.id">{{ c.name }} - {{ c.modelName }}</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="向量数据库配置">
          <a-select v-model="form.vectorDbConfigId" placeholder="请选择向量数据库配置" allow-clear>
            <a-option v-for="c in vectorDbConfigs" :key="c.id" :value="c.id">{{ c.name }}</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="设为默认">
          <a-switch v-model="form.isDefault" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getAgentBindingList, createAgentBinding, updateAgentBinding, deleteAgentBinding, getEnabledLlmConfigs, getEnabledVectorDbConfigs, setDefaultAgentBinding } from '@/services/api';
import { Message } from '@arco-design/web-vue';

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '智能体', dataIndex: 'agentName' },
  { title: 'LLM配置', dataIndex: 'llmConfigName' },
  { title: '向量DB配置', dataIndex: 'vectorDbConfigName' },
  { title: '默认', slotName: 'isDefault', width: 80 },
  { title: '操作', slotName: 'actions', width: 200 },
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
  agentName: '',
  llmConfigId: null,
  vectorDbConfigId: null,
  isDefault: false,
});

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getAgentBindingList({ pageNum: pagination.value.current, pageSize: pagination.value.pageSize });
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
    llmConfigs.value = llmRes.data;
    const vectorRes = await getEnabledVectorDbConfigs();
    vectorDbConfigs.value = vectorRes.data;
  } catch (e) {
    console.error('加载配置失败', e);
  }
};

const onPageChange = (page) => {
  pagination.value.current = page;
  fetchData();
};

const showAddModal = () => {
  isEdit.value = false;
  form.value = { id: null, agentName: '', llmConfigId: null, vectorDbConfigId: null, isDefault: false };
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
      await updateAgentBinding(form.value.id, form.value);
      Message.success('更新成功');
    } else {
      await createAgentBinding(form.value);
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
    await deleteAgentBinding(record.id);
    Message.success('删除成功');
    fetchData();
  } catch (e) {
    Message.error('删除失败');
  }
};

const handleSetDefault = async (record) => {
  try {
    await setDefaultAgentBinding(record.id, record.agentName);
    Message.success('设置成功');
    fetchData();
  } catch (e) {
    Message.error('设置失败');
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
