package com.ssafy.live.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.live.dto.RealEstateNews;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NewsController {
	
	@GetMapping("/news/all")
	public ResponseEntity<?> getNewsList(HttpServletRequest request) throws IOException{
		final String newsList = "https://land.naver.com/news/headline.naver";
		Document document = Jsoup.connect(newsList)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.79 Safari/537.36")
				.header("scheme", "https")
	            .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
	            .header("accept-encoding", "gzip, deflate, br")
	            .header("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,es;q=0.6")
	            .header("cache-control", "no-cache")
	            .header("pragma", "no-cache")
	            .header("upgrade-insecure-requests", "1")
				.get();
		Elements NewsTable = document.select("div.section_headline");
		List<RealEstateNews> list = new ArrayList<>();
		String photo = "";
		String title = "";
		String url  = "";
		for(Element e: NewsTable.select("dt")) {
			
			if(e.className().equals("photo")) {
//				photo = e.childNode(0).childNode(0).attr("src");
				url = e.childNode(0).attr("href");
				Document imgD = Jsoup.connect("http://land.naver.com"+url).get();
				Elements imgDiv = imgD.select("span.end_photo_org");
				photo = imgDiv.get(0).childNode(0).attr("src");
			}
			title = e.text();
			
			if(!photo.equals("") && !title.equals("")) {
				list.add(new RealEstateNews(photo,title,url));
				photo = "";
				title = "";
				url = "";
			}
		}
		
		return new ResponseEntity<List<RealEstateNews>>(list,HttpStatus.OK);
	}

}
