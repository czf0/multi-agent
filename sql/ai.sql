-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS ai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE ai;

-- 创建对话记忆表
CREATE TABLE IF NOT EXISTS chatmemory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    message_order INT NOT NULL,
    message_type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    message_json TEXT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete BOOLEAN DEFAULT 0,
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_conversation_order (conversation_id, message_order),
    INDEX idx_is_delete (is_delete)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建大模型配置表
CREATE TABLE IF NOT EXISTS llm_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '配置名称',
    provider VARCHAR(50) NOT NULL COMMENT '供应商(DASHSCOPE/OLLAMA/DEEPSEEK/OPENAI/CLAUDE)',
    model_name VARCHAR(100) NOT NULL COMMENT '模型名称',
    api_key VARCHAR(500) COMMENT 'API Key',
    api_base_url VARCHAR(255) COMMENT 'API Base URL',
    enabled BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    priority INT DEFAULT 0 COMMENT '优先级',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_provider (provider),
    INDEX idx_enabled (enabled),
    INDEX idx_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建向量数据库配置表
CREATE TABLE IF NOT EXISTS vector_db_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '配置名称',
    provider VARCHAR(50) NOT NULL COMMENT '供应商(PGVECTOR/MILVUS/ES/CHROMA)',
    host VARCHAR(255) COMMENT '主机地址',
    port INT COMMENT '端口',
    database_name VARCHAR(100) COMMENT '数据库名',
    username VARCHAR(100) COMMENT '用户名',
    password VARCHAR(255) COMMENT '密码',
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_provider (provider),
    INDEX idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建智能体表
CREATE TABLE IF NOT EXISTS agent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '智能体名称',
    description TEXT COMMENT '智能体描述',
    system_prompt TEXT COMMENT '系统提示词',
    llm_config_id BIGINT COMMENT '绑定的大模型配置ID',
    vector_db_config_id BIGINT COMMENT '绑定的向量数据库配置ID',
    enabled BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_name (name),
    INDEX idx_enabled (enabled),
    INDEX idx_llm_config_id (llm_config_id),
    INDEX idx_vector_db_config_id (vector_db_config_id),
    FOREIGN KEY (llm_config_id) REFERENCES llm_config(id),
    FOREIGN KEY (vector_db_config_id) REFERENCES vector_db_config(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


