package com.renj.imageselect.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;

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
public class LoadSDImageUtils {

    /**
     * 从sd卡中加载图片
     *
     * @param context                          上下文
     * @param loadImageForSdCardFinishListener 加载完成监听，回调在子线程中执行
     */
    public static void loadImageForSdCard(@NonNull Activity context, @NonNull LoadImageForSdCardFinishListener loadImageForSdCardFinishListener) {
        LoadImageForSdCard loadImageForSdCard = new LoadImageForSdCard(context, loadImageForSdCardFinishListener);
        Utils.runOnNewThread(loadImageForSdCard);
    }


    /**
     * 从SD卡中加载所有图片完成线程
     */
    public interface LoadImageForSdCardFinishListener {
        /**
         * 加载完成回调函数
         *
         * @param imageModels  所有图片集合
         * @param folderModels 将图片分类后的集合
         */
        void finish(List<ImageModel> imageModels, List<FolderModel> folderModels);
    }

    /**
     * 获取SD卡中的所有图片并按所在目录进行分类处理的线程
     */
    static class LoadImageForSdCard implements Runnable {
        private Context context;
        private LoadImageForSdCardFinishListener listener;

        public LoadImageForSdCard(@NonNull Context context, @NonNull LoadImageForSdCardFinishListener listener) {
            this.context = context;
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

            List<ImageModel> imageModels = new ArrayList<>();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                imageModels.add(new ImageModel(path, name, time));
            }
            cursor.close();

            List<FolderModel> folderModels = splitFolder(imageModels);

            if (listener != null)
                listener.finish(imageModels, folderModels);
        }

        /**
         * 将图片按照目录进行拆分
         *
         * @param imageModels
         * @return
         */
        private List<FolderModel> splitFolder(@NonNull List<ImageModel> imageModels) {
            List<FolderModel> folderModels = new ArrayList<>();
            if (imageModels == null || imageModels.size() <= 0) return folderModels;

            folderModels.add(new FolderModel("全部图片", imageModels));
            for (ImageModel imageModel : imageModels) {
                String name = getFolderNameByPath(imageModel.path);
                if (!TextUtils.isEmpty(name)) {
                    FolderModel folderModel = getFolderModelByName(name, folderModels);
                    folderModel.addImageModel(imageModel);
                }
            }
            return folderModels;
        }

        /**
         * 根据目录名称获取FolderModel对象
         *
         * @param name
         * @param folderModels
         * @return
         */
        private FolderModel getFolderModelByName(String name, List<FolderModel> folderModels) {
            if (folderModels != null && !folderModels.isEmpty()) {
                for (FolderModel folderModel : folderModels) {
                    if (name.equals(folderModel.name)) {
                        return folderModel;
                    }
                }
            }
            FolderModel folderModel = new FolderModel(name);
            folderModels.add(folderModel);
            return folderModel;
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
