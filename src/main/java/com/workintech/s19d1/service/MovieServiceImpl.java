package com.workintech.s19d1.service;

import com.workintech.s19d1.entity.Movie; // Movie entity sınıfını import ediyoruz
import com.workintech.s19d1.exceptions.ApiException; // Özel hata sınıfımızı import ediyoruz (ileride tanımlanacak)
import com.workintech.s19d1.repository.MovieRepository; // MovieRepository'yi import ediyoruz
import org.springframework.beans.factory.annotation.Autowired; // Bağımlılık enjeksiyonu için
import org.springframework.http.HttpStatus; // HTTP durum kodları için
import org.springframework.stereotype.Service; // Bu sınıfın bir servis bileşeni olduğunu belirtir

import java.util.List; // Liste tipi için import
import java.util.Optional; // findById metodunun Optional döndürmesi için, dolaylı olarak kullanılıyor


@Service // Bu anotasyon, Spring'e bu sınıfın bir servis bileşeni olduğunu ve Spring IoC (Inversion of Control) container'ı tarafından yönetilmesi gerektiğini söyler.
public class MovieServiceImpl implements MovieService { // MovieService arayüzünü implement ediyoruz

    private final MovieRepository movieRepository; // MovieRepository bağımlılığı tanımlanıyor. 'final' olması, atamanın sadece constructor'da yapılacağını garanti eder.

    // Constructor Injection: Spring, MovieServiceImpl nesnesini oluştururken MovieRepository'nin bir örneğini otomatik olarak buraya enjekte edecektir.
    // @Autowired anotasyonu burada zorunlu değildir çünkü tek bir constructor olduğu sürece Spring bunu otomatik olarak algılar.
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository; // Enjekte edilen MovieRepository nesnesini sınıf değişkenine atıyoruz.
    }

    @Override // MovieService arayüzündeki metodun override edildiğini belirtir
    public Movie save(Movie movie) {
        // movieRepository'nin save metodu çağrılarak yeni bir film kaydedilir veya mevcut bir film güncellenir.
        // Spring Data JPA, id varsa update, yoksa insert işlemi yapar.
        return movieRepository.save(movie);
    }

    @Override // MovieService arayüzündeki metodun override edildiğini belirtir
    public void delete(Movie movie) {
        // movieRepository'nin delete metodu çağrılarak belirli bir Movie nesnesi veritabanından silinir.
        // Gelen Movie nesnesinin ID'si üzerinden silme işlemi yapılır.
        movieRepository.delete(movie);
    }

    @Override // MovieService arayüzündeki metodun override edildiğini belirtir
    public Movie findById(long id) {
        // movieRepository'nin findById metodu çağrılarak belirli bir ID'ye sahip film aranır.
        // findById metodu bir Optional<Movie> döndürür.
        // .orElseThrow(): Eğer film bulunamazsa (Optional boşsa), parantez içindeki lambda ifadesi çalışır ve bir ApiException fırlatılır.
        // new ApiException(...): Özel ApiException sınıfımız ile 'Movie is not found' mesajı ve HTTP 404 (NOT_FOUND) durum kodu döner.
        return movieRepository.findById(id).orElseThrow(() ->
                new ApiException("Movie is not found with id: " + id, HttpStatus.NOT_FOUND)
        );
    }

    @Override // MovieService arayüzündeki metodun override edildiğini belirtir
    public List<Movie> findAll() {
        // movieRepository'nin findAll metodu çağrılarak veritabanındaki tüm filmler liste olarak getirilir.
        return movieRepository.findAll();
    }
}