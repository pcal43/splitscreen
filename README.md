# Splitscreen Support - Updated & Maintained Fork

> **🔧 This is an actively maintained fork of the original [Splitscreen Support](https://modrinth.com/mod/splitscreen) mod by pcal.net**
> 
> **✅ Updated for Minecraft 1.21.7+** | **✅ Active maintenance** | **✅ Community-driven improvements**

Simple mod that makes it easier to position windows for local multiplayer.

With this mod installed, hitting the 'Toggle Fullscreen' button (F11) will cycle you through different half- and quarter-size
window positions for split screen play.  It then saves your choice and brings the window up in the same place when you
restart the game. 

This makes it easier for you to start two or more copies of Minecraft and have the windows be positioned automatically 
for splitscreen.

This mod is designed to be used in conjunction with a Minecraft launcher (such as [PrismLauncher](https://prismlauncher.org/)) and
a gamepad mod (such [Midnight Controls](https://modrinth.com/mod/midnightcontrols))

## Usage Video

[![Video](https://github.com/pcal43/splitscreen/blob/pcal/readme-updates/etc/movie-thumb.png?raw=true)](https://youtu.be/QtsTT2dEED0)

## Suggested Setup 

### Stuff you need
* Two or more legal minecraft accounts
* [PrismLauncher](https://prismlauncher.org/)
* [Midnight Controls](https://modrinth.com/mod/midnightcontrols)
* [Splitscreen Support (This Fork)](https://modrinth.com/mod/tPDTaOq8)
* Two or more gamepads
* One or more friends
* One big TV hooked up to your computer
* One couch

### What to do
* Create two or more minecraft instances in PrismLauncher, one for each account
* In each instance, install SplitScreen Support and MidnightControls
* Launch the instances and configure _each_ of them as follows:
  * **SplitScreen Support**: Press `F11` to position the windows
  * **MidnightControls**: Enable `Unfocused Input` and `Virtual Mouse` as [described here](https://www.midnightdust.eu/wiki/midnightcontrols/)
  * **Minecraft**: Press `F3+P` to disable [_Pause on Lost Focus_](https://minecraft.wiki/w/Debug_hotkey)
* Get a gamepad for each instance and configure MidnightControls
* In one of the instances, create a new world and 'Open to LAN'
* Connect the other instances to that one (e.g., localhost:25565)
* Get your friends and have a Minecraft party!

## Other mod features
* Username is displayed in the upper-left corner of the main menu (so you can tell who's who)

## Adjusting the gap between windows
By default, the mod puts a small gap between windows.  You can change the width of this gap
(or make it `0`) by setting the value of the `gap` property in `config/splitscreen.properties`.
(This file doesn't exist until you quit minecraft for the first time with the mod running).

## Why This Fork?

This fork exists because the original mod wasn't accepting pull requests for newer Minecraft versions. Key differences:

- **🎯 Updated for Minecraft 1.21.7+** (and future versions as they release)
- **🔧 Active maintenance** - Issues get addressed, PRs get reviewed
- **🚀 Faster updates** for new Minecraft versions
- **🤝 Community-driven** - Welcoming contributions and feedback
- **📋 Same great features** - All original functionality preserved

## Requirements

- **Minecraft**: 1.21.7+ 
- **Mod Loader**: Fabric
- **Dependencies**: Fabric API

## Acknowledgements
* **Original mod** by [pcal.net](https://github.com/pcal43/splitscreen) - All credit for the core concept and initial implementation
* **This fork** maintained by nicholas with community contributions
* Video courtesy of [wormstweaker](https://www.youtube.com/@WORMSTweaker)
* Icon based on [Squares icons created by Rahul Kaklotar - Flaticon](https://www.flaticon.com/free-icons/squares)

## Questions & Support

For this fork:
- **Issues & Bug Reports**: [GitHub Issues](https://github.com/FlyingEwok/splitscreen/issues)
- **Feature Requests**: [GitHub Discussions](https://github.com/FlyingEwok/splitscreen/discussions)
- **Pull Requests**: Always welcome!

For the original mod questions, see: [https://discord.pcal.net](https://discord.pcal.net)

## Contributing

This fork welcomes contributions! Whether you're fixing bugs, adding features, or updating for new Minecraft versions:

1. Fork this repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

See `README-FORK.md` for detailed instructions on setting up your own fork for publishing.

![System](https://github.com/pcal43/splitscreen/blob/main/etc/screenshot-0.png?raw=true)
