Project status: On hold until Website v2 and [Trak](https://github.com/Noah-Huppert/trak) are finished
#[![Build Status](https://travis-ci.org/Noah-Huppert/Reflect.svg)](https://travis-ci.org/Noah-Huppert/Reflect)

#![Reflect](/app/src/main/res/mipmap-xxhdpi/ic_launcher.png) Reflect
A basic open source texting app

#Open Source
Reflect is and will always be open source and free. If you would like to contribute
just submit a pull request.

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
