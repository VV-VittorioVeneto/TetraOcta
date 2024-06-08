package com.vv.octa.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class OctaKeyBindings {
    public static final String bindingGroup = "octa.binding.group";
    public static final KeyMapping openBinding;
    public static final KeyMapping safeModeBinding;
    public static final KeyMapping switchWindowBinding;

    public OctaKeyBindings () {
    }

    static {
        openBinding = new KeyMapping("octa.artillery.binding.open", OctaKeyBindings.OctaKeyConflictContext.artillery, KeyModifier.ALT, InputConstants.Type.KEYSYM, 66, "octa.binding.group");
        safeModeBinding = new KeyMapping("octa.artillery.binding.safe", OctaKeyBindings.OctaKeyConflictContext.artillery, KeyModifier.ALT, InputConstants.Type.KEYSYM, 71, "octa.binding.group");
        switchWindowBinding = new KeyMapping("octa.artillery.binding.switch", OctaKeyBindings.OctaKeyConflictContext.artillery, KeyModifier.ALT, InputConstants.Type.KEYSYM, 72, "octa.binding.group");
    }

    enum OctaKeyConflictContext implements IKeyConflictContext {
        artillery {
            public boolean isActive() {
                return Minecraft.getInstance().screen == null;
            }

            public boolean conflicts(IKeyConflictContext other) {
                return this == other;
            }
        };

        private OctaKeyConflictContext() {
        }
    }
}
