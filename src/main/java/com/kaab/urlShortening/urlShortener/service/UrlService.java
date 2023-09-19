package com.kaab.urlShortening.urlShortener.service;

import com.kaab.urlShortening.urlShortener.model.Url;
import com.kaab.urlShortening.urlShortener.model.UrlDto;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public interface UrlService {
    public Url generateShortLink(UrlDto urlDto) throws NoSuchAlgorithmException;
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
}
