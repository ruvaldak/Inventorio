package me.lizardofoz.inventorio.client.ui

import me.lizardofoz.inventorio.client.quickbar.QuickBarUIWidget
import me.lizardofoz.inventorio.mixin.client.accessor.HandledScreenAccessor
import me.lizardofoz.inventorio.player.PlayerAddon
import me.lizardofoz.inventorio.screenhandler.PlayerScreenHandlerAddon
import me.lizardofoz.inventorio.util.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object PlayerInventoryUIAddon
{
    private val BACKGROUND_TEXTURE = Identifier("inventorio", "textures/gui/player_inventory.png")

    private lateinit var playerAddon : PlayerAddon
    private lateinit var handlerAccessor: HandledScreenAccessor
    private lateinit var quickBarWidget: QuickBarUIWidget

    fun init(inventoryScreen: InventoryScreen)
    {
        playerAddon = PlayerAddon.Client.local
        quickBarWidget = QuickBarUIWidget(playerAddon)
        handlerAccessor = inventoryScreen as HandledScreenAccessor
        handlerAccessor.backgroundWidth = GUI_GENERAL_SCREEN_WIDTH
        handlerAccessor.backgroundHeight += playerAddon.getExtraRows() * INVENTORY_SLOT_SIZE
    }

    fun postInit()
    {
        (playerAddon.handlerAddon as PlayerScreenHandlerAddon).checkCapacity()
        if (handlerAccessor.buttons.isNotEmpty())
            handlerAccessor.buttons[0].x = -9385
        handlerAccessor.titleX = 130
    }

    fun drawAddon(matrices: MatrixStack)
    {
        MinecraftClient.getInstance().textureManager.bindTexture(BACKGROUND_TEXTURE)

        val screenX = handlerAccessor.x
        val screenY = handlerAccessor.y
        val extensionRows = playerAddon.getExtraRows()

        //Top Part
        DrawableHelper.drawTexture(matrices,
                screenX + GUI_PLAYER_INVENTORY_TOP_WITH_DECO.x, screenY + GUI_PLAYER_INVENTORY_TOP_WITH_DECO.y,
                CANVAS_PLAYER_INVENTORY_TOP_WITH_DECO.x.toFloat(), CANVAS_PLAYER_INVENTORY_TOP_WITH_DECO.y.toFloat(),
                GUI_PLAYER_INVENTORY_TOP_WITH_DECO.width, GUI_PLAYER_INVENTORY_TOP_WITH_DECO.height,
                256, 512)

        //Main Rows
        val guiMainRect = GUI_PLAYER_INVENTORY_MAIN_WITH_DECO(extensionRows)
        DrawableHelper.drawTexture(matrices,
                screenX + guiMainRect.x, screenY + guiMainRect.y,
                CANVAS_PLAYER_INVENTORY_MAIN_WITH_DECO.x.toFloat(), CANVAS_PLAYER_INVENTORY_MAIN_WITH_DECO.y.toFloat(),
                guiMainRect.width, guiMainRect.height,
                256, 512)

        //Extension Rows
        if (extensionRows > 0)
        {
            val guiExtensionRect1 = GUI_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P1(extensionRows)
            DrawableHelper.drawTexture(matrices,
                    screenX + guiExtensionRect1.x, screenY + guiExtensionRect1.y,
                    CANVAS_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P1.x.toFloat(), CANVAS_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P1.y.toFloat(),
                    guiExtensionRect1.width, guiExtensionRect1.height,
                    256, 512)

            val guiExtensionRect2 = GUI_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P2(extensionRows)
            DrawableHelper.drawTexture(matrices,
                    screenX + guiExtensionRect2.x, screenY + guiExtensionRect2.y,
                    CANVAS_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P2.x.toFloat(), CANVAS_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P2.y.toFloat(),
                    guiExtensionRect2.width, guiExtensionRect2.height,
                    256, 512)

            DrawableHelper.drawTexture(matrices,
                    screenX + GUI_PLAYER_INVENTORY_UTILITY_EXT.x, screenY + GUI_PLAYER_INVENTORY_UTILITY_EXT.y,
                    CANVAS_PLAYER_INVENTORY_UTILITY_EXT_WITH_DECO.x.toFloat(), CANVAS_PLAYER_INVENTORY_UTILITY_EXT_WITH_DECO.y.toFloat(),
                    GUI_PLAYER_INVENTORY_UTILITY_EXT.width, GUI_PLAYER_INVENTORY_UTILITY_EXT.height,
                    256, 512)
        }

        //QuickBar (Physical slots when present)
        val quickBarRect = GUI_PLAYER_INVENTORY_QUICK_BAR(extensionRows)
        quickBarWidget.drawPhysSlots(matrices,
                screenX + quickBarRect.x, screenY + quickBarRect.y,
                CANVAS_PLAYER_INVENTORY_PHYS_BAR.x, CANVAS_PLAYER_INVENTORY_PHYS_BAR.y,
                INVENTORIO_ROW_LENGTH,
                256, 512)

        //UtilityBeltFrame
        DrawableHelper.drawTexture(matrices,
                screenX + GUI_PLAYER_INVENTORY_UTILITY_SELECTION_START_POS.x + (playerAddon.inventoryAddon.selectedUtility / 4) * INVENTORY_SLOT_SIZE,
                screenY + GUI_PLAYER_INVENTORY_UTILITY_SELECTION_START_POS.y + (playerAddon.inventoryAddon.selectedUtility % 4) * INVENTORY_SLOT_SIZE,
                CANVAS_PLAYER_INVENTORY_UTILITY_SELECTION_WITH_DECO.x.toFloat(), CANVAS_PLAYER_INVENTORY_UTILITY_SELECTION_WITH_DECO.y.toFloat(),
                GUI_PLAYER_INVENTORY_UTILITY_SELECTION_START_POS.width, GUI_PLAYER_INVENTORY_UTILITY_SELECTION_START_POS.height,
                256, 512)

        //ToolBelt - Empty Items
        //This isn't particularly nice, but the built-in system requires an empty slot icon to be a part of a vanilla block atlas
        for ((absolute, relative) in TOOL_BELT_RANGE.withRelativeIndex())
        {
            if (playerAddon.player.inventory.getStack(absolute).isEmpty)
            {
                val slotSlot = SLOTS_PLAYER_INVENTORY_TOOL_BELT_SLOT(relative)
                val canvasSlot = CANVAS_PLAYER_INVENTORY_TOOL_BELT_SLOT(relative)
                DrawableHelper.drawTexture(matrices,
                        screenX + slotSlot.x, screenY + slotSlot.y,
                        canvasSlot.x.toFloat(), canvasSlot.y.toFloat(),
                        slotSlot.width, slotSlot.height,
                        256, 512)
            }
        }
    }

}