package channel;

public class EmailChannel implements Channel {

    private static final String TAG = "[EMAIL] ";

    @Override
    public String formatEnvelope(String recipient, String title, String body) {
        return "To: " + recipient + " | Subject: " + title + " | Body: " + body;
    }

    @Override
    public String transmit(String envelope) {
        return TAG + envelope;
    }
}
