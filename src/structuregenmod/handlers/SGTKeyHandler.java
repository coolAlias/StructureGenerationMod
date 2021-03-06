/**
    Copyright (C) <2014> <coolAlias>

    This file is part of coolAlias' Structure Generation Tool; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package structuregenmod.handlers;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import structuregenmod.items.ItemStructureSpawnerBase;
import structuregenmod.lib.ModInfo;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SGTKeyHandler extends KeyHandler
{
	public static final String label = ModInfo.MOD_NAME + " Key";
			
	public SGTKeyHandler(KeyBinding[] keys, boolean[] repeat) { super(keys, repeat); }

	public SGTKeyHandler(KeyBinding[] keys) { super(keys); }

	@Override
	public String getLabel() { return this.label; }

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (tickEnd && FMLClientHandler.instance().getClient().inGameHasFocus) {
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemStructureSpawnerBase) {
				SGTPacketHandler.sendPacketKeyPress(kb.keyCode);
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {}

	@Override
	public EnumSet<TickType> ticks() { return EnumSet.of(TickType.CLIENT); }
}
