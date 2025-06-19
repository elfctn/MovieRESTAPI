package com.workintech.s19d1.exceptions;

import org.springframework.http.HttpStatus; // HTTP durum kodları için
import org.springframework.http.ResponseEntity; // HTTP yanıtı oluşturmak için
import org.springframework.web.bind.annotation.ControllerAdvice; // Global exception handling için ana anotasyon
import org.springframework.web.bind.annotation.ExceptionHandler; // Belirli exception'ları yakalamak için

import java.time.LocalDateTime; // Hatanın oluştuğu zamanı belirtmek için


@ControllerAdvice // Bu anotasyon, bu sınıfın uygulamadaki tüm @Controller'lar için global bir hata yöneticisi (advice) olduğunu belirtir.
public class GlobalExceptionHandler {

    // Bu metot, projemizde tanımladığımız ApiException türündeki tüm hataları yakalar.
    // ApiException fırlatıldığında, kontrol buraya gelir.
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(ApiException apiException) {
        // ApiException'dan gelen hata mesajı, HTTP durum kodu ve o anki zaman damgası ile
        // istemciye dönecek ExceptionResponse nesnesini oluştururuz.
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                apiException.getMessage(), // ApiException'dan gelen özel hata mesajı
                apiException.getHttpStatus().value(), // ApiException'dan gelen HTTP durum kodunun sayısal değeri
                LocalDateTime.now() // Hatanın oluştuğu anın zaman damgası
        );
        // Oluşturduğumuz ExceptionResponse nesnesini ve ApiException'dan gelen HTTP durum kodunu içeren
        // bir ResponseEntity döndürürüz. Bu, istemcinin hem hata detaylarını hem de doğru HTTP durum kodunu almasını sağlar.
        return new ResponseEntity<>(exceptionResponse, apiException.getHttpStatus());
    }

    // Bu metot, ApiException dışındaki diğer tüm beklenmedik Exception türündeki hataları yakalar.
    // Bu bir 'fallback' (yedek) handler'dır ve uygulamanın genel bir hata durumunda bile çökmesini engeller.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception exception) {
        // Genel bir Exception için, istemciye dönecek hata mesajı, HTTP 500 (INTERNAL_SERVER_ERROR) durumu ve zaman damgası ile
        // bir ExceptionResponse nesnesi oluşturulur.
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getMessage(), // Yakalanan genel hatanın mesajı (üretim ortamında daha genel bir mesaj tercih edilebilir)
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // Beklenmeyen hatalar için HTTP 500 durum kodu
                LocalDateTime.now() // Hatanın oluştuğu anın zaman damgası
        );
        // Oluşturduğumuz ExceptionResponse nesnesini ve HTTP 500 (INTERNAL_SERVER_ERROR) durum kodunu içeren
        // bir ResponseEntity döndürürüz.
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}