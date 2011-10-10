package com.suntek.scheduler.ui;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * <p>Title: Appointment Scheduler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Sunteksystems</p>
 *
 * @author Steve Cheang
 * @version 1.0
 */
public class RubyTheme extends DefaultMetalTheme {

    public RubyTheme() {
    }

    public String getName() {
        return "Ruby";
    }

    public static final ColorUIResource red = new ColorUIResource(164, 77, 77); //red
    public static final ColorUIResource lightRed = new ColorUIResource(192, 118, 118); //light red
    public static final ColorUIResource lightRed2 = new ColorUIResource(208, 176, 176); // light red

    public static final ColorUIResource grey = new ColorUIResource(102, 102, 102); //grey
    public static final ColorUIResource grey2 = new ColorUIResource(153, 153, 153); //grey
    public static final ColorUIResource lightGreen = new ColorUIResource(219, 219, 181); //green

    protected ColorUIResource getPrimary1() {
        return red;
    }

    protected ColorUIResource getPrimary2() {
        return lightRed;
    }

    protected ColorUIResource getPrimary3() {
        return lightRed2;
    }

    protected ColorUIResource getSecondary1() {
        return grey;
    }

    protected ColorUIResource getSecondary2() {
        return grey2;
    }

    protected ColorUIResource getSecondary3() {
        return lightGreen;
    }

}
