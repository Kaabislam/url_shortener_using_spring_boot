package com.kaab.urlShortening.urlShortener.service;

import com.kaab.urlShortening.urlShortener.model.Url;
import com.kaab.urlShortening.urlShortener.model.UrlDto;
import org.springframework.util.StringUtils;

public class UrlServiceImpl implements UrlService{
    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if(urlDto.getUrl()!=null && urlDto.getUrl().length()!=0){
            String encodedUrl = "encode " + urlDto.getUrl();
        }
        return null;
    }

    @Override
    public Url persistShortLink(Url url) {

        return null;
    }

    @Override
    public Url getEncodedUrl(String url) {
        return null;
    }

    @Override
    public void deleteShortLink(Url url) {

    }
}
