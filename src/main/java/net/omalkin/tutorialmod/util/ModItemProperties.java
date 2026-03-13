package net.omalkin.tutorialmod.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.omalkin.tutorialmod.TutorialMod;
import net.omalkin.tutorialmod.component.ModDataComponents;
import net.omalkin.tutorialmod.item.ModItems;

public class ModItemProperties {
    public static void addCustomProperties() {
        ItemProperties.register(ModItems.CHISEL.get(), ResourceLocation.fromNamespaceAndPath(TutorialMod.MODID, "used"),
                ((stack, level, entity, seed) -> stack.get(ModDataComponents.COORDS) != null ? 1f : 0f));

        makeCustomBow(ModItems.KAUPEN_BOW.get());
    }

    public static void makeCustomBow(Item item) {
        ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pull"), (p_344163_, p_344164_, p_344165_, p_344166_) -> {
            if (p_344165_ == null) {
                return 0.0F;
            } else {
                return p_344165_.getUseItem() != p_344163_ ? 0.0F : (float)(p_344163_.getUseDuration(p_344165_) - p_344165_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(
                item,
                ResourceLocation.withDefaultNamespace("pulling"),
                (p_174630_, p_174631_, p_174632_, p_174633_) -> p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F
        );
    }
}
