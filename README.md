# FTBExtras

Let's you really see team/chunk information! For all those mod packs that are out of date, or servers looking for some more admin features.

## Permissions/Commands

Permission Node | Command | Info | Console | Player
----------------|---------|------|---------|---------
ftbextras.main  | /ft | Get team info of chunk | :x: | :heavy_check_mark:
ftbextras.online | /ft online | List online players by team | :heavy_check_mark: | :heavy_check_mark:
ftbextras.team.find  | /ft find | Find team of player | :heavy_check_mark: | :heavy_check_mark:
ftbextras.team.list | /ft list | List all teams | :heavy_check_mark: | :heavy_check_mark:
ftbextras.team.info | /ft info | Get info of team | :heavy_check_mark: | :heavy_check_mark:
ftbextras.chunk.claim | /ft claim | Claim chunks with radius/player | :x: | :heavy_check_mark:
ftbextras.chunk.unclaim | /ft unclaim | Unclaim all chunks of a team | :heavy_check_mark: | :heavy_check_mark:
ftbextras.admin.add | /ft add | Add a player to a team | :heavy_check_mark: | :heavy_check_mark:
ftbextras.admin.remove | /ft remove | Remove a player from a team | :heavy_check_mark: | :heavy_check_mark:
ftbextras.admin.delete | /ft delete | Delete a team | :heavy_check_mark: | :heavy_check_mark:
ftbextras.admin.promote | /ft promote | Promote a player to team owner | :heavy_check_mark: | :heavy_check_mark:
ftbextras.admin.settings | /ft settings | Open settings GUI of a team | :x: | :heavy_check_mark:
<!--ftbextras.chunk.list | /ft chunks | Get claimed hunks of team | :heavy_check_mark: | :heavy_check_mark:-->

## Commands

These are the currently available commands

``/ft``

Main command. Will show you info of chunk you are standing in, if it's a claimed chunk.

---
``/ft help``

Lists out the commands and arguments with a brief description.

---
``/ft online``

Lists online players grouped by team.

---
``/ft find <playername>``

Find the team of a player. Will list out team information as well.

---
``/ft list``

List out all the teams on the server.

---
``/ft info [teamid]``

Will list out the team information from optional teamid. Otherwise functions like ``/ft`` showing team info of a claimed chunk.

---
``/ft claim [-f] [radius] [teamid]``

Claim chunks in a radius and/or as another team. Supplying no arguments will claim the chunk you are standing in as for your team.
Use ``-f`` flag to force unclaim of exsiting chunks in the radius. Before unclaiming/claiming chunks a check is done to ensure the specified team has enough chunks avialable to claim the area. For server teams this check is bypassed. 

---
``/ft unclaim <teamid>``

Will unclaim all the chunks of a team.

---
``/ft add <playername> <teamid>``

Adds the specified player to supplied teamid.

---
``/ft remove <playername>``

Will remove the player from their team. If they are the only member their team will be deleted.

---
``/ft delete <teamid>``

Deletes the specified team.

---
``/ft promote <playername>``

Promotes the specified player to owner of the team they are in.

---
``/ft settings <teamid>``

Opens the team GUI settings for the supplied teamid. 



