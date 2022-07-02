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

    @SuppressWarnings("java:S106")
    public static void main(String[] args) throws AWTException {        
        var executor = Executors.newSingleThreadScheduledExecutor();
        var robot = new Robot();
        
        System.out.println(getLogo());
        GlobalScreen.addNativeKeyListener(new DecaffeinateNativeKeyListener(executor));
        LOG.info("Caffeinating...");

        executor.scheduleWithFixedDelay(() -> {
            Point location = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(location.x, location.y); // Sets to same location to make invisible to user.
        }, 5, 5, TimeUnit.SECONDS);
    }
    
    private static String getLogo() {
        var sb = new StringBuilder();
        
        sb.append("MM'\"\"\"\"'YMM          .8888b .8888b          oo                     dP\n");            
        sb.append("M' .mmm. `M          88   \" 88   \"                                 88\n");            
        sb.append("M  MMMMMooM .d8888b. 88aaa  88aaa  .d8888b. dP 88d888b. .d8888b. d8888P .d8888b.\n"); 
        sb.append("M  MMMMMMMM 88'  `88 88     88     88ooood8 88 88'  `88 88'  `88   88   88ooood8\n"); 
        sb.append("M. `MMM' .M 88.  .88 88     88     88.  ... 88 88    88 88.  .88   88   88.  ...\n"); 
        sb.append("MM.     .dM `88888P8 dP     dP     `88888P' dP dP    dP `88888P8   dP   `88888P'\n"); 
        sb.append("MMMMMMMMMMM                                       Steven Jenkins De Haro v1.0.0\n");
        
        return sb.toString();
    }
}