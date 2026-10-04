package notification;

import channel.Channel;

public class UrgentAlert extends Notification {

    private static final String TITLE = "URGENT";

    public UrgentAlert(String id, String recipient, String message, Channel channel) {
        super(id, recipient, message, channel);
    }

    @Override
    public String execute() {
        return dispatch(TITLE, getMessage());
    }
}
