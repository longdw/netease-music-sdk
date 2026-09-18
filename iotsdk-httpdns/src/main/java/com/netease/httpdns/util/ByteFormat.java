package com.netease.httpdns.util;

public class ByteFormat {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int ROW_BYTES = 16;
    private static final int ROW_QTR1 = 3;
    private static final int ROW_HALF = 7;
    private static final int ROW_QTR2 = 11;

    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);

        for(int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }

    public static String toHex(byte[] buf) {
        if (buf != null && buf.length != 0) {
            StringBuilder out = new StringBuilder();

            for(int i = 0; i < buf.length; ++i) {
                out.append(HEX[buf[i] >> 4 & 15]).append(HEX[buf[i] & 15]);
            }

            return out.toString();
        } else {
            return "";
        }
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else {
            char[] hex = str.toCharArray();
            int length = hex.length / 2;
            byte[] raw = new byte[length];

            for(int i = 0; i < length; ++i) {
                int high = Character.digit(hex[i * 2], 16);
                int low = Character.digit(hex[i * 2 + 1], 16);
                int value = high << 4 | low;
                if (value > 127) {
                    value -= 256;
                }

                raw[i] = (byte)value;
            }

            return raw;
        }
    }

    public static String dumpHex(byte[] buf) {
        return buf == null ? "" : dumpHex(buf, 0, buf.length);
    }

    private static String dumpHex(byte[] buf, int offset, int numBytes) {
        if (buf != null && buf.length != 0) {
            if (offset >= buf.length) {
                offset = buf.length - 1;
            }

            if (numBytes > buf.length - offset) {
                numBytes = buf.length - offset;
            }

            StringBuilder out = new StringBuilder();
            byte[] save_buf = new byte[18];
            char[] hex_buf = new char[4];
            char[] idx_buf = new char[8];
            int rows = numBytes >> 4;
            int residue = numBytes & 15;

            int i;
            for(i = 0; i < rows; ++i) {
                int hexVal = i * 16;
                idx_buf[0] = HEX[hexVal >> 12 & 15];
                idx_buf[1] = HEX[hexVal >> 8 & 15];
                idx_buf[2] = HEX[hexVal >> 4 & 15];
                idx_buf[3] = HEX[hexVal & 15];
                String idxStr = new String(idx_buf, 0, 4);
                out.append(idxStr).append(": ");

                int j;
                for(j = 0; j < 16; ++j) {
                    save_buf[j] = buf[offset + i * 16 + j];
                    hex_buf[0] = HEX[save_buf[j] >> 4 & 15];
                    hex_buf[1] = HEX[save_buf[j] & 15];
                    out.append(hex_buf[0]);
                    out.append(hex_buf[1]);
                    out.append(' ');
                    if (j == 3 || j == 7 || j == 11) {
                        out.append(' ');
                    }

                    if (save_buf[j] < 32 || save_buf[j] > 126) {
                        save_buf[j] = 46;
                    }
                }

                String saveStr = new String(save_buf, 0, j);
                out.append(" ; ").append(saveStr).append("\n");
            }

            if (residue > 0) {
                int hexVal = i * 16;
                idx_buf[0] = HEX[hexVal >> 12 & 15];
                idx_buf[1] = HEX[hexVal >> 8 & 15];
                idx_buf[2] = HEX[hexVal >> 4 & 15];
                idx_buf[3] = HEX[hexVal & 15];
                String idxStr = new String(idx_buf, 0, 4);
                out.append(idxStr).append(": ");

                int j;
                for(j = 0; j < residue; ++j) {
                    save_buf[j] = buf[offset + i * 16 + j];
                    hex_buf[0] = HEX[save_buf[j] >> 4 & 15];
                    hex_buf[1] = HEX[save_buf[j] & 15];
                    out.append(hex_buf[0]);
                    out.append(hex_buf[1]);
                    out.append(' ');
                    if (j == 3 || j == 7 || j == 11) {
                        out.append(' ');
                    }

                    if (save_buf[j] < 32 || save_buf[j] > 126) {
                        save_buf[j] = 46;
                    }
                }

                for(; j < 16; ++j) {
                    save_buf[j] = 32;
                    out.append("   ");
                    if (j == 3 || j == 7 || j == 11) {
                        out.append(" ");
                    }
                }

                String saveStr = new String(save_buf, 0, j);
                out.append(" ; ").append(saveStr).append("\n");
            }

            return out.toString();
        } else {
            return "";
        }
    }
}