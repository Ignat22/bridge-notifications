package channel;

public class SmsChannel implements Channel {

    private static final String TAG = "[SMS] ";

    @Override
    public String formatEnvelope(String recipient, String title, String body) {
        return recipient + " <- " + singleLine(title + ": " + body);
    }

    @Override
    public String transmit(String envelope) {
        return TAG + envelope;
    }

    private static String singleLine(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }
}
