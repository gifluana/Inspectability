package com.lunazstudios.inspectability.neoforge;

import com.lunazstudios.inspectability.Inspectability;
import net.neoforged.fml.common.Mod;

@Mod(Inspectability.MOD_ID)
public final class InspectabilityNeoForge {
    public InspectabilityNeoForge() {
        // Run our common setup.
        Inspectability.init();
    }
}
