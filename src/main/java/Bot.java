import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    public String[] angries = {"Жизнь испортили!", "Убить вас мало!", "Уроды, а не люди!", "Сталина на вас нет!", "Тікай з міста"};
    private String previous;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try{
            telegramBotsApi.registerBot(new Bot());
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        String x = update.getMessage().getText();
        if(x.equals("/angry_bot")){
            previous = "";
            sendMsg(update, 0);
            previous = null;
        }
    }

    public synchronized void sendMsg(Update update, int n){
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            SendMessage s = new SendMessage();
            s.enableMarkdown(true);
            s.setChatId(message.getChatId().toString());
            s.setReplyToMessageId(message.getMessageId());
            String frase = angries[(int) (Math.random()* 5)];
            if(frase == previous){
                sendMsg(update, n);
            }else {
                previous = frase;
                s.setText(frase);

                try {
                    if (n == 4) {
                        execute(s);
                        return;
                    }
                    execute(s);
                    Thread.sleep(1000);
                    sendMsg(update, n + 1);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public String getBotUsername() {
        return "My Lovely Bot";
    }

    @Override
    public String getBotToken() {
        return "640577970:AAEmP5ZICXx9KpaDFXmSR4xVO9oQ81wQUEc";
    }
}
