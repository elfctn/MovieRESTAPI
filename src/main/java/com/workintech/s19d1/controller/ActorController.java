package com.workintech.s19d1.controller;

import com.workintech.s19d1.dto.ActorRequest; // Actor oluşturma isteği için DTO
import com.workintech.s19d1.dto.ActorResponse; // Actor yanıtı için DTO
import com.workintech.s19d1.entity.Actor; // Actor entity sınıfı
import com.workintech.s19d1.entity.Movie; // Movie entity sınıfı (Actor ile ilişkili olduğu için)
import com.workintech.s19d1.service.ActorService; // ActorService bağımlılığı
import com.workintech.s19d1.util.Converter; // DTO dönüşümleri için yardımcı sınıf (Henüz oluşturmadık)
import lombok.AllArgsConstructor; // Lombok: Constructor injection için
import lombok.extern.slf4j.Slf4j; // Lombok: Loglama için (Opsiyonel gereksinim)
import org.springframework.validation.annotation.Validated; // Validasyon tetiklemek için (POST/PUT metotlarında kullanılabilir)
import org.springframework.web.bind.annotation.*; // RESTful anotasyonlar için

import java.util.List; // Liste tipi için

@AllArgsConstructor // Lombok anotasyonu: Constructor Injection için ActorService bağımlılığını otomatik olarak enjekte eder.
@RestController // Bu sınıfın bir RESTful kontrolcü olduğunu belirtir.
// @RequestMapping("/actor"): Proje gereksinimlerine göre (server.servlet.context-path=/workintech ve endpoint tanımlamaları)
// bu path'in "/workintech/actors" olması beklenmektedir. Şu anki halinde endpoint'leriniz http://localhost:9000/actor/ gibi olacaktır.
@RequestMapping("/actor")
@Slf4j // Lombok anotasyonu: Loglama için bir logger objesi (log) oluşturur.
public class ActorController {

    private final ActorService actorService; // ActorService bağımlılığı enjekte edildi.

    // GET /actor -> Tüm aktörleri listeler
    @GetMapping
    public List<ActorResponse> findAll() {
        log.info("Fetching all actors"); // Loglama eklendi
        List<Actor> allActors = actorService.findAll(); // Servis katmanından tüm aktörler alınır
        // Converter sınıfı kullanılarak Actor entity listesi, ActorResponse DTO listesine dönüştürülür.
        return Converter.actorResponseConvert(allActors);
    }

    // GET /actor/{id} -> Belirli bir ID'ye sahip aktörü döndürür
    @GetMapping("/{id}")
    public ActorResponse findById(@PathVariable long id) {
        log.info("Fetching actor with id: {}", id); // Loglama eklendi
        Actor actor = actorService.findById(id); // Servis katmanından aktör bulunur
        // Bulunan Actor entity'si, ActorResponse DTO'suna dönüştürülür.
        return Converter.actorResponseConvert(actor);
    }

    // POST /actor -> Yeni bir aktörü ve ilişkili filmleri kaydeder
    // Not: RESTful API tasarımında, POST endpoint'leri için trailing slash ('/') kullanımı genellikle tercih edilmez. Yani "/actor" daha yaygındır.
    @PostMapping
    public ActorResponse save(@Validated @RequestBody ActorRequest actorRequest) { // @Validated: Gelen ActorRequest üzerinde validasyon kurallarını tetikler.
        log.info("Saving new actor with request: {}", actorRequest); // Loglama eklendi

        Actor actor = actorRequest.getActor(); // İstekten aktör nesnesi alınır
        List<Movie> movies = actorRequest.getMovies(); // İstekten filmler listesi alınır

        // Gelen filmler listesindeki her bir filmi, aktörün filmler listesine ekler.
        // Bu, many-to-many ilişkisini kurar.
        if (movies != null) { // Filmler listesinin null olmadığını kontrol etmek önemlidir.
            for (Movie movie : movies) {
                actor.addMovie(movie); // Actor entity'sindeki addMovie metodu kullanılır.
                // Not: Burada Movie entity'sinin de Actor'u kendi listesine eklemesi (movie.addActor(actor)) gerekebilir
                // Eğer Movie tarafında ilişki tam olarak yönetilmiyorsa ve cascade ayarları yeterli değilse.
                // Ancak Actor tarafındaki @JoinTable ve Movie'deki ManyToMany ilişkisi zaten bu durumu yönetecektir.
            }
        }

        // Aktör servis aracılığıyla veritabanına kaydedilir.
        // Actor entity'sinde Movie'ye doğru cascade ayarları (örn: CascadeType.PERSIST veya CascadeType.MERGE) varsa,
        // yeni filmler de bu esnada otomatik olarak kaydedilebilir.
        Actor savedActor = actorService.save(actor);
        // Kaydedilen aktör, ActorResponse DTO'suna dönüştürülür ve döndürülür.
        // Not: Başarılı POST işlemleri için genellikle HttpStatus.CREATED (201) dönmek iyi bir RESTful pratiktir.
        // ResponseEntity kullanarak bu durumu ayarlayabilirsiniz.
        return Converter.actorCreateResponseConvert(savedActor); // Yeni bir Converter metodu kullanılmış.
    }

    // PUT /actor/{id} -> Mevcut bir aktörü günceller
    @PutMapping("/{id}")
    public ActorResponse update(@RequestBody Actor actor, @PathVariable Long id) {
        log.info("Updating actor with id: {}", id); // Loglama eklendi

        // ID ile mevcut aktör bulunur. Bulunamazsa ApiException fırlatılır (Service katmanından).
        Actor foundActor = actorService.findById(id);

        // Not: Gelen 'actor' objesinin ID'si, URL'den gelen 'id' ile aynı olmalıdır veya controller'da 'id' parametresini kullanmalıyız.
        // Burada, bulunan aktörün ID'si ve mevcut filmler listesi, gelen 'actor' objesine atanıyor.
        // Bu, gelen istekte ID ve filmlerin gelmemesi durumunda mevcut bilgilerin korunmasını sağlar.
        actor.setMovies(foundActor.getMovies()); // Mevcut aktörün filmlerini, gelen aktöre set ediyoruz. Bu, mevcut ilişkilerin korunmasını sağlar.
        actor.setId(foundActor.getId()); // URL'den gelen ID'yi veya bulunan aktörün ID'sini gelen aktöre set ediyoruz.

        // Güncellenmiş aktör veritabanına kaydedilir.
        actorService.save(actor);
        // Güncellenen aktör, ActorResponse DTO'suna dönüştürülerek döndürülür.
        // Not: Başarılı PUT işlemleri için genellikle HttpStatus.OK (200) dönmek iyi bir RESTful pratiktir.
        // ResponseEntity kullanarak bu durumu ayarlayabilirsiniz.
        return Converter.actorResponseConvert(actor);
    }

    // DELETE /actor/{id} -> Belirli bir aktörü siler
    @DeleteMapping("/{id}")
    public ActorResponse delete(@PathVariable long id) {
        log.info("Deleting actor with id: {}", id); // Loglama eklendi

        // ID ile silinecek aktör bulunur. Bulunamazsa ApiException fırlatılır.
        Actor foundActor = actorService.findById(id);
        // Bulunan aktör servis katmanı aracılığıyla silinir.
        actorService.delete(foundActor); // ActorService'deki delete metodu Actor objesi aldığı için, burada bulunan objeyi iletiyoruz.
        // Silinen aktör, ActorResponse DTO'suna dönüştürülerek döndürülür.
        // Not: Başarılı DELETE işlemleri için genellikle HttpStatus.OK (200) veya HttpStatus.NO_CONTENT (204) dönmek iyi bir RESTful pratiktir.
        // ResponseEntity kullanarak bu durumu ayarlayabilirsiniz.
        return Converter.actorResponseConvert(foundActor);
    }
}