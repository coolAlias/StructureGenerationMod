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

import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import structuregenmod.gen.ModStructureGenerator;
import coolalias.structuregenapi.util.LogHelper;
import coolalias.structuregenapi.util.Structure;
import coolalias.structuregenapi.util.StructureGeneratorBase;

public class ItemStructureSpawner extends ItemStructureSpawnerBase
{
	public ItemStructureSpawner(int par1) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
	public List<Structure> getStructureList(ItemStack itemstack) {
		return ModStructureGenerator.structures;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
	{
		if (itemstack.stackTagCompound == null) {
			initNBTCompound(itemstack);
		}

		if (!world.isRemote && ModStructureGenerator.structures.size() > 0)
		{
			/*
			LinkedStructureGenerator link = new LinkedStructureGenerator();
			Structure structure = getCurrentStructure(itemstack);
			
			link.setRotation(getData(itemstack, ROTATIONS));
			
			for (int i = 0; i < 6; ++i) {
				link.addStructureWithOffset(structure,(i/2) * structure.getWidthZ() - i % 2,0,(i % 2 == 0 ? 1 : -1) * (structure.getWidthX() / 2 + 1));
				link.setLastRotation(i % 2 == 0 ? 3 : 1);
			}
			for (int i = 0; i < 6; ++i) {
				link.addStructureWithOffset(structure,(i % 2 == 0 ? -1 : -2) * structure.getWidthX() - (i % 2 == 0 ? 0 : 1),0,(i/2 + 2) * structure.getWidthZ() - (i % 2 == 0 ? 1 : 0));
				link.setLastRotation(i % 2 == 0 ? 0 : 2);
			}
			
			link.generateLinkedStructures(player, world, world.rand, x, y+1, z);
			*/
			
			// Necessary for SMP compatibility, as using static variables will fail
			StructureGeneratorBase gen = new ModStructureGenerator();
			Structure structure = getCurrentStructure(itemstack);
			
			if (structure == null) {
				LogHelper.log(Level.WARNING, "Current structure is null.");
				return false;
			}
			
			if (world.getBlockId(x,y,z) == Block.snow.blockID) { --y; }
			
			gen.setPlayerFacing(player);
			gen.setRemoveStructure(getRemove(itemstack));
			gen.setStructureWithRotation(structure, getData(itemstack, ROTATIONS));
			gen.setDefaultOffset(structure.getOffsetX() + getData(itemstack, OFFSET_X), structure.getOffsetY() + getData(itemstack, OFFSET_Y), structure.getOffsetZ() + getData(itemstack, OFFSET_Z));
			gen.generate(world, world.rand, x, y, z);
		}

		return true;
	}
	
	/**
	 * Allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		String name = (getCurrentStructure(itemstack) != null ? getCurrentStructure(itemstack).name : "None");
		list.add(EnumChatFormatting.ITALIC + "Current: " + name);
	}
}
