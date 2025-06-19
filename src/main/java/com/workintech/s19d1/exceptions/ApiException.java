package com.workintech.s19d1.exceptions;

import lombok.Getter; // Lombok: Getter metotlarını otomatik oluşturur
import org.springframework.http.HttpStatus; // HTTP durum kodları için

// Uygulamaya özgü özel hata sınıfımız
// RuntimeException'dan türediği için unchecked exception'dır, yani try-catch bloğu ile sarmalanması zorunlu değildir.
@Getter // Lombok anotasyonu: Bu sınıfın tüm private field'ları için getter metotlarını otomatik oluşturur.
public class ApiException extends RuntimeException {
    // Bu hata ile birlikte istemciye dönecek HTTP durum kodu
    private HttpStatus httpStatus;

    // Constructor: Hata mesajı ve HTTP durum kodu alır
    public ApiException(String message, HttpStatus httpStatus) {
        super(message); // RuntimeException'ın constructor'ını çağırarak hata mesajını ayarlar
        this.httpStatus = httpStatus; // HTTP durum kodunu ayarlar
    }
}