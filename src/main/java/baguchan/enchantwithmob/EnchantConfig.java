package baguchan.enchantwithmob;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
        Pair<Client, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue showEnchantedMobHud;
        public final ForgeConfigSpec.BooleanValue disablePoisonParticle;
        public final ForgeConfigSpec.BooleanValue disableAuraRender;

        public Client(ForgeConfigSpec.Builder builder) {
            showEnchantedMobHud = builder
                    .translation(EnchantWithMob.MODID + ".config.showEnchantedMobHud")
                    .define("Show Enchanted Mob Hud", true);
            disablePoisonParticle = builder
                    .comment("Disable Poison Mob Enchant Particle. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.disablePoisonParticle")
                    .define("Disable Poison Particle", true);
            disableAuraRender = builder
                    .comment("Disable Aura Render. [true / false]")
                    .define("Disable Aura Render", true);
        }
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue naturalSpawnEnchantedMob;
        public final ForgeConfigSpec.BooleanValue spawnEnchantedAnimal;
        public final ForgeConfigSpec.BooleanValue enchantYourSelf;
        public final ForgeConfigSpec.BooleanValue changeSizeWhenEnchant;
        public final ForgeConfigSpec.BooleanValue dungeonsLikeHealth;
        public final ForgeConfigSpec.BooleanValue bigYourSelf;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANT_ON_SPAWN_EXCLUSION_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAY_ENCHANTABLE_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAY_ENCHANTABLE_ANCIENT_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLE_ENCHANTS;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLE_POISON_CLOUD_PROJECTILE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLE_MULTISHOT_PROJECTILE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> WHITELIST_SHOOT_ENTITY;

        public Common(ForgeConfigSpec.Builder builder) {
            naturalSpawnEnchantedMob = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.naturalSpawnEnchantedMob")
                    .define("Enchanted Mob can Spawn Natural", true);
            ENCHANT_ON_SPAWN_EXCLUSION_MOBS = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: minecraft:ender_dragon.")
                    .define("enchantOnSpawnExclusionMobs", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"));
            ALWAY_ENCHANTABLE_MOBS = builder
                    .comment("Allow the specific mob from alway receiveing enchantments on spawn. Use the full name, eg: minecraft:zombie.")
                    .define("alwayEnchantableMobs", Lists.newArrayList());
            ALWAY_ENCHANTABLE_ANCIENT_MOBS = builder
                    .comment("Allow the specific mob from alway receiveing enchantments as Ancient Mob on spawn(This feature may break for balance so be careful). Use the full name, eg: minecraft:zombie.")
                    .define("alwayEnchantableAncientMobs", Lists.newArrayList());
            DISABLE_ENCHANTS = builder
                    .comment("Disables the specific mob enchant. Use the full name(This config only disabled mob enchant when mob spawn. not mean delete complete, eg: enchantwithmob:thorn.")
                    .define("disableMobEnchants", Lists.newArrayList());
            DISABLE_POISON_CLOUD_PROJECTILE = builder
                    .comment("Disables the poison cloud for projectile. Use the full name(eg: minecraft:potion.")
                    .define("disablePoisonCloudProjectiles", Lists.newArrayList("minecraft:potion", "minecraft:experience_bottle", "minecraft:ender_pearl", "minecraft:egg", "earthmobsmod:smelly_egg"));
            DISABLE_MULTISHOT_PROJECTILE = builder
                    .comment("Disables the multi shot for projectile. Use the full name(eg: minecraft:potion.")
                    .define("disableMultiShotProjectiles", Lists.newArrayList("minecraft:experience_bottle", "minecraft:ender_pearl", "minecraft:eye_of_ender", "minecraft:egg", "earthmobsmod:smelly_egg", "conjurer_illager:throwing_card", "conjurer_illager:bouncy_ball"));

            WHITELIST_SHOOT_ENTITY = builder
                    .comment("Whitelist the projectile mob enchant for mob. Use the full name(eg: minecraft:zombie.")
                    .define("whitelistShootEntity", Lists.newArrayList("minecraft:skeleton", "minecraft:pillager", "minecraft:shulker", "minecraft:llama", "conjurer_illager:conjurer", "earthmobsmod:bone_spider", "earthmobsmod:lobber_zombie", "earthmobsmod:lobber_drowned"
                            , "earthmobsmod:melon_golem", "minecraft:piglin", "minecraft:snow_golem", "minecraft:player"));


            spawnEnchantedAnimal = builder
                    .comment("Enable the the spawning of enchanted animal mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.spawnEnchantedAnimal")
                    .define("Enchanted Animal can Spawn Natural", false);
            enchantYourSelf = builder
                    .comment("Enable enchanting yourself. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.enchantYourSelf")
                    .define("Enchant yourself", true);
            changeSizeWhenEnchant = builder
                    .comment("Enable Change Size When Enchanted. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.changeSizeWhenEnchant")
                    .define("Change Size", true);
            dungeonsLikeHealth = builder
                    .comment("Enable Increase Health like Dungeons When Enchanted. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.dungeonsLikeHealth")
                    .define("Increase Health like Dungeons", false);
            bigYourSelf = builder
                    .comment("Enable Player More Bigger When You have Huge Enchant. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.bigYourSelf")
                    .define("Big Your Self", false);
        }
    }

}
