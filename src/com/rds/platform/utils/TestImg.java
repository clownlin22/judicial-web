package com.rds.platform.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class TestImg {
	// 源文件夹   
    static String url1 = "F:/new/tomcat/webapps/judicial-web/judicial/Z2017091486/";  
    // 目标文件夹   
    static String url2 = "E:/new/123123/";  
    public static void main(String[] args) throws IOException {
    	
    	// 创建目标文件夹   
        (new File(url2)).mkdirs();  
        // 获取源文件夹当前下的文件或目录   
        File[] file = (new File(url1)).listFiles();  
        for (int i = 0; i < file.length; i++) {  
            if (file[i].isFile()) {  
                // 复制文件   
                copyFile(file[i],new File(url2+file[i].getName()));  
            }  
            if (file[i].isDirectory()) {  
                // 复制目录   
                String sourceDir=url1+File.separator+file[i].getName();  
                String targetDir=url2+File.separator+file[i].getName();  
                copyDirectiory(sourceDir, targetDir);  
            }  
        }  
//    	BigDecimal b = new BigDecimal(100-100/1.03);
//    	System.out.println( b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//    	System.out.println("父亲：张东（血痕） ".split("：")[1].split("）")[1]);
//    	WindowMenu win = new WindowMenu("俄罗斯方块",200,30,900,500);
// 	   String[] last12Months = new String[36];  
//       
//       Calendar cal = Calendar.getInstance();  
//       cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1); //要先+1,才能把本月的算进去</span>  
//       for(int i=0; i<36; i++){  
//           cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月  
//           last12Months[35-i] = cal.get(Calendar.YEAR)+ "-" + ((cal.get(Calendar.MONTH)+1)<10?"0"+(cal.get(Calendar.MONTH)+1):(cal.get(Calendar.MONTH)+1));  
//       }  

//		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
//        calendar.add(Calendar.DATE, -1);    //得到前一天
//        String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
//        String test = "8,9,10,11,12,13,14,15,16,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,52,53";
//        double a = -1.0;
//       System.out.println(a!=0);
//       
//    	Calendar cal = Calendar.getInstance();
//    	int year = cal.get(Calendar.YEAR);
//    	int month = cal.get(Calendar.MONTH )+1;
//    	System.out.println("2018-01-01".substring(0, 7));
//    	
//        String filePath = "d:\\Big1Small.jpg";
//        String outPath = "d:\\2.jpg";
//        String str="123,";
//        System.out.println(str.split(",")[0]);
//        drawTextInImgFront(filePath, outPath, new FontText( 7, "#32495B", 49, "微软雅黑"));

//        drawTextInImgBack("D:\\back.jpg", "D:\\back_1.jpg", new FontText(7, "#32495B", 40, "微软雅黑"));
    }
    
 // 复制文件   
    public static void copyFile(File sourceFile,File targetFile)   
    throws IOException{  
            // 新建文件输入流并对它进行缓冲   
            FileInputStream input = new FileInputStream(sourceFile);  
            BufferedInputStream inBuff=new BufferedInputStream(input);  
      
            // 新建文件输出流并对它进行缓冲   
            FileOutputStream output = new FileOutputStream(targetFile);  
            BufferedOutputStream outBuff=new BufferedOutputStream(output);  
              
            // 缓冲数组   
            byte[] b = new byte[1024 * 5];  
            int len;  
            while ((len =inBuff.read(b)) != -1) {  
                outBuff.write(b, 0, len);  
            }  
            // 刷新此缓冲的输出流   
            outBuff.flush();  
              
            //关闭流   
            inBuff.close();  
            outBuff.close();  
            output.close();  
            input.close();  
        } 
    // 复制文件夹   
    public static void copyDirectiory(String sourceDir, String targetDir)  
            throws IOException {  
        // 新建目标目录   
        (new File(targetDir)).mkdirs();  
        // 获取源文件夹当前下的文件或目录   
        File[] file = (new File(sourceDir)).listFiles();  
        for (int i = 0; i < file.length; i++) {  
            if (file[i].isFile()) {  
                // 源文件   
                File sourceFile=file[i];  
                // 目标文件   
               File targetFile=new   
File(new File(targetDir).getAbsolutePath()  
+File.separator+file[i].getName());  
                copyFile(sourceFile,targetFile);  
            }  
            if (file[i].isDirectory()) {  
                // 准备复制的源文件夹   
                String dir1=sourceDir + "/" + file[i].getName();  
                // 准备复制的目标文件夹   
                String dir2=targetDir + "/"+ file[i].getName();  
                copyDirectiory(dir1, dir2);  
            }  
        }  
    }  
    public static void drawTextInImgFront(String filePath,String outPath, FontText text) {
        ImageIcon imgIcon = new ImageIcon(filePath);
        Image img = imgIcon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bimage.createGraphics();
        g.setColor(getColor(text.getWm_text_color()));
        g.setBackground(Color.white);
        g.drawImage(img, 0, 0, null);
        Font font = null;
        font = new Font(text.getWm_text_font(), Font.PLAIN,
                text.getWm_text_size());

        g.setFont(font);
        g.drawString("身份证号：350802201608150048", 480, 605);
		g.drawString("姓名：何", 110, 775);
		System.out.println("呵呵".length());
		g.drawString("何的".substring(0,1), 340, 775);
		g.drawString("性别：女", 480, 775);
		g.drawString(
				"父亲：何晓龙 350802201608150048", 760, 775);
		g.drawString("出生：2017", 110,
				878);
		g.drawString("年", 385, 878);
		g.drawString("11", 447, 878);
		g.drawString("月", 512, 878);
		g.drawString("12", 563, 878);
		g.drawString("日", 623, 878);
		g.drawString("母亲：易文婕 350802201608150048", 760, 878);
        
        
//        g.drawString("身份证号：350802201608150048", 480, 605);
//        g.drawString("姓名：何", 110, 750);
//        g.drawString("宁", 440, 750);
//        g.drawString("性别：女", 480, 775);
//        g.drawString("父亲：何晓龙 350802201608150048", 760, 750);
//        g.drawString("出生："+"2016-01-05".substring(0,4), 940, 750);
//        g.drawString("年", 1210, 750);
//        g.drawString("2016-01-05".substring(5,7), 1270, 750);
//        g.drawString("月", 1330, 750);
//        g.drawString("2016-01-05".substring(8,10), 1390, 750);
//        g.drawString("日", 1450, 750);
//        g.drawString("母亲：易文婕 350802201608150048", 760, 853);
        g.dispose();

        try {
            FileOutputStream out = new FileOutputStream(outPath);
            ImageIO.write(bimage, "PNG", out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void drawTextInImgBack(String filePath,String outPath, FontText text) {
        ImageIcon imgIcon = new ImageIcon(filePath);
        Image img = imgIcon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bimage.createGraphics();
        g.setColor(getColor(text.getWm_text_color()));
        g.setBackground(Color.white);
        g.drawImage(img, 0, 0, null);
        Font font = null;
        font = new Font(text.getWm_text_font(), Font.PLAIN,
                text.getWm_text_size());

        g.setFont(font);
        g.drawString("J201711094", 210, 960);
        font = new Font(text.getWm_text_font(), Font.PLAIN,32);
        g.setFont(font);
        g.drawString("Marker", 820, 60);
        g.drawString("Allele 1", 1100, 60);
        g.drawString("Allele 2", 1330, 60);
        
        for(int i = 0 ; i < 23 ; i ++ )
        {
            g.drawString("CSF1PO", 820, 105 + 42*i);
            g.drawString("22.3", 1127, 105+42*i);
            g.drawString("12.3", 1357, 105+42*i);
        }
//        g.drawString("CSF1PO", 820, 165);
//        g.drawString("D12S391", 820, 215);
//        g.drawString("D13S317", 820, 260);
//        g.drawString("D16S433", 820, 305);
//        g.drawString("D18S51", 820, 350);
//        g.drawString("D19S433", 820, 395);
//        g.drawString("D21S11", 820, 440);
//        g.drawString("D2S1338", 820, 485);
//        g.drawString("D3S1358", 820, 530);
//        g.drawString("D5S818", 820, 575);
//        g.drawString("D6S1043", 820, 640);
//        g.drawString("D7S820", 820, 685);
//        g.drawString("D8S1179", 820, 730);
//        g.drawString("FGA", 820, 775);
//        g.drawString("Penta D", 820, 820);
//        g.drawString("Penta E", 820, 865);
//        g.drawString("TH01", 820, 910);
//        g.drawString("TPOX", 820, 955);
//        g.drawString("vWA", 820, 1000);
//        g.drawString("vWA", 820, 1020);
//        g.drawString("姓名：何欣宁", 110, 775);
//        g.drawString("性别：女", 480, 775);
//        g.drawString("父亲：何晓龙 350802201608150048", 760, 775);
//        g.drawString("出生：2016", 110, 878);
//        g.drawString("年", 385, 878);
//        g.drawString("08", 447, 878);
//        g.drawString("月", 512, 878);
//        g.drawString("15", 563, 878);
//        g.drawString("日", 623, 878);
//        g.drawString("母亲：易文婕 350802201608150048", 760, 878);
        g.dispose();

        try {
            FileOutputStream out = new FileOutputStream(outPath);
            ImageIO.write(bimage, "PNG", out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // color #2395439
    public static Color getColor(String color) {
        if (color.charAt(0) == '#') {
            color = color.substring(1);
        }
        if (color.length() != 6) {
            return null;
        }
        try {
            int r = Integer.parseInt(color.substring(0, 2), 16);
            int g = Integer.parseInt(color.substring(2, 4), 16);
            int b = Integer.parseInt(color.substring(4), 16);
            return new Color(r, g, b);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}