import channel.Channel;
import channel.EmailChannel;
import channel.PushChannel;
import channel.SmsChannel;
import notification.Notification;
import notification.Reminder;
import notification.UrgentAlert;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String RECIPIENT = "aidos";
    private static final String REMINDER_ID = "R-1";
    private static final String REMINDER_TEXT = "Team meeting at 10:00";
    private static final String ALERT_ID = "A-1";
    private static final String ALERT_TEXT = "Server is down";

    private static final String REMINDER_EMAIL = "[EMAIL] To: aidos | Subject: Reminder | Body: Team meeting at 10:00";
    private static final String REMINDER_SMS = "[SMS] aidos <- Reminder: Team meeting at 10:00";
    private static final String ALERT_EMAIL = "[EMAIL] To: aidos | Subject: URGENT | Body: Server is down";
    private static final String ALERT_SMS = "[SMS] aidos <- URGENT: Server is down";
    private static final String REMINDER_PUSH = "[PUSH] {device: aidos, title: Reminder, body: Team meeting at 10:00}";
    private static final String ALERT_PUSH = "[PUSH] {device: aidos, title: URGENT, body: Server is down}";

    private record Outcome(String id, boolean passed, String headline, String detail, String expected) {
    }

    public static void main(String[] args) {
        if (args.length == 1 && "--demo".equals(args[0])) {
            runDemo();
        } else {
            System.out.println("Usage: java -cp out Main --demo");
        }
    }

    private static void runDemo() {
        Channel email = new EmailChannel();
        Channel sms = new SmsChannel();
        Channel push = new PushChannel();

        List<Outcome> outcomes = new ArrayList<>();
        outcomes.add(checkCombination("T1", newReminder(email), email, REMINDER_EMAIL));
        outcomes.add(checkCombination("T2", newReminder(sms), sms, REMINDER_SMS));
        outcomes.add(checkCombination("T3", newAlert(email), email, ALERT_EMAIL));
        outcomes.add(checkCombination("T4", newAlert(sms), sms, ALERT_SMS));
        outcomes.add(checkRuntimeSwitch());
        outcomes.add(checkCombination("T6", newReminder(push), push, REMINDER_PUSH));
        outcomes.add(checkCombination("T7", newAlert(push), push, ALERT_PUSH));

        outcomes.forEach(Main::print);

        long passed = outcomes.stream().filter(Outcome::passed).count();
        System.out.println("SUMMARY: " + passed + "/" + outcomes.size() + " PASS");
        if (passed != outcomes.size()) {
            System.exit(1);
        }
    }

    private static Notification newReminder(Channel channel) {
        return new Reminder(REMINDER_ID, RECIPIENT, REMINDER_TEXT, channel);
    }

    private static Notification newAlert(Channel channel) {
        return new UrgentAlert(ALERT_ID, RECIPIENT, ALERT_TEXT, channel);
    }

    private static Outcome checkCombination(String id, Notification notification, Channel channel, String expected) {
        String actual = notification.execute();
        String participants = notification.getClass().getSimpleName() + " + " + channel.getClass().getSimpleName();
        return new Outcome(id, expected.equals(actual), participants + " | result=" + actual, "", "result=" + expected);
    }

    private static Outcome checkRuntimeSwitch() {
        Notification original = newReminder(new EmailChannel());
        String idBefore = original.getId();
        String recipientBefore = original.getRecipient();
        String messageBefore = original.getMessage();
        String before = original.execute();

        Notification afterSwitch = switchImplementation(original, new SmsChannel());
        String after = afterSwitch.execute();

        boolean sameObject = original == afterSwitch;
        boolean stateUnchanged = idBefore.equals(afterSwitch.getId())
                && recipientBefore.equals(afterSwitch.getRecipient())
                && messageBefore.equals(afterSwitch.getMessage());
        boolean passed = sameObject && stateUnchanged && REMINDER_EMAIL.equals(before) && REMINDER_SMS.equals(after);

        String headline = "sameObject=" + sameObject + " | stateUnchanged=" + stateUnchanged;
        String detail = "before=" + before + " | after=" + after;
        String expected = "sameObject=true | stateUnchanged=true | before=" + REMINDER_EMAIL + " | after=" + REMINDER_SMS;
        return new Outcome("T5", passed, headline, detail, expected);
    }

    private static Notification switchImplementation(Notification notification, Channel channel) {
        notification.setImplementation(channel);
        return notification;
    }

    private static void print(Outcome outcome) {
        System.out.println(outcome.id() + " " + (outcome.passed() ? "PASS" : "FAIL") + " | " + outcome.headline());
        if (!outcome.detail().isEmpty()) {
            System.out.println(" " + outcome.detail());
        }
        if (!outcome.passed()) {
            System.out.println(" expected: " + outcome.expected());
        }
    }
}
