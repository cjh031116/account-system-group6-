package com.accounting.system.service.impl;

import com.accounting.system.common.Utils;
import com.accounting.system.model.Voucher;
import com.accounting.system.service.AiService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.squareup.okhttp.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AiServiceImpl implements AiService {

    private OkHttpClient okHttpClient = null;

    @Override
    public String query(String message) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(5, TimeUnit.MINUTES);
            okHttpClient.setReadTimeout(5, TimeUnit.MINUTES);
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"content\":\"You are a helpful assistant\",\"role\":\"system\"},{\"content\":\"" + message + "\",\"role\":\"user\"}],\"model\":\"deepseek-chat\",\"frequency_penalty\":0,\"max_tokens\":2048,\"presence_penalty\":0,\"response_format\":{\"type\":\"text\"},\"stop\":null,\"stream\":false,\"stream_options\":null,\"temperature\":1,\"top_p\":1,\"tools\":null,\"tool_choice\":\"none\",\"logprobs\":false,\"top_logprobs\":null}");
        Request request = new Request.Builder()
            .url("https://api.deepseek.com/chat/completions")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer sk-af34850f0c63469fb4cc1413947dbf2c")
            .build();
        try {
            String response = okHttpClient.newCall(request).execute().body().string();
            JSONObject json = JSON.parseObject(response);
            return json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        } catch (Exception e) {
            Utils.showError("Error", "Failed to query from AI: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String category(String desc) {
        return query("please pick a word from the list: [Shopping,Education,Entertainment,Housing,Utilities], which is most related to this sentence: " + desc + ". please give the word only");
    }

    @Override
    public String suggestion(List<Voucher> voucherList) {
        List<String> monthList = voucherList.stream().map(Voucher::getMonth).distinct().sorted().toList();
        Map<String, Double> incomeMap = voucherList.stream().filter(v -> "Income".equals(v.getDirection())).collect(Collectors.groupingBy(Voucher::getMonth, Collectors.summingDouble(Voucher::getTotalDebit)));
        Map<String, Map<String, Double>> expenseMap = new HashMap<>();
        voucherList.stream().filter(v -> "Expense".equals(v.getDirection())).forEach(voucher -> {
            expenseMap.computeIfAbsent(voucher.getMonth(), k -> new HashMap<>());
            Map<String, Double> innerMap = expenseMap.get(voucher.getMonth());
            double amount = innerMap.getOrDefault(voucher.getCategory(), 0D) + voucher.getTotalDebit();
            innerMap.put(voucher.getCategory(), amount);
        });
        StringBuilder sb = new StringBuilder();
        for (String month : monthList) {
            sb.append(month).append(":income=").append(incomeMap.getOrDefault(month, 0D));
            Map<String, Double> innerMap = expenseMap.getOrDefault(month, new HashMap<>());
            for (Map.Entry<String, Double> entry : innerMap.entrySet()) {
                sb.append(",expense(").append(entry.getKey()).append(")=").append(entry.getValue());
            }
            sb.append(";");
        }
        return query("please suggest monthly budgets, saving goals, and cost-cutting recommendations based on below data: " + sb + ". please adjust the budget moderately according to Chinese spending habits during holidays");
    }

}
