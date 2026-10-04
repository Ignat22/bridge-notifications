# Assignment 3 | Bridge Pattern

| | |
|---|---|
| Name | Igor Kuhta |
| Group | SE-2530 |
| Topic | **B - Notifications** |
| Repository | https://github.com/Ignat22/bridge-notifications |
| Base commit (working I1/I2 version) | `50046f3c4d68dd876d4d1fd7244e42d851eea720` |

Course: ShP-2216 Software Design Patterns, Astana IT University. Java 17, JDK classes only.

## Role map

| Role | Class | Source path |
|---|---|---|
| Abstraction | `Notification` | `src/notification/Notification.java` |
| A1 (refined abstraction) | `Reminder` | `src/notification/Reminder.java` |
| A2 (refined abstraction) | `UrgentAlert` | `src/notification/UrgentAlert.java` |
| Implementor | `Channel` | `src/channel/Channel.java` |
| I1 | `EmailChannel` | `src/channel/EmailChannel.java` |
| I2 | `SmsChannel` | `src/channel/SmsChannel.java` |
| I3 (extension) | `PushChannel` | `src/channel/PushChannel.java` |
| Client | `Main` | `src/Main.java` |

## Where to look

| What | Location |
|---|---|
| Bridge field (`Channel channel`) | `Notification.java` line 10 |
| Constructor that receives the implementor | `Notification.java` line 12 |
| `execute()` declared in the abstraction | `Notification.java` line 19 |
| `execute()` in A1 / A2 | `Reminder.java` line 14 / `UrgentAlert.java` line 14 |
| `setImplementation(Channel)` | `Notification.java` line 21 |
| Delegation to the implementor (`dispatch`) | `Notification.java` line 37 |
| Low-level operations `formatEnvelope`, `transmit` | `Channel.java` lines 5 and 7 |
| T5 runtime-switch check | `Main.java`, `checkRuntimeSwitch()` line 75 |

## Build and run

From the extracted project folder:

```
javac --release 17 -encoding UTF-8 -d out "@sources.txt"
java -cp out Main --demo
```

## Sample data

Recipient `aidos`; A1 `Reminder` id `R-1`, message `Team meeting at 10:00`; A2 `UrgentAlert` id `A-1`, message `Server is down`.

## Expected results

| Check | Setup | Expected result |
|---|---|---|
| T1 | Reminder + EmailChannel | `[EMAIL] To: aidos \| Subject: Reminder \| Body: Team meeting at 10:00` |
| T2 | Reminder + SmsChannel | `[SMS] aidos <- Reminder: Team meeting at 10:00` |
| T3 | UrgentAlert + EmailChannel | `[EMAIL] To: aidos \| Subject: URGENT \| Body: Server is down` |
| T4 | UrgentAlert + SmsChannel | `[SMS] aidos <- URGENT: Server is down` |
| T5 | one Reminder: Email, then `setImplementation(SmsChannel)`, then again | `sameObject=true`, `stateUnchanged=true`, before = T1 result, after = T2 result |
| T6 | Reminder + PushChannel | `[PUSH] {device: aidos, title: Reminder, body: Team meeting at 10:00}` |
| T7 | UrgentAlert + PushChannel | `[PUSH] {device: aidos, title: URGENT, body: Server is down}` |

Final line: `SUMMARY: 7/7 PASS`. The program exits with code 1 if any check fails.

## Extension

`PushChannel` was added after the base commit. Only `src/channel/PushChannel.java` (new) and `src/Main.java` (demo checks T6/T7) changed inside `src`; see `extension.diff`.
