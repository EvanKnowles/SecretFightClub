package za.co.knonchalant.telegram;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;

public interface VerticalButtonBuilder
{
  /**
   * Turns an array of InlineKeyboardButton objects into the datastructure used by Telegram to represent vertical buttons
   * Sounds fancy, but it just turns an array { a, b, c } into an array { {a}, {b}, {c} }
   * @param buttons the buttons
   * @return 2D array, with each element being a single-element array with one of the buttons
   */
  static InlineKeyboardButton[][] createVerticalButtons(InlineKeyboardButton[] buttons)
  {
    InlineKeyboardButton[][] outer = new InlineKeyboardButton[buttons.length][];
    for (int i = 0; i < buttons.length; i++)
    {
      outer[i] = new InlineKeyboardButton[] { buttons[i] };
    }
    return outer;
  }
}
