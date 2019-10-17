package utn.tdm.meegos.listener;

import utn.tdm.meegos.domain.Chat;

public interface ChatListener {
    int onChatDelete(Chat chat);
}