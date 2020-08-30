# Tronic
The planets dopest Discord Bot
(for real)

## Features
- Autocorrect commands
- YouTubeMusic tracks for better audio quality and search results
- Track volume correction via crowdsourcing
- Easy button controls
- Simple queue system

## Invite
[Juz click on zis epic link](https://discordapp.com/oauth2/authorize?scope=bot&client_id=554803884439240705&permissions=8)

## Install to host
1.  Clone the repo with ``` git clone https://github.com/LowLevelSubmarine/Tronic.git ```
2.  Compile the repo with ``` gradle shadowJar ``` (You could use the gradlew or gradlew.bat scripts as well)
3.  Just start the buildet jar in build/libs
4.  Configure the bot by fillin your Discordbot token and your discord users id in the generated config.json
5.  Have fun!

### Activate Tronics Rest API
1. Add ```"activateApi": true``` to the tronic.conf file
2. If you want to https create a KeyStore.jks in tronics jar path, by running ``` keytool -genkey -alias localhost -keyalg RSA -keystore KeyStore.jks -validity 365 -keysize 2048``` in a console
