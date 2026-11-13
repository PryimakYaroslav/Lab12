
package Task3;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Task3 {

    public static void main(String[] args) {
        runDemo();
    }

    public static void runDemo() {
        // Створюємо альянс
        GameGroup alliance = new GameGroup("Альянс Півночі");

        // Створюємо дві гільдії
        GameGroup wolves = new GameGroup("Гільдія Вовків");
        GameGroup dragons = new GameGroup("Гільдія Драконів");

        // Створюємо загони
        GameGroup squad1 = new GameGroup("Загін Мисливців");
        GameGroup squad2 = new GameGroup("Загін Захисників");
        GameGroup squad3 = new GameGroup("Загін Магів");

        // Вкладений загін для рейду
        GameGroup raidSquad = new GameGroup("Рейдовий загін");

        // Додаємо гравців
        squad1.add(new Player("Ярик", 120));
        squad1.add(new Player("Андрій", 100));

        raidSquad.add(new Player("Максим", 90));
        raidSquad.add(new Player("Олег", 80));

        // Загін усередині загону
        squad1.add(raidSquad);

        squad2.add(new Player("Іван", 110));
        squad2.add(new Player("Петро", 95));

        squad3.add(new Player("Дмитро", 140));
        squad3.add(new Player("Богдан", 105));

        // Формуємо гільдії
        wolves.add(squad1);
        wolves.add(squad2);

        dragons.add(squad3);

        // Формуємо альянс
        alliance.add(wolves);
        alliance.add(dragons);

        // Виводимо дерево структури
        Log.event("SYSTEM", "Структура альянсу:");
        alliance.printTree("");

        // Підрахунок показників
        showStats(alliance);
        showStats(wolves);

        // Нарахування нагород
        Log.event("REWARD", "Нагорода для всього альянсу:");
        alliance.distributeReward(800);

        Log.event("REWARD", "Нагорода для гільдії Вовків:");
        wolves.distributeReward(400);
    }

    // Однаковий метод для альянсу та гільдії
    private static void showStats(GameComponent component) {
        Log.event("STATS",
                component.getName()
                        + " | Сила: " + component.getPower()
                        + " | Гравців: " + component.getPlayerCount());
    }
}

// Журнал подій
class Log {
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void event(String source, String message) {
        System.out.printf("[%s] [%s] %s%n",
                LocalTime.now().format(FORMAT),
                source,
                message);
    }
}

// Component — спільний інтерфейс
interface GameComponent {
    String getName();

    int getPower();

    int getPlayerCount();

    void printTree(String indent);

    void distributeReward(int amount);
}

// Leaf — окремий гравець
class Player implements GameComponent {
    private final String name;
    private final int power;

    public Player(String name, int power) {
        this.name = name;
        this.power = power;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getPlayerCount() {
        return 1;
    }

    @Override
    public void printTree(String indent) {
        Log.event("TREE",
                indent + "Гравець: " + name
                        + " | Сила: " + power);
    }

    @Override
    public void distributeReward(int amount) {
        Log.event("REWARD",
                "Гравець " + name
                        + " отримав " + amount + " золота");
    }
}

// Composite — група гравців
class GameGroup implements GameComponent {
    private final String name;

    private final List<GameComponent> members =
            new ArrayList<>();

    public GameGroup(String name) {
        this.name = name;
    }

    public void add(GameComponent component) {
        if (component == null || component == this) {
            throw new IllegalArgumentException(
                    "Некоректний учасник групи");
        }
        if (component instanceof GameGroup
                && ((GameGroup) component).containsGroup(this)) {
            throw new IllegalArgumentException(
                    "Циклічне вкладення груп заборонене");
        }
        members.add(component);
    }

    // Захист від циклічного вкладення груп
    private boolean containsGroup(GameGroup target) {
        if (this == target) {
            return true;
        }
        for (GameComponent member : members) {
            if (member instanceof GameGroup
                    && ((GameGroup) member).containsGroup(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPower() {
        int total = 0;

        for (GameComponent member : members) {
            total += member.getPower();
        }

        return total;
    }

    @Override
    public int getPlayerCount() {
        int total = 0;

        for (GameComponent member : members) {
            total += member.getPlayerCount();
        }

        return total;
    }

    @Override
    public void printTree(String indent) {
        Log.event("TREE",
                indent + "Група: " + name
                        + " | Сила: " + getPower()
                        + " | Гравців: " + getPlayerCount());

        for (GameComponent member : members) {
            member.printTree(indent + "    ");
        }
    }

    @Override
    public void distributeReward(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                    "Нагорода не може бути від'ємною");
        }

        int count = getPlayerCount();

        if (count == 0) {
            Log.event("REWARD",
                    "У групі " + name + " немає гравців");
            return;
        }

        // Розподіл порівну між усіма гравцями
        int each = amount / count;
        int remainder = amount % count;

        distributeRecursive(each, remainder);
    }

    private int distributeRecursive(int each, int remainder) {
        for (GameComponent member : members) {
            if (member instanceof Player) {
                int reward = each;

                if (remainder > 0) {
                    reward++;
                    remainder--;
                }

                member.distributeReward(reward);

            } else if (member instanceof GameGroup) {
                remainder = ((GameGroup) member)
                        .distributeRecursive(each, remainder);
            }
        }

        return remainder;
    }
}
