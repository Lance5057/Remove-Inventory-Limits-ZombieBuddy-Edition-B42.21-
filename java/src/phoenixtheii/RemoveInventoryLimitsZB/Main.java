package phoenixtheii.RemoveInventoryLimitsZB;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import net.bytebuddy.agent.ByteBuddyAgent;
import zombie.SandboxOptions;
import zombie.debug.DebugLog;

public class Main {
    public static HashMap<Object, Long> millisCache = new HashMap<>();
    public static HashMap<Object, Float> floatCache = new HashMap<>();
    public static HashMap<Object, Integer> intCache = new HashMap<>();
    public static boolean getSandboxOption(String string, boolean f) {
        SandboxOptions.SandboxOption sandboxOption = SandboxOptions.instance.getOptionByName(string);
        if (sandboxOption == null) {
            return f;
        }
        boolean f2 = (boolean)Boolean.parseBoolean(sandboxOption.asConfigOption().getValueAsString());
        if (f != f2) {
            DebugLog.log("Nan sandbox value detected");
            return f;
        }
        return f2;
    }
    public static float getSandboxOption(String string, float f) {
        SandboxOptions.SandboxOption sandboxOption = SandboxOptions.instance.getOptionByName(string);
        if (sandboxOption == null) {
            return f;
        }
        float f2 = (float)Double.parseDouble(sandboxOption.asConfigOption().getValueAsString());
        if (f2 != f2) {
            DebugLog.log("Nan sandbox value detected");
            return f;
        }
        return f2;
    }
    public static void main(String[] args) {
        // Package-private: only ZombieBuddy's own classes may touch the instrumentation handle.
        // External mods (different package) cannot access this field directly, which prevents
        // an approved mod from gaining unrestricted bytecode manipulation of the entire JVM.
        // static Instrumentation g_instrumentation;

        // Got it @Zed, we'll do it another way then ;)



        //Reflection fetch Instrumentation:
        /*try {
            Instrumentation install = Main.fetchInstrumentation(Main.class.getName());
            PreMain.premain(args, install);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }*/

        //ByteBuddy easy way:
        Instrumentation install = ByteBuddyAgent.install();
        PreMain.premain(args, install);


        //SelfAttach Agent way:
        /*try {
            DebugLog.log("Cook a jar to feed to JVM");
            createJar();
            DebugLog.log("Allowing Self attachings... heehee");
            Properties properties = System.getProperties();
            properties.setProperty("jdk.attach.allowAttachSelf", Boolean.toString(true));
            DebugLog.log("Attaching to JVM");
            VirtualMachine jvm = VirtualMachine.attach(((Long)ProcessHandle.current().pid()).toString());
            DebugLog.log("Loading Remove Inventory Limits");
            jvm.loadAgent("removeinventorylimitszb.jar");
            jvm.detach();
            DebugLog.log("Seems the JVM loved my jar ;)");


        }
        catch (Throwable e) {
            e.printStackTrace();
        }*/


    }

    public static void createJar() {
        try {
            FileOutputStream fout = new FileOutputStream("removeinventorylimitszb.jar");
            JarOutputStream jarOut = new JarOutputStream(fout);
            jarOut.putNextEntry(new ZipEntry("META-INF/")); // Folder
            jarOut.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
            jarOut.write(Main.class.getResourceAsStream("/resources/MANIFEST.MF").readAllBytes());
            jarOut.closeEntry();

            jarOut.putNextEntry(new ZipEntry("phoenixtheii/RemoveInventoryLimitsZB/")); // Folder
            // s must end with "/".
            jarOut.putNextEntry(new ZipEntry("phoenixtheii/RemoveInventoryLimitsZB/Main.class"));
            jarOut.write(Main.class.getResourceAsStream("/phoenixtheii/RemoveInventoryLimitsZB/Main.class").readAllBytes());
            jarOut.closeEntry();

            jarOut.putNextEntry(new ZipEntry("phoenixtheii/RemoveInventoryLimitsZB/Patch_IsoGameCharacter.class"));
            jarOut.write(Main.class.getResourceAsStream("/phoenixtheii/RemoveInventoryLimitsZB/Patch_IsoGameCharacter.class").readAllBytes());
            jarOut.closeEntry();

            jarOut.putNextEntry(new ZipEntry("phoenixtheii/RemoveInventoryLimitsZB/Patch_ItemContainer.class"));
            jarOut.write(Main.class.getResourceAsStream("/phoenixtheii/RemoveInventoryLimitsZB/Patch_ItemContainer.class").readAllBytes());
            jarOut.closeEntry();

            jarOut.putNextEntry(new ZipEntry("phoenixtheii/RemoveInventoryLimitsZB/Patch_LotHeadergetZombieIntensityForChunk.class"));
            jarOut.write(Main.class.getResourceAsStream("/phoenixtheii/RemoveInventoryLimitsZB/Patch_LotHeadergetZombieIntensityForChunk.class").readAllBytes());
            jarOut.closeEntry();

            jarOut.putNextEntry(new ZipEntry("phoenixtheii/RemoveInventoryLimitsZB/PreMain.class"));
            jarOut.write(Main.class.getResourceAsStream("/phoenixtheii/RemoveInventoryLimitsZB/PreMain.class").readAllBytes());
            jarOut.closeEntry();

            jarOut.close();
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //WIP hack code to allow
    /*static
    {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            Unsafe unsafe = unsafeConstructor.newInstance();
            Field constructorModifiers = Constructor.class.getDeclaredField("modifiers");
            long constructorModifiersOffset = unsafe.objectFieldOffset(constructorModifiers);
            Field methodModifiers = Method.class.getDeclaredField("modifiers");
            long methodModifiersOffset = unsafe.objectFieldOffset(methodModifiers);
            Field fieldModifiers = Field.class.getDeclaredField("modifiers");
            long fieldModifiersOffset = unsafe.objectFieldOffset(fieldModifiers);
            Method setAccessible = AccessibleObject.class.getDeclaredMethod("setAccessible0", VM.class);
            setForceAccessible(setAccessible);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static boolean setForceAccessible(AccessibleObject accessibleObject)
    {
        try
        {
            if (accessibleObject instanceof Constructor)
            {
                Constructor<?> object = (Constructor<?>) accessibleObject;
                unsafe.getAndSetInt(object, constructorModifiersOffset, addPublicModifier(object.getModifiers()));
                return true;
            }
            if (accessibleObject instanceof Method)
            {
                Method object = (Method) accessibleObject;
                unsafe.getAndSetInt(object, methodModifiersOffset, addPublicModifier(object.getModifiers()));
                return true;
            }
            if (accessibleObject instanceof Field)
            {
                Field object = (Field) accessibleObject;
                unsafe.getAndSetInt(object, fieldModifiersOffset, addPublicModifier(object.getModifiers()));
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static int addPublicModifier(int mod)
    {
        mod &= ~ (Modifier.PRIVATE);
        mod &= ~ (Modifier.PROTECTED);
        mod |= (Modifier.PUBLIC);
        return mod;
    }*/


    /*
    private static Instrumentation fetchInstrumentation(String cname) throws Throwable {
        Class<?> c = Class.forName(cname, false, ClassLoader.getSystemClassLoader());
        Field f = c.getField("instrumentation");
        f.setAccessible(true);
        return (Instrumentation) f.get(null);

    }*/


}
