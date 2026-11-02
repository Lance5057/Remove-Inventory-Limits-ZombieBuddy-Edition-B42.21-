package phoenixtheii.RemoveInventoryLimitsZB;

import java.util.HashMap;

import me.zed_0xff.zombie_buddy.annotations.Patch;
import zombie.characters.IsoGameCharacter;


public class Patch_IsoGameCharacter {
    @Patch(className = "zombie.characters.IsoGameCharacter", methodName = "getInventoryWeight")
    public static class Patch_IsoGameCharactergetInventoryWeight {
        public static HashMap<Object, Long> getInventoryWeightmillis = new HashMap<>();
        public static HashMap<Object, Float> getInventoryWeightTotal = new HashMap<>();
        public static HashMap<Object, Boolean> exited = new HashMap<>();

        @Patch.OnEnter(skipOn = true)
        public static boolean enter(@Patch.This IsoGameCharacter self) {
            long millis = System.currentTimeMillis();
            Long l = getInventoryWeightmillis.get(self);
            if (l != null && (millis - l) < 100) {
                exited.put(self, true);
                return true;
            }
            exited.put(self, false);
            getInventoryWeightmillis.put(self, millis);
            return false;
        }

        @Patch.OnExit
        public static void exit(@Patch.This IsoGameCharacter self, @Patch.Return(readOnly = false) float result) {
            Boolean bool = exited.get(self);
            if (bool != null && bool) {
                result = getInventoryWeightTotal.get(self);
            } else {
                getInventoryWeightTotal.put(self, result);
            }

        }
    }
}



