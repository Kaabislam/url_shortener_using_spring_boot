package com.kaab.urlShortening.urlShortener.controller;

import com.kaab.urlShortening.urlShortener.model.Url;
import com.kaab.urlShortening.urlShortener.model.UrlDto;
import com.kaab.urlShortening.urlShortener.model.UrlErrorResponseDto;
import com.kaab.urlShortening.urlShortener.model.UrlResponseDto;
import com.kaab.urlShortening.urlShortener.service.UrlService;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.http.HttpClient;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


@RestController
public class UrlShorteningController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) throws NoSuchAlgorithmException {
        Url urlToRet = urlService.generateShortLink(urlDto);
        if(urlToRet != null){
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setShortLink(urlToRet.getShortLink());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }
        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("An Error !!");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
    }

    @GetMapping("/{shortlink}")
    public RedirectView redirectTooriginalUrl(@PathVariable String shortlink) throws IOException {
        if(shortlink == null || shortlink.length() == 0){
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("invalid Url");
            urlErrorResponseDto.setStatus("504");
//            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        Url urlToRet = urlService.getEncodedUrl(shortlink);
        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now())){
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url expired !!");
            urlErrorResponseDto.setStatus("604");
//            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        RedirectView redirectView = new RedirectView(urlToRet.getOriginalUrl());

        return redirectView;
//        httpServletResponse.sendRedirect();
    }


}
