package com.npospolita.nocringebot.api;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.GetMyCommandsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramApi {

    private final TelegramBot bot;

    public void sendMessage(Update update, String text, ParseMode parseMode, boolean withPreview) {
        SendMessage request = new SendMessage(update.message().chat().id(), text)
                .parseMode(parseMode)
                .disableWebPagePreview(withPreview)
                .disableNotification(true)
                .replyToMessageId(update.message().messageId());

        execute(request);
    }

    public void sendSticker(Update update, String stickerFileId) {
        Message message = update.message();

        SendSticker request = new SendSticker(message.chat().id(), stickerFileId)
                .disableNotification(true)
                .replyToMessageId(message.messageId());

        execute(request);
    }

    public void addCommand(String command, String description) {
        List<BotCommand> botCommands = getCommands();

        botCommands.add(new BotCommand(command, description));

        SetMyCommands setMyCommands = new SetMyCommands(botCommands.toArray(new BotCommand[0]));

        execute(setMyCommands);
    }

    public boolean isChatMember(Long botChatId, User user) {
        GetChatMember getChatMember = new GetChatMember(botChatId, user.id());

        GetChatMemberResponse chatMemberResponse = (GetChatMemberResponse) execute(getChatMember);

        return chatMemberResponse.chatMember().isMember();
    }

    private List<BotCommand> getCommands() {
        GetMyCommands getMyCommands = new GetMyCommands();

        GetMyCommandsResponse commandsResponse = bot.execute(getMyCommands);

        return new ArrayList<>(Arrays.asList(commandsResponse.commands()));
    }

    private <T extends BaseRequest> BaseResponse execute(T request) {
        BaseResponse response = bot.execute(request);

        if (!response.isOk()) {
            log.error("error: {}", response);
        }

        return response;
    }
}
