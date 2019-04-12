package com.rds.code.utils.file;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * @author lys
 * @className RdsFileUtil
 * @description 文件处理工具类
 * @date 2015/4/10
 */
public class RdsFileUtil {
	/**
	 * 把一个目录打包到一个指定的zip文件中
	 * 
	 * @param zipFilename
	 *            打包后zip文件绝对路径
	 * @param dirpath
	 *            待打包文件绝对路径
	 * @param pathName
	 *            打包后zip文件内部压缩路径
	 */
	public static void packToolFiles(String zipFilename, String dirpath,
			String pathName) throws FileNotFoundException, IOException {
		File f = new File(zipFilename);
		ByteArrayOutputStream tempbaos = null;
		BufferedOutputStream tempbos = null;
		ZipArchiveOutputStream zaos = null;
		try {
			zaos = new ZipArchiveOutputStream(new BufferedOutputStream(
					new FileOutputStream(f)));
			zaos.setEncoding("GBK");
			tempbaos = new ByteArrayOutputStream();
			tempbos = new BufferedOutputStream(tempbaos);

			File dir = new File(dirpath);
			// 返回此绝对路径下的文件
			File[] files = dir.listFiles();
			if (files == null || files.length < 1) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				// 判断此文件是否是一个文件夹
				if (files[i].isDirectory()) {
					packToolFiles(zipFilename, files[i].getAbsolutePath(),
							pathName + files[i].getName() + File.separator);
				} else {
					zaos.putArchiveEntry(new ZipArchiveEntry(pathName
							+ files[i].getName()));
					IOUtils.copy(
							new FileInputStream(files[i].getAbsolutePath()),
							zaos);
					zaos.closeArchiveEntry();

				}

			}
		} finally {
			tempbaos.flush();
			tempbaos.close();

			tempbos.flush();
			tempbos.close();

			zaos.flush();
			zaos.close();
		}
	}

	/**
	 * 把一个zip文件解压到一个指定的目录中
	 * 
	 * @param zipfilename
	 *            zip文件地址
	 * @param outputdir
	 *            解压后目录路径
	 */
	public static void unZipToFolder(String zipfilename, String outputdir)
			throws IOException {
		File zipfile = new File(zipfilename);
		if (zipfile.exists()) {
			outputdir = outputdir + File.separator;
			FileUtils.forceMkdir(new File(outputdir));

			ZipFile zf = new ZipFile(zipfile, "GBK");
			Enumeration zipArchiveEntrys = zf.getEntries();
			while (zipArchiveEntrys.hasMoreElements()) {
				ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) zipArchiveEntrys
						.nextElement();
				if (zipArchiveEntry.isDirectory()) {
					FileUtils.forceMkdir(new File(outputdir
							+ zipArchiveEntry.getName() + File.separator));
				} else {
					IOUtils.copy(
							zf.getInputStream(zipArchiveEntry),
							FileUtils.openOutputStream(new File(outputdir
									+ zipArchiveEntry.getName())));
				}
			}
		} else {
			throw new IOException("指定的解压文件不存在：\t" + zipfilename);
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param path
	 *            上传路径
	 * @param is
	 *            输入流
	 * @throws Exception
	 */
	public static void fileUpload(String path, InputStream is)
			throws IOException {
		FileUtils.copyInputStreamToFile(is, new File(path));
	}

	/**
	 * 判断指定路径文件或目录是否存在
	 *
	 *
	 *
	 *
	 * @param strPath
	 *            文件全路径（含文件名）/目录全路径
	 *
	 *
	 *
	 * @return boolean
	 */
	public static boolean getState(String strPath) {
		boolean blnResult = false;
		File file = null;
		try {
			file = new File(strPath);
			if (file.exists() || file.isFile()) {
				blnResult = true;
			}
		} catch (Exception e) {
			System.out.println("类:FileUtil,方法:getState,信息:路径有误，" + strPath);
		} finally {
			file = null;
		}
		// 释放对象
		strPath = null;
		return blnResult;
	}

	/**
	 * 读取文件到字符串中
	 *
	 *
	 *
	 *
	 * @param strFilePath
	 *            文件全路径(含文件名)
	 * @param strCoding
	 *            编码格式
	 * @return String
	 */
	public static String fileToString(String strFilePath, String strCoding) {
		StringBuffer strBuffResult = new StringBuffer();
		int i = 0;
		if (strCoding == null || strCoding.trim().length() <= 0) {
			strCoding = "UTF-8";
		}
		BufferedReader bufferedReader = null;
		try {
			if (strCoding == null || strCoding.trim().length() <= 0) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(strFilePath)));
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(strFilePath), strCoding));
			}
			while ((i = bufferedReader.read()) != -1) {
				strBuffResult.append((char) i);
			}
			bufferedReader.close();
		} catch (Exception ex) {
			System.out.println("类:FileUtil,方法:fileToString,信息:" + ex);
		} finally {
			bufferedReader = null;
		}
		// 释放对象
		strCoding = null;
		strFilePath = null;
		return strBuffResult.toString();
	}

	/**
	 * 读取文件 流中
	 *
	 *
	 * @param strFilePath
	 *            文件全路径(含文件名)
	 * @return String
	 */
	public static FileInputStream fileToInputStream(String strFilePath) {
		FileInputStream inputStream = null;

		try {
			if (RdsFileUtil.getState(strFilePath)) {
				inputStream = new FileInputStream(strFilePath);
			}
		} catch (Exception ex) {
			System.out.println("类:FileUtil,方法:fileToStream,信息:" + ex);
		}
		return inputStream;
	}

	/**
	 *
	 *
	 * @param strFilePath
	 *            文件全路径(含文件名)
	 * @return String
	 */
	public static FileOutputStream fileToOutputStream(String strFilePath) {
		FileOutputStream outputStream = null;

		try {
			if (RdsFileUtil.getState(strFilePath)) {
				outputStream = new FileOutputStream(strFilePath);
			}
		} catch (Exception ex) {
			System.out.println("类:FileUtil,方法:fileToOutputStream,信息:" + ex);
		}
		return outputStream;
	}

	/**
	 * 将字符串写入到文件中
	 *
	 * @param strContent
	 *            字符串内容
	 *
	 *
	 *
	 * @param strFilePath
	 *            文件全路径(含文件名)
	 * @param strCoding
	 *            编码格式,默认：UTF-8
	 * @return boolean
	 */
	public static boolean stringToFile(String strContent, String strFilePath,
			String strCoding) {
		boolean blnResult = false;
		if (strCoding == null || strCoding.trim().length() <= 0) {
			strCoding = "UTF-8";
		}
		FileOutputStream fileOutputStream = null; // 文件输出对象
		Writer writer = null;
		try {
			fileOutputStream = new FileOutputStream(strFilePath);
			if (strCoding == null || strCoding.trim().length() <= 0) {
				writer = new OutputStreamWriter(fileOutputStream);
			} else {
				writer = new OutputStreamWriter(fileOutputStream, strCoding);
			}
			writer.write(strContent);
			writer.flush();
			writer.close();
			fileOutputStream.close();
			blnResult = true;
		} catch (Exception ex) {
			System.out.println("类:FileUtil；方法:stringToFile；信息:" + ex);
		} finally {
			writer = null;
			fileOutputStream = null;
		}
		// 释放对象
		strCoding = null;
		strContent = null;
		strFilePath = null;
		return blnResult;
	}

	/**
	 * 新建目录
	 *
	 * @param strFolderPath
	 *            目录路径（含要创建的目录名称）
	 *
	 *
	 *
	 * @return boolean
	 */
	public static boolean createFolder(String strFolderPath) {
		boolean blnResult = true;
		File file = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			try {
				file = new File(strFolderPath);
				if (!file.exists()) {
					file.mkdir();
				}
			} catch (Exception e) {
				System.out.println("类:FileUtil,方法:createFolder,信息:创建目录操作出错,"
						+ strFolderPath);
				blnResult = false;
			} finally {
				file = null;
			}
		}
		// 释放对象
		strFolderPath = null;
		return blnResult;
	}

	/**
	 * 复制整个文件夹的内容
	 *
	 * @param strOldFolderPath
	 *            准备拷贝的目录
	 *
	 *
	 *
	 * @param strNewFolderPath
	 *            指定绝对路径的新目录
	 * @return void
	 */
	public static void copyFolder(String strOldFolderPath,
			String strNewFolderPath) {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		File file = null;
		String[] strArrayFile = null;
		File fileTemp = null;
		byte[] byteArray = null;
		int intIndex = 0;
		try {
			new File(strNewFolderPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹

			file = new File(strOldFolderPath);
			strArrayFile = file.list();
			for (int i = 0; i < strArrayFile.length; i++) {
				if (strOldFolderPath.endsWith(File.separator)) {
					fileTemp = new File(strOldFolderPath + strArrayFile[i]);
				} else {
					fileTemp = new File(strOldFolderPath + File.separator
							+ strArrayFile[i]);
				}
				if (fileTemp.isFile() && (!fileTemp.isHidden())) {
					fileInputStream = new FileInputStream(fileTemp);
					fileOutputStream = new FileOutputStream(strNewFolderPath
							+ "/" + (fileTemp.getName()).toString());
					byteArray = new byte[1024 * 5];
					while ((intIndex = fileInputStream.read(byteArray)) != -1) {
						fileOutputStream.write(byteArray, 0, intIndex);
					}
					fileOutputStream.flush();
					fileOutputStream.close();
					fileInputStream.close();
					intIndex = 0;
				}
				if (fileTemp.isDirectory() && (!fileTemp.isHidden())) {// 如果是子文件夹

					copyFolder(strOldFolderPath + File.separator
							+ strArrayFile[i], strNewFolderPath
							+ File.separator + strArrayFile[i]);
				}
			}
		} catch (Exception e) {
			System.out
					.println("类:FileUtil,方法:copyFolder,信息:复制整个文件夹内容操作出错," + e);
		} finally {
			fileInputStream = null;
			fileOutputStream = null;
			file = null;
			fileTemp = null;
			byteArray = null;
		}
		// 释放对象
		strArrayFile = null;
		strNewFolderPath = null;
		strOldFolderPath = null;
	}

	/**
	 * 复制单个文件
	 *
	 * @param strOldFilePath
	 *            准备复制的文件源
	 * @param strNewFilePath
	 *            拷贝到新绝对路径带文件名
	 * @return boolean
	 */
	public static boolean copyFile(String strOldFilePath, String strNewFilePath) {
		boolean blnResult = false;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		File file = null;
		byte[] byteArray = null;
		int intIndex = 0;
		try {
			fileInputStream = new FileInputStream(strOldFilePath);
			file = new File(strNewFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			byteArray = new byte[1024];
			while ((intIndex = fileInputStream.read(byteArray)) != -1) {
				for (int i = 0; i < intIndex; i++)
					fileOutputStream.write(byteArray[i]);
			}
			intIndex = 0;
			fileInputStream.close();
			fileOutputStream.close();
			blnResult = true;
		} catch (Exception e) {
			System.out.println("类:FileUtil,方法:copyFile,信息:被拷贝文件不存在!" + e);
		} finally {
			fileInputStream = null;
			fileOutputStream = null;
			file = null;
			byteArray = null;
		}
		// 释放对象
		strNewFilePath = null;
		strOldFilePath = null;
		return blnResult;
	}

	/**
	 * 删除文件
	 *
	 * @param strFilePath
	 *            文件全路径（含文件名）
	 *
	 *
	 *
	 * @return boolean
	 */
	public static boolean delFile(String strFilePath) {
		boolean blnResult = false;
		File file = null;
		if (strFilePath != null && strFilePath.trim().length() > 0) {
			try {
				file = new File(strFilePath);
				if (file.exists()) {
					file.delete();
					blnResult = true;
				} else {
					System.out.println("类:FileUtil,方法:delFile,信息:被文件不存在,"
							+ strFilePath);
				}
			} catch (Exception e) {
				System.out.println("类:FileUtil,方法:delFile,信息:删除文件有误," + e);
			} finally {
				file = null;
			}
		} else {
			System.out.println("类:FileUtil,方法:delFile,"
					+ "信息:strFilePath = null.");
		}
		// 释放对象
		strFilePath = null;
		return blnResult;
	}

	/**
	 * 删除文件夹
	 *
	 *
	 *
	 *
	 * @param strFolderPath
	 *            文件夹完整绝对路径
	 *
	 *
	 *
	 * @return void
	 */
	public static void delFolder(String strFolderPath) {
		File file = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			try {
				delAllFile(strFolderPath); // 删除完里面所有内容

				file = new File(strFolderPath);
				file.delete(); // 删除空文件夹
			} catch (Exception e) {
				System.out.println("类:FileUtil,方法:delFolder,信息:删除目录有误," + e);
			} finally {
				file = null;
			}
		} else {
			System.out.println("类:FileUtil,方法:delFolder,"
					+ "信息:strFolderPath=null");
		}
		// 释放对象
		strFolderPath = null;
	}

	/**
	 * 删除指定文件夹下所有文件及目录
	 *
	 * @param strFolderPath
	 *            文件夹完整绝对路径
	 *
	 *
	 *
	 * @return boolean
	 */
	public static boolean delAllFile(String strFolderPath) {
		boolean blnResult = false;
		int intFileCount = 0;
		String[] strArrayFile = null;
		File file = new File(strFolderPath);
		if (file.exists() && file.isDirectory()) {
			strArrayFile = file.list();
			if (strArrayFile == null || strArrayFile.length <= 0) {
				blnResult = true;
			} else {
				intFileCount = strArrayFile.length;
				if (intFileCount > 0) {
					for (int i = 0; i < intFileCount; i++) {
						if (strFolderPath.endsWith(File.separator)) {
							file = new File(strFolderPath + strArrayFile[i]);
						} else {
							file = new File(strFolderPath + File.separator
									+ strArrayFile[i]);
						}
						if (file.isFile()) {
							file.delete();
						}
						if (file.isDirectory()) {
							delAllFile(strFolderPath + File.separator
									+ strArrayFile[i]);// 先删除文件夹里面的文件

							delFolder(strFolderPath + File.separator
									+ strArrayFile[i]);// 再删除空文件夹

						}
						blnResult = true;
					}
				} else {
					blnResult = true;
				}
			}
		} else {
			System.out.println("类:FileUtil,方法:delAllFile,信息:删除文件目录有误,"
					+ strFolderPath);
		}
		// 释放对象
		strFolderPath = null;
		strArrayFile = null;
		file = null;
		return blnResult;
	}

	/**
	 * 获得一个目录下面所有文件
	 *
	 *
	 *
	 *
	 * @param strFolderPath
	 *            文件夹完整绝对路径
	 *
	 *
	 *
	 * @return String[]
	 */
	public static String[] getAllFile(String strFolderPath) {
		String[] strArrayResult = null;
		File file = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			file = new File(strFolderPath);
			if (getState(strFolderPath) && file.isDirectory()) {
				strArrayResult = file.list();
			}
			if (strArrayResult == null || strArrayResult.length <= 0
					|| strArrayResult[0].trim().length() <= 0) {
				strArrayResult = null;
			}
		} else {
			System.out.println("类:FileUtil,方法:getAllFile,"
					+ "信息:strFolderPath=null");
		}
		// 释放对象
		file = null;
		strFolderPath = null;
		return strArrayResult;
	}

	/**
	 * 获得指定目录下所有的一级目录
	 *
	 *
	 *
	 *
	 * @param strFolderPath
	 *            指定路径名
	 *
	 *
	 *
	 * @return File[]
	 */
	public static File[] getAllFolder(String strFolderPath) {
		File file = null;
		File[] fileArray = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			file = new File(strFolderPath);
			fileArray = file.listFiles();
			if (fileArray == null || fileArray.length <= 0) {
				fileArray = null;
			}
		} else {
			System.out.println("类:FileUtil,方法:getAllFolder,"
					+ "信息:strFolderPath=null");
		}
		// 释放对象
		file = null;
		strFolderPath = null;
		return fileArray;
	}

	/**
	 * 递归创建文件夹
	 * 
	 * @param index
	 * @param path
	 */
	public static void createFolderForRecursion(int index, String path) {
		int temp = path.indexOf(File.separator, index);
		int length = path.length();
		while (temp > 0 && temp < length) {
			createFolder(path.substring(0, temp));
			temp = path.indexOf(File.separator, temp + 1);
		}
	}

	/**
	 * 解压RAR文件
	 * 
	 * @param destDir
	 *            解压至目标文件夹
	 * @param sourceRar
	 *            解压源文件
	 */

	public static void unrar(String sourceRar, String destDir) throws Exception {
		Archive a = null;
		FileOutputStream fos = null;
		try {
			a = new Archive(new File(sourceRar));
			FileHeader fh = a.nextFileHeader();
			while (fh != null) {
				if (!fh.isDirectory()) {
					// 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
					String compressFileName = fh.getFileNameString().trim();
					String destFileName = "";
					String destDirName = "";
					// 非windows系统
					if (File.separator.equals("/")) {
						destFileName = destDir
								+ compressFileName.replaceAll("\\\\", "/");
						destDirName = destFileName.substring(0,
								destFileName.lastIndexOf("/"));
						// windows系统
					} else {
						destFileName = destDir
								+ compressFileName.replaceAll("/", "\\\\");
						destDirName = destFileName.substring(0,
								destFileName.lastIndexOf("\\"));
					}
					// 2创建文件夹
					File dir = new File(destDirName);
					if (!dir.exists() || !dir.isDirectory()) {
						dir.mkdirs();
					}
					// 3解压缩文件
					fos = new FileOutputStream(new File(destFileName));
					a.extractFile(fh, fos);
					fos.close();
					fos = null;
				}
				fh = a.nextFileHeader();
			}
			a.close();
			a = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (a != null) {
				try {
					a.close();
					a = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 功能:压缩多个文件成一个zip文件
	 * <p>
	 * 作者 陈亚标 Jul 16, 2010 10:59:40 AM
	 * 
	 * @param srcfile
	 *            ：源文件列表
	 * @param zipfile
	 *            ：压缩后的文件
	 */
	public static void zipFiles(List<File> srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		try {
			// ZipOutputStream类：完成文件或文件夹的压缩
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipfile));
			for (int i = 0; i < srcfile.size(); i++) {
				FileInputStream in = new FileInputStream(srcfile.get(i));
				out.putNextEntry(new ZipEntry(srcfile.get(i).getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
			System.out.println("压缩完成.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, Map<String, String>> getFilesByExtension(
			String directory, String extension) throws IOException {
		String[] strs = new String[] { extension };
		Iterator<File> it = FileUtils.iterateFiles(new File(directory), strs,
				false);
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		while (it.hasNext()) {
			File file = it.next();
			List<String> results = FileUtils.readLines(file);
			for (String str : results) {
				System.out.println(str);
				String[] str1 = str.split("\\t");
				Map<String, String> subMap = new HashMap<String, String>();
				subMap.put(str1[5], str1[6]);
				map.put(str1[3], subMap);
			}
		}
		return map;
	}

	public static List<File> PDFchangToImage(File file, String ext_flag)
			throws Exception {
		// 输出文件
		List<File> outFiles = new LinkedList<File>();

		// 加载指定pdf文件
		PDDocument doc = PDDocument.load(file);

		// 获取pdf文件页码
		int pageCount = doc.getNumberOfPages();

		// 输出页码，可以注释掉
		List pages = doc.getDocumentCatalog().getAllPages();

		for (int i = 0; i < pages.size(); i++) {

			PDPage page = (PDPage) pages.get(i);

			// 将页面转换为图片

			BufferedImage image = page.convertToImage();

			Iterator iter = ImageIO.getImageWritersBySuffix("jpg");

			ImageWriter writer = (ImageWriter) iter.next();

			File outFile = null;

			if ("Y".equals(ext_flag)) {
				outFile = new File(file.getAbsolutePath().replaceAll(".pdf",
						"ext.jpg"));
			} else {
				outFile = new File(file.getAbsolutePath().replaceAll(".pdf",
						".jpg"));
			}

			FileOutputStream out = new FileOutputStream(outFile);

			ImageOutputStream outImage = ImageIO.createImageOutputStream(out);

			writer.setOutput(outImage);

			writer.write(new IIOImage(image, null, null));

			outFiles.add(outFile);

		}

		doc.close();

		return outFiles;
	}

	public static List<File> PDFToJpg(File file, String ext_flag)
			throws Exception {
		// 输出文件
		List<File> outFiles = new LinkedList<File>();
		String filePath = file.getAbsolutePath();
		System.out.println("filePath--------------------------------"+filePath);
		Document document = new Document();
		document.setFile(filePath);
		float scale = 2.5f;// 缩放比例
		float rotation = 0f;// 旋转角度
		for (int i = 0; i < document.getNumberOfPages(); i++) {
			BufferedImage image = (BufferedImage) document.getPageImage(i,
					GraphicsRenderingHints.SCREEN,
					org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation,
					scale);
			RenderedImage rendImage = image;
			try {
				File outFileTemp = null;
				if ("Y".equals(ext_flag)) {
					outFileTemp = new File(file.getAbsolutePath().replaceAll(".pdf",
							"ext.jpg"));
				} else {
					outFileTemp = new File(file.getAbsolutePath().replaceAll(".pdf",
							".jpg"));
				}
//				File file1 = new File("D://" + i + ".jpg");
				ImageIO.write(rendImage, "jpg", outFileTemp);
				outFiles.add(outFileTemp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			image.flush();
		}
		document.dispose();
		return outFiles;
	}

	public static String getMd5ByFile(String path) throws IOException {
		FileInputStream fis = new FileInputStream(path);
		String md5 = DigestUtils.md5Hex(org.apache.commons.io.IOUtils
				.toByteArray(fis));
		org.apache.commons.io.IOUtils.closeQuietly(fis);
		return md5;
	}

	// word转pdf
	public static void wordToPDF(String sfileName, String toFileName)
			throws Exception {
		Class.forName("com.jacob.com.Dispatch");
		System.out.println("启动Word...");
		long start = System.currentTimeMillis();
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			// doc = Dispatch.call(docs, "Open" , sourceFile).toDispatch();
			doc = Dispatch.invoke(
					docs,
					"Open",
					Dispatch.Method,
					new Object[] { sfileName, new Variant(false),
							new Variant(true) }, new int[1]).toDispatch();
			System.out.println("打开文档..." + sfileName);
			System.out.println("转换文档到PDF..." + toFileName);
			File tofile = new File(toFileName);
			if (tofile.exists()) {
				tofile.delete();
			}
			// Dispatch.call(doc, "SaveAs", destFile, 17);
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
					toFileName, new Variant(17) }, new int[1]);
			long end = System.currentTimeMillis();
			System.out.println("转换完成..用时：" + (end - start) + "ms.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("========Error:文档转换失败：" + e.getMessage());
		} finally {
			Dispatch.call(doc, "Close", false);
			System.out.println("关闭文档");
			if (app != null)
				app.invoke("Quit", new Variant[] {});
			// 如果没有这句话,winword.exe进程将不会关闭
			ComThread.Release();
		}
	}
}
