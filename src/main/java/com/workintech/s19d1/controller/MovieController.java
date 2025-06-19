package com.workintech.s19d1.controller;

import com.workintech.s19d1.dto.MovieActorRequest; // Movie ve Actor objelerini içeren özel bir DTO (Data Transfer Object) import edildi. Bu sınıfı henüz oluşturmadık.
import com.workintech.s19d1.entity.Actor; // Actor entity sınıfı import edildi.
import com.workintech.s19d1.entity.Movie; // Movie entity sınıfı import edildi.
import com.workintech.s19d1.service.ActorService; // ActorService arayüzü import edildi. Servis katmanından aktör işlemleri için kullanılacak.
import com.workintech.s19d1.service.MovieService; // MovieService arayüzü import edildi. Servis katmanından film işlemleri için kullanılacak.
import org.springframework.beans.factory.annotation.Autowired; // Bağımlılık enjeksiyonu için kullanılan anotasyon.
import org.springframework.web.bind.annotation.*; // RESTful web servisleri için gerekli olan tüm Spring anotasyonları (RestController, RequestMapping, GetMapping vb.) import edildi.

import java.util.List; // Filmlerin listesi için List interface'i import edildi.

@RestController // Bu anotasyon, bu sınıfın bir RESTful web servisi kontrolcüsü olduğunu ve metotların doğrudan HTTP yanıtı (response body) döndüreceğini belirtir. @Controller ve @ResponseBody anotasyonlarının birleşimidir.
// @RequestMapping("/movie"): Bu anotasyon, bu kontrolcüdeki tüm endpoint'lerin '/movie' ile başlayacağını belirtir.
// Not: Proje gereksinimlerine göre (application.properties'deki server.servlet.context-path=/workintech ve endpoint tanımlamaları) bu path'in "/workintech/movies" olması beklenmektedir. Şu anki halinde endpoint'leriniz http://localhost:9000/movie/ gibi olacaktır.
@RequestMapping("/movie")
public class MovieController {

    private final MovieService manager; // MovieService bağımlılığı tanımlanmıştır. 'manager' isimlendirmesi yerine 'movieService' daha yaygın ve açıklayıcıdır.
    private final ActorService actorService; // ActorService bağımlılığı tanımlanmıştır.

    // Constructor Injection: Spring IoC konteyneri, MovieController nesnesini oluştururken
    // MovieService ve ActorService bağımlılıklarını otomatik olarak bu constructor üzerinden enjekte eder.
    // Tek bir constructor olduğu için @Autowired anotasyonu aslında zorunlu değildir, Spring bunu otomatik olarak anlar.
    @Autowired
    public MovieController(MovieService manager, ActorService actorService) {
        this.manager = manager; // Enjekte edilen MovieService nesnesi atanır.
        this.actorService = actorService; // Enjekte edilen ActorService nesnesi atanır.
    }

    @GetMapping // Bu metot GET HTTP isteklerini karşılar. @RequestMapping("/movie") ile birleşerek '/movie' path'ine gelen GET isteklerini işler.
    public List<Movie> getAllMovies() {
        // manager.findAll() metodu çağrılarak tüm filmler servis katmanından alınır.
        // Spring, bu List<Movie> nesnesini otomatik olarak JSON formatına dönüştürüp HTTP yanıt gövdesine yazar.
        return manager.findAll();
    }

    @GetMapping("/{id}") // Bu metot GET HTTP isteklerini karşılar ve URL'den bir 'id' parametresi alır. Örn: /movie/1
    public Movie getMovie(@PathVariable("id") Long id) { // @PathVariable: URL yolundaki '{id}' kısmındaki değeri 'id' parametresine bağlar.
        // manager.findById(id) metodu çağrılarak belirtilen ID'ye sahip film servis katmanından alınır.
        // Eğer film bulunamazsa, MovieServiceImpl'de fırlatılan ApiException, GlobalExceptionHandler tarafından işlenir.
        return manager.findById(id);
    }

    @PostMapping("/") // Bu metot POST HTTP isteklerini karşılar. @RequestMapping("/movie") ile birleşerek '/movie/' path'ine gelen POST isteklerini işler.
    // Not: RESTful API tasarımında, POST endpoint'leri için trailing slash ('/') kullanımı genellikle tercih edilmez. Yani "/movie" daha yaygındır.
    public Movie saveMovieWithActor(@RequestBody MovieActorRequest request) { // @RequestBody: HTTP isteğinin gövdesini (genellikle JSON formatında) MovieActorRequest objesine otomatik olarak dönüştürür.
        Movie movie = request.getMovie(); // İstek gövdesinden gelen Movie nesnesi alınır.
        Actor actor = request.getActor(); // İstek gövdesinden gelen Actor nesnesi alınır.

        // Movie ve Actor arasındaki many-to-many ilişkiyi her iki taraftan da kurarız.
        // Bu, JPA'nın ilişkileri doğru bir şekilde yönetmesi için önemlidir.
        if (movie != null) { // movie nesnesinin null olmadığını kontrol etmek iyi bir pratik olabilir.
            movie.addActor(actor); // Filme aktörü ekler (Movie sınıfındaki addActor metodu kullanılarak)
        }
        if (actor != null) { // actor nesnesinin null olmadığını kontrol etmek iyi bir pratik olabilir.
            actor.addMovie(movie); // Aktöre filmi ekler (Actor sınıfındaki addMovie metodu kullanılarak)
        }


        // Not: İlişkili nesnelerin kaydedilme sırası ve cascade ayarları önemlidir.
        // Actor entity'sindeki @JoinTable anotasyonu ilişkinin sahipliğini belirtir.
        // Movie entity'sindeki @ManyToMany(cascade = CascadeType.ALL) ayarı, Movie kaydedildiğinde ilişkili Actor'ların da kaydedileceğini gösterir.
        // Dolayısıyla burada hem actorService.save(actor) hem de manager.save(movie) çağrıları, eğer entity'lerde doğru cascade ayarları varsa,
        // ya çift kayıt/güncellemeye yol açabilir ya da sadece bir tarafı kaydetmek yeterli olabilir.
        // Genellikle ilişkinin sahibi olan tarafı (Actor) veya CascadeType.ALL olan tarafı (Movie) kaydetmek yeterli olur.
        // Şu anki durumda, movieService.save(movie) çağrısı, Movie'deki CascadeType.ALL sayesinde ilişkili Actor'ı da veritabanına kaydetmelidir.
        actorService.save(actor); // Aktörü kaydeder.
        Movie savedMovie = manager.save(movie); // Filmi kaydeder (Movie'deki cascade ayarları nedeniyle actor da kaydedilmiş olabilir/olacaktır).

        // Kaydedilen filmi HTTP yanıtı olarak döndürür. Spring bunu otomatik olarak JSON'a dönüştürür.
        // Not: Başarılı POST işlemleri için genellikle HttpStatus.CREATED (201) dönmek iyi bir RESTful pratiktir.
        // ResponseEntity kullanarak bunu ayarlayabilirsiniz.
        return savedMovie;
    }
    // EKSİK METOTLAR: PUT ve DELETE endpoint'leri henüz implement edilmemiş.
    // [PUT]/workintech/movies/{id}
    // [DELETE]/workintech/movies/{id}
}