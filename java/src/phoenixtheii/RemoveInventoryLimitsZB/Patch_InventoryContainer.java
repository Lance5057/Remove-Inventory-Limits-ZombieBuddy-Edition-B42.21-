package phoenixtheii.RemoveInventoryLimitsZB;

import me.zed_0xff.zombie_buddy.annotations.Patch;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import zombie.characters.IsoGameCharacter;
import zombie.debug.DebugLog;
import zombie.inventory.types.InventoryContainer;

public class Patch_InventoryContainer {

	@Patch(className = "zombie.inventory.types.InventoryContainer", methodName = "setCapacity")
	public static class Patch_setCapacity {
		@Patch.OnEnter(skipOn = true)
		public static boolean enter(@Patch.This InventoryContainer ic, @Patch.Argument(0) int capacity,
				@Patch.Field("capacity") int outCapacity) {
			DebugLog.log("InventoryContainer::setCapacity - Capacity set to " + capacity);
			outCapacity = capacity;
			ic.getItemContainer().setCapacity(capacity);
			return true;
		}
	}

	@Patch(className = "zombie.inventory.types.InventoryContainer", methodName = "getCapacity")
	public static class Patch_getCapacity {
		@Patch.OnEnter(skipOn = true)
		public static boolean enter(@Patch.This InventoryContainer self) {

			return true;
		}

		@Patch.OnExit()
		public static void exit(@Patch.This InventoryContainer self, @Patch.Return(readOnly = false) int result) {
			DebugLog.log("Capacity is " + self.getContainer().getCapacity());
			result = self.getContainer().getCapacity();
		}

	}

	@Patch(className = "zombie.inventory.types.InventoryContainer", methodName = "getEffectiveCapacity")
	public static class Patch_getEffectiveCapacity {
		@Patch.OnEnter(skipOn = true)
		public static boolean enter(@Patch.This InventoryContainer self, @Patch.Argument(0) IsoGameCharacter chr) {
			return true;
		}

		@Patch.OnExit()
		public static void exit(@Patch.This InventoryContainer self, @Patch.Argument(0) IsoGameCharacter chr,
				@Patch.Return(readOnly = false) int result, @Patch.Field("capacity") int outCapacity) {
			DebugLog.log("InventoryContainer::getEffectiveCapacity - Capacity is " + self.getContainer().getCapacity());
			result = self.getContainer().getEffectiveCapacity(chr);
		}
	}
}
