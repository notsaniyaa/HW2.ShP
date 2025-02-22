import java.util.*;

class Room {
    private final String name;
    private final String description;
    private final List<Item> items;
    private final Map<String, Room> exits;
    private boolean doorClosed;
    private String npc;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
        this.exits = new HashMap<>();
        this.doorClosed = false;  
        this.npc = null;  
    }

    public void addExit(String direction, Room room, boolean doorClosed) {
        exits.put(direction, room);
        this.doorClosed = doorClosed;
    }

    public Room getRoomInDirection(String direction) {
        return exits.get(direction);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item getItem(String itemName) {
        return items.stream()
                .filter(item -> item.name().equalsIgnoreCase(itemName))
                .findFirst()
                .orElse(null);
    }

    public void addNPC(String npcName) {
        this.npc = npcName;
    }

    public boolean hasNPC() {
        return npc != null;
    }

    public String getNPC() {
        return npc;
    }

    public boolean isDoorClosed() {
        return doorClosed;
    }

    public void openDoor() {
        doorClosed = false;
    }

    public String describe() {
        return STR."\{name} - \{description}\nItems here: \{items.isEmpty() ? "None" : String.join(", ", items.stream().map(Item::name).toArray(String[]::new))}" +
                (npc != null ? STR."\nYou see \{npc} here." : "");
    }
}

record Item(String name) {
}

class Player {
    private Room currentRoom;
    private final List<Item> inventory;

    public Player(Room startRoom) {
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }
}

public class MUDController {
    private final Player player;
    private boolean running;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
    }

    public void runGameLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the MUD game! Type 'help' for a list of commands.");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            handleInput(input);
        }
        scanner.close();
    }

    private void handleInput(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) pickUp(argument.substring(3));
                else System.out.println("Unknown command.");
                break;
            case "inventory":
                checkInventory();
                break;
            case "attack":
                attack();
                break;
            case "open":
                if ("door".equals(argument)) openDoor();
                else System.out.println("You can't open that.");
                break;
            case "talk":
                talk();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                System.out.println("Exiting game. Goodbye!");
                break;
            default:
                System.out.println("Unknown command.");
        }
    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.describe());
    }

    private void move(String direction) {
        Room currentRoom = player.getCurrentRoom();
        Room nextRoom = currentRoom.getRoomInDirection(direction);

        if (nextRoom != null) {
            if (currentRoom.isDoorClosed()) {
                System.out.println("The door is closed. Try opening it first.");
                return;
            }
            player.setCurrentRoom(nextRoom);
            System.out.println(STR."You moved \{direction}.");
            lookAround();
        } else {
            System.out.println("You can't go that way!");
        }
    }

    private void pickUp(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);
        if (item != null) {
            player.addItemToInventory(item);
            currentRoom.removeItem(item);
            System.out.println(STR."You picked up the \{itemName}.");
        } else {
            System.out.println(STR."No item named \{itemName} here!");
        }
    }

    private void checkInventory() {
        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying: ");
            for (Item item : inventory) {
                System.out.println(STR."- \{item.name()}");
            }
        }
    }

    private void attack() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.hasNPC()) {
            System.out.println(STR."You attack \{currentRoom.getNPC()}! They run away.");
            currentRoom.addNPC(null);
        } else {
            System.out.println("There is no one to attack here.");
        }
    }

    private void openDoor() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.isDoorClosed()) {
            currentRoom.openDoor();
            System.out.println("You open the door.");
        } else {
            System.out.println("The door is already open.");
        }
    }

    private void talk() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.hasNPC()) {
            System.out.println(STR."You talk to \{currentRoom.getNPC()}. They greet you warmly.");
        } else {
            System.out.println("There is no one to talk to.");
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Describe the current room");
        System.out.println("move <forward|back|left|right> - Move in a direction");
        System.out.println("pick up <itemName> - Pick up an item");
        System.out.println("inventory - Show your items");
        System.out.println("attack - Attack an NPC");
        System.out.println("open door - Open a closed door");
        System.out.println("talk - Talk to an NPC");
        System.out.println("help - Show this help menu");
        System.out.println("quit/exit - Leave the game");
    }

    public static void main(String[] args) {
        Room startRoom = new Room("Start Room", "A dark, cold chamber.");
        Room hallway = new Room("Hallway", "A long, narrow corridor.");
        startRoom.addExit("forward", hallway, true);  // Дверь закрыта
        hallway.addExit("back", startRoom, false);

        Item sword = new Item("sword");
        startRoom.addItem(sword);

        hallway.addNPC("old wizard");

        Player player = new Player(startRoom);
        MUDController controller = new MUDController(player);
        controller.runGameLoop();
    }
}
