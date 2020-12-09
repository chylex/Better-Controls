Better Controls
===============

![Logo](https://raw.githubusercontent.com/chylex/Better-Controls/master/logo.png "Depiction of Zero Regrets")

Better Controls is a reimagining of [Better Sprinting](https://www.curseforge.com/minecraft/mc-mods/better-sprinting) with even more customizability. It features several new keybindings and many configurable options for how sprinting, sneaking, and flying should behave.

Better Controls only focuses on complementing vanilla mechanics rather than adding new mechanics (such as [Better Sprinting](https://www.curseforge.com/minecraft/mc-mods/better-sprinting)'s option to allow sprinting in all directions) that only work on client side and server owners must explicitly opt in to enable them. By not having such features in Better Controls, it significantly reduces complexity by not having a server side version of the mod, and not needing a way to completely disable the mod while the game is running.

Another major difference is the amount and granularity of options. Better Controls could be considered an *advanced controls mod* - by default, none of the keybinds are bound, and all options are set to match vanilla behavior. To take full advantage, go into *Options - Controls - Better Controls*, check out all of the available options, and figure out the best configuration for you.

## Features

The mod adds **Toggle Keybinds** for sprinting, sneaking, flying (creative mode), walking, and jumping. You can use modifier keys (`Control` / `Shift` / `Alt`) for each, including for example setting `Control` to Sneak, and `Control + Y` to Toggle Sneak. If you press the original key, the toggle will be canceled (in the previous example, you can Toggle Sneak by pressing `Control + Y`, and stop sneaking by simply tapping Sneak). Note that the vanilla options for toggling sprinting/sneaking are disabled to avoid conflicts with the custom keybinds.

You can also bind a key that resets all **Toggle Keybinds** at once. That makes it easy to for ex. turn on walking, jumping, and sprinting, and then turn all of them off again by pressing one key instead of three.

#### Sprinting

* **Sprint Key Mode** changes how the Sprint key behaves. You can choose between *Tap To Start Sprinting*, *Tap To Start / Stop Sprinting*, and *Hold To Sprint*.
* **Double Tap 'Walk Forwards' To Sprint** can be turned off to prevent accidental sprinting.
* **Resume Sprinting After Hitting Obstacle** automatically presses the Sprint key once you are no longer touching any blocks (helpful when climbing hills, especially if the previous option is enabled).

#### Sneaking

* **Move Camera Smoothly** lets you disable the smooth movement when sneaking or unsneaking.

#### Flying

* **Sprint Key Mode While Flying** changes how the Sprint key behaves during flight.
* **Disable Field Of View Changing** prevents sprinting, potions, and other factors from changing the FOV while flying in creative and spectator mode.
* **Fly On Ground** lets you fly while touching the ground in creative mode (and also lets you stop flying by tapping Sneak while touching the ground).
* **Flight Speed Multiplier** (0.25x - 8x) changes how fast you fly in creative and spectator mode.
* **Vertical Speed Boost** (up to +300%) adds additional vertical speed boost while flying in creative and spectator mode.

## Installation

The following mod loaders are supported:

* **[Fabric](https://fabricmc.net/use/)** (note: this mod does not require Fabric API)
* **[Forge](https://files.minecraftforge.net/)**

After you install the mod loader of your choice, [download the mod](https://www.curseforge.com/minecraft/mc-mods/better-controls/files) and place the `.jar` file into `.minecraft/mods`.

### Minecraft Updates

The mod will attempt to load on new versions of Minecraft (including snapshots) as they come out. I made it that way so that if the mod works on a newly released version of Minecraft without any changes, I can simply mark it as compatible and you don't have to wait for an update.

While this is convenient, there is potential for bugs which can affect server play. If you plan to use the mod on a server, please always use the latest version of the mod, and wait until I either mark the version as compatible with new Minecraft updates, or release a new version of the mod that is marked as compatible.

### Compatibility

* Better Controls is a client-side mod, it will do nothing when installed on a server.
* If you run into a conflict with another mod that modifies the *Controls* screen, hold Alt while opening *Controls* to prevent Better Controls from adding its button. If another mod prevents the button from appearing, you can install [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) which adds a mod list with configuration buttons for mods.

## Screenshot

![Options Menu Screenshot](https://repo.chylex.com/better-controls.png)

Source Code
===========

The `main` branch includes the latest version for Fabric. Older versions and their Forge equivalents are in branches named `fabric/<version>` and `forge/<version>`.

## Contributing

All contributions should target the `main` branch, unless the contribution is specifically made for older versions. Every feature must be portable to Forge to ensure parity, unless the feature adds integration with another mod that is only compatible with one mod loader. Integrations for Forge-only mods should target the most recent `forge/<version>` branch.

For any larger contributions, please [open an issue](https://github.com/chylex/Better-Controls/issues/new) first before you make a PR.

Please keep in mind that this mod is designed to be very simple and easy to test. Over the years, [Better Sprinting](https://github.com/chylex/Better-Sprinting) has gained a fair amount of bloat that requires extensive testing before every release, and it is the reason why I will not be accepting PRs which add:

* **Translations**
  * If you want to translate the mod, you are welcome to create a separate version of it. Unfortunately, I've always ran into enough problems with maintaining and testing community mod translations that I will not be adding official support for localization.
* **Server-side code**
  * Better Controls will always be exclusively client-side. The server-side support in Better Sprinting is a major time sink which requires testing 3 separate client/server configurations per mod loader, and is the main reason I decided to drop support in favor of this mod.
* **Singleplayer-only features**
  * Better Controls intends to add accessibility options that can be used on servers, so features that only work in singleplayer or anything that could be used for cheating on servers will not be accepted.
