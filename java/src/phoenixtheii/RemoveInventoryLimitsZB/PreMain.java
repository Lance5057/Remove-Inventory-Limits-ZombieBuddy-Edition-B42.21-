package phoenixtheii.RemoveInventoryLimitsZB;

import static net.bytebuddy.matcher.ElementMatchers.is;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import zombie.characters.IsoGameCharacter;
import zombie.debug.DebugLog;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.InventoryContainer;
import zombie.iso.LotHeader;

public class PreMain {

	public static void premain(String[] args, Instrumentation instrumentation) {
		try {
			DebugLog.log("Installing/transforming classes to instrumentation. Cool!");
			byte[] getZombieIntensityForChunks = new ByteBuddy().redefine(LotHeader.class)
					.method(is(LotHeader.class.getMethod("getZombieIntensityForChunk", LotHeader.class, int.class,
							int.class)))
					.intercept(MethodDelegation.to(Patch_LotHeadergetZombieIntensityForChunk.class)).make().getBytes();
			instrumentation.redefineClasses(new ClassDefinition(LotHeader.class, getZombieIntensityForChunks));
			System.out.println("RemoveInventoryLimitsZB: IsoGameCharacter.getZombieIntensityForChunk, redefined");


		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

}
