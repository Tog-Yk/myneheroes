package net.togyk.myneheroes.entity;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.random.Random;
import net.togyk.myneheroes.util.ModTags;

import java.util.function.IntFunction;

public enum MeteorVariant {
    DEFAULT(0, 6, ModTags.Blocks.METEOR_CORE_BLOCKS, ModTags.Blocks.METEOR_CRUST_BLOCKS),
    KRYPTONITE(1, 1, ModTags.Blocks.KRYPTONITE_METEOR_CORE_BLOCKS, ModTags.Blocks.KRYPTONITE_METEOR_CRUST_BLOCKS),
    SCULK(2, 2, ModTags.Blocks.SCULK_METEOR_CORE_BLOCKS, ModTags.Blocks.SCULK_METEOR_CRUST_BLOCKS),
    VIBRANIUM(3, 1, ModTags.Blocks.VIBRANIUM_METEOR_CORE_BLOCKS, ModTags.Blocks.VIBRANIUM_METEOR_CRUST_BLOCKS);

    public static final Codec<MeteorVariant> CODEC = Codec.INT.xmap(
            MeteorVariant::byId,      // decode: int → MeteorVariant
            MeteorVariant::getId      // encode: MeteorVariant → int
    );
    private static final IntFunction<MeteorVariant> BY_ID = ValueLists.createIdToValueFunction(MeteorVariant::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);
    private final int id;
    private final int weight;
    private final String coreBlockTag;
    private final String crustBlockTag;

    MeteorVariant(final int id, int weight, final String coreBlockTag, final String crustBlockTag) {
        this.id = id;
        this.weight = weight;
        this.coreBlockTag = coreBlockTag;
        this.crustBlockTag = crustBlockTag;
    }

    MeteorVariant(final int id, int weight, TagKey<Block> coreBlockTag, TagKey<Block> crustBlockTag) {
        this.id = id;
        this.weight = weight;
        this.coreBlockTag = coreBlockTag.id().toString();
        this.crustBlockTag = crustBlockTag.id().toString();
    }

    public int getId() {
        return this.id;
    }

    public static MeteorVariant byId(int id) {
        return (MeteorVariant) BY_ID.apply(id);
    }

    public TagKey<Block> getCoreBlockTag() {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(this.coreBlockTag));
    }

    public TagKey<Block> getCrustBlockTag() {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(this.crustBlockTag));
    }

    public static int getTotalWeight() {
        int w = 0;
        for (MeteorVariant v : values()) {
            w += v.weight;
        }
        return w;
    }

    public static MeteorVariant getRandomVariant(Random random) {
        int r = random.nextInt(getTotalWeight()) + 1;
        int cumulative = 0;
        for (MeteorVariant v : values()) {
            cumulative += v.weight;
            if (r <= cumulative) {
                return v;
            }
        }
        // should never happen if weights > 0
        return DEFAULT;
    }
    public static MeteorVariant getRandomVariant(java.util.Random random) {
        int r = random.nextInt(getTotalWeight()) + 1;
        int cumulative = 0;
        for (MeteorVariant v : values()) {
            cumulative += v.weight;
            if (r <= cumulative) {
                return v;
            }
        }
        // should never happen if weights > 0
        return DEFAULT;
    }
}
