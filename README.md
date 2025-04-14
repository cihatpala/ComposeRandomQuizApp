# Compose Quiz App 📱

Modern Android quiz uygulaması, Jetpack Compose ve Open Trivia Database API kullanılarak geliştirilmiştir.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

## Özellikler ✨

- 🎯 Çoktan seçmeli sorular
- 🔄 Yanlış cevaplanan soruları tekrar sorma
- 📊 İlerleme çubuğu
- ⏱️ Otomatik soru geçişi için geri sayım
- 🎨 Material 3 tasarım
- 🌐 Open Trivia Database API entegrasyonu
- ✅ Anlık doğru/yanlış geri bildirimi
- 📱 Modern ve kullanıcı dostu arayüz

## Teknolojiler 🛠️

- **Jetpack Compose**: Modern UI geliştirme
- **Material 3**: Tasarım sistemi
- **Retrofit**: API istekleri
- **Coroutines**: Asenkron işlemler
- **ViewModel**: UI state yönetimi
- **Open Trivia DB**: Soru kaynağı

## Ekran Görüntüleri 📸

[Ekran görüntüleri buraya eklenecek]

## Kurulum 🚀

1. Projeyi klonlayın:
```bash
git clone https://github.com/cihatpala/ComposeRandomQuizApp
```

2. Android Studio'da açın

3. Gradle sync işlemini tamamlayın

4. Uygulamayı çalıştırın

## Mimari 🏗️

Uygulama MVVM (Model-View-ViewModel) mimarisi kullanılarak geliştirilmiştir:

```
app/
├── data/
│   ├── api/
│   │   └── QuizApiService.kt
│   └── model/
│       └── QuizResponse.kt
├── ui/
│   └── QuizScreen.kt
└── viewmodel/
    └── QuizViewModel.kt
```

## Özellikler Detayı 📝

### Quiz Akışı
1. API'den rastgele 10 soru yüklenir
2. Her soru için:
   - Doğru cevap yeşil ile gösterilir
   - Yanlış cevap kırmızı ile gösterilir
   - 3 saniyelik geri sayım sonrası sonraki soruya geçilir
3. Yanlış cevaplanan sorular tekrar sorulur
4. Tüm sorular doğru cevaplanınca quiz tamamlanır

### İlerleme Takibi
- İlerleme çubuğu doğru cevaplanan soru sayısını gösterir
- Kalan soru sayısı sürekli güncellenir
- Quiz sonunda başarı yüzdesi gösterilir

## Katkıda Bulunma 🤝

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'feat: Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## Lisans 📄

Bu proje MIT lisansı altında lisanslanmıştır - detaylar için [LICENSE](LICENSE) dosyasına bakın.

## İletişim 📬

[İsim] - [@twitter_handle](https://twitter.com/twitter_handle) - email@example.com

Proje Linki: [https://github.com/[kullanıcı-adı]/compose-quiz-app](https://github.com/[kullanıcı-adı]/compose-quiz-app) 
