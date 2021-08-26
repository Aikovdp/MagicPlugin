# TODO

# For 10.0 Release

 - Inheritance bahhhh!
   Can we make like "requires:" instead of "inherit" or something?
   Add-on configs like engineering or spawnmobs can override robes even though they
   have no crafting at all, because they inherit survival's crafting
   The inherit is only there for people who use "mconfig example set robes", there must be a better way

 - Need to handle magic blocks in unloaded worlds, keep them around and then retry when the world loads

 - Change behavior of "locked" on wands to match items? Maybe a config switch to change it back?

 - All the zombies on my demo server seem to be named...
   or are these left over from mooses?
   
 - Add some help instructions in survival crafting section for enabling wand recipe auto-discover
   
 - Take some timings and spark profiles (during club, ideally), consider impact
   of possibly changing mage_update_interval to 1
   
 - Test recipes on 1.16 and 1.9

 - Test magicheart configs
   
# Glyph Wand

 - Make brushes work (swing to use)

# Vivecraft

 - Vivecraft integration: https://github.com/jrbudda/Vivecraft_Spigot_Extensions#metadata
 - Handle "seated: true" case, I *think* it should just turn off the hand code

# Set Bonuses
Sample config:
```
sets: dwarven,elven
set_requirements: 4
set_bonus_properties:
   modifiers: hidden
   potion_effects:
      haste: 1
```

# Important

 - Spells that disable mana regen need to constantly reset the mana catchup timestamp (on ... wands.. hrm)
 - Allow slotted upgrades to rename (rename and rename_description options)
 - Prevent blaze fireballs from doing damage?
   Arenas always auto-rollback fireballs and stuff from mobs?
   Auto-rollback all spells from players after arena is done
 - Spell icons that are shields will go to the offhand when shift+clicked 
 - Test applying infinity to a sword, is it allowed? Enchantable should bypass vanilla restrictions
 - Ability to enchant sticks and such?
 - Enchantment levels as variables in spell and wand configs would be very cool
   support Power on a magic bow?
 - Dying at the end of an arena puts you in a broken state (maybe fixed?)
 - Buy/sell shops get inaccurate if they don't auto-close, need to refresh lore after each purchase
 - Add support for nms.SnowballEntity.setItemStack, for custom snowball projectiles in Projectile action

# Undo / Build Issues

 - Item frames and paintings can't be copied, maybe only E/W facing - important for backup
 - Holes in containment if automata gets contained while in the glass
 - Cancelled spells (`/magic cancel`) should still auto-undo

# New

 - picked up trigger for dropped wand items
 - Light arrow bypasses pvp?
 - Mob aggro still no bueno
 - Per-spell setting for dynmap
 - Add requirements to path progression
 - Look at to_otherside warp, absolutely insane mob spawning (turn mob spawning back on after)
   ... it's the silverfish rule, but I think it may be unavoidable since the fish will just die? idk
 - Wand active_properties
 - Base modifier "allow_melee", and move reflect parameters from wand
 - Save spell can make upside-down schematics

## Arenas

 - Arena won't let you join next round if you are dead
 - Add option to save inventory and class layout
   - Mage stored inventories need to be by key
   - Restore all inventories on login, if restoring inventories remove any spells
   - Restore classes on login (save these by key, too?
 - Add class selector option

 - Arena auto-build/repair using schematics
 - A way for examples to provide schematics
 - Prevent players teleporting into an arena
   - Or at least teleporting to a player in an arena?
   - And don't save their death location
 - Prevent spiders climbing?

# New API

 - Possible to change sky color?
 - Item frame fixed/visible official API
   https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/commits/9a6a5a664c15127db80ae154523f9f8b9afd51ba
 - Non-Collidable modifier
   https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/commits/ad52a4ec1d7b7254e3fe68ca497b2cc63afac373

# Resource pack changes near-term

 - Configurable off-by-default RP load invulnerability window  

# Near-Term Hopeful List

 - Separate cooldown for fail/no_target
 - Can't offset `target: self` using py or ty (?)
 - Add editor support for the variables section, it's very confusing
 - Need a way to duplicate NPCs
 - Creative mode should maybe consume PS blocks when placed? idk
 - Add a way to get new brushes if you're maxed out
 - Prevent axolotls from dying on land
 - Add tab-completion and editor support for wand UI configs
 - Add clone/replicate source selected effects (cube)  
 - Add brush for creative mode, uses last held block  
 - Ability to open an editor session on behalf of another player
 - Some indicator for long-running engineering casts?
 - Would be cool to represent automata as mobs, maybe just need entity-less mob spawning ability? 
   .. make sure it gets tracked for `/mmob list` and `remove` functionality
 - Custom portal-making via spells, use block metadata to allow portal destinations or spells  
 - Shapeless recipe support

# Bugs

 - Should /mmap load not reload an existing image? Or maybe it's too hard to match cropping/etc
 - Adding RPG configs prevents you from using a survival wand?
 - `/mconfig apply` doesn't work without a slug, after starting a new editor session
 - Tag wand spell icons so they're not droppable!
   .. I had my spell inventory replace survival while configuring a wand.. see if I can reproduce that :(
 - PW having inventory loss issues (spells go into normal inventory)
 - /mmob remove doesn't remove mounts
 - Triggered spells need to track the wand they came from (think this is on the list somewhere already)
 - Does FakeBlock not really respect indestructible blocks? test out invisiworld
 - Don't drop blocks when breaking magically-changed blocks in creative mode
 - Copying dragon cage from httyd world doesn't work- block data wrong?  
 - Got "Recall got to doTeleport with no waypoint selected" .. how?
 - using inherit: sellshop make the quiet: true not working on item parameter but it works correctly with inherit: buyshop (brushette)
 - Do automata drop loot when expiring due to chunk load?
 - Selector cost at top-level (default cost) doesn't work
 - Need to be able to override default_earn_scale per-shop (for making exchange shops)
 - Can't make crafting recipes that overlap with vanilla ones, see ruby sword, etc
 - Need an easier way to set prices that works with wands, too
 - Spells pass through walls in protected regions with blocked-spells set? https://www.youtube.com/watch?v=xHnxopWsSJM
 - See if you can get FishingHook to work as a projectile? requires an EntityHuman to construct
 - Mobs that are friendly *and* happen to hit each other shouldn't force-target
   (could not find this, doesn't seem like a get a targeting event.. is magic forcing them? Can't find that either)

# Pets

 - https://docs.google.com/document/d/1ORA5W9q3UxN40sqemiDPM0u1UF5pvQ-1r91iGcOc4Ys/edit

# Engineering Improvements

 - Fill out worth values for all blocks (and items, for shops) .. mostly done?
 - Tree spell allow using materia?

## 1.17

 - Separate out NMS, CB and Bukkit access
 - Try shading again on a shared NMS lib?
 - PR APIs for getPrivateNeedsFixing stuff
 - PR APIs for CB stuff .. ?

## RPG

## Suggestions

 - A way to damage mobs as if from a looting weapon (increase drops)
 - Chat trigger
 - A way to have wands update item attributes on an interval, such as with healthsword
 - disable_health_regeneration: true/false
 - Spell option to carry over CastContext
 - BetonQuest, expand integration - NPC quest dialog, class and cast count support
   https://github.com/BetonQuest/BetonQuest
 - RideEntity action parameter to work on target mob (I think it has this already?)
 - Add requirements lists to wands
 - Some way to make a spell that uses the reflect behavior of shield wands (reflective modifiers)
 - AureliumSkills + path integration
 - Variable crafting recipe ingredients (e.g. all planks)
 - CheckMount action, klein_76 would like to detect when players are sitting down.. triggers?
 - Crackshot support, apparently it sets health directly or otherwise bypasses damage events?
   It has an API, but is closed source with no Maven
 - Map brushes should fill transparent bits with air if the erase modifiers is active
 - Modifiers from wands keep track of their wand item to use in CastContext for triggered spells
 - Custom death message support?
 - Register attributes as placeholderapi placeholders  
 - Drop rule add required tools
 - Can smithing recipes support magic item outputs?  
 - Allow randomized loot in kits
 - Add "tick" effects for Projectile
 - CheckInventory needs an option to just compare material
 - KingdomsX support (currency, protection?) https://github.com/CryptoMorin/KingdomsX/wiki/API
 - Allow some custom variables in wand lore, to be able to show the result of equations in lore
 - Shops should just hide invalid items (option to hide them, or just use show_unavailable)
 - Use hitbox of disguised mob
 - Wands that apply a mage cooldown on activation
 - Selector/Shop `layout` parameter to allow making specific layouts easier
   This got a little complicated, but I think we need to put a "symbol" parameter on 
   each item, or ideally support a map of symbol to option/item
   Then layout is a list of rows of strings of symbols
 - Custom wand name formatting
 - Per-wand flag for allowing offhand casting
 - Sound override (works like particle override) for wands
 - Allow multiple slugs to /mconfig apply/load to load multiple configs at once

## Client Localization

 - Add MageController.getMessage(Player) and Mage.getMessages()
 - Don't merge localization files in config load
 - Use language setting to set default Messages instance
 - Will need a special-case for EN
 - Refactor, refactor, refactor ...

## Hot

 - Improve mnpc describe, need to be able to describe sub-keys, with tab completion
 - Brooms should ignore half-blocks for takeoff
 - Weird behavior spamming (holding) right-click: https://youtu.be/FJfDqyCgztE
 - Recall to bed not working? (Kit reported) ("Not home set" ... "or something" lol)
 - Need a way to add spells to a path from a custom config (like add_spells ... needs to be a map)
 - target_hit_timeout for CustomProjectile (Phantom)
 - /mmap slice command for easily making multi-maps

## Older

 - Auto-level up skill items when spell levels... ?
   ... honestly the whole levelling system could use some changes, like spells should just use the right level when cast
   - spell levels also seem wonky with spellbook (or maybe wands in general?), have to let go and hold again to get the upgrade
 - Wand modifiers should be merged in as part of Mage.updatePassiveEffects, not in wand activate/deactivate
   Need modifiers to work on wand armor and offhand
 - CheckTrigger should be able to use a full trigger config, not just a key name
 - Passives don't upgrade (see critical)

 - Night asking about cooldowns on dragon mount spell, shouldn't they reset at end of cast?
 - Automaton parameters don't seem to work, when applied with CreateAutomaton or a populator
 - Consecutive days played attribute?

 - Overrides should probably be cached

 - New mob triggers? (cornelia)
   enters water, flys/falls, walks, stands still, etc?
 - New plyaer triggers (NeoFalcon) for blocking/stop blocking

 - Allow enchantable wands to use an anvil/book
 - Capture doesn't work on piglin brutes

 - hit/entity _count parameters don't work with EntityProjectile
 - Targeting options for passive or hostile mobs only

 - Consider revamping the "work" mechanic, maybe just allow some # of ms per tick?
 - Check smallairpump, overrides didn't work?
 - block-based automaton, more official features... interact actions
 - Test /mauto configure spell with parameters
 - Make /mauto cast command? or better cast parameters configuration?
 - Does "grow" not work on Phantoms?

 - Paste all entities in schematics

 - People are still getting broomstick handles somehow ;( ;(
 - Refactor passive effects system from wand to work with classes and modifiers
 - Add spell immunity list to mob data

 - Wolf house signs are broke again (check teleporter room)
 - Add "melee" flag for wands to allow them to do melee damage
 - Magic mobs or automata fight without players around?
 - Direct damage option to Damage action

## Editor

   - Gets confused by spaces, test out recipes
     row_2: "aa ...
   - Add support for triggers
   - Add automaton support (or maybe fix- pluralize problem?)
   - Can't insert creator into materials or effects since they are lists!
   - Test effectlib classes in effects block (stand-alone effects), doesn't seem to work

## World Gen

 - Some way to scale mobs up (levels?) with distance from spawn

## RPG

 - Support for simple list of string triggers

## List is too long

 - Undo on world save is flawed
   - Chunks save when unloaded
   - Do we need to auto-undo spells on chunk unload?
 - Crafting recipe option to preserve enchantments, use for magic sword
   .. there was also that rando on discord who wanted to use this on a Geyser server to emulate a smithing table
      on bedrock

 - Wands deactivate on TP (sometimes? cross-world? idk, can't reproduce)
 - Magic crossbow support: https://editor.elmakers.com/bz8e4d

 - Modifiers
   - Allow variables in modifiers
   - Inputs are all of the target wand's properties
   - they can stack, process sequentially in updateModifiers
   - Handle overrides as a special separate case
 - wand property migration
   swap out protection for something else
    - magic strength (or damage increase using attributes)
    - range - use attributes
    - larger radius ?
    - longer DOT durations ?

 - Can we make pets?
 - Make "untame" spell
 - Spells ignore tamed mobs of friends ?
 - Mage add brush tab-completion is dumb (or does this command not exist?)
 - Absorb should allow you to bypass restricted materials when you have bypass perms

## Hot Issues

 - Can fly through glass panes on a broomstick

 - Allow customizing effects, or lists in general?
 - Paginate wands and spells command output, make base class of SelectionManager, like Paginator<T extends Named>
 - Automata re-activation delay ?

 - Make melee a tag instead of a material list

 - Passives:
   - Some way to tag/group passives, where only N spells from a group can be active at a time

 - Add support for Sponge schematics

 - Add support for Bukkit projectiles, when there is an entity shooter

 - Add option to reset mana on wand activation

 - Add denied_item parameter to CheckInventory action
 - Look at bug with equations from Reuben
 - Some way to specify order of icons in Recall

 - while moving or flying (it happens more often while doing this) around and right clicking to cast, it throws this error: https://hastebin.com/ixilazasig.makefile  it doesn't seem to happen with left click. Not an expert but by reading a bit I think it's because minecraft thinks the player item is being replaced by air and acts like we want to place such air as a block, which is imposible. Hope it helps!
   (This may be a creative mode only bug?)
 - Rocket boots + rocket spell underwater = profit? https://www.youtube.com/watch?v=mAOrgu8P4r8
 - Tweak controllable air scooter, why is initial acceleration so slow?
 - Freezing water changes it to a source block when undone... ?? https://forge.avatarmc.com/T1993
 - Using alter on stained glass pane makes it go funky in 1.13

 - Broom crafting, wheat isn't needed in 1.13.1 - something weird definitely going on there, maybe just need to
   convert legacy ingredients before registering?
 - Look at lightsaber crafting, seems broken? Maybe only in 1.13? Starshop also always gives white stained glass...

 - Some clean way to make magic bow arrow spells useable on a wand?
   Mainly to avoid people always reporting them as bugs :\
 - Issues with wand/bows migrating to new version (waiting on details)

 - (PS - /ps allowall lags?)

 - Small bug! When you die, it makes your wand auto-select the first spell in your hotbar:
   https://youtu.be/wExxOdFWCV4

 - /m strange tab-completion behavior

 - Test attributes when reloading mage class configs live
 - Option to specify a slot for class items to auto-equip

 - Creating blocks (e.g. Reflect level 5) on the right-bottom corner of a painting breaks the painting but doesn't
   catch the dropped item.

 - There seems to be an undo issue here somewhere.
   - Goldwalker blocks didn't undo once on sandbox (Can not reproduce)
   - Nathanwolf auto at dev spawn has some permanent blocks now
   - Random blocks show up in mob arena sometimes (web, broken floor)
   - Can't find a pattern. Tried world save... don't know. :(

 - Change potter progression, maybe add spells per year graduation?

 - Cars look floaty

 - Need some option to orient an EffectRing, or some other way to make a ring of effects that follows a projectile

## Recent Requests

 - Some way to copy the last spell cast by your target.
 - Support for "FationsOne" (sic, sp?) - I guess? Good lord, Factions, get your act together.
 - Special blocks that act as automata when placed, remove automata when used
 - Selector support for scaling costs based on lore values
 - Add "deactivate" actions for toggle spells
 - Add damage lore to spells   
 - Allow use of equations in book action, somehow.
 - Custom spell lore showing arbitrary parameters (or something)

## Future

 - Ability to earn SP (Or whatever) from various actions, sword use, bow use, mining, etc.
   Generalized XP system?

 - Take a look at this, maybe use accurate block hitboxes?
   https://bitbucket.org/BillyGalbreath/pl3xsigns/src/c4ce6a50592aca67be0aef26117cc8b7e069c3eb/src/main/java/net/pl3x/bukkit/pl3xsigns/lineofsight/BoundingBox.java?at=master&fileviewer=file-view-default

 - FOV change action?
   https://wiki.vg/Protocol#Player_Abilities_.28clientbound.29

 - It'd be really cool to support configs from remote repos....
   Github has an API for fetching a list of files, maybe can use that?
   https://api.github.com/repos/grisstyl/BetterPotter/contents/spells/spells

 - Add path 2nd parameter to enchant command to stop when reaching end of a path

 - Optional lore on wands to say which classes they work with

 - Update skill icon lore when armor updates (to take buffs into account)

 - Brushette requests damage reduction (as in subtract an amount)
 - Status effect system
   - Some way to temporarily modify properties that works with stacking and is guaranteed to undo
   - Invoke via action, similar to ModifyProperties (maybe extend from it)

 - Make editor work with selector options

## Attributes

 - Attributes can improve with rankup (spell purchase? need new progression mechanism...)
 - Also allow classes to define attribute global effects
   - Physical damage
   - Speed
   - Magic damage (physical versus magic- maybe allow other damage classes?)
   - Cooldowns
   - Max mana / regen
   - Mana costs
   - SP costs or SP earn bonuses
   - Attack speed
   - Damage protection
   - Anti-hunger

## Suggestions

 - mauto spawner option to randomize facing direction
 - Option to /wand fill to fill a specific category
 - Cast location offset override per spell.
 - Flag to prevent putting SP in chests
 - Check knockback resistance in Velocity action, or add Mage knockback resistance.

 - Add an action to simulate the red screen you get while out of bounds. See:
   https://gist.github.com/leonardosnt/ffa8e72b60df197c762d1f2e903cc67f

 - Placeholder API integration: https://www.spigotmc.org/wiki/hooking-into-placeholderapi/
   - Allow placeholder-driven attributes

 - Mana regen cooldown, so that casting a spell puts mana regen on a cooldown

 - Paginate wand and spell lists

 - TreeAction should grow the right type of tree for the given sapling

## On Hold

 - Casting blob on an item frame makes the frame disappear. User reports dropped frame, too, but could not reproduce.
 - Spells drop on death with lag (maybe)?
 - PerWorldInventory logout issues, can't reproduce
 - Broom issues when in creative mode (re-opened spigot issue for this)
 - Wand disappearing during duels- maybe via disarm, maybe drop action? (red 0 on PW)
 - PW would like some custom lore for wand "quiet" and quick cast settings.
 - Recall warps don't show up with /mage getdata?

## Fast Blocks

 - The easiest way to deal with that is to probably pretend the client doesn't have the chunk yet
 - just entityplayer.playerConnection.sendPacket(new PacketPlayOutMapChunk(this.chunk, '\uffff')); should work
 - or even easier .. set the dirtyCount of the player chunk to 64, and update the h field with the chunk sections that you modified
 - and make sure to call playerChunkMap.a(playerChunk); to schedule an update

  https://github.com/tastybento/askyblock/blob/master/src/com/wasteofplastic/askyblock/nms/v1_12_R1/NMSHandler.java

## Not so High-Priority

 - Per-player language settings (See https://www.spigotmc.org/resources/api-languages.22305/)
 - Would be cool to have a configurable max # of maps, and start re-using map ids when limit is hit, LRU
 - Ability to alter flower pots and beds (need to tweak TileEntity data.. doable, but messy to track with Material keys)
 - Aliases don't work with levels
 - Ability to specify map dimensions/offset in percentage

## Sabers

 - Paths being able to upgrade blocking/reflect power

## Reported Issues

### Requests

 - Async config load on startup option
 - Allow multiple welcome_wand entries
 - An attribute that lets spells level up more quickly
 - Wand Power based on Strength potion effect
 - Add ModifyPower action

 ZQuest API: https://www.spigotmc.org/resources/zquestapi-feel-the-might-of-java.35327/
 ZQuest How to make Extensions: http://zquestwiki.com/index.php?title=APIcreatingExtensions
 ZQuest Page: https://www.spigotmc.org/resources/zquest-feel-the-might-of-creating-1-9-1-10-1-11.18045/

### Suggestions

 - Bonuses to wands:
   - If you could do this with a command like "/wand configure mana_regen/max_mana <amount in seconds>" would be great as I could make my own shops for this
   - Also by combining in an anvil with an item that has these buffs that you can add to your wand
     - Temporary max mana
     - Temporary faster mana regeneration
     - Temporary anything else you can think of really, the more we can add, the better :)
 - Add a TeamProvider for Factions
 - Upgrades to Spells
   - Can only choose 1 of the 3 and only when applicable (actual bonus could be customized)
   - Bonus to max distance x2
   - Bonus to travel speed of the spell 40% faster
   - 10% bonus to damage
 - Ability to set the max amount of spells that a wand (or a path?) can have
 - With the ability to add/remove spells to your list
   - bonus to that would be if you could set a configurable cooldown for being able to change out the spells on your wand
 - If you add permanent bonuses I think it should work more like rune stones with the ability to add certain bonuses to specific armor pieces in the way you would add an enchant to an item. This would allow you to add the buffs to existing armor that already has enchants on them. This would allow for creating an economy around these buffs as you would need to eventually obtain more rune stones to enchant more armor. You could possibly even have a whitelist/blacklist of items that could/could not be enchanted with the rune stone. Of course these buffs would all have to do with making you a better wand caster that fit with the Magic plugin theme.
 - Chance to fizzle should only happen if you have been cursed (I saw a spell that say's reduced chance of fizzle)

## To Review

 - MagicArenas: Doesn't TP players out on a draw.. ?
 - Getting hit with aqua erecto says "cast unknown" on you?
 - Admission+Break door = dupe door (thought this was fixed???)
 - Grow/Shrink doesn't work on rabbits?
 - Put a size cap on slimes with Grow

 - Shops/Items:
   - Add new command, mshop
     - /mshop create [shoptype] [title] : Create a new shop NPC, default type "buyshop" in configs
     - /mshop add [worth] <add item in hand to shop, or update worth if present>
     - /mshop remove <remove item in hand from shop>
     - Should look up itemstack (with count of 1) first, save as key if found
     - Eventually a GUI would be nice.

 - Alter/grow/shrink/rollback should prevent mobs dropping loot

 - See if snow that falls on something undoable can undo?

 - Undo bugs with pistons.. yuck. https://youtu.be/S-d-XT2r_vM

 - Arenas preventing last death Recall isn't working?

 - Show mana from offhand wand (if no wand in main hand)
 - Add builtin "help" command, check messaging for missing commands- shows "no permission"?
 - Expand armor to include power, other modifiers?

 - "Triggered" spells.. ? Apparently MagicSpells does this or somesuch.
   Examples- Activated spells/ passive spells Upon taking damage- activate a heal spell effect Upon dealing damage- explosion in line of sight

 - Add option to only cast spells when damaging an entity

 - Can still drop your wand by holding while closing your inv
 - Spell organization by path?

 - Pull/Accio on top of an entity sends it skyrocketing
 - Column's radius doesn't seem to work

 - CoreProtect integration
   http://minerealm.com/community/viewtopic.php?f=32&t=16362

 - Add effects template, for wands to stay one template but act like another (? - for cane)
 - Prevent TNT breaking secured chests

 - There's an area in the RoR where you can cast alohomora and it will spawn doors, in the center of the right side when you enter~
 - Need to fix some door-related undo issues, e.g. casting admission then breaking door sometimes drops door

 - Fix block targeting cast messages

 - Fix that horrible inv-dupe issue :(

 - Add spell or brush or something to auto-convert from MagicWorlds configs

 - Try to support projectile hitbox modifiers that aren't cubes

 - Overrides with commas in them are broken again

 - Spells acting oddly with Copy - Box, Iterate

 - Lead on possible undo issue- undo another player's spell while building, it still builds another tick or so?

 - Goggles deactivate on death (maybe only in Azkaban?)
 - Test baby/giant wither bosses:
   You have to set (every tick) the invulnerability of the wither.
   Either you get the data watcher and watch 20 with a value big but below than 1000. I use 600 not to have a too small wither. But there is an easier method which is r() which does it.
   ((CraftWither)wither).getHandle().r(600);
 - Aliases are broken /wand add doesn't activate, spell items don't work

 - Can't hitbox-target entities standing in a corner? Issues with spiders?
 - Add "drops" parameter to Damage action to prevent mobs dropping loot.

 - Add /mmap reload command

 - Hover text for spells
 - Cat GIF generator? http://catdb.yawk.at/images?tag=gif
 - Check Regenerate - got stuck, couldn't cancel?
 - Make Portal spell portals avoid creating a frame on the other side (maybe handle TP'ing on portal event?)
 - Logout on death still buggy? (can not reproduce)
 - Wand dupe issue: tl;dr : he was able to drag a wand while in the spell inventory.

 - Clean up MaterialBrush target system, automate somehow?
 - Copy target brush action for tandem replication
 - Glitching Fill behavior
 - Spell shop improvements:
   - Color spells player can't afford
   - Some sort of ability to set up in-game?
   - Add alphabetize option to base shop (hrm, no, config is a map.. bleh)
   - Allow air as filler blocks
 - Trait improvements
   - Add /parameter command for inspecting single param
 - Nerf Force (shift to break free? Fall protection?)
 - Make map work like replicate, clone - with start point set on activate
   - and maybe not repeat
 - Add repeat option for schematic brushes
   - or generalized option that works with map, too
 - /magic describe should show info about current spell?

# Old, Possibly Ignored

 - Arena spell/schematic
 - Magic stats (that persist) - block modified, etc. (Statistics API?)
 - Collapse blocklist chains on save (?)
 - EnderDragon familiars that don't do block damage or spawn a portal on death?

## NEW SPELLS

 - avatar - Goes into "avatar" state- free spells, super power, levitating, special effects- but rapidly decreasing mana while active.
    - will need a spell-based power/protection system, or use potion effects?
    - would be nice if this could extend from potioneffect and levitate- maybe refactor?
    - will need a separate "active cost reduction" property
 - banish - sets a player's home and spawn?
 - Fix/finish stairs spell
 - Fix tunnel spell
 - Some kind of "ramp" version of fill, like stairs but with arbitrary blocks?
 - Decorate, places paintings at target

## OTHER STUFF

 - Customize dynmap map wand pop-ups? Red with black shadows looked cool.. use wand effect color?
 - Make volley multi-sample?
 - Alter names sheep "jeb_", - others "Dinnerbone" ?

 - Need separate activate/deactivate costs. Fill vs levitate :\
   - Variable costs would be nice, too- for fill and superconstruct.
 - prevent pillar from passing through non-air blocks of different materials than the target
 - If possible, label more material data like stair direction.
 - Add locale option to suffix messages.yml

 - Show active spells in a different color

 - Continue work on combining wands on anvils

## STACK TRACES / EXTRA DEBUG INFO

## PLAYER REQUESTS:

 - Add some sort of integration with Partec plugin (Deprecated since 2015, so probably not)
   https://www.spigotmc.org/resources/partec-custom-particles-visuals-plugin-api.15386/
