package net.pedroksl.advanced_ae.datagen;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.pedroksl.advanced_ae.AdvancedAE;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdvancedAE.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AAEDataGen {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent dataEvent) {
        var pack = dataEvent.getGenerator().getVanillaPack(true);
        var fileHelper = dataEvent.getExistingFileHelper();
        var lookup = dataEvent.getLookupProvider();
        pack.addProvider(p -> new AAEBlockStateProvider(p, fileHelper));
        var blockTagsProvider = pack.addProvider(p -> new AAEBlockTagProvider(p, lookup, fileHelper));
        pack.addProvider(p -> new AAEItemModelProvider(p, fileHelper));
        pack.addProvider(p -> new AAEItemTagProvider(p, lookup, blockTagsProvider.contentsGetter(), fileHelper));
        pack.addProvider(AAELootTableProvider::new);
        pack.addProvider(AAERecipeProvider::new);
    }
}
