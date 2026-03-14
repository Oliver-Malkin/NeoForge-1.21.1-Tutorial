package net.omalkin.tutorialmod.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.omalkin.tutorialmod.TutorialMod;
import net.omalkin.tutorialmod.worldgen.ModConfiguredFeatures;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower BLOODWOOD = new TreeGrower(TutorialMod.MODID + ":bloodwood",
            Optional.empty(), Optional.of(ModConfiguredFeatures.BLOODWOOD_KEY), Optional.empty());
}
