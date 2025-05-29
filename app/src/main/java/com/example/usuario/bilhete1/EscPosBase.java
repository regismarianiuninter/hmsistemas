package com.example.usuario.bilhete1;

public class EscPosBase {

    // from: EscPosBase
    public static final byte ESC = 0x1B;// Escape
    public static final byte FS =  0x1C;// Text delimiter
    public static final byte GS =  0x1D;// Group separator
    public static final byte DLE = 0x10;// data link escape
    public static final byte EOT = 0x04;// End of transmission
    public static final byte ENQ = 0x05;// Enquiry character
    public static final byte SP =  0x20;// Spaces
    public static final byte HT =  0x09;// Horizontal list
    public static final byte LF =  0x0A;// Print and wrap (horizontal orientation)
    public static final byte CR =  0x0D;// Home key
    public static final byte FF =  0x0C;// Carriage control (print and return to the standard mode (in page mode))
    public static final byte CAN = 0x18;// Canceled (cancel print data in page mode)


    // from: EscPosPrinterCommands
    public static final byte[] RESET_PRINTER = new byte[]{0x1B, 0x40};

    public static final byte[] TEXT_ALIGN_LEFT = new byte[]{0x1B, 0x61, 0x00};
    public static final byte[] TEXT_ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};
    public static final byte[] TEXT_ALIGN_RIGHT = new byte[]{0x1B, 0x61, 0x02};

    public static final byte[] TEXT_WEIGHT_NORMAL = new byte[]{0x1B, 0x45, 0x00};
    public static final byte[] TEXT_WEIGHT_BOLD = new byte[]{0x1B, 0x45, 0x01};

    public static final byte[] TEXT_FONT_A = new byte[]{0x1B, 0x4D, 0x00};
    public static final byte[] TEXT_FONT_B = new byte[]{0x1B, 0x4D, 0x01};
    public static final byte[] TEXT_FONT_C = new byte[]{0x1B, 0x4D, 0x02};
    public static final byte[] TEXT_FONT_D = new byte[]{0x1B, 0x4D, 0x03};
    public static final byte[] TEXT_FONT_E = new byte[]{0x1B, 0x4D, 0x04};

    public static final byte[] TEXT_SIZE_NORMAL = new byte[]{0x1D, 0x21, 0x00};
    public static final byte[] TEXT_SIZE_DOUBLE_HEIGHT = new byte[]{0x1D, 0x21, 0x01};
    public static final byte[] TEXT_SIZE_DOUBLE_WIDTH = new byte[]{0x1D, 0x21, 0x10};
    public static final byte[] TEXT_SIZE_BIG = new byte[]{0x1D, 0x21, 0x11};
    public static final byte[] TEXT_SIZE_2 = new byte[]{0x1D, 0x21, 0x01};

    public static byte[] getFontSmalll() {
        return new byte[]{0x1D, 0x21, 0x00}; // Font normal condensada 1x1
    }
    public static byte[] getFontNormall() {
        return new byte[]{0x1D, 0x21, 0x00}; // Font Normal Tamanho 1x1
    }
    public static byte[] getFontLarge() {
        return new byte[]{0x1D, 0x21, 0x01}; // Font Grande Tamanho 2x2
    }
    public static byte[] getFontExtraLarge() {
        return new byte[]{0x1D, 0x21, 0x02}; // Font Extra grande Tamanho 4x4
    }

    public static byte[] condensado()
    {
        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        cmd[2] |= 0x01;

        return cmd;
    }

    public static byte[] pordefecto()
    {
        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        cmd[2] |= 0x00;
        return cmd;
    }


    public static final byte[] TEXT_SIZE_SMALL = new byte[]{0x1D, 0x00, 0x00};


    public static final byte[] TEXT_UNDERLINE_OFF = new byte[]{0x1B, 0x2D, 0x00};
    public static final byte[] TEXT_UNDERLINE_ON = new byte[]{0x1B, 0x2D, 0x01};
    public static final byte[] TEXT_UNDERLINE_LARGE = new byte[]{0x1B, 0x2D, 0x02};

    public static final byte[] TEXT_DOUBLE_STRIKE_OFF = new byte[]{0x1B, 0x47, 0x00};
    public static final byte[] TEXT_DOUBLE_STRIKE_ON = new byte[]{0x1B, 0x47, 0x01};

    public static final byte[] TEXT_COLOR_BLACK = new byte[]{0x1B, 0x72, 0x00};
    public static final byte[] TEXT_COLOR_RED = new byte[]{0x1B, 0x72, 0x01};

    public static final byte[] TEXT_COLOR_REVERSE_OFF = new byte[]{0x1D, 0x42, 0x00};
    public static final byte[] TEXT_COLOR_REVERSE_ON = new byte[]{0x1D, 0x42, 0x01};


    public static final int BARCODE_TYPE_UPCA = 65;
    public static final int BARCODE_TYPE_UPCE = 66;
    public static final int BARCODE_TYPE_EAN13 = 67;
    public static final int BARCODE_TYPE_EAN8 = 68;
    public static final int BARCODE_TYPE_ITF = 70;
    public static final int BARCODE_TYPE_128 = 73;

    public static final int BARCODE_TEXT_POSITION_NONE = 0;
    public static final int BARCODE_TEXT_POSITION_ABOVE = 1;
    public static final int BARCODE_TEXT_POSITION_BELOW = 2;

    public static final int QRCODE_1 = 49;
    public static final int QRCODE_2 = 50;



    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 0x40;
        return result;
    }


    public static byte[] nextLine() {
        byte[] x = {LF};
        return x;
    }

    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    public static byte[] alignLeft() {
//        byte[] result = new byte[3];
//        result[0] = ESC;
//        result[1] = 97;
//        result[2] = 0;
//        return result;

        return TEXT_ALIGN_LEFT;
    }

    public static byte[] alignCenter() {
//        byte[] result = new byte[3];
//        result[0] = ESC;
//        result[1] = 97;
//        result[2] = 1;
//        return result;
        return TEXT_ALIGN_CENTER;
    }

    public static byte[] alignRight() {
//        byte[] result = new byte[3];
//        result[0] = ESC;
//        result[1] = 97;
//        result[2] = 2;
//        return result;
        return TEXT_ALIGN_RIGHT;
    }


    public static byte[] getFontNormal() {
        return EscPosBase.TEXT_SIZE_NORMAL;
    }

    public static byte[] getFontTall() {
        return EscPosBase.TEXT_SIZE_DOUBLE_HEIGHT;
    }
    public static byte[] getFontsmall() {
        return EscPosBase.TEXT_SIZE_SMALL;
    }

    public static byte[] getFontWBold() {
        return EscPosBase.TEXT_WEIGHT_BOLD;
    }

    public static byte[] getFontWNormal() {
        return EscPosBase.TEXT_WEIGHT_NORMAL;
    }

    public static byte[] getFontSize_2() {
        return EscPosBase.TEXT_SIZE_2;
    }

    public static byte[] getFontNormal2() {
        return EscPosBase.TEXT_SIZE_BIG;
    }

//    protected static byte[] getFontBig() {
//        return EscPosBase.TEXT_SIZE_BIG;
//    }
//
//    protected static byte[] getFontSmall() {
//        return EscPosBase.TEXT_SIZE_SMALL;
//    }
}
