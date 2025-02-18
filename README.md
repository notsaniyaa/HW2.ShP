# HW2.ShP
MUD Game (Text-based Adventure)
This project is a simple MUD (Multi-User Dungeon) text-based game where the player can explore rooms, pick up items, open doors, interact with NPCs, and even attack them.

📜 Description
The player starts in a dark room and can:

Explore the surroundings (look)
Move in different directions (move <direction>)
Pick up items (pick up <item name>)
Check inventory (inventory)
Open doors (open door)
Talk to NPCs (talk)
Attack NPCs (attack)
Get a list of available commands (help)
Exit the game (quit / exit)
🛠️ Installation & Run
1. Clone the repository
git clone https://github.com/your-username/mud-game.git
cd mud-game
2. Compile the game
javac MUDController.java
3. Run the game

java MUDController
🎮 How to Play
Once the game starts, you will see a prompt (>) where you can enter commands.
Example gameplay:

look
Start Room - A dark, cold chamber.
Items here: sword

pick up sword
You picked up the sword.

move forward
The door is closed. Try opening it first.

open door
You open the door.

move forward
You moved forward.
Hallway - A long, narrow corridor.
You see an old wizard here.

talk
You talk to the old wizard. They greet you warmly.

attack
You attack the old wizard! They run away.
📜 Commands
Command	Description
look	Describes the current room
move <direction>	Moves the player (e.g., move forward)
pick up <item>	Picks up an item (e.g., pick up sword)
inventory	Displays items in inventory
open door	Opens a door if it is closed
talk	Talks to NPCs (if any)
attack	Attacks NPCs (if any)
help	Displays a list of commands
quit / exit	Exits the game
📌 TODO / Improvements
 Add more NPCs with different dialogues
 Implement a combat system
 Create more rooms and complex mechanics
📝 License
This project is licensed under the MIT License.
