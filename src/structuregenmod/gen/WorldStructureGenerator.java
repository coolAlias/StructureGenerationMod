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

package structuregenmod.gen;

import java.util.Random;
import java.util.logging.Level;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import coolalias.structuregenapi.util.LogHelper;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldStructureGenerator implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch(world.provider.dimensionId)
		{
		case -1:
			// not currently generating anything in the nether
			// generateNether(world, random, chunkX * 16, chunkZ * 16);
			break;
		case 0:
			// 25% chance of a single structure per chunk; could make a weighted list
			// Recall that a chunk is only 16x16 blocks in area, so this is quite a lot of structures
			if (random.nextFloat() < 0.25F)
				generateStructure(world, random, chunkX * 16, chunkZ * 16);
			break;
		default:
			break;
		}
	}

	private final void generateStructure(World world, Random rand, int chunkX, int chunkZ)
	{
		// Need to create a new instance each time or the generate() methods may overlap themselves and cause a crash
		ModStructureGenerator gen = new ModStructureGenerator();
		int struct; // This will store a random index of the structure to generate

		struct = rand.nextInt(gen.structures.size());
		LogHelper.log(Level.INFO, "[GEN] Generating " + gen.structures.get(struct).name);
		int x = chunkX + rand.nextInt(16);
		int z = chunkZ + rand.nextInt(16);

		// nice way of getting a height to work from; it returns the topmost
		// non-air block at an x/z position, such as tall grass, dirt or leaves
		int y = world.getHeightValue(x, z);

		// find ground level, ignoring blocks such as grass and water
		while (!world.doesBlockHaveSolidTopSurface(x, y, z) && y > world.provider.getAverageGroundLevel())
		{
			--y;
		}

		if (!world.doesBlockHaveSolidTopSurface(x, y, z))
		{
			LogHelper.log(Level.INFO, "Failed to find suitable surface. Not generating structure. Block id " + world.getBlockId(x, y, z));
			return;
		}
		int widthX = gen.structures.get(struct).getWidthX();
		int widthZ = gen.structures.get(struct).getWidthZ();
		int height = gen.structures.get(struct).getHeight();

		// check if structure will collide with any others in area
		// might be able to use built-in StructureBoundBox
		/*
		for (int i = x - (widthX / 2); i < x + (widthX / 2) ; ++i)
		{
			for (int j = z - (widthZ / 2); j < z + (widthZ / 2); ++j)
			{
				for (int k = y; k < y + height; ++k)
				{
					// check for collisions somehow - no generated structures map to reference
				}
			}
		}
		 */
		// Set structure and random facing, then generate; no offset needed here
		gen.setStructure(gen.structures.get(struct));
		gen.setStructureFacing(rand.nextInt(4));
		gen.generate(world, rand, x, y, z);
	}
}
