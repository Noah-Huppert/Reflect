#![Reflect](https://github.com/Noah-Huppert/Reflect/raw/master/app/src/main/res/mipmap-xxhdpi/ic_launcher.png) Reflect
A basic texting app

#Features
- SMS and MMS support
- XMPP
 - Google XMPP(Host: talk.google.com, Port: 5222)
 - Using Smack([Smack Docs](https://www.igniterealtime.org/builds/smack/docs/4.1.0/documentation/))

#Todo
- Finish planing `class ChatProvider`
  - Figure out how `conversationId` would work
- Plan `class ReflectMessage`
- Plan `class ReflectConversation`
- Plan `class ContactManager`
  - Make it a singleton

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

#Preferred Communication Method
Each Reflect client keeps track of the available communication methods. These communication methods
can sometimes be unavailable due to lack of cellphone or data coverage. The preferred communication
method will be used to suggest what method of communication to use when contacting a client.

The preferred communication method can be subscribed to by any other client. When the method changes
a message will sent out to the subscribers.

If all communication methods are available the user's preferred communication is the preferred
communication method.



#Planning
- `class ReflectContact`
  - `List<String> googleContactIds`
    - A list of Google contact ids
    - Ids retrieved from the Android Contact Api
  - `List<String> xmppContactIds`
    - A list of XMPP contact ids
    - Ids retrieved from XMPPContact
- `class XMPPContact`
  - `XMPPProvider xmppProvider`
  - `String xmppId`
- `class XMPPProvider`
  - `String host`
  - `int port`
- `class XMPPIdentity`
  - `XMPPProvider xmppProvider`
  - `String xmppId`
  - `String xmppPassword`
    - Encrypted in local storage
- `class ChatProvider`
 - A class to abstract the handling of the messaging process
 - `List<ReflectMessage> getMessages(int conversationId)`
 - `ReflectMessage getMessage(int conversationId, int messageId)`
