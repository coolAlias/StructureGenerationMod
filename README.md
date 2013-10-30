STRUCTURE GENERATION MOD
========================
This is a mod demonstrating the functionality and use of my Structure Generation API. Using the Structure Spawner Item,
structure generation is as easy as the click of a button. Currently with 6 awesome structures.

FEATURES
========
SMP Compatible!

A ready-to-go mod that will help familiarize you with the capabilities and functionality of the Structure Generation
API, or to use just for fun.

Several structures are included and can be easily toggled between in-game:
- "Hut" - A highly modified village hut. Enter at your own risk.
- "Blacksmith" - Vanilla blacksmith shop. Thanks to Microjunk for this one!
- "Viking Shop" - A cool viking-style shop, but riddled with holes from disuse. Credit again to Microjunk.
- "Redstone Dungeon" - Can you find the treasure and live to tell about it?
- "Watermill" - An awesomely impressive water/windmill, courtesy of Microjunk.
- "Redstone Portcullis" - A design by PearSquirrel, coded by me (coolAlias)

These are the default controls:

'Sneak' - highlights the area in which structure will generate

'Arrow Keys' - up: moves structure away from player; down: moves structure towards/behind player; left / right

'O' - changes structure's default orientation by 90 degrees. This will have the effect of changing which side spawns
      facing the player.

'I' - toggles between increment and decrement y offset

'Y' - increments or decrements y offset (i.e. structure will generate further up / down)

'U' - reset x/y/z offsets to 0

'V' - toggles between generate / remove structure - when removing, be sure to click the EXACT position you clicked
      when spawning it (easier to do with highlighting enabled)

'[' / ']' - Previous / Next structure in the list

Right click - spawn / remove structure at tile location clicked

Controls can be customized from the config file that is generated the first time you load this mod.

Or use coolAliasStructureWorldGenDemo.zip to try out randomly generated structures dotting your landscape! Initial
world loading times will be longer than normal.

FOR MODDERS:
- Easily add new items capable of generating structures by extending ItemStructureSpawnerBase, working seamlessly
  with the ready-made key bindings, packet handling and preview highlighting mode
- Try out the LinkedStructureGenerator by using the commented-out code in ItemStructureSpawner's onItemUse method
  instead of the currently active code
- Uncomment out the WorldStructureGenerator registration in StructureGenMain to try out world generation of structures
- Check out the StructureGenAPI for the full range of possibilities.
 
POSSIBLE FUTURE FEATURES
========================
- The current recipe for the item is dirt on a stick... I'll change it when I think of something better.
- More structures?
 
KNOWN BUGS
==========
- Not currently aware of any.

INSTALLATION
============
Requires Forge. Written for and tested with Minecraft v1.6.4 and Forge build 916.

MOD

1. Download the zipped mod distributable and place it in your minecraft/mods folder. You're good to go!

2. Alternatively, follow the directions below to add the 'StructureGenMod' files to your project, giving you access
to a secondary API for easily creating new Items capable of spawning structures with all the pre-made functionality.

3. If using the source code rather than the distributable, the StructureGenAPI is also required.

API

1. Download the 'StructureGenAPI' from my github repository

2. Place the entire folder either in your project or as a required project on your project's build path

3. Build your own structures / structure arrays by following the guidelines in the Instructions

4. If you are using custom hooks, either edit the included StructureGenerator.java file or create your own class that
   extends StructureGeneratorBase to handle your custom hooks

5. Use your StructureGenerator class to generate your structures from whatever location you choose, such as a block
   or item, or create a class that implements IWorldGenerator (such as the included WorldStructureGenerator) to
   generate structures during world generation

SCREEN SHOTS
============
Check them out on the forums: http://www.minecraftforum.net/topic/1963371-structure-generation-and-rotation-tool/
