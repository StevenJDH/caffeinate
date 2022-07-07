/*
 * This file is part of caffeinate <https://github.com/StevenJDH/caffeinate>.
 * Copyright (C) 2022 Steven Jenkins De Haro.
 *
 * caffeinate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * caffeinate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with caffeinate.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.stevenjdh.caffeinate.hotkeys;

import com.github.kwhat.jnativehook.DefaultLibraryLocator;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import java.io.File;
import java.util.concurrent.ExecutorService;
import jnr.constants.platform.Signal;
import jnr.posix.POSIXFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecaffeinateNativeKeyListener implements NativeKeyListener {

    private static final Logger LOG = LoggerFactory.getLogger(DecaffeinateNativeKeyListener.class.getName());

    private short hotkeyFlag             = 0x00;   // 0000
    private static final short MASK_CRTL = 1 << 0; // 0001
    private static final short MASK_X    = 1 << 1; // 0010
    private static final short MASK_C    = 1 << 2; // 0100

    private boolean exitRequested;
    private final ExecutorService executor;    

    public DecaffeinateNativeKeyListener(ExecutorService executor) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            LOG.error("Failed to register the native hook: {}", ex.getMessage());
            System.exit(1);
        }
        exitRequested = false;
        this.executor = executor;
        disableDefaultConsoleExit();
        // Only works on supported operating systems, not Windows for example.
        var locator = new DefaultLibraryLocator();
        locator.getLibraries().forEachRemaining(File::deleteOnExit);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent evt) {
        switch (evt.getKeyCode()) {
            case NativeKeyEvent.VC_CONTROL:
                hotkeyFlag |= MASK_CRTL;
                break;
            case NativeKeyEvent.VC_X:
                hotkeyFlag |= MASK_X;
                break;
            case NativeKeyEvent.VC_C:
                hotkeyFlag |= MASK_C;
                break;
            default:
                hotkeyFlag = 0x00;
                break;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent evt) {
        if (hotkeyFlag != (MASK_CRTL ^ MASK_X ^ MASK_C) || exitRequested) {
            hotkeyFlag = 0x00;
            return;
        }

        exitRequested = true;
        
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            LOG.error(ex.getMessage());
        }

        LOG.info("Decaffeinated.");
        executor.shutdown();
    }

    private void disableDefaultConsoleExit() {
        final var posix = POSIXFactory.getJavaPOSIX();
        posix.signal(Signal.SIGINT, sig -> LOG.trace("Got signal: [{}]", sig));
    }
}