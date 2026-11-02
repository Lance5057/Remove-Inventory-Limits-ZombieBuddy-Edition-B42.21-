package phoenixtheii.RemoveInventoryLimitsZB;

import java.util.HashMap;

import me.zed_0xff.zombie_buddy.annotations.Patch;
import zombie.debug.DebugLog;
import zombie.inventory.ItemContainer;

public class Patch_ItemContainer {

	@Patch(className = "zombie.inventory.ItemContainer", methodName = "getContentsWeight")
	public static class Patch_getContentsWeight {
		public static HashMap<Object, Long> getInventoryWeightmillis = new HashMap<>();
		public static HashMap<Object, Float> getInventoryWeightTotal = new HashMap<>();
		public static HashMap<Object, Boolean> exited = new HashMap<>();

		@Patch.OnEnter(skipOn = true)
		public static boolean enter(@Patch.This ItemContainer self) {
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
		public static void exit(@Patch.This ItemContainer self, @Patch.Return(readOnly = false) float result) {
			Boolean bool = exited.get(self);
			if (bool != null && bool) {
				result = getInventoryWeightTotal.get(self);
			} else {
				getInventoryWeightTotal.put(self, result);
			}

		}

	}

	@Patch(className = "zombie.inventory.ItemContainer", methodName = "getCapacityWeight")
	public static class Patch_getCapacityWeight {
		public static HashMap<Object, Long> getInventoryWeightmillis = new HashMap<>();
		public static HashMap<Object, Float> getInventoryWeightTotal = new HashMap<>();
		public static HashMap<Object, Boolean> exited = new HashMap<>();

		@Patch.OnEnter(skipOn = true)
		public static boolean enter(@Patch.This ItemContainer self) {
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
		public static void exit(@Patch.This ItemContainer self, @Patch.Return(readOnly = false) float result) {
			Boolean bool = exited.get(self);
			if (bool != null && bool) {
				result = getInventoryWeightTotal.get(self);
			} else {
				DebugLog.log("ItemContainer::getCapacityWeight - Capacity set to " + result);
				getInventoryWeightTotal.put(self, result);
			}

		}

	}

	@Patch(className = "zombie.inventory.ItemContainer", methodName = "getCapacity")
	public static class Patch_getCapacity {

		@Patch.OnEnter(skipOn = true)
		public static boolean enter(@Patch.This ItemContainer container) {
//			DebugLog.log("ItemContainer::getCapacity - Capacity is " + container.capacity);
			return true;
		}

		@Patch.OnExit()
		public static void exit(@Patch.This ItemContainer container, @Patch.Return(readOnly = false) int result) {
			DebugLog.log("ItemContainer::getCapacity - Capacity is " + container.capacity);
			result = container.capacity;
		}
	}

	@Patch(className = "zombie.inventory.ItemContainer", methodName = "setCapacity")
	public static class Patch_setCapacity {

		@Patch.OnEnter(skipOn = true)
		public static boolean enter(@Patch.This ItemContainer container, @Patch.Argument(0) int capacity) {
			DebugLog.log("ItemContainer::setCapacity - Capacity set to " + capacity);
			container.capacity = capacity;
			return true;
		}
		
		
	}
}
