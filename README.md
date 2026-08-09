NinjaCheat

A personal Minecraft utility / cheat mod for Minecraft 1.21.1 made with Fabric, Yarn mappings, and Mixin.

I made this for learning purposes — mainly to study Mixin, event systems, and client-side modding.  
It takes a lot of inspiration from CheatUtils, FDPClient/LiquidBounce, and the FullBright addon, then adds a bunch of modules I wanted myself.

⚠️ For educational use only.  
Using cheats on public servers is against most servers' rules and can get you banned.  
Only use this in singleplayer or on servers where you have permission. Use at your own risk.

Features

Around 60 modules across Combat, Movement, Render, Player, World, Exploit, Other, and Client
Simple event system with @EventHandler
Settings (bool, number, enum, color, string)
Commands starting with .
ClickGUI (Right Shift)
Basic HUD (watermark, arraylist, coords, FPS, ping, session time)
Some 3D rendering (ESP, tracers, HoleESP, etc.)
Config saved as JSON

Modules

Combat
KillAura
Criticals
AutoClicker
Velocity
HitBox
Reach
AutoCrystal
Surround

Movement
Fly
Speed
NoFall
Scaffold
Step
Jesus
Spider
HighJump
InvMove
Strafe
AntiVoid

Render
ESP
Tracers
FullBright (based on L3nnart_'s addon)
NameTags
FreeCam
XRay
Chams
Zoom
HoleESP
StorageESP
NoRender
Trajectories
LogoutSpots

Player
AutoEat
AutoTool
AutoTotem
NoRotate
FastUse
AutoArmor
Regen

World
Nuker
FastBreak
FastPlace
Timer
AutoFish
AirPlace

Exploit
Disabler
PingSpoof
Blink
Phase
GhostHand

Other
ChestStealer
MiddleClick
AntiAFK
AutoSoup

Client
ClickGUI
Hud
AntiBot
StaffDetector
DiscordRPC
Macros

How to Build

You need JDK 21.

./gradlew build

The jar will be in build/libs/.

Installation

Install Fabric Loader for 1.21.1
Put Fabric API and this mod's jar into the mods folder
Launch the game

Usage

Right Shift → Open ClickGUI
Default keybinds:
  R → KillAura
  F → Fly
  V → Speed
  G → FullBright
  X → XRay
  C → Zoom

Commands (type in chat)

.toggle 
.bind  
.set   
.config save / .config load
.panic (disables everything)
.list
.help

Config is saved to ~/.minecraft/ninjacheat/config.json.

Credits

This project is heavily inspired by:

CheatUtils by Zergatul
FDPClient / LiquidBounce by CCBlueX and contributors

A lot of the module ideas and architecture come from those projects.  
I added and changed many things for my own use.

License

MIT License

Copyright (c) 2026 yaemimu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

Disclaimer

This is just a personal learning project.  
I’m not responsible for any bans, lost items, or anything else that happens if you use it.  
Please don’t use it on servers where cheats aren’t allowed.