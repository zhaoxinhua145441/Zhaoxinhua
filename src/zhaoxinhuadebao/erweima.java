package zhaoxinhuadebao;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;


public class erweima {
	
	public static void main(String[] args) throws Exception  {
			//int width = 127;
			//int height = 127;
			int imageSize =127;
			
			int version = 6;
			
			Qrcode qrcode = new Qrcode();
			qrcode.setQrcodeErrorCorrect('H');
			qrcode.setQrcodeErrorCorrect('B');
			String content = "www.dijiaxueshe.com";
			qrcode.setQrcodeVersion(version);
				
				byte[] data = content.getBytes("utf-8");
				boolean[][] qrdata = qrcode.calQrcode(data);
			
			//用来设置图片缓冲
			BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
			//用来图片绘画
			Graphics2D gs = bufferedImage.createGraphics();
			//用来设置背景色
			gs.setBackground(Color.WHITE);
			
			//用来清除画布
			gs.clearRect(0, 0, imageSize, imageSize);
			int startR=255,startG=0,startB=0;
			int endR=0,endG=0,endB=255;
			//用来二维码绘画
			int pixoff = 2;
			for(int i=0;i<qrdata.length;i++){
				for(int j=0;j<qrdata.length;j++){
					if(qrdata[i][j]){
						
						int num1 =startR+(endR-startR)*(i+1)/qrdata.length;
						int num2 =startG+(endG-startG)*(i+1)/qrdata.length;
						int num3 =startB+(endB-startB)*(i+1)/qrdata.length;
						Color color = new Color(num1,num2,num3); 
						gs.setColor(color);
						gs.fillRect(i*3+pixoff, j*3+pixoff, 3, 3);
					}
				}             
			}
			//BufferedImage logo  = ImageIO.read(new File("D:/logo.jpg"));
			BufferedImage logo = scale("D:/logo2.jpg",40,40,true);
			//int logoSize = imageSize/4;
			//设置logo图片在二维码中心
			//int o = (imageSize-logoSize)/2;
			//gs.drawImage(logo, o, o, logoSize, logoSize, null);
			int o = (imageSize-logo.getHeight())/2;
			gs.drawImage(logo,o,o,40,40,null);
			
			gs.dispose();//关闭绘图
			bufferedImage.flush();
			try{
				ImageIO.write(bufferedImage, "png", new File("D:/qrcode.png"));
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("产生了问题");
			}
			System.out.println("生成二维码！");
		}
		private static BufferedImage scale (String logoPath,int width,int height,boolean hasFiller) throws Exception{
			double ratio = 0.0;
			File file = new File(logoPath);
			BufferedImage srcImage = ImageIO.read(file);
			Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
			
			//计算比例
			if((srcImage.getHeight()>height)||(srcImage.getWidth()>width)){
				if(srcImage.getHeight()>srcImage.getWidth()){
					ratio = (new Integer(height).doubleValue()) /srcImage.getHeight();
				}else{
					ratio = (new Integer(width).doubleValue()) /srcImage.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio),null);
				destImage = op.filter(srcImage, null);
			}
			if(hasFiller){
				BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
				Graphics2D graphic = image.createGraphics();
				graphic.setColor(Color.white);
				graphic.fillRect(0, 0, width, height);
				if(width == destImage.getWidth(null)){
					graphic.drawImage(destImage, 0, (height-destImage.getHeight(null)) /2, 
							destImage.getWidth(null),destImage.getHeight(null),Color.white,null);
				}else{
					graphic.drawImage(destImage, (width-destImage.getWidth(null)) /2, 0,
							destImage.getWidth(null),destImage.getHeight(null),Color.white,null);
				}
				graphic.dispose();
				destImage = image;
			}
			return (BufferedImage) destImage;
			
			}

	}

