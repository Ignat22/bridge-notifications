package notification;

import channel.Channel;

public class Reminder extends Notification {

    private static final String TITLE = "Reminder";

    public Reminder(String id, String recipient, String message, Channel channel) {
        super(id, recipient, message, channel);
    }

    @Override
    public String execute() {
        return dispatch(TITLE, getMessage());
    }
}
