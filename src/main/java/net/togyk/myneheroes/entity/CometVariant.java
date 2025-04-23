package net.togyk.myneheroes.entity;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.ValueLists;
import net.togyk.myneheroes.util.ModTags;

import java.util.Random;
import java.util.function.IntFunction;

public enum CometVariant {
    DEFAULT(0, 6, ModTags.Blocks.COMET_CORE_BLOCKS, ModTags.Blocks.COMET_CRUST_BLOCKS),
    KRYPTONITE(1, 1, ModTags.Blocks.KRYPTONITE_COMET_CORE_BLOCKS, ModTags.Blocks.KRYPTONITE_COMET_CRUST_BLOCKS),
    SCULK(2, 2, ModTags.Blocks.SCULK_COMET_CORE_BLOCKS, ModTags.Blocks.SCULK_COMET_CRUST_BLOCKS),
    VIBRANIUM(3, 1, ModTags.Blocks.VIBRANIUM_COMET_CORE_BLOCKS, ModTags.Blocks.VIBRANIUM_COMET_CRUST_BLOCKS);

    public static final Codec<CometVariant> CODEC = Codec.INT.xmap(
            CometVariant::byId,      // decode: int → CometVariant
            CometVariant::getId      // encode: CometVariant → int
    );
    private static final IntFunction<CometVariant> BY_ID = ValueLists.createIdToValueFunction(CometVariant::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);
    private final int id;
    private final int weight;
    private final String coreBlockTag;
    private final String crustBlockTag;

    CometVariant(final int id, int weight, final String coreBlockTag, final String crustBlockTag) {
        this.id = id;
        this.weight = weight;
        this.coreBlockTag = coreBlockTag;
        this.crustBlockTag = crustBlockTag;
    }

    CometVariant(final int id, int weight, TagKey<Block> coreBlockTag, TagKey<Block> crustBlockTag) {
        this.id = id;
        this.weight = weight;
        this.coreBlockTag = coreBlockTag.id().toString();
        this.crustBlockTag = crustBlockTag.id().toString();
    }

    public int getId() {
        return this.id;
    }

    public static CometVariant byId(int id) {
        return (CometVariant) BY_ID.apply(id);
    }

    public TagKey<Block> getCoreBlockTag() {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(this.coreBlockTag));
    }

    public TagKey<Block> getCrustBlockTag() {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(this.crustBlockTag));
    }

    public static int getTotalWeight() {
        int w = 0;
        for (CometVariant v : values()) {
            w += v.weight;
        }
        return w;
    }

    public static CometVariant getRandomVariant(Random random) {
        int r = random.nextInt(getTotalWeight()) + 1;
        int cumulative = 0;
        for (CometVariant v : values()) {
            cumulative += v.weight;
            if (r <= cumulative) {
                return v;
            }
        }
        // should never happen if weights > 0
        return DEFAULT;
    }
}
