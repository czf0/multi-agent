package com.licanfu. aiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatLanguageModel model = QwenChatModel.builder()
                .apiKey("sk-dd4b2ee7b44f468281f2adc443b43e74")
                .modelName("qwen-max")
                .build();
        System.out.println("LangChainAi调用：" + model.chat("我是程序员licanfu"));
    }
}
