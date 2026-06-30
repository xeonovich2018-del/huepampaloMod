package com.huepampalo.world;

import com.huepampalo.blocks.ModBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class VenomOreFeature extends Feature<NoneFeatureConfiguration> {

    public VenomOreFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {

        WorldGenLevel world = ctx.level();
        BlockPos pos = ctx.origin();

        for (BlockPos check : BlockPos.betweenClosed(pos.offset(-4, -4, -4), pos.offset(4, 4, 4))) {

            if (world.getBlockState(check).is(Blocks.DIAMOND_ORE)
                    || world.getBlockState(check).is(Blocks.DEEPSLATE_DIAMOND_ORE)) {

                world.setBlock(pos, ModBlocks.VENOM_BLOCK.defaultBlockState(), 2);
                return true;
            }
        }

        return false;
    }
}