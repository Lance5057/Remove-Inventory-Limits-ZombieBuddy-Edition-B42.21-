package phoenixtheii.RemoveInventoryLimitsZB;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import zombie.core.math.PZMath;
import zombie.iso.IsoLot;
import zombie.iso.LotHeader;
import zombie.iso.MapFiles;


public abstract class Patch_LotHeadergetZombieIntensityForChunk {

    @RuntimeType
    public static int intercept(@Argument(0) LotHeader lotHeader, @Argument(1) int chunkX, @Argument(2) int chunkY) {
        //System.out.println("getZombieIntensityForChunk: We're here!");
        if (chunkX < 0 || chunkY < 0 || chunkX >= 32 || chunkY >= 32) {
            return -1;
        }
        if (lotHeader == null) {
            return -1;

        }
        for (int j = lotHeader.mapFiles.priority; j < IsoLot.MapFiles.size(); j++) {
            MapFiles mapFiles = (MapFiles) IsoLot.MapFiles.get(j);
            int cell300X = PZMath.fastfloor((lotHeader.cellX * 256 + chunkX * 8) / 300.0F);
            if (mapFiles.bgHasCell300
                    .getValue(cell300X - mapFiles.minCell300X, PZMath.fastfloor((lotHeader.cellY * 256 + chunkY * 8) / 300.0F) - mapFiles.minCell300Y)) {
                LotHeader lotHeader2 = mapFiles.getLotHeader(lotHeader.cellX, lotHeader.cellY);
                if (lotHeader2 == null) {
                    return -1;

                }

                return lotHeader2.getZombieIntensity(chunkX + chunkY * 32) & 0xFF;

            }
        }
        return -1;
    }

}


