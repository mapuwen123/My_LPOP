package com.mapuw.lpop.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 与保存相关的工具类
 * Created by mapuw on 16-7-5.
 * Usage:
 * 　　保存是个耗时操作，最好放在子线程执行，防止发生ANR
 * 默认图片保存目录是/sdcard/Android/data/com.wenming.weiswift.file/Pictures/weibo
 * 最好设置一下fileName
 * SaveImgUtil.create(context)
 * // 缺省
 * //.setOutputDirectory(directory)
 * //.setImageFormat(format)
 * //.setImageQuality(quality)
 * //.setFileName(name)
 * .saveImage(bitmap)
 */
public class SaveImgUtil {

    private static final String TAG = SaveImgUtil.class.getSimpleName();

    private static final int IMG_DEFAULT_QUALITY = 100;

    private static final String FOLDER_NAME = "WeiSwiftPic";

    /**
     * Save a file in internal storage or external storage
     */
    private File mOutputDirectory;
    /**
     * The format of the picture which will be saved
     */
    private Bitmap.CompressFormat mFormat;
    /**
     * The quality of the picture saved.
     */
    private int mQuality;
    /**
     * The name of the file saved
     */
    private String mFileName;

    private Context mContext;

    private static SaveImgUtil mSaveUtil;

    private SaveImgUtil(Context context) {
        this.mOutputDirectory = getDirectory(context, FOLDER_NAME);
        this.mFormat = Bitmap.CompressFormat.PNG;
        this.mContext = context;
        this.mQuality = IMG_DEFAULT_QUALITY;
        this.mFileName = System.currentTimeMillis() + ".png";
    }

    public static SaveImgUtil create(Context context) {
        if (mSaveUtil == null) {
            mSaveUtil = new SaveImgUtil(context);
        }
        return mSaveUtil;
    }

    /**
     * @param directory 文件保存路径
     *                  Internal Storage(only access by your own app)
     *                  context.getFilesDir()
     *                  context.getCacheDir()
     *                  External Storage(when use this, should first check isSDCardEnable)
     *                  Environment.getExternalStoragePublicDirectory
     *                  context.getExternalFilesDir
     * @return
     */
    public SaveImgUtil setOutputDirectory(File directory) {
        this.mOutputDirectory = directory;
        return this;
    }

    /**
     * @param format 　图片保存格式
     * @return
     */
    public SaveImgUtil setImageFormat(Bitmap.CompressFormat format) {
        this.mFormat = format;
        return this;
    }

    /**
     * @param quality 图片质量
     * @return
     */
    public SaveImgUtil setImageQuality(int quality) {
        this.mQuality = quality;
        return this;
    }

    /**
     * @param fileName 文件名
     * @return
     */
    public SaveImgUtil setFileName(String fileName) {
        this.mFileName = fileName;
        return this;
    }

    /**
     * @param bitmap 保存Bitmap
     */
    public File saveImage(File imgFile, Bitmap bitmap, String ImgName) {
        // 首先保存图片
//        File appDir = new File(Environment.getExternalStorageDirectory(), "weiSwiftImg");
        if (!imgFile.exists()) {
            imgFile.mkdir();
        }
        String fileName = ImgName;
        File file = new File(imgFile, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 最后通知图库更新
//        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imgFile)));
//        Toast.makeText(mContext, "保存成功！", Toast.LENGTH_SHORT).show();
        return file;
    }

    public File saveGif(File imgFile, File gif, String ImgName) {
        if (!imgFile.exists()) {
            imgFile.mkdir();
        }
        String fileName = ImgName;
        File file = new File(imgFile, fileName);
        try {
            int bytesum = 0;
            int byteread = 0;
            file.createNewFile();
            if (gif.exists()) {
                InputStream inStream = new FileInputStream(gif); //读入原文件
                FileOutputStream fs = new FileOutputStream(file);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }


    /**
     * 创建保存图片的目录
     *
     * @param context
     * @param folderName
     * @return
     */
    private File getDirectory(Context context, String folderName) {
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), folderName);
        if (!file.mkdirs()) {
            LogUtil.e(TAG, "Directory not created");
        }
        return file;
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ToastUtil.showShort(mContext, "保存成功");
        }
    };

}
