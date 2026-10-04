package channel;

public interface Channel {

    String formatEnvelope(String recipient, String title, String body);

    String transmit(String envelope);
}
