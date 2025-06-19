package com.workintech.s19d1.exceptions;

import lombok.AllArgsConstructor; // Lombok: Tüm argümanları içeren constructor oluşturur
import lombok.Data;              // Lombok: Getter, Setter, equals, hashCode ve toString metotlarını otomatik oluşturur
import lombok.NoArgsConstructor;   // Lombok: Argümansız constructor oluşturur

import java.time.LocalDateTime; // Tarih ve saat bilgisini tutmak için

// Bu sınıf, API'mizden istemcilere dönecek hata yanıtlarının yapısını tanımlayan bir Data Transfer Object (DTO)'dir.
// Amacı, hata durumlarında tutarlı ve anlaşılır bir format sağlamaktır.
@NoArgsConstructor // Lombok anotasyonu: Parametresiz constructor oluşturur (JSON serileştirme/deserileştirme için genellikle gereklidir)
@AllArgsConstructor // Lombok anotasyonu: Tüm field'ları (message, status, dateTime) parametre alan bir constructor oluşturur.
@Data // Lombok anotasyonu: message, status, dateTime field'ları için getter ve setter metotlarını, ayrıca toString, equals ve hashCode metotlarını otomatik olarak oluşturur.
public class ExceptionResponse {
    private String message; // Hatanın açıklayıcı mesajı (örn: "Film bulunamadı", "Geçersiz giriş")
    private int status;     // HTTP durum kodunun sayısal değeri (örn: 404, 500)
    private LocalDateTime dateTime; // Hatanın meydana geldiği zaman ve tarih bilgisi
}