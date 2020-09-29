package com.npospolita.nocringebot.handler;

import com.npospolita.nocringebot.api.TelegramApi;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class RulesListHandler implements Handler {

    @Value("${nocringe.rules}")
    String rules;

    private static final String COMMAND = "/rules";

    private final TelegramApi api;

    @Override
    public boolean canHandle(Update update) {
        Message message = update.message();
        return !StringUtils.isEmpty(message.text())
                &&  message.text().contains(COMMAND);
    }

    @Override
    public void handle(Update update) {
        api.sendMessage(update, rules, ParseMode.Markdown, true);
    }
}
