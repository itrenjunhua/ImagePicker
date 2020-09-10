package com.renj.imagepicker.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.renj.imagepicker.model.ImagePickerFolderModel;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.ImagePickerParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-27   14:58
 * <p>
 * 描述：从SD卡中加载图片的工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RLoadSDImageUtils {

    /**
     * 从sd卡中加载图片
     *
     * @param context                          上下文
     * @param imagePickerParams
     * @param loadImageForSdCardFinishListener 加载完成监听，回调在子线程中执行
     */
    public static void loadImageForSdCard(@NonNull Activity context, ImagePickerParams imagePickerParams,
                                          @NonNull LoadImageForSdCardFinishListener loadImageForSdCardFinishListener) {
        LoadImageForSdCard loadImageForSdCard = new LoadImageForSdCard(context, imagePickerParams, loadImageForSdCardFinishListener);
        runOnNewThread(loadImageForSdCard);
    }

    private static void runOnNewThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }


    /**
     * 从SD卡中加载所有图片完成线程
     */
    public interface LoadImageForSdCardFinishListener {
        /**
         * 加载完成回调函数
         *
         * @param imagePickerModels       所有图片集合
         * @param imagePickerFolderModels 将图片分类后的集合
         */
        void finish(List<ImagePickerModel> imagePickerModels, List<ImagePickerFolderModel> imagePickerFolderModels);
    }

    /**
     * 获取SD卡中的所有图片并按所在目录进行分类处理的线程
     */
    static class LoadImageForSdCard implements Runnable {
        private Context context;
        private ImagePickerParams imagePickerParams;
        private LoadImageForSdCardFinishListener listener;

        public LoadImageForSdCard(@NonNull Context context, ImagePickerParams imagePickerParams,
                                  @NonNull LoadImageForSdCardFinishListener listener) {
            this.context = context;
            this.imagePickerParams = imagePickerParams;
            this.listener = listener;
        }

        @Override
        public void run() {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();
            Cursor cursor = mContentResolver.query(mImageUri, new String[]{
                            MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.DATE_ADDED,},
                    null,
                    null,
                    MediaStore.Images.Media.DATE_ADDED);

            if (cursor == null) return;

            List<ImagePickerModel> imagePickerModels = new ArrayList<>();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                imagePickerModels.add(new ImagePickerModel(path, name, time));
            }
            cursor.close();

            List<ImagePickerFolderModel> imagePickerFolderModels = splitFolder(imagePickerModels);

            if (listener != null)
                listener.finish(imagePickerModels, imagePickerFolderModels);
        }

        /**
         * 将图片按照目录进行拆分
         *
         * @param imagePickerModels
         * @return
         */
        private List<ImagePickerFolderModel> splitFolder(@NonNull List<ImagePickerModel> imagePickerModels) {
            List<ImagePickerFolderModel> imagePickerFolderModels = new ArrayList<>();
            if (imagePickerModels == null || imagePickerModels.size() <= 0)
                return imagePickerFolderModels;

            imagePickerFolderModels.add(new ImagePickerFolderModel("全部图片", imagePickerModels));
            List<String> fileSuffix = imagePickerParams.getFileSuffix();
            for (ImagePickerModel imagePickerModel : imagePickerModels) {
                String name = getFolderNameByPath(imagePickerModel.path);
                if (!TextUtils.isEmpty(name)) {
                    if (fileSuffix != null) {
                        for (String suffix : fileSuffix) {
                            if (name.endsWith(suffix)) {
                                ImagePickerFolderModel imagePickerFolderModel = getFolderModelByName(name, imagePickerFolderModels);
                                imagePickerFolderModel.addImageModel(imagePickerModel);
                                break;
                            }
                        }
                    } else {
                        ImagePickerFolderModel imagePickerFolderModel = getFolderModelByName(name, imagePickerFolderModels);
                        imagePickerFolderModel.addImageModel(imagePickerModel);
                    }
                }
            }
            return imagePickerFolderModels;
        }

        /**
         * 根据目录名称获取FolderModel对象
         *
         * @param name
         * @param imagePickerFolderModels
         * @return
         */
        private ImagePickerFolderModel getFolderModelByName(String name, List<ImagePickerFolderModel> imagePickerFolderModels) {
            if (imagePickerFolderModels != null && !imagePickerFolderModels.isEmpty()) {
                for (ImagePickerFolderModel imagePickerFolderModel : imagePickerFolderModels) {
                    if (name.equals(imagePickerFolderModel.name)) {
                        return imagePickerFolderModel;
                    }
                }
            }
            ImagePickerFolderModel imagePickerFolderModel = new ImagePickerFolderModel(name);
            imagePickerFolderModels.add(imagePickerFolderModel);
            return imagePickerFolderModel;
        }

        /**
         * 根据路径获取最后一个目录名称
         *
         * @param path
         * @return
         */
        private String getFolderNameByPath(String path) {
            if (!TextUtils.isEmpty(path)) {
                String[] strings = path.split(File.separator);
                if (strings != null && strings.length >= 2) {
                    return strings[strings.length - 2];
                }
            }
            return "";
        }
    }
}
