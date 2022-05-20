package net.mrlolf.xenchant.mixin;

import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Enchantment.class)
public class EnchantmentLevel {
    /**
     * @author Fabian Roscher
     */
    @Overwrite
    public int getMaxLevel() {
        return 1000;
    }
}
