<template>
  <div class="admin-page">
    <div class="page-header">
      <h1>向量数据库配置管理</h1>
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
            <a-option value="PGVECTOR">PGVector</a-option>
            <a-option value="MILVUS">Milvus</a-option>
            <a-option value="ES">Elasticsearch</a-option>
            <a-option value="CHROMA">Chroma</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="主机地址">
          <a-input v-model="form.host" placeholder="如: localhost" />
        </a-form-item>
        <a-form-item label="端口">
          <a-input-number v-model="form.port" :min="1" :max="65535" />
        </a-form-item>
        <a-form-item label="数据库名">
          <a-input v-model="form.databaseName" placeholder="数据库名" />
        </a-form-item>
        <a-form-item label="用户名">
          <a-input v-model="form.username" placeholder="用户名" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input-password v-model="form.password" placeholder="密码" />
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
import { getVectorDbConfigList, createVectorDbConfig, updateVectorDbConfig, deleteVectorDbConfig } from '@/services/api';
import { Message } from '@arco-design/web-vue';

const columns = [
  { title: 'ID', dataIndex: 'id', width: 60 },
  { title: '配置名称', dataIndex: 'name' },
  { title: '供应商', dataIndex: 'provider' },
  { title: '主机', dataIndex: 'host' },
  { title: '端口', dataIndex: 'port', width: 80 },
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
  provider: 'PGVECTOR',
  host: '',
  port: 5432,
  databaseName: '',
  username: '',
  password: '',
  enabled: true,
});

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getVectorDbConfigList({ pageNum: pagination.value.current, pageSize: pagination.value.pageSize });
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
  form.value = { id: null, name: '', provider: 'PGVECTOR', host: '', port: 5432, databaseName: '', username: '', password: '', enabled: true };
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
      await updateVectorDbConfig(form.value.id, form.value);
      Message.success('更新成功');
    } else {
      await createVectorDbConfig(form.value);
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
    await deleteVectorDbConfig(record.id);
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
