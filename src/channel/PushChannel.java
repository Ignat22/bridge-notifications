package channel;

public class PushChannel implements Channel {

    private static final String TAG = "[PUSH] ";

    @Override
    public String formatEnvelope(String recipient, String title, String body) {
        return "{device: " + recipient + ", title: " + title + ", body: " + body + "}";
    }

    @Override
    public String transmit(String envelope) {
        return TAG + envelope;
    }
}
