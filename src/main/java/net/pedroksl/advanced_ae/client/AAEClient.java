package net.pedroksl.advanced_ae.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.pedroksl.advanced_ae.AdvancedAE;
import net.pedroksl.advanced_ae.client.gui.*;
import net.pedroksl.advanced_ae.client.gui.OutputDirectionScreen;
import net.pedroksl.advanced_ae.client.renderer.AAECraftingUnitModelProvider;
import net.pedroksl.advanced_ae.client.renderer.ReactionChamberTESR;
import net.pedroksl.advanced_ae.common.blocks.AAECraftingUnitType;
import net.pedroksl.advanced_ae.common.definitions.AAEBlockEntities;
import net.pedroksl.advanced_ae.common.definitions.AAEItems;
import net.pedroksl.advanced_ae.common.definitions.AAEMenus;

import appeng.api.util.AEColor;
import appeng.client.gui.me.common.PinnedKeys;
import appeng.client.render.StaticItemColor;
import appeng.client.render.crafting.CraftingCubeModel;
import appeng.hooks.BuiltInModelHooks;
import appeng.init.client.InitScreens;

@SuppressWarnings("unused")
@Mod(value = AdvancedAE.MOD_ID, dist = Dist.CLIENT)
public class AAEClient extends AdvancedAE {

    private static AAEClient INSTANCE;

    public AAEClient(IEventBus eventBus, ModContainer container) {
        super(eventBus, container);

        eventBus.addListener(AAEClient::initScreens);
        eventBus.addListener(AAEClient::initCraftingUnitModels);
        eventBus.addListener(AAEClient::initItemColours);
        eventBus.addListener(AAEClient::initRenderers);
        eventBus.addListener(this::registerHotkeys);

        INSTANCE = this;

        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> {
            tickPinnedKeys(Minecraft.getInstance());
            Hotkeys.checkHotkeys();
        });
    }

    private static void initScreens(RegisterMenuScreensEvent event) {
        InitScreens.register(
                event, AAEMenus.QUANTUM_COMPUTER, QuantumComputerScreen::new, "/screens/quantum_computer.json");

        InitScreens.register(
                event,
                AAEMenus.ADV_PATTERN_PROVIDER,
                AdvPatternProviderScreen::new,
                "/screens/adv_pattern_provider.json");
        InitScreens.register(
                event,
                AAEMenus.SMALL_ADV_PATTERN_PROVIDER,
                SmallAdvPatternProviderScreen::new,
                "/screens/small_adv_pattern_provider.json");
        InitScreens.register(
                event, AAEMenus.ADV_PATTERN_ENCODER, AdvPatternEncoderScreen::new, "/screens/adv_pattern_encoder.json");
        InitScreens.register(
                event, AAEMenus.REACTION_CHAMBER, ReactionChamberScreen::new, "/screens/reaction_chamber.json");
        InitScreens.register(
                event, AAEMenus.QUANTUM_CRAFTER, QuantumCrafterScreen::new, "/screens/quantum_crafter.json");

        InitScreens.register(
                event, AAEMenus.STOCK_EXPORT_BUS, StockExportBusScreen::new, "/screens/stock_export_bus.json");

        InitScreens.register(
                event, AAEMenus.OUTPUT_DIRECTION, OutputDirectionScreen::new, "/screens/output_direction.json");
        InitScreens.register(
                event,
                AAEMenus.CRAFTER_PATTERN_CONFIG,
                QuantumCrafterConfigPatternScreen::new,
                "/screens/quantum_crafter_pattern_config.json");
        InitScreens.register(event, AAEMenus.SET_AMOUNT, SetAmountScreen::new, "/screens/aae_set_amount.json");

        InitScreens.register(
                event,
                AAEMenus.QUANTUM_ARMOR_CONFIG,
                QuantumArmorConfigScreen::new,
                "/screens/quantum_armor_config.json");
        InitScreens.register(
                event,
                AAEMenus.QUANTUM_ARMOR_FILTER_CONFIG,
                QuantumArmorFilterConfigScreen::new,
                "/screens/quantum_armor_filter_config.json");
    }

    @SuppressWarnings("deprecation")
    private static void initCraftingUnitModels(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (var type : AAECraftingUnitType.values()) {
                if (type == AAECraftingUnitType.STRUCTURE) {
                    BuiltInModelHooks.addBuiltInModel(
                            AdvancedAE.makeId("block/crafting/" + type.getAffix() + "_formed"),
                            new CraftingCubeModel(new AAECraftingUnitModelProvider(type)));
                }
                ItemBlockRenderTypes.setRenderLayer(type.getDefinition().block(), RenderType.cutout());
            }
        });
    }

    private void tickPinnedKeys(Minecraft minecraft) {
        // Only prune pinned keys when no screen is currently open
        if (minecraft.screen == null) {
            PinnedKeys.prune();
        }
    }

    @Override
    public void registerHotkey(String id) {
        Hotkeys.registerHotkey(id);
    }

    private void registerHotkeys(RegisterKeyMappingsEvent e) {
        Hotkeys.finalizeRegistration(e::register);
    }

    private static void initItemColours(RegisterColorHandlersEvent.Item event) {
        event.register(makeOpaque(new StaticItemColor(AEColor.TRANSPARENT)), AAEItems.THROUGHPUT_MONITOR.asItem());
    }

    private static void initRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AAEBlockEntities.REACTION_CHAMBER.get(), ReactionChamberTESR::new);
    }

    private static ItemColor makeOpaque(ItemColor itemColor) {
        return (stack, tintIndex) -> FastColor.ARGB32.opaque(itemColor.getColor(stack, tintIndex));
    }

    public static AAEClient instance() {
        return INSTANCE;
    }
}
