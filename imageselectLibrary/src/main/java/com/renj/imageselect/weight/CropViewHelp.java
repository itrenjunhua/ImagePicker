package com.renj.imageselect.weight;

import android.graphics.RectF;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-07-28   16:37
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CropViewHelp {
    public static final int CROP_VIEW_MIN_SIZE = 30;
    public static final int CROP_VIEW_EVENT_OFFSET = 15;
    public static final int CROP_VIEW_EVENT_AREA_CENTER = 0;
    public static final int CROP_VIEW_EVENT_AREA_LEFT = 1;
    public static final int CROP_VIEW_EVENT_AREA_TOP = 2;
    public static final int CROP_VIEW_EVENT_AREA_RIGHT = 3;
    public static final int CROP_VIEW_EVENT_AREA_BOTTOM = 4;
    public static final int CROP_VIEW_EVENT_AREA_LEFT_TOP = 5;
    public static final int CROP_VIEW_EVENT_AREA_RIGHT_TOP = 6;
    public static final int CROP_VIEW_EVENT_AREA_RIGHT_BOTTOM = 7;
    public static final int CROP_VIEW_EVENT_AREA_LEFT_BOTTOM = 8;
    public static final int CROP_VIEW_EVENT_AREA_OTHER = -1;

    /**
     * 根据按下位置和裁剪区域判断按下区域,用来判断移动时的操作
     *
     * @param downX 按下位置 x
     * @param downY 按下位置 y
     * @param rectF 裁剪区域
     * @return
     */
    public static int getEventAreaByDown(int downX, int downY, RectF rectF) {
        int eventArea = CROP_VIEW_EVENT_AREA_OTHER;
        if ((downX >= rectF.left - CROP_VIEW_EVENT_OFFSET && downX <= rectF.left + CROP_VIEW_EVENT_OFFSET)
                && (downY >= rectF.top - CROP_VIEW_EVENT_OFFSET && downY <= rectF.top + CROP_VIEW_EVENT_OFFSET)) {
            eventArea = CROP_VIEW_EVENT_AREA_LEFT_TOP;
        } else if ((downX >= rectF.right - CROP_VIEW_EVENT_OFFSET && downX <= rectF.right + CROP_VIEW_EVENT_OFFSET)
                && (downY >= rectF.top - CROP_VIEW_EVENT_OFFSET && downY <= rectF.top + CROP_VIEW_EVENT_OFFSET)) {
            eventArea = CROP_VIEW_EVENT_AREA_RIGHT_TOP;
        } else if ((downX >= rectF.right - CROP_VIEW_EVENT_OFFSET && downX <= rectF.right + CROP_VIEW_EVENT_OFFSET)
                && (downY >= rectF.bottom - CROP_VIEW_EVENT_OFFSET && downY <= rectF.bottom + CROP_VIEW_EVENT_OFFSET)) {
            eventArea = CROP_VIEW_EVENT_AREA_RIGHT_BOTTOM;
        } else if ((downX >= rectF.left - CROP_VIEW_EVENT_OFFSET && downX <= rectF.left + CROP_VIEW_EVENT_OFFSET)
                && (downY >= rectF.bottom - CROP_VIEW_EVENT_OFFSET && downY <= rectF.bottom + CROP_VIEW_EVENT_OFFSET)) {
            eventArea = CROP_VIEW_EVENT_AREA_LEFT_BOTTOM;
        } else if (downX >= rectF.left - CROP_VIEW_EVENT_OFFSET && downX <= rectF.left + CROP_VIEW_EVENT_OFFSET) {
            eventArea = CROP_VIEW_EVENT_AREA_LEFT;
        } else if (downY >= rectF.top - CROP_VIEW_EVENT_OFFSET && downY <= rectF.top + CROP_VIEW_EVENT_OFFSET) {
            eventArea = CROP_VIEW_EVENT_AREA_TOP;
        } else if (downX >= rectF.right - CROP_VIEW_EVENT_OFFSET && downX <= rectF.right + CROP_VIEW_EVENT_OFFSET) {
            eventArea = CROP_VIEW_EVENT_AREA_RIGHT;
        } else if (downY >= rectF.bottom - CROP_VIEW_EVENT_OFFSET && downY <= rectF.bottom + CROP_VIEW_EVENT_OFFSET) {
            eventArea = CROP_VIEW_EVENT_AREA_BOTTOM;
        } else if (rectF.contains(downX, downY)) {
            eventArea = CROP_VIEW_EVENT_AREA_CENTER;
        }
        return eventArea;
    }

    /**
     * 移动裁剪区域时,对边界进行判断
     *
     * @param offsetX x轴方向移动距离
     * @param offsetY y轴方向移动距离
     * @param vWidth  控件宽
     * @param vHeight 控件高
     * @param rectF   裁剪区域
     */
    public static void moveBorderHandler(float offsetX, float offsetY, float vWidth, float vHeight, RectF rectF) {
        if (rectF.left + offsetX < 0) {
            offsetX = 0 - rectF.left;
        } else if (rectF.right + offsetX > vWidth) {
            offsetX = vWidth - rectF.right;
        }

        if (rectF.top + offsetY < 0) {
            offsetY = 0 - rectF.top;
        } else if (rectF.bottom + offsetY > vHeight) {
            offsetY = vHeight - rectF.bottom;
        }
        rectF.offset(offsetX, offsetY);
    }

    /**
     * 移动过程中计算裁剪区域(缩放区域)
     *
     * @param eventArea      移动时间类型
     * @param cropArea       裁剪区域
     * @param calculateRectF 计算区域
     * @param vWidth         控件宽
     * @param vHeight        控件高
     * @param offsetX        x轴方向移动距离
     * @param offsetY        y轴方向移动距离
     * @param isRatioScale   是否等比例缩放
     * @param widthRatio     宽比例
     * @param heightRadio    高比例
     * @return 是否需要重新绘制 true: 重绘 false: 不需要重绘
     */
    public static boolean calculateCropArea(int eventArea, RectF cropArea, RectF calculateRectF, float vWidth, float vHeight,
                                            float offsetX, float offsetY, boolean isRatioScale, int widthRatio, int heightRadio) {
        if (eventArea == CROP_VIEW_EVENT_AREA_LEFT) {
            // 左边线：移动左边
            float tmpLeft = calculateRectF.left + offsetX;
            if (tmpLeft < 0) tmpLeft = 0;
            if (tmpLeft > vWidth) tmpLeft = vWidth;

            if (cropArea.right - tmpLeft >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.left = tmpLeft;
                if (isRatioScale) {
                    // 如果需要等比例，计算上边；上边不够，用下边接着增加
                    if (tmpLeft != 0 && tmpLeft != vWidth) {
                        float tmpTop = calculateRectF.top + offsetX * (heightRadio * 1.0f / widthRatio);
                        if (tmpTop < 0) {
                            calculateRectF.top = 0;
                            calculateRectF.bottom = calculateRectF.bottom - offsetX * (heightRadio * 1.0f / widthRatio);
                        } else {
                            calculateRectF.top = tmpTop;
                        }
                    }
                }
            } else {
                calculateRectF.left = cropArea.right - CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.top < 0 || calculateRectF.top > vHeight || calculateRectF.bottom < 0 || calculateRectF.bottom > vHeight) {
                return false;
            }
        } else if (eventArea == CROP_VIEW_EVENT_AREA_TOP) {
            // 上边线：移动上边
            float tmpTop = calculateRectF.top + offsetY;
            if (tmpTop < 0) tmpTop = 0;
            if (tmpTop > vHeight) tmpTop = vHeight;

            if (cropArea.bottom - tmpTop >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.top = tmpTop;
                if (isRatioScale) {
                    // 如果需要等比例，计算左边；左边不够，用右边接着增加
                    if (tmpTop != 0 && tmpTop != vHeight) {
                        float tmpLeft = calculateRectF.left + offsetY * (widthRatio * 1.0f / heightRadio);
                        if (tmpLeft < 0) {
                            calculateRectF.left = 0;
                            calculateRectF.right = calculateRectF.right - offsetY * (widthRatio * 1.0f / heightRadio);
                        } else {
                            calculateRectF.left = tmpLeft;
                        }
                    }
                }
            } else {
                calculateRectF.top = cropArea.bottom - CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.left < 0 || calculateRectF.left > vWidth || calculateRectF.right < 0 || calculateRectF.right > vWidth) {
                return false;
            }
        } else if (eventArea == CROP_VIEW_EVENT_AREA_RIGHT) {
            // 右边线：移动右边
            float tmpRight = calculateRectF.right + offsetX;
            if (tmpRight < 0) tmpRight = 0;
            if (tmpRight > vWidth) tmpRight = vWidth;

            if (tmpRight - cropArea.left >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.right = tmpRight;
                if (isRatioScale) {
                    // 如果需要等比例，计算下边；下边不够，用上边接着增加
                    if (tmpRight != 0 && tmpRight != vWidth) {
                        float tmpBottom = calculateRectF.bottom + offsetX * (heightRadio * 1.0f / widthRatio);
                        if (tmpBottom > vHeight) {
                            calculateRectF.bottom = vHeight;
                            calculateRectF.top = calculateRectF.top - offsetX * (heightRadio * 1.0f / widthRatio);
                        } else {
                            calculateRectF.bottom = tmpBottom;
                        }
                    }
                }
            } else {
                calculateRectF.right = cropArea.left + CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.top < 0 || calculateRectF.top > vHeight || calculateRectF.bottom < 0 || calculateRectF.bottom > vHeight) {
                return false;
            }

        } else if (eventArea == CROP_VIEW_EVENT_AREA_BOTTOM) {
            // 下边线：移动下边
            float tmpBottom = calculateRectF.bottom + offsetY;
            if (tmpBottom < 0) tmpBottom = 0;
            if (tmpBottom > vHeight) tmpBottom = vHeight;

            if (tmpBottom - cropArea.top >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.bottom = tmpBottom;
                if (isRatioScale) {
                    // 如果需要等比例，计算右边；右边不够，用左边接着增加
                    if (tmpBottom != 0 && tmpBottom != vHeight) {
                        float tmpRight = calculateRectF.right + offsetY * (widthRatio * 1.0f / heightRadio);
                        if (tmpRight > vWidth) {
                            calculateRectF.right = vWidth;
                            calculateRectF.left = calculateRectF.left - offsetY * (widthRatio * 1.0f / heightRadio);
                        } else {
                            calculateRectF.right = tmpRight;
                        }
                    }
                }
            } else {
                calculateRectF.bottom = cropArea.top + CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.left < 0 || calculateRectF.left > vWidth || calculateRectF.right < 0 || calculateRectF.right > vWidth) {
                return false;
            }
        } else if (eventArea == CROP_VIEW_EVENT_AREA_LEFT_TOP) {
            // 移动位置为左上角：移动左边和上边，如果需要等比例，以x轴位移为准，自动上边，上边不够，下边接着移动
            float tmpLeft = calculateRectF.left + offsetX;
            if (tmpLeft < 0) tmpLeft = 0;
            if (tmpLeft > vWidth) tmpLeft = vWidth;

            if (cropArea.right - tmpLeft >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.left = tmpLeft;
                if (isRatioScale) {
                    // 如果需要等比例，以x轴位移为准，自动上边，上边不够，下边接着移动
                    if (tmpLeft != 0 && tmpLeft != vWidth) {
                        float tmpTop = calculateRectF.top + offsetX * (heightRadio * 1.0f / widthRatio);
                        if (tmpTop < 0) {
                            calculateRectF.top = 0;
                            calculateRectF.bottom = calculateRectF.bottom - offsetX * (heightRadio * 1.0f / widthRatio);
                        } else {
                            calculateRectF.top = tmpTop;
                        }
                    }
                }
            } else {
                calculateRectF.left = cropArea.right - CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.top < 0 || calculateRectF.top > vHeight || calculateRectF.bottom < 0 || calculateRectF.bottom > vHeight) {
                return false;
            }

            // 不需要等比例，上下方向得移动距离为 offsetY
            if (!isRatioScale) {
                float tmpTop = calculateRectF.top + offsetY;
                if (tmpTop < 0) tmpTop = 0;
                if (tmpTop > vHeight) tmpTop = vHeight;

                if (cropArea.bottom - tmpTop >= CROP_VIEW_MIN_SIZE) {
                    calculateRectF.top = tmpTop;
                } else {
                    calculateRectF.top = cropArea.bottom - CROP_VIEW_MIN_SIZE;
                }

                // 边界判断
                if (calculateRectF.left < 0 || calculateRectF.left > vWidth || calculateRectF.right < 0 || calculateRectF.right > vWidth) {
                    return false;
                }
            }
        } else if (eventArea == CROP_VIEW_EVENT_AREA_RIGHT_TOP) {
            // 移动位置为右上角：移动右边和上边，如果需要等比例，以y轴位移为准，自动上边，上边不够，下边接着移动
            float tmpRight = calculateRectF.right + offsetX;
            if (tmpRight < 0) tmpRight = 0;
            if (tmpRight > vWidth) tmpRight = vWidth;

            if (tmpRight - cropArea.left >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.right = tmpRight;
                if (isRatioScale) {
                    // 如果需要等比例，以y轴位移为准，自动上边，上边不够，下边接着移动
                    if (tmpRight != 0 && tmpRight != vWidth) {
                        float tmpTop = calculateRectF.top - offsetX * (heightRadio * 1.0f / widthRatio);
                        if (tmpTop < 0) {
                            calculateRectF.top = 0;
                            calculateRectF.bottom = calculateRectF.bottom + offsetX * (heightRadio * 1.0f / widthRatio);
                        } else {
                            calculateRectF.top = tmpTop;
                        }
                    }
                }
            } else {
                calculateRectF.right = cropArea.left + CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.top < 0 || calculateRectF.top > vHeight || calculateRectF.bottom < 0 || calculateRectF.bottom > vHeight) {
                return false;
            }

            // 不需要等比例，上下方向得移动距离为 offsetY
            if (!isRatioScale) {
                float tmpTop = calculateRectF.top + offsetY;
                if (tmpTop < 0) tmpTop = 0;
                if (tmpTop > vHeight) tmpTop = vHeight;

                if (cropArea.bottom - tmpTop >= CROP_VIEW_MIN_SIZE) {
                    calculateRectF.top = tmpTop;
                } else {
                    calculateRectF.top = cropArea.bottom - CROP_VIEW_MIN_SIZE;
                }

                // 边界判断
                if (calculateRectF.left < 0 || calculateRectF.left > vWidth || calculateRectF.right < 0 || calculateRectF.right > vWidth) {
                    return false;
                }
            }
        } else if (eventArea == CROP_VIEW_EVENT_AREA_RIGHT_BOTTOM) {
            // 移动位置为右下角：移动右边和下边，如果需要等比例，以y轴位移为准，自动下边，下边不够，上边接着移动
            float tmpRight = calculateRectF.right + offsetX;
            if (tmpRight < 0) tmpRight = 0;
            if (tmpRight > vWidth) tmpRight = vWidth;

            if (tmpRight - cropArea.left >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.right = tmpRight;
                if (isRatioScale) {
                    // 如果需要等比例，以y轴位移为准，自动下边，下边不够，上边接着移动
                    if (tmpRight != 0 && tmpRight != vWidth) {
                        float tmpBottom = calculateRectF.bottom + offsetX * (heightRadio * 1.0f / widthRatio);
                        if (tmpBottom > vHeight) {
                            calculateRectF.bottom = vHeight;
                            calculateRectF.top = calculateRectF.top - offsetX * (heightRadio * 1.0f / widthRatio);
                        } else {
                            calculateRectF.bottom = tmpBottom;
                        }
                    }
                }
            } else {
                calculateRectF.right = cropArea.left + CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.top < 0 || calculateRectF.top > vHeight || calculateRectF.bottom < 0 || calculateRectF.bottom > vHeight) {
                return false;
            }

            // 不需要等比例，上下方向得移动距离为 offsetY
            if (!isRatioScale) {
                float tmpBottom = calculateRectF.bottom + offsetY;
                if (tmpBottom < 0) tmpBottom = 0;
                if (tmpBottom > vHeight) tmpBottom = vHeight;

                if (tmpBottom - cropArea.top >= CROP_VIEW_MIN_SIZE) {
                    calculateRectF.bottom = tmpBottom;
                } else {
                    calculateRectF.bottom = cropArea.top + CROP_VIEW_MIN_SIZE;
                }

                // 边界判断
                if (calculateRectF.left < 0 || calculateRectF.left > vWidth || calculateRectF.right < 0 || calculateRectF.right > vWidth) {
                    return false;
                }
            }
        } else if (eventArea == CROP_VIEW_EVENT_AREA_LEFT_BOTTOM) {
            // 移动位置为左下角：移动左边和下边，如果需要等比例，以x轴位移为准，自动下边，下边不够，上边接着移动
            float tmpLeft = calculateRectF.left + offsetX;
            if (tmpLeft < 0) tmpLeft = 0;
            if (tmpLeft > vWidth) tmpLeft = vWidth;

            if (cropArea.right - tmpLeft >= CROP_VIEW_MIN_SIZE) {
                calculateRectF.left = tmpLeft;
                if (isRatioScale) {
                    // 如果需要等比例，以x轴位移为准，自动下边，下边不够，上边接着移动
                    if (tmpLeft != 0 && tmpLeft != vWidth) {
                        float tmpBottom = calculateRectF.bottom - offsetX * (heightRadio * 1.0f / widthRatio);
                        if (tmpBottom > vHeight) {
                            calculateRectF.bottom = vHeight;
                            calculateRectF.top = calculateRectF.top + offsetX * (heightRadio * 1.0f / widthRatio);
                        } else {
                            calculateRectF.bottom = tmpBottom;
                        }
                    }
                }
            } else {
                calculateRectF.left = cropArea.right - CROP_VIEW_MIN_SIZE;
            }

            // 边界判断
            if (calculateRectF.top < 0 || calculateRectF.top > vHeight || calculateRectF.bottom < 0 || calculateRectF.bottom > vHeight) {
                return false;
            }

            // 不需要等比例，上下方向得移动距离为 offsetY
            if (!isRatioScale) {
                float tmpBottom = calculateRectF.bottom + offsetY;
                if (tmpBottom < 0) tmpBottom = 0;
                if (tmpBottom > vHeight) tmpBottom = vHeight;

                if (tmpBottom - cropArea.top >= CROP_VIEW_MIN_SIZE) {
                    calculateRectF.bottom = tmpBottom;
                } else {
                    calculateRectF.bottom = cropArea.top + CROP_VIEW_MIN_SIZE;
                }

                // 边界判断
                if (calculateRectF.left < 0 || calculateRectF.left > vWidth || calculateRectF.right < 0 || calculateRectF.right > vWidth) {
                    return false;
                }
            }
        }
        return true;
    }
}
