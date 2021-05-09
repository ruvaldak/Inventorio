package me.lizardofoz.inventorio.mixin.client;

import me.lizardofoz.inventorio.client.ui.HotbarHUDRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 1500)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixinHP
{
    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    /**
     * This mixin calls the hotbar addon rendering
     */
    @Inject(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"))
    public void renderHotbarAddons(MatrixStack matrices, float tickDelta, CallbackInfo ci)
    {
        PlayerEntity playerEntity = getCameraPlayer();
        if (playerEntity != null && playerEntity.isAlive() && playerEntity.playerScreenHandler != null)
            HotbarHUDRenderer.INSTANCE.renderHotbarAddons(matrices);
    }

    /**
     * This mixin removes the vanilla hotbar display (both the frame and the item)
     */
    @Redirect(method = "renderHotbar",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getOffHandStack()Lnet/minecraft/item/ItemStack;"),
            require = 0)
    public ItemStack removeOffhandDisplayFromHotbar(PlayerEntity playerEntity)
    {
        return ItemStack.EMPTY;
    }
}