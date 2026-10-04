package notification;

import channel.Channel;

public abstract class Notification {

    private final String id;
    private final String recipient;
    private final String message;
    private Channel channel;

    protected Notification(String id, String recipient, String message, Channel channel) {
        this.id = requireText(id, "id");
        this.recipient = requireText(recipient, "recipient");
        this.message = requireText(message, "message");
        this.channel = requireChannel(channel);
    }

    public abstract String execute();

    public void setImplementation(Channel channel) {
        this.channel = requireChannel(channel);
    }

    public String getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    protected final String dispatch(String title, String body) {
        String envelope = channel.formatEnvelope(recipient, title, body);
        return channel.transmit(envelope);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    private static Channel requireChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel must not be null");
        }
        return channel;
    }
}
