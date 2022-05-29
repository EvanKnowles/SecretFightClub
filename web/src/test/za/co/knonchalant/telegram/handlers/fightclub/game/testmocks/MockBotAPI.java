package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import com.pengrad.telegrambot.model.request.*;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.*;
import za.co.knonchalant.telegram.scheduled.AwfulMockUpdate;

import java.lang.reflect.Field;
import java.util.*;

public class MockBotAPI implements IBotAPI<AwfulMockUpdate> {
    private Map<Long, String> lastResponse = new HashMap<>();

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
        lastResponse.put(message.getUser().getId(), text);
        System.out.println("MOCK: " + text + "\n\t");
        for (Object arg : args) {
            System.out.println(arg);
        }

    }

    @Override
    public void sendMessage(AwfulMockUpdate message, String text) {
        lastResponse.put(message.getUser().getId(), text);
        System.out.println("MOCK: " + text);
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
        System.out.println("MOCK:  " + message);
        lastResponse.put(chatId, message);

        System.out.println("Keyboard: ");
        if (keyboard instanceof ReplyKeyboardMarkup) {
            ReplyKeyboardMarkup replyKeyboardMarkup = (ReplyKeyboardMarkup) keyboard;
            try {
                Field keyboardField = ReplyKeyboardMarkup.class.getDeclaredField("keyboard");
                keyboardField.setAccessible(true);
                List<List<KeyboardButton>> buttonRow = (List<List<KeyboardButton>>) keyboardField.get(replyKeyboardMarkup);
                Field textField = KeyboardButton.class.getDeclaredField("text");
                textField.setAccessible(true);
                for (List<KeyboardButton> buttons : buttonRow) {

                    for (KeyboardButton button : buttons) {
                        String text = (String) textField.get(button);
                        System.out.print(text + "\t");
                    }
                    System.out.println();
                }

            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateMessage(Long chatId, String message, Integer messageId, InlineKeyboardMarkup keyboard) {
//        lastResponse = message;

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

    public String getLastResponse(long userId) {
        return lastResponse.get(userId);
    }
}
