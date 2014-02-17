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

package structuregenmod.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import structuregenmod.lib.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseModItem extends Item {

	public BaseModItem(int par1) {
		super(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName().substring(5).toLowerCase());
	}
}
