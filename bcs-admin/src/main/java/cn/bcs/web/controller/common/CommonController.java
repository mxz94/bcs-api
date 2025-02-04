package cn.bcs.web.controller.common;

import cn.bcs.common.config.RuoYiConfig;
import cn.bcs.common.constant.Constants;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.FileVO;
import cn.bcs.common.core.domain.model.FilesVO;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.common.utils.file.FileUploadUtils;
import cn.bcs.common.utils.file.FileUtils;
import cn.bcs.framework.config.SecurityConfig;
import cn.bcs.framework.config.ServerConfig;
import cn.bcs.system.service.SysUserService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final String FILE_DELIMETER = ",";
    @Resource
    private ServerConfig serverConfig;
    @Resource
    private SysUserService sysUserService;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public Result<FileVO> uploadFile(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            fileName = "/api"+ fileName;
            String url = serverConfig.getUrl() + fileName;
            FileVO fileVO = new FileVO().setUrl(url)
                    .setFileName(fileName)
                    .setNewFileName(FileUtils.getName(fileName))
                    .setOriginalFilename(file.getOriginalFilename());
            return Result.success(fileVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/uploadshoukuan")
    public Result<FileVO> uploadshoukuan(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getShoukuanPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            fileName = "/api" + fileName;
            String url = serverConfig.getUrl() + fileName;
            FileVO fileVO = new FileVO().setUrl(url)
                    .setFileName(fileName)
                    .setNewFileName(FileUtils.getName(fileName))
                    .setOriginalFilename(file.getOriginalFilename());
            sysUserService.lambdaUpdate().eq(SysUser::getUserId, SecurityUtils.getUserId())
                    .set(SysUser::getShoukuanUrl, url).update();
            return Result.success(fileVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public Result<FilesVO> uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            FilesVO vo = new FilesVO().setUrls(StrUtil.join(FILE_DELIMETER, urls))
                    .setFileNames(StrUtil.join(FILE_DELIMETER, fileNames))
                    .setNewFileNames(StrUtil.join(FILE_DELIMETER, newFileNames))
                    .setOriginalFilenames(StrUtil.join(FILE_DELIMETER, originalFilenames));
            return Result.success(vo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }
}
