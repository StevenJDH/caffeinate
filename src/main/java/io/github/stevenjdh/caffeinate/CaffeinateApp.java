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

package io.github.stevenjdh.caffeinate;

import com.github.kwhat.jnativehook.GlobalScreen;
import io.github.stevenjdh.caffeinate.hotkeys.DecaffeinateNativeKeyListener;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaffeinateApp {

    private static final Logger LOG = LoggerFactory.getLogger(CaffeinateApp.class.getName());

    public static void main(String[] args) throws AWTException {        
        var executor = Executors.newSingleThreadScheduledExecutor();
        var robot = new Robot();

        GlobalScreen.addNativeKeyListener(new DecaffeinateNativeKeyListener(executor));
        LOG.info("Caffeinating...");

        executor.scheduleWithFixedDelay(() -> {
            Point location = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(location.x, location.y); // Sets to same location to make invisible to user.
        }, 5, 5, TimeUnit.SECONDS);
    }
}