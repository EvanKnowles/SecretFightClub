package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.*;
import za.co.knonchalant.telegram.scheduled.AwfulMockUpdate;

import java.util.List;

public class MockBotAPI implements IBotAPI<AwfulMockUpdate> {
    public MockBotAPI() {
    }

    @Override
    public List<IMessageHandler> getHandlers() {
        return null;
    }

    @Override
    public void setInlineHandler(IInlineHandler handler) {

    }

    @Override
    public IInlineHandler getInlineHandler() {
        return null;
    }

    @Override
    public void addHandler(IMessageHandler handler) {

    }

    @Override
    public void addHandlers(List<IMessageHandler> handler) {

    }

    @Override
    public List<AwfulMockUpdate> getUpdates(Integer limit) {
        return null;
    }

    @Override
    public void sendMessage(AwfulMockUpdate message, String text, Object... args) {
        System.out.println("MOCK: Message sent: " + text);
    }

    @Override
    public void sendMessage(AwfulMockUpdate message, String text) {
        System.out.println("MOCK: Message sent: " + text);
    }

    @Override
    public void sendMessageWithKeyboard(IUpdate update, List<List<String>> keyboardList, String text) {

    }

    @Override
    public boolean typing(IUpdate update) {
        return false;
    }

    @Override
    public void sendMessage(Long chatId, String message, ParseMode parseMode, boolean disableWebPagePreview, Integer messageId, Keyboard keyboard) {
        System.out.println("MOCK: Message sent: " + message);
    }

    @Override
    public void updateMessage(Long chatId, String message, Integer messageId, InlineKeyboardMarkup keyboard) {
        System.out.println("MOCK: Message updated: " + message);
    }

    @Override
    public void setOffset(int updateId) {

    }

    @Override
    public List<User> getChatUsers(long chatId) {
        return null;
    }

    @Override
    public void sendInlinePhoto(String inlineId, String photoUrl, String thumbnailUrl, int width, int height) {

    }

    @Override
    public void sendPhoto(AwfulMockUpdate chat, byte[] photoUrl) {

    }

    @Override
    public void sendAnimation(String chatId, byte[] photoUrl) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean supportsUpdateListener() {
        return false;
    }

    @Override
    public void registerUpdateListener(IBotUpdatesHandler handler) {

    }

    @Override
    public void unregisterUpdateListener() {

    }



/*    @Override
    public void sendMessage(IUpdate message, String text) {
        System.out.println("MOCK: Message sent: " + text);
    }*/
}
