package net.omalkin.tutorialmod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.omalkin.tutorialmod.TutorialMod;
import net.omalkin.tutorialmod.entity.ModEntities;
import net.omalkin.tutorialmod.entity.client.GeckoModel;
import net.omalkin.tutorialmod.entity.custom.GeckoEntity;

@EventBusSubscriber(modid = TutorialMod.MODID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GeckoModel.LAYER_LOCATION, GeckoModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GECKO.get(), GeckoEntity.createAttributes().build());
    }
}
