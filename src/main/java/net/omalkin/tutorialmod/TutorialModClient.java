package net.omalkin.tutorialmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.omalkin.tutorialmod.block.entity.ModBlockEntities;
import net.omalkin.tutorialmod.block.entity.renderer.PedestalBlockEntityRenderer;
import net.omalkin.tutorialmod.entity.ModEntities;
import net.omalkin.tutorialmod.entity.client.ChairRenderer;
import net.omalkin.tutorialmod.entity.client.GeckoRenderer;
import net.omalkin.tutorialmod.entity.client.TomahawkProjectileRenderer;
import net.omalkin.tutorialmod.particle.BismuthParticles;
import net.omalkin.tutorialmod.particle.ModParticles;
import net.omalkin.tutorialmod.screen.ModMenuTypes;
import net.omalkin.tutorialmod.screen.custom.GrowthChamberScreen;
import net.omalkin.tutorialmod.screen.custom.PedestalScreen;
import net.omalkin.tutorialmod.util.ModItemProperties;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = TutorialMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = TutorialMod.MODID, value = Dist.CLIENT)
public class TutorialModClient {
    public TutorialModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        TutorialMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        TutorialMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        ModItemProperties.addCustomProperties();
        EntityRenderers.register(ModEntities.GECKO.get(), GeckoRenderer::new);
        EntityRenderers.register(ModEntities.TOMAHAWK.get(), TomahawkProjectileRenderer::new);
        EntityRenderers.register(ModEntities.CHAIR_ENTITY.get(), ChairRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.BISMUTH_PARTICLES.get(), BismuthParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL_BE.get(), PedestalBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PEDESTAL_MENU.get(), PedestalScreen::new);
        event.register(ModMenuTypes.GROWTH_CHAMBER_MENU.get(), GrowthChamberScreen::new);
    }
}
