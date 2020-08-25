package com.renj.imagepicker;

import com.renj.imagepicker.listener.ImagePickerViewModule;
import com.renj.imagepicker.listener.OnResultCallBack;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-07-24   16:41
 * <p>
 * 描述：辅助类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
class RImagePickerHelp {
    // private static volatile Stack<CallAndViewModule> callAndViewModuleStack = new Stack<>();
    private static CallAndViewModule callAndViewModule;

    private RImagePickerHelp() {
    }

    static ImagePickerViewModule getImagePickerViewModule() {
        // return callAndViewModuleStack.peek().imagePickerViewModule;
        return callAndViewModule.imagePickerViewModule;
    }

    static OnResultCallBack getOnResultCallBack() {
        // return callAndViewModuleStack.peek().onResultCallBack;
        return callAndViewModule.onResultCallBack;
    }

    static void addModule(OnResultCallBack onResultCallBack, ImagePickerViewModule imagePickerViewModule) {
        // callAndViewModuleStack.push(new CallAndViewModule(onResultCallBack, imagePickerViewModule));
        callAndViewModule = new CallAndViewModule(onResultCallBack, imagePickerViewModule);
    }

    static void removeModule() {
        // callAndViewModuleStack.pop();
    }
}
