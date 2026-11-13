package Task2;

import java.util.*;

class VegetationType {
    private final String name;
    private final String model;
    private final String texture;
    private final String animation;

    public VegetationType(String name, String model,
                          String texture, String animation) {
        this.name = name;
        this.model = model;
        this.texture = texture;
        this.animation = animation;
    }

    public String getName() {
        return name;
    }

    public void displayInfo() {
        System.out.println("Вид: " + name +
                " | Модель: " + model +
                " | Текстура: " + texture +
                " | Анімація: " + animation);
    }
}

class Vegetation {
    private final VegetationType type;

    private final double x;
    private final double y;
    private final double scale;
    private final double rotation;

    public Vegetation(VegetationType type, double x,
                      double y, double scale, double rotation) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = rotation;
    }

    public VegetationType getType() {
        return type;
    }

    public void display() {
        System.out.println(
                type.getName() +
                        " | Координати: (" + x + ", " + y + ")" +
                        " | Масштаб: " + scale +
                        " | Поворот: " + rotation
        );
    }
}

// Фабрика для повторного використання видів
class VegetationFactory {
    private final Map<String, VegetationType> types = new HashMap<>();

    public VegetationType getType(String name) {
        return types.computeIfAbsent(name, this::createType);
    }

    private VegetationType createType(String name) {
        switch (name) {
            case "Дуб":
                return new VegetationType(
                        "Дуб", "oak.obj", "oak.png", "oak_wind.anim");

            case "Сосна":
                return new VegetationType(
                        "Сосна", "pine.obj", "pine.png", "pine_wind.anim");

            case "Береза":
                return new VegetationType(
                        "Береза", "birch.obj", "birch.png", "birch_wind.anim");

            case "Кущ":
                return new VegetationType(
                        "Кущ", "bush.obj", "bush.png", "bush_wind.anim");

            case "Кактус":
                return new VegetationType(
                        "Кактус", "cactus.obj", "cactus.png", "cactus_wind.anim");

            case "Камінь":
                return new VegetationType(
                        "Камінь", "rock.obj", "rock.png", "none");

            default:
                throw new IllegalArgumentException("Невідомий вид: " + name);
        }
    }

    public int getTypesCount() {
        return types.size();
    }
}

public class task2 {
    public static void main(String[] args) {
        VegetationFactory factory = new VegetationFactory();

        String[] names = {
                "Дуб", "Сосна", "Береза",
                "Кущ", "Кактус", "Камінь"
        };

        List<Vegetation> vegetation = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 100_000; i++) {
            String name = i < 2 ? "Дуб" :
                    names[random.nextInt(names.length)];

            VegetationType type = factory.getType(name);

            vegetation.add(new Vegetation(
                    type,
                    random.nextDouble() * 1000,
                    random.nextDouble() * 1000,
                    0.5 + random.nextDouble(),
                    random.nextDouble() * 360
            ));
        }

        System.out.println("Кількість об'єктів: " + vegetation.size());
        System.out.println("Кількість видів: " + factory.getTypesCount());

        Vegetation oak1 = vegetation.get(0);
        Vegetation oak2 = vegetation.get(1);

        System.out.println("\nПерший дуб:");
        oak1.display();

        System.out.println("\nДругий дуб:");
        oak2.display();

        System.out.println("\nОднаковий об'єкт виду: " +
                (oak1.getType() == oak2.getType()));
    }
}
