package utn.tdm.meegos.listener;

import utn.tdm.meegos.domain.Chat;

public interface ChatListener {
    void onMessageSent(Chat chat);
    void onChatDelete(Chat chat);
}