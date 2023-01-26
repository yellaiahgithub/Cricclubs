package com.cricket.utility;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import com.cricket.dao.NewsFactory;
import com.cricket.dto.NewsDto;
import com.cricket.dto.UserDto;
import com.cricket.dto.lite.UserLiteDto;
import com.cricket.service.pushnotification.AmazonS3StorageService;
import com.google.gson.Gson;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;


public class CCAPIUtils {	
	
	public static String saveImage(InputStream is) throws Exception {
		
		BufferedImage image = ImageIO.read(is);
		
		if (image != null) {
			UUID uid = UUID.randomUUID();
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage newImage = resizeImage(image, (width > 600) ? 600 : width, (width > 600) ? ((height * 600) / width) : height);
			
			String s3Path = "static-assets/documentsRep/ccapi/media/";	
			
			saveToS3("jpg", uid+"", newImage, s3Path, null);
			
			return "/documentsRep/ccapi/media/"+ uid + ".jpg";
		}
		return null;
	}
	
	public static boolean saveVideo(InputStream iStream, long videoLength, String videoExtension, String uid, String s3Path) throws Exception {
		
		return  AmazonS3StorageService.putObjectToS3(s3Path, uid, iStream, null, videoLength, videoExtension);
	}
	
	private static BufferedImage resizeImage(BufferedImage image, int width,
			int height) {
		
		int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
		
		BufferedImage resizedImage = new BufferedImage(width, height, type);		
		Graphics2D g = resizedImage.createGraphics();		
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(image, 0, 0, width, height, null);		
		g.dispose();
		
		return resizedImage;		
	}
	
	public static boolean saveToS3(String imgType, String uid, BufferedImage newImage, String filePath, InputStream is)
			throws IOException {

		InputStream localStream = null;
		if (is != null) {
			localStream = is;
		}else {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(newImage, imgType, os);
			localStream = new ByteArrayInputStream(os.toByteArray());
		}

		return AmazonS3StorageService.putObjectToS3(filePath, uid, localStream, "image", localStream.available(),imgType);
	}
	
	
	public static String getDefualtCreateDateTimeString() {
		LocalDateTime todayDateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
		String formatDateTime = todayDateTime.format(format);
		return formatDateTime;
	}

	@SuppressWarnings("unchecked")
	public static <T> T beanDtoJsonMapper(T bean, Object obj){
		
		Gson json = new Gson();
		String jsonString = json.toJson(obj);
		T t = (T) json.fromJson(jsonString, bean.getClass());		
		return t;
	}
	
	public static <T, K> Map<String, T> mapToMapConverter(Map<String, K> overMap,T bean) {
		Map<String, T> newMap = new TreeMap<String, T>();
		for(String key:overMap.keySet()) {
			newMap.put(key, beanDtoJsonMapper(bean, overMap.get(key)));
		}
		return newMap;
	}
    
	public static <T> Object beanDtoListJsonMapper(T bean, List<?> list) {
		
		List<Object> objList = new ArrayList<Object>();
		
		for(Object o:list) {
			objList.add(beanDtoJsonMapper(bean, o));
		}
		return objList;
	}
	
	public static List<Integer> longToIntIds(List<Long> playerIds) {
		List<Integer> newPlayerIds = new ArrayList<Integer>();
		for(Long playerId : playerIds) {
			newPlayerIds.add(playerId.intValue());			
		}
		return newPlayerIds;
	}
	
	public static String numberToAlphabet(int number) {
		char c = (char) (number + 64);
		return Character.toString(c);
	}
	
	public static String getOutMethodString(String outMethod) {
		
		if (CommonUtility.isNullOrEmpty(outMethod)) {
			return "Not Out";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_BOWLED)) {
			return "Bowled";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_CATCH)) {
			return "Caught";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_CATCH_WK)) {
			return "WktKpr Catch";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_RUN_OUT)) {
			return "Run Out";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_STUMPED)) {
			return "Stumped";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_LBW)) {
			return "LBW";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_RETIRED)) {
			return "Retired Hurt";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_RETIRED_OUT)) {
			return "Retired Out";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_HIT_WICKET)) {
			return "Hit Wicket";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_HIT_BALL_TWICE)) {
			return "Hit Ball Twice";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_HANDLED_BALL)) {
			return "Handled Ball";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_TIMED_OUT)) {
			return "Timed Out";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_OBSTRUCTING_THE_FIELD)) {
			return "Obstructing The Field";
		} else if (outMethod.equals(CommonLogic.OUT_METHOD_MANKADING)) {
			return "Run Out (Mankad Out)";
		}
		return "";
	}
	
	public static String getTimeDifference(String nodeDateTimeStr) {
		
		if(!CommonUtility.isNullOrEmpty(nodeDateTimeStr)) {
			
			nodeDateTimeStr = nodeDateTimeStr.substring(0,nodeDateTimeStr.indexOf("."));
			
			nodeDateTimeStr = nodeDateTimeStr.replaceAll("\"", "");
			
			LocalDateTime toDateTime = LocalDateTime.now();			
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			LocalDateTime nodeDateTime = LocalDateTime.parse(nodeDateTimeStr, format);
	        
	        long years = ChronoUnit.YEARS.between(nodeDateTime, toDateTime);
	        
	        if(years>0) {
	        	return years+"y";
	        }
	        long months = ChronoUnit.MONTHS.between(nodeDateTime, toDateTime);
	        if(months>0) {
	        	return months+"m";
	        }
	        long days = ChronoUnit.DAYS.between(nodeDateTime, toDateTime);	        
	        if(days>0) {	
	        	return days+"d";
	        }
	        long hours = ChronoUnit.HOURS.between(nodeDateTime, toDateTime); 
	        if(hours>0) {
	        	return hours+"h";
	        }
	        long minutes = ChronoUnit.MINUTES.between(nodeDateTime, toDateTime);
	        if(minutes>0) {
	        	return minutes+"mi";
	        }
	        long seconds = ChronoUnit.SECONDS.between(nodeDateTime, toDateTime);
	        if(seconds>0) {
	        	return seconds+"s";
	        } 
			return "";
		}
		return "";				
	}
	
	public static String getPassword(int n) {
		
		char[] pw = new char[n];
		int c = 'A';
		int r1 = 0;
		for (int i = 0; i < n; i++) {
			r1 = (int) (Math.random() * 3);
			switch (r1) {
			case 0:
				c = '0' + (int) (Math.random() * 10);
				break;
			case 1:
				c = 'a' + (int) (Math.random() * 26);
				break;
			case 2:
				c = 'A' + (int) (Math.random() * 26);
				break;
			}
			pw[i] = (char) c;
		}
		return new String(pw);
	}
	
	public static UserLiteDto getUserLiteDto(UserDto user) {
		
		UserLiteDto userLite = new UserLiteDto();
		
		userLite.setUserId(user.getUserID());
		userLite.setPlayerId(user.getPlayerID());
		userLite.setfName(user.getFname());
		userLite.setlName(user.getLname());
		userLite.setEmail(user.getEmail());
		userLite.setCountryCode(user.getCountryCode());
		userLite.setActive(user.getIsActive()==1?true:false);
		
		return userLite;
		
	}
	
	public static String getMatchType(String  seriesType) {
		
		String matchType = "";
		
		if("One Day".equalsIgnoreCase(seriesType)){
			matchType = "ODI";
		}else if("Twenty20".equalsIgnoreCase(seriesType)) {
			matchType = "T20";
		}else if("Test".equalsIgnoreCase(seriesType)) {
			matchType = "Test";
		}else if("2X".equalsIgnoreCase(seriesType)) {
			matchType = "2X";
		}else if("Ten10".equalsIgnoreCase(seriesType)) {
			matchType = "T10";
		}else if("100b".equalsIgnoreCase(seriesType)) {
			matchType = "100b";
		}
		return matchType;
	}
	
	public static Map<String, String> getURLMetaData(String urlStr) {
		
		Map<String, String> metaDataMap = new HashMap<String, String>();
		
		try {
				Document doc = Jsoup.connect(urlStr).get();				
				Elements metaOgTitle = doc.select("meta[property=og:title]");				
				if (metaOgTitle != null) {
					String title = metaOgTitle.attr("content");
					if(CommonUtility.isNullOrEmpty(title)) {
						title = doc.title();
					}
					metaDataMap.put("title", title);
				}				
				Elements metaOgDesc = doc.select("meta[property=og:description]");				
				if (metaOgDesc != null) {
					 metaDataMap.put("desc", metaOgDesc.attr("content"));
				}				
				Elements metaOgType = doc.select("meta[property=og:type]");				
				if (metaOgType != null) {
					 metaDataMap.put("type", metaOgType.attr("content"));
				}
				Elements metaOgImage = doc.select("meta[property=og:image]");				
				if (metaOgImage != null) {
					 metaDataMap.put("image", metaOgImage.attr("content"));
				}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return metaDataMap;
	}
	
	public static String getNewsFromRSSFeed(String urlStr, String emailTo, String envToUse) {
		
		  int newsCount = 0;
		  String title = "";
		
		try {
			 InputStream is = new URL(urlStr).openConnection().getInputStream();
		        InputSource source = new InputSource(is);

		        SyndFeedInput input = new SyndFeedInput();
		        SyndFeed feed = null;
				try {
					feed = input.build(source);
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (FeedException e1) {
					e1.printStackTrace();
				}
		        
		        NewsDto news = new NewsDto();

				for (Iterator iterator = feed.getEntries().iterator(); iterator.hasNext();) {
					SyndEntry syndEntry = (SyndEntry) iterator.next();
					title = syndEntry.getTitle();
					title = title.replace("‘", "'");
					title = title.replace("’", "'");
					title = title.replace("–", "-");
					news.setTitle(title);
					
					if (syndEntry.getDescription() != null) {
						SyndContent sc = syndEntry.getDescription();
						String newsDesc = sc.getValue();
						newsDesc = newsDesc.replace("‘", "'");
						newsDesc = newsDesc.replace("’", "'");
						newsDesc = newsDesc.replace("–", "-");
						news.setNewsDesc(newsDesc);
					}

					if (syndEntry.getContents() != null) {
						for (Iterator<?> it = syndEntry.getContents().iterator(); it.hasNext();) {
							SyndContent syndContent = (SyndContent) it.next();

							if (syndContent != null) {
								String newsContent = syndContent.getValue();
								String image = "";
								Pattern pattern = Pattern.compile("<img.*?src=\"(.*?)\"");
							    Matcher matcher = pattern.matcher(newsContent);
							    while (matcher.find()) {
							       image = matcher.group(1);
							       break;
							    }
							    news.setImage(image);
								//String image = newsContent.substring(newsContent.indexOf("src=")+"src=".length()+1, newsContent.indexOf("alt=")-3);						
								//news.setImage(image);
														
								
								newsContent = newsContent.replaceAll( "<a(.*?)crictracker.com(.*?)>", "" );
								//newsContent = newsContent.replaceAll( "</a>", "" );
							
							/*
							 * newsContent = newsContent.replace("‘", "'"); newsContent =
							 * newsContent.replace("’", "'"); newsContent = newsContent.replace("–", "-");
							 * newsContent = newsContent.replace("­–", "-");
							 */
								news.setNews(newsContent);
							}
						}
					}
					try {
						int newsId = NewsFactory.getNewsIdByTitle(title, 11707);
						if (newsId == 0) {
							news.setUserId(1065833);
							news.setIsPublished(0);
							news.setIsInternational(1);
							int dbNewsId = NewsFactory.insertNews(news, 11707);
							news.setNewsId(dbNewsId);
							NotificationHelper.sendEmailForNewInternationalNews(news, emailTo, envToUse);
							newsCount++;
						}

					} catch (Exception e) {
						e.printStackTrace();
				}
				try {
					NewsFactory.deleteUnPublishedNews(11707,7);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Total News Copied: " + newsCount;
	}
	
}
