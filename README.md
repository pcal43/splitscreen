# Splitscreen Support

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
* [Splitscreen Support](https://modrinth.com/mod/splitscreen)
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

## Acknowledgements
* Video courtesy of [wormstweaker](https://www.youtube.com/@WORMSTweaker)
* Icon based on [Squares icons created by Rahul Kaklotar - Flaticon](https://www.flaticon.com/free-icons/squares)

## Questions?

If you have questions about this mod, please join the Discord server:

[https://discord.pcal.net](https://discord.pcal.net)

Comments have been disabled and I will **not** reply to private messages on Curseforge.

![System](https://github.com/pcal43/splitscreen/blob/main/etc/screenshot-0.png?raw=true)
