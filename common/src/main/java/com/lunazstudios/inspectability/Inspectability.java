package com.lunazstudios.inspectability;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public final class Inspectability {
    public static final String MOD_ID = "inspectability";

    public static void init() {
        if (Platform.getEnvironment() == Env.CLIENT) {
            InspectabilityClient.init();
        }
    }
}
