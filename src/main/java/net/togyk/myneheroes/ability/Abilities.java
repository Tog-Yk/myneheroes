package net.togyk.myneheroes.ability;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.detailed.*;
import net.togyk.myneheroes.client.HudType;
import net.togyk.myneheroes.entity.*;
import net.togyk.myneheroes.registry.ModRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Abilities {
    public static void registerAbilitiesAfterItems() {
        MyneHeroes.LOGGER.info("Registering abilities");

        BONE_CLAWS = registerAbility(new AddOrRemoveDualItemAbility(Identifier.of(MyneHeroes.MOD_ID, "bone_claws"), ModItems.BONE_CLAWS, 10, new Ability.Settings().appearsMultipleTimes(false)));
        ADAMANTIUM_CLAWS = registerAbility(new AddOrRemoveDualItemAbility(Identifier.of(MyneHeroes.MOD_ID, "adamantium_claws"), ModItems.ADAMANTIUM_CLAWS, 10, new Ability.Settings().appearsMultipleTimes(false)));

        for (Ability ability : ModRegistries.ABILITY.stream().toList()) {
            if (ability != null) {
                Ability copy = ability.copy();
                if (ability.getClass() != copy.getClass()) {
                    //not overridden the copy method correctly
                    throw new UnsupportedOperationException(
                            "Subclass of Ability must override copy() method: " + ability.getClass().getName()
                    );
                }
            } else {
                throw new UnsupportedOperationException(
                        "An Ability can't be null"
                );
            }
        }
    }

    public static final HudAbility TOGGLE_MECHANICAL_HUD = registerAbility(new HudAbility(Identifier.of(MyneHeroes.MOD_ID, "toggle_mechanical_hud"), new Ability.Settings(), HudType.MECHANICAL));
    public static final HudAbility TOGGLE_SPEEDSTER_HUD = registerAbility(new HudAbility(Identifier.of(MyneHeroes.MOD_ID, "toggle_speedster_hud"), new Ability.Settings(), HudType.SPEEDSTER));
    public static final HudAbility TUTOR_HUD = registerAbility(new HudAbility(Identifier.of(MyneHeroes.MOD_ID, "tutor_hud"), new Ability.Settings(), HudType.TUTOR));
    public static final Ability SHOOT_LASER = registerAbility(new Ability(Identifier.of(MyneHeroes.MOD_ID, "shoot_laser"), 10, new Ability.Settings(), (player) -> {
        ItemStack reactorStack = MyneHeroes.getReactorItemClass(player);
        if (!player.getWorld().isClient) {
            if (reactorStack.getItem() instanceof ReactorItem reactor) {
                int reactorPower = reactor.getStoredPowerOrDefault(reactorStack, 0);
                if (reactorPower >= 50) {
                    reactor.setStoredPower(reactorStack, reactorPower - 50);
                    // shoot a laser
                    Vec3d look = player.getRotationVec(1.0F);

                    LaserEntity projectile = new LaserEntity(ModEntities.LASER, player.getWorld());
                    projectile.setOwner(player);
                    projectile.setPosition(player.getX(), player.getEyeY() - projectile.getHeight(), player.getZ());
                    projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
                    projectile.applyDamageModifier(2.0F);
                    projectile.setColor(0x3300FFFF);
                    projectile.setInnerColor(0xFFF0FFFF);

                    player.getWorld().spawnEntity(projectile);
                    player.swingHand(Hand.MAIN_HAND);

                } else {
                    player.sendMessage(Text.literal("your reactor has to have enough charge"), true);
                }
            } else {
                player.sendMessage(Text.literal("you must have an arc reactor"), true);
            }
        }
        return true;
    }));
    public static final FlyFromReactorAbility ALLOW_FLYING = registerAbility(new FlyFromReactorAbility(Identifier.of(MyneHeroes.MOD_ID, "allow_flying"), new Ability.Settings(), 1));

    public static final ReleaseKineticEnergyAbility RELEASE_KINETIC_ENERGY = registerAbility(new ReleaseKineticEnergyAbility(Identifier.of(MyneHeroes.MOD_ID, "release_kinetic_energy"), new Ability.Settings(), 120, 200, 1.0F));

    public static final LaserEyesAbility LASER_EYES = registerAbility(new LaserEyesAbility(Identifier.of(MyneHeroes.MOD_ID, "laser_eyes"), 2, 1000, 4, new Ability.Settings(), 0xF0FFF0F0, 0x40FF0000));

    public static final Ability TAKE_OFF_SUIT = registerAbility(new Ability(Identifier.of(MyneHeroes.MOD_ID, "take_off_suit"), 10, new Ability.Settings().appearsMultipleTimes(false),  (player) -> {
        List<ItemStack> armor = new ArrayList<>();
        for (ItemStack stack : player.getArmorItems()) {
            if (stack.getItem() instanceof AdvancedArmorItem item) {
                List<Identifier> ids = item.getAbilities(stack).stream().map(Ability::getId).toList();
                List<Identifier> armor_ids = item.getArmorAbilities(stack).stream().map(Ability::getId).toList();
                if (ids.contains(Identifier.of(MyneHeroes.MOD_ID, "take_off_suit")) || armor_ids.contains(Identifier.of(MyneHeroes.MOD_ID, "take_off_suit"))) {
                    armor.add(stack);
                }
            }
        }
        if (armor.size() == 4) {
            if (!player.getWorld().isClient()) {
                StationaryArmorEntity entity = new StationaryArmorEntity(ModEntities.STATIONARY_ARMOR, player.getWorld());
                entity.setPosition(player.getX(), player.getY(), player.getZ());
                entity.setAngles(player.getYaw(), player.getPitch());
                entity.setVelocity(player.getVelocity());

                for (ItemStack stack : armor) {
                    entity.equipStack(((AdvancedArmorItem) stack.getItem()).getSlotType(), stack.copyAndEmpty());
                }

                player.getWorld().spawnEntity(entity);
                return true;
            }
        } else {
            player.sendMessage(Text.literal("All your equipment has to have this ability"), true);
        }
        return false;
    }));

    public static final StockpileLinkedAbility FROST_BREATH = registerAbility(new StockpileLinkedAbility(Identifier.of(MyneHeroes.MOD_ID, "frost_breath"), 4, 48, 8, new Ability.Settings(), null,  (player) -> {
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d origin = player.getEyePos().add(0, -0.2, 0);
        World world = player.getWorld();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            double speedX = (random.nextDouble()) * direction.x / 2;
            double speedY = (random.nextDouble()) * direction.y / 2;
            double speedZ = (random.nextDouble()) * direction.z / 2;
            world.addParticle(ParticleTypes.SNOWFLAKE,
                    origin.getX(),
                    origin.getY(),
                    origin.getZ(),
                    speedX, speedY, speedZ);
        }
        if (!world.isClient()) {

            Box area = new Box(origin, origin.add(direction.multiply(6))).expand(1.5);

            List<Entity> targets = world.getOtherEntities(player, area, entity -> entity instanceof LivingEntity);
            for (Entity entity : targets) {
                if (entity instanceof LivingEntity living && living.isAlive()) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2)); // Slowness III
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 1)); // Optional
                    entity.setFrozenTicks(Math.min( entity.getFrozenTicks() + 4, 200)); // Freezing effect (vanilla)
                }
            }

            // Optional: frost particles or freezing blocks
            Vec3d current = origin;
            for (int i = 0; i < 6 * 2; i++) {
                BlockPos pos = BlockPos.ofFloored(current);
                BlockPos below = pos.down();
                if (world.getBlockState(pos).isOf(Blocks.WATER)) {
                    world.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState());
                } else if (world.getBlockState(pos).isIn(BlockTags.FIRE)) {
                    BlockSoundGroup soundGroup = world.getBlockState(pos).getSoundGroup();
                    world.playSound(null ,pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, soundGroup.getVolume(), soundGroup.getPitch());
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                } else if (world.getBlockState(pos).isOf(Blocks.LAVA)) {
                    world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                } else if (world.getBlockState(below).isFullCube(world, below) && world.getBlockState(pos).isAir()) {
                    world.setBlockState(pos, Blocks.SNOW.getDefaultState());
                }
                current = current.add(direction.multiply(0.5));
            }

            return true;
        }
        return false;
    }));

    public static final VariableLinkedAbility SPEED_UP = registerAbility(new VariableLinkedAbility(Identifier.of(MyneHeroes.MOD_ID, "speed_up"), 2, new Ability.Settings(),  (player, power) -> {
        int speed = power.getInt("speedLevel");
        return power.setInt("speedLevel", speed + 1);
    }, ( power) -> {
        int speed = power.getInt("speedLevel");
        return power.canSetInt("speedLevel", speed + 1);
    }));

    public static final VariableLinkedAbility SPEED_DOWN = registerAbility(new VariableLinkedAbility(Identifier.of(MyneHeroes.MOD_ID, "speed_down"), 2, new Ability.Settings(),  (player, power) -> {
        int speed = power.getInt("speedLevel");
        return power.setInt("speedLevel", speed - 1);
    }, ( power) -> {
        int speed = power.getInt("speedLevel");
        return power.canSetInt("speedLevel", speed - 1);
    }));

    public static final VariableLinkedAbility TOGGLE_SPEED = registerAbility(new VariableLinkedAbility(Identifier.of(MyneHeroes.MOD_ID, "toggle_speed"), 2, new Ability.Settings(),  (player, power) -> {
        boolean isActive = power.getBoolean("speedActive");
        return power.setBoolean("speedActive", !isActive);
    }, (power) -> {
        boolean isActive = power.getBoolean("speedActive");
        return power.canSetBoolean("speedActive", !isActive);
    }));

    public static final VariableLinkedAbility TOGGLE_PHASING = registerAbility(new VariableLinkedAbility(Identifier.of(MyneHeroes.MOD_ID, "toggle_phasing"), 2, new Ability.Settings(),  (player, power) -> {
        boolean isPhasing = power.getBoolean("phasing");
        return power.setBoolean("phasing", !isPhasing);
    }, null, (power) -> {
        boolean isPhasing = power.getBoolean("phasing");
        return power.canSetBoolean("phasing", !isPhasing);
    }));

    public static final ToolbeltAbility TOOLBELT_3 = registerAbility(new ToolbeltAbility(Identifier.of(MyneHeroes.MOD_ID, "toolbelt_3"), new Ability.Settings(), 3));

    public static final SpiderSenseHudAbility SPIDER_SENSE = registerAbility(new SpiderSenseHudAbility(Identifier.of(MyneHeroes.MOD_ID, "spider_sense"), new Ability.Settings()));

    public static final ShootProjectilePassiveAbility<WebEntity> SHOOT_WEB = registerAbility(new ShootProjectilePassiveAbility<>(Identifier.of(MyneHeroes.MOD_ID, "shoot_web"), 2, 1, new Ability.Settings(), (player, passive) -> {
        Vec3d look = player.getRotationVec(1.0F);

        WebEntity projectile = new WebEntity(player.getWorld());
        projectile.setOwner(player);
        projectile.setPosition(player.getX(), player.getEyeY() - projectile.getHeight(), player.getZ());
        projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
        projectile.applyDamageModifier(2.0F);

        return projectile;
    }));

    public static final ShootProjectilePassiveAbility<WebEntity> SHOOT_TASER_WEB = registerAbility(new ShootProjectilePassiveAbility<>(Identifier.of(MyneHeroes.MOD_ID, "shoot_taser_web"), 2, 5, new Ability.Settings(), (player, passive) -> {
        Vec3d look = player.getRotationVec(1.0F);

        WebEntity projectile = new WebEntity(player.getWorld());
        projectile.setOwner(player);
        projectile.setPosition(player.getX(), player.getEyeY() - projectile.getHeight(), player.getZ());
        projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
        projectile.applyDamageModifier(2.0F);

        projectile.setIsTaser(true);

        return projectile;
    }));

    public static final ShootDiscardableProjectilePassiveAbility<WebSwingEntity> SHOOT_SWING_WEB = registerAbility(new ShootDiscardableProjectilePassiveAbility<>(Identifier.of(MyneHeroes.MOD_ID, "shoot_swing_web"), 2, 2, new Ability.Settings(), (player, passive) -> {
        Vec3d look = player.getRotationVec(1.0F);

        WebSwingEntity projectile = new WebSwingEntity(player.getWorld(), player);
        projectile.setPosition(player.getX(), player.getEyeY() - projectile.getHeight(), player.getZ());
        projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);

        return projectile;
    }));

    public static final PassiveSelectionAbility WEB_SHOOTER = registerAbility(new PassiveSelectionAbility(Identifier.of(MyneHeroes.MOD_ID, "web_shooter"), new Ability.Settings().appearsMultipleTimes(false), List.of(SHOOT_WEB, SHOOT_SWING_WEB, SHOOT_TASER_WEB)));

    public static final PassiveSelectionAbility ORGANIC_WEBBING = registerAbility(new PassiveSelectionAbility(Identifier.of(MyneHeroes.MOD_ID, "organic_webbing"), new Ability.Settings().appearsMultipleTimes(false), List.of(SHOOT_WEB, SHOOT_SWING_WEB)));

    public static final RageAbility RAGE = registerAbility(new RageAbility(Identifier.of(MyneHeroes.MOD_ID, "rage"), new Ability.Settings().appearsMultipleTimes(false), 0.25F));

    public static AddOrRemoveDualItemAbility BONE_CLAWS;
    public static AddOrRemoveDualItemAbility ADAMANTIUM_CLAWS;

    public static final ImmortalityAbility IMMORTALITY = registerAbility(new ImmortalityAbility(Identifier.of(MyneHeroes.MOD_ID, "immortality"), 2400, new Ability.Settings().appearsMultipleTimes(true)));

    private static <T extends Ability> T registerAbility(T ability) {
        return Registry.register(ModRegistries.ABILITY, ability.id, ability);
    }

    public static Ability get(Identifier id) {
        Ability ability = ModRegistries.ABILITY.get(id);
        if (ability != null) {
            return ability.copy();
        }
        return null;
    }

    public static boolean contains(Identifier id) {
        return ModRegistries.ABILITY.containsId(id);
    }
}