#![Reflect](https://github.com/Noah-Huppert/Reflect/raw/master/app/src/main/res/mipmap-xxhdpi/ic_launcher.png) Reflect
A basic texting app

#Features
- SMS and MMS support
- XMPP
 - Google XMPP(Host: talk.google.com, Port: 5222)
 - Using Smack([Smack Docs](https://www.igniterealtime.org/builds/smack/docs/4.1.0/documentation/))

#Todo
- Plan a database wrapper

#Screens
- First time setup screen
 - Multiple pages
  - SMS Setup
   - Prompt: "Set as SMS app"
   - Buttons: "Yes" and "No"
  - Hangouts Setup
   - Prompt: "Connect Google Hangouts account"
   - Button: "Why?"
    - Prompt: "If Reflect detects that contact does not have Cellphone coverage it will send them a Hangouts message seamlessly"
   - Button: "Connect" and "No"
  - XMPP Setup
   - Prompt: "Add Jabber/XMPP accounts"
   - List: Connected XMPP accounts
   - Button: "Add" and "Continue"

#Storage
Reflect will use SQLite on Android to store data. To store lists just
concatenate the data with spaces.

EX:  
The data is a series of User Ids
- 34
- 546
- 78
- 65

In the database this would be stored as
```
userIds => "34 546 78 65"
```

#Preferred Communication Method
Each Reflect client keeps track of the available communication methods. These communication methods
can sometimes be unavailable due to lack of cellphone or data coverage. The preferred communication
method will be used to suggest what method of communication to use when contacting a client.

The preferred communication method can be subscribed to by any other client. When the method changes
a message will sent out to the subscribers.

If all communication methods are available the user's preferred communication is the preferred
communication method.

#Planning V2
- **General**
  - `enum CommunicationType`
    - `SMS`
    - `XMPP`
- **Reflect Contact**
  - `class ReflectCombinedContact`
    - `List<String> contactIds`
    - `CommunicationType prefferedCommunicationType`
  - `class ReflectContact`
    - `String id`
    - `String uri`
      - For SMS the uri is the raw google contact Id
      - For XMPP the uri is in the form of `username@host:port`
        - Port not needed if its the default XMPP port(`5222`)
    - `CommunicationType communicationType`
    - `String firstName`
    - `String lastName`
    - `String avatarUrl`
- **Storing XMPP Accounts**
  - Use the [AccountManager](http://developer.android.com/reference/android/accounts/AccountManager.html) to store user info
    - Use `AccountManager.addAccountExplicitly()` which allows you to store a username, password and a `Bundle` of extras
- **Conversations**
  - `class Message`
    - `String id`
    - `String conversationId`
    - `String contactId`
    - `String content`
    - `Date timestamp`
  - `class Conversation`
    - `String id`
    - `List<String> userIds`
- **Messenging**
  - `abtract class ChatProvider`
    - `public abtract List<String> fetchConversations()`
      - Returns list of conversation ids that are stored in the database
      - Retrieves conversations from source and makes sure they are stored in
        database
    - `public abtract List<String> fetchConversationMessages(String conversationId)`
      - Returns list of message ids that are stored in the database
      - Retrieves messages from source and makes sure they are stored in
        database
    - `public abtract String fetchMessage(String messageId)`
      - Returns message id of message that is stored in the database
      - Retrieves message from source and makes sure it is stored in database
    - `public abtract String createConversation(List<String> userIds)`
      - Returns id of newly created and stored(in database) conversation
      - Communicates with source then creates conversation, and stores in
        database
    - `public abtract String sendMessage(String conversationId, String contactId, String content)`
      - Returns id of newly created and stored(in database) message
      - Communicates with source then send message and stores in database
