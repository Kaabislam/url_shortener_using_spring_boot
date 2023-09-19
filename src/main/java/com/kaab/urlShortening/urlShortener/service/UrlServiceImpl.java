package com.kaab.urlShortening.urlShortener.service;

import com.google.common.hash.Hashing;
import com.kaab.urlShortening.urlShortener.model.Url;
import com.kaab.urlShortening.urlShortener.model.UrlDto;
import com.kaab.urlShortening.urlShortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Component
public class UrlServiceImpl implements UrlService{
    @Autowired
    private UrlRepository urlRepository;
    @Override
    public Url generateShortLink(UrlDto urlDto) throws NoSuchAlgorithmException {
        if(urlDto.getUrl()!=null && urlDto.getUrl().length()!=0){
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlTopersist = new Url();
            urlTopersist.setShortLink(encodedUrl);
            urlTopersist.setOriginalUrl(urlDto.getUrl());
            urlTopersist.setCreationDate(LocalDateTime.now());
            urlTopersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlTopersist.getCreationDate()));
            Url urlToRet= persistShortLink(urlTopersist);
            if(urlToRet != null)
                return urlToRet;
            return null;
        }
        return null;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {

        return creationDate.plusSeconds(120);
    }

    private String encodeUrl(String url) throws NoSuchAlgorithmException {
        String encodeUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodeUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()),StandardCharsets.UTF_8)
                .toString();
        return encodeUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        Url urlToRet = urlRepository.save(url);
        return urlToRet;
    }

    @Override
    public Url getEncodedUrl(String url) {
        Url urlToRet = urlRepository.findByShortLink(url);
        return urlToRet;
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}
