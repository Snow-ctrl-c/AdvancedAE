package net.pedroksl.advanced_ae.client;

import appeng.api.util.AEColor;
import appeng.client.render.StaticItemColor;
import appeng.init.client.InitScreens;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.pedroksl.advanced_ae.common.AAEItemAndBlock;
import net.pedroksl.advanced_ae.gui.advpatternprovider.AdvPatternProviderMenu;
import net.pedroksl.advanced_ae.gui.advpatternprovider.AdvPatternProviderGui;

public class AAEClientRegistryHandler {
	public static final net.pedroksl.advanced_ae.client.AAEClientRegistryHandler INSTANCE = new net.pedroksl.advanced_ae.client.AAEClientRegistryHandler();

	public void init() {
		this.registerGui();
	}

	public void registerGui() {
		InitScreens.register(AdvPatternProviderMenu.TYPE, AdvPatternProviderGui::new, "/screens/adv_pattern_provider.json");
	}

	@SubscribeEvent
	@SuppressWarnings("deprecation")
	public void registerColorHandler(RegisterColorHandlersEvent.Item event) {
		var color = event.getItemColors();
		color.register(new StaticItemColor(AEColor.TRANSPARENT), AAEItemAndBlock.ADV_PATTERN_PROVIDER);
	}
}
