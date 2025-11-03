package com.licanfu. aiagent.demo.invoke;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.utils.JsonUtils;

import java.util.Arrays;

public class SdkAiInvoke {
    public static void main(String[] args) {
        try {
            Generation gen = new Generation();
            GenerationParam param = GenerationParam.builder()
                    .apiKey("sk-dd4b2ee7b44f468281f2adc443b43e74")
                    .model("qwen-plus")
                    .messages(Arrays.asList(
                            Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.")
                                    .build(),
                            Message.builder().role(Role.USER.getValue()).content("你是谁？").build()))
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            GenerationResult result = gen.call(param);
            System.out.println("SDK调用：" + JsonUtils.toJson(result));
        } catch (Exception e) {
            System.err.println("调用服务错误: " + e.getMessage());
        }
    }
}