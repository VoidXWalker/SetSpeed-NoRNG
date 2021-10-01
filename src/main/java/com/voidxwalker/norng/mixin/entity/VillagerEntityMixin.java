package com.voidxwalker.norng.mixin.entity;

import com.google.common.collect.ImmutableMap;
import com.voidxwalker.norng.villager.trade.offers.AccessWorkAround;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
    @Shadow public abstract VillagerData getVillagerData();
    /**
     * @author VoidXWalker
     * @reason always desired trades
     */
    @ModifyVariable(
            method = {"fillRecipes"},
            at = @At("STORE")
    )
    private Int2ObjectMap<TradeOffers.Factory[]> fillRecipes(Int2ObjectMap<TradeOffers.Factory[]> result) {
        if (VillagerProfession.FARMER.equals(this.getVillagerData().getProfession())) {
            return new Int2ObjectOpenHashMap(ImmutableMap.of(1, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.WHEAT, 20, 16, 2),   new AccessWorkAround.SellItemFactory(Items.BREAD, 1, 6, 16, 1)}, 2, new TradeOffers.Factory[]{new AccessWorkAround.SellItemFactory(Items.PUMPKIN_PIE, 1, 4, 5), new AccessWorkAround.SellItemFactory(Items.APPLE, 1, 4, 16, 5)}, 3, new TradeOffers.Factory[]{new AccessWorkAround.SellItemFactory(Items.COOKIE, 3, 18, 10), new AccessWorkAround.BuyForOneEmeraldFactory(Blocks.MELON, 4, 12, 20)}, 4, new TradeOffers.Factory[]{new AccessWorkAround.SellItemFactory(Blocks.CAKE, 1, 1, 12, 15), new AccessWorkAround.SellSuspiciousStewFactory(StatusEffects.SATURATION, 100, 15)}, 5, new TradeOffers.Factory[]{new AccessWorkAround.SellItemFactory(Items.GOLDEN_CARROT, 3, 3, 30), new AccessWorkAround.SellItemFactory(Items.GLISTERING_MELON_SLICE, 4, 3, 30)}));
        }
        else if (VillagerProfession.FISHERMAN.equals(this.getVillagerData().getProfession())){
            return new Int2ObjectOpenHashMap(ImmutableMap.of(1, new TradeOffers.Factory[]{ new AccessWorkAround.BuyForOneEmeraldFactory(Items.COAL, 10, 16, 2), new AccessWorkAround.SellItemFactory(Items.COD_BUCKET, 3, 1, 16, 1)}, 2, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.COD, 15, 16, 10), new AccessWorkAround.ProcessItemFactory(Items.SALMON, 6, Items.COOKED_SALMON, 6, 16, 5), new AccessWorkAround.SellItemFactory(Items.CAMPFIRE, 2, 1, 5)}, 3, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.SALMON, 13, 16, 20), new AccessWorkAround.SellEnchantedToolFactory(Items.FISHING_ROD, 3, 3, 10, 0.2F)}, 4, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.TROPICAL_FISH, 6, 12, 30)}, 5, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.PUFFERFISH, 4, 12, 30), new AccessWorkAround.TypeAwareBuyForOneEmeraldFactory(1, 12, 30, ImmutableMap.builder().put(VillagerType.PLAINS, Items.OAK_BOAT).put(VillagerType.TAIGA, Items.SPRUCE_BOAT).put(VillagerType.SNOW, Items.SPRUCE_BOAT).put(VillagerType.DESERT, Items.JUNGLE_BOAT).put(VillagerType.JUNGLE, Items.JUNGLE_BOAT).put(VillagerType.SAVANNA, Items.ACACIA_BOAT).put(VillagerType.SWAMP, Items.DARK_OAK_BOAT).build())}));
        }
        else if (VillagerProfession.CLERIC.equals(this.getVillagerData().getProfession())){
            return new Int2ObjectOpenHashMap(ImmutableMap.of(1, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.ROTTEN_FLESH, 32, 16, 2), new AccessWorkAround.SellItemFactory(Items.REDSTONE, 1, 2, 1)}, 2, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.GOLD_INGOT, 3, 12, 10), new AccessWorkAround.SellItemFactory(Items.LAPIS_LAZULI, 1, 1, 5)}, 3, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.RABBIT_FOOT, 2, 12, 20), new AccessWorkAround.SellItemFactory(Blocks.GLOWSTONE, 4, 1, 12, 10)}, 4, new TradeOffers.Factory[]{ new AccessWorkAround.BuyForOneEmeraldFactory(Items.GLASS_BOTTLE, 9, 12, 30), new AccessWorkAround.SellItemFactory(Items.ENDER_PEARL, 5, 1, 15)}, 5, new TradeOffers.Factory[]{new AccessWorkAround.BuyForOneEmeraldFactory(Items.NETHER_WART, 22, 12, 30), new AccessWorkAround.SellItemFactory(Items.EXPERIENCE_BOTTLE, 3, 1, 30)}));
        }
        else{
            return result;
        }
    }
}
