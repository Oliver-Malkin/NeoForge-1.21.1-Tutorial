package net.omalkin.tutorialmod;

import net.omalkin.tutorialmod.block.ModBlocks;
import net.omalkin.tutorialmod.component.ModDataComponents;
import net.omalkin.tutorialmod.effect.ModEffects;
import net.omalkin.tutorialmod.enchantment.ModEnchantmentEffects;
import net.omalkin.tutorialmod.entity.ModEntities;
import net.omalkin.tutorialmod.item.ModCreativeModeTabs;
import net.omalkin.tutorialmod.item.ModItems;
import net.omalkin.tutorialmod.particle.ModParticles;
import net.omalkin.tutorialmod.potion.ModPotions;
import net.omalkin.tutorialmod.sound.ModSounds;
import net.omalkin.tutorialmod.villager.ModVillagers;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TutorialMod.MODID)
public class TutorialMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tutorialmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public TutorialMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // This is the 'entry point' for each of the custom classes/deferred registers
        // Register the added items/tabs etc
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModEnchantmentEffects.register(modEventBus);
        ModEntities.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModParticles.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        /*
        How to add something to a creative tab that already exists

        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.BISMUTH);
            event.accept(ModItems.RAW_BISMUTH);
        }
        */
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Look at the TutorialModClient class instead
        //ModItemProperties.addCustomProperties(); // This causes the server to crash. It is a client only thing
    }
}
