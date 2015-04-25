#![Reflect](/app/src/main/res/mipmap-xxhdpi/ic_launcher.png) Reflect
A basic texting app

#Features
- SMS and MMS support
- XMPP
 - Google XMPP(Host: talk.google.com, Port: 5222)
 - Using Smack([Smack Docs](https://www.igniterealtime.org/builds/smack/docs/4.1.0/documentation/))

#Views
- Action Bar
  - Tabs
    - Recent
      - ListView of recent conversations
        - ListView Item
          - Circle avatar on left
          - Name on to the top right of avatar
          - Most recent message bellow name
            - Bold if unread
          - Date of most recent message to right of name
    - SMS
      - Identical to Recent tab except it only shows SMS messages
    - Jabber
      - Identical to Recent tab except it only shows XMPP messages
- First Time Setup
  - SMS Setup
    - Prompt: "Use Reflect for SMS"
    - Button: "Yes"
  - Connect Google Hangouts
    - Prompt: "Connect Google Hangouts"
    - Input: Username
    - Input: Password
    - Button: "Connect"
  - Connect XMPP Accounts
    - Prompt: "Add Jabber Accounts"
    - Input: Username
    - Input: Host
    - Input: Port
    - Input: Password
    - Button: "Add"
- Conversation Screen
  - Action Bar
    - Left: Receiver Info
      - Left: Avatar
      - Top Right(Of: Avatar): Name
      - Bottom Right(Of: Avatar): Uri
      - Click: Show list of other possible receiver contacts
  - Messages Area
    - Sent messages on right, received messages on left
    - Sent messages are primary color
    - Received messages are secondary color
    - Avatar to the edge of message
    - "Via <Communication Method>" in bottom right of content area
  - Sending Area
    - Left: Paper clip attach icon
    - Center: Composition area
    - Right: Current identity avatar
      - If composition area is not empty turns into send icon
      - Click: Shows all other identity options

#Messaging Uri Scheme
**: Used to identify various resources that are common in messaging

**Scheme Parts**
```
scheme://resource_type@resource_provider/resource_id
```

**Scheme**: `messaging`  

**Resource Type**:
Used to identify what type of resource is being requested.  
Valid resource types are:
- message
- conversation
- contact

**Resource Provider**:
Used to identify what messaging service provides the resource.  
Valid resource providers are:
- xmpp
- sms
- joint
 - Specifies a resource which is pulled from both sms and xmpp, usualy by
 merging data from both

**Resource Id**:
The id of the resource.

**Special Cases**:
For contacts that are provided by the `xmpp` provider the following scheme is used:
```
messaging://contact@xmpp/xmpp_host:xmpp_port/xmpp_username
```

The `xmpp_host` and `xmpp_username` in the context of the `jabber` scheme is:
```
jabber://xmpp_username@xmpp_host:xmpp_port
```


#Preferred Communication Method
Each Reflect client keeps track of the available communication methods. These communication methods
can sometimes be unavailable due to lack of cellphone or data coverage. The preferred communication
method will be used to suggest what method of communication to use when contacting a client.

The preferred communication method can be subscribed to by any other client. When the method changes
a message will sent out to the subscribers.

If all communication methods are available the user's preferred communication is the preferred
communication method.

#Planning
- **General**
  - `enum CommunicationType`
    - `SMS`
    - `XMPP`
- **Reflect Contact**
  - `class ReflectCombinedContact`
    - `String id`
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
    - `Timestamp timestamp`
    - `CommunicationType communicationType`
  - `class Conversation`
    - `String id`
    - `List<String> contactIds`
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
  - `class XMPPChatProvider extends ChatProvider`
    - Implements all methods using XMPP
  - `class SMSChatProvider extends ChatProvider`
    - Implements all methods using SMS
- **Database**
  - Use [DBFlow](https://github.com/Raizlabs/DBFlow)
  - `class ReflectDatabase`
    - Stores basic database information
    - `public static final String NAME`
    - `public static final int VERSION`
  - `class ListConverter extends TypeConverter<String, List>`
    - `public String getDBValue(List list)`
      - Converts to string with GSON
    - `public List getModelValue(String data)`
      - Returns `List` from data
