package Task1;

import java.util.Arrays;
import java.util.List;

// Інтерфейс стихії
interface Element {
    String getName();
    void applyEffect(String target);
}

class Fire implements Element {
    @Override
    public String getName() {
        return "Вогонь";
    }

    @Override
    public void applyEffect(String target) {
        System.out.println(target + " отримує ефект: підпалення!");
    }
}

class Ice implements Element {
    @Override
    public String getName() {
        return "Крига";
    }

    @Override
    public void applyEffect(String target) {
        System.out.println(target + " отримує ефект: уповільнення!");
    }
}

class Lightning implements Element {
    @Override
    public String getName() {
        return "Блискавка";
    }

    @Override
    public void applyEffect(String target) {
        System.out.println(target + " отримує ефект: оглушення!");
    }
}

class Poison implements Element {
    @Override
    public String getName() {
        return "Отрута";
    }

    @Override
    public void applyEffect(String target) {
        System.out.println(target + " отримує ефект: отруєння!");
    }
}

// Базовий клас уміння
abstract class Skill {
    protected Element element;

    public Skill(Element element) {
        this.element = element;
    }

    public abstract void use(String caster, List<String> targets);
}

class SingleTargetSkill extends Skill {
    public SingleTargetSkill(Element element) {
        super(element);
    }

    @Override
    public void use(String caster, List<String> targets) {
        if (targets.isEmpty()) {
            return;
        }

        String target = targets.get(0);

        System.out.println(caster + " застосовує удар по одній цілі стихії "
                + element.getName());

        element.applyEffect(target);
    }
}

class AreaSkill extends Skill {
    public AreaSkill(Element element) {
        super(element);
    }

    @Override
    public void use(String caster, List<String> targets) {
        System.out.println(caster + " застосовує удар по площі стихії "
                + element.getName());

        for (String target : targets) {
            element.applyEffect(target);
        }
    }
}

class BarrierSkill extends Skill {
    public BarrierSkill(Element element) {
        super(element);
    }

    @Override
    public void use(String caster, List<String> targets) {
        System.out.println(caster + " створює захисний бар'єр стихії "
                + element.getName());

        element.applyEffect("Ворог, який торкнувся бар'єра");
    }
}

public class task1 {
    public static void main(String[] args) {
        String player = "Маг";

        List<String> enemies = Arrays.asList(
                "Гоблін", "Орк", "Скелет"
        );

        Skill fireAttack = new SingleTargetSkill(new Fire());
        fireAttack.use(player, enemies);

        System.out.println();

        Skill iceAttack = new AreaSkill(new Ice());
        iceAttack.use(player, enemies);

        System.out.println();

        Skill lightningBarrier = new BarrierSkill(new Lightning());
        lightningBarrier.use(player, enemies);

        System.out.println();

        Skill poisonAttack = new AreaSkill(new Poison());
        poisonAttack.use(player, enemies);
    }
}