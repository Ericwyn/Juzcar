package com.ericwyn.juzcar.server.out;

import com.ericwyn.ezerver.util.FileUtils;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.server.config.JuzcarDocConfig;
import com.ericwyn.juzcar.server.tml.HttpTemple;
import com.ericwyn.juzcar.server.tml.TempleKey;
import com.ericwyn.juzcar.server.tml.TempleUtils;
import com.ericwyn.juzcar.utils.JuzcarLogs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 该工具用来导出文档，使得文档能保存到本地
 *
 * Created by Ericwyn on 19-3-5.
 */
public class OutputService {
    private static final String juzcarStaticDir = JuzcarDocConfig.JAR_STATIC_PATH;
    private static final String juzcarOutTempDir = JuzcarDocConfig.JUZCAR_OUT_TEMP_DIR;
    private static final File outTempDir = new File(juzcarOutTempDir);

    private static OutputService outputService;

    private OutputService(){

    }

    public static OutputService getService(){
        if (outputService == null){
            synchronized (OutputService.class) {
                if (outputService == null){
                    outputService = new OutputService();
                }
            }
        }
        return outputService;
    }

    /**
     * 获取 HTML 格式的导出文档的压缩包
     * @return
     */
    public File getOutputHTMLZip(Map<String, JuzcarApiList> apiMaps){
        if (!outTempDir.isDirectory()) {
            outTempDir.mkdirs();
        }
        // 创建临时文件夹
        String outPutName = "" + System.currentTimeMillis() + "_" + ((int)(Math.random() * 100));
        File tempDir = new File(juzcarOutTempDir+"/OutDoc_" + outPutName);
        // 复制进去
        FileUtils.copyDir(juzcarStaticDir, tempDir.getAbsolutePath());
        // 生成 ApiController 页面到临时文件夹当中
        generalOutApiPage(apiMaps, tempDir);
        // 生成 index 页面到临时文件夹
        generalOutIndexPage(apiMaps, tempDir);

        String outPutZipName = outPutName+".zip";
        File outPutZipFile = new File(juzcarOutTempDir + "/" + outPutZipName);

        try {
            ZipUtils.compress(tempDir.getAbsolutePath(), outPutZipFile.getAbsolutePath());
            if (outPutZipFile.isFile() && outPutZipFile.length() > 0){
                // 删除文件夹
                FileUtils.deleteDir(tempDir.getAbsolutePath());
                return outPutZipFile;
            } else {
                JuzcarLogs.SOUT(" HTML 文档导出成 zip 失败");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            JuzcarLogs.SOUT(" HTML 文档导出成 zip 失败, 异常" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过一个 apiMaps 创建多个 HTML 文件到指定文件夹当中
     * @param apis
     * @param saveDir
     * @return
     */
    private List<File> generalOutApiPage(Map<String, JuzcarApiList> apis, File saveDir){
        HttpTemple apiPageTemple = new HttpTemple(TempleUtils.getTemple("controller"));
        List<File> resList = new ArrayList<>();
        // 将 Controller 一个个变成 html 页面
        for (String key : apis.keySet()){
            String fileName = TempleUtils.getUriFromApiKey(key)+".html";
            resList.add(FileUtils.writeToFile(new File(saveDir, fileName), apiPageTemple.clearReplace()
                                                .replace(TempleKey.API_Nav, TempleUtils.getNavTemple(apis))
                                                .replace(TempleKey.API_PackgeName, key)
                                                .replace(TempleKey.API_ControllerNote, apis.get(key).getClazz().getNote())
                                                .replace(TempleKey.API_ApiList, TempleUtils.getApiListTemple(apis.get(key)))
                                                .string()
                                            )
            );
        }
        return resList;
    }

    /**
     * 通过一个 apiMap 生成文档的 index 页到指定文件夹当中
     * @param apis
     * @param saveDir
     * @return
     */
    private File generalOutIndexPage(Map<String, JuzcarApiList> apis, File saveDir){
        HttpTemple temple = new HttpTemple(TempleUtils.getTemple("index"));
        File outIndexPage = new File(saveDir, "index.html");
        FileUtils.writeToFile(
                outIndexPage,
                temple.replace(TempleKey.INDEX_README, "欢迎使用 Juzcar ，一个简单、快捷的 Web API 文档自动生成工具。")
                        .replace(TempleKey.INDEX_Nav, TempleUtils.getNavTemple(apis))
                        .string()
        );
        return outIndexPage;
    }

}
