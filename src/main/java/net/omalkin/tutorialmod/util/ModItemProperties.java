package net.omalkin.tutorialmod.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.omalkin.tutorialmod.TutorialMod;
import net.omalkin.tutorialmod.component.ModDataComponents;
import net.omalkin.tutorialmod.item.ModItems;

public class ModItemProperties {
    public static void addCustomProperties() {
        ItemProperties.register(ModItems.CHISEL.get(), ResourceLocation.fromNamespaceAndPath(TutorialMod.MODID, "used"),
                ((stack, level, entity, seed) -> stack.get(ModDataComponents.COORDS) != null ? 1f : 0f));
    }
}
