package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.item.MobEnchantBookItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = EnchantWithMob.MODID)
public class ModCreativeTabs {
	@SubscribeEvent
	public static void registerCreativeTab(CreativeModeTabEvent.BuildContents event) {
		if (event.getTab() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(ModItems.ENCHANTER_SPAWNEGG.get());
		}
		if (event.getTab() == CreativeModeTabs.COMBAT) {
			event.accept(ModItems.MOB_UNENCHANT_BOOK.get());
			event.acceptAll(MobEnchantBookItem.generateMobEnchantmentBookTypesOnlyMaxLevel());
		}
	}
}
