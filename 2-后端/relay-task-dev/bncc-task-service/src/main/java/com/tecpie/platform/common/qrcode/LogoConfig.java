package com.tecpie.platform.common.qrcode;

import java.awt.*;

/**
 * Logo配置
 *
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author Tanzj
 * @date 2020/6/29 16:53
 */
public class LogoConfig {

    /** logo默认边框颜色 */
    public static final Color DEFAULT_BORDER_COLOR = Color.WHITE;
    /** logo默认边框宽度 */
    public static final int DEFAULT_BORDER = 2;
    /** logo大小默认为照片的1/5 */
    public static final int DEFAULT_LOGO_PART = 5;

    private final int border = DEFAULT_BORDER;

    private final Color borderColor;

    private final int logoPart;

    public LogoConfig() {
        this(DEFAULT_BORDER_COLOR, DEFAULT_LOGO_PART);
    }

    public LogoConfig(Color borderColor, int logoPart) {
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorder() {
        return border;
    }

    public int getLogoPart() {
        return logoPart;
    }
}
