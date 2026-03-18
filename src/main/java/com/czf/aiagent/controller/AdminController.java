package com.czf.aiagent.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czf.aiagent.entity.Agent;
import com.czf.aiagent.entity.LlmConfig;
import com.czf.aiagent.entity.VectorDbConfig;
import com.czf.aiagent.service.AgentService;
import com.czf.aiagent.service.DynamicModelService;
import com.czf.aiagent.service.LlmConfigService;
import com.czf.aiagent.service.VectorDbConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 后台管理Controller - 管理LLM配置、向量数据库配置、智能体
 *
 * @author Jofend
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "后台管理", description = "管理大模型配置、向量数据库配置、智能体")
public class AdminController {

    private final LlmConfigService llmConfigService;
    private final VectorDbConfigService vectorDbConfigService;
    private final AgentService agentService;
    private final DynamicModelService dynamicModelService;

    // ==================== 刷新缓存接口 ====================

    /**
     * 刷新模型缓存
     *
     * @param configId 可选的配置ID，如果为空则清空所有缓存
     * @return 缓存刷新结果信息
     */
    @PostMapping("/refresh-cache")
    @Operation(summary = "刷新模型缓存")
    public String refreshCache(@RequestParam(required = false) Long configId) {
        if (configId != null) {
            dynamicModelService.refreshConfig(configId);
            return "Cache refreshed for config: " + configId;
        } else {
            dynamicModelService.clearAllCache();
            return "All cache cleared";
        }
    }

    // ==================== 智能体管理接口 ====================

    /**
     * 获取智能体列表
     *
     * @param pageNum 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param name 可选的智能体名称搜索关键字
     * @return 分页后的智能体列表
     */
    @GetMapping("/agent/list")
    @Operation(summary = "获取智能体列表")
    public Page<Agent> listAgents(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Agent> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Agent> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Agent::getName, name);
        }
        wrapper.orderByDesc(Agent::getId);
        return agentService.page(page, wrapper);
    }

    @GetMapping("/agent/{id}")
    @Operation(summary = "获取智能体详情")
    public Agent getAgent(@PathVariable Long id) {
        return agentService.getById(id);
    }

    @PostMapping("/agent")
    @Operation(summary = "创建智能体")
    public boolean createAgent(@RequestBody Agent agent) {
        return agentService.save(agent);
    }

    @PutMapping("/agent/{id}")
    @Operation(summary = "更新智能体")
    public boolean updateAgent(@PathVariable Long id, @RequestBody Agent agent) {
        agent.setId(id);
        return agentService.updateById(agent);
    }

    @DeleteMapping("/agent/{id}")
    @Operation(summary = "删除智能体")
    public boolean deleteAgent(@PathVariable Long id) {
        return agentService.removeById(id);
    }

    @GetMapping("/agent/enabled")
    @Operation(summary = "获取启用的智能体列表")
    public java.util.List<Agent> listEnabledAgents() {
        return agentService.list(new LambdaQueryWrapper<Agent>().eq(Agent::getEnabled, true));
    }

    @PatchMapping("/agent/{id}/disable")
    @Operation(summary = "禁用智能体")
    public boolean disableAgent(@PathVariable Long id) {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setEnabled(false);
        return agentService.updateById(agent);
    }

    @PatchMapping("/agent/{id}/enable")
    @Operation(summary = "启用智能体")
    public boolean enableAgent(@PathVariable Long id) {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setEnabled(true);
        return agentService.updateById(agent);
    }

    // ==================== LLM配置接口 ====================

    @GetMapping("/llm-config/list")
    @Operation(summary = "获取LLM配置列表")
    public Page<LlmConfig> listLlmConfigs(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String provider) {
        Page<LlmConfig> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<LlmConfig> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(LlmConfig::getName, name);
        }
        if (provider != null && !provider.isEmpty()) {
            wrapper.eq(LlmConfig::getProvider, provider);
        }
        wrapper.orderByDesc(LlmConfig::getPriority);
        return llmConfigService.page(page, wrapper);
    }

    @GetMapping("/llm-config/{id}")
    @Operation(summary = "获取LLM配置详情")
    public LlmConfig getLlmConfig(@PathVariable Long id) {
        return llmConfigService.getById(id);
    }

    @PostMapping("/llm-config")
    @Operation(summary = "创建LLM配置")
    public boolean createLlmConfig(@RequestBody LlmConfig config) {
        return llmConfigService.save(config);
    }

    @PutMapping("/llm-config/{id}")
    @Operation(summary = "更新LLM配置")
    public boolean updateLlmConfig(@PathVariable Long id, @RequestBody LlmConfig config) {
        config.setId(id);
        return llmConfigService.updateById(config);
    }

    @DeleteMapping("/llm-config/{id}")
    @Operation(summary = "删除LLM配置")
    public boolean deleteLlmConfig(@PathVariable Long id) {
        return llmConfigService.removeById(id);
    }
//
//    @PostMapping("/llm-config/test")
//    @Operation(summary = "测试LLM配置")
//    public String testLlmConfig(@RequestBody LlmConfig config) {
//        return llmConfigService.testConfig(config);
//    }

    @GetMapping("/llm-config/enabled")
    @Operation(summary = "获取启用的LLM配置列表")
    public java.util.List<LlmConfig> listEnabledLlmConfigs() {
        return llmConfigService.listEnabled();
    }

    @PatchMapping("/llm-config/{id}/disable")
    @Operation(summary = "禁用LLM配置")
    public boolean disableLlmConfig(@PathVariable Long id) {
        LlmConfig config = new LlmConfig();
        config.setId(id);
        config.setEnabled(false);
        return llmConfigService.updateById(config);
    }

    @PatchMapping("/llm-config/{id}/enable")
    @Operation(summary = "启用LLM配置")
    public boolean enableLlmConfig(@PathVariable Long id) {
        LlmConfig config = new LlmConfig();
        config.setId(id);
        config.setEnabled(true);
        return llmConfigService.updateById(config);
    }

    // ==================== 向量数据库配置接口 ====================

    @GetMapping("/vector-db-config/list")
    @Operation(summary = "获取向量数据库配置列表")
    public Page<VectorDbConfig> listVectorDbConfigs(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String provider) {
        Page<VectorDbConfig> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<VectorDbConfig> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(VectorDbConfig::getName, name);
        }
        if (provider != null && !provider.isEmpty()) {
            wrapper.eq(VectorDbConfig::getProvider, provider);
        }
        wrapper.orderByDesc(VectorDbConfig::getId);
        return vectorDbConfigService.page(page, wrapper);
    }

    @GetMapping("/vector-db-config/{id}")
    @Operation(summary = "获取向量数据库配置详情")
    public VectorDbConfig getVectorDbConfig(@PathVariable Long id) {
        return vectorDbConfigService.getById(id);
    }

    @PostMapping("/vector-db-config")
    @Operation(summary = "创建向量数据库配置")
    public boolean createVectorDbConfig(@RequestBody VectorDbConfig config) {
        return vectorDbConfigService.save(config);
    }

    @PutMapping("/vector-db-config/{id}")
    @Operation(summary = "更新向量数据库配置")
    public boolean updateVectorDbConfig(@PathVariable Long id, @RequestBody VectorDbConfig config) {
        config.setId(id);
        return vectorDbConfigService.updateById(config);
    }

    @DeleteMapping("/vector-db-config/{id}")
    @Operation(summary = "删除向量数据库配置")
    public boolean deleteVectorDbConfig(@PathVariable Long id) {
        return vectorDbConfigService.removeById(id);
    }

    @PostMapping("/vector-db-config/test")
    @Operation(summary = "测试向量数据库配置")
    public String testVectorDbConfig(@RequestBody VectorDbConfig config) {
        return vectorDbConfigService.testConfig(config);
    }

    @GetMapping("/vector-db-config/enabled")
    @Operation(summary = "获取启用的向量数据库配置列表")
    public java.util.List<VectorDbConfig> listEnabledVectorDbConfigs() {
        return vectorDbConfigService.listEnabled();
    }
}
