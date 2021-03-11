package me.danetnaverno.inventorio.slot

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

open class ExtensionSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y)
{
    var canTakeItems = true

    override fun canTakeItems(playerEntity: PlayerEntity): Boolean
    {
        return canTakeItems
    }

    override fun canInsert(stack: ItemStack): Boolean
    {
        return canTakeItems
    }

    @Environment(EnvType.CLIENT)
    override fun doDrawHoveringEffect(): Boolean
    {
        return canTakeItems
    }
}