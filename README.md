# OpenVPNTile
Android Quick Settings Tile for OpenVPN

This app provides an Android Quick Settings tile which will allow you to connect/disconnect a specific OpenVPN Connect profile. Note that this is not a standalone VPN app. It requires you to have the official OpenVPN Connect* app installed for this to function properly.

If you have the OpenVPN Connect app correctly installed and configured, then this app is for you! After installing, edit your Quick Settings and a new tile called "OpenVPN" will be available at the bottom. Hold and drag this icon up to add to your Quick Settings panel.

When tapping the tile for the first time, you will have to type the name of the profile EXACTLY as named in the OpenVPN Connect app. Also, choose if it is a OVPN profile or an Access Server profile. Then, subsequent taps will signal to OpenVPN Connect to connect/disconnect this profile.


Known issues:
- Tapping the quick connect tile will fire an intent telling the OpenVPN Connect app to launch and take the appropriate action. You will see the app briefly open and close. Sorry, that's how the OpenVPN Connect app works.
- Because the OpenVPN Connect app doesn't provide any state callbacks, it is possible for the tile state to become out-of-sync with the VPN state. (i.e. the tile says the VPN is connect, but it is in fact not.) This usually happens after a reboot if you have the OpenVPN Connect app configured to automatically reconnect. Tapping the tile should then force OpenVPN Connect into the correct state.

*Disclaimer: This app is completely unaffiliated with OpenVPN, OpenVPN Connect or anything else for that matter. It is simply a convenient way to execute the commands documented here: https://openvpn.net/vpn-server-resources/faq-regarding-openvpn-connect-android/#How_do_I_use_tasker_with_OpenVPN_Connect_for_Android

This app is free and open source released under the Apache 2.0 license. The source code is available on GitHub: https://github.com/banasiak/OpenVPNTile
