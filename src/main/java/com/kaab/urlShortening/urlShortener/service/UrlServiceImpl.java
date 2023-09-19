package com.kaab.urlShortening.urlShortener.service;

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
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private String encodeUrl(String url) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                url.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
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
